package com.ecommerce.admin.product;

import com.ecommerce.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<Product> listAll(){
        return productService.listAll();
    }

    @PostMapping("/products/save")
    public Product saveProduct(Product product){
        Product savedProduct = productService.save(product);
        return savedProduct;
    }

}
