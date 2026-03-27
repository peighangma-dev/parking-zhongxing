package com.parking.invoice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.invoice.entity.Invoice;
import com.parking.invoice.entity.InvoiceTitle;
import com.parking.invoice.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "发票管理")
@RestController
@RequestMapping("/api/invoice/v1")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Operation(summary = "发票抬头列表")
    @GetMapping("/titles")
    public Result<IPage<InvoiceTitle>> pageTitles(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId) {
        return Result.success(invoiceService.pageTitles(current, size, userId));
    }

    @Operation(summary = "发票抬头详情")
    @GetMapping("/titles/{id}")
    public Result<InvoiceTitle> getTitleById(@PathVariable Long id) {
        return Result.success(invoiceService.getTitleById(id));
    }

    @Operation(summary = "创建发票抬头")
    @PostMapping("/titles")
    public Result<InvoiceTitle> saveTitle(@RequestBody InvoiceTitle title) {
        return Result.success(invoiceService.saveTitle(title));
    }

    @Operation(summary = "更新发票抬头")
    @PutMapping("/titles/{id}")
    public Result<InvoiceTitle> updateTitle(@PathVariable Long id, @RequestBody InvoiceTitle title) {
        title.setId(id);
        return Result.success(invoiceService.updateTitle(title));
    }

    @Operation(summary = "删除发票抬头")
    @DeleteMapping("/titles/{id}")
    public Result<Void> deleteTitle(@PathVariable Long id) {
        return Result.success(invoiceService.deleteTitle(id) ? null : false);
    }

    @Operation(summary = "设置默认抬头")
    @PutMapping("/titles/{id}/default")
    public Result<Void> setDefaultTitle(
            @RequestParam Long userId,
            @PathVariable Long id) {
        return Result.success(invoiceService.setDefaultTitle(userId, id) != null ? null : false);
    }

    @Operation(summary = "发票列表")
    @GetMapping("/invoices")
    public Result<IPage<Invoice>> pageInvoices(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status) {
        return Result.success(invoiceService.pageInvoices(current, size, userId, status));
    }

    @Operation(summary = "发票详情")
    @GetMapping("/invoices/{id}")
    public Result<Invoice> getInvoiceById(@PathVariable Long id) {
        return Result.success(invoiceService.getInvoiceById(id));
    }

    @Operation(summary = "申请发票")
    @PostMapping("/invoices")
    public Result<Invoice> applyInvoice(
            @RequestParam Long userId,
            @RequestParam Long titleId,
            @RequestParam String plateNumber,
            @RequestParam BigDecimal amount) {
        return Result.success(invoiceService.applyInvoice(userId, titleId, plateNumber, amount));
    }

    @Operation(summary = "审批通过")
    @PutMapping("/invoices/{id}/approve")
    public Result<Void> approve(@PathVariable Long id) {
        return Result.success(invoiceService.approveInvoice(id) ? null : false);
    }

    @Operation(summary = "审批拒绝")
    @PutMapping("/invoices/{id}/reject")
    public Result<Void> reject(@PathVariable Long id, @RequestParam(required = false) String reason) {
        return Result.success(invoiceService.rejectInvoice(id, reason) ? null : false);
    }

    @Operation(summary = "开具发票")
    @PutMapping("/invoices/{id}/issue")
    public Result<Void> issue(@PathVariable Long id) {
        return Result.success(invoiceService.issueInvoice(id) ? null : false);
    }

    @Operation(summary = "取消发票")
    @PutMapping("/invoices/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        return Result.success(invoiceService.cancelInvoice(id) ? null : false);
    }
}
