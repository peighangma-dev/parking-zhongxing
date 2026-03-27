package com.parking.common.core;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    private long total;

    private List<T> records;

    private long current;

    private long size;

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
