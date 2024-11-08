package com.ecommerce.admin.product;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.admin.category.CategoryService;
import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.Product;
import com.ecommerce.common.entity.ProductImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ProductController {
    private static final Logger LOGGER= LoggerFactory.getLogger(ProductController.class);
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
                               @RequestParam("fileImage") MultipartFile mainImageMultipart,
                               @RequestParam("extraImage") MultipartFile[] extraImageMultiparts,
                               @RequestParam(name = "detailIDs",required = false) String[] detailIDs,
                               @RequestParam(name = "detailNames",required = false) String[] detailNames,
                               @RequestParam(name = "detailValues",required = false) String[] detailValues,
                               @RequestParam(name = "imageIds",required = false) String[] imageIDs,
                               @RequestParam(name = "detailValues",required = false) String[] imageNames) throws IOException {

            setMainImage(mainImageMultipart,product);
            setExistingExtraImageNames(imageIDs,imageNames,product);
            setNewExtraImageNames(extraImageMultiparts,product);
            setProductDetails(detailIDs,detailNames,detailValues,product);

            Product savedProduct = productService.save(product);
            saveUploadedImages(mainImageMultipart,extraImageMultiparts,savedProduct);
            deleteExtraImagesWereRemovedOnForm(product);
            return  savedProduct;
    }

    private void deleteExtraImagesWereRemovedOnForm(Product product) {
        String extraImageDir = "../product-images/"+ product.getId() + "/extras";
        Path dirPath = Paths.get(extraImageDir);

        try{
            Files.list(dirPath).forEach(file -> {
                String filename = file.toFile().getName();
                if(!product.cotainsImageName(filename)){
                    try {
                        Files.delete(file);
                        LOGGER.info("deleted extra images: "+filename);
                    }
                    catch (IOException e){
                        LOGGER.error("Could not delete extra image "+ filename);
                    }
                }
            });
        }
        catch (IOException e){
            LOGGER.error("Could not list directory "+ dirPath);
        }
    }

    private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
        if(imageIDs == null || imageIDs.length == 0) return;
        Set<ProductImage> images = new HashSet<>();
        for (int count = 0; count<imageIDs.length; count++){
            Integer id = Integer.parseInt(imageIDs[count]);
            String name = imageNames[count];
            images.add(new ProductImage(product,name,id));
        }
        product.setImages(images);
    }

    private void setProductDetails(String[] detailsIds,String[] detailNames, String[] detailValues, Product product) {
        if(detailNames == null || detailNames.length == 0 ) return;

        for (int count =0; count< detailNames.length; count++){
            String name = detailNames[count];
            String value = detailValues[count];
            Integer id = Integer.parseInt(detailsIds[count]);
            if(id != 0){
                product.addDetail(id,name,value);
            }
            if(!name.isEmpty() && !value.isEmpty()){
                product.addDetail(name,value);
            }
        }
    }

    private void saveUploadedImages(MultipartFile mainImageMultipart,
                                    MultipartFile[] extraImageMultiparts,
                                    Product savedProduct) throws IOException {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            String uploadDir = "/product-images/" + savedProduct.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,mainImageMultipart);
        }

        if(extraImageMultiparts.length > 0 ) {
            String uploadDir = "/product-images/" + savedProduct.getId() + "extras";
            for (MultipartFile multipartFile : extraImageMultiparts) {
                if (multipartFile.isEmpty()) continue;
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            }
                }

    }

    private void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
        if(extraImageMultiparts.length > 0 ) {
            for (MultipartFile multipartFile : extraImageMultiparts){
                if(!multipartFile.isEmpty()){
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    if(!product.cotainsImageName(fileName)) {
                        product.addExtraImage((fileName));
                    }
                }
            }
        }
    }

    private void setMainImage(MultipartFile mainImageMultipart,Product product) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            product.setMainImage(fileName);
        }
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
