package com.example.vibetribesdemo.Controller;

import com.example.vibetribesdemo.DTOs.EventRequestDto;
import com.example.vibetribesdemo.DTOs.EventResponseDto;
import com.example.vibetribesdemo.Service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Create a new event
    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(
            @RequestBody EventRequestDto eventRequestDto,
            @AuthenticationPrincipal(expression = "username") String creatorUsername
    ) {
        EventResponseDto eventResponse = eventService.createEvent(eventRequestDto, creatorUsername);
        return ResponseEntity.ok(eventResponse);
    }

    // Get all events
    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        List<EventResponseDto> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
        EventResponseDto event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable Long id,
            @RequestBody EventRequestDto eventRequestDto,
            Principal principal // Use Principal to get the username
    ) {
        String username = principal.getName(); // Get the username from Principal
        EventResponseDto updatedEvent = eventService.updateEvent(id, eventRequestDto, username);
        return ResponseEntity.ok(updatedEvent);
    }


    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelEvent(
            @PathVariable Long id,
            Principal principal // Use Principal to get the username
    ) {
        String username = principal.getName(); // Get the username from Principal
        eventService.cancelEvent(id, username);
        return ResponseEntity.noContent().build();
    }

    // Search events
    @GetMapping("/search")
    public ResponseEntity<List<EventResponseDto>> searchEvents(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<EventResponseDto> events = eventService.searchEvents(query, locationId, startDate, endDate);
        return ResponseEntity.ok(events);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<String> joinEvent(@PathVariable Long id, Principal principal) {
        String username = principal.getName(); // Get the username from the authenticated user
        eventService.joinEvent(id, username);
        return ResponseEntity.ok("Successfully joined the event.");
    }

    @PostMapping("/{eventId}/leave")
    public ResponseEntity<String> leaveEvent(@PathVariable Long eventId, Principal principal) {
        eventService.leaveEvent(eventId, principal.getName());
        return ResponseEntity.ok("You have successfully left the event.");
    }


    }



