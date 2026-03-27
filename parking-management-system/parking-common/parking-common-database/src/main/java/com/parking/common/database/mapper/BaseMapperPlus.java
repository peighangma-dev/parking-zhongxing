package com.parking.common.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BaseMapperPlus<T> extends BaseMapper<T> {

    @Select("SELECT * FROM ${tableName} WHERE id = #{id} AND deleted = 0")
    T selectByIdWithoutDeleted(@Param("tableName") String tableName, @Param("id") Long id);

    default T selectById(Long id) {
        return selectByIdWithoutDeleted(getTableName(), id);
    }

    default List<T> selectList() {
        return selectList(null);
    }

    String getTableName();
}
