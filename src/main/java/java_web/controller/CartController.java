package java_web.controller;

import java_web.entity.Cart;
import java_web.entity.CartItem;
import java_web.entity.Customer;
import java_web.entity.Product;
import java_web.repository.ProductRepository;
import java_web.service.CartService;
import java_web.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable int productId, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("currentUser");
        if (customer == null)
            return "redirect:/auth/login";

        Cart cart = cartService.findByCustomerId(customer.getId());
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(customer);
            cartService.save(cart);
        }

        Product product = productService.findById((long) productId);
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(1);

        cart.getItems().add(item);
        cartService.update(cart);
        model.addAttribute("message", "Product added to cart successfully!");
        return "redirect:/home";
    }

    @GetMapping("/view")
    public String viewCart(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("currentUser");
        if (customer == null)
            return "redirect:/auth/login";

        Cart cart = cartService.findByCustomerId(customer.getId());
        if (cart == null || cart.getItems().isEmpty()) {
            model.addAttribute("message", "Giỏ hàng của bạn đang trống.");
            return "cart";
        }


        model.addAttribute("cartItems", cart.getItems());

        double total = cart.getItems().stream()
                .mapToDouble(item -> {
                    Product product = productService.findById((long) item.getProduct().getId());
                    return product.getPrice() * item.getQuantity();
                })
                .sum();
        model.addAttribute("total", total);

        return "cart";
    }

    @PostMapping("/remove/{itemId}")
    public String removeItem(@PathVariable int itemId, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("currentUser");
        if (customer == null)
            return "redirect:/auth/login";

        Cart cart = cartService.findByCustomerId(customer.getId());
        cartService.delete(cart, itemId);

        return "redirect:/cart/view";
    }

}