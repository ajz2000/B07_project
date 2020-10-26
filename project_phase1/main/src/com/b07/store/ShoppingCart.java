package com.b07.store;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.NotAuthenticatedException;
import com.b07.inventory.Item;
import com.b07.users.Customer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class allowing authenticated users to make purchase from the inventory.
 * 
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class ShoppingCart {

  private HashMap<Item, Integer> items = new HashMap<Item, Integer>();
  private Customer customer = null;
  private BigDecimal total = new BigDecimal("0.00");
  private static final BigDecimal TAXRATE = new BigDecimal("1.13");

  /**
   * Create a new shopping cart with associated customer.
   * 
   * @param customer the customer to whom the cart belongs.
   * @throws NotAuthenticatedException if the customer is not authenticated.
   */
  public ShoppingCart(Customer customer) throws NotAuthenticatedException {
    if (customer.getAuthenticated()) {
      this.customer = customer;
    } else {
      throw new NotAuthenticatedException();
    }
  }

  /**
   * Add some quantity of an item to the cart.
   * 
   * @param item the item to add.
   * @param quantity the number of that item to add.
   */
  public void addItem(Item item, int quantity) {

    if (quantity < 0) {
      return;
    }

    if (items.containsKey(item)) {
      int count = items.get(item);
      items.put(item, count + quantity);
    } else {
      items.put(item, quantity);
    }
    total = total.add(item.getPrice().multiply(new BigDecimal(quantity)));
  }

  /**
   * Remove some quantity of an item from the cart.
   * 
   * @param item the item to remove.
   * @param quantity the number of that item to remove.
   */
  public void removeItem(Item item, int quantity) {
    if (items.containsKey(item)) {
      int count = items.get(item);
      if (count - quantity <= 0) {
        items.remove(item);
        total = total.subtract(item.getPrice().multiply(new BigDecimal(count)));
      } else {
        items.put(item, count - quantity);
        total = total.subtract(item.getPrice().multiply(new BigDecimal(quantity)));
      }
    }
  }

  /**
   * Get the items in the shopping cart.
   * 
   * @return list of items in shopping cart
   */
  public List<Item> getItems() {
    return new ArrayList<Item>(items.keySet());
  }

  /**
   * Retrieve the current customer in the shopping cart.
   * 
   * @return customer
   */
  public Customer getCustomer() {
    return customer;
  }

  /**
   * Get total price of shopping cart.
   * 
   * @return total
   */
  public BigDecimal getTotal() {
    return total;
  }

  /**
   * Return tax rate.
   */
  public BigDecimal getTaxRate() {
    return TAXRATE;
  }

  /**
   * Clear shopping cart.
   */
  public void clearCart() {
    items.clear();
    total = new BigDecimal("0.00");
  }

  /**
   * Attempt to check the user's cart out.
   * 
   * @return true if succesful, false otherwise.
   */
  public boolean checkOutCart() {
    if (customer == null) {
      return false;
    } else {
      try {

        List<Item> allItems = getItems();
        int itemId;

        // Checking if inventory contains required amount of all items
        for (int i = 0; i < allItems.size(); i++) {
          itemId = allItems.get(i).getId();
          if (DatabaseSelectHelper.getInventoryQuantity(itemId) < items.get(allItems.get(i))) {
            return false;
          }
        }

        // Calculate and submit price after tax
        BigDecimal totalPrice = total.multiply(TAXRATE);
        DatabaseInsertHelper.insertSale(customer.getId(), totalPrice);

        // Update inventory
        int quantity;
        int toRemove;
        for (int i = 0; i < allItems.size(); i++) {
          itemId = allItems.get(i).getId();
          quantity = DatabaseSelectHelper.getInventoryQuantity(itemId);
          toRemove = items.get(allItems.get(i));
          DatabaseUpdateHelper.updateInventoryQuantity(quantity - toRemove, itemId);
        }
        clearCart();
      } catch (Exception e) {
        return false;
      }
      return true;
    }
  }

  /**
   * String representation of cart.
   */
  @Override
  public String toString() {
    StringBuilder value = new StringBuilder();
    value.append("\nShopping Cart: ");

    for (Item item : items.keySet()) {
      value.append(item.getName() + "(" + items.get(item) + ") ");
    }

    return value.toString();
  }
}
