package com.kepa;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car create(Car car) {
        return carRepository.save(car);
    }

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public List<Car> getByModel(String model) {
        return carRepository.findByModel(model);
    }

    public Car update(Car car) {
        Optional<Car> c = carRepository.findById(car.get_id());
        if(!c.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Nie znaleziono takiego obiketu");
        carRepository.save(car);
        return car;
    }

    public void deleteAll() {
        carRepository.deleteAll();
    }

    public Car delete(ObjectId id) {
        Optional<Car> car = carRepository.findById(id);
        if(!car.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Nie znaleziono takiego obiketu");
        car.ifPresent(value -> carRepository.delete(value));
        return car.get();
    }
}
