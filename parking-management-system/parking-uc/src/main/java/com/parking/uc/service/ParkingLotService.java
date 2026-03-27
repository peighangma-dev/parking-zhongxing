package com.parking.uc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.uc.entity.ParkingLot;

public interface ParkingLotService {

    ParkingLot getById(Long id);

    IPage<ParkingLot> page(Integer current, Integer size, String lotName);

    ParkingLot create(ParkingLot lot);

    boolean update(Long id, ParkingLot lot);

    boolean delete(Long id);
}
