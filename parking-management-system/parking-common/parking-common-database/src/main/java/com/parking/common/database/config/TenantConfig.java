package com.parking.common.database.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.parking.common.core.context.TenantContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.List;

public class TenantConfig {

    public static MybatisPlusInterceptor getTenantInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler()));
        return interceptor;
    }

    public static class TenantLineHandler implements TenantLineInnerInterceptor.TenantLineHandler {
        
        private static final List<String> IGNORE_TABLES = List.of(
            "sys_tenant", "sys_package", "sys_package_feature",
            "parking_tenant", "sys_menu", "sys_role", "sys_role_menu", "sys_user_role"
        );

        @Override
        public Expression getTenantId() {
            Long tenantId = TenantContext.getTenantId();
            if (tenantId != null) {
                return new LongValue(tenantId);
            }
            return new LongValue(0);
        }

        @Override
        public String getTenantIdColumn() {
            return "tenant_id";
        }

        @Override
        public boolean ignoreTable(String tableName) {
            if (tableName == null) {
                return true;
            }
            String name = PropertyNamer.propertyToField(tableName);
            return IGNORE_TABLES.contains(name) || IGNORE_TABLES.contains(tableName);
        }
    }
}
