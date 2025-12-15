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
import java.util.List;
import java.util.Random;

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

        entityManager.createNativeQuery("ALTER TABLE invoices AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE subscriber_services AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE subscribers AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE services AUTO_INCREMENT = 1").executeUpdate();

        entityManager.flush();

        Service serv1 = new Service("Интернет 100 Мбит/с", 550.00);
        Service serv2 = new Service("Мобильная связь", 350.00);
        Service serv3 = new Service("Антивирус PRO", 120.00);
        Service serv4 = new Service("IP-TV (150 каналов)", 290.00);
        Service serv5 = new Service("Статический IP", 150.00);

        List<Service> allServices = Arrays.asList(serv1, serv2, serv3, serv4, serv5);
        serviceRepository.saveAll(allServices);

        Random random = new Random();
        String[] firstNames = {"Иван", "Петр", "Алексей", "Дмитрий", "Сергей", "Андрей", "Михаил", "Николай", "Егор", "Максим", "Ольга", "Анна", "Елена", "Мария", "Светлана"};
        String[] lastNames = {"Иванов", "Петров", "Сидоров", "Смирнов", "Кузнецов", "Попов", "Васильев", "Соколов", "Михайлов", "Новиков", "Федоров", "Морозов"};

        for (int i = 0; i < 50; i++) {
            String name = firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)];
            String phone = "+79" + String.format("%09d", random.nextInt(1_000_000_000));

            double balance = Math.round((random.nextDouble() * 2500 - 500) * 100.0) / 100.0;

            boolean isBlocked = random.nextInt(10) == 0;

            Subscriber sub = new Subscriber(name, phone, balance, isBlocked);

            int servicesCount = random.nextInt(4);
            for (int j = 0; j < servicesCount; j++) {
                sub.getServices().add(allServices.get(random.nextInt(allServices.size())));
            }

            subscriberRepository.save(sub);

            if (random.nextInt(5) == 0) {
                double amount = Math.round((random.nextDouble() * 1000 + 100) * 100.0) / 100.0;
                Invoice invoice = new Invoice(amount, LocalDate.now().minusDays(random.nextInt(30)), false, sub);
                invoiceRepository.save(invoice);
            }
        }
    }
}