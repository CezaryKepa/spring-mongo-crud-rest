package com.kepa;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/cars")
public class CarController {
    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("")
    public List<Car> findAll(){
        List<Car> cars=carService.getAll();
        if(cars.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Brak samochodow");
        return cars;
    }
    @PostMapping("")
    public ResponseEntity<Car> save(@RequestBody Car car) {
        if (car.get_id() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Zapisywany obiekt nie może mieć ustawionego id");
        Car savedCar = carService.create(car);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCar.get_id())
                .toUri();
        return ResponseEntity.created(location).body(savedCar);
    }

    @GetMapping("{model}")
    public List<Car> getByModel(@PathVariable String model){
        List<Car> cars = carService.getByModel(model);
        if(cars.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Nie znaleziono takich modeli");
        return carService.getByModel(model);
    }

    @DeleteMapping("{carId}/delete")
    public ResponseEntity deleteAsset(@PathVariable ObjectId carId) {
        Car carDeleted = carService.delete(carId);
        return ResponseEntity.ok().body(carDeleted);
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteAsset() {
        carService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Car> update(@RequestBody Car car, @PathVariable ObjectId id) {
        if (!id.equals(car.get_id()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aktualizowany obiekt musi mieć id zgodne z id w ścieżce zasobu");
        Car updatedCar = carService.update(car);
        return ResponseEntity.ok(updatedCar);
    }

}
