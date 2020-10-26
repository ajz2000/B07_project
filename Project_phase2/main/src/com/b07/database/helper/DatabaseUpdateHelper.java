package com.b07.database.helper;

import com.b07.database.DatabaseUpdater;
import com.b07.exceptions.DatabaseInsertException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A series of helper methods for updating information in the database.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Si
 * @author Payam Yektamaram
 */
public class DatabaseUpdateHelper extends DatabaseUpdater {

  /**
   * Change the name associated with a roleID in the database.
   *
   * @param name the new name to be set.
   * @param id the roleID to be altered.
   * @return true if the name change is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the role name cannot be updated.
   */
  public static boolean updateRoleName(String name, int id)
      throws SQLException, DatabaseInsertException {

    if (name == null || !DatabaseSelectHelper.roleIdExists(id)) {
      throw new DatabaseInsertException();
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateRoleName(name, id, connection);
    connection.close();
    return complete;
  }

  /**
   * Update the name of a user in the database.
   *
   * @param name the new name to be set.
   * @param userId the id of the user to be altered.
   * @return true if the operation is successful
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the user's name cannot be updated.
   */
  public static boolean updateUserName(String name, int userId)
      throws SQLException, DatabaseInsertException {
    if (!DatabaseSelectHelper.userIdExists(userId) || name == null) {
      throw new DatabaseInsertException();
    }
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserName(name, userId, connection);
    connection.close();
    return complete;
  }

  /**
   * Update the age of a user in the database.
   *
   * @param age the new age to be set.
   * @param userId the id of the user to be altered.
   * @return true if the operation is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the user's age cannot be updated
   */
  public static boolean updateUserAge(int age, int userId)
      throws SQLException, DatabaseInsertException {

    if (!DatabaseSelectHelper.userIdExists(userId) || age < 0) {
      throw new DatabaseInsertException();
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserAge(age, userId, connection);
    connection.close();
    return complete;
  }

  /**
   * Update the address of a user in the database.
   *
   * @param address the new address to be set.
   * @param userId the id of the user to be altered.
   * @return true if the operation is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the user's address cannot be updated.
   */
  public static boolean updateUserAddress(String address, int userId)
      throws SQLException, DatabaseInsertException {

    if (!DatabaseSelectHelper.userIdExists(userId) || address.length() > 100) {
      throw new DatabaseInsertException();
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserAddress(address, userId, connection);
    connection.close();
    return complete;
  }

  /**
   * Update the role of a user in the database.
   *
   * @param roleId the id of the new role to be set.
   * @param userId the id of the user to be altered.
   * @return true if the operation is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the user's role cannot be updated.
   */
  public static boolean updateUserRole(int roleId, int userId)
      throws SQLException, DatabaseInsertException {
    if (!DatabaseSelectHelper.userIdExists(userId) || !DatabaseSelectHelper.roleIdExists(roleId)) {
      throw new DatabaseInsertException();
    }
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserRole(roleId, userId, connection);
    connection.close();
    return complete;
  }

  /**
   * Update the name of an item in the database.
   *
   * @param name the new name to be set.
   * @param itemId the id of the item to be altered.
   * @return true if the operation is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the items name cannot be updated.
   */
  public static boolean updateItemName(String name, int itemId)
      throws SQLException, DatabaseInsertException {
    if (name == null || name.length() > 64 || !DatabaseSelectHelper.itemExists(itemId)) {
      throw new DatabaseInsertException();
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateItemName(name, itemId, connection);
    connection.close();
    return complete;
  }

  /**
   * Update an item's price in the database.
   *
   * @param price the new price to be set.
   * @param itemId the id of the item to be updated.
   * @return true if the operation is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the item's price cannot be updated.
   */
  public static boolean updateItemPrice(BigDecimal price, int itemId)
      throws SQLException, DatabaseInsertException {

    if (Math.max(0, price.scale()) != 2
        || !DatabaseSelectHelper.itemExists(itemId)
        || price.compareTo(BigDecimal.ZERO) < 0) {
      throw new DatabaseInsertException();
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateItemPrice(price, itemId, connection);
    connection.close();
    return complete;
  }

  /**
   * Update an item's quantity in the database.
   *
   * @param quantity the new quantity to be set.
   * @param itemId the id of the item who's quantity will be altered.
   * @return true if the operation is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the inventory quantity cannot be updated.
   */
  public static boolean updateInventoryQuantity(int quantity, int itemId)
      throws SQLException, DatabaseInsertException {

    if (!DatabaseSelectHelper.itemExists(itemId) || quantity < 0) {
      throw new DatabaseInsertException();
    }

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateInventoryQuantity(quantity, itemId, connection);
    connection.close();
    return complete;
  }
}
