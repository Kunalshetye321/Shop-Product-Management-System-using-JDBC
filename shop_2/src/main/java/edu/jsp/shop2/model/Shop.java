package edu.jsp.shop2.model;

import java.util.List;

//Shop entity
public class Shop {
    private int sid;
    private String shopName;
    private String address;
    private String gst;
    private long contact;
    private String ownerName;
    
    private List<Product> products;

    
    public Shop(int sid, String shopName, String address, String gst, long contact, String ownerName,
			List<Product> products) {
		super();
		this.sid = sid;
		this.shopName = shopName;
		this.address = address;
		this.gst = gst;
		this.contact = contact;
		this.ownerName = ownerName;
		this.products = products;
	}

	public Shop() {
		super();
	}
    
	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGst() {
		return gst;
	}

	public void setGst(String gst) {
		this.gst = gst;
	}

	public long getContact() {
		return contact;
	}

	public void setContact(long contact) {
		this.contact = contact;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Shop [sid=" + sid + ", shopName=" + shopName + ", address=" + address + ", gst=" + gst + ", contact="
				+ contact + ", ownerName=" + ownerName + ", products=" + products + "]";
	}
    
    
}
