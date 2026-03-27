package com.parking.common.core;

import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.Serializable;

@Data
public class PageRequest implements Serializable {

    @Min(value = 1, message = "页码最小为1")
    private long current = 1;

    @Min(value = 1, message = "每页大小最小为1")
    @Max(value = 100, message = "每页大小最大为100")
    private long size = 10;

    private String sortField;

    private String sortOrder = "desc";

    public long getOffset() {
        return (current - 1) * size;
    }
}
