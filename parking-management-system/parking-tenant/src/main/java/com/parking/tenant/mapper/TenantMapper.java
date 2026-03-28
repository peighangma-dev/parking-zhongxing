package com.parking.tenant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.tenant.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
    
    @Select("<script>" +
            "SELECT SQL_CALC_FOUND_ROWS * FROM sys_tenant WHERE deleted = 0 " +
            "<if test='tenantName != null and tenantName != \"\"'> AND tenant_name LIKE CONCAT('%', #{tenantName}, '%') </if>" +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            "ORDER BY id DESC LIMIT #{offset}, #{limit}" +
            "</script>")
    List<Tenant> selectPageWithConditions(@Param("offset") int offset, 
                                        @Param("limit") int limit,
                                        @Param("tenantName") String tenantName,
                                        @Param("status") String status);
    
    @Select("SELECT FOUND_ROWS()")
    long selectFoundRows();
}
