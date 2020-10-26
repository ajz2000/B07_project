package com.b07.store;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Item;
import com.b07.users.Customer;
import com.b07.users.User;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class used by the SalesApplication to verify certain account related information.
 *
 * @author Aidan Zorbas
 */
public class AccountHelper {
  /**
   * Check if there is an account associated with a customer.
   *
   * @param userId The id of the user to check the account of.
   * @return true if the user has an account.
   * @throws SQLException if there is an issue communicating with the database.
   */
  protected static boolean customerHasAccount(int userId) throws SQLException {
    List<Integer> accounts = DatabaseSelectHelper.getUserAccountsById(userId);
    if (accounts == null) {
      return false;
    }
    return !accounts.isEmpty();
  }

  /**
   * Check if there are any items stored in a customer's accounts.
   *
   * @param userId the id of the user to check the accounts of.
   * @return true if the user has accounts, and has items in them, false otherwise.
   * @throws SQLException if there is an issue communicating with the database.
   */
  protected static boolean customerHasShoppingCarts(int userId) throws SQLException {
    List<ShoppingCart> carts = new ArrayList<ShoppingCart>();
    List<Integer> ids = DatabaseSelectHelper.getUserAccountsById(userId);
    User user = DatabaseSelectHelper.getUserDetails(userId);
    if (ids == null || user == null || !(user instanceof Customer)) {
      return false;
    }
    for (int i = 0; i < ids.size(); i++) {
      carts.add(DatabaseSelectHelper.getAccountDetails(ids.get(i)));
    }
    for (int j = 0; j < carts.size(); j++) {
      if (carts.get(j) != null) {
        List<Item> items = carts.get(j).getItems();
        if (!items.isEmpty()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Retrieve a cart of the item's stored in ALL of the user's accounts.
   *
   * @param userId the user who's items will be retrieved.
   * @return A shopping cart containing their stored items, null if no such cart can be made.
   * @throws SQLException if there is an issue communicating with the database.
   */
  protected static ShoppingCart retrieveCustomerCart(int userId) throws SQLException {
    List<ShoppingCart> carts = new ArrayList<ShoppingCart>();
    List<Integer> ids = DatabaseSelectHelper.getUserAccountsById(userId);
    User user = DatabaseSelectHelper.getUserDetails(userId);
    if (ids == null || user == null || !(user instanceof Customer)) {
      return null;
    }

    // Populate list with the items stored in all of a user's accounts.
    for (int i = 0; i < ids.size(); i++) {
      carts.add(DatabaseSelectHelper.getAccountDetails(ids.get(i)));
    }
    // Combine all the items into one shopping cart to be returned.
    ShoppingCart combinedCart = new ShoppingCart((Customer) user);
    // For each shopping cart
    for (int j = 0; j < carts.size(); j++) {
      // Get items contained within
      if (carts.get(j) != null) {
        HashMap<Item, Integer> items = carts.get(j).getItemsWithQuantity();
        // Add each item to the combined cart.
        for (Item item : items.keySet()) {
          combinedCart.addItem(item, items.get(item));
        }
      }
    }

    return combinedCart;
  }

  /**
   * Saves a customer's cart to their lowest numbered account.
   *
   * @param userId the user to save the cart of.
   * @param cart the cart of items to be saved.
   * @return true if the operation succeeds, false otherwise.
   * @throws SQLException if there is an issue communicating with the database.
   */
  protected static boolean saveCustomerCart(int userId, ShoppingCart cart) throws SQLException {
    List<Integer> ids = DatabaseSelectHelper.getUserAccountsById(userId);
    if (ids.isEmpty()) {
      return false;
    }
    int account = ids.get(0);
    HashMap<Item, Integer> items = cart.getItemsWithQuantity();
    for (Item item : items.keySet()) {
      try {
        DatabaseInsertHelper.insertAccountLine(account, item.getId(), items.get(item));
      } catch (DatabaseInsertException e) {
        return false;
      }
    }
    return true;
  }
}
