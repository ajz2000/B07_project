package com.b07.serialize;

import android.content.Context;
import android.util.Log;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.database.helper.DatabaseMethodHelper;
import com.b07.exceptions.ConnectionFailedException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.DifferentEnumException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.inventory.ItemTypes;
import com.b07.store.Coupon;
import com.b07.store.CouponImpl;
import com.b07.store.DiscountTypes;
import com.b07.store.Sale;
import com.b07.store.SalesLog;
import com.b07.store.ShoppingCart;
import com.b07.users.Account;
import com.b07.users.Roles;
import com.b07.users.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SerializeDatabase {

  private static DataStorage getDatabaseObject() throws SQLException {

    ArrayList<Integer> roleIds = (ArrayList) DatabaseHelperAdapter.getRoleIds();
    // Below
    HashMap<Integer, String> roleIdToRoleNames = new HashMap<Integer, String>();
    for (Integer id : roleIds) {
      roleIdToRoleNames.put(id, DatabaseHelperAdapter.getRoleName(id));
    }

    ArrayList<Integer> userIds = (ArrayList) DatabaseHelperAdapter.getUserIds();

    // Below
    ArrayList<User> users = (ArrayList) DatabaseHelperAdapter.getUsersDetails();

    // Below
    HashMap<Integer, Integer> userToRole = new HashMap<Integer, Integer>();
    for (Integer id : userIds) {
      userToRole.put(id, DatabaseHelperAdapter.getUserRoleId(id));
    }

    // Below
    ArrayList<Item> items = (ArrayList) DatabaseHelperAdapter.getAllItems();

    // Below
    Inventory inventory = DatabaseHelperAdapter.getInventory();

    // Below
    SalesLog sales = DatabaseHelperAdapter.getSales();

    // Below
    SalesLog itemizedSales = DatabaseHelperAdapter.getItemizedSales();

    // Below
    ArrayList<Account> accounts = new ArrayList<Account>();
    ArrayList<Integer> accountIds = (ArrayList<Integer>) DatabaseHelperAdapter.getAllAccountIds();
    int userId;
    boolean active;
    ArrayList<Integer> activeAccounts;
    Account newAccount;
    for (Integer accountId : accountIds) {
      userId = DatabaseHelperAdapter.getUserIdByAccountId(accountId);
      activeAccounts = (ArrayList<Integer>) DatabaseHelperAdapter.getUserActiveAccounts(userId);
      active = activeAccounts.contains(accountId);
      newAccount = new Account(userId, accountId, active);
      newAccount.retrieveCustomerCart();
      accounts.add(newAccount);
    }

    // Below
    HashMap<Integer, String> userToHashedPWs = new HashMap<Integer, String>();
    for (Integer id : userIds) {
      userToHashedPWs.put(id, DatabaseHelperAdapter.getPassword(id));
    }

    ArrayList<Integer> discountTypeIds =
        (ArrayList<Integer>) DatabaseHelperAdapter.getDiscountTypeIds();
    // Below
    HashMap<Integer, String> discountTypes = new HashMap<Integer, String>();
    for (Integer discountIds : discountTypeIds) {
      discountTypes.put(discountIds, DatabaseHelperAdapter.getDiscountTypeName(discountIds));
    }

    ArrayList<Integer> couponIds = (ArrayList<Integer>) DatabaseHelperAdapter.getCouponIds();
    // Below
    HashMap<Integer, Coupon> coupons = new HashMap<Integer, Coupon>();
    Integer itemId;
    int uses;
    String type;
    BigDecimal discount;
    String code;
    for (Integer couponId : couponIds) {
      itemId = DatabaseHelperAdapter.getCouponItem(couponId);
      uses = DatabaseHelperAdapter.getCouponUses(couponId);
      type = DatabaseHelperAdapter.getDiscountTypeName(couponId);
      discount = DatabaseHelperAdapter.getDiscountAmount(couponId);
      // TODO: Get the code from Database
      code = DatabaseHelperAdapter.getCouponCode(couponId);
      coupons.put(couponId, new CouponImpl(itemId, uses, type, discount, code));
    }

    DataStorage data =
        new DataStorage(
            roleIdToRoleNames,
            users,
            userToRole,
            items,
            inventory,
            sales,
            itemizedSales,
            accounts,
            userToHashedPWs,
            discountTypes,
            coupons);

    return data;
  }

  public static void serializeToFile(String location) throws SQLException, IOException {

    DataStorage database = getDatabaseObject();
    SerializeFunc.serialize(database, location + "/database_copy.ser");
    System.out.println("Database stored in: " + location + "/database_copy.ser");
  }

  private static boolean checkEnums(DataStorage database) {
    List<String> rolesEnum = new ArrayList<String>();
    for (Roles role : Roles.values()) {
      rolesEnum.add(role.toString());
    }

    List<String> discountTypesEnum = new ArrayList<String>();
    for (DiscountTypes discountType : DiscountTypes.values()) {
      discountTypesEnum.add(discountType.toString());
    }

    List<String> itemTypesEnum = new ArrayList<String>();
    for (ItemTypes itemType : ItemTypes.values()) {
      itemTypesEnum.add(itemType.toString());
    }

    for (String roleDatabase : database.getRoleIdToRoleNames().values()) {
      if (!rolesEnum.contains(roleDatabase)) {
        return false;
      }
    }

    for (String discountDatabase : database.getDiscountTypes().values()) {
      if (!discountTypesEnum.contains(discountDatabase)) {
        return false;
      }
    }

    for (Item itemDatabase : database.getItems()) {
      if (!itemTypesEnum.contains(itemDatabase.getName())) {
        return false;
      }
    }

    return true;
  }

  private static void insertDataStorage(DataStorage database, Context context)
      throws SQLException, DatabaseInsertException {
    HashMap<Integer, String> roleIdToRoleNames = database.getRoleIdToRoleNames();
    ArrayList<User> users = database.getUsers();
    HashMap<Integer, Integer> userToRole = database.getUserToRole();
    ArrayList<Item> items = database.getItems();
    Inventory inventory = database.getInventory();
    SalesLog sales = database.getSales();
    SalesLog itemizedSales = database.getItemizedSales();
    ArrayList<Account> accounts = database.getAccounts();
    HashMap<Integer, String> userToHashedPWs = database.getUserToHashedPWs();
    HashMap<Integer, String> discountTypes = database.getDiscountTypes();
    HashMap<Integer, Coupon> couponIdsToCoupons = database.getCouponIdsToCoupons();

    // Roles
    ArrayList<Integer> roleIds = new ArrayList<Integer>();
    for (Integer roleId : roleIdToRoleNames.keySet()) {
      roleIds.add(roleId);
    }
    Collections.sort(roleIds);
    for (Integer roleId : roleIds) {
      DatabaseHelperAdapter.insertRole(roleIdToRoleNames.get(roleId));
    }

    // Users + UserPW
    HashMap<Integer, User> idToUser = new HashMap<Integer, User>();
    ArrayList<Integer> userIds = new ArrayList<Integer>();
    for (User user : users) {
      idToUser.put(user.getId(), user);
      userIds.add(user.getId());
    }
    Collections.sort(userIds);
    User currentUser;
    for (Integer userId : userIds) {
      currentUser = idToUser.get(userId);
      // DatabaseHelperAdapter.insertNewUser(currentUser.getName(), currentUser.getAge(),
      // currentUser.getAddress(), "temp");
      SerializationPasswordHelper.insertUserNoHash(
          currentUser.getName(),
          currentUser.getAge(),
          currentUser.getAddress(),
          database.getUserToHashedPWs().get(currentUser.getId()),
          context);
    }

    // Userrole
    for (Integer userId : userToRole.keySet()) {
      DatabaseHelperAdapter.insertUserRole(userId, userToRole.get(userId));
    }

    // Items
    HashMap<Integer, Item> idToItems = new HashMap<Integer, Item>();
    ArrayList<Integer> itemIds = new ArrayList<Integer>();
    for (Item item : items) {
      idToItems.put(item.getId(), item);
      itemIds.add(item.getId());
    }
    Collections.sort(itemIds);
    Item currentItem;
    for (Integer itemId : itemIds) {
      currentItem = idToItems.get(itemId);
      DatabaseHelperAdapter.insertItem(currentItem.getName(), currentItem.getPrice());
    }

    // inventory
    for (Item currItem : inventory.getItemMap().keySet()) {
      DatabaseHelperAdapter.insertInventory(currItem.getId(), inventory.getItemMap().get(currItem));
    }

    // Sales
    ArrayList<Sale> allSales = (ArrayList<Sale>) sales.getSales();
    HashMap<Integer, Sale> idToSales = new HashMap<Integer, Sale>();
    ArrayList<Integer> saleIds = new ArrayList<Integer>();
    for (Sale sale : allSales) {
      idToSales.put(sale.getId(), sale);
      saleIds.add(sale.getId());
    }
    Collections.sort(saleIds);
    Sale currentSale;
    for (Integer saleId : saleIds) {
      currentSale = idToSales.get(saleId);
      DatabaseHelperAdapter.insertSale(currentSale.getUser().getId(), currentSale.getTotalPrice());
    }

    // ItemizedSales
    for (Sale currSale : itemizedSales.getSales()) {
      HashMap<Item, Integer> itemToQuantity = currSale.getItemMap();
      for (Item currItem : itemToQuantity.keySet()) {
        DatabaseHelperAdapter.insertItemizedSale(
            currSale.getId(), currItem.getId(), itemToQuantity.get(currItem));
      }
    }

    // Account
    HashMap<Integer, Account> accountIdToAccount = new HashMap<Integer, Account>();
    ArrayList<Integer> accountIds = new ArrayList<Integer>();
    for (Account currAccount : accounts) {
      accountIdToAccount.put(currAccount.getAccountId(), currAccount);
      accountIds.add(currAccount.getAccountId());
    }
    Collections.sort(accountIds);
    Account currentAccount;
    for (Integer accountId : accountIds) {
      currentAccount = accountIdToAccount.get(accountId);
      DatabaseHelperAdapter.insertAccount(
          currentAccount.getUserId(), currentAccount.getActiveStatus());
      // AccountSummary
      ShoppingCart currentCart = currentAccount.getCart();
      HashMap<Item, Integer> itemToQuantity = currentCart.getItemsWithQuantity();
      for (Item item : currentCart.getItems()) {
        DatabaseHelperAdapter.insertAccountLine(accountId, item.getId(), itemToQuantity.get(item));
      }
    }

    // Discounttypes
    ArrayList<Integer> discountIds = new ArrayList<Integer>();
    for (Integer discountId : discountTypes.keySet()) {
      discountIds.add(discountId);
    }
    Collections.sort(discountIds);
    for (Integer discountId : discountIds) {
      DatabaseHelperAdapter.insertDiscountType(discountTypes.get(discountId));
    }

    // Coupons
    ArrayList<Integer> couponIds = new ArrayList<Integer>();
    for (Integer couponId : couponIdsToCoupons.keySet()) {
      couponIds.add(couponId);
    }
    Collections.sort(couponIds);
    Coupon currCoupon;
    for (Integer couponId : couponIds) {
      currCoupon = couponIdsToCoupons.get(couponId);
      DatabaseHelperAdapter.insertCoupon(
          currCoupon.getItemId(),
          currCoupon.getUses(),
          currCoupon.getType(),
          currCoupon.getDiscount(),
          currCoupon.getCode());
    }
  }

  public static void populateFromFile(String location, Context context)
      throws SQLException, IOException, ClassNotFoundException, DifferentEnumException {
    DataStorage database = SerializeFunc.deserialize(location + "/database_copy.ser");
    if (!checkEnums(database)) {
      throw new DifferentEnumException();
    }
    // This is a gross violation of SOLID design
    // Please lord Mo forgive my transgression
    // For I know not what I do
    SerializeFunc.serialize(getDatabaseObject(), location + "/database_backup.ser");
    DatabaseMethodHelper h = new DatabaseMethodHelper(context);
    h.deleteDatabase();

    try {
      DatabaseHelperAdapter.reInitialize();
      insertDataStorage(database, context);
    } catch (Exception i) {
      Log.e("Deserialiaze", "Need to revert", i);
      database = SerializeFunc.deserialize(location + "/database_backup.ser");
      System.out.println("Encourtered an error, reverting...");
      try {
        DatabaseHelperAdapter.reInitialize();
        insertDataStorage(database, context);
        System.out.println("Revert Successful");
      } catch (ConnectionFailedException | DatabaseInsertException e) {
        e.printStackTrace();
        System.out.println("Failed to revert, please manually revert Database File");
        throw new SQLException();
      }
    }
  }
}
