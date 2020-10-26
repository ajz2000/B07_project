package com.b07.database.helper;

import com.b07.exceptions.ConnectionFailedException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.store.DiscountTypes;
import com.b07.store.Sale;
import com.b07.store.SalesLog;
import com.b07.store.ShoppingCart;
import com.b07.users.User;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DatabaseHelperAdapter {
  private static DatabasePlatformHelper platformHelper;

  public static void setPlatformHelper(DatabasePlatformHelper toSet) {
    DatabaseHelperAdapter.platformHelper = toSet;
  }

  public static Connection connectOrCreateDataBase() {
    return platformHelper.connectOrCreateDataBase();
  }

  public static Connection reInitialize() throws ConnectionFailedException {
    return platformHelper.reInitialize();
  }

  public static int insertRole(String name) throws DatabaseInsertException, SQLException {
    return platformHelper.insertRole(name);
  }

  public static int insertNewUser(String name, int age, String address, String password)
      throws DatabaseInsertException, SQLException {
    return platformHelper.insertNewUser(name, age, address, password);
  }

  public static int insertUserRole(int userId, int roleId)
      throws SQLException, DatabaseInsertException {
    return platformHelper.insertUserRole(userId, roleId);
  }

  public static int insertItem(String name, BigDecimal price)
      throws DatabaseInsertException, SQLException {
    return platformHelper.insertItem(name, price);
  }

  public static int insertInventory(int itemId, int quantity)
      throws SQLException, DatabaseInsertException {
    return platformHelper.insertInventory(itemId, quantity);
  }

  public static int insertSale(int userId, BigDecimal totalPrice)
      throws SQLException, DatabaseInsertException {
    return platformHelper.insertSale(userId, totalPrice);
  }

  public static int insertItemizedSale(int saleId, int itemId, int quantity)
      throws SQLException, DatabaseInsertException {
    return platformHelper.insertItemizedSale(saleId, itemId, quantity);
  }

  public static int insertAccount(int userId, boolean active)
      throws DatabaseInsertException, SQLException {
    return platformHelper.insertAccount(userId, active);
  }

  public static int insertAccountLine(int accountId, int itemId, int quantity)
      throws DatabaseInsertException, SQLException {
    return platformHelper.insertAccountLine(accountId, itemId, quantity);
  }

  public static int insertDiscountType(String name) throws DatabaseInsertException, SQLException {
    return platformHelper.insertDiscountType(name);
  }

  public static int insertCoupon(
      int itemId, int uses, String type, BigDecimal discount, String code)
      throws SQLException, DatabaseInsertException {
    return platformHelper.insertCoupon(itemId, uses, type, discount, code);
  }

  public static boolean updateRoleName(String name, int id)
      throws SQLException, DatabaseInsertException {
    return platformHelper.updateRoleName(name, id);
  }

  public static boolean updateUserName(String name, int userId)
      throws SQLException, DatabaseInsertException {
    return platformHelper.updateUserName(name, userId);
  }

  public static boolean updateUserAge(int age, int userId)
      throws SQLException, DatabaseInsertException {
    return platformHelper.updateUserAge(age, userId);
  }

  public static boolean updateUserAddress(String address, int userId)
      throws SQLException, DatabaseInsertException {
    return platformHelper.updateUserAddress(address, userId);
  }

  public static boolean updateUserRole(int roleId, int userId)
      throws SQLException, DatabaseInsertException {
    return platformHelper.updateUserRole(roleId, userId);
  }

  public static boolean updateItemName(String name, int itemId)
      throws SQLException, DatabaseInsertException {
    return platformHelper.updateItemName(name, itemId);
  }

  public static boolean updateItemPrice(BigDecimal price, int itemId)
      throws SQLException, DatabaseInsertException {
    return platformHelper.updateItemPrice(price, itemId);
  }

  public static boolean updateInventoryQuantity(int quantity, int itemId)
      throws SQLException, DatabaseInsertException {
    return platformHelper.updateInventoryQuantity(quantity, itemId);
  }

  public static boolean updateCouponUses(int uses, int couponId)
      throws SQLException, DatabaseInsertException {
    return platformHelper.updateCouponUses(uses, couponId);
  }

  public static boolean updateAccountStatus(int userId, int accountId, boolean active)
      throws SQLException, DatabaseInsertException {
    return platformHelper.updateAccountStatus(userId, accountId, active);
  }

  public static List<Integer> getRoleIds() throws SQLException {
    return platformHelper.getRoleIds();
  }

  public static String getRoleName(int roleId) throws SQLException {
    return platformHelper.getRoleName(roleId);
  }

  public static int getUserRoleId(int userId) throws SQLException {
    return platformHelper.getUserRoleId(userId);
  }

  public static List<Integer> getUsersByRole(int roleId) throws SQLException {
    return platformHelper.getUsersByRole(roleId);
  }

  public static List<User> getUsersDetails() throws SQLException {
    return platformHelper.getUsersDetails();
  }

  public static User getUserDetails(int userId) throws SQLException {
    return platformHelper.getUserDetails(userId);
  }

  public static String getPassword(int userId) throws SQLException {
    return platformHelper.getPassword(userId);
  }

  public static List<Item> getAllItems() throws SQLException {
    return platformHelper.getAllItems();
  }

  public static Item getItem(int itemId) throws SQLException {
    return platformHelper.getItem(itemId);
  }

  public static Inventory getInventory() throws SQLException {
    return platformHelper.getInventory();
  }

  public static int getInventoryQuantity(int itemId) throws SQLException {
    return platformHelper.getInventoryQuantity(itemId);
  }

  public static SalesLog getSales() throws SQLException {
    return platformHelper.getSales();
  }

  public static Sale getSaleById(int saleId) throws SQLException {
    return platformHelper.getSaleById(saleId);
  }

  public static List<Sale> getSalesToUser(int userId) throws SQLException {
    return platformHelper.getSalesToUser(userId);
  }

  public static Sale getItemizedSaleById(int saleId) throws SQLException {
    return platformHelper.getItemizedSaleById(saleId);
  }

  public static SalesLog getItemizedSales() throws SQLException {
    return platformHelper.getItemizedSales();
  }

  public static List<Integer> getUserIds() throws SQLException {
    return platformHelper.getUserIds();
  }

  public static boolean itemExists(int itemId) throws SQLException {
    return platformHelper.itemExists(itemId);
  }

  public static boolean roleIdExists(int roleID) throws SQLException {
    return platformHelper.roleIdExists(roleID);
  }

  public static boolean userIdExists(int userID) throws SQLException {
    return platformHelper.userIdExists(userID);
  }

  public static boolean couponIdExists(int userID) throws SQLException {
    return platformHelper.couponIdExists(userID);
  }

  public static List<Integer> getCouponIds() throws SQLException {
    return platformHelper.getCouponIds();
  }

  public static int getRoleIdByName(String name) throws SQLException {
    return platformHelper.getRoleIdByName(name);
  }

  public static boolean saleExists(int saleId) throws SQLException {
    return platformHelper.saleExists(saleId);
  }

  public static List<Integer> getUserAccountsById(int userId) throws SQLException {
    return platformHelper.getUserAccountsById(userId);
  }

  public static int getUserIdByAccountId(int accountId) throws SQLException {
    return platformHelper.getUserIdByAccountId(accountId);
  }

  public static ShoppingCart getAccountDetails(int accountId) throws SQLException {
    return platformHelper.getAccountDetails(accountId);
  }

  public static List<Integer> getAllAccountIds() throws SQLException {
    return platformHelper.getAllAccountIds();
  }

  public static int getDiscountTypeIdByName(String type) throws SQLException {
    return platformHelper.getDiscountTypeIdByName(type);
  }

  public static String getDiscountTypeName(int discountTypeId) throws SQLException {
    return platformHelper.getDiscountTypeName(discountTypeId);
  }

  public static boolean discountTypeIdExists(int discountTypeId) throws SQLException {
    return platformHelper.discountTypeIdExists(discountTypeId);
  }

  public static List<Integer> getDiscountTypeIds() throws SQLException {
    return platformHelper.getDiscountTypeIds();
  }

  public static int getCouponId(String code) throws SQLException {
    return platformHelper.getCouponId(code);
  }

  public static DiscountTypes getDiscountType(int couponId) throws SQLException {
    return platformHelper.getDiscountType(couponId);
  }

  public static BigDecimal getDiscountAmount(int couponId) throws SQLException {
    return platformHelper.getDiscountAmount(couponId);
  }

  public static int getCouponItem(int couponId) throws SQLException {
    return platformHelper.getCouponItem(couponId);
  }

  public static int getCouponUses(int couponId) throws SQLException {
    return platformHelper.getCouponUses(couponId);
  }

  public static String getCouponCode(int couponId) throws SQLException {
    return platformHelper.getCouponCode(couponId);
  }

  public static boolean customerHasAccount(int userId) throws SQLException {
    return platformHelper.customerHasAccount(userId);
  }

  public static List<Integer> getUserActiveAccounts(int userId) throws SQLException {
    return platformHelper.getUserActiveAccounts(userId);
  }

  public static List<Integer> getUserInactiveAccounts(int userId) throws SQLException {
    return platformHelper.getUserInactiveAccounts(userId);
  }
}
