package com.ecommerce.admin.product;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.admin.brand.BrandNotFoundException;
import com.ecommerce.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id){
            try {
                productService.delete(id);
            } catch (ProductNotFoundException e) {
                throw new RuntimeException(e);
            }
            String categoryDir = "...brand-logos/" + id;
            FileUploadUtil.removeDir(categoryDir);
            return "deleted";
        }
}
