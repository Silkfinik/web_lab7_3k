package org.example.controller;

import org.example.entity.Service;
import org.example.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/services")
    public String getAllServices(@RequestParam(defaultValue = "0") int page,
                                 Model model) {
        Pageable paging = PageRequest.of(page, 5);
        Page<Service> servicesPage = serviceRepository.findAll(paging);

        model.addAttribute("services", servicesPage);
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