package com.parking.vehicle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.vehicle.entity.Vehicle;
import java.util.List;

public interface VehicleService {

    IPage<Vehicle> page(Integer current, Integer size, String plateNumber, String vehicleTypeCat, String status);

    List<Vehicle> list(String plateNumber, String vehicleTypeCat);

    Vehicle getById(Long id);

    Vehicle getByPlateNumber(String plateNumber);

    Vehicle save(Vehicle vehicle);

    Vehicle update(Vehicle vehicle);

    boolean delete(Long id);

    boolean enable(Long id);

    boolean disable(Long id);

    boolean isBlacklisted(Long vehicleId);
}
