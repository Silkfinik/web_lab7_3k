package org.example.controller;

import org.example.entity.Service;
import org.example.entity.Subscriber;
import org.example.repository.ServiceRepository;
import org.example.repository.SubscriberRepository;
import org.example.service.DataInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SubscriberController {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private DataInitService dataInitService;

    @GetMapping("/subscribers")
    public String getAllSubscribers(Model model) {
        Iterable<Subscriber> subscribers = subscriberRepository.findAll();
        model.addAttribute("subscribers", subscribers);
        return "subscribers";
    }

    @GetMapping("/subscribers/add")
    public String showAddSubscriberForm(Model model) {
        model.addAttribute("subscriber", new Subscriber());
        return "subscriber-add";
    }

    @PostMapping("/subscribers/add")
    public String addSubscriber(@ModelAttribute Subscriber subscriber) {
        subscriberRepository.save(subscriber);

        return "redirect:/subscribers";
    }

    @PostMapping("/subscribers/{id}/block")
    public String blockSubscriber(@PathVariable("id") int id) {
        Subscriber subscriber = subscriberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscriber Id:" + id));

        subscriber.setBlocked(true);
        subscriberRepository.save(subscriber);

        return "redirect:/subscribers";
    }

    @GetMapping("/subscribers/{id}")
    public String getSubscriberDetails(@PathVariable("id") int id, Model model) {
        Subscriber subscriber = subscriberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscriber Id:" + id));

        model.addAttribute("subscriber", subscriber);

        model.addAttribute("allServices", serviceRepository.findAll());

        return "subscriber-details";
    }

    @PostMapping("/subscribers/{id}/services")
    public String addServiceToSubscriber(@PathVariable("id") int subscriberId,
                                         @RequestParam("serviceId") int serviceId) {

        Subscriber subscriber = subscriberRepository.findById(subscriberId).orElseThrow();
        Service service = serviceRepository.findById(serviceId).orElseThrow();

        subscriber.getServices().add(service);
        subscriberRepository.save(subscriber);

        return "redirect:/subscribers/" + subscriberId;
    }

    @PostMapping("/reset-db")
    public String resetDatabase() {
        dataInitService.reinitDatabase();
        return "redirect:/subscribers";
    }
}