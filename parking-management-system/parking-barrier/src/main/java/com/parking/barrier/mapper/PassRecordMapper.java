package com.parking.barrier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parking.barrier.entity.PassRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PassRecordMapper extends BaseMapper<PassRecord> {
}
