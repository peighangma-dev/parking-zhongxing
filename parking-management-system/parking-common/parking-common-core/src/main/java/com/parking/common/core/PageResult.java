package com.parking.common.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "分页结果")
public class PageResult<T> implements Serializable {

    @Schema(description = "总记录数")
    private long total;

    @Schema(description = "当前页数据")
    private List<T> records;

    @Schema(description = "当前页码")
    private long current;

    @Schema(description = "每页大小")
    private long size;

    @Schema(description = "总页数")
    private long pages;

    public PageResult(List<T> records, long total, long current, long size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = (total + size - 1) / size;
    }

    public static <T> PageResult<T> of(List<T> records, long total, long current, long size) {
        return new PageResult<>(records, total, current, size);
    }

    public static <T> PageResult<T> empty(long current, long size) {
        return new PageResult<>(List.of(), 0, current, size);
    }
}
