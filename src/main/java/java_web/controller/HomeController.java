package java_web.controller;

import java_web.entity.Customer;
import java_web.entity.Product;
import java_web.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;

    @GetMapping("/home")
    public String showHome(
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model, HttpSession session
    ) {
        Customer customer = (Customer) session.getAttribute("currentUser");
        if (customer == null)
            return "redirect:/auth/login";

        int size = 5;
        long totalItems = productService.countProducts();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("products", productService.findProducts(page, size));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "home";
    }


    @GetMapping("/product/{id}")
    public String showDetail(@PathVariable("id") Long id, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("currentUser");
        if (customer == null)
            return "redirect:/auth/login";

        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/home";
        }
        model.addAttribute("product", product);
        return "product_detail";
    }
}