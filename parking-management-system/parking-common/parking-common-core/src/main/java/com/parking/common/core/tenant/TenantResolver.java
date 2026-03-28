package com.parking.common.core.tenant;

import com.parking.common.core.context.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class TenantResolver {
    
    private static final String TENANT_CACHE_KEY = "tenant:code:";
    
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
            if (cached != null && cached instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> tenant = (Map<String, Object>) cached;
                Object idObj = tenant.get("id");
                if (idObj instanceof Number) {
                    TenantContext.setTenantId(((Number) idObj).longValue());
                }
            }
        }
    }
    
    public void clear() {
        TenantContext.clear();
    }
}
