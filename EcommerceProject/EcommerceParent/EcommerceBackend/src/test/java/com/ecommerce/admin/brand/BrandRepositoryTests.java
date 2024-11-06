package com.ecommerce.admin.brand;

import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTests {
    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void testCreateBrand1(){
        Category laptops = new Category(6);
        Brand acer = new Brand("Acer");
        acer.getCategories().add(laptops);
        Brand savedBrand = brandRepository.save(acer);
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrand2(){
        Brand samsung = new Brand("Samsung");
        samsung.getCategories().add(new Category((22)));
        samsung.getCategories().add(new Category((32)));
        Brand savedBrand = brandRepository.save(samsung);
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }


    @Test
    public void testFindAll(){
        Iterable<Brand> brands = brandRepository.findAll();
        brands.forEach(System.out::println);
        assertThat(brands).isNotNull();

    }

    @Test
    public void testGetById(){
        Brand brand = brandRepository.findById(1).get();

        assertThat(brand.getName()).isEqualTo("Acer");

    }

    @Test
    public void testDelete(){
        Integer id = 2;
        brandRepository.deleteById(id);
        Optional<Brand> result = brandRepository.findById(id);

        assertThat(result).isEmpty();

    }
}
