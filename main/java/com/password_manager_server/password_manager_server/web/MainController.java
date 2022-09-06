package com.password_manager_server.password_manager_server.web;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.password_manager_server.password_manager_server.model.User;
import com.password_manager_server.password_manager_server.repository.UserRepository;
import com.password_manager_server.password_manager_server.service.UserService;
import com.password_manager_server.password_manager_server.web.dto.AccountDto;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public MainController(UserRepository userRepository, UserService userService) {
        super();
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @ModelAttribute("account")
    public AccountDto accountDto() {
        return new AccountDto();
    }

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
        User curUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("Username not found");
                });

        model.addAttribute("serviceList", curUser.getAcctList());
        return "index";
    }

    @PostMapping
    public String addAccount(@ModelAttribute("account") AccountDto accountDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        userService.addAccount(currentUsername, accountDto);

        return "redirect:/?success";

    }

}
