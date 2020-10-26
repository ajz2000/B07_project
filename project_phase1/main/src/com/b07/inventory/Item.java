package com.b07.inventory;

import java.math.BigDecimal;

/**
 * Abstraction for item.
 * 
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public interface Item {

  /**
   * Return the item's id.
   * 
   * @return id
   */
  public int getId();

  /**
   * Set item's id.
   * 
   * @param id the id to be set to.
   */
  public void setId(int id);

  /**
   * Return the item's name.
   * 
   * @return name item's name
   */
  public String getName();

  /**
   * Set item's name.
   * 
   * @param name the name to be set to.
   */
  public void setName(String name);

  /**
   * Return the item's price.
   * 
   * @return price
   */
  public BigDecimal getPrice();

  /**
   * Set item's price.
   * 
   * @param price the price to be set to.
   */
  public void setPrice(BigDecimal price);

}
