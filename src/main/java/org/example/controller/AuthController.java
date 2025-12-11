package org.example.controller;

import jakarta.servlet.http.HttpSession;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String login,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userRepository.findByLogin(login).orElse(null);

        if (user != null && user.getPassword().equals(PasswordUtil.hash(password))) {
            session.setAttribute("user", user);
            return "redirect:/subscribers";
        } else {
            model.addAttribute("errorMessage", "Неверный логин или пароль");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String login,
                           @RequestParam String password,
                           HttpSession session,
                           Model model) {

        if (userRepository.findByLogin(login).isPresent()) {
            model.addAttribute("errorMessage", "Пользователь с таким логином уже существует");
            return "register";
        }

        User user = new User(login, PasswordUtil.hash(password), Role.USER);

        if ("admin".equalsIgnoreCase(login)) {
            user.setRole(Role.ADMIN);
        }

        userRepository.save(user);

        session.setAttribute("user", user);
        return "redirect:/subscribers";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}