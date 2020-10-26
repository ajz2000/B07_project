package com.b07.database.helper;

import com.b07.database.DatabaseSelector;
import com.b07.inventory.Inventory;
import com.b07.inventory.InventoryImpl;
import com.b07.inventory.Item;
import com.b07.inventory.ItemImpl;
import com.b07.store.Sale;
import com.b07.store.SaleImpl;
import com.b07.store.SalesLog;
import com.b07.store.SalesLogImpl;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.Employee;
import com.b07.users.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A series of helper methods for obtaining information from the database.
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Si
 * @author Payam Yektamaram
 */
public class DatabaseSelectHelper extends DatabaseSelector {
  /**
   * Get a list of role IDs from the database.
   * @return a list of roleIDs
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static List<Integer> getRoleIds() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    List<Integer> ids = new ArrayList<Integer>();
    ResultSet results = DatabaseSelector.getRoles(connection);
    while (results.next()) {
      ids.add(results.getInt("ID"));
    }
    results.close();
    connection.close();
    return ids;    
  }
  
  /**
   * Get the name of a role by it's role ID from the database.
   * @param roleId the role ID.
   * @return the name of the role, null if no such role.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static String getRoleName(int roleId) throws SQLException {
    if (!roleIdExists(roleId)) {
      return null;
    }
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    String role = DatabaseSelector.getRole(roleId, connection);
    connection.close();
    return role;
  }
  
  /**
   * Get the role ID of a user.
   * @param userId the user's ID.
   * @return the user's role ID, -1 if no such user.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static int getUserRoleId(int userId) throws SQLException {
    if (!userIdExists(userId)) {
      return -1;
    }
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int roleId = DatabaseSelector.getUserRole(userId, connection);
    connection.close();
    return roleId;
  }
  
  /**
   * Get a list of all users with specific role from the database.
   * @param roleId the role ID of the role.
   * @return a list of all users with this role.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static List<Integer> getUsersByRole(int roleId) 
      throws SQLException {
    List<Integer> userIds = new ArrayList<Integer>();
    if (!roleIdExists(roleId)) {
      return userIds;
    }
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getUsersByRole(roleId, connection);
    
    while (results.next()) {
      userIds.add(results.getInt("USERID"));
    }
    results.close();
    connection.close();
    return userIds;
    
  }
  
  /**
   * get a list of all users in the database.
   * @return a list of all users in the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static List<User> getUsersDetails() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getUsersDetails(connection);
    List<User> users = new ArrayList<User>();
    int id;
    String name;
    int age;
    String address;
    
    while (results.next()) {
      id = results.getInt("ID");
      name = results.getString("NAME");
      age = results.getInt("AGE");
      address = results.getString("ADDRESS");
      User newUser = userById(id, name, age, address);
      if (newUser != null) {
        users.add(newUser);
      }
    }
    results.close();
    connection.close();
    return users;
  }
  
  /**
   * get a user by their user ID.
   * @param userId the user's ID.
   * @return an object representing the user, null if no such user exists.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static User getUserDetails(int userId) throws SQLException {
    if (!userIdExists(userId)) {
      return null;
    }
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getUserDetails(userId, connection);
    int id;
    String name;
    int age;
    String address;
    User newUser = null;
    while (results.next()) {
      id = results.getInt("ID");
      name = results.getString("NAME");
      age = results.getInt("AGE");
      address = results.getString("ADDRESS");
      newUser = userById(id, name, age, address);     
    }
    results.close();
    connection.close();
    return newUser;
  }
  
  /**
   * Get a hashed version of a user's password.
   * @param userId the user's ID.
   * @return a hashed version of the user's password, null if no such user.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static String getPassword(int userId) throws SQLException {
    if (!userIdExists(userId)) {
      return null;
    }
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    String password = DatabaseSelector.getPassword(userId, connection);
    connection.close();
    return password;
  }
  
  /**
   * Get a list of all items in the database.
   * @return a list of all items in the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static List<Item> getAllItems() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getAllItems(connection);
    int id;
    String name;
    BigDecimal price;
    List<Item> items = new ArrayList<Item>();
    while (results.next()) {
      id = results.getInt("ID");
      name = results.getString("NAME");
      price = new BigDecimal(results.getString("PRICE"));
      Item newItem = new ItemImpl(id, name, price);
      items.add(newItem);
    }
    results.close();
    connection.close();
    return items;
  }
  
  /**
   * Get an item from the database by it's ID.
   * @param itemId the item's ID.
   * @return an object representing the item, null if no such item exists.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static Item getItem(int itemId) throws SQLException {
    if (!itemExists(itemId)) {
      return null;
    }
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getItem(itemId, connection);
    int id = 0;
    String name = "";
    BigDecimal price = new BigDecimal((Integer.toString(0)));
    while (results.next()) {
      id = results.getInt("ID");
      name = results.getString("NAME");
      price = new BigDecimal(results.getString("PRICE"));
    }
    results.close();
    connection.close();
    return new ItemImpl(id,name,price);
  }
  
  /**
   * Get the entire inventory of the database.
   * @return the inventory of the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static Inventory getInventory() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getInventory(connection);
    int itemId;
    int quantity;
    Item item;
    Inventory inventory = new InventoryImpl();
    while (results.next()) {
      itemId = results.getInt("ITEMID");
      quantity = results.getInt("QUANTITY");
      item = getItem(itemId);
      if (item != null) {
        inventory.updateMap(item, quantity);  
      }
    }
    results.close();
    connection.close();
    return inventory;
  }
  
  /**
   * Get the quantity of an item stored in the database.
   * @param itemId the item's ID.
   * @return the quantity of the item in the database, -1 if no such item exists.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static int getInventoryQuantity(int itemId) throws SQLException {
    if (!itemExists(itemId)) {
      return -1;
    }
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int quantity = DatabaseSelector.getInventoryQuantity(itemId, connection);
    connection.close();
    return quantity;
  }
  
  /**
   * Get a log of all sales in the database.
   * @return an object containing information of all sales in the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static SalesLog getSales() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getSales(connection);
    
    SalesLog salesLog = new SalesLogImpl();
    Sale sale;
    int saleId;
    int userId;
    User user;
    BigDecimal totalPrice;
    while (results.next()) {
      saleId = results.getInt("ID");
      userId = results.getInt("USERID");
      user = getUserDetails(userId);
      totalPrice = new BigDecimal(results.getString("TOTALPRICE"));
      if (user != null) {
        sale = new SaleImpl(saleId, user, totalPrice);
        salesLog.addSale(sale);
      }
    }
    results.close();
    connection.close();
    return salesLog;
  }
  
  /**
   * Get a sale from the database by it's ID.
   * @param saleId the ID of the sale.
   * @return an object representing the sale, null if no such sale exists.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static Sale getSaleById(int saleId) throws SQLException {
    
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getSaleById(saleId, connection);
    int id;
    int userId;
    BigDecimal totalPrice;
    Sale sale = null;
    while (results.next()) {
      id = results.getInt("ID");
      userId = results.getInt("USERID");
      totalPrice = (new BigDecimal(results.getString("TOTALPRICE")));
      User user = getUserDetails(userId);
      if (user != null) {
        sale = new SaleImpl(id, user, totalPrice);
      }
    }
    results.close();
    connection.close();
    return sale;
  }
  
  /**
   * Get a list of all sales to a certain user.
   * @param userId the user's ID.
   * @return a list of all sales to the user, null if there is no such user.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static List<Sale> getSalesToUser(int userId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelectHelper.getSalesToUser(userId, connection);
    if (!userIdExists(userId)) {
      return null;
    }
    List<Sale> sales = new ArrayList<>();
    int id;
    BigDecimal totalPrice;
    Sale sale = null;
    User user = getUserDetails(userId);
    while (results.next()) {
      id = results.getInt("ID");
      totalPrice = (new BigDecimal(results.getString("TOTALPRICE")));
      sale = new SaleImpl(id, user, totalPrice);
      sales.add(sale);
    }
    results.close();
    connection.close();
    return sales;
  }
  
  /**
   * Return an itemized sale from the database.
   * @param saleId the sale's ID.
   * @return an itemized sale corresponding to the sale, null if there is no such sale.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static Sale getItemizedSaleById(int saleId) throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getItemizedSaleById(saleId, connection);
    
    if (!saleExists(saleId)) {
      return null;
    }
    
    int id;
    int itemId;
    Item item;
    Integer quantity;
    Sale sale = null;
    HashMap<Item, Integer> itemMap = new HashMap<Item, Integer>();
    while (results.next()) {
      id = results.getInt("SALEID");
      if (sale == null) {
        sale = getSaleById(id);
      }
      itemId = results.getInt("ITEMID");
      item = getItem(itemId);
      quantity = results.getInt("QUANTITY");
      if (item != null) {
        itemMap.put(item, quantity);
      }
    }
    
    results.close();
    connection.close();
    sale.setItemMap(itemMap);
    return sale;
  }
  
  /**
   * Get a list of all itemized sales in the database.
   * @return a list of all itemized sales in the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static SalesLog getItemizedSales() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getItemizedSales(connection);
    SalesLog salesLog = new SalesLogImpl();
    Sale sale;
    while (results.next()) {
      sale = getItemizedSaleById(results.getInt("SALEID"));
      if (sale != null) {
        salesLog.addSale(sale);    
      }
    }
    results.close();
    connection.close();
    return salesLog;
  }
  
  //Methods below this point are newly created and not part of the original class
  
  /**
   * Get a list of all user IDs in the database.
   * @return a list of user IDs.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static List<Integer> getUserIds() throws SQLException {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getUsersDetails(connection);
    List<Integer> ids = new ArrayList<>();
    while (results.next()) {
      ids.add(results.getInt("ID"));
    }
    results.close();
    connection.close();
    return ids;
  }
  
  /**
   * Check if an item is in the database.
   * @param itemId the item's ID.
   * @return true if the item is in the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static boolean itemExists(int itemId) throws SQLException {
    List<Item> items = getAllItems();
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i).getId() == itemId) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean roleIdExists(int roleID) throws SQLException {
    List<Integer> validRoleIds = DatabaseSelectHelper.getRoleIds();
    return validRoleIds.contains(roleID);
  }
  
  public static boolean userIdExists(int userID) throws SQLException {
    List<Integer> validUserIds = DatabaseSelectHelper.getUserIds();
    return validUserIds.contains(userID);
  }
  
  /**
   * Creates a new user of the correct type based on their ID.
   * @param id the user's ID.
   * @param name the user's name.
   * @param age the user's age.
   * @param address the user's address
   * @return a user of the correct type, based on their ID, null if no such user exists.
   * @throws SQLException if there is an issue communicating with the database.
   */
  private static User userById(int id, String name, int age, String address) 
      throws SQLException {
    int roleId = getUserRoleId(id);
    String roleName = getRoleName(roleId);
    if (roleName.equals("ADMIN")) {
      return new Admin(id, name, age, address);
    } else if (roleName.equals("EMPLOYEE")) {
      return new Employee(id, name, age, address);

    } else if (roleName.equals("CUSTOMER")) {
      return new Customer(id, name, age, address);
    }
    return null;
  }

  /**
   * Get the Role ID of a role by it's associated name.
   * @param name the name of the role.
   * @return the role's ID, -1 if no such role.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static int getRoleIdByName(String name) throws SQLException {
    List<Integer> ids;
    ids = getRoleIds();  
    for (int i = 0; i < ids.size(); i++) {
      if (getRoleName(ids.get(i)) != null && getRoleName(ids.get(i)).equals(name)) {
        return ids.get(i);
      }
    }
    
    return -1;
  }
 
  /**
   * Checks if a sale exists within the database.
   * @param saleId the id of the sale to check.
   * @return true if the sale is in the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static boolean saleExists(int saleId) throws SQLException {
    
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getSaleById(saleId, connection);

    return results.next();
  }
}
