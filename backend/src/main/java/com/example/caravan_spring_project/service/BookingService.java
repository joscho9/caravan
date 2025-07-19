package com.example.caravan_spring_project.service;

import com.example.caravan_spring_project.dto.BookingDTO;
import com.example.caravan_spring_project.model.Booking;
import com.example.caravan_spring_project.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private EmailService emailService;

    public BookingDTO saveBooking(BookingDTO dto) {
        Booking booking = new Booking(
                null,
                dto.getName(),
                dto.getEmail(),
                dto.getSubject(),
                dto.getMessage(),
                dto.getCaravanName(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getLocation(),
                dto.getTotalPrice(),
                dto.getPricePerDay()
        );
        Booking saved = bookingRepository.save(booking);
        BookingDTO savedDTO = toDTO(saved);
        emailService.sendBookingNotification(savedDTO);
        return savedDTO;
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private BookingDTO toDTO(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getName(),
                booking.getEmail(),
                booking.getSubject(),
                booking.getMessage(),
                booking.getCaravanName(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getLocation(),
                booking.getTotalPrice(),
                booking.getPricePerDay(),
                booking.getCreatedAt()
        );
    }
} 