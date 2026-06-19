package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = modelMapper.map(productRequest, Product.class);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductResponse.class);
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    modelMapper.map(productRequest, existingProduct);
                    Product updatedProduct = productRepository.save(existingProduct);
                    return modelMapper.map(updatedProduct, ProductResponse.class);
                });
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findByActiveTrue();
        return products.stream()
                .map(p -> modelMapper.map(p, ProductResponse.class))
                .toList();
    }

    public Boolean deleteProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(p -> modelMapper.map(p, ProductResponse.class))
                .toList();
    }
}
