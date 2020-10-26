package com.b07.database.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.b07.database.DatabaseDriverAndroid;
import java.math.BigDecimal;

/**
 * An extension of the android databaseDriver which allows public access to it's protected methods.
 *
 * <p>Javadoc omitted as all internal behaviour is specified in DatabaseAndroidHelper
 */
public class DatabaseMethodHelper extends DatabaseDriverAndroid {

  public DatabaseMethodHelper(Context context) {
    super(context);
  }

  // Setters

  public long insertRole(String role) {
    return super.insertRole(role);
  }

  public long insertNewUser(String name, int age, String address, String password) {
    return super.insertNewUser(name, age, address, password);
  }

  public long insertUserRole(int userId, int roleId) {
    return super.insertUserRole(userId, roleId);
  }

  public long insertItem(String name, BigDecimal price) {
    return super.insertItem(name, price);
  }

  public long insertInventory(int itemId, int quantity) {
    return super.insertInventory(itemId, quantity);
  }

  public long insertSale(int userId, BigDecimal totalPrice) {
    return super.insertSale(userId, totalPrice);
  }

  public long insertItemizedSale(int saleId, int itemId, int quantity) {
    return super.insertItemizedSale(saleId, itemId, quantity);
  }

  public long insertAccount(int userId, boolean active) {
    return super.insertAccount(userId, active);
  }

  public long insertAccountLine(int accountId, int itemId, int quantity) {
    return super.insertAccountLine(accountId, itemId, quantity);
  }

  public long insertDiscountType(String type) {
    return super.insertDiscountType(type);
  }

  protected long insertCoupon(int uses, int typeId, int itemId, BigDecimal discount, String code) {
    return super.insertCoupon(uses, typeId, itemId, discount, code);
  }

  // Getters

  public Cursor getRoles() {
    return super.getRoles();
  }

  public String getRole(int id) {
    return super.getRole(id);
  }

  public int getUserRole(int userId) {
    return super.getUserRole(userId);
  }

  public Cursor getUsersByRole(int roleId) {
    return super.getUsersByRole(roleId);
  }

  public Cursor getUsersDetails() {
    return super.getUsersDetails();
  }

  public Cursor getUserDetails(int userId) {
    return super.getUserDetails(userId);
  }

  public String getPassword(int userId) {
    return super.getPassword(userId);
  }

  public Cursor getAllItems() {
    return super.getAllItems();
  }

  public Cursor getItem(int itemId) {
    return super.getItem(itemId);
  }

  public Cursor getInventory() {
    return super.getInventory();
  }

  public int getInventoryQuantity(int itemId) {
    return super.getInventoryQuantity(itemId);
  }

  public Cursor getSales() {
    return super.getSales();
  }

  public Cursor getSaleById(int saleId) {
    return super.getSaleById(saleId);
  }

  public Cursor getSalesToUser(int userId) {
    return super.getSalesToUser(userId);
  }

  public Cursor getItemizedSales() {
    return super.getItemizedSales();
  }

  public Cursor getItemizedSaleById(int saleId) {
    return super.getItemizedSaleById(saleId);
  }

  public Cursor getUserAccounts(int userId) {
    return super.getUserAccounts(userId);
  }

  public Cursor getAccountDetails(int accountId) {
    return super.getAccountDetails(accountId);
  }

  public Cursor getUserActiveAccounts(int userId) {
    return super.getUserActiveAccounts(userId);
  }

  public Cursor getUserInactiveAccounts(int userId) {
    return super.getUserInactiveAccounts(userId);
  }

  public String getDiscountType(int discountTypeId) {
    return super.getDiscountType(discountTypeId);
  }

  public int getCouponDiscountType(int couponId) {
    return super.getCouponDiscountType(couponId);
  }

  public Cursor getDiscountTypeIds() {
    return super.getDiscountTypeIds();
  }

  public int getCouponId(String code) {
    return super.getCouponId(code);
  }

  public String getCouponDiscountAmount(int id) {
    return super.getCouponDiscountAmount(id);
  }

  public int getCouponItemId(int id) {
    return super.getCouponItemId(id);
  }

  public Cursor getCoupons() {
    return super.getCoupons();
  }

  public int getCouponUses(int couponId) {
    return super.getCouponUses(couponId);
  }

  public String getCouponCode(int couponId) {
    return super.getCouponCode(couponId);
  }

  // Updaters

  public boolean updateRoleName(String name, int id) {
    return super.updateRoleName(name, id);
  }

  public boolean updateUserName(String name, int id) {
    return super.updateUserName(name, id);
  }

  public boolean updateUserAge(int age, int id) {
    return super.updateUserAge(age, id);
  }

  public boolean updateUserAddress(String address, int id) {
    return super.updateUserAddress(address, id);
  }

  public boolean updateUserRole(int roleId, int id) {
    return super.updateUserRole(roleId, id);
  }

  public boolean updateItemName(String name, int id) {
    return super.updateItemName(name, id);
  }

  public boolean updateItemPrice(BigDecimal price, int id) {
    return super.updateItemPrice(price, id);
  }

  public boolean updateInventoryQuantity(int quantity, int id) {
    return super.updateInventoryQuantity(quantity, id);
  }

  public boolean updateAccountStatus(int accountId, boolean active) {
    return super.updateAccountStatus(accountId, active);
  }

  public boolean updateCouponUses(int couponId, int uses) {
    return super.updateCouponUses(couponId, uses);
  }

  // Other, android specific.

  public void deleteDatabase() {
    SQLiteDatabase sqLiteDatabase = super.getWritableDatabase();
    super.onUpgrade(sqLiteDatabase, 1, 2);
  }

  public SQLiteDatabase getReadableDatabase() {
    return super.getReadableDatabase();
  }
}
