package com.ecommerce.admin.category;

import com.ecommerce.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listAll(){
        return (List<Category>) categoryRepository.findAll();
    }

    public List<Category> listCategoriesUsedInForm(){
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDb = categoryRepository.findAll();
        for (Category category:categoriesInDb){
            if(category.getParent() == null){
                listCategoriesUsedInForm().add(new Category(category.getName()));
                Set<Category> children = category.getChildren();
                for (Category subCategories: children){
                    String name = "--" + subCategories.getName();
                    listCategoriesUsedInForm().add(new Category(name));
                    listChildren(categoriesUsedInForm,subCategories,1);
                }
            }


        }
        return  categoriesUsedInForm;
    }
    private void listChildren(List<Category> categoriesUsedInForm,Category parent,int subLevel){
    int newSubLevel = subLevel + 1;
    Set<Category> children = parent.getChildren();
    for (Category subCategories: children){
        String name = "";
        for (int i = 0;i < newSubLevel; i++){

            name += "--";
        }
        name+=subCategories.getName();
        listCategoriesUsedInForm().add(new Category(name));
        listChildren(categoriesUsedInForm,subCategories,newSubLevel);
    }
    }

}
