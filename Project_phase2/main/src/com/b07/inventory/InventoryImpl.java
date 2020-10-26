package com.b07.inventory;

import java.util.HashMap;

/**
 * Implementation of the inventory interface.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class InventoryImpl implements Inventory {

  private HashMap<Item, Integer> itemMap = new HashMap<Item, Integer>();
  int totalItems;

  /**
   * Return hash-map where the keys are the items and values are the quantities.
   *
   * @return hash-map representing the inventory
   */
  @Override
  public HashMap<Item, Integer> getItemMap() {
    return itemMap;
  }

  /**
   * Set the hash-map that represents the inventory.
   *
   * @param itemMap the hash-map to be set to
   */
  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    this.itemMap = itemMap;

    totalItems = 0;

    for (Integer quantity : itemMap.values()) {
      totalItems += quantity;
    }
  }

  /**
   * Update an item's quantity in inventory or add a new item.
   *
   * @param item the item to be updated.
   * @param value the value of the item to be updated.
   */
  @Override
  public void updateMap(Item item, Integer value) {

    if (itemMap.containsKey(item)) {
      int originalCount = itemMap.get(item);
      totalItems -= originalCount;
    }

    itemMap.put(item, value);
    totalItems += value;

    // TODO: Check if hashCode() and equals() have to be overridden
  }

  /**
   * Return the total number of items in the inventory.
   *
   * @return total number of items in hash-map
   */
  @Override
  public int getTotalItems() {
    return totalItems;
  }

  /**
   * Set the total number of items in the inventory.
   *
   * @param total the total number of items in inventory
   */
  @Override
  public void setTotalItems(int total) {
    int localTotal = 0;

    for (Integer quantity : itemMap.values()) {
      localTotal += quantity;
    }

    if (localTotal == total) {
      totalItems = total;
    }
  }
}
