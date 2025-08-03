package com.example.caravan_spring_project.service;

import com.example.caravan_spring_project.dto.BookingDTO;
import com.example.caravan_spring_project.model.Booking;
import com.example.caravan_spring_project.model.Caravan;
import com.example.caravan_spring_project.repository.BookingRepository;
import com.example.caravan_spring_project.repository.CaravanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CaravanRepository caravanRepository;
    @Autowired
    private EmailService emailService;

    public BookingDTO saveBooking(BookingDTO dto) {
        logger.info("Starting to save booking for customer: {}, email: {}, caravan: {} (ID: {})", 
                   dto.getName(), dto.getEmail(), dto.getCaravanName(), dto.getCaravanId());
        logger.debug("Booking period: {} to {}, total price: {}, price per day: {}", 
                    dto.getStartDate(), dto.getEndDate(), dto.getTotalPrice(), dto.getPricePerDay());
        
        try {
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
            
            logger.debug("Created booking entity with confirmation token: {}", booking.getConfirmationToken());
            
            Booking saved = bookingRepository.save(booking);
            logger.info("Successfully saved booking to database with ID: {}, status: {}", 
                       saved.getId(), saved.getStatus());
            
            BookingDTO savedDTO = toDTO(saved);
            
            logger.info("Sending booking notification email to: {}", dto.getEmail());
            emailService.sendBookingNotification(savedDTO);
            logger.info("Booking notification email sent successfully for booking ID: {}", saved.getId());
            
            return savedDTO;
        } catch (Exception e) {
            logger.error("Failed to save booking for customer: {}, caravan: {}", 
                        dto.getName(), dto.getCaravanName(), e);
            throw e;
        }
    }

    public List<BookingDTO> getAllBookings() {
        logger.info("Retrieving all bookings from database");
        
        try {
            List<Booking> bookings = bookingRepository.findAll();
            logger.info("Found {} bookings in database", bookings.size());
            
            List<BookingDTO> bookingDTOs = bookings.stream().map(this::toDTO).collect(Collectors.toList());
            
            // Log status distribution
            java.util.Map<String, Long> statusCounts = bookingDTOs.stream()
                .collect(Collectors.groupingBy(BookingDTO::getStatus, Collectors.counting()));
            logger.debug("Booking status distribution: {}", statusCounts);
            
            return bookingDTOs;
        } catch (Exception e) {
            logger.error("Failed to retrieve all bookings", e);
            throw e;
        }
    }

    public BookingDTO confirmBooking(String token) {
        logger.info("Attempting to confirm booking with token: {}", token);
        
        try {
            Optional<Booking> bookingOpt = bookingRepository.findByConfirmationToken(token);
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                logger.info("Found booking with ID: {} for customer: {}, current status: {}", 
                           booking.getId(), booking.getName(), booking.getStatus());
                
                if ("PENDING".equals(booking.getStatus())) {
                    logger.info("Confirming booking ID: {} for caravan: {} from {} to {}", 
                               booking.getId(), booking.getCaravanName(), booking.getStartDate(), booking.getEndDate());
                    
                    booking.setStatus("CONFIRMED");
                    
                    // Add unavailable dates to caravan
                    if (booking.getCaravanId() != null) {
                        logger.debug("Adding unavailable dates to caravan ID: {}", booking.getCaravanId());
                        Optional<Caravan> caravanOpt = caravanRepository.findById(booking.getCaravanId());
                        if (caravanOpt.isPresent()) {
                            Caravan caravan = caravanOpt.get();
                            List<LocalDate> unavailableDates = caravan.getUnavailableDates();
                            if (unavailableDates == null) {
                                unavailableDates = new ArrayList<>();
                            }
                            
                            int initialDateCount = unavailableDates.size();
                            
                            // Add all dates from startDate to endDate
                            LocalDate currentDate = booking.getStartDate();
                            while (!currentDate.isAfter(booking.getEndDate())) {
                                if (!unavailableDates.contains(currentDate)) {
                                    unavailableDates.add(currentDate);
                                    logger.debug("Added unavailable date: {} for caravan ID: {}", currentDate, booking.getCaravanId());
                                }
                                currentDate = currentDate.plusDays(1);
                            }
                            
                            caravan.setUnavailableDates(unavailableDates);
                            caravanRepository.save(caravan);
                            logger.info("Updated caravan ID: {} with {} new unavailable dates (total: {})", 
                                       booking.getCaravanId(), unavailableDates.size() - initialDateCount, unavailableDates.size());
                        } else {
                            logger.warn("Caravan with ID: {} not found while confirming booking", booking.getCaravanId());
                        }
                    }
                    
                    Booking saved = bookingRepository.save(booking);
                    logger.info("Successfully confirmed booking ID: {} for customer: {}", saved.getId(), saved.getName());
                    return toDTO(saved);
                } else {
                    logger.warn("Cannot confirm booking ID: {} - current status is: {}", booking.getId(), booking.getStatus());
                }
            } else {
                logger.warn("No booking found with confirmation token: {}", token);
            }
        } catch (Exception e) {
            logger.error("Error occurred while confirming booking with token: {}", token, e);
            throw e;
        }
        return null;
    }

    public BookingDTO rejectBooking(String token) {
        logger.info("Attempting to reject booking with token: {}", token);
        
        try {
            Optional<Booking> bookingOpt = bookingRepository.findByConfirmationToken(token);
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                logger.info("Found booking with ID: {} for customer: {}, current status: {}", 
                           booking.getId(), booking.getName(), booking.getStatus());
                
                if ("PENDING".equals(booking.getStatus())) {
                    logger.info("Rejecting booking ID: {} for caravan: {} from {} to {}", 
                               booking.getId(), booking.getCaravanName(), booking.getStartDate(), booking.getEndDate());
                    
                    booking.setStatus("REJECTED");
                    Booking saved = bookingRepository.save(booking);
                    logger.info("Successfully rejected booking ID: {} for customer: {}", saved.getId(), saved.getName());
                    return toDTO(saved);
                } else {
                    logger.warn("Cannot reject booking ID: {} - current status is: {}", booking.getId(), booking.getStatus());
                }
            } else {
                logger.warn("No booking found with confirmation token: {}", token);
            }
        } catch (Exception e) {
            logger.error("Error occurred while rejecting booking with token: {}", token, e);
            throw e;
        }
        return null;
    }

    public boolean isValidPendingToken(String token) {
        logger.debug("Validating pending token: {}", token);
        
        try {
            Optional<Booking> bookingOpt = bookingRepository.findByConfirmationToken(token);
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                boolean isValid = "PENDING".equals(booking.getStatus());
                logger.debug("Token validation result - Booking ID: {}, Status: {}, Valid: {}", 
                           booking.getId(), booking.getStatus(), isValid);
                return isValid;
            } else {
                logger.debug("No booking found for token: {}", token);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error occurred while validating token: {}", token, e);
            return false;
        }
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