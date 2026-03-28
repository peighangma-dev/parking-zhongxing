package com.parking.common.core.tenant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.parking.common.core.context.TenantContext;
import com.parking.common.database.entity.BaseEntity;
import com.parking.common.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TenantResolver {
    
    private static final String TENANT_CACHE_KEY = "tenant:code:";
    private static final long CACHE_EXPIRE = 10;
    
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;
    
    public void resolveByDomain(String domain) {
        if (domain == null || domain.isEmpty()) {
            return;
        }
        
        String tenantCode = parseSubdomain(domain);
        if (tenantCode != null) {
            setTenantContext(tenantCode);
        }
    }
    
    public void resolveByHeader(Long tenantId) {
        if (tenantId != null) {
            TenantContext.setTenantId(tenantId);
        }
    }
    
    private String parseSubdomain(String domain) {
        if (domain.endsWith(".localhost") || domain.endsWith(".monkeycode-ai.online")) {
            int dotIndex = domain.indexOf('.');
            if (dotIndex > 0) {
                return domain.substring(0, dotIndex);
            }
        }
        return null;
    }
    
    private void setTenantContext(String tenantCode) {
        TenantContext.setTenantCode(tenantCode);
        
        if (redisTemplate != null) {
            String cacheKey = TENANT_CACHE_KEY + tenantCode;
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                Map<String, Object> tenant = (Map<String, Object>) cached;
                TenantContext.setTenantId(((Number) tenant.get("id")).longValue();
            }
        }
    }
    
    public void clear() {
        TenantContext.clear();
    }
}
