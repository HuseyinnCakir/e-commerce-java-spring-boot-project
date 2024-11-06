package com.ecommerce.admin.brand;

import com.ecommerce.admin.category.CategoryRepository;
import com.ecommerce.admin.category.CategoryService;
import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> listAll(){
        return (List<Brand>) brandRepository.findAll();

    }
    public Brand get(Integer id) throws BrandNotFoundException {
        try {
            return brandRepository.findById(id).get();

        }catch (NoSuchElementException ex){
            throw new BrandNotFoundException("Could not find any brand with ID "+ id);
        }
    }
    public Brand save(Brand brand){
        return brandRepository.save(brand);
    }
    public void delete(Integer id ) throws BrandNotFoundException {
        Long countById = brandRepository.countById(id);
        if(countById == null || countById == 0){
            throw new BrandNotFoundException("Could not find any brand with ID "+ id);
        }
        brandRepository.deleteById(id);
    }
}
