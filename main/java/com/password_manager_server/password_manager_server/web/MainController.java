package com.password_manager_server.password_manager_server.web;

import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import com.password_manager_server.password_manager_server.service.UserServiceImpl.AccountNameAlreadyExistsException;

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

    @GetMapping("/AddAccount")
    public String getAddAccount(@Valid @ModelAttribute("account") Account account) {
        return "add-account";
    }

    @PostMapping("/AddAccount")
    public String addAccount(@Valid @ModelAttribute("account") Account account,
            BindingResult bindingResult) {
        // Refactor: Add service needs to be in its own page, or be a model popup
        if (bindingResult.hasErrors()) {

            return "add-account";
        }
        // Refactor: Add service needs to be in its own page, or be a model popup

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

        // System.out.println("\n\nCurrent ID: " + currAcct.getId() + "\n\n");
        return "update-account";
    }

    @PostMapping("/updateAccount/{acctId}")
    public String updateAccount(@PathVariable String acctId, @Valid @ModelAttribute("account") Account updatedAccount,
            BindingResult bindingResult, Model model) {

        Account currAcct = accountRepository.findById(Long.parseLong(acctId))
                .orElseThrow(() -> new NoSuchElementException("Account not found for this user"));

        if (bindingResult.hasErrors()) {
            model.addAttribute("accountData", currAcct);
            return "update-account";
        }

        try {

            updatedAccount.setId(Long.parseLong(acctId));
            userService.updateAccount(updatedAccount, acctId);

        } catch (AccountNameAlreadyExistsException e) {
            bindingResult.addError(new FieldError("account", "name",
                    "Account name already exists. Cannot have accounts with duplicate names."));
            model.addAttribute("accountData", currAcct);
            return "update-account";
        } catch (Exception e) {

            // return "redirect:/Error";
        }

        return "redirect:/?accountUpdated";
    }

}
