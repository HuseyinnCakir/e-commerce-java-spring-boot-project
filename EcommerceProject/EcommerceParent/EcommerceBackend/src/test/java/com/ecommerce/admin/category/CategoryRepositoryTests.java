package com.ecommerce.admin.category;

import com.ecommerce.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreateRootCategory(){
        Category category = new Category("Computers");
        Category savedCategory = categoryRepository.save(category);
        assertThat(savedCategory.getId()).isGreaterThan(0);
    }


    @Test
    public void testCreateSubCategory(){
        Category parent = new Category(1);
        Category subCategory = new Category("Desktops",parent);
        Category savedCategory = categoryRepository.save(subCategory);
        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetCategory(){

        Category category = categoryRepository.findById(1).get();

        Set<Category> children = category.getChildren();
        for (Category subCategory:children ){
            System.out.println(subCategory.getName());
        }
        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testListRootCategories(){
        List<Category> categories = categoryRepository.findRootCategories();
        categories.forEach(cat -> System.out.println(cat.getName()));
    }
}
