package com.parking.payment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.Result;
import com.parking.payment.entity.RateRule;
import com.parking.payment.mapper.RateRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment/v1/rates")
public class RateRuleController {

    private final RateRuleMapper rateRuleMapper;

    @Autowired
    public RateRuleController(RateRuleMapper rateRuleMapper) {
        this.rateRuleMapper = rateRuleMapper;
    }

@GetMapping("/page")
    public Result<IPage<RateRule>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<RateRule> page = new Page<>(current, size);
        return Result.success(rateRuleMapper.selectPage(page, null));
    }

    @GetMapping("/{id}")
    public Result<RateRule> getById(@PathVariable Long id) {
        return Result.success(rateRuleMapper.selectById(id));
    }

@PostMapping
    public Result<RateRule> save(@RequestBody RateRule rateRule) {
        rateRuleMapper.insert(rateRule);
        return Result.success(rateRule);
    }

    @PutMapping("/{id}")
    public Result<RateRule> update(@PathVariable Long id, @RequestBody RateRule rateRule) {
        rateRule.setId(id);
        rateRuleMapper.updateById(rateRule);
        return Result.success(rateRule);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(rateRuleMapper.deleteById(id) > 0);
    }
}
