package com.ecommerce.admin.category;

import com.ecommerce.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    @Query("Select c FROM Category c WHERE c.parent.id is NULL")
    public List<Category> findRootCategories(Sort sort);
    @Query("Select c FROM Category c WHERE c.parent.id is NULL")
    public Page<Category> findRootCategories(Pageable pageable);
    public Category findByName(String name);
    public Category findByAlias(String alias);
    public Long countById(Integer id);
    @Query("UPDATE Category c SET c.enabled =?2 WHERE id=?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

    @Query("SELECT c FROM Category c WHERE c.name LIKE=%?1% WHERE id=?1")
    public Page<Category> search (String keyword,Pageable pageable);
}
