package com.parking.tenant.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TenantDatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INIT_SQL = 
        "CREATE DATABASE IF NOT EXISTS `%s` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";

    public boolean createDatabase(String tenantCode) {
        try {
            String dbName = "parking_" + tenantCode;
            jdbcTemplate.execute(String.format(INIT_SQL, dbName));
            log.info("Database created: {}", dbName);
            return true;
        } catch (Exception e) {
            log.error("Failed to create database: {}", e.getMessage());
            return false;
        }
    }

    public boolean databaseExists(String tenantCode) {
        try {
            String dbName = "parking_" + tenantCode;
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = ?", 
                Integer.class, dbName);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
