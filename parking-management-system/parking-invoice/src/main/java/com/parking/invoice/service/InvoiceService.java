package com.parking.invoice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.invoice.entity.Invoice;
import com.parking.invoice.entity.InvoiceTitle;
import java.math.BigDecimal;

public interface InvoiceService {

    IPage<InvoiceTitle> pageTitles(Integer current, Integer size, Long userId);

    InvoiceTitle getTitleById(Long id);

    InvoiceTitle saveTitle(InvoiceTitle title);

    InvoiceTitle updateTitle(InvoiceTitle title);

    boolean deleteTitle(Long id);

    InvoiceTitle setDefaultTitle(Long userId, Long titleId);

    IPage<Invoice> pageInvoices(Integer current, Integer size, Long userId, String status);

    Invoice getInvoiceById(Long id);

    Invoice applyInvoice(Long userId, Long titleId, String plateNumber, BigDecimal amount);

    boolean approveInvoice(Long id);

    boolean rejectInvoice(Long id, String reason);

    boolean issueInvoice(Long id);

    boolean cancelInvoice(Long id);
}
