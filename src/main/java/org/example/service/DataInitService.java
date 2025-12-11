package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.entity.Invoice;
import org.example.entity.Service;
import org.example.entity.Subscriber;
import org.example.repository.InvoiceRepository;
import org.example.repository.ServiceRepository;
import org.example.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;

@org.springframework.stereotype.Service
public class DataInitService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void reinitDatabase() {
        invoiceRepository.deleteAll();
        subscriberRepository.deleteAll();
        serviceRepository.deleteAll();

        entityManager.flush();

        Service serv1 = new Service("Интернет 50 Мбит/с", 450.00);
        Service serv2 = new Service("Мобильная связь", 300.00);
        Service serv3 = new Service("Антивирус", 100.00);

        serviceRepository.saveAll(Arrays.asList(serv1, serv2, serv3));

        Subscriber sub1 = new Subscriber("Иван Иванов", "+375291234567", 150.50, false);
        Subscriber sub2 = new Subscriber("Петр Петров", "+375337654321", -50.00, true);

        sub1.getServices().add(serv1);
        sub1.getServices().add(serv2);

        sub2.getServices().add(serv2);
        sub2.getServices().add(serv3);

        subscriberRepository.saveAll(Arrays.asList(sub1, sub2));

        Invoice inv1 = new Invoice(750.00, LocalDate.parse("2025-09-01"), true, sub1);
        Invoice inv2 = new Invoice(400.00, LocalDate.parse("2025-09-05"), false, sub2);

        invoiceRepository.saveAll(Arrays.asList(inv1, inv2));
    }
}