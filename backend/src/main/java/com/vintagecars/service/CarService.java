package com.vintagecars.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.vintagecars.model.Car;
import com.vintagecars.repository.CarRepository;

@Service
public class CarService {

@Autowired
private CarRepository repo;

public List<Car> getAllCars(){

return repo.findAll();

}

}