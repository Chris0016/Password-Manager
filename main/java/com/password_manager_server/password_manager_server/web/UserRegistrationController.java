package com.password_manager_server.password_manager_server.web;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.password_manager_server.password_manager_server.model.User;
import com.password_manager_server.password_manager_server.service.UserService;

@Controller
@RequestMapping("/register")
public class UserRegistrationController {

    private UserService userService;

    public UserRegistrationController(UserService userService) {
        super();
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User defaultInstance() {
        return new User();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    // @ModelAttribute("user")
    // public UserRegistrationDto userRegristrationDto() {
    // return new UserRegistrationDto();
    // }

    // @PostMapping
    // public String checkUserInformation(@Valid @ModelAttribute("user")
    // UserRegistrationDto userRegristrationDto,
    // BindingResult bindingResult) {
    // if (bindingResult.hasErrors())
    // return "registration";

    // userService.save(userRegristrationDto);
    // return "redirect:/register?success";
    // }

    @PostMapping
    public String checkUserInfo(@Valid @ModelAttribute("user") User user,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "registration";

        userService.save(user);
        return "redirect:/register?success";
    }

}
