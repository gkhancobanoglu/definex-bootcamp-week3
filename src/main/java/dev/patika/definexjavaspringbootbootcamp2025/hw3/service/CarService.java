package dev.patika.definexjavaspringbootbootcamp2025.hw3.service;

import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.Car;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.NoSuchCarException;

import java.util.List;
import java.util.UUID;

public interface CarService {
    List<Car> list();
    Car getById(UUID carId) throws NoSuchCarException;
    Car create(Car car);
    Car update(UUID carId,Car updatedCar) throws NoSuchCarException;
}
