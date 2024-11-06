package com.ecommerce.admin.brand;

import com.ecommerce.admin.category.CategoryCsvExporter;
import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Category;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("/brands")
    public List<Brand> listAll() throws IOException {
       List<Brand> brands = brandService.listAll();
       return brands;
    }
}
