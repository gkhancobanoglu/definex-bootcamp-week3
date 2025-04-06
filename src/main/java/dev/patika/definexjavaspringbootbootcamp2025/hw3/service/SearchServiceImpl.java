package dev.patika.definexjavaspringbootbootcamp2025.hw3.service;

import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.Car;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.InvalidDateRangeException;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.InvalidPriceRangeException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private List<Car> cars;

    @Override
    public List<Car> findAvailableCars(LocalDateTime startDate, LocalDateTime endDate) throws InvalidDateRangeException {

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException();
        }

        return cars.stream()
                .filter(car -> car.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> searchByPriceRange(Double minPrice, Double maxPrice) throws InvalidPriceRangeException {
        if (minPrice == null || maxPrice == null || minPrice < 0 || maxPrice < 0) {
            throw new InvalidPriceRangeException();
        }

        return cars.stream()
                .filter(car -> car.getDailyRate() >= minPrice && car.getDailyRate() <= maxPrice) // Fiyat aralığında olan araçlar
                .collect(Collectors.toList());
    }
}
