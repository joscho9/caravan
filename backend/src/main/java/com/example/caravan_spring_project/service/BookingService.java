package com.example.caravan_spring_project.service;

import com.example.caravan_spring_project.dto.BookingDTO;
import com.example.caravan_spring_project.model.Booking;
import com.example.caravan_spring_project.model.Caravan;
import com.example.caravan_spring_project.repository.BookingRepository;
import com.example.caravan_spring_project.repository.CaravanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CaravanRepository caravanRepository;
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
                dto.getCaravanId(),
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

    public BookingDTO confirmBooking(String token) {
        Optional<Booking> bookingOpt = bookingRepository.findByConfirmationToken(token);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            if ("PENDING".equals(booking.getStatus())) {
                booking.setStatus("CONFIRMED");
                
                // Add unavailable dates to caravan
                if (booking.getCaravanId() != null) {
                    Optional<Caravan> caravanOpt = caravanRepository.findById(booking.getCaravanId());
                    if (caravanOpt.isPresent()) {
                        Caravan caravan = caravanOpt.get();
                        List<LocalDate> unavailableDates = caravan.getUnavailableDates();
                        if (unavailableDates == null) {
                            unavailableDates = new ArrayList<>();
                        }
                        
                        // Add all dates from startDate to endDate
                        LocalDate currentDate = booking.getStartDate();
                        while (!currentDate.isAfter(booking.getEndDate())) {
                            if (!unavailableDates.contains(currentDate)) {
                                unavailableDates.add(currentDate);
                            }
                            currentDate = currentDate.plusDays(1);
                        }
                        
                        caravan.setUnavailableDates(unavailableDates);
                        caravanRepository.save(caravan);
                    }
                }
                
                Booking saved = bookingRepository.save(booking);
                return toDTO(saved);
            }
        }
        return null;
    }

    public BookingDTO rejectBooking(String token) {
        Optional<Booking> bookingOpt = bookingRepository.findByConfirmationToken(token);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            if ("PENDING".equals(booking.getStatus())) {
                booking.setStatus("REJECTED");
                Booking saved = bookingRepository.save(booking);
                return toDTO(saved);
            }
        }
        return null;
    }

    private BookingDTO toDTO(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getName(),
                booking.getEmail(),
                booking.getSubject(),
                booking.getMessage(),
                booking.getCaravanName(),
                booking.getCaravanId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getLocation(),
                booking.getTotalPrice(),
                booking.getPricePerDay(),
                booking.getCreatedAt(),
                booking.getStatus(),
                booking.getConfirmationToken()
        );
    }
} 