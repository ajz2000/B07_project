package com.b07.store;

import com.b07.inventory.Item;
import com.b07.users.User;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Abstraction of Sale object.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public interface Sale extends Serializable {

  /**
   * Return the sale's id.
   *
   * @return id
   */
  public int getId();

  /**
   * Set the sale's id.
   *
   * @param id the id to be set to.
   */
  public void setId(int id);

  /**
   * Return the sale's user.
   *
   * @return user
   */
  public User getUser();

  /**
   * Set the sale's user.
   *
   * @param user the user to be set to.
   */
  public void setUser(User user);

  /**
   * Return total price.
   *
   * @return total price
   */
  public BigDecimal getTotalPrice();

  /**
   * Set total price.
   *
   * @param price the price to be set to
   */
  public void setTotalPrice(BigDecimal price);

  /**
   * Return hash-map that maps each item to an integer.
   *
   * @return itemMap
   */
  public HashMap<Item, Integer> getItemMap();

  /**
   * Set's the itemMap.
   *
   * @param itemMap the itemMap to be set to.
   */
  public void setItemMap(HashMap<Item, Integer> itemMap);
}
