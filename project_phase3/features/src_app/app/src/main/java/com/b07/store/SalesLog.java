package com.b07.store;

import com.b07.inventory.Item;
import com.b07.users.User;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * A log to keep track of all transactions.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public interface SalesLog extends Serializable {

  /**
   * Get the log of all the sale transactions.
   *
   * @return list of sales
   */
  public List<Sale> getSales();

  /**
   * Add a sale to the log.
   *
   * @param sale the sale to be added
   */
  public void addSale(Sale sale);

  /**
   * Get the total number of sales.
   *
   * @return total number of sales
   */
  public int getTotalSalesCount();

  /**
   * Get the total value of all the sales.
   *
   * @return total sales
   */
  public BigDecimal getTotalValueOfSales();

  /**
   * Get the number of item sold.
   *
   * @return the sales of the item
   */
  public int getItemSaleQuantity(Item item);

  /**
   * Get a list of sales to a given customer in the log.
   *
   * @return the sales to the customer
   */
  public List<Sale> getSalesToCustomer(User user);

  /**
   * Get a list of customers who have purchased items.
   *
   * @return a list of customers
   */
  public List<User> getCustomers();

  /**
   * Get a list of all the items in sales log.
   *
   * @return list of items.
   */
  public List<Item> getItems();

  /**
   * Get a map of items sold to the quantity of each item sold.
   *
   * @return a map of items to quantity
   */
  public HashMap<Item, Integer> getItemsSaleQuantity();

  /**
   * Get the sales and quantities of items sold.
   *
   * @return a string with the books
   */
  public String viewBooks();
}
