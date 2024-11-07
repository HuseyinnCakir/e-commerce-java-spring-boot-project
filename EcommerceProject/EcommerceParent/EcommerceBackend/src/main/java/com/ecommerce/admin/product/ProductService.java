package com.ecommerce.admin.product;

import com.ecommerce.admin.brand.BrandNotFoundException;
import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> listAll(){
        return (List<Product>) productRepository.findAll();
    }

    public Product save(Product product) {
        if(product.getId() == null) {
            product.setCreatedTime(new Date());
        }
        if(product.getAlias() == null || product.getAlias().isEmpty()){
            String defaultAlias = product.getName().replaceAll(" ","-");
            product.setAlias(defaultAlias);
        }
        else {
            product.setAlias((product.getAlias().replaceAll(" ","-")));
        }
        product.setUpdateTime(new Date());
        return productRepository.save(product);
    }


    public String checkUnique(Integer id, String name){
        boolean isCreatingNew = (id == null || id == 0);
        Product productByName = productRepository.findByName(name);
        if(isCreatingNew){
            if(productByName != null){
                return "Duplicate";
            }
            else{

                if(productByName != null && productByName.getId() != id){
                    return "Duplicate";
                }
            }
        }

        return "OK";
    }

    public void delete(Integer id ) throws ProductNotFoundException {
        Long countById = productRepository.countById(id);
        if(countById == null || countById == 0){
            throw new ProductNotFoundException("Could not find any product with ID "+ id);
        }
        productRepository.deleteById(id);
    }
        public Product get(Integer id) throws ProductNotFoundException {
        try {
            return productRepository.findById(id).get();
        }
        catch (NoSuchElementException ex){
            throw new ProductNotFoundException("Could not find any product with ID "+ id);
        }
        }
}
