package dev.patika.definexjavaspringbootbootcamp2025.hw3.controller;

import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.Booking;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.NoSuchBookingException;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;


    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Booking>> getBookings() {
        List<Booking>bookings = bookingService.list();
        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = bookingService.create(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable UUID userId) {
        try {
            List<Booking> userBookings = bookingService.getUserBookings(userId);
            return ResponseEntity.ok(userBookings);
        } catch (NoSuchBookingException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable UUID id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            bookingService.cancelBooking(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchBookingException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
