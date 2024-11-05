package com.ecommerce.admin.category;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> listAll(@Param("sortDir") String sortDir){
        return categoryService.listAll(sortDir);
    }

    @PostMapping("/categories/save")
    public Category saveCategory(Category category, @RequestParam("fileImage")MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImages(fileName);
            Category savedCategory = categoryService.save(category);
            String uploadDir = "/category-images/" + savedCategory.getId();
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            return  savedCategory;
        }
        else{
            Category savedCategory= categoryService.save(category);
            return  savedCategory;
        }



    }


    @GetMapping("/categories/edit/{id}")
    public Category editCategory(@PathVariable(name = "id") Integer id){
        try{
            Category category = categoryService.get(id);

            return  category;
        } catch (CategoryNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled ){
        categoryService.updateCategoryEnabledStatus(id,enabled);
        return "updated";
    }
}
