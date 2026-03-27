package com.parking.space.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.space.entity.ParkingArea;
import com.parking.space.entity.ParkingSpace;
import java.util.List;
import java.util.Map;

public interface ParkingAreaService {

    IPage<ParkingArea> page(Integer current, Integer size, String areaName, String floor);

    List<ParkingArea> list(String areaName, String floor);

    ParkingArea getById(Long id);

    ParkingArea save(ParkingArea area);

    ParkingArea update(ParkingArea area);

    boolean delete(Long id);

    Map<String, Object> getStatistics();
}
