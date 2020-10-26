package com.b07.serialize;

import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.store.Coupon;
import com.b07.store.SalesLog;
import com.b07.users.Account;
import com.b07.users.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DataStorage implements Serializable {

  /** Generated SerialID */
  private static final long serialVersionUID = -2513398115449642993L;

  private HashMap<Integer, String> roleIdToRoleNames;
  private ArrayList<User> users;
  private HashMap<Integer, Integer> userToRole;
  private ArrayList<Item> items;
  private Inventory inventory;
  private SalesLog sales;
  private SalesLog itemizedSales;
  private ArrayList<Account> accounts;
  private HashMap<Integer, String> userToHashedPWs;
  private HashMap<Integer, String> discountTypes;
  private HashMap<Integer, Coupon> couponIdsToCoupons;

  /**
   * Makes a object that stores the whole database
   *
   * @param roleIdToRoleNames
   * @param users
   * @param userToRole
   * @param items
   * @param inventory
   * @param sales
   * @param itemizedSales
   * @param accounts
   * @param userToHashedPWs
   * @param discountTypes
   * @param couponIdsToCoupons
   */
  public DataStorage(
      HashMap<Integer, String> roleIdToRoleNames,
      ArrayList<User> users,
      HashMap<Integer, Integer> userToRole,
      ArrayList<Item> items,
      Inventory inventory,
      SalesLog sales,
      SalesLog itemizedSales,
      ArrayList<Account> accounts,
      HashMap<Integer, String> userToHashedPWs,
      HashMap<Integer, String> discountTypes,
      HashMap<Integer, Coupon> couponIdsToCoupons) {
    this.roleIdToRoleNames = roleIdToRoleNames;
    this.users = users;
    this.userToRole = userToRole;
    this.items = items;
    this.inventory = inventory;
    this.sales = sales;
    this.itemizedSales = itemizedSales;
    this.accounts = accounts;
    this.userToHashedPWs = userToHashedPWs;
    this.discountTypes = discountTypes;
    this.couponIdsToCoupons = couponIdsToCoupons;
  }

  /** @return the serialversionuid */
  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  /** @return the roleIdToRoleNames */
  public HashMap<Integer, String> getRoleIdToRoleNames() {
    return roleIdToRoleNames;
  }

  /** @return the users */
  public ArrayList<User> getUsers() {
    return users;
  }

  /** @return the userToRole */
  public HashMap<Integer, Integer> getUserToRole() {
    return userToRole;
  }

  /** @return the items */
  public ArrayList<Item> getItems() {
    return items;
  }

  /** @return the inventory */
  public Inventory getInventory() {
    return inventory;
  }

  /** @return the sales */
  public SalesLog getSales() {
    return sales;
  }

  /** @return the itemizedSales */
  public SalesLog getItemizedSales() {
    return itemizedSales;
  }

  /** @return the accounts */
  public ArrayList<Account> getAccounts() {
    return accounts;
  }

  /** @return the userToHashedPWs */
  public HashMap<Integer, String> getUserToHashedPWs() {
    return userToHashedPWs;
  }

  /** @return the discountTypes */
  public HashMap<Integer, String> getDiscountTypes() {
    return discountTypes;
  }

  /** @return the couponIdsToCoupons */
  public HashMap<Integer, Coupon> getCouponIdsToCoupons() {
    return couponIdsToCoupons;
  }
}
