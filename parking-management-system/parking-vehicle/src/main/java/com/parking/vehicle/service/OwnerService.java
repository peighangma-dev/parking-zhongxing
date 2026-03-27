package com.parking.vehicle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.vehicle.entity.Owner;
import java.util.List;

public interface OwnerService {

    IPage<Owner> page(Integer current, Integer size, String name, String phone);

    List<Owner> list(String name, String phone);

    Owner getById(Long id);

    Owner save(Owner owner);

    Owner update(Owner owner);

    boolean delete(Long id);
}
