package com.vintagecars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vintagecars.model.Car;

public interface CarRepository extends JpaRepository<Car,Long>{

}