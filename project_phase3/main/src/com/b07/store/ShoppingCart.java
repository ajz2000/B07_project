package com.b07.store;

import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Item;
import com.b07.users.Customer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class allowing authenticated users to make purchases from the inventory.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class ShoppingCart implements Serializable {
  /** Serial Version ID of ShoppingCart. */
  private static final long serialVersionUID = 1L;

  private HashMap<Item, Integer> items = new HashMap<Item, Integer>();
  private Customer customer = null;
  private BigDecimal total = new BigDecimal("0.00");
  private static BigDecimal taxRate = new BigDecimal("1.13");
  private ArrayList<String> discountCodes = new ArrayList<String>();
  private HashMap<Item, BigDecimal> itemDiscounts = new HashMap<Item, BigDecimal>();

  /**
   * Create a new shopping cart with associated customer.
   *
   * @param customer the customer to whom the cart belongs.
   */
  public ShoppingCart(Customer customer) {
    // This method used to require user to be logged in
    // A design decision was made to change this behaviour
    this.customer = customer;
  }

  /**
   * Add some quantity of an item to the cart.
   *
   * @param item the item to add.
   * @param quantity the number of that item to add.
   */
  public void addItem(Item item, int quantity) {
    if (item == null) {
      return;
    }
    if (items.containsKey(item)) {
      items.put(item, quantity + items.get(item));
    } else {
      items.put(item, quantity);
    }
    total = calculateCost();
  }

  /**
   * Remove some quantity of an item from the cart.
   *
   * @param item the item to remove.
   * @param quantity the number of that item to remove.
   */
  public boolean removeItem(Item item, int quantity) {
    if (quantity < 0 || items.get(item) == null) {
      return false;
    }
    items.put(item, Math.max(items.getOrDefault(item, 0) - quantity, 0));
    total = calculateCost();
    if (items.get(item) == 0) {
      items.remove(item);
    }
    return true;
  }

  /**
   * Get a list of all items in shopping cart.
   *
   * @return list of items
   */
  public List<Item> getItems() {
    List<Item> allItems = new ArrayList<Item>(items.keySet());
    return allItems;
  }

  /**
   * Get a hashmap of items mapped to its amount in cart.
   *
   * @return a hashmap of item, amount in cart
   */
  public HashMap<Item, Integer> getItemsWithQuantity() {
    return items;
  }

  /**
   * Get the customer.
   *
   * @return the customer
   */
  public Customer getCustomer() {
    return customer;
  }

  /**
   * Get the combined price of all the items in the shopping cart.
   *
   * @return the combined price
   */
  public BigDecimal getTotal() {
    return total;
  }

  /**
   * Get the tax rate.
   *
   * @return the tax rate
   */
  public BigDecimal getTaxRate() {
    return taxRate;
  }

  /** clear the shopping cart. */
  public void clearCart() {
    items.clear();
    total = BigDecimal.ZERO;
    itemDiscounts.clear();
    discountCodes.clear();
  }

  /** remove a coupon code */
  public boolean removeCoupon(String code) {
    boolean success = discountCodes.remove(code);
    total = calculateCost();
    return success;
  }

  /**
   * Apply a coupon code to an item and recalculate its price
   *
   * @param item the item to apply the coupon to
   * @param code the coupon code
   */
  public void applyCoupon(String code) {
    // TODO: add check for whether a given coupon code already exists when adding
    // new code
    try {
      int couponId = DatabaseHelperAdapter.getCouponId(code);
      int itemId = DatabaseHelperAdapter.getCouponItem(couponId);
      Item item = DatabaseHelperAdapter.getItem(itemId);
      BigDecimal price = item.getPrice();
      DiscountTypes type = DatabaseHelperAdapter.getDiscountType(couponId);
      BigDecimal discount = DatabaseHelperAdapter.getDiscountAmount(couponId);
      if (!couponCanBeApplied(code, 1)) {
        return;
      }
      if (discountCodes.contains(code)) {
        System.out.println("This coupon has already been applied");
        return;
      }
      if (type.equals(DiscountTypes.FLAT_RATE)) {
        price = price.subtract(discount);
      } else if (type.equals(DiscountTypes.PERCENTAGE)) {
        price =
            price.multiply(new BigDecimal("100").subtract(discount)).divide(new BigDecimal("100"));
      }
      discountCodes.add(code);

      System.out.println(
          String.format(
              "Original price of item %s: %s%nNew Price: %s",
              item.getName(), item.getPrice(), price.toPlainString()));
      BigDecimal priceChange = item.getPrice().subtract(price);
      itemDiscounts.put(item, priceChange);
      total = calculateCost();
      System.out.println(String.format("Your new total is %s", total.toPlainString()));
    } catch (SQLException e) {
      System.out.println("Unable to find a coupon with this code");
    }
  }

  /**
   * Attempt to check the user's cart out.
   *
   * @return true if successful, false otherwise.
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
          if (DatabaseHelperAdapter.getInventoryQuantity(itemId) < items.get(allItems.get(i))) {
            return false;
          }
        }
        // Try every coupon to make sure that the coupon in the database hasn't been
        // invalidated in the time it took to shop
        for (String code : discountCodes) {
          try {
            // this first check exists so that it can notify the user of issues with the
            // coupon to avoid throwing an exception later
            if (!couponCanBeApplied(code, 1)) {
              System.out.println(String.format("Coupon code %s is no longer valid.", code));
              System.out.println(
                  String.format(
                      "The invalid coupon code has been removed. Please check out again."));
              removeCoupon(code);
              return false;
            }
            int couponId = DatabaseHelperAdapter.getCouponId(code);
            int uses = DatabaseHelperAdapter.getCouponUses(couponId);
            Item item =
                DatabaseHelperAdapter.getItem(DatabaseHelperAdapter.getCouponItem(couponId));
            int itemQuantity = items.getOrDefault(item, 0);
            if (!couponCanBeApplied(code, itemQuantity)) {
              System.out.println(String.format("Coupon code %s is no longer valid.", code));
              System.out.println(
                  String.format(
                      "The invalid coupon code has been removed. Please check out again."));
              removeCoupon(code);
              return false;
            }
            DatabaseHelperAdapter.updateCouponUses(uses - itemQuantity, couponId);
          } catch (DatabaseInsertException e) {
            System.out.println(
                String.format("Unable to update remaining coupon uses for %s", code));
          }
        }

        // Calculate and submit price after tax
        BigDecimal totalPrice = total.multiply(taxRate).setScale(2, RoundingMode.CEILING);
        int saleId;
        saleId = DatabaseHelperAdapter.insertSale(customer.getId(), totalPrice);
        // Update inventory
        int quantity;
        int toRemove;
        for (int i = 0; i < allItems.size(); i++) {
          itemId = allItems.get(i).getId();
          quantity = DatabaseHelperAdapter.getInventoryQuantity(itemId);
          toRemove = items.get(allItems.get(i));
          DatabaseHelperAdapter.updateInventoryQuantity(quantity - toRemove, itemId);
        }
        // Insert Itemized sales
        for (Item item : items.keySet()) {
          // System.out.println("SaleId: " + saleId);
          // System.out.println("item ID: " + item.getId());
          // System.out.println("Item quantity " + items.get(item));
          DatabaseHelperAdapter.insertItemizedSale(saleId, item.getId(), items.get(item));
        }

        clearCart();
      } catch (Exception e) {
        return false;
      }
      return true;
    }
  }

  private boolean couponCanBeApplied(String code, int quantity) throws SQLException {
    int couponId = DatabaseHelperAdapter.getCouponId(code);
    int itemId = DatabaseHelperAdapter.getCouponItem(couponId);
    DatabaseHelperAdapter.getItem(itemId);
    DiscountTypes type = DatabaseHelperAdapter.getDiscountType(couponId);
    BigDecimal discount = DatabaseHelperAdapter.getDiscountAmount(couponId);
    int uses = DatabaseHelperAdapter.getCouponUses(couponId);
    if (uses - quantity < 0) {
      System.out.println("This coupon has already been used the maximum number of times");
      return false;
    }
    //    if (discountCodes.contains(code)) {
    //      System.out.println("This coupon has already been applied");
    //      return false;
    //    }
    if (type == null) {
      System.out.println("Unable to get discount type for this coupon");
      return false;
    }
    if (discount == null) {
      System.out.println("Unable to get discount amount for this coupon");
      return false;
    }
    if (!type.equals(DiscountTypes.FLAT_RATE) && !type.equals(DiscountTypes.PERCENTAGE)) {
      System.out.println("Could not apply discount: unknown discount type.");
      return false;
    }
    return true;
  }

  /** @return the total cost of all the items in the cart */
  private BigDecimal calculateCost() {
    if (items == null) {
      return BigDecimal.ZERO;
    }
    BigDecimal cost = BigDecimal.ZERO;
    for (java.util.Map.Entry<Item, Integer> entry : items.entrySet()) {
      if (entry.getKey() == null) {
        continue;
      }
      BigDecimal quantity = new BigDecimal(entry.getValue().toString());
      Item item = entry.getKey();
      BigDecimal discount = itemDiscounts.getOrDefault(item, BigDecimal.ZERO);
      cost = cost.add(item.getPrice().subtract(discount).multiply(quantity));
    }
    return cost;
  }
}
