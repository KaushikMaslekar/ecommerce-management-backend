package com.ecommerce.api.ecommerce_api_manaagement.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "SKU is required") //Stock Keeping Unit
    @Size(max = 100, message = "SKU cannot exceed 100 characters")
    @Column(name = "sku", unique = true, nullable = false)
    private String sku;

    @Size(min = 10, max = 2000, message = "Description must be required and between 10 and 2000 characters")
    @Column(name = "description")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)//10 digits in total, 2 after the decimal point
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "discount price must be positive")
    @Column(name = "discounted_price", precision = 10, scale = 2)
    private BigDecimal discountedPrice;

    @NotNull(message = "Stock Quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Min(value = 0, message = "reserves stock quantity cannot be negative")
    @Column(name = "reserved_stock_quantity", nullable = false)
    private Integer reserverdQuantity = 0;

    @Min(value = 0, message = "reorder level cannot be negative")
    @Column(name = "reorder_level", nullable = false)
    private Integer reorderLevel = 5;

    @Size(max = 100, message = "Brand name cannot exceed 100 characters")
    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "weight")
    private Double weight; // in kilograms

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @DecimalMin(value = "0.0", message = "rating must be positive")
    @DecimalMax(value = "1.0", message = "rating must be less than or equal to 1.0")
    @Column(name = "tax_rate", precision = 3, scale = 2) // e.g., 0.07 for 7%
    private BigDecimal taxRate = BigDecimal.ZERO; // Default to 0% tax

    @Column(name = "created_at", updatable = false)
    private Long createdAt = System.currentTimeMillis();

    @Column(name = "updated_at")
    private Long updatedAt = System.currentTimeMillis();

    @ManyToOne(fetch = FetchType.LAZY) //Many products can belong to one category
    @JoinColumn(name = "category_id", nullable = false) //foreign key column
    @NotNull(message = "Category is required")
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL) //One product can have many cart items
    @JsonBackReference // to avoid infinite recursion during serialization
    private List<CartItem> cartItems;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL) //One product can have many order items
    @JsonBackReference
    private List<OrderItem> orderItems;

    //Constructors
    public Product() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public Product(String name, String sku, String description, BigDecimal price, Integer stockQuantity, Category category) {
        this();
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.category = category;
    }

    @PreUpdate
    public void PreUpdate() {
        this.updatedAt = System.currentTimeMillis();
    }

    //Business methods
    public Integer getAvailableStock() {
        return stockQuantity - reserverdQuantity;
    }

    public BigDecimal getEffectivePrice() {
        return discountedPrice != null ? discountedPrice : price; //return discounted price if available, otherwise return regular price
    }

    public boolean isLowStock() {
        return getAvailableStock() <= reorderLevel; //true if available stock is less than or equal to reorder level
    }

    public boolean isInStock() {
        return getAvailableStock() > 0; //true if available stock is greater than 0
    }

    //Getters and Setters 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getReserverdQuantity() {
        return reserverdQuantity;
    }

    public void setReserverdQuantity(Integer reserverdQuantity) {
        this.reserverdQuantity = reserverdQuantity;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
