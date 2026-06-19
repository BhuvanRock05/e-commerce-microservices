package com.ecommerce.order.service;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.CartItemResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public boolean addToCart(String userId, CartItemRequest request) {
//        Optional<Product> productOptional = productRepository.findById(request.getProductId());
//
//        if (productOptional.isEmpty()) {
//            return false;
//        }
//        Product product = productOptional.get();
//        if (product.getStockQuantity() < request.getQuantity()) {
//            return false;
//        }
//
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//
//        if (userOptional.isEmpty()) {
//            return false;
//        }
//        User user = userOptional.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(Long.valueOf(userId), request.getProductId());

        if (existingCartItem != null) {
            // update cart
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
//            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(existingCartItem);
        } else {
            // create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUserId(Long.valueOf(userId));
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
//            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
//        Optional<Product> productOptional = productRepository.findById(productId);
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
         CartItem cartItem = cartItemRepository.findByUserIdAndProductId(Long.valueOf(userId), productId);

        if (cartItem != null) {
            cartItemRepository.deleteByUserIdAndProductId(Long.valueOf(userId), productId);
            return true;
        }
        return false;
    }

    public List<CartItem> getCartItems(String userId) {
        return cartItemRepository.findByUserId(Long.valueOf(userId));
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
