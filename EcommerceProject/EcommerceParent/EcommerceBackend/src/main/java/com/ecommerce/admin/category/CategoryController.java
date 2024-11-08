package com.ecommerce.admin.category;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.common.entity.Category;
import exception.CategoryNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
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
    public List<Category> listFirstPage(@Param("sortDir") String sortDir){
        return categoryService.listByPage(null,1,sortDir,null);
    }

    @GetMapping("/categories/page/{pageNum}")
    public List<Category> listByPage(@PathVariable("pageNum") int pageNum,
                                     @Param("sortDir") String sortDir,
                                     @Param("keyword") String keyword){
        if(sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        CategoryPageInfo categoryPageInfo = new CategoryPageInfo();
        List <Category> listCategories = categoryService.listByPage(categoryPageInfo,pageNum,sortDir,keyword);
        long startCount = (pageNum -1 ) * CategoryService.ROOT_CATEGORIES_PER_PAGE +1;
        long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE -1;
        if(endCount > categoryPageInfo.getTotalElements()){
            endCount = categoryPageInfo.getTotalElements();
        }

        return  listCategories; // will fix soon
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

    @GetMapping("/categories/delete/{id}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id){
        try {
            categoryService.delete(id);
            String categoryDir = "...category-images/" + id;
            FileUploadUtil.removeDir(categoryDir);
        }
        catch (CategoryNotFoundException e ){
            System.out.println(e);

        }

        return "deleted";
    }

    @GetMapping("/categories/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
       List<Category> listCategories = categoryService.listCategoriesUsedInForm("asc");
       CategoryCsvExporter exporter = new CategoryCsvExporter();
       exporter.export(listCategories,response);
    }
}
