package edu.jsp.shop2.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.jsp.shop2.controller.Controller;
import edu.jsp.shop2.model.Product;
import edu.jsp.shop2.model.Shop;

//DRIVER CLASS
//view method displays presentation logic
public class view {
	static Scanner sc = new Scanner(System.in);
	static Controller controller = new Controller();
	static Shop shop, shopExist = null;
	static {
		//isShopExist method responsible to check if shop details 
		//are present in the database
		shopExist = controller.isShopExist();
		if (shopExist == null) {
			shop = new Shop();
			System.out.println("Welcome to Shop App");
			System.out.print("Enter Shop ID: ");
			shop.setSid(sc.nextInt());
			sc.nextLine();// Try commenting

			System.out.print("Enter Shop Name: ");
			shop.setShopName(sc.nextLine());

			System.out.print("Enter Shop Address: ");
			shop.setAddress(sc.nextLine());

			System.out.print("Enter Shop GST Number");
			shop.setGst(sc.nextLine());

			System.out.print("Enter Shop Contact: ");
			shop.setContact(sc.nextLong());
			sc.nextLine();

			System.out.print("Enter Owner Name:");
			shop.setOwnerName(sc.nextLine());
			System.out.println();

			// call method responsible to create shop
			controller.addShop(shop);
		} 
		else {
			System.out.println("WELCOME BACK TO " + shopExist.getShopName());
			System.out.println("OWNER : " + shopExist.getOwnerName());
			System.out.println("ADDERSS: " + shopExist.getAddress());
			System.out.println("CONTACT: " + shopExist.getContact());
			System.out.println("GST: " + shopExist.getGst());
			System.out.println();
		}
	}

	public static void main(String[] args) {

		do {
			System.out.println("Select Operations to perform: ");
			System.out.println(
					"1. Add Product/s\n2. Remove Product\n3. Update Product\n4. Fetch Product\n5.Fetch All Products\n6.Exit");
			System.out.print("Enter digit respective to desired options: ");
			byte userChoice = sc.nextByte();
			sc.nextLine();

			switch (userChoice) {
			case 1:// Add Product method
				boolean toContinue = true;
				ArrayList<Product> products = new ArrayList<Product>();
				do {
					Product product = new Product();

					System.out.print("Enter Product  id : ");
					product.setPid(sc.nextInt());
					sc.nextLine();
					System.out.print("Enter Product Name : ");
					product.setProductName(sc.nextLine());
					System.out.print("Enter Product Price : ");
					product.setPrice(sc.nextInt());
					sc.nextLine();
					System.out.print("Enter Product Quantity : ");
					product.setQuantity(sc.nextInt());
					sc.nextLine();
					// Availability of product will be decided in controller
					// depending upon products quantity
                  
					products.add(product);
				} while (toContinue("adding product"));

				boolean verifyInsert = true;
				
				//'shop' stores shop details when new retailer run app for first time
				if (shop != null) {
					verifyInsert = controller.addProducts(products, shop);
				} 
				//'shopExist' will be used if retailer's shop exist in database
				else {
					verifyInsert = controller.addProducts(products, shopExist);
				}

				if (verifyInsert) {
					System.out.println("successfully inserted product/s");
				} else {
					System.out.println("Issue while inserting records.");
				}
				break;
			case 2:// Remove Product
				if (printAllProducts()) {
					List<Integer> productIds = new ArrayList<Integer>();
					do {
						System.out.print("Enter ID/s to remove products: ");
						int id = sc.nextInt();
						sc.nextLine();
						productIds.add(id);
					} while (toContinue("adding id to remove product"));
					ArrayList<Integer> productRemoveFailed_List = controller.removeProducts(productIds);
					if (productRemoveFailed_List.size() > 0) {
						for (Integer integer : productRemoveFailed_List) {
							System.out.println("Unable to remove product with id : " + integer);
						}
					}
				} else {
					System.out.println("Nothing to remove.");
				}

				break;
			case 3:// Update Product
				if (printAllProducts()) {
					System.out.println("Enter Product ID to update: ");
					int id = sc.nextInt();
					sc.nextLine();
					//
					Product productToUpdate = new Product();
					productToUpdate.setPid(id);
					System.out.print("Enter Product Name : ");
					productToUpdate.setProductName(sc.nextLine());
					System.out.print("Enter Product Price : ");
					productToUpdate.setPrice(sc.nextInt());
					sc.nextLine();
					System.out.print("Enter Product Quantity : ");
					productToUpdate.setQuantity(sc.nextInt());
					sc.nextLine();
					if (controller.updateProduct(productToUpdate)>0) {
						System.out.println("Product "+id+" updated successfully");
					} else {
                        System.out.println("Product "+id+" does not exist");
					}
				    
				}
				break;
			case 4:// Fetch Particular Product
				Product product = controller.fetchProductById(11);
				System.out.printf("| %-5s | %-15s | %-15s | %-12s | %-12s |%n", "ID", "Product Name", "Price",
						"Quantity", "Availability");
				System.out.printf("| %-5d |", product.getPid());
				System.out.printf(" %-15s |", product.getProductName());
				System.out.printf(" %-15f |", product.getPrice());
				System.out.printf(" %-15d |", product.getQuantity());
				System.out.printf(" %-12b |", product.isAvailability());
				System.out.println();
				break;
			case 5:// Fetch All Product
				printAllProducts();
				break;
			case 6:
				System.out.println("Exitted.");
				// Call method responsible to close the connection
				controller.closeConnection();
				System.exit(0);
				break;

			default:
				System.out.println("Invalid selection.\n");
				break;
			}
		} while (true);

	}

	//printAllProducts() method prints list of all the products and returns
	//true if shop contains some products
	public static boolean printAllProducts() {
		boolean isPresent = false;
		ResultSet resultSet = controller.fetchAllProducts();
		System.out.println();
		if (resultSet != null) {
			isPresent = true;
			try {
				System.out.printf("| %-5s | %-15s | %-15s | %-12s | %-12s |%n", "ID", "Product Name", "Price",
						"Quantity", "Availability");
				while (resultSet.next()) {
					System.out.printf("| %-5d |", resultSet.getInt(1));
					System.out.printf(" %-15s |", resultSet.getString(2));
					System.out.printf(" %-15f |", resultSet.getDouble(3));
					System.out.printf(" %-15d |", resultSet.getInt(4));
					System.out.printf(" %-12b |", resultSet.getBoolean(5));
					System.out.println();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No product available.");
		}
		System.out.println();
		return isPresent;
	}

	//toContinue() helps to create list of similar type based on users choice
	//whether the user want to continue adding items into the list
	public static boolean toContinue(String operation) {
		System.out.print("Continue " + operation + " ? Y/N : ");
		String continueChoice = sc.nextLine();
		if (continueChoice.toLowerCase().charAt(0) == 'n') {
			return false;
		}
		return true;
	}
}
