package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {

    private Long id;

    private Long userId;

    private Long productId;

    private Integer quantity;

    private BigDecimal price;
}
