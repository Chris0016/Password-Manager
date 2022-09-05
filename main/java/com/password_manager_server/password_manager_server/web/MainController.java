package com.password_manager_server.password_manager_server.web;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.password_manager_server.password_manager_server.model.Service;
import com.password_manager_server.password_manager_server.model.User;
import com.password_manager_server.password_manager_server.repository.UserRepository;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        // ArrayList<Service> serviceList = new ArrayList<>();

        // serviceList.add(new Service("Netflix", "StrongPassword123", "Yesterday", "7
        // days ago"));

        model.addAttribute("serviceList", userRepository.findByEmail(currentUsername).getServiceList());

        return "index";
    }
}
