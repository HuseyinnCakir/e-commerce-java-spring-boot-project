package com.ecommerce.common.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 50,nullable = false,unique = true)
    private String name;
    @Column(length = 128,nullable = false)
    private String logo;

    @ManyToMany
    @JoinTable(name = "brands_categories",
    joinColumns =@JoinColumn(name = "brand_id"),
    inverseJoinColumns =@JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public Brand(String name) {
        this.name =  name;
        this.logo ="brand-logo.png";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Brand [id="+id + ", name="+ ", categories=" + categories+ "]";
    }

    @Transient
    public String getLogoPath(){
        if(this.id == null) return "/images/image-thumbnail.png";

        return "/brand-logos/" + this.id + "/" + this.logo;
    }
}
