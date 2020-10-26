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

public interface DatabasePlatformHelper {
  // Driver Helper
  public Connection connectOrCreateDataBase();

  public Connection reInitialize() throws ConnectionFailedException;

  // InsertHelper
  public int insertRole(String name) throws DatabaseInsertException, SQLException;

  public int insertNewUser(String name, int age, String address, String password)
      throws DatabaseInsertException, SQLException;

  public int insertUserRole(int userId, int roleId) throws DatabaseInsertException, SQLException;

  public int insertItem(String name, BigDecimal price) throws DatabaseInsertException, SQLException;

  public int insertInventory(int itemId, int quantity) throws DatabaseInsertException, SQLException;

  public int insertSale(int userId, BigDecimal totalPrice)
      throws DatabaseInsertException, SQLException;

  public int insertItemizedSale(int saleId, int itemId, int quantity)
      throws DatabaseInsertException, SQLException;

  public int insertAccount(int userId, boolean active) throws DatabaseInsertException, SQLException;

  public int insertAccountLine(int accountId, int itemId, int quantity)
      throws DatabaseInsertException, SQLException;

  public int insertDiscountType(String name) throws DatabaseInsertException, SQLException;

  public int insertCoupon(int itemId, int uses, String type, BigDecimal discount, String code)
      throws DatabaseInsertException, SQLException;

  // UpdateHelper
  public boolean updateRoleName(String name, int id) throws DatabaseInsertException, SQLException;

  public boolean updateUserName(String name, int userId)
      throws DatabaseInsertException, SQLException;

  public boolean updateUserAge(int age, int userId) throws DatabaseInsertException, SQLException;

  public boolean updateUserAddress(String address, int userId)
      throws DatabaseInsertException, SQLException;

  public boolean updateUserRole(int roleId, int userId)
      throws DatabaseInsertException, SQLException;

  public boolean updateItemName(String name, int itemId)
      throws DatabaseInsertException, SQLException;

  public boolean updateItemPrice(BigDecimal price, int itemId)
      throws DatabaseInsertException, SQLException;

  public boolean updateInventoryQuantity(int quantity, int itemId)
      throws DatabaseInsertException, SQLException;

  public boolean updateCouponUses(int uses, int couponId)
      throws DatabaseInsertException, SQLException;

  public boolean updateAccountStatus(int userId, int accountId, boolean active)
      throws DatabaseInsertException, SQLException;

  // SelectHelper
  public List<Integer> getRoleIds() throws SQLException;

  public String getRoleName(int roleId) throws SQLException;

  public int getUserRoleId(int userId) throws SQLException;

  public List<Integer> getUsersByRole(int roleId) throws SQLException;

  public List<User> getUsersDetails() throws SQLException;

  public User getUserDetails(int userId) throws SQLException;

  public String getPassword(int userId) throws SQLException;

  public List<Item> getAllItems() throws SQLException;

  public Item getItem(int itemId) throws SQLException;

  public Inventory getInventory() throws SQLException;

  public int getInventoryQuantity(int itemId) throws SQLException;

  public SalesLog getSales() throws SQLException;

  public Sale getSaleById(int saleId) throws SQLException;

  public List<Sale> getSalesToUser(int userId) throws SQLException;

  public Sale getItemizedSaleById(int saleId) throws SQLException;

  public SalesLog getItemizedSales() throws SQLException;

  public List<Integer> getUserIds() throws SQLException;

  public boolean itemExists(int itemId) throws SQLException;

  public boolean roleIdExists(int roleID) throws SQLException;

  public boolean userIdExists(int userID) throws SQLException;

  public boolean couponIdExists(int userID) throws SQLException;

  public List<Integer> getCouponIds() throws SQLException;

  public int getRoleIdByName(String name) throws SQLException;

  public boolean saleExists(int saleId) throws SQLException;

  public List<Integer> getUserAccountsById(int userId) throws SQLException;

  public int getUserIdByAccountId(int accountId) throws SQLException;

  public ShoppingCart getAccountDetails(int accountId) throws SQLException;

  public List<Integer> getAllAccountIds() throws SQLException;

  public int getDiscountTypeIdByName(String type) throws SQLException;

  public String getDiscountTypeName(int discountTypeId) throws SQLException;

  public boolean discountTypeIdExists(int discountTypeId) throws SQLException;

  public List<Integer> getDiscountTypeIds() throws SQLException;

  public int getCouponId(String code) throws SQLException;

  public DiscountTypes getDiscountType(int couponId) throws SQLException;

  public BigDecimal getDiscountAmount(int couponId) throws SQLException;

  public int getCouponItem(int couponId) throws SQLException;

  public int getCouponUses(int couponId) throws SQLException;

  public String getCouponCode(int couponId) throws SQLException;

  public boolean customerHasAccount(int userId) throws SQLException;

  public List<Integer> getUserActiveAccounts(int userId) throws SQLException;

  public List<Integer> getUserInactiveAccounts(int userId) throws SQLException;
}
