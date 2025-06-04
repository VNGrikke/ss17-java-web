    package java_web.controller;

    import java_web.entity.*;
    import java_web.repository.CartRepository;
    import java_web.repository.OrderRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import javax.servlet.http.HttpSession;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    @Controller
    @RequestMapping("/order")
    public class OrderController {

        @Autowired
        private CartRepository cartRepo;

        @Autowired
        private OrderRepository orderRepo;

        @GetMapping("/checkout")
        public String showCheckoutForm(HttpSession session, Model model) {
            Customer customer = (Customer) session.getAttribute("currentUser");
            if (customer == null)
                return "redirect:/auth/login";

            Cart cart = cartRepo.findByCustomerId(customer.getId());
            if (cart == null || cart.getItems().isEmpty()) {
                model.addAttribute("message", "Giỏ hàng của bạn đang trống!");
                return "user/home";
            }

            model.addAttribute("cartItems", cart.getItems());

            double total = cart.getItems().stream()
                    .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                    .sum();
            model.addAttribute("total", total);

            return "user/checkout";
        }


        @PostMapping("/checkout")
        public String checkout(@ModelAttribute OrderForm orderForm, HttpSession session, Model model) {
            String name = orderForm.getRecipientName();
            String address = orderForm.getAddress();
            String phone = orderForm.getPhone();


        Customer customer = (Customer) session.getAttribute("currentUser");
            if (customer == null)
                return "redirect:/auth/login";

            Cart cart = cartRepo.findByCustomerId(customer.getId());
            if (cart == null || cart.getItems().isEmpty())
                return "redirect:/cart/view";


            Order order = new Order();
            order.setCustomer(customer);
            order.setRecipientName(name);
            order.setAddress(address);
            order.setPhone(phone);
            order.setOrderDate(LocalDateTime.now());

            List<OrderDetail> details = new ArrayList<>();
            for (CartItem item: cart.getItems()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrder(order);
                detail.setProduct(item.getProduct());
                detail.setQuantity(item.getQuantity());
                detail.setUnitPrice(item.getProduct().getPrice());
                details.add(detail);
            }
            order.setOrderDetails(details);

            orderRepo.save(order);

            cart.getItems().clear();
            cartRepo.update(cart);

            model.addAttribute("message", "Đặt hàng thành công!");
            return "redirect:/home";
        }
    }
