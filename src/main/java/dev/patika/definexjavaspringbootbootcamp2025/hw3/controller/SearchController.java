package dev.patika.definexjavaspringbootbootcamp2025.hw3.controller;

import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.Car;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.InvalidDateRangeException;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.InvalidPriceRangeException;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;


    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<Car>> searchAvailableCars(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {

        try {
            List<Car> availableCars = searchService.findAvailableCars(startDate, endDate);
            return ResponseEntity.ok(availableCars);
        } catch (InvalidDateRangeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/priceRange")
    public ResponseEntity<List<Car>> searchByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        try {
            List<Car> carsInRange = searchService.searchByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(carsInRange);
        } catch (InvalidPriceRangeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
