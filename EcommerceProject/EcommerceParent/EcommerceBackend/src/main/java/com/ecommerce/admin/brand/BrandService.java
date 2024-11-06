package com.ecommerce.admin.brand;

import com.ecommerce.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> listAll(){
        return(List<Brand>) brandRepository.findAll();
    }
}
