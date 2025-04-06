package dev.patika.definexjavaspringbootbootcamp2025.hw3.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.controller.CarController;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.Car;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarService carService;

    @Test
    void getAllCars_ShouldReturnOk() throws Exception {

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

        List<Car> carList = List.of(car1, car2);


        when(carService.list()).thenReturn(carList);


        mockMvc.perform(get("/api/car/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(carList.size()))
                .andExpect(jsonPath("$[0].model").value("Model 1"))
                .andExpect(jsonPath("$[1].model").value("Model 2"))
                .andExpect(jsonPath("$[0].brand").value("Brand 1"))
                .andExpect(jsonPath("$[1].brand").value("Brand 2"));
    }



    @Test
    void createCar_ShouldReturnCreated() throws Exception {

        Car car = Car.builder()
                .id(UUID.randomUUID())
                .model("Test Model")
                .brand("Test Brand")
                .year(2023)
                .licensePlate("TEST123")
                .dailyRate(120.0)
                .available(true)
                .build();


        when(carService.create(any(Car.class))).thenReturn(car);


        mockMvc.perform(post("/api/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model").value(car.getModel()))
                .andExpect(jsonPath("$.brand").value(car.getBrand()))
                .andExpect(jsonPath("$.year").value(car.getYear()))
                .andExpect(jsonPath("$.licensePlate").value(car.getLicensePlate()))
                .andExpect(jsonPath("$.dailyRate").value(car.getDailyRate()))
                .andExpect(jsonPath("$.available").value(car.getAvailable()));
    }

    @Test
    void updateCar_ShouldReturnAccepted() throws Exception {

        UUID carId = UUID.randomUUID();

        Car updatedCar = Car.builder()
                .id(carId)
                .model("updated model 1")
                .brand("updated brand: BMW")
                .year(2022)
                .licensePlate("UPDATED123")
                .dailyRate(200.0)
                .available(false)
                .build();

        when(carService.update(eq(carId), any(Car.class))).thenReturn(updatedCar);

        mockMvc.perform(put("/api/car/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carId.toString()))
                .andExpect(jsonPath("$.model").value("updated model 1"))
                .andExpect(jsonPath("$.brand").value("updated brand: BMW"))
                .andExpect(jsonPath("$.year").value(2022))
                .andExpect(jsonPath("$.licensePlate").value("UPDATED123"))
                .andExpect(jsonPath("$.dailyRate").value(200.0))
                .andExpect(jsonPath("$.available").value(false));

    }

    @Test
    void updateCar_ShouldReturnBadRequest() throws Exception {

        String invalidCarId = "invalid-uuid";
        String carJson = "{\"name\": \"Tesla Model X\", \"licensePlate\": \"XYZ123\"}";


        mockMvc.perform(put("/api/car/{id}", invalidCarId)
                        .contentType("application/json")
                        .content(carJson))
                .andExpect(status().isBadRequest());

        verify(carService, times(0)).update(any(UUID.class), any(Car.class));
    }


    @Test
    void getCar_ShouldReturnOk() throws Exception {

        UUID carId = UUID.randomUUID();

        Car car = Car.builder()
                .id(carId)
                .model("Model XXX")
                .brand("Brand YYY")
                .year(2024)
                .licensePlate("XYZ13")
                .dailyRate(300.0)
                .available(true)
                .build();

        when(carService.getById(eq(carId))).thenReturn(car);

        mockMvc.perform(get("/api/car/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carId.toString()))
                .andExpect(jsonPath("$.model").value("Model XXX"))
                .andExpect(jsonPath("$.brand").value("Brand YYY"));

    }

    @Test
    void getCar_ShouldReturnBadRequest() throws Exception {

        String invalidCarId = "invalid-uuid";

        mockMvc.perform(get("/api/car/{id}", invalidCarId))
                .andExpect(status().isBadRequest());

        verify(carService, times(0)).getById(any(UUID.class));
    }

}
