package com.example.vibetribesdemo.ServiceImplementation;

import com.example.vibetribesdemo.DTOs.EventRequestDto;
import com.example.vibetribesdemo.DTOs.EventResponseDto;
import com.example.vibetribesdemo.Repository.AttendanceRepository;
import com.example.vibetribesdemo.Repository.EventRepository;
import com.example.vibetribesdemo.Repository.UserRepository;
import com.example.vibetribesdemo.Repository.LocationRepository;
import com.example.vibetribesdemo.Service.BadgeService;
import com.example.vibetribesdemo.Service.NominatimService;
import com.example.vibetribesdemo.entities.AttandanceEntity;
import com.example.vibetribesdemo.entities.EventEntity;
import com.example.vibetribesdemo.entities.LocationEntity;
import com.example.vibetribesdemo.entities.UserEntity;
import com.example.vibetribesdemo.Service.EventService;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EntityManager entityManager;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final AttendanceRepository attendanceRepository;
    private final BadgeService badgeService;
    private final NominatimService nominatimService;

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, LocationRepository locationRepository, AttendanceRepository attendanceRepository, BadgeService badgeService, NominatimService nominatimService ) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.attendanceRepository = attendanceRepository;
        this.badgeService = badgeService;
        this.nominatimService = nominatimService;
    }



    @Override
    public EventResponseDto createEvent(EventRequestDto eventRequestDto, String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Geocode the address
        Map<String, Object> geocodeResult = nominatimService.geocode(eventRequestDto.getAddress());
        double latitude = Double.parseDouble((String) geocodeResult.get("lat"));
        double longitude = Double.parseDouble((String) geocodeResult.get("lon"));
        String name = (String) geocodeResult.getOrDefault("name", eventRequestDto.getAddress());
        String type = (String) geocodeResult.getOrDefault("type", "unknown"); // Extract type or fallback to "unknown"

        Optional<LocationEntity> existingLocation = locationRepository.findByLatitudeAndLongitude(latitude, longitude);
        LocationEntity location = existingLocation.orElseGet(() -> {
            LocationEntity newLocation = new LocationEntity();
            newLocation.setName(name);
            newLocation.setAddress(eventRequestDto.getAddress());
            newLocation.setLatitude(latitude);
            newLocation.setLongitude(longitude);
            newLocation.setType(type); // Set the type of the location
            return locationRepository.save(newLocation);
        });

        // Create and save the event
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

        badgeService.awardEventBadges(user);

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

        // Update location if address is changed
        if (eventRequestDto.getAddress() != null && !eventRequestDto.getAddress().equals(event.getLocation().getAddress())) {
            Map<String, Object> geocodeResult = nominatimService.geocode(eventRequestDto.getAddress());
            double latitude = Double.parseDouble((String) geocodeResult.get("lat"));
            double longitude = Double.parseDouble((String) geocodeResult.get("lon"));

            Optional<LocationEntity> existingLocation = locationRepository.findByLatitudeAndLongitude(latitude, longitude);
            LocationEntity location = existingLocation.orElseGet(() -> {
                LocationEntity newLocation = new LocationEntity();
                newLocation.setAddress(eventRequestDto.getAddress());
                newLocation.setLatitude(latitude);
                newLocation.setLongitude(longitude);
                return locationRepository.save(newLocation);
            });

            event.setLocation(location);
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
    public List<EventResponseDto> searchEvents(
            String query,
            String address,  // Match the updated interface
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        // Example logic for searching events by address
        List<EventEntity> events = eventRepository.searchEventsWithFilters(
                query,
                address,  // Use the address parameter for filtering
                startDate,
                endDate
        );

        return events.stream()
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

        // Check if the user already has an active attendance record
        Optional<AttandanceEntity> attendance = attendanceRepository.findByEvent_EventIdAndUser_UserId(event.getEventId(), user.getUserId());
        if (attendance.isPresent()) {
            throw new IllegalArgumentException("User has already joined this event.");
        }

        // Create a new attendance record
        AttandanceEntity newAttendance = new AttandanceEntity();
        newAttendance.setEvent(event);
        newAttendance.setUser(user);
        newAttendance.setStatus("RSVP’d");

        attendanceRepository.save(newAttendance);

        // Increment current attendees count
        event.setCurrentAttendees(event.getCurrentAttendees() + 1);
        eventRepository.save(event);

        System.out.println("User successfully joined the event.");
    }


    private EventResponseDto mapToResponseDto(EventEntity event) {
        EventResponseDto response = new EventResponseDto();
        response.setEventId(event.getEventId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setCreatedBy(event.getCreatedBy().getUsername());
        response.setLocationName(event.getLocation().getAddress());
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
    @Override
    public void leaveEvent(Long eventId, String username) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<AttandanceEntity> attendance = attendanceRepository.findByEvent_EventIdAndUser_UserId(event.getEventId(), user.getUserId());

        if (attendance.isPresent()) {
            AttandanceEntity attendanceEntity = attendance.get();
            attendanceRepository.delete(attendanceEntity);
            entityManager.flush();  // Ensures that the database state is updated immediately
            entityManager.refresh(attendanceEntity);  // Refreshes the entity from the database to avoid session cache issues
        } else {
            throw new RuntimeException("User is not part of this event or has already left.");
        }

        event.setCurrentAttendees(event.getCurrentAttendees() - 1);
        eventRepository.save(event);
    }








}













