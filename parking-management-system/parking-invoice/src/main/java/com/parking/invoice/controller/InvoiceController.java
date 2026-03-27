package com.parking.invoice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.invoice.entity.Invoice;
import com.parking.invoice.entity.InvoiceTitle;
import com.parking.invoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/invoice/v1")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/titles/page")
    public Result<IPage<InvoiceTitle>> pageTitles(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId) {
        return Result.success(invoiceService.pageTitles(current, size, userId));
    }

    @GetMapping("/titles/{id}")
    public Result<InvoiceTitle> getTitleById(@PathVariable Long id) {
        return Result.success(invoiceService.getTitleById(id));
    }

    @PostMapping("/titles")
    public Result<InvoiceTitle> saveTitle(@RequestBody InvoiceTitle title) {
        return Result.success(invoiceService.saveTitle(title));
    }

    @PutMapping("/titles/{id}")
    public Result<InvoiceTitle> updateTitle(@PathVariable Long id, @RequestBody InvoiceTitle title) {
        title.setId(id);
        return Result.success(invoiceService.updateTitle(title));
    }

    @DeleteMapping("/titles/{id}")
    public Result<Boolean> deleteTitle(@PathVariable Long id) {
        return Result.success(invoiceService.deleteTitle(id));
    }

    @PutMapping("/titles/{id}/default")
    public Result<InvoiceTitle> setDefaultTitle(
            @PathVariable Long id,
            @RequestParam Long userId) {
        return Result.success(invoiceService.setDefaultTitle(userId, id));
    }

    @GetMapping("/invoices/page")
    public Result<IPage<Invoice>> pageInvoices(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status) {
        return Result.success(invoiceService.pageInvoices(current, size, userId, status));
    }

    @GetMapping("/invoices/{id}")
    public Result<Invoice> getInvoiceById(@PathVariable Long id) {
        return Result.success(invoiceService.getInvoiceById(id));
    }

    @PostMapping("/invoices/apply")
    public Result<Invoice> applyInvoice(
            @RequestParam Long userId,
            @RequestParam Long titleId,
            @RequestParam String plateNumber,
            @RequestParam BigDecimal amount) {
        return Result.success(invoiceService.applyInvoice(userId, titleId, plateNumber, amount));
    }

    @PutMapping("/invoices/{id}/approve")
    public Result<Boolean> approve(@PathVariable Long id) {
        return Result.success(invoiceService.approveInvoice(id));
    }

    @PutMapping("/invoices/{id}/reject")
    public Result<Boolean> reject(@PathVariable Long id, @RequestParam(required = false) String reason) {
        return Result.success(invoiceService.rejectInvoice(id, reason));
    }

    @PutMapping("/invoices/{id}/issue")
    public Result<Boolean> issue(@PathVariable Long id) {
        return Result.success(invoiceService.issueInvoice(id));
    }

    @PutMapping("/invoices/{id}/cancel")
    public Result<Boolean> cancel(@PathVariable Long id) {
        return Result.success(invoiceService.cancelInvoice(id));
    }
}
