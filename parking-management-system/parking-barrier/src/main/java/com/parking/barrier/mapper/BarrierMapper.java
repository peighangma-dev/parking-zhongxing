package com.parking.barrier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parking.barrier.entity.Barrier;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BarrierMapper extends BaseMapper<Barrier> {
}
