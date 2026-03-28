package com.parking.common.core.context;

public class TenantContext {
    
    private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> TENANT_CODE = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> SUPER_ADMIN = new ThreadLocal<>();

    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    public static void setTenantCode(String tenantCode) {
        TENANT_CODE.set(tenantCode);
    }

    public static String getTenantCode() {
        return TENANT_CODE.get();
    }

    public static void setSuperAdmin(boolean isSuperAdmin) {
        SUPER_ADMIN.set(isSuperAdmin);
    }

    public static Boolean isSuperAdmin() {
        return SUPER_ADMIN.get();
    }

    public static boolean canViewAllTenants() {
        return Boolean.TRUE.equals(SUPER_ADMIN.get());
    }

    public static void clear() {
        TENANT_ID.remove();
        TENANT_CODE.remove();
        SUPER_ADMIN.remove();
    }
}
