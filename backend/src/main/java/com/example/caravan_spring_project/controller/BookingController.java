package com.example.caravan_spring_project.controller;

import com.example.caravan_spring_project.dto.BookingDTO;
import com.example.caravan_spring_project.service.BookingService;
import com.example.caravan_spring_project.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private TemplateService templateService;

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
        BookingDTO saved = bookingService.saveBooking(bookingDTO);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping(value = "/confirm/{token}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> confirmBooking(@PathVariable String token) {
        BookingDTO confirmed = bookingService.confirmBooking(token);
        if (confirmed != null) {
            String html = templateService.renderBookingConfirmed(confirmed);
            return ResponseEntity.ok().body(html);
        } else {
            String html = templateService.renderBookingError("Buchung konnte nicht bestaetigt werden. Token ungueltig oder Buchung bereits bearbeitet.");
            return ResponseEntity.badRequest().body(html);
        }
    }

    @GetMapping(value = "/reject/{token}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> rejectBooking(@PathVariable String token) {
        BookingDTO rejected = bookingService.rejectBooking(token);
        if (rejected != null) {
            String html = templateService.renderBookingRejected(rejected);
            return ResponseEntity.ok().body(html);
        } else {
            String html = templateService.renderBookingError("Buchung konnte nicht abgelehnt werden. Token ungueltig oder Buchung bereits bearbeitet.");
            return ResponseEntity.badRequest().body(html);
        }
    }
}