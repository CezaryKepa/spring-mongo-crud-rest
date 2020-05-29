package com.kepa;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface CarRepository extends MongoRepository<Car, ObjectId> {

    List<Car> findByModel(String model);
}
