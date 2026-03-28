package com.parking.tenant.service;

import com.parking.tenant.entity.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class TenantDatabaseService {

    private final Map<Long, DataSource> tenantDataSources = new ConcurrentHashMap<>();
    
    @Autowired
    private TenantService tenantService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public DataSource getDataSource(Long tenantId) {
        return tenantDataSources.computeIfAbsent(tenantId, this::createDataSource);
    }

    private DataSource createDataSource(Long tenantId) {
        Tenant tenant = tenantService.getById(tenantId);
        if (tenant == null || tenant.getDatabaseName() == null) {
            return null;
        }
        
        String url = String.format(
            "jdbc:mysql://127.0.0.1:3306/%s?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai",
            tenant.getDatabaseName()
        );
        
        log.info("Creating datasource for tenant: {}, database: {}", tenant.getTenantCode(), tenant.getDatabaseName());
        
        com.zaxxer.hikari.HikariDataSource ds = new com.zaxxer.hikari.HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername("root");
        ds.setPassword("root123");
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setMaximumPoolSize(5);
        ds.setMinimumIdle(2);
        
        return ds;
    }

    public boolean createTenantDatabase(Long tenantId) {
        Tenant tenant = tenantService.getById(tenantId);
        if (tenant == null) {
            return false;
        }

        String dbName = tenant.getDatabaseName();
        if (dbName == null || dbName.isEmpty()) {
            dbName = "parking_tenant_" + tenant.getTenantCode();
            tenant.setDatabaseName(dbName);
            tenantService.updateById(tenant);
        }

        try {
            jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS " + dbName + " DEFAULT CHARACTER SET utf8mb4");
            log.info("Created database: {} for tenant: {}", dbName, tenant.getTenantCode());
            return true;
        } catch (Exception e) {
            log.error("Failed to create database for tenant: {}", tenantId, e);
            return false;
        }
    }

    public void initTenantTables(Long tenantId) {
        Tenant tenant = tenantService.getById(tenantId);
        if (tenant == null || tenant.getDatabaseName() == null) {
            return;
        }

        String dbName = tenant.getDatabaseName();
        String baseSql = "CREATE TABLE IF NOT EXISTS ";
        
        List<String> tables = List.of(
            "vehicle", "owner", "member", "blacklist", "payment",
            "payment_channel", "rate_rule", "barrier", "lane", "camera",
            "parking_lot", "parking_area", "parking_space", "pass_record",
            "alarm", "invoice", "invoice_title", "file_info", "storage_config"
        );

        for (String table : tables) {
            try {
                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS " + dbName + "." + table + 
                    "(id BIGINT AUTO_INCREMENT PRIMARY KEY, tenant_id BIGINT DEFAULT " + tenantId + ")");
            } catch (Exception e) {
                log.warn("Table {} may already exist or error: {}", table, e.getMessage());
            }
        }
    }

    public void switchDatabase(Long tenantId) {
        DataSource ds = getDataSource(tenantId);
        if (ds != null) {
            tenantDataSources.put(tenantId, ds);
        }
    }

    public void clearCache() {
        tenantDataSources.clear();
    }
}
