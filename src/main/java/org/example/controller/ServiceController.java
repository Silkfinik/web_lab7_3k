package org.example.controller;

import org.example.entity.Service;
import org.example.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/services")
    public String getAllServices(Model model) {
        model.addAttribute("services", serviceRepository.findAll());
        return "services";
    }

    @GetMapping("/services/add")
    public String showAddServiceForm(Model model) {
        model.addAttribute("service", new Service());
        return "service-add";
    }

    @PostMapping("/services/add")
    public String addService(@ModelAttribute Service service) {
        serviceRepository.save(service);
        return "redirect:/services";
    }
}