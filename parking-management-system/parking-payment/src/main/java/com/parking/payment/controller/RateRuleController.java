package com.parking.payment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.payment.entity.RateRule;
import com.parking.payment.mapper.RateRuleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "费率规则管理")
@RestController
@RequestMapping("/api/payment/v1/rates")
@RequiredArgsConstructor
public class RateRuleController {

    private final RateRuleMapper rateRuleMapper;

    @Operation(summary = "规则列表")
    @GetMapping
    public Result<IPage<RateRule>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<RateRule> page = new Page<>(current, size);
        return Result.success(rateRuleMapper.selectPage(page, null));
    }

    @Operation(summary = "规则详情")
    @GetMapping("/{id}")
    public Result<RateRule> getById(@PathVariable Long id) {
        return Result.success(rateRuleMapper.selectById(id));
    }

    @Operation(summary = "创建规则")
    @PostMapping
    public Result<RateRule> save(@RequestBody RateRule rateRule) {
        rateRuleMapper.insert(rateRule);
        return Result.success(rateRule);
    }

    @Operation(summary = "更新规则")
    @PutMapping("/{id}")
    public Result<RateRule> update(@PathVariable Long id, @RequestBody RateRule rateRule) {
        rateRule.setId(id);
        rateRuleMapper.updateById(rateRule);
        return Result.success(rateRule);
    }

    @Operation(summary = "删除规则")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return Result.success(rateRuleMapper.deleteById(id) > 0 ? null : false);
    }
}
