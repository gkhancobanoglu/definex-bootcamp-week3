package dev.patika.definexjavaspringbootbootcamp2025.hw3.service;

import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.Booking;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.NoSuchBookingException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final Map<UUID, Booking> bookings = new HashMap<>();

    @Override
    public List<Booking> list() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public Booking create(Booking booking) {

        if (booking.getId() == null) {
            booking.setId(UUID.randomUUID());
        }
        bookings.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public List<Booking> getUserBookings(UUID userId) throws NoSuchBookingException {
        List<Booking> userBookings = bookings.values().stream()
                .filter(booking -> booking.getUserId().equals(userId))
                .collect(Collectors.toList());

        if (userBookings.isEmpty()) {
            throw new NoSuchBookingException();
        }

        return userBookings;
    }

    @Override
    public Booking cancelBooking(UUID bookingId) throws NoSuchBookingException {
        Booking booking = Optional.ofNullable(bookings.get(bookingId))
                .orElseThrow(NoSuchBookingException::new);

        booking.setStatus("CANCELLED");
        return booking;
    }
}
