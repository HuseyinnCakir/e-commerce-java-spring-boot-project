package com.ecommerce.admin.product;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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

    @GetMapping("/products")
    public List<Product> listAll(){
        return productService.listAll();
    }

    @PostMapping("/products/save")
    public Product saveProduct(Product product,
                               @RequestParam("fileImage") MultipartFile mainImageMultipart,
                               @RequestParam("extraImage") MultipartFile[] extraImageMultiparts,
                               @RequestParam(name = "detailNames",required = false) String[] detailNames,
                               @RequestParam(name = "detailValues",required = false) String[] detailValues) throws IOException {

            setMainImage(mainImageMultipart,product);
            setExtraImageNames(extraImageMultiparts,product);
            setProductDetails(detailNames,detailValues,product);

            Product savedProduct = productService.save(product);
            saveUploadedImages(mainImageMultipart,extraImageMultiparts,savedProduct);

            return  savedProduct;
    }

    private void setProductDetails(String[] detailNames, String[] detailValues, Product product) {
        if(detailNames == null || detailNames.length == 0 ) return;

        for (int count =0; count< detailNames.length; count++){
            String name = detailNames[count];
            String value = detailValues[count];
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

    private void setExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
        if(extraImageMultiparts.length > 0 ) {
            for (MultipartFile multipartFile : extraImageMultiparts){
                if(!multipartFile.isEmpty()){
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    product.addExtraImage((fileName));
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



}
