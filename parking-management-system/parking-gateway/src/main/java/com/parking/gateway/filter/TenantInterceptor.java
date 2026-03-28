package com.parking.gateway.filter;

import com.parking.common.core.context.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@Order(1)
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_HEADER = "X-Tenant-Id";
    private static final String TENANT_PARAM = "tenant_id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(TENANT_HEADER);
        if (tenantId == null) {
            tenantId = request.getParameter(TENANT_PARAM);
        }
        
        if (tenantId != null && !tenantId.isEmpty()) {
            try {
                TenantContext.setTenantId(Long.parseLong(tenantId));
                TenantContext.setTenantCode(request.getHeader("X-Tenant-Code"));
            } catch (NumberFormatException e) {
                log.warn("Invalid tenant id: {}", tenantId);
            }
        }
        
        String host = request.getHeader("Host");
        if (host != null) {
            String tenantCode = parseSubdomain(host);
            if (tenantCode != null) {
                TenantContext.setTenantCode(tenantCode);
            }
        }
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.clear();
    }

    private String parseSubdomain(String host) {
        if (host == null) {
            return null;
        }
        String domain = host.split(":")[0];
        if (domain.endsWith(".monkeycode-ai.online") || domain.endsWith(".localhost")) {
            int dotIndex = domain.indexOf('.');
            if (dotIndex > 0) {
                String subdomain = domain.substring(0, dotIndex);
                if (!subdomain.equals("www") && !subdomain.equals("api")) {
                    return subdomain;
                }
            }
        }
        return null;
    }
}
