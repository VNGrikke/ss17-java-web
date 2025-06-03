package java_web.controller;

import java_web.entity.Customer;
import java_web.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private CustomerService customersevice;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("customer") @Valid Customer customer,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        if (customersevice.findByUsername(customer.getUsername()) != null) {
            return "register";
        }

        customersevice.save(customer);
        return "redirect:login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "login";
    }

    @GetMapping("/admin_home")
    public String showAdminHome(Model model){
        return "admin_home";
    }


    @PostMapping("/login")
    public String processLogin(@ModelAttribute("customer") Customer customer,
                               HttpSession session, Model model) {
        Customer found = customersevice.login(customer.getUsername(), customer.getPassword());

        if (found == null) {
            return "login";
        }

        if (!found.isStatus()) {
            return "login";
        }

        session.setAttribute("currentUser", found);

        if ("ADMIN".equalsIgnoreCase(found.getRole())) {
            return "admin_home";
        } else {
            model.addAttribute("customer", found);
            return  "redirect:/home";
        }
    }

}