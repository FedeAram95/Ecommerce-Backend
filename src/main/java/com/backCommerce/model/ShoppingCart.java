package com.backCommerce.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SHOPPING_CART", schema = "SHOPPING")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;


    @Column(name = "PURCHASED_AT")
    private LocalDateTime purchasedAt;

    @OneToMany(
            mappedBy = "shoppingCart",
            cascade = CascadeType.MERGE,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<ShoppingItem> shoppingItems = new ArrayList<>();

}
