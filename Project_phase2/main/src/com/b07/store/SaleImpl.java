package com.b07.store;

import com.b07.inventory.Item;
import com.b07.users.User;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Simple Implementation of sale interface.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class SaleImpl implements Sale {

  private int id;
  private User user;
  private BigDecimal totalPrice;
  private HashMap<Item, Integer> itemMap = new HashMap<Item, Integer>();

  /**
   * Constructor for SaleImpl, without an itemMap.
   *
   * @param id the sale ID.
   * @param user the user to whom the sale is assigned.
   * @param totalPrice the total price of the sale.
   */
  public SaleImpl(int id, User user, BigDecimal totalPrice) {
    this.id = id;
    this.user = user;
    this.totalPrice = totalPrice;
  }

  /**
   * Constructor for SaleImpl, with an itemMap.
   *
   * @param id the sale ID.
   * @param user the user to whom the sale is assigned.
   * @param totalPrice the total price of the sale.
   * @param itemMap a hashmap relating items in the sale to their quantity.
   */
  public SaleImpl(int id, User user, BigDecimal totalPrice, HashMap<Item, Integer> itemMap) {
    this.id = id;
    this.user = user;
    this.totalPrice = totalPrice;
    this.itemMap = itemMap;
  }

  /**
   * Return the sale's id.
   *
   * @return id
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * Set the sale's id.
   *
   * @param id the id to be set to.
   */
  @Override
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Return the sale's user.
   *
   * @return user
   */
  @Override
  public User getUser() {
    return user;
  }

  /**
   * Set the sale's user.
   *
   * @param user the user to be set to.
   */
  @Override
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Return total price.
   *
   * @return total price
   */
  @Override
  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  /**
   * Set total price.
   *
   * @param price the price to be set to
   */
  @Override
  public void setTotalPrice(BigDecimal price) {
    this.totalPrice = price;
  }

  /**
   * Return hash-map that maps each item to an integer.
   *
   * @return itemMap
   */
  @Override
  public HashMap<Item, Integer> getItemMap() {
    return itemMap;
  }

  /**
   * Set's the itemMap.
   *
   * @param itemMap the itemMap to be set to.
   */
  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    this.itemMap = itemMap;
  }
}
