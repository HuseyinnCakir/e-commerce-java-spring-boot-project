package com.ecommerce.admin.brand;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.admin.category.CategoryService;
import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.exception.BrandNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/brands")
    public List<Brand> listAll() {
       List<Brand> brands = brandService.listAll();
       return brands;
    }
    @GetMapping("/brands/page/{pageNum}")
    public List<Brand> listByPage(@PathVariable("pageNum") int pageNum,
                                     @Param("sortDir") String sortDir,
                                     @Param("keyword") String keyword){

        Page<Brand> page = brandService.listByPage(pageNum,sortDir,keyword);
        List<Brand> listBrands =page.getContent();
        long startCount = (pageNum -1 ) * brandService.BRAND_PER_PAGE +1;
        long endCount = startCount + brandService.BRAND_PER_PAGE -1;
        if(endCount > page.getTotalElements()){
            endCount = page.getTotalElements();
        }

        return  listBrands; // will fix soon
    }

    @GetMapping("/brands/new")
    public List<Category> newBrand(){
        List<Category> listCategories = categoryService.listCategoriesUsedInForm("asc");
        return listCategories;
    }

    @PostMapping("/brands/save")
    public Brand saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);
            Brand savedBrand = brandService.save(brand);
            String uploadDir = "/brand-logos/" + savedBrand.getId();
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            return  savedBrand;
        }
        else{
            Brand savedBrand= brandService.save(brand);
            return  savedBrand;
        }
    }
    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id){
        try {
            brandService.delete(id);
            String categoryDir = "...brand-logos/" + id;
            FileUploadUtil.removeDir(categoryDir);
        }
        catch (BrandNotFoundException e ){
            System.out.println(e);

        }

        return "deleted";
    }
}
