package dev.patika.definexjavaspringbootbootcamp2025.hw3.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.controller.BookingController;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.Booking;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.NoSuchBookingException;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.service.BookingService;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;


    @Test
    void getBookings_ShouldReturnOk() throws Exception {

        Booking booking1 = Booking.builder()
                .id(UUID.randomUUID())
                .carId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .status("CONFIRMED")
                .totalPrice(500.0)
                .build();

        Booking booking2 = Booking.builder()
                .id(UUID.randomUUID())
                .carId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .status("CONFIRMED")
                .totalPrice(200.0)
                .build();

        List<Booking> bookings = Arrays.asList(booking1, booking2);

        when(bookingService.list()).thenReturn(bookings);

        mockMvc.perform(get("/api/booking/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"))
                .andExpect(jsonPath("$[1].status").value("CONFIRMED"))
                .andExpect(jsonPath("$[0].totalPrice").value(500.0))
                .andExpect(jsonPath("$[1].totalPrice").value(200.0));
    }


    @Test
    void createBooking_ShouldReturnCreated() throws Exception {
        Booking booking = Booking.builder()
                .id(UUID.randomUUID())
                .carId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .status("CONFIRMED")
                .totalPrice(500.0)
                .build();


        when(bookingService.create(booking)).thenReturn(booking);

        mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isCreated());
    }


    @Test
    void getUserBookings_ShouldReturnOk() throws Exception {
        UUID userId = UUID.randomUUID();

        Booking booking1 = Booking.builder()
                .id(UUID.randomUUID())
                .carId(UUID.randomUUID())
                .userId(userId)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .status("CONFIRMED")
                .totalPrice(250.0)
                .build();

        Booking booking2 = Booking.builder()
                .id(UUID.randomUUID())
                .carId(UUID.randomUUID())
                .userId(userId)
                .startDate(LocalDateTime.now().plusDays(3))
                .endDate(LocalDateTime.now().plusDays(5))
                .status("PENDING")
                .totalPrice(300.0)
                .build();

        List<Booking> bookings = List.of(booking1, booking2);


        when(bookingService.getUserBookings(userId)).thenReturn(bookings);


        mockMvc.perform(get("/api/booking/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK bekliyoruz
                .andExpect(jsonPath("$.size()").value(bookings.size()))
                .andExpect(jsonPath("$[0].userId").value(userId.toString()))
                .andExpect(jsonPath("$[1].userId").value(userId.toString()));
    }


    @Test
    void getUserBookings_ShouldReturnBadRequest() throws Exception {
        String invalidUserId = "invalid-uuid";

        mockMvc.perform(get("/api/booking/user/{userId}", invalidUserId))
                .andExpect(status().isBadRequest());

        verify(bookingService, times(0)).getUserBookings(any(UUID.class));
    }




    @Test
    void cancelBooking_ShouldReturnOk() throws Exception {
        UUID bookingId = UUID.randomUUID();


        doReturn(null).when(bookingService).cancelBooking(bookingId);


        mockMvc.perform(put("/api/booking/{id}/cancel", bookingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void cancelBooking_ShouldReturnBadRequest() throws Exception {

        String invalidBookingId = "invalid-uuid";

        mockMvc.perform(put("/api/booking/{id}/cancel", invalidBookingId))
                .andExpect(status().isBadRequest());

        verify(bookingService, times(0)).cancelBooking(any(UUID.class));
    }

}
