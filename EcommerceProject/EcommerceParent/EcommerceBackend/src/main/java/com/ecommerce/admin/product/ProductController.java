package com.ecommerce.admin.product;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.admin.category.CategoryService;
import com.ecommerce.admin.security.EcommerceUserDetails;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.Product;
import exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public List<Product> listAll(){
        return listByPage(1,"name","asc",null,0);
    }
    @GetMapping("/products/page/{pageNum}")
    public List<Product> listByPage(@PathVariable("pageNum") int pageNum,
                                    @Param("sortField") String sortField,
                                  @Param("sortDir") String sortDir,
                                  @Param("keyword") String keyword,
                                    @Param("categoryId") Integer categoryId ){

        Page<Product> page = productService.listByPage(pageNum,sortField,sortDir,keyword,categoryId);
        List<Product> listProducts =page.getContent();
        List<Category> listCategories = categoryService.listCategoriesUsedInForm("asc"); // will add that to response
        long startCount = (pageNum -1 ) * productService.PRODUCTS_PER_PAGE +1;
        long endCount = startCount + productService.PRODUCTS_PER_PAGE -1;
        if(endCount > page.getTotalElements()){
            endCount = page.getTotalElements();
        }

        return  listProducts; // will fix soon
    }
    @PostMapping("/products/save")
    public Product saveProduct(Product product,
                               @RequestParam(value = "fileImage") MultipartFile mainImageMultipart,
                               @RequestParam(value = "extraImage") MultipartFile[] extraImageMultiparts,
                               @RequestParam(name = "detailIDs",required = false) String[] detailIDs,
                               @RequestParam(name = "detailNames",required = false) String[] detailNames,
                               @RequestParam(name = "detailValues",required = false) String[] detailValues,
                               @RequestParam(name = "imageIds",required = false) String[] imageIDs,
                               @RequestParam(name = "detailValues",required = false) String[] imageNames,
                               @AuthenticationPrincipal EcommerceUserDetails loggedUser) throws IOException {
            if(loggedUser.hasRole("Salesperson")){
                productService.saveProductPrice(product);
                return product;
            }
            ProductSaveHelper.setMainImage(mainImageMultipart,product);
            ProductSaveHelper.setExistingExtraImageNames(imageIDs,imageNames,product);
            ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts,product);
            ProductSaveHelper.setProductDetails(detailIDs,detailNames,detailValues,product);

            Product savedProduct = productService.save(product);
            ProductSaveHelper.saveUploadedImages(mainImageMultipart,extraImageMultiparts,savedProduct);
            ProductSaveHelper.deleteExtraImagesWereRemovedOnForm(product);
            return  savedProduct;
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id){
            try {
                productService.delete(id);
                String productImageDir = "../product-images/" + id;
                String productExtraImagesDir = "../product-images/" + id + "/extras";
                FileUploadUtil.removeDir(productExtraImagesDir);
                FileUploadUtil.removeDir(productImageDir);
            } catch (ProductNotFoundException e) {
                throw new RuntimeException(e);
            }
            String categoryDir = "...brand-logos/" + id;
            FileUploadUtil.removeDir(categoryDir);
            return "deleted";
        }

        @GetMapping("/products/edit/{id}")
        public Product editProduct(@PathVariable("id") Integer id){
            try {
                Product product = productService.get(id);
                return  product;
            } catch (ProductNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

    @GetMapping("/products/detail/{id}")
    public Product viewDetailProduct(@PathVariable("id") Integer id){
        try {
            Product product = productService.get(id);
            return  product;
        } catch (ProductNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
