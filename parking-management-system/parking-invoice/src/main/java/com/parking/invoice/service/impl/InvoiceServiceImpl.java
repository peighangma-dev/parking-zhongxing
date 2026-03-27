package com.parking.invoice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.invoice.entity.Invoice;
import com.parking.invoice.entity.InvoiceTitle;
import com.parking.invoice.mapper.InvoiceMapper;
import com.parking.invoice.mapper.InvoiceTitleMapper;
import com.parking.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceTitleMapper titleMapper;
    private final InvoiceMapper invoiceMapper;

    @Override
    public IPage<InvoiceTitle> pageTitles(Integer current, Integer size, Long userId) {
        Page<InvoiceTitle> page = new Page<>(current, size);
        LambdaQueryWrapper<InvoiceTitle> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(InvoiceTitle::getUserId, userId);
        }
        wrapper.orderByDesc(InvoiceTitle::getCreatedAt);
        return titleMapper.selectPage(page, wrapper);
    }

    @Override
    public InvoiceTitle getTitleById(Long id) {
        InvoiceTitle title = titleMapper.selectById(id);
        if (title == null) {
            throw new BusinessException(ErrorCode.INVOICE_NOT_FOUND);
        }
        return title;
    }

    @Override
    public InvoiceTitle saveTitle(InvoiceTitle title) {
        titleMapper.insert(title);
        return title;
    }

    @Override
    public InvoiceTitle updateTitle(InvoiceTitle title) {
        if (title.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        titleMapper.updateById(title);
        return title;
    }

    @Override
    public boolean deleteTitle(Long id) {
        return titleMapper.deleteById(id) > 0;
    }

    @Override
    public InvoiceTitle setDefaultTitle(Long userId, Long titleId) {
        LambdaQueryWrapper<InvoiceTitle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvoiceTitle::getUserId, userId);
        titleMapper.selectList(wrapper).forEach(t -> {
            t.setIsDefault(0);
            titleMapper.updateById(t);
        });
        
        InvoiceTitle title = titleMapper.selectById(titleId);
        if (title != null) {
            title.setIsDefault(1);
            titleMapper.updateById(title);
        }
        return title;
    }

    @Override
    public IPage<Invoice> pageInvoices(Integer current, Integer size, Long userId, String status) {
        Page<Invoice> page = new Page<>(current, size);
        LambdaQueryWrapper<Invoice> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Invoice::getUserId, userId);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Invoice::getStatus, status);
        }
        wrapper.orderByDesc(Invoice::getCreatedAt);
        return invoiceMapper.selectPage(page, wrapper);
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        Invoice invoice = invoiceMapper.selectById(id);
        if (invoice == null) {
            throw new BusinessException(ErrorCode.INVOICE_NOT_FOUND);
        }
        return invoice;
    }

    @Override
    public Invoice applyInvoice(Long userId, Long titleId, String plateNumber, BigDecimal amount) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNo("INV" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setUserId(userId);
        invoice.setTitleId(titleId);
        invoice.setPlateNumber(plateNumber);
        invoice.setAmount(amount);
        invoice.setTaxAmount(amount.multiply(BigDecimal.valueOf(0.06)));
        invoice.setStatus("pending");
        invoice.setInvoiceType("normal");
        invoice.setSendingType("email");
        invoiceMapper.insert(invoice);
        return invoice;
    }

    @Override
    public boolean approveInvoice(Long id) {
        Invoice invoice = getInvoiceById(id);
        if (!"pending".equals(invoice.getStatus())) {
            throw new BusinessException(ErrorCode.INVOICE_ISSUED.getCode(), "只能审批待处理的发票");
        }
        invoice.setStatus("approved");
        return invoiceMapper.updateById(invoice) > 0;
    }

    @Override
    public boolean rejectInvoice(Long id, String reason) {
        Invoice invoice = getInvoiceById(id);
        if (!"pending".equals(invoice.getStatus())) {
            throw new BusinessException(ErrorCode.INVOICE_ISSUED.getCode(), "只能拒绝待处理的发票");
        }
        invoice.setStatus("rejected");
        return invoiceMapper.updateById(invoice) > 0;
    }

    @Override
    public boolean issueInvoice(Long id) {
        Invoice invoice = getInvoiceById(id);
        if (!"approved".equals(invoice.getStatus())) {
            throw new BusinessException(ErrorCode.INVOICE_ISSUED.getCode(), "只能开具已审批的发票");
        }
        invoice.setStatus("issued");
        invoice.setBillingTime(LocalDateTime.now());
        invoice.setInvoiceUrl("/invoices/" + invoice.getInvoiceNo() + ".pdf");
        return invoiceMapper.updateById(invoice) > 0;
    }

    @Override
    public boolean cancelInvoice(Long id) {
        Invoice invoice = getInvoiceById(id);
        if ("issued".equals(invoice.getStatus())) {
            throw new BusinessException(ErrorCode.INVOICE_CANCELLED.getCode(), "已开具的发票不能取消");
        }
        invoice.setStatus("cancelled");
        return invoiceMapper.updateById(invoice) > 0;
    }
}
