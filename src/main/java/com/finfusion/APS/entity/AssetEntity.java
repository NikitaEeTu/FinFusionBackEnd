package com.finfusion.APS.entity;

import com.finfusion.APS.dto.CryptoActivityType;
import com.finfusion.APS.dto.Type;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "assets")
@Data
public class AssetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private CryptoActivityType cryptoActivityType;
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private UserEntity user;
}
