package com.ecommerce.admin.category;

import com.ecommerce.common.entity.Category;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {
    private CategoryRepository repo;

    @InjectMocks
    private CategoryService service;

    public void testCheckUniqueInNewModeReturnDuplicateName(){
        Integer id = null;
        String name = "Computers";
        String alias = "abc";

        Category category = new Category(id,name,alias);
        Mockito.when(repo.findByName(name)).thenReturn(category);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);
        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateName");
    }


    public void testCheckUniqueInNewModeReturnDuplicateAlias(){
        Integer id = null;
        String name = "NameABC";
        String alias = "computers";

        Category category = new Category(id,name,alias);
        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(category);
        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateAlias");
    }

    public void testCheckUniqueInNewModeReturnOK(){
        Integer id = null;
        String name = "NameABC";
        String alias = "computers";

        Category category = new Category(id,name,alias);
        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);
        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("OK");
    }

    public void testCheckUniqueInEditModeReturnDuplicateName(){
        Integer id = 1;
        String name = "Computers";
        String alias = "abc";

        Category category = new Category(2,name,alias);
        Mockito.when(repo.findByName(name)).thenReturn(category);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);
        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateName");
    }

    public void testCheckUniqueInEditModeReturnDuplicateAlias(){
        Integer id = 1;
        String name = "NameABC";
        String alias = "computers";

        Category category = new Category(2,name,alias);
        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(category);
        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateAlias");
    }
    public void testCheckUniqueInEditModeReturnOK(){
        Integer id = 1;
        String name = "NameABC";
        String alias = "computers";

        Category category = new Category(id,name,alias);
        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(category);
        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("OK");
    }

}
