package com.ecommerce.admin.brand;

import com.ecommerce.common.entity.Brand;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BrandRepository extends PagingAndSortingRepository<Brand,Integer> {
}
