package org.example.controller;

import org.example.entity.Invoice;
import org.example.entity.Subscriber;
import org.example.repository.InvoiceRepository;
import org.example.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@Controller
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @GetMapping("/invoices/unpaid")
    public String getUnpaidInvoices(@RequestParam(defaultValue = "0") int page,
                                    Model model) {
        Pageable paging = PageRequest.of(page, 5, Sort.by("id").descending());

        Page<Invoice> invoicesPage = invoiceRepository.findByIsPaidFalse(paging);

        model.addAttribute("invoices", invoicesPage);
        return "invoices-unpaid";
    }

    @PostMapping("/invoices/{id}/pay")
    public String payInvoice(@PathVariable("id") int id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid invoice Id:" + id));

        invoice.setPaid(true);
        invoiceRepository.save(invoice);

        return "redirect:/subscribers/" + invoice.getSubscriber().getId();
    }

    @PostMapping("/subscribers/{subscriberId}/invoices")
    public String createInvoice(@PathVariable("subscriberId") int subscriberId,
                                @RequestParam("amount") double amount) {

        Subscriber subscriber = subscriberRepository.findById(subscriberId).orElseThrow();

        Invoice invoice = new Invoice();
        invoice.setAmount(amount);
        invoice.setIssueDate(LocalDate.now());
        invoice.setPaid(false);
        invoice.setSubscriber(subscriber);

        invoiceRepository.save(invoice);

        return "redirect:/subscribers/" + subscriberId;
    }
}