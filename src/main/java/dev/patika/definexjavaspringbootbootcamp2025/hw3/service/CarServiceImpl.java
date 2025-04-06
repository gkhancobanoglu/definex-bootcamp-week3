package dev.patika.definexjavaspringbootbootcamp2025.hw3.service;

import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.Car;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.NoSuchCarException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CarServiceImpl implements CarService{

    private final Map<UUID,Car> cars = new HashMap<>();

    @Override
    public List<Car> list() {
        return new ArrayList<>(cars.values());
    }

    @Override
    public Car getById(UUID carId) throws NoSuchCarException {
        return Optional.ofNullable(cars.get(carId))
                .orElseThrow(NoSuchCarException::new);
    }

    @Override
    public Car create(Car car) {
        UUID carId = UUID.randomUUID();
        car.setId(carId);
        cars.put(carId, car);
        return car;
    }

    @Override
    public Car update(UUID carId, Car updatedCar) throws NoSuchCarException {
        Car existingCar = getById(carId);
        updatedCar.setId(carId);
        cars.put(carId, updatedCar);
        return updatedCar;
    }
}
