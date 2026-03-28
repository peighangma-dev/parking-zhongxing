package com.parking.tenant.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.parking.common.core.context.TenantContext;
import io.netty.util.internal.StringUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineInnerInterceptor.TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                Long tenantId = TenantContext.getTenantId();
                if (tenantId != null) {
                    return new LongValue(tenantId);
                }
                return null;
            }

            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {
                return !StringUtil.isNullOrEmpty(tableName) && 
                       (tableName.startsWith("sys_") || 
                        tableName.equals("parking_tenant") ||
                        tableName.startsWith("parking_common"));
            }
        }));
        return interceptor;
    }
}
