package com.b07.database.helper;

import com.b07.database.DatabaseInserter;
import com.b07.database.helper.DatabaseDriverHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.ItemTypes;
import com.b07.users.Roles;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A series of helper methods for inserting information
 * into the database.
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Si
 * @author Payam Yektamaram
 * 
 */
public class DatabaseInsertHelper extends DatabaseInserter {
  
  /**
   * Add a role to the database.
   * @param name the name of the role.
   * @return the roleID of the role in the database.
   * @throws DatabaseInsertException if the role cannot be inserted into the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static int insertRole(String name) 
      throws DatabaseInsertException, SQLException {
     
    //Check that role name is Roles enum
    boolean valid = false;
    for (Roles role : Roles.values()) {
      if (role.toString().equals(name)) {
        valid = true;
      }
    }
    
    if (!valid || name == null) {
      throw new DatabaseInsertException(); 
    }
    
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int roleId = DatabaseInserter.insertRole(name, connection);
    connection.close();
    return roleId;
  }
  
  /**
   * Add a new user to the database.
   * @param name the user's name.
   * @param age the user's age.
   * @param address the user's address.
   * @param password the user's password.
   * @return the user's userID in the database.
   * @throws DatabaseInsertException if the role cannot be inserted into the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static int insertNewUser(String name, int age, String address, String password) 
      throws DatabaseInsertException, SQLException {
    
    if (age < 0 || name == null || address.length() > 100) {
      throw new DatabaseInsertException();
    }
    
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int userId = DatabaseInserter.insertNewUser(name, age, address, password, connection);
    connection.close();
    return userId;
  }
  
  /**
   * Set a user's role in the database.
   * @param userId the user who's role will be set.
   * @param roleId the role to set.
   * @return the unique user role relationship id.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the user/role combination cannot be inserted into
   *     the database.
   */
  public static int insertUserRole(int userId, int roleId) 
      throws SQLException, DatabaseInsertException {
    
    List<Integer> validRoleIds = DatabaseSelectHelper.getRoleIds();
    List<Integer> validUserIds = DatabaseSelectHelper.getUserIds();
    if (!validRoleIds.contains(roleId) || !validUserIds.contains(userId)) {
      throw new DatabaseInsertException();
    }
    
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int userRoleId = DatabaseInserter.insertUserRole(userId, roleId, connection);
    connection.close();
    return userRoleId;
  }

  /**
   * Insert a new item into the database.
   * @param name the name of the item.
   * @param price the item's price.
   * @return the itemID of the item.
   * @throws DatabaseInsertException if the item cannot be inserted into the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static int insertItem(String name, BigDecimal price) 
      throws DatabaseInsertException, SQLException {
    
    //Check if item is a valid type
    boolean valid = false;
    for (ItemTypes type : ItemTypes.values()) {
      if (type.toString().equals(name)) {
        valid = true;
      }
    }
    
    if (!valid || name == null || name.length() > 64 
        || Math.max(0, price.scale()) != 2 || price.compareTo(BigDecimal.ZERO) < 0) {
      throw new DatabaseInsertException();
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int itemId = DatabaseInserter.insertItem(name, price, connection);
    connection.close();
    return itemId;
  }
  
  /**
   * Insert a quantity of item into the inventory of the database.
   * @param itemId the item to be inserted.
   * @param quantity the quantity to insert.
   * @return The id of the inserted record.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the inventory cannot be inserted into the database.
   */
  public static int insertInventory(int itemId, int quantity) 
      throws SQLException, DatabaseInsertException {
    
    if (!DatabaseSelectHelper.itemExists(itemId) || quantity <= 0) {
      throw new DatabaseInsertException();
    }
    
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int inventoryId = DatabaseInserter.insertInventory(itemId, quantity, connection);
    connection.close();
    return inventoryId;
  }
  
  /**
   * Insert a sale into the database.
   * @param userId the user with which the sale is associated.
   * @param totalPrice the total price of the sale.
   * @return the sale ID of the sale;
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the sale cannot be inserted into the database.
   */
  public static int insertSale(int userId, BigDecimal totalPrice) 
      throws SQLException, DatabaseInsertException {
    
    if (!DatabaseSelectHelper.userIdExists(userId) || totalPrice.compareTo(BigDecimal.ZERO) < 0) {
      throw new DatabaseInsertException();
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int saleId = DatabaseInserter.insertSale(userId, totalPrice, connection);
    connection.close();
    return saleId;
  }

  /**
   * Insert an itemized record for a specific item in a sale into the database.
   * @param saleId the ID of the sale.
   * @param itemId the ID of the item.
   * @param quantity the quantity of the item sold in this sale.
   * @return the id of the inserted record.
   * @throws SQLException if there is an issue communicating with the database..
   * @throws DatabaseInsertException  if the itemized sale cannot be inserted into the database.
   */
  public static int insertItemizedSale(int saleId, int itemId, int quantity) 
      throws SQLException, DatabaseInsertException {
    
    if (!DatabaseSelectHelper.itemExists(itemId) 
        || DatabaseSelectHelper.saleExists(saleId) || quantity < 0) {
      throw new DatabaseInsertException();
    }
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int itemizedId = DatabaseInserter.insertItemizedSale(saleId, itemId, quantity, connection);
    connection.close();
    return itemizedId;
  }

  
}
