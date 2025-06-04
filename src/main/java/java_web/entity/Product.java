package java_web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String productName;

    @Size(max = 500, message = "Mô tả quá dài (tối đa 500 ký tự)")
    private String description;

    @Min(value = 0, message = "Giá phải >= 0")
    private double price;

    @Min(value = 0, message = "Số lượng tồn phải >= 0")
    private Integer stock;
    private String image;
}
