package dev.patika.definexjavaspringbootbootcamp2025.hw3.controller;

import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.Car;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.NoSuchCarException;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/car")
public class CarController {

    private final CarService carService;


    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.list();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable UUID id) {
        try {
            Car car = carService.getById(id);
            return ResponseEntity.ok(car);
        } catch (NoSuchCarException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car createdCar = carService.create(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable UUID id, @RequestBody Car car) {

        try{
            Car updatedCar = carService.update(id, car);
            return ResponseEntity.ok(updatedCar);
        } catch (NoSuchCarException e) {
            return ResponseEntity.notFound().build();

        }
    }
}
