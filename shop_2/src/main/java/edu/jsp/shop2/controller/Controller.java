package edu.jsp.shop2.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.postgresql.Driver;

import edu.jsp.shop2.model.Product;
import edu.jsp.shop2.model.Shop;

//Controller handles business logic
public class Controller {

	static String url = "jdbc:postgresql://localhost:5432/shop_2";
	static Connection connection = null;

	// Logic for JDBC Step 1,2
	static {
		// Step1: Register Driver
		Driver driver = new Driver();
		try {
			DriverManager.registerDriver(driver);

			// Step2: Establish Connection two argument
			FileInputStream fileInputStream = new FileInputStream("dbconfig.properties");
			Properties properties = new Properties();
			properties.load(fileInputStream);
			connection = DriverManager.getConnection(url, properties);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Method to GET Shop from view and store it in database
	public boolean addShop(Shop shop) {
		try {
			PreparedStatement prepareStatement = connection.prepareStatement("INSERT INTO shop VALUES (?,?,?,?,?,?);");
			prepareStatement.setInt(1, shop.getSid());
			prepareStatement.setString(2, shop.getShopName());
			prepareStatement.setString(3, shop.getAddress());
			prepareStatement.setString(4, shop.getGst());
			prepareStatement.setLong(5, shop.getContact());
			prepareStatement.setString(6, shop.getOwnerName());
			if (prepareStatement.executeUpdate() == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	//isShopExist() methods returns shop details if present.
	//Else it returns null if shop details are not present.
	public Shop isShopExist() {
		Shop shop = null;
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM shop;");
			while (resultSet.next()) {
				shop = new Shop();
				shop.setSid(resultSet.getInt(1));
				shop.setShopName(resultSet.getString(2));
				shop.setAddress(resultSet.getString(3));
				shop.setGst(resultSet.getString(4));
				shop.setContact(resultSet.getLong(5));
				shop.setOwnerName(resultSet.getString(6));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return shop;
	}

	//closeConnection() closes the connection if created
	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	//addProducts() inserts product details into the product table
	//along with productID and shopID details stored in association table shopProduct
	public boolean addProducts(List<Product> products, Shop shop) {
		CallableStatement callableStatement = null;
		int[] difference = new int[products.size()];
		int iteration = 0;
		try {
			callableStatement = connection
					.prepareCall("CALL add_product_maintian_association_shop_product(?,?,?,?,?,?,?,?,?,?);");
			for (Product product : products) {
				callableStatement.setInt(1, product.getPid());
				callableStatement.setString(2, product.getProductName());
				callableStatement.setDouble(3, product.getPrice());
				int quantity = product.getQuantity();
				callableStatement.setInt(4, quantity);
				if (quantity > 0) {
					callableStatement.setBoolean(5, true);
				} else {
					callableStatement.setBoolean(5, product.isAvailability());
				}
				callableStatement.setInt(6, shop.getSid());
				callableStatement.registerOutParameter(7, Types.NUMERIC);
				callableStatement.registerOutParameter(8, Types.NUMERIC);
				callableStatement.registerOutParameter(9, Types.NUMERIC);
				callableStatement.registerOutParameter(10, Types.NUMERIC);
				callableStatement.execute();
				int initialProductCount = callableStatement.getBigDecimal(7).intValue();
				int finalProductCount = callableStatement.getBigDecimal(8).intValue();
				int initialAssociationCount = callableStatement.getBigDecimal(9).intValue();
				int finalAssociationCount = callableStatement.getBigDecimal(10).intValue();
				if ((finalProductCount - initialProductCount == 1)
						&& (finalAssociationCount - initialAssociationCount == 1)) {
					difference[iteration++] = 1;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//verifying whether product details were add successfully in product and 
		//shop_product association table
		for (int i = 0; i < difference.length; i++) {
			if (difference[i] == 0) {
				return false;
			}
		}
		return true;
	}

	//fetchAllProducts() reads the product database
	//and return list of all the products.
	public ResultSet fetchAllProducts() {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product;");
			if (preparedStatement.executeQuery().next()) {
				return preparedStatement.executeQuery();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	//fetchProductById() returns requested product details
	// through product ID(pid)
	public Product fetchProductById(int id) {
		try {
			PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM product WHERE pid = ?;");
			prepareStatement.setInt(1, id);
			ResultSet resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				Product product = new Product();
				product.setPid(resultSet.getInt(1));
				product.setProductName(resultSet.getString(2));
				product.setPrice(resultSet.getDouble(3));
				product.setQuantity(resultSet.getInt(4));
				product.setAvailability(resultSet.getBoolean(5));
				return product;

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	//removeProducts() methods deletes products from the database
	//It contains executeUpdate() JDBC method which return no. of 
	//rows affected. If executeUpdate() return 0 then product remove 
	//is fail. removeProducts() returns id's of products which where
	//failed to remove.
	public ArrayList<Integer> removeProducts(List<Integer> productIDs) {
		ArrayList<Integer> unableToRemove = null;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement("DELETE FROM product WHERE pid = ?;");
			unableToRemove = new ArrayList<Integer>();
			for (Integer id : productIDs) {
				prepareStatement.setInt(1, id);
				if (prepareStatement.executeUpdate() == 0) {
					unableToRemove.add(id);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return unableToRemove;
	}

	//updateProduct() updates product details.
	//If value returned is 0 then product update is failed
	//since no. of rows affected is 0.
	public int updateProduct(Product product) {
		try {
			if(product.getQuantity()>0)
				product.setAvailability(true);
			else
				product.setAvailability(false);
			
			PreparedStatement 
			prepareStatement = connection.prepareStatement("UPDATE product SET product_name = ?, price = ?, quantity = ? , availability = ?"
					+ "WHERE pid = ?;");
			prepareStatement.setString(1, product.getProductName());
			prepareStatement.setDouble(2, product.getPrice());
			prepareStatement.setInt(3, product.getQuantity());
			prepareStatement.setBoolean(4, product.isAvailability());
			prepareStatement.setInt(5, product.getPid());
			return prepareStatement.executeUpdate();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}