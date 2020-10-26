package com.b07.inventory;

import java.util.HashMap;

/**
 * Abstraction for inventory.
 * 
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public interface Inventory {

  /**
   * Return hash-map of items in inventory with the item as the key and quantity as value.
   * 
   * @return inventory hash-map
   */
  public HashMap<Item, Integer> getItemMap();

  /**
   * Set inventory hash-map with the item as the key and quantity as value.
   * 
   * @param itemMap the hash-map to be set to
   */
  public void setItemMap(HashMap<Item, Integer> itemMap);

  /**
   * Update item in hash-map.
   * 
   * @param item the item to be updated.
   * @param value the quantity of the item to be updated.
   */
  public void updateMap(Item item, Integer value);

  /**
   * Return the total number of items in the inventory.
   * 
   * @return total number of items
   */
  public int getTotalItems();

  /**
   * Set the total number of items in the inventory.
   * 
   * @param total the total number of items in inventory
   */
  public void setTotalItems(int total);
}
