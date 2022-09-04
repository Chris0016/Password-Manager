package com.password_manager_server.password_manager_server.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.password_manager_server.password_manager_server.service.UserService;
import com.password_manager_server.password_manager_server.web.dto.UserRegistrationDto;

@Controller
@RequestMapping("/register")
public class UserRegistrationController {

    private UserService userService;

    public UserRegistrationController(UserService userService) {
        super();
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegristrationDto() {
        return new UserRegistrationDto();
    }

    @PostMapping
    public String registerAccount(@ModelAttribute("user") UserRegistrationDto userRegristrationDto) {
        userService.save(userRegristrationDto);
        return "redirect:/register?success";
    }

}
