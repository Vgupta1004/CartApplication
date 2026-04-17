package com.app.cartapp.model;
import jakarta.persistence.*;
@Entity
@Table(name="products")
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long productId;
	private String productName;
	private String description;
	private String category;
	private double price;
	private String sku;
	public Product() {}
	public Product(	Long productId, String productName, String description, String category, double price, String sku) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.description = description;
		this.category = category;
		this.price = price;
		this.sku = sku;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
}
