package com.b07.inventory;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Implementation of the item interface.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class ItemImpl implements Item, Serializable {

  /** Serial Version ID of ItemImpl. */
  private static final long serialVersionUID = 1L;

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
   *
   * @return the price of item
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * Set the item's id.
   *
   * @param id the new item id
   */
  @Override
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Return the item's name.
   *
   * @return the name of the item
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Set the item's name.
   *
   * @param name the new name
   */
  @Override
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the item's price.
   *
   * @return the price of the item
   */
  @Override
  public BigDecimal getPrice() {
    return price;
  }

  /**
   * Set the item's price.
   *
   * @param price the new price
   */
  @Override
  public void setPrice(BigDecimal price) {
    if (price.compareTo(BigDecimal.ZERO) >= 0) {
      this.price = price;
    }
  }

  /**
   * Overrides equals to compare the Id of two items.
   *
   * @param o the object to compare to
   * @return true if this item had the same ID as the parameter;false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Item)) {
      return false;
    }

    Item toCompare = (Item) o;
    return (toCompare.getId() == id);
  }

  /** Generate a hashcode for an item. */
  @Override
  public int hashCode() {
    return id;
  }
}
