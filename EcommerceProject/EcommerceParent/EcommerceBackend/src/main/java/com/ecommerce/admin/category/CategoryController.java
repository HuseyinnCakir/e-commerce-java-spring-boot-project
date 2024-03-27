package com.ecommerce.admin.category;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> listAll(){
        return categoryService.listAll();
    }

    @PostMapping("/categories/save")
    public Category saveCategory(Category category, @RequestParam("fileImage")MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        category.setImages(fileName);
        Category savedCategory = categoryService.save(category);
        String uploadDir = "/category-images/" + savedCategory.getId();
        FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        return  savedCategory;
    }
}
