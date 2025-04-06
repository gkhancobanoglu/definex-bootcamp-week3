package dev.patika.definexjavaspringbootbootcamp2025.hw3.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.controller.SearchController;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.Car;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.InvalidDateRangeException;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.InvalidPriceRangeException;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.service.SearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
public class SearchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void searchAvailableCars_ShouldReturnOk() throws Exception {

        LocalDateTime startDate = LocalDateTime.of(2025, 3, 1, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 3, 5, 10, 0);

        Car car1 = Car.builder()
                .id(UUID.randomUUID())
                .model("Model 1")
                .brand("Brand 1")
                .year(2020)
                .licensePlate("ABC123")
                .dailyRate(100.0)
                .available(true)
                .build();

        Car car2 = Car.builder()
                .id(UUID.randomUUID())
                .model("Model 2")
                .brand("Brand 2")
                .year(2021)
                .licensePlate("XYZ456")
                .dailyRate(150.0)
                .available(true)
                .build();

        List<Car> availableCars = List.of(car1, car2);


        when(searchService.findAvailableCars(startDate, endDate)).thenReturn(availableCars);

        mockMvc.perform(get("/api/search/available")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(availableCars.size()))
                .andExpect(jsonPath("$[0].model").value("Model 1"))
                .andExpect(jsonPath("$[1].model").value("Model 2"));
    }


    @Test
    void searchAvailableCars_ShouldReturnBadRequest() throws Exception {
        when(searchService.findAvailableCars(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenThrow(new InvalidDateRangeException());

        mockMvc.perform(get("/api/search/available")
                        .param("startDate", "2025-02-20T10:00:00")
                        .param("endDate", "2025-02-19T10:00:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(searchService, times(1)).findAvailableCars(any(LocalDateTime.class), any(LocalDateTime.class));
    }


    @Test
    void searchByPriceRange_ShouldReturnOk() throws Exception {

        Car car = Car.builder()
                .model("Model S")
                .brand("Tesla")
                .dailyRate(300.0)
                .build();


        when(searchService.searchByPriceRange(100.0, 500.0))
                .thenReturn(Collections.singletonList(car));

        mockMvc.perform(get("/api/search/priceRange")
                        .param("minPrice", "100.0")
                        .param("maxPrice", "500.0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].model").value("Model S"))
                .andExpect(jsonPath("$[0].brand").value("Tesla"))
                .andExpect(jsonPath("$[0].dailyRate").value(300.0));

        verify(searchService, times(1)).searchByPriceRange(100.0, 500.0);
    }

    @Test
    void searchByPriceRange_ShouldReturnBadRequest() throws Exception {
        when(searchService.searchByPriceRange(anyDouble(), anyDouble()))
                .thenThrow(new InvalidPriceRangeException());

        mockMvc.perform(get("/api/search/priceRange")
                        .param("minPrice", "500.0")
                        .param("maxPrice", "100.0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(searchService, times(1)).searchByPriceRange(anyDouble(), anyDouble());
    }

}
