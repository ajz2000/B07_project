package com.b07.inventory;

import java.math.BigDecimal;

/**
 * Implementation of the item interface.
 * 
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class ItemImpl implements Item {

  private String name;
  private int id;
  private BigDecimal price;

  /**
   * Constructor for ItemImpl.
   * 
   * @param id the item's ID.
   * @param name the item's name.
   * @param price the item's price.
   */
  public ItemImpl(int id, String name, BigDecimal price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  /**
   * Return the item's price.
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * Set the item's id.
   */
  @Override
  public void setId(int id) {
    this.id = id;

  }

  /**
   * Return the item's name.
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Set the item's name.
   */
  @Override
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the item's price.
   */
  @Override
  public BigDecimal getPrice() {
    return price;
  }

  /**
   * Set the item's price.
   */
  @Override
  public void setPrice(BigDecimal price) {
    if (price.compareTo(BigDecimal.ZERO) >= 0) {
      this.price = price;
    }
  }

}
