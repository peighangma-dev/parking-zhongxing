package com.parking.gateway.filter;

import com.parking.common.core.context.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class TenantFilter implements GlobalFilter, Ordered {

    private static final String TENANT_HEADER = "X-Tenant-Id";
    private static final String TENANT_CODE_HEADER = "X-Tenant-Code";
    private static final String SUPER_ADMIN_HEADER = "X-Super-Admin";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        String tenantId = request.getHeaders().getFirst(TENANT_HEADER);
        String tenantCode = request.getHeaders().getFirst(TENANT_CODE_HEADER);
        String isSuperAdmin = request.getHeaders().getFirst(SUPER_ADMIN_HEADER);
        
        if (tenantId == null) {
            tenantId = request.getQueryParams().getFirst("tenant_id");
        }
        
        if ("true".equals(isSuperAdmin)) {
            TenantContext.setSuperAdmin(true);
        } else {
            if (tenantId != null && !tenantId.isEmpty()) {
                try {
                    TenantContext.setTenantId(Long.parseLong(tenantId));
                    if (tenantCode != null) {
                        TenantContext.setTenantCode(tenantCode);
                    }
                } catch (NumberFormatException e) {
                    log.warn("Invalid tenant id: {}", tenantId);
                }
            }
            
            String host = request.getHeaders().getFirst("Host");
            if (host != null && tenantCode == null) {
                String parsedCode = parseSubdomain(host);
                if (parsedCode != null) {
                    TenantContext.setTenantCode(parsedCode);
                }
            }
        }

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            TenantContext.clear();
        }));
    }

    @Override
    public int getOrder() {
        return -90;
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
