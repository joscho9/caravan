package com.example.caravan_spring_project.controller;

import com.example.caravan_spring_project.dto.BookingDTO;
import com.example.caravan_spring_project.service.BookingService;
import com.example.caravan_spring_project.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private TemplateService templateService;

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
        logger.info("Received booking creation request for caravan: {}, customer: {}, email: {}", 
                   bookingDTO.getCaravanName(), bookingDTO.getName(), bookingDTO.getEmail());
        logger.debug("Booking details - Start date: {}, End date: {}, Total price: {}, Location: {}", 
                    bookingDTO.getStartDate(), bookingDTO.getEndDate(), bookingDTO.getTotalPrice(), bookingDTO.getLocation());
        
        try {
            BookingDTO saved = bookingService.saveBooking(bookingDTO);
            logger.info("Successfully created booking with ID: {} for customer: {}", saved.getId(), saved.getName());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            logger.error("Failed to create booking for customer: {}, caravan: {}", 
                        bookingDTO.getName(), bookingDTO.getCaravanName(), e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        logger.info("Received request to retrieve all bookings");
        
        try {
            List<BookingDTO> bookings = bookingService.getAllBookings();
            logger.info("Successfully retrieved {} bookings", bookings.size());
            logger.debug("Booking statuses breakdown: {}", 
                        bookings.stream().collect(java.util.stream.Collectors.groupingBy(
                            BookingDTO::getStatus, java.util.stream.Collectors.counting())));
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            logger.error("Failed to retrieve all bookings", e);
            throw e;
        }
    }

    @GetMapping(value = "/confirm/{token}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> confirmBooking(@PathVariable String token, HttpServletRequest request) {
        logger.info("Received booking confirmation request with token: {} from IP: {}", 
                   token, request.getRemoteAddr());
        logger.debug("Request method: {}, User-Agent: {}", request.getMethod(), request.getHeader("User-Agent"));
        
        // HEAD requests should only check if token exists without modifying status
        if ("HEAD".equals(request.getMethod())) {
            logger.debug("Processing HEAD request for token validation");
            boolean tokenExists = bookingService.isValidPendingToken(token);
            logger.info("Token validation result for HEAD request - Token: {}, Valid: {}", token, tokenExists);
            if (tokenExists) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
        
        try {
            BookingDTO confirmed = bookingService.confirmBooking(token);
            if (confirmed != null) {
                logger.info("Successfully confirmed booking with ID: {} for customer: {} using token: {}", 
                           confirmed.getId(), confirmed.getName(), token);
                String html = templateService.renderBookingConfirmed(confirmed);
                return ResponseEntity.ok().body(html);
            } else {
                logger.warn("Failed to confirm booking - invalid or already processed token: {}", token);
                String html = templateService.renderBookingError("Buchung konnte nicht bestaetigt werden. Token ungueltig oder Buchung bereits bearbeitet.");
                return ResponseEntity.badRequest().body(html);
            }
        } catch (Exception e) {
            logger.error("Error occurred while confirming booking with token: {}", token, e);
            String html = templateService.renderBookingError("Ein Fehler ist aufgetreten. Bitte versuchen Sie es spaeter erneut.");
            return ResponseEntity.internalServerError().body(html);
        }
    }

    @GetMapping(value = "/reject/{token}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> rejectBooking(@PathVariable String token, HttpServletRequest request) {
        logger.info("Received booking rejection request with token: {} from IP: {}", 
                   token, request.getRemoteAddr());
        logger.debug("Request method: {}, User-Agent: {}", request.getMethod(), request.getHeader("User-Agent"));
        
        // HEAD requests should only check if token exists without modifying status
        if ("HEAD".equals(request.getMethod())) {
            logger.debug("Processing HEAD request for token validation");
            boolean tokenExists = bookingService.isValidPendingToken(token);
            logger.info("Token validation result for HEAD request - Token: {}, Valid: {}", token, tokenExists);
            if (tokenExists) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
        
        try {
            BookingDTO rejected = bookingService.rejectBooking(token);
            if (rejected != null) {
                logger.info("Successfully rejected booking with ID: {} for customer: {} using token: {}", 
                           rejected.getId(), rejected.getName(), token);
                String html = templateService.renderBookingRejected(rejected);
                return ResponseEntity.ok().body(html);
            } else {
                logger.warn("Failed to reject booking - invalid or already processed token: {}", token);
                String html = templateService.renderBookingError("Buchung konnte nicht abgelehnt werden. Token ungueltig oder Buchung bereits bearbeitet.");
                return ResponseEntity.badRequest().body(html);
            }
        } catch (Exception e) {
            logger.error("Error occurred while rejecting booking with token: {}", token, e);
            String html = templateService.renderBookingError("Ein Fehler ist aufgetreten. Bitte versuchen Sie es spaeter erneut.");
            return ResponseEntity.internalServerError().body(html);
        }
    }
}