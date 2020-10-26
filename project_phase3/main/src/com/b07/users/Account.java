package com.b07.users;

import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Item;
import com.b07.store.ShoppingCart;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * A class representing a user account.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class Account implements Serializable {

  /** Serial Version ID of Account Class. */
  private static final long serialVersionUID = -7781563226375042210L;

  private ShoppingCart cart;
  private int accountId;
  private int userId;
  private boolean active;

  /**
   * Makes an account object
   *
   * @param userId the id of the user
   * @param accountId the id of the account
   * @param active the active status of the account
   */
  public Account(int userId, int accountId, boolean active) {
    this.userId = userId;
    this.accountId = accountId;
    this.active = active;
  }

  /**
   * Get a list of customer's account by userID and stores them in the account object
   *
   * @return true if successful
   * @throws SQLException
   */
  public boolean retrieveCustomerCart() throws SQLException {
    if (!DatabaseHelperAdapter.userIdExists(userId)) {
      return false;
    }
    List<Integer> accountIds = DatabaseHelperAdapter.getUserAccountsById(userId);
    if (accountIds == null || !accountIds.contains(accountId)) {
      return false;
    }

    cart = DatabaseHelperAdapter.getAccountDetails(accountId);
    return true;
  }

  /**
   * Saves a customer's cart to their lowest numbered account.
   *
   * @param userId the user to save the cart of.
   * @param cart the cart of items to be saved.
   * @return true if the operation succeeds, false otherwise.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public boolean saveCustomerCart(ShoppingCart toAdd) throws SQLException {

    if (!DatabaseHelperAdapter.userIdExists(userId)) {
      return false;
    }
    List<Integer> accountIds = DatabaseHelperAdapter.getUserAccountsById(userId);
    if (accountIds == null || !accountIds.contains(accountId)) {
      return false;
    }
    HashMap<Item, Integer> items = toAdd.getItemsWithQuantity();
    for (Item item : items.keySet()) {
      try {
        DatabaseHelperAdapter.insertAccountLine(accountId, item.getId(), items.get(item));
      } catch (DatabaseInsertException e) {
        return false;
      }
    }
    return true;
  }

  /**
   * get the shopping cart of the account
   *
   * @return the shopping cart
   */
  public ShoppingCart getCart() {
    return cart;
  }

  /**
   * get the user's id of the account
   *
   * @return user id
   */
  public int getUserId() {
    return userId;
  }

  /**
   * Get the id of the account
   *
   * @return account id
   */
  public int getAccountId() {
    return accountId;
  }

  /**
   * Get the active status of the account
   *
   * @return true if active, false if not
   */
  public boolean getActiveStatus() {
    return active;
  }

  /**
   * deactivates this account
   *
   * @return true if success, false otherwise
   * @throws SQLException
   */
  public boolean deactivate() throws SQLException {
    try {
      return DatabaseHelperAdapter.updateAccountStatus(userId, accountId, false);
    } catch (DatabaseInsertException e) {
      return false;
    }
  }
}
