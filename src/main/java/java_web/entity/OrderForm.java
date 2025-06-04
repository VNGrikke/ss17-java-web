package java_web.entity;

import lombok.Data;

@Data
public class OrderForm {
    private String recipientName;
    private String address;
    private String phone;
}
