package com.password_manager_server.password_manager_server.web;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.password_manager_server.password_manager_server.model.Account;
import com.password_manager_server.password_manager_server.model.User;
import com.password_manager_server.password_manager_server.repository.AccountRepository;
import com.password_manager_server.password_manager_server.repository.UserRepository;
import com.password_manager_server.password_manager_server.service.UserService;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    public MainController(UserRepository userRepository, UserService userService, AccountRepository accountRepository) {
        super();
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    @ModelAttribute("account")
    public Account defaultInstance() {
        return new Account();
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
    public String addAccount(@ModelAttribute("account") Account account) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        userService.addAccount(currentUsername, account);

        return "redirect:/?accountAdded";

    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(@RequestParam("accountId") String acctId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        User user = userRepository.findByEmail(currentUsername).orElseThrow(() -> {
            throw new NoSuchElementException("Username not found");
        });

        Account acct = accountRepository.getReferenceById(Long.parseLong(acctId));

        user.getAcctList().remove(acct);
        userRepository.save(user);
        accountRepository.deleteById((long) Integer.parseInt(acctId));

        return "redirect:/";
    }

    @GetMapping("/updateAccount/{acctId}")
    public String getUpdateAccount(@PathVariable("acctId") String acctId, Model model) {

        Account currAcct = accountRepository.findById(Long.parseLong(acctId))
                .orElseThrow(() -> new NoSuchElementException("Account not found for this user"));

        model.addAttribute("accountData", currAcct);

        return "update-account";
    }

    @PostMapping("/updateAccount/{acctId}")
    public String updateAccount(@PathVariable String acctId, @ModelAttribute("account") Account updatedAccount) {
        userService.updateAccount(updatedAccount, acctId);

        return "redirect:/?accountUpdated";

    }

}
