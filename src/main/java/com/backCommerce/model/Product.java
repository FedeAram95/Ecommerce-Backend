package com.backCommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PRODUCT", schema = "PRODUCTS")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STOCK")
    private int stock;
    
    @Column(name = "IMAGE", columnDefinition = "LONGTEXT")
    private String image;
    
    @Column(name = "TAGS")
    private String tags;

    @Column(name = "PRICE")
    private int price;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private Types type;

    /*@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategoryType> categoryTypeRelations;*/

    // @ManyToMany(mappedBy = "products")
    // private List<ShoppingCart> shoppingCarts;

    @Transient
    public Category getCategory(){
        return type != null ? type.getCategory() : null ;
    }

}