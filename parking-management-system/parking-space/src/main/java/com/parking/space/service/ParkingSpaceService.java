package com.parking.space.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.space.entity.ParkingSpace;
import java.util.List;

public interface ParkingSpaceService {

    IPage<ParkingSpace> page(Integer current, Integer size, Long areaId, String spaceType, String status);

    List<ParkingSpace> list(Long areaId, String spaceType, String status);

    ParkingSpace getById(Long id);

    ParkingSpace save(ParkingSpace space);

    ParkingSpace update(ParkingSpace space);

    boolean delete(Long id);

    boolean occupy(Long id, String plateNumber);

    boolean release(Long id);
}
