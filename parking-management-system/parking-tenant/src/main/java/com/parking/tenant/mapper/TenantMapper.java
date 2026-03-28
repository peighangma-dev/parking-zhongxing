package com.parking.tenant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parking.tenant.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
}
