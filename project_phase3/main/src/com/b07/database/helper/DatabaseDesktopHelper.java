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

public class DatabaseDesktopHelper implements DatabasePlatformHelper {

  @Override
  public Connection connectOrCreateDataBase() {
    return DatabaseDriverHelper.connectOrCreateDataBase();
  }

  @Override
  public Connection reInitialize() throws ConnectionFailedException {
    return DatabaseDriverHelper.reInitialize();
  }

  @Override
  public int insertRole(String name) throws DatabaseInsertException, SQLException {
    return DatabaseInsertHelper.insertRole(name);
  }

  @Override
  public int insertNewUser(String name, int age, String address, String password)
      throws DatabaseInsertException, SQLException {
    return DatabaseInsertHelper.insertNewUser(name, age, address, password);
  }

  @Override
  public int insertUserRole(int userId, int roleId) throws SQLException, DatabaseInsertException {
    return DatabaseInsertHelper.insertUserRole(userId, roleId);
  }

  @Override
  public int insertItem(String name, BigDecimal price)
      throws DatabaseInsertException, SQLException {
    return DatabaseInsertHelper.insertItem(name, price);
  }

  @Override
  public int insertInventory(int itemId, int quantity)
      throws SQLException, DatabaseInsertException {
    return DatabaseInsertHelper.insertInventory(itemId, quantity);
  }

  @Override
  public int insertSale(int userId, BigDecimal totalPrice)
      throws SQLException, DatabaseInsertException {
    return DatabaseInsertHelper.insertSale(userId, totalPrice);
  }

  @Override
  public int insertItemizedSale(int saleId, int itemId, int quantity)
      throws SQLException, DatabaseInsertException {
    return DatabaseInsertHelper.insertItemizedSale(saleId, itemId, quantity);
  }

  @Override
  public int insertAccount(int userId, boolean active)
      throws DatabaseInsertException, SQLException {
    return DatabaseInsertHelper.insertAccount(userId, active);
  }

  @Override
  public int insertAccountLine(int accountId, int itemId, int quantity)
      throws DatabaseInsertException, SQLException {
    return DatabaseInsertHelper.insertAccountLine(accountId, itemId, quantity);
  }

  @Override
  public int insertDiscountType(String name) throws DatabaseInsertException, SQLException {
    return DatabaseInsertHelper.insertDiscountType(name);
  }

  @Override
  public int insertCoupon(int itemId, int uses, String type, BigDecimal discount, String code)
      throws SQLException, DatabaseInsertException {
    return DatabaseInsertHelper.insertCoupon(itemId, uses, type, discount, code);
  }

  @Override
  public boolean updateRoleName(String name, int id) throws SQLException, DatabaseInsertException {
    return DatabaseUpdateHelper.updateRoleName(name, id);
  }

  @Override
  public boolean updateUserName(String name, int userId)
      throws SQLException, DatabaseInsertException {
    return DatabaseUpdateHelper.updateUserName(name, userId);
  }

  @Override
  public boolean updateUserAge(int age, int userId) throws SQLException, DatabaseInsertException {
    return DatabaseUpdateHelper.updateUserAge(age, userId);
  }

  @Override
  public boolean updateUserAddress(String address, int userId)
      throws SQLException, DatabaseInsertException {
    return DatabaseUpdateHelper.updateUserAddress(address, userId);
  }

  @Override
  public boolean updateUserRole(int roleId, int userId)
      throws SQLException, DatabaseInsertException {
    return DatabaseUpdateHelper.updateUserRole(roleId, userId);
  }

  @Override
  public boolean updateItemName(String name, int itemId)
      throws SQLException, DatabaseInsertException {
    return DatabaseUpdateHelper.updateItemName(name, itemId);
  }

  @Override
  public boolean updateItemPrice(BigDecimal price, int itemId)
      throws SQLException, DatabaseInsertException {
    return DatabaseUpdateHelper.updateItemPrice(price, itemId);
  }

  @Override
  public boolean updateInventoryQuantity(int quantity, int itemId)
      throws SQLException, DatabaseInsertException {
    return DatabaseUpdateHelper.updateInventoryQuantity(quantity, itemId);
  }

  @Override
  public boolean updateCouponUses(int uses, int couponId)
      throws SQLException, DatabaseInsertException {
    return DatabaseUpdateHelper.updateCouponUses(uses, couponId);
  }

  @Override
  public boolean updateAccountStatus(int userId, int accountId, boolean active)
      throws SQLException, DatabaseInsertException {
    return DatabaseUpdateHelper.updateAccountStatus(userId, accountId, active);
  }

  @Override
  public List<Integer> getRoleIds() throws SQLException {
    return DatabaseSelectHelper.getRoleIds();
  }

  @Override
  public String getRoleName(int roleId) throws SQLException {
    return DatabaseSelectHelper.getRoleName(roleId);
  }

  @Override
  public int getUserRoleId(int userId) throws SQLException {
    return DatabaseSelectHelper.getUserRoleId(userId);
  }

  @Override
  public List<Integer> getUsersByRole(int roleId) throws SQLException {
    return DatabaseSelectHelper.getUsersByRole(roleId);
  }

  @Override
  public List<User> getUsersDetails() throws SQLException {
    return DatabaseSelectHelper.getUsersDetails();
  }

  @Override
  public User getUserDetails(int userId) throws SQLException {
    return DatabaseSelectHelper.getUserDetails(userId);
  }

  @Override
  public String getPassword(int userId) throws SQLException {
    return DatabaseSelectHelper.getPassword(userId);
  }

  @Override
  public List<Item> getAllItems() throws SQLException {
    return DatabaseSelectHelper.getAllItems();
  }

  @Override
  public Item getItem(int itemId) throws SQLException {
    return DatabaseSelectHelper.getItem(itemId);
  }

  @Override
  public Inventory getInventory() throws SQLException {
    return DatabaseSelectHelper.getInventory();
  }

  @Override
  public int getInventoryQuantity(int itemId) throws SQLException {
    return DatabaseSelectHelper.getInventoryQuantity(itemId);
  }

  @Override
  public SalesLog getSales() throws SQLException {
    return DatabaseSelectHelper.getSales();
  }

  @Override
  public Sale getSaleById(int saleId) throws SQLException {
    return DatabaseSelectHelper.getSaleById(saleId);
  }

  @Override
  public List<Sale> getSalesToUser(int userId) throws SQLException {
    return DatabaseSelectHelper.getSalesToUser(userId);
  }

  @Override
  public Sale getItemizedSaleById(int saleId) throws SQLException {
    return DatabaseSelectHelper.getItemizedSaleById(saleId);
  }

  @Override
  public SalesLog getItemizedSales() throws SQLException {
    return DatabaseSelectHelper.getItemizedSales();
  }

  @Override
  public List<Integer> getUserIds() throws SQLException {
    return DatabaseSelectHelper.getUserIds();
  }

  @Override
  public boolean itemExists(int itemId) throws SQLException {
    return DatabaseSelectHelper.itemExists(itemId);
  }

  @Override
  public boolean roleIdExists(int roleID) throws SQLException {
    return DatabaseSelectHelper.roleIdExists(roleID);
  }

  @Override
  public boolean userIdExists(int userID) throws SQLException {
    return DatabaseSelectHelper.userIdExists(userID);
  }

  @Override
  public boolean couponIdExists(int userID) throws SQLException {
    return DatabaseSelectHelper.couponIdExists(userID);
  }

  @Override
  public List<Integer> getCouponIds() throws SQLException {
    return DatabaseSelectHelper.getCouponIds();
  }

  @Override
  public int getRoleIdByName(String name) throws SQLException {
    return DatabaseSelectHelper.getRoleIdByName(name);
  }

  @Override
  public boolean saleExists(int saleId) throws SQLException {
    return DatabaseSelectHelper.saleExists(saleId);
  }

  @Override
  public List<Integer> getUserAccountsById(int userId) throws SQLException {
    return DatabaseSelectHelper.getUserAccountsById(userId);
  }

  @Override
  public int getUserIdByAccountId(int accountId) throws SQLException {
    return DatabaseSelectHelper.getUserIdByAccountId(accountId);
  }

  @Override
  public ShoppingCart getAccountDetails(int accountId) throws SQLException {
    return DatabaseSelectHelper.getAccountDetails(accountId);
  }

  @Override
  public List<Integer> getAllAccountIds() throws SQLException {
    return DatabaseSelectHelper.getAllAccountIds();
  }

  @Override
  public int getDiscountTypeIdByName(String type) throws SQLException {
    return DatabaseSelectHelper.getDiscountTypeIdByName(type);
  }

  @Override
  public String getDiscountTypeName(int discountTypeId) throws SQLException {
    return DatabaseSelectHelper.getDiscountTypeName(discountTypeId);
  }

  @Override
  public boolean discountTypeIdExists(int discountTypeId) throws SQLException {
    return DatabaseSelectHelper.discountTypeIdExists(discountTypeId);
  }

  @Override
  public List<Integer> getDiscountTypeIds() throws SQLException {
    return DatabaseSelectHelper.getDiscountTypeIds();
  }

  @Override
  public int getCouponId(String code) throws SQLException {
    return DatabaseSelectHelper.getCouponId(code);
  }

  @Override
  public DiscountTypes getDiscountType(int couponId) throws SQLException {
    return DatabaseSelectHelper.getDiscountType(couponId);
  }

  @Override
  public BigDecimal getDiscountAmount(int couponId) throws SQLException {
    return DatabaseSelectHelper.getDiscountAmount(couponId);
  }

  @Override
  public int getCouponItem(int couponId) throws SQLException {
    return DatabaseSelectHelper.getCouponItem(couponId);
  }

  @Override
  public int getCouponUses(int couponId) throws SQLException {
    return DatabaseSelectHelper.getCouponUses(couponId);
  }

  @Override
  public String getCouponCode(int couponId) throws SQLException {
    return DatabaseSelectHelper.getCouponCode(couponId);
  }

  @Override
  public boolean customerHasAccount(int userId) throws SQLException {
    return DatabaseSelectHelper.customerHasAccount(userId);
  }

  @Override
  public List<Integer> getUserActiveAccounts(int userId) throws SQLException {
    return DatabaseSelectHelper.getUserActiveAccounts(userId);
  }

  @Override
  public List<Integer> getUserInactiveAccounts(int userId) throws SQLException {
    return DatabaseSelectHelper.getUserInactiveAccounts(userId);
  }
}
