package com.parking.barrier.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.barrier.entity.Barrier;
import java.util.List;

public interface BarrierService {

    IPage<Barrier> page(Integer current, Integer size, String code, String name, String status);

    List<Barrier> list(String code, String name, String status);

    Barrier getById(Long id);

    Barrier getByCode(String code);

    Barrier save(Barrier barrier);

    Barrier update(Barrier barrier);

    boolean delete(Long id);

    boolean updateStatus(Long id, String status);
}
