package com.ecommerce.admin.category;

import com.ecommerce.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listAll(){
        List<Category> rootCategories = categoryRepository.findRootCategories();
        return listHierarchicalCategories(rootCategories);
    }
    private  List<Category> listHierarchicalCategories( List<Category> rootCategories){
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory: rootCategories){
            hierarchicalCategories.add(Category.copyFull(rootCategory));
            Set<Category> children = rootCategory.getChildren();

            for (Category subCategory: children){
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory,name));
                listSubHierarchicalCategories(hierarchicalCategories,subCategory,1);
            }
        }

        return hierarchicalCategories;
    }
    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,Category parent,int subLevel){
        Set<Category> children = parent.getChildren();

        for (Category subCategory:children){
            String name = "";
            int newSubLevel = subLevel + 1;
            for (int i = 0;i < newSubLevel; i++){

                name += "--";
            }
            name+=subCategory.getName();

            hierarchicalCategories.add(Category.copyFull(subCategory,name));
            listSubHierarchicalCategories(hierarchicalCategories,subCategory,newSubLevel );
        }
    }
    public Category save(Category category){
        return categoryRepository.save(category);
}  public List<Category> listCategoriesUsedInForm(){
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDb = categoryRepository.findAll();
        for (Category category:categoriesInDb){
            if(category.getParent() == null){
                listCategoriesUsedInForm().add(Category.copyIdAndName(category));
                Set<Category> children = category.getChildren();
                for (Category subCategories: children){
                    String name = "--" + subCategories.getName();
                    listCategoriesUsedInForm().add(Category.copyIdAndName(subCategories.getId(),name));
                    listSubCategoriesUsedInForm(categoriesUsedInForm,subCategories,1);
                }
            }


        }
        return  categoriesUsedInForm;
    }
    private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm,Category parent,int subLevel){
    int newSubLevel = subLevel + 1;
    Set<Category> children = parent.getChildren();
    for (Category subCategories: children){
        String name = "";
        for (int i = 0;i < newSubLevel; i++){

            name += "--";
        }
        name+=subCategories.getName();
        listCategoriesUsedInForm().add(Category.copyIdAndName(subCategories.getId(),name));
        listSubCategoriesUsedInForm(categoriesUsedInForm,subCategories,newSubLevel);
    }
    }

    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return  categoryRepository.findById(id).get();
        }
        catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

}
