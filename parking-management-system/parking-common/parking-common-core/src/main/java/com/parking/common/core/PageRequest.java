package com.parking.common.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.Serializable;

@Data
@Schema(description = "分页请求")
public class PageRequest implements Serializable {

    @Schema(description = "当前页码", example = "1")
    @Min(value = 1, message = "页码最小为1")
    private long current = 1;

    @Schema(description = "每页大小", example = "10")
    @Min(value = 1, message = "每页大小最小为1")
    @Max(value = 100, message = "每页大小最大为100")
    private long size = 10;

    @Schema(description = "排序字段")
    private String sortField;

    @Schema(description = "排序方向 asc/desc")
    private String sortOrder = "desc";

    public long getOffset() {
        return (current - 1) * size;
    }
}
