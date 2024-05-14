package edu.jsp.shop2.model;

//Product entity 
public class Product {
    private int pid;
    private String productName;
    private double price;
    private int quantity;
    private boolean availability;
    
    public Product() {
		super();
	}
    
	public Product(int pid, String productName, double price, int quantity, boolean availability) {
		super();
		this.pid = pid;
		this.productName = productName;
		this.price = price;
		this.quantity = quantity;
		this.availability = availability;
	}
    
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public boolean isAvailability() {
		return availability;
	}
	public void setAvailability(boolean availability) {
		this.availability = availability;
	}
	
	@Override
	public String toString() {
		return "Product [pid=" + pid + ", productName=" + productName + ", price=" + price + ", quantity=" + quantity
				+ ", availability=" + availability + "]";
	}
    
}
