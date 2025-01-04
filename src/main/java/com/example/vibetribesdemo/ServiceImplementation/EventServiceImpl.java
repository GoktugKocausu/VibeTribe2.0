package com.example.vibetribesdemo.ServiceImplementation;

import com.example.vibetribesdemo.DTOs.EventRequestDto;
import com.example.vibetribesdemo.DTOs.EventResponseDto;
import com.example.vibetribesdemo.Repository.AttendanceRepository;
import com.example.vibetribesdemo.Repository.EventRepository;
import com.example.vibetribesdemo.Repository.UserRepository;
import com.example.vibetribesdemo.Repository.LocationRepository;
import com.example.vibetribesdemo.entities.AttandanceEntity;
import com.example.vibetribesdemo.entities.EventEntity;
import com.example.vibetribesdemo.entities.LocationEntity;
import com.example.vibetribesdemo.entities.UserEntity;
import com.example.vibetribesdemo.Service.EventService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;






@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final AttendanceRepository attendanceRepository;

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, LocationRepository locationRepository, AttendanceRepository attendanceRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public EventResponseDto createEvent(EventRequestDto eventRequestDto, String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocationEntity location = locationRepository.findById(eventRequestDto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        // Check for overlapping events
        boolean isOverlapping = eventRepository.existsByOverlappingTimeAndLocation(
                location,
                eventRequestDto.getStartTime(),
                eventRequestDto.getEndTime()
        );

        if (isOverlapping) {
            throw new IllegalArgumentException("Another event at this location overlaps with the selected time range.");
        }
        if (eventRequestDto.getStartTime().isAfter(eventRequestDto.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        EventEntity event = new EventEntity();
        event.setTitle(eventRequestDto.getTitle());
        event.setDescription(eventRequestDto.getDescription());
        event.setLocation(location);
        event.setStartTime(eventRequestDto.getStartTime());
        event.setEndTime(eventRequestDto.getEndTime());
        event.setMaxAttendees(eventRequestDto.getMaxAttendees());
        event.setStatus("ACTIVE");
        event.setCreatedBy(user);

        EventEntity savedEvent = eventRepository.save(event);

        return mapToResponseDto(savedEvent);
    }

    @Override
    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponseDto getEventById(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return mapToResponseDto(event);
    }

    @Override
    public EventResponseDto updateEvent(Long eventId, EventRequestDto eventRequestDto, String username) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getCreatedBy().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to update this event.");
        }

        // Status validation
        if ("CANCELED".equals(event.getStatus())) {
            throw new RuntimeException("Cannot update a canceled event.");
        }

        // Update event details
        event.setTitle(eventRequestDto.getTitle());
        event.setDescription(eventRequestDto.getDescription());
        event.setStartTime(eventRequestDto.getStartTime());
        event.setEndTime(eventRequestDto.getEndTime());
        event.setMaxAttendees(eventRequestDto.getMaxAttendees());


        // Update location
        if (eventRequestDto.getLocationId() != null) {
            LocationEntity location = locationRepository.findById(eventRequestDto.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            event.setLocation(location); // Update the location entity
        }

        EventEntity updatedEvent = eventRepository.save(event);

        return mapToResponseDto(updatedEvent);
    }

    @Override
    public void cancelEvent(Long eventId, String username) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getCreatedBy().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to cancel this event.");
        }

        event.setStatus("CANCELED");
        eventRepository.save(event);
    }

    @Override
    public List<EventResponseDto> searchEvents(String query, Long locationId, LocalDateTime startDate, LocalDateTime endDate) {
        return eventRepository.searchEventsWithFilters(query, locationId, startDate, endDate).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }



    @Override
    public void joinEvent(Long eventId, String username) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Check if the event is active
        if (!"ACTIVE".equals(event.getStatus())) {
            throw new IllegalArgumentException("Cannot join a canceled or inactive event.");
        }

        // Check if the maximum number of attendees has been reached
        if (event.getCurrentAttendees() >= event.getMaxAttendees()) {
            throw new IllegalArgumentException("Event is already full.");
        }

        // Fetch the user
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Prevent duplicate attendance
        boolean alreadyJoined = attendanceRepository.existsByEventAndUser(event, user);
        if (alreadyJoined) {
            throw new IllegalArgumentException("User has already joined this event.");
        }

        // Create a new attendance record
        AttandanceEntity attendance = new AttandanceEntity();
        attendance.setEvent(event);
        attendance.setUser(user);
        attendance.setStatus("RSVP’d");

        attendanceRepository.save(attendance);

        // Increment current attendees count
        event.setCurrentAttendees(event.getCurrentAttendees() + 1);
        eventRepository.save(event);
    }


    private EventResponseDto mapToResponseDto(EventEntity event) {
        EventResponseDto response = new EventResponseDto();
        response.setEventId(event.getEventId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setCreatedBy(event.getCreatedBy().getUsername());
        response.setLocationName(event.getLocation().getName()); // Fetch from LocationEntity
        response.setLocationId(event.getLocation().getLocationId());
        response.setStartTime(event.getStartTime());
        response.setEndTime(event.getEndTime());
        response.setMaxAttendees(event.getMaxAttendees());
        response.setCurrentAttendees(event.getCurrentAttendees());
        response.setStatus(event.getStatus());
        return response;
    }

    @Override
    public void cancelEventByAdmin(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if ("CANCELED".equals(event.getStatus())) {
            throw new IllegalArgumentException("Event is already canceled.");
        }

        event.setStatus("CANCELED");
        eventRepository.save(event);
    }

    @Transactional
    public void leaveEvent(Long eventId, String username) {
        // Fetch user by username
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Log user ID
        System.out.println("User ID: " + user.getUserId());

        // Fetch event by ID
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Log event ID
        System.out.println("Event ID: " + event.getEventId());

        // Check if the event is canceled
        if ("CANCELED".equalsIgnoreCase(event.getStatus())) {
            throw new IllegalArgumentException("Cannot leave a canceled event.");
        }

        // Check attendance
        Optional<AttandanceEntity> attendance = attendanceRepository.findByEventAndUser(eventId, user.getUserId());
        System.out.println("Attendance found: " + attendance.isPresent());
        if (attendance.isEmpty()) {
            throw new IllegalArgumentException("User is not a participant in this event.");
        }

        // Delete attendance record
        attendanceRepository.deleteAttendanceByEventAndUser(event.getEventId(), user.getUserId());

        // Decrease attendee count
        event.setCurrentAttendees(event.getCurrentAttendees() - 1);
        eventRepository.save(event);
    }


}













