package com.ecommerce.common.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_iamges")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage() {

    }

    public ProductImage(Product product, String name, Integer id) {
        this.product = product;
        this.name = name;
        this.id = id;
    }

    public ProductImage(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    @Transient
    public String getImagePath() {
        return "/product-images/" + product.getId() + "/extras" +this.name;
    }
}
