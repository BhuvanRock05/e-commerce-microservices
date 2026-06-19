package com.ecommerce.order.service;

import com.ecommerce.order.dto.CartItemResponse;
import com.ecommerce.order.dto.OrderItemDto;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    public Optional<OrderResponse> createOrder(String userId) {
        // Validate for cart items
        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }

//        // Validate for user
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if (userOptional.isEmpty()) {
//            return Optional.empty();
//        }
//        User user = userOptional.get();

        // Calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // Create order
        Order order = new Order();
        order.setUserId(Long.valueOf(userId));
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                )).toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getItems().stream()
                        .map(
                                orderItem -> new OrderItemDto(
                                        orderItem.getId(),
                                        orderItem.getProductId(),
                                        orderItem.getQuantity(),
                                        orderItem.getPrice(),
                                        orderItem.getPrice().multiply(new BigDecimal((orderItem.getQuantity())))
                                )
                        ).toList(),
                order.getCreatedAt()
        );
    }
}
