package dev.patika.definexjavaspringbootbootcamp2025.hw3.service;

import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.Car;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.InvalidDateRangeException;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.InvalidPriceRangeException;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchService {
    List<Car> findAvailableCars(LocalDateTime startDate, LocalDateTime endDate) throws InvalidDateRangeException;
    List<Car> searchByPriceRange(Double minPrice, Double maxPrice) throws InvalidPriceRangeException;
}
