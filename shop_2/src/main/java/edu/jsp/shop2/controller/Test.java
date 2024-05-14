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
import java.util.Properties;

public class Test {
	static Connection connection = Controller.connection;

	public static void main(String[] args) {
		ResultSet fetchAllProducts = fetchAllProducts();

		try {
			while (fetchAllProducts.next()) {
			   System.out.print(fetchAllProducts.getInt(1));
			   System.out.print(fetchAllProducts.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ResultSet fetchAllProducts() {
		ResultSet resultSet = null;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM product;");
			resultSet = prepareStatement.executeQuery();
			if (!resultSet.next()) {
				return null;
			}
			resultSet.previous();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	public void demo() {
		//Statement execution using executeUodate
		//This method is use to execute non select query.
		try {
		Statement statement  = connection.createStatement();
		statement.executeUpdate("INSERT INTO sample_table VALUES(10,'Kunal');");
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
