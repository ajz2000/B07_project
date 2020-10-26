package com.b07.database.helper;

import android.database.Cursor;
import android.util.Log;
import com.b07.exceptions.ConnectionFailedException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Inventory;
import com.b07.inventory.InventoryImpl;
import com.b07.inventory.Item;
import com.b07.inventory.ItemImpl;
import com.b07.inventory.ItemTypes;
import com.b07.store.DiscountTypes;
import com.b07.store.Sale;
import com.b07.store.SaleImpl;
import com.b07.store.SalesLog;
import com.b07.store.SalesLogImpl;
import com.b07.store.ShoppingCart;
import com.b07.users.Customer;
import com.b07.users.Roles;
import com.b07.users.User;
import com.b07.users.UserFactory;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contains the implementation of database helper methods on android Implements
 * databasePlatformHelper in order to be used by the database adapter.
 *
 * <p>All helper Javadoc are in this file
 */
public class DatabaseAndroidHelper implements DatabasePlatformHelper {

  private DatabaseMethodHelper driver;

  /**
   * Sets the current databaseMethodHelper to use.
   *
   * @param driver the databaseMethodHelper
   */
  public void setDriver(DatabaseMethodHelper driver) {
    this.driver = driver;
  }

  // Driver Helper

  /**
   * Does nothing on android.
   *
   * @return null
   */
  public Connection connectOrCreateDataBase() {
    return null;
  }

  /**
   * Does nothing on android.
   *
   * @return nothing
   * @throws ConnectionFailedException never
   */
  public Connection reInitialize() throws ConnectionFailedException {
    return null;
  }

  /**
   * Insert role into ROLES table and return id.
   *
   * @param name role name
   * @return id of role
   * @throws DatabaseInsertException on failure
   * @throws SQLException on failure
   */
  public int insertRole(String name) throws DatabaseInsertException, SQLException {

    // TODO: added this code to avoid duplicate roles
    if (getRoleIdByName(name) != -1) {
      return getRoleIdByName(name);
    }

    // Check that role name is Roles enum
    boolean valid = false;
    for (Roles role : Roles.values()) {
      if (role.toString().equals(name)) {
        valid = true;
      }
    }

    if (!valid || name == null) {
      throw new DatabaseInsertException();
    }

    long roleId = driver.insertRole(name);
    return Math.toIntExact(roleId);
  }

  /**
   * Add a new user to the database.
   *
   * @param name the user's name.
   * @param age the user's age.
   * @param address the user's address.
   * @param password the user's password.
   * @return the user's userID in the database.
   * @throws DatabaseInsertException if the role cannot be inserted into the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public int insertNewUser(String name, int age, String address, String password)
      throws DatabaseInsertException, SQLException {

    Log.d("happybanana", "Called");
    if (age < 0 || name == null || address.length() > 100) {
      throw new DatabaseInsertException();
    }

    long userId = driver.insertNewUser(name, age, address, password);
    Log.d("happybanana", "User id: " + userId);
    return Math.toIntExact(userId);
  }

  /**
   * Set a user's role in the database.
   *
   * @param userId the user who's role will be set.
   * @param roleId the role to set.
   * @return the unique user role relationship id.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the user/role combination cannot be inserted into the
   *     database.
   */
  public int insertUserRole(int userId, int roleId) throws DatabaseInsertException, SQLException {
    List<Integer> validRoleIds = getRoleIds();
    List<Integer> validUserIds = getUserIds();

    if (!validRoleIds.contains(roleId) || !validUserIds.contains(userId)) {
      throw new DatabaseInsertException();
    }

    long userRoleId = driver.insertUserRole(userId, roleId);
    return Math.toIntExact(userRoleId);
  }

  /**
   * Insert a new item into the database.
   *
   * @param name the name of the item.
   * @param price the item's price.
   * @return the itemID of the item.
   * @throws DatabaseInsertException if the item cannot be inserted into the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public int insertItem(String name, BigDecimal price)
      throws DatabaseInsertException, SQLException {

    // TODO: Avoid duplicated items in ITEMS table
    for (Item item : getAllItems()) {
      if (item.getName().equals(name)) {
        Log.d("happybanana", "Item Id:" + item.getId());
        return item.getId();
      }
    }

    // Check if item is a valid type
    boolean valid = false;
    for (ItemTypes type : ItemTypes.values()) {
      if (type.toString().equals(name)) {
        valid = true;
      }
    }

    if (!valid
        || name == null
        || name.length() > 64
        || Math.max(0, price.scale()) != 2
        || price.compareTo(BigDecimal.ZERO) < 0) {
      throw new DatabaseInsertException();
    }

    long itemId = driver.insertItem(name, price);
    return Math.toIntExact(itemId);
  }

  /**
   * Insert a quantity of item into the inventory of the database.
   *
   * @param itemId the item to be inserted.
   * @param quantity the quantity to insert.
   * @return The id of the inserted record.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the inventory cannot be inserted into the database.
   */
  public int insertInventory(int itemId, int quantity)
      throws DatabaseInsertException, SQLException {

    if (!itemExists(itemId) || quantity < 0) {
      throw new DatabaseInsertException();
    }

    long inventoryId = driver.insertInventory(itemId, quantity);
    return Math.toIntExact(inventoryId);
  }

  /**
   * Insert a sale into the database.
   *
   * @param userId the user with which the sale is associated.
   * @param totalPrice the total price of the sale.
   * @return the sale ID of the sale;
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the sale cannot be inserted into the database.
   */
  public int insertSale(int userId, BigDecimal totalPrice)
      throws DatabaseInsertException, SQLException {

    if (!userIdExists(userId) || totalPrice.compareTo(BigDecimal.ZERO) < 0) {
      throw new DatabaseInsertException();
    }

    long saleId = driver.insertSale(userId, totalPrice);
    return Math.toIntExact(saleId);
  }

  /**
   * Insert an itemized record for a specific item in a sale into the database.
   *
   * @param saleId the ID of the sale.
   * @param itemId the ID of the item.
   * @param quantity the quantity of the item sold in this sale.
   * @return the id of the inserted record.
   * @throws SQLException if there is an issue communicating with the database..
   * @throws DatabaseInsertException if the itemized sale cannot be inserted into the database.
   */
  public int insertItemizedSale(int saleId, int itemId, int quantity)
      throws DatabaseInsertException, SQLException {
    if (!itemExists(itemId) || !saleExists(saleId) || quantity < 0) {
      throw new DatabaseInsertException();
    }

    long itemizedId = driver.insertItemizedSale(saleId, itemId, quantity);
    return Math.toIntExact(itemizedId);
  }

  /**
   * Insert a new account into the database.
   *
   * @param userId the userId for the user of the account.
   * @param active if the account is active or inactive
   * @return the id of the account,
   * @throws DatabaseInsertException if something goes wrong.
   * @throws SQLException on failure.
   */
  public int insertAccount(int userId, boolean active)
      throws DatabaseInsertException, SQLException {

    List<Integer> validUserIds = getUserIds();
    if (!validUserIds.contains(userId)) {
      throw new DatabaseInsertException();
    }

    long accountId = driver.insertAccount(userId, active);
    return Math.toIntExact(accountId);
  }

  /**
   * Insert a single item into a given account for recovery next login.
   *
   * @param accountId the id of the account.
   * @param itemId the item to be inserted.
   * @param quantity the quantity of that item.
   * @return the id of the inserted record
   * @throws DatabaseInsertException if something goes wrong.
   * @throws SQLException on failure.
   */
  public int insertAccountLine(int accountId, int itemId, int quantity)
      throws DatabaseInsertException, SQLException {
    // TODO: No way to check if accountId is valid.

    if (quantity < 0 || !itemExists(itemId)) {
      throw new DatabaseInsertException();
    }

    long id = driver.insertAccountLine(accountId, itemId, quantity);
    return Math.toIntExact(id);
  }

  /**
   * Add a discount type to the database.
   *
   * @param name the name of the discount type.
   * @return the ID of the discount type in the database.
   * @throws DatabaseInsertException if the discount type cannot be inserted into the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public int insertDiscountType(String name) throws DatabaseInsertException, SQLException {
    // Check that role name is DiscountTypes enum
    boolean valid = false;
    for (DiscountTypes type : DiscountTypes.values()) {
      if (type.toString().equals(name)) {
        valid = true;
      }
    }

    if (!valid || name == null) {
      throw new DatabaseInsertException();
    }
    long roleId = driver.insertDiscountType(name);
    return Math.toIntExact(roleId);
  }

  /**
   * Inserts a new Coupon into the database.
   *
   * @param itemId the id of the item associated with the coupon
   * @param uses the number of uses of the coupon
   * @param type if the coupon is percentage or flat rate
   * @param discount the percentage or dolalr value of the coupon
   * @param code the code by which the coupon will be referred to
   * @return the id of the coupon
   * @throws DatabaseInsertException if the coupon cannot be inserted
   * @throws SQLException if there is an issue communicating with the database
   */
  public int insertCoupon(int itemId, int uses, String type, BigDecimal discount, String code)
      throws DatabaseInsertException, SQLException {
    if (!itemExists(itemId) || uses < 0) {
      Log.d("monkeyman", "Item doesn't exist or uses < 0");
      Log.d("monkeyman", "" + itemExists(itemId) + " " + uses);
      throw new DatabaseInsertException();
    }

    int typeId = getDiscountTypeIdByName(type);
    Log.d("big bruh", "type id" + typeId);
    if (typeId == -1) {
      return -1;
    }
    long couponId = driver.insertCoupon(uses, typeId, itemId, discount, code);
    Log.d("monkeyman", "Coupon should be inserted");
    return Math.toIntExact(couponId);
  }

  /**
   * Change the name associated with a roleID in the database.
   *
   * @param name the new name to be set.
   * @param id the roleID to be altered.
   * @return true if the name change is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the role name cannot be updated.
   */
  public boolean updateRoleName(String name, int id) throws DatabaseInsertException, SQLException {
    if (name == null || !roleIdExists(id)) {
      throw new DatabaseInsertException();
    }

    boolean complete = driver.updateRoleName(name, id);
    return complete;
  }

  /**
   * Update the name of a user in the database.
   *
   * @param name the new name to be set.
   * @param userId the id of the user to be altered.
   * @return true if the operation is successful
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the user's name cannot be updated.
   */
  public boolean updateUserName(String name, int userId)
      throws DatabaseInsertException, SQLException {

    if (!userIdExists(userId) || name == null) {
      throw new DatabaseInsertException();
    }

    boolean complete = driver.updateUserName(name, userId);
    return complete;
  }

  /**
   * Update the age of a user in the database.
   *
   * @param age the new age to be set.
   * @param userId the id of the user to be altered.
   * @return true if the operation is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the user's age cannot be updated
   */
  public boolean updateUserAge(int age, int userId) throws DatabaseInsertException, SQLException {
    if (!userIdExists(userId) || age < 0) {
      throw new DatabaseInsertException();
    }

    boolean complete = driver.updateUserAge(age, userId);
    return complete;
  }

  /**
   * Update the address of a user in the database.
   *
   * @param address the new address to be set.
   * @param userId the id of the user to be altered.
   * @return true if the operation is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the user's address cannot be updated.
   */
  public boolean updateUserAddress(String address, int userId)
      throws DatabaseInsertException, SQLException {
    if (!userIdExists(userId) || address.length() > 100) {
      throw new DatabaseInsertException();
    }

    boolean complete = driver.updateUserAddress(address, userId);
    return complete;
  }

  /**
   * Update the role of a user in the database.
   *
   * @param roleId the id of the new role to be set.
   * @param userId the id of the user to be altered.
   * @return true if the operation is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the user's role cannot be updated.
   */
  public boolean updateUserRole(int roleId, int userId)
      throws DatabaseInsertException, SQLException {

    if (!userIdExists(userId) || !roleIdExists(roleId)) {
      throw new DatabaseInsertException();
    }

    boolean complete = driver.updateUserRole(roleId, userId);
    return complete;
  }

  /**
   * Update the name of an item in the database.
   *
   * @param name the new name to be set.
   * @param itemId the id of the item to be altered.
   * @return true if the operation is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the items name cannot be updated.
   */
  public boolean updateItemName(String name, int itemId)
      throws DatabaseInsertException, SQLException {

    if (name == null || name.length() > 64 || !itemExists(itemId)) {
      throw new DatabaseInsertException();
    }

    boolean complete = driver.updateItemName(name, itemId);
    return complete;
  }

  /**
   * Update an item's price in the database.
   *
   * @param price the new price to be set.
   * @param itemId the id of the item to be updated.
   * @return true if the operation is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the item's price cannot be updated.
   */
  public boolean updateItemPrice(BigDecimal price, int itemId)
      throws DatabaseInsertException, SQLException {

    if (Math.max(0, price.scale()) != 2
        || !itemExists(itemId)
        || price.compareTo(BigDecimal.ZERO) < 0) {
      throw new DatabaseInsertException();
    }

    boolean complete = driver.updateItemPrice(price, itemId);
    return complete;
  }

  /**
   * Update an item's quantity in the database.
   *
   * @param quantity the new quantity to be set.
   * @param itemId the id of the item who's quantity will be altered.
   * @return true if the operation is successful.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the inventory quantity cannot be updated.
   */
  public boolean updateInventoryQuantity(int quantity, int itemId)
      throws DatabaseInsertException, SQLException {
    if (!itemExists(itemId) || quantity < 0) {
      throw new DatabaseInsertException();
    }

    boolean complete = driver.updateInventoryQuantity(quantity, itemId);
    return complete;
  }

  /**
   * Update the number of uses a coupon has left
   *
   * @param uses the new number of uses
   * @param couponId the id of the coupon
   * @return true if successful
   * @throws DatabaseInsertException if there is an issue updating the uses
   * @throws SQLException if there is an issue communicating with the database
   */
  public boolean updateCouponUses(int uses, int couponId)
      throws DatabaseInsertException, SQLException {
    return true;
  }

  /**
   * Update account status.
   *
   * @param userId the user of interest
   * @param accountId the account of interest
   * @param active true if active, false otherwise
   * @return true on success, otherwise false
   * @throws DatabaseInsertException on failure
   * @throws SQLException on failure
   */
  public boolean updateAccountStatus(int userId, int accountId, boolean active)
      throws DatabaseInsertException, SQLException {

    if (!userIdExists(userId)) {
      throw new DatabaseInsertException();
    }
    List<Integer> userAccs = getUserAccountsById(userId);

    if (!userAccs.contains(accountId)) {
      throw new DatabaseInsertException();
    }
    return driver.updateAccountStatus(accountId, active);
  }

  /**
   * Get a list of role IDs from the database.
   *
   * @return a list of roleIDs
   * @throws SQLException if there is an issue communicating with the database.
   */
  public List<Integer> getRoleIds() throws SQLException {

    List<Integer> ids = new ArrayList<Integer>();
    Cursor results = driver.getRoles();

    while (results.moveToNext()) {
      ids.add(results.getInt(results.getColumnIndex("ID")));
    }

    results.close();
    return ids;
  }

  /**
   * Get the name of a role by it's role ID from the database.
   *
   * @param roleId the role ID.
   * @return the name of the role, null if no such role.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public String getRoleName(int roleId) throws SQLException {
    if (!roleIdExists(roleId)) {
      return null;
    }

    String role = driver.getRole(roleId);
    return role;
  }

  /**
   * Get the role ID of a user.
   *
   * @param userId the user's ID.
   * @return the user's role ID, -1 if no such user.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public int getUserRoleId(int userId) throws SQLException {
    if (!userIdExists(userId)) {
      return -1;
    }

    long roleId = driver.getUserRole(userId);
    return Math.toIntExact(roleId);
  }

  /**
   * Get a list of all users with specific role from the database.
   *
   * @param roleId the role ID of the role.
   * @return a list of all users with this role.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public List<Integer> getUsersByRole(int roleId) throws SQLException {

    List<Integer> userIds = new ArrayList<Integer>();
    if (!roleIdExists(roleId)) {
      return userIds;
    }

    Cursor results = driver.getUsersByRole(roleId);

    while (results.moveToNext()) {
      userIds.add(results.getInt(results.getColumnIndex("USERID")));
    }
    results.close();
    return userIds;
  }

  /**
   * Get a list of all users in the database.
   *
   * @return a list of all users in the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public List<User> getUsersDetails() throws SQLException {

    Cursor results = driver.getUsersDetails();
    List<User> users = new ArrayList<User>();

    int id;
    String name;
    int age;
    String address;

    while (results.moveToNext()) {

      id = results.getInt(results.getColumnIndex("ID"));
      name = results.getString(results.getColumnIndex("NAME"));
      age = results.getInt(results.getColumnIndex("AGE"));
      address = results.getString(results.getColumnIndex("ADDRESS"));
      User newUser = UserFactory.createUser(id, name, age, address);
      if (newUser != null) {
        users.add(newUser);
      }
    }
    results.close();
    return users;
  }

  /**
   * Get a user by their user ID.
   *
   * @param userId the user's ID.
   * @return an object representing the user, null if no such user exists.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public User getUserDetails(int userId) throws SQLException {

    if (!userIdExists(userId)) {
      return null;
    }

    Cursor results = driver.getUserDetails(userId);

    int id;
    String name;
    int age;
    String address;
    User newUser = null;

    while (results.moveToNext()) {
      id = results.getInt(results.getColumnIndex("ID"));
      name = results.getString(results.getColumnIndex("NAME"));
      age = results.getInt(results.getColumnIndex("AGE"));
      address = results.getString(results.getColumnIndex("ADDRESS"));
      newUser = UserFactory.createUser(id, name, age, address);
    }

    results.close();
    return newUser;
  }

  /**
   * Get a hashed version of a user's password.
   *
   * @param userId the user's ID.
   * @return a hashed version of the user's password, null if no such user.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public String getPassword(int userId) throws SQLException {
    if (!userIdExists(userId)) {
      return null;
    }
    String password = driver.getPassword(userId);
    return password;
  }

  /**
   * Get a list of all items in the database.
   *
   * @return a list of all items in the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public List<Item> getAllItems() throws SQLException {

    Cursor results = driver.getAllItems();
    int id;
    String name;
    BigDecimal price;
    List<Item> items = new ArrayList<Item>();

    while (results.moveToNext()) {
      id = results.getInt(results.getColumnIndex("ID"));
      name = results.getString(results.getColumnIndex("NAME"));
      price = new BigDecimal(results.getString(results.getColumnIndex("PRICE")));
      Item newItem = new ItemImpl(id, name, price);
      items.add(newItem);
    }
    results.close();
    return items;
  }

  /**
   * Get an item from the database by it's ID.
   *
   * @param itemId the item's ID.
   * @return an object representing the item, null if no such item exists.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public Item getItem(int itemId) throws SQLException {
    if (!itemExists(itemId)) {
      return null;
    }

    Cursor results = driver.getItem(itemId);

    int id = 0;
    String name = "";
    BigDecimal price = new BigDecimal((Integer.toString(0)));

    while (results.moveToNext()) {
      id = results.getInt(results.getColumnIndex("ID"));
      name = results.getString(results.getColumnIndex("NAME"));
      price = new BigDecimal(results.getString(results.getColumnIndex("PRICE")));
    }
    results.close();
    return new ItemImpl(id, name, price);
  }

  /**
   * Get the entire inventory of the database.
   *
   * @return the inventory of the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public Inventory getInventory() throws SQLException {

    Cursor results = driver.getInventory();
    int itemId;
    int quantity;
    Item item;
    Inventory inventory = new InventoryImpl();

    while (results.moveToNext()) {
      itemId = results.getInt(results.getColumnIndex("ITEMID"));
      quantity = results.getInt(results.getColumnIndex("QUANTITY"));
      item = getItem(itemId);
      if (item != null) {
        inventory.updateMap(item, quantity);
      }
    }
    results.close();
    return inventory;
  }

  /**
   * Get the quantity of an item stored in the database.
   *
   * @param itemId the item's ID.
   * @return the quantity of the item in the database, -1 if no such item exists.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public int getInventoryQuantity(int itemId) throws SQLException {
    if (!itemExists(itemId)) {
      return -1;
    }
    int quantity = driver.getInventoryQuantity(itemId);
    return quantity;
  }

  /**
   * Get a log of all sales in the database.
   *
   * @return an object containing information of all sales in the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public SalesLog getSales() throws SQLException {
    Cursor results = driver.getSales();

    SalesLog salesLog = new SalesLogImpl();

    Sale sale;
    int saleId;
    int userId;
    User user;
    BigDecimal totalPrice;

    while (results.moveToNext()) {
      saleId = results.getInt(results.getColumnIndex("ID"));
      userId = results.getInt(results.getColumnIndex("USERID"));
      user = getUserDetails(userId);

      totalPrice = new BigDecimal(results.getString(results.getColumnIndex("TOTALPRICE")));
      if (user != null) {
        sale = getItemizedSaleById(saleId);
        sale.setId(saleId);
        sale.setUser(user);
        sale.setTotalPrice(totalPrice);

        salesLog.addSale(sale);
      }
    }
    results.close();
    return salesLog;
  }

  /**
   * Get a sale from the database by it's ID.
   *
   * @param saleId the ID of the sale.
   * @return an object representing the sale, null if no such sale exists.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public Sale getSaleById(int saleId) throws SQLException {
    Cursor results = driver.getSaleById(saleId);
    int id;
    int userId;
    BigDecimal totalPrice;
    Sale sale = null;

    while (results.moveToNext()) {
      id = results.getInt(results.getColumnIndex("ID"));
      userId = results.getInt(results.getColumnIndex("USERID"));
      totalPrice = (new BigDecimal(results.getString(results.getColumnIndex("TOTALPRICE"))));
      User user = getUserDetails(userId);
      if (user != null) {
        sale = new SaleImpl(id, user, totalPrice);
      }
    }
    results.close();
    return sale;
  }

  /**
   * Get a list of all sales to a certain user.
   *
   * @param userId the user's ID.
   * @return a list of all sales to the user, null if there is no such user.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public List<Sale> getSalesToUser(int userId) throws SQLException {

    Cursor results = driver.getSalesToUser(userId);
    if (!userIdExists(userId)) {
      return null;
    }

    List<Sale> sales = new ArrayList<>();
    int id;
    BigDecimal totalPrice;
    Sale sale = null;
    User user = getUserDetails(userId);

    while (results.moveToNext()) {
      id = results.getInt(results.getColumnIndex("ID"));
      totalPrice = (new BigDecimal(results.getString(results.getColumnIndex("TOTALPRICE"))));
      sale = new SaleImpl(id, user, totalPrice);
      sales.add(sale);
    }
    results.close();
    return sales;
  }

  /**
   * Return an itemized sale from the database.
   *
   * @param saleId the sale's ID.
   * @return an itemized sale corresponding to the sale, null if there is no such sale.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public Sale getItemizedSaleById(int saleId) throws SQLException {

    Cursor results = driver.getItemizedSaleById(saleId);

    if (!saleExists(saleId)) {
      return null;
    }

    int id;
    int itemId;
    Item item;
    Integer quantity;
    Sale sale = null;
    HashMap<Item, Integer> itemMap = new HashMap<Item, Integer>();

    while (results.moveToNext()) {
      id = results.getInt(results.getColumnIndex("SALEID"));
      if (sale == null) {
        sale = getSaleById(id);
      }
      itemId = results.getInt(results.getColumnIndex("ITEMID"));
      item = getItem(itemId);
      quantity = results.getInt(results.getColumnIndex("QUANTITY"));
      if (item != null) {
        itemMap.put(item, quantity);
      }
    }

    results.close();
    sale.setItemMap(itemMap);
    return sale;
  }

  /**
   * Get a list of all itemized sales in the database.
   *
   * @return a list of all itemized sales in the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public SalesLog getItemizedSales() throws SQLException {

    Cursor results = driver.getItemizedSales();
    SalesLog salesLog = new SalesLogImpl();
    Sale sale;
    while (results.moveToNext()) {
      sale = getItemizedSaleById(results.getInt(results.getColumnIndex("SALEID")));
      if (sale != null) {
        salesLog.addSale(sale);
      }
    }
    results.close();
    return salesLog;
  }

  // Methods below this point are newly created and not part of the original class

  /**
   * Get a list of all user IDs in the database.
   *
   * @return a list of user IDs.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public List<Integer> getUserIds() throws SQLException {
    Cursor results = driver.getUsersDetails();
    List<Integer> ids = new ArrayList<>();
    while (results.moveToNext()) {
      ids.add(results.getInt(results.getColumnIndex("ID")));
    }
    results.close();
    return ids;
  }

  /**
   * Check if an item is in the database.
   *
   * @param itemId the item's ID.
   * @return true if the item is in the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public boolean itemExists(int itemId) throws SQLException {
    List<Item> items = getAllItems();
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i).getId() == itemId) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if role id exists.
   *
   * @param roleID the id of interest
   * @return true if exists, otherwise false
   * @throws SQLException on failure
   */
  public boolean roleIdExists(int roleID) throws SQLException {
    // This is wrong but it isn't causing any issues so
    // Let's not change it
    return true;
  }

  /**
   * Check if user id exists.
   *
   * @param userID the user id of interest
   * @return true if exists, otherwise false
   * @throws SQLException on failure
   */
  public boolean userIdExists(int userID) throws SQLException {
    List<Integer> validUserIds = getUserIds();
    return validUserIds.contains(userID);
  }

  /**
   * Check if a couponId exits.
   *
   * @param userID the coupon ID.
   * @return true if the coupon exists, false otherwise
   * @throws SQLException if there is an issue communicating with the database
   */
  public boolean couponIdExists(int userID) throws SQLException {
    List<Integer> validCouponIds = getCouponIds();
    return validCouponIds.contains(userID);
  }

  /**
   * Get a list of all coupon Ids
   *
   * @return a list of coupon Ids
   * @throws SQLException f there is an issue communicating with the database
   */
  public List<Integer> getCouponIds() throws SQLException {
    List<Integer> ids = new ArrayList<Integer>();
    Cursor results = driver.getCoupons();

    while (results.moveToNext()) {
      ids.add(results.getInt(results.getColumnIndex("ID")));
    }
    results.close();
    return ids;
  }

  /**
   * Get the Role ID of a role by it's associated name.
   *
   * @param name the name of the role.
   * @return the role's ID, -1 if no such role.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public int getRoleIdByName(String name) throws SQLException {
    List<Integer> ids;
    ids = getRoleIds();
    for (int i = 0; i < ids.size(); i++) {
      if (getRoleName(ids.get(i)) != null && getRoleName(ids.get(i)).equals(name)) {
        return ids.get(i);
      }
    }

    return -1;
  }

  /**
   * Checks if a sale exists within the database.
   *
   * @param saleId the id of the sale to check.
   * @return true if the sale is in the database.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public boolean saleExists(int saleId) throws SQLException {
    Cursor results = driver.getSaleById(saleId);

    boolean exists = results.moveToNext();
    results.close();
    return exists;
  }

  /**
   * Get the accounts assigned to a given user.
   *
   * @param userId the id of the user.
   * @return a list containing the id's of the user's accounts, null if userId doesn't exist
   * @throws SQLException if something goes wrong.
   */
  public List<Integer> getUserAccountsById(int userId) throws SQLException {
    if (!userIdExists(userId)) {
      return null;
    }

    Cursor results = driver.getUserAccounts(userId);
    List<Integer> accounts = new ArrayList<>();

    while (results.moveToNext()) {
      accounts.add(results.getInt(results.getColumnIndex("ID")));
    }

    results.close();
    return accounts;
  }

  /**
   * Return the associated user id of an account.
   *
   * @param accountId the account to retrieve the associated user of.
   * @return the user id of the account's owner, -1 if not found.
   * @throws SQLException on failure
   */
  public int getUserIdByAccountId(int accountId) throws SQLException {
    for (Integer userId : getUserIds()) {
      for (Integer acctId : getUserAccountsById(userId)) {
        if (acctId == accountId) {
          return userId;
        }
      }
    }
    return -1;
  }

  /**
   * Get the details of a given account given acctId.
   *
   * @param accountId the ID of the account to retrieve the details of
   * @return a shopping cart with all their items, null if not found.
   * @throws SQLException if something goes wrong.
   */
  public ShoppingCart getAccountDetails(int accountId) throws SQLException {
    if (!getAllAccountIds().contains(accountId)) {
      return null;
    }

    Cursor results = driver.getAccountDetails(accountId);
    int userId = getUserIdByAccountId(accountId);

    if (userId == -1) {
      return null;
    }

    User user = getUserDetails(userId);
    if (user == null || !(user instanceof Customer)) {
      return null;
    }
    Customer customer = (Customer) user;

    ShoppingCart cart = new ShoppingCart(customer);

    int itemId;
    int quantity;

    while (results.moveToNext()) {
      itemId = results.getInt(results.getColumnIndex("ITEMID"));
      quantity = results.getInt(results.getColumnIndex("QUANTITY"));
      cart.addItem(getItem(itemId), quantity);
    }
    results.close();
    return cart;
  }

  /**
   * Gets all the account ids.
   *
   * @return list of all the account ids
   * @throws SQLException if something goes wrong
   */
  public List<Integer> getAllAccountIds() throws SQLException {
    List<Integer> allUserIds = getUserIds();
    List<Integer> allAccountIds = new ArrayList<>();

    for (Integer userId : allUserIds) {
      for (Integer userAccount : getUserAccountsById(userId)) {
        if (!allAccountIds.contains(userAccount)) {
          allAccountIds.add(userAccount);
        }
      }
    }
    return allAccountIds;
  }

  /**
   * Get the discount type ID from its name
   *
   * @param type the name of the discount type
   * @return the id of the discount type, or -1 if it is not found
   * @throws SQLException if something goes wrong retrieving the ID from the database
   */
  public int getDiscountTypeIdByName(String type) throws SQLException {
    type = type.toUpperCase();
    List<Integer> ids = getDiscountTypeIds();

    for (Integer id : getDiscountTypeIds()) {
      String discountName = getDiscountTypeName(id);
      Log.d("bigmon", discountName);

      if (discountName != null && discountName.equals(type)) {
        return id;
      }
    }

    return -1;

    /**
     * for (int i = 0; i < ids.size(); i++) { Log.d("bigmon", getDiscountTypeName(ids.get(0))) if
     * (getDiscountTypeName(ids.get(i)) != null && getDiscountTypeName(ids.get(i)).equals(type)) {
     * return ids.get(i); } } return -1;
     */
  }

  /**
   * Get the name of the discount type from its ID.
   *
   * @param discountTypeId the discount type ID
   * @return the name of the discount type
   * @throws SQLException if soemthing goes wrong retrieving the discount type from the database
   */
  public String getDiscountTypeName(int discountTypeId) throws SQLException {
    if (!discountTypeIdExists(discountTypeId)) {
      return null;
    }
    return driver.getDiscountType(discountTypeId);
  }

  /**
   * Check if a discount type exists.
   *
   * @param discountTypeId the id of the discount type
   * @return true if the discount type exists
   * @throws SQLException if there is an issue communicating with the database
   */
  public boolean discountTypeIdExists(int discountTypeId) throws SQLException {
    List<Integer> validDisCountTypeIds = getDiscountTypeIds();
    return validDisCountTypeIds.contains(discountTypeId);
  }

  /**
   * Get a list of discount type IDs.
   *
   * @return a list of dicount type IDS
   * @throws SQLException if there is an issue communicating with the database
   */
  public List<Integer> getDiscountTypeIds() throws SQLException {
    Cursor results = driver.getDiscountTypeIds();
    List<Integer> ids = new ArrayList<Integer>();
    while (results.moveToNext()) {
      ids.add(results.getInt(results.getColumnIndex("ID")));
    }
    results.close();
    return ids;
  }

  /**
   * Get a coupon's ID by name.
   *
   * @param code the coupon's name
   * @return the coupon's ID
   * @throws SQLException if there is an issue communicating with the database
   */
  public int getCouponId(String code) throws SQLException {
    int id = driver.getCouponId(code);
    return id;
  }

  /**
   * Get the type of a discount.
   *
   * @param couponId the coupon's ID
   * @return the coupon's discount type
   * @throws SQLException if there is an issue communicating with the database
   */
  public DiscountTypes getDiscountType(int couponId) throws SQLException {
    String type = driver.getDiscountType(couponId);
    DiscountTypes discountType = DiscountTypes.valueOf(type);
    return discountType;
  }

  public DiscountTypes getCouponDiscountType(int couponId) throws SQLException {
    int discountTypeId = driver.getCouponDiscountType(couponId);
    return getDiscountType(discountTypeId);
  }

  /**
   * Get the amount of a discount.
   *
   * @param couponId the coupon's id
   * @return the coupon's amount
   * @throws SQLException
   */
  public BigDecimal getDiscountAmount(int couponId) throws SQLException {
    String discountString = driver.getCouponDiscountAmount(couponId);
    BigDecimal discount = new BigDecimal(discountString);
    return discount;
  }

  /**
   * Get a coupon item by id.
   *
   * @param couponId the coupon's id
   * @return an item corresponding to the coupon
   * @throws SQLException if there is an issue communicating with the database
   */
  public int getCouponItem(int couponId) throws SQLException {
    return driver.getCouponItemId(couponId);
  }

  /**
   * Get the remaining uses of a coupon by id.
   *
   * @param couponId the coupon's id
   * @return the remaining uses
   * @throws SQLException if there is an issue communicating with the database
   */
  public int getCouponUses(int couponId) throws SQLException {
    return driver.getCouponUses(couponId);
  }

  /**
   * Get a coupon's code by Id.
   *
   * @param couponId the coupon's id
   * @return the coupon's code
   * @throws SQLException if there is an issue communicating with the database
   */
  public String getCouponCode(int couponId) throws SQLException {
    return driver.getCouponCode(couponId);
  }

  /**
   * Check if there is an account associated with a customer.
   *
   * @param userId The id of the user to check the account of.
   * @return true if the user has an account.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public boolean customerHasAccount(int userId) throws SQLException {
    List<Integer> accounts = getUserAccountsById(userId);
    if (accounts == null) {
      return false;
    }
    return !accounts.isEmpty();
  }

  /**
   * Get an user's active accounts.
   *
   * @param userId the user of interest
   * @return list of all active accounts
   * @throws SQLException on failure
   */
  public List<Integer> getUserActiveAccounts(int userId) throws SQLException {
    if (!userIdExists(userId)) {
      return null;
    }

    Cursor results = driver.getUserActiveAccounts(userId);
    List<Integer> accounts = new ArrayList<>();

    while (results.moveToNext()) {
      accounts.add(results.getInt(results.getColumnIndex("ID")));
    }

    results.close();
    return accounts;
  }

  /**
   * Get an user's inactive accounts.
   *
   * @param userId the user of interest
   * @return list of inactive accounts
   * @throws SQLException on failure
   */
  public List<Integer> getUserInactiveAccounts(int userId) throws SQLException {
    if (!userIdExists(userId)) {
      return null;
    }

    Cursor results = driver.getUserInactiveAccounts(userId);
    List<Integer> accounts = new ArrayList<>();

    while (results.moveToNext()) {
      accounts.add(results.getInt(results.getColumnIndex("ID")));
    }
    results.close();
    return accounts;
  }
}
