package com.b07.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.b07.security.PasswordHelpers;
import java.math.BigDecimal;

/** Created by Joe on 2017-11-19. */
public class DatabaseDriverAndroid extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "inventorymgmt.db";

  public DatabaseDriverAndroid(Context context) {
    super(context, DATABASE_NAME, null, 1);
    Log.d("Database", "Constructor called");
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {

    Log.d("Database", "Created");

    String sql =
        "CREATE TABLE ROLES " + "(ID INTEGER PRIMARY KEY NOT NULL," + "NAME TEXT NOT NULL)";
    sqLiteDatabase.execSQL(sql);

    sql =
        "CREATE TABLE USERS "
            + "(ID INTEGER PRIMARY KEY NOT NULL,"
            + "NAME TEXT NOT NULL,"
            + "AGE INTEGER NOT NULL,"
            + "ADDRESS CHAR(100))";
    sqLiteDatabase.execSQL(sql);

    sql =
        "CREATE TABLE USERROLE "
            + "(USERID INTEGER PRIMARY KEY NOT NULL,"
            + "ROLEID INTEGER NOT NULL,"
            + "FOREIGN KEY(USERID) REFERENCES USERS(ID),"
            + "FOREIGN KEY(ROLEID) REFERENCES ROLES(ID))";
    sqLiteDatabase.execSQL(sql);

    sql =
        "CREATE TABLE USERPW "
            + "(USERID INTEGER PRIMARY KEY NOT NULL,"
            + "PASSWORD CHAR(64),"
            + "FOREIGN KEY(USERID) REFERENCES USER(ID))";
    sqLiteDatabase.execSQL(sql);

    sql =
        "CREATE TABLE ITEMS "
            + "(ID INTEGER PRIMARY KEY NOT NULL,"
            + "NAME CHAR(64) NOT NULL,"
            + "PRICE TEXT NOT NULL)";
    sqLiteDatabase.execSQL(sql);

    sql =
        "CREATE TABLE INVENTORY "
            + "(ITEMID INTEGER PRIMARY KEY NOT NULL,"
            + "QUANTITY INTEGER NOT NULL,"
            + "FOREIGN KEY(ITEMID) REFERENCES ITEMS(ID))";
    sqLiteDatabase.execSQL(sql);

    sql =
        "CREATE TABLE SALES "
            + "(ID INTEGER PRIMARY KEY NOT NULL,"
            + "USERID INTEGER NOT NULL,"
            + "TOTALPRICE TEXT NOT NULL,"
            + "FOREIGN KEY(USERID) REFERENCES USERS(ID))";
    sqLiteDatabase.execSQL(sql);

    sql =
        "CREATE TABLE ITEMIZEDSALES "
            + "(SALEID INTEGER NOT NULL,"
            + "ITEMID INTEGER NOT NULL,"
            + "QUANTITY INTEGER NOT NULL,"
            + "FOREIGN KEY(SALEID) REFERENCES SALES(ID),"
            + "FOREIGN KEY(ITEMID) REFERENCES ITEMS(ID),"
            + "PRIMARY KEY(SALEID, ITEMID))";
    sqLiteDatabase.execSQL(sql);

    sql =
        "CREATE TABLE ACCOUNT "
            + "(ID INTEGER PRIMARY KEY NOT NULL, "
            + "USERID INTEGER NOT NULL, "
            + "ACTIVE INTEGER, "
            + "FOREIGN KEY(USERID) REFERENCES USER(ID))";
    sqLiteDatabase.execSQL(sql);

    sql =
        "CREATE TABLE ACCOUNTSUMMARY "
            + "(ACCTID INTEGER NOT NULL, "
            + "ITEMID INTEGER NOT NULL, "
            + "QUANTITY INTEGER NOT NULL, "
            + "FOREIGN KEY(ACCTID) REFERENCES ACCOUNT(ID), "
            + "FOREIGN KEY(ITEMID) REFERENCES ITEMS(ID), "
            + "PRIMARY KEY(ACCTID, ITEMID))";
    sqLiteDatabase.execSQL(sql);

    sql =
        "CREATE TABLE DISCOUNTTYPES " + "(ID INTEGER PRIMARY KEY NOT NULL," + "NAME TEXT NOT NULL)";
    sqLiteDatabase.execSQL(sql);

    sql =
        "CREATE TABLE COUPONS "
            + "(ID INTEGER PRIMARY KEY NOT NULL, "
            + "ITEMID INTEGER NOT NULL, "
            + "USES INTEGER NOT NULL, "
            + "TYPEID INTEGER NOT NULL , "
            + "DISCOUNT TEXT NOT NULL , "
            + "CODE TEXT NOT NULL , "
            + "FOREIGN KEY(ITEMID) REFERENCES ITEMS(ID), "
            + "FOREIGN KEY(TYPEID) REFERENCES DISCOUNTTYPES(ID))";
    sqLiteDatabase.execSQL(sql);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ROLES");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USERS");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USERROLE");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USERPW");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ITEMS");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS INVENTORY");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS SALES");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ITEMIZEDSALES");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ACCOUNT");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ACCOUNTSUMMARY");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS DISCOUNTTYPES");
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS COUPONS");

    onCreate(sqLiteDatabase);
  }

  // INSERTS
  protected long insertRole(String role) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("NAME", role);
    long id = sqLiteDatabase.insert("ROLES", null, contentValues);
    sqLiteDatabase.close();
    return id;
  }

  protected long insertNewUser(String name, int age, String address, String password) {
    long id = insertUser(name, age, address);
    insertPassword(password, (int) id);
    return id;
  }

  protected long insertUserRole(int userId, int roleId) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("USERID", userId);
    contentValues.put("ROLEID", roleId);
    long id = sqLiteDatabase.insert("USERROLE", null, contentValues);
    sqLiteDatabase.close();
    return id;
  }

  protected long insertItem(String name, BigDecimal price) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("NAME", name);
    contentValues.put("PRICE", price.toPlainString());
    long id = sqLiteDatabase.insert("ITEMS", null, contentValues);
    sqLiteDatabase.close();
    return id;
  }

  protected long insertInventory(int itemId, int quantity) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("ITEMID", itemId);
    contentValues.put("QUANTITY", quantity);
    long id = sqLiteDatabase.insert("INVENTORY", null, contentValues);
    sqLiteDatabase.close();
    return id;
  }

  protected long insertSale(int userId, BigDecimal totalPrice) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("USERID", userId);
    contentValues.put("TOTALPRICE", totalPrice.toPlainString());
    long id = sqLiteDatabase.insert("SALES", null, contentValues);
    sqLiteDatabase.close();
    return id;
  }

  protected long insertItemizedSale(int saleId, int itemId, int quantity) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("SALEID", saleId);
    contentValues.put("ITEMID", itemId);
    contentValues.put("QUANTITY", quantity);
    long id = sqLiteDatabase.insert("ITEMIZEDSALES", null, contentValues);
    sqLiteDatabase.close();
    return id;
  }

  protected long insertAccount(int userId, boolean active) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("USERID", userId);
    contentValues.put("ACTIVE", active ? 1 : 0);
    long id = sqLiteDatabase.insert("ACCOUNT", null, contentValues);
    sqLiteDatabase.close();
    return id;
  }

  protected long insertAccountLine(int accountId, int itemId, int quantity) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("ACCTID", accountId);
    contentValues.put("ITEMID", itemId);
    contentValues.put("QUANTITY", quantity);
    long id = sqLiteDatabase.insert("ACCOUNTSUMMARY", null, contentValues);
    sqLiteDatabase.close();
    return id;
  }

  private long insertUser(String name, int age, String address) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("NAME", name);
    contentValues.put("AGE", age);
    contentValues.put("ADDRESS", address);

    long id = sqLiteDatabase.insert("USERS", null, contentValues);
    sqLiteDatabase.close();

    return id;
  }

  private void insertPassword(String password, int userId) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();

    password = PasswordHelpers.passwordHash(password);
    contentValues.put("USERID", userId);
    contentValues.put("PASSWORD", password);
    sqLiteDatabase.insert("USERPW", null, contentValues);
    sqLiteDatabase.close();
  }

  /**
   * Use this to insert new discount types into the database.
   *
   * @param type the new discount type to be added.
   * @return the id of the discount type that was inserted.
   */
  protected long insertDiscountType(String type) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("NAME", type);
    long id = sqLiteDatabase.insert("DISCOUNTTYPES", null, contentValues);
    sqLiteDatabase.close();
    return id;
  }

  protected long insertCoupon(int uses, int typeId, int itemId, BigDecimal discount, String code) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("USES", uses);
    contentValues.put("TYPEID", typeId);
    contentValues.put("ITEMID", itemId);
    contentValues.put("DISCOUNT", discount.toPlainString());
    contentValues.put("CODE", code);
    long id = sqLiteDatabase.insert("COUPONS", null, contentValues);
    sqLiteDatabase.close();
    return id;
  }

  // SELECT METHODS
  protected Cursor getRoles() {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    return sqLiteDatabase.rawQuery("SELECT * FROM ROLES;", null);
  }

  protected String getRole(int id) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor =
        sqLiteDatabase.rawQuery(
            "SELECT NAME FROM ROLES WHERE ID = ?", new String[] {String.valueOf(id)});
    cursor.moveToFirst();
    String value = cursor.getString(cursor.getColumnIndex("NAME"));
    cursor.close();
    return value;
  }

  protected int getUserRole(int userId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor =
        sqLiteDatabase.rawQuery(
            "SELECT ROLEID FROM USERROLE WHERE USERID = ?", new String[] {String.valueOf(userId)});
    cursor.moveToFirst();
    int result = cursor.getInt(cursor.getColumnIndex("ROLEID"));
    cursor.close();
    return result;
  }

  protected Cursor getUsersByRole(int roleId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    return sqLiteDatabase.rawQuery(
        "SELECT USERID FROM USERROLE WHERE ROLEID = ?", new String[] {String.valueOf(roleId)});
  }

  protected Cursor getUsersDetails() {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    return sqLiteDatabase.rawQuery("SELECT * FROM USERS", null);
  }

  protected Cursor getUserDetails(int userId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    return sqLiteDatabase.rawQuery(
        "SELECT * FROM USERS WHERE ID = ?", new String[] {String.valueOf(userId)});
  }

  protected String getPassword(int userId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor =
        sqLiteDatabase.rawQuery(
            "SELECT PASSWORD FROM USERPW WHERE USERID = ?", new String[] {String.valueOf(userId)});
    cursor.moveToFirst();
    String result = cursor.getString(cursor.getColumnIndex("PASSWORD"));
    cursor.close();
    return result;
  }

  protected Cursor getAllItems() {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    return sqLiteDatabase.rawQuery("SELECT * FROM ITEMS", null);
  }

  protected Cursor getItem(int itemId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    return sqLiteDatabase.rawQuery(
        "SELECT * FROM ITEMS WHERE ID = ?;", new String[] {String.valueOf(itemId)});
  }

  protected Cursor getInventory() {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    return sqLiteDatabase.rawQuery("SELECT * FROM INVENTORY;", null);
  }

  protected int getInventoryQuantity(int itemId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor =
        sqLiteDatabase.rawQuery(
            "SELECT QUANTITY FROM INVENTORY WHERE ITEMID = ?;",
            new String[] {String.valueOf(itemId)});
    cursor.moveToFirst();
    int result = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
    cursor.close();
    return result;
  }

  protected Cursor getSales() {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM SALES", null);
    return cursor;
  }

  protected Cursor getSaleById(int saleId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor =
        sqLiteDatabase.rawQuery(
            "SELECT * FROM SALES WHERE ID = ?;", new String[] {String.valueOf(saleId)});
    return cursor;
  }

  protected Cursor getSalesToUser(int userId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor =
        sqLiteDatabase.rawQuery(
            "SELECT * FROM SALES WHERE USERID = ?;", new String[] {String.valueOf(userId)});
    return cursor;
  }

  protected Cursor getItemizedSales() {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM ITEMIZEDSALES;", null);
    return cursor;
  }

  protected Cursor getItemizedSaleById(int saleId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor =
        sqLiteDatabase.rawQuery(
            "SELECT * FROM ITEMIZEDSALES WHERE SALEID = ?;", new String[] {String.valueOf(saleId)});
    return cursor;
  }

  protected Cursor getUserAccounts(int userId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor =
        sqLiteDatabase.rawQuery(
            "SELECT ID FROM ACCOUNT WHERE USERID = ?;", new String[] {String.valueOf(userId)});
    return cursor;
  }

  protected Cursor getAccountDetails(int accountId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor =
        sqLiteDatabase.rawQuery(
            "SELECT * FROM ACCOUNTSUMMARY WHERE ACCTID = ?;",
            new String[] {String.valueOf(accountId)});
    return cursor;
  }

  protected Cursor getUserActiveAccounts(int userId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor =
        sqLiteDatabase.rawQuery(
            "SELECT * FROM ACCOUNT WHERE userId = ? and ACTIVE = ?",
            new String[] {String.valueOf(userId), String.valueOf(1)});
    return cursor;
  }

  protected Cursor getUserInactiveAccounts(int userId) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor =
        sqLiteDatabase.rawQuery(
            "SELECT * FROM ACCOUNT WHERE userId = ? and ACTIVE = ?",
            new String[] {String.valueOf(userId), String.valueOf(0)});
    return cursor;
  }
  /**
   * Get the discount type for a given ID.
   *
   * @param discountTypeId the discount type id.
   * @return the name of the discount type
   */
  protected String getDiscountType(int discountTypeId) {
    Log.d("bigmon", " " + discountTypeId);
    String sql = "SELECT NAME FROM DISCOUNTTYPES WHERE ID = ?";
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[] {String.valueOf(discountTypeId)});
    cursor.moveToFirst();
    String results = cursor.getString(cursor.getColumnIndex("NAME"));
    cursor.close();
    return results;
  }

  /**
   * Get every discount type ID.
   *
   * @return a cursor containing every discount type id in the database.
   */
  protected Cursor getDiscountTypeIds() {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM DISCOUNTTYPES;", null);
    return cursor;
  }

  /**
   * get the id of a coupon
   *
   * @param code the coupon code
   * @return the id of the coupon
   */
  protected int getCouponId(String code) {
    String sql = "SELECT ID FROM COUPONS WHERE CODE = ?";
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[] {code});
    cursor.moveToFirst();
    int id = cursor.getInt(cursor.getColumnIndex("ID"));
    cursor.close();
    return id;
  }

  /**
   * get the discount amount for a coupon
   *
   * @param id the coupon id
   * @return the id of the coupon
   * @if something goes wrong
   */
  protected String getCouponDiscountAmount(int id) {
    String sql = "SELECT DISCOUNT FROM COUPONS WHERE ID = ?";
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[] {String.valueOf(id)});
    cursor.moveToFirst();
    String discount = cursor.getString(cursor.getColumnIndex("DISCOUNT"));
    cursor.close();
    return discount;
  }

  /**
   * get the item id for a coupon
   *
   * @param id the coupon id
   * @return the id of the item
   * @if something goes wrong
   */
  protected int getCouponItemId(int id) {
    // String sql = "SELECT ITEMID FROM COUPONS WHERE CODE = ?";
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor =
        sqLiteDatabase.rawQuery(
            "SELECT ITEMID FROM COUPONS WHERE ID + ?", new String[] {String.valueOf(id)});
    cursor.moveToFirst();
    int itemId = cursor.getInt(cursor.getColumnIndex("ITEMID"));
    return itemId;

    /*
    String sql = "SELECT ITEMID FROM COUPONS WHERE ID = ?";
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{String.valueOf(id)});
    int item = cursor.getInt(cursor.getColumnIndex("ITEMID"));
    cursor.close();
    return item;*/
  }

  /**
   * Return a list of coupons
   *
   * @return a cursor of coupon ids
   * @if something goes wrong
   */
  protected Cursor getCoupons() {
    String sql = "SELECT * FROM COUPONS;";
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    return sqLiteDatabase.rawQuery(sql, null);
  }

  /**
   * Return the number of uses left for a coupon
   *
   * @param couponId the coupon id
   * @return the number of uses left
   * @if something goes wrong
   */
  protected int getCouponUses(int couponId) {
    String sql = "SELECT USES FROM COUPONS WHERE ID = ?";
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[] {String.valueOf(couponId)});
    cursor.moveToNext();
    int item = cursor.getInt(cursor.getColumnIndex("USES"));
    cursor.close();
    return item;
  }

  protected String getCouponCode(int couponId) {
    String sql = "SELECT CODE FROM COUPONS WHERE ID = ?";
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[] {String.valueOf(couponId)});
    cursor.moveToFirst();
    String item = cursor.getString(cursor.getColumnIndex("CODE"));
    cursor.close();
    return item;
  }

  // UPDATE METHODS

  protected boolean updateRoleName(String name, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("NAME", name);
    boolean result =
        sqLiteDatabase.update("ROLES", contentValues, "ID = ?", new String[] {String.valueOf(id)})
            > 0;
    sqLiteDatabase.close();
    return result;
  }

  protected boolean updateUserName(String name, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("NAME", name);
    boolean result =
        sqLiteDatabase.update("USERS", contentValues, "ID = ?", new String[] {String.valueOf(id)})
            > 0;
    sqLiteDatabase.close();
    return result;
  }

  protected boolean updateUserAge(int age, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("AGE", age);
    boolean result =
        sqLiteDatabase.update("USERS", contentValues, "ID = ?", new String[] {String.valueOf(id)})
            > 0;
    sqLiteDatabase.close();
    return result;
  }

  protected boolean updateUserAddress(String address, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("ADDRESS", address);
    boolean result =
        sqLiteDatabase.update("USERS", contentValues, "ID = ?", new String[] {String.valueOf(id)})
            > 0;
    sqLiteDatabase.close();
    return result;
  }

  protected boolean updateUserRole(int roleId, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("ROLEID", roleId);
    boolean result =
        sqLiteDatabase.update(
                "USERROLE", contentValues, "USERID = ?", new String[] {String.valueOf(id)})
            > 0;
    sqLiteDatabase.close();
    return result;
  }

  protected boolean updateItemName(String name, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("NAME", name);
    boolean result =
        sqLiteDatabase.update("ITEMS", contentValues, "ID = ?", new String[] {String.valueOf(id)})
            > 0;
    sqLiteDatabase.close();
    return result;
  }

  protected boolean updateItemPrice(BigDecimal price, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("PRICE", price.toPlainString());
    boolean result =
        sqLiteDatabase.update("ITEMS", contentValues, "ID = ?", new String[] {String.valueOf(id)})
            > 0;
    sqLiteDatabase.close();
    return result;
  }

  protected boolean updateInventoryQuantity(int quantity, int id) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("QUANTITY", quantity);
    boolean result =
        sqLiteDatabase.update(
                "INVENTORY", contentValues, "ITEMID = ?", new String[] {String.valueOf(id)})
            > 0;
    sqLiteDatabase.close();
    return result;
  }

  protected boolean updateAccountStatus(int accountId, boolean active) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("ACTIVE", active ? 1 : 0);
    boolean result =
        sqLiteDatabase.update(
                "ACCOUNT", contentValues, "ID=?", new String[] {String.valueOf(accountId)})
            > 0;
    sqLiteDatabase.close();
    return result;
  }

  /**
   * Update the uses of a coupon.
   *
   * @param couponId the id of the coupon.
   * @param uses the remaining number of uses the coupon should have
   * @return true if successful, false otherwise.
   */
  protected boolean updateCouponUses(int couponId, int uses) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("ID", couponId);
    contentValues.put("USES", uses);
    boolean result =
        sqLiteDatabase.update(
                "COUPONS", contentValues, "ID=?", new String[] {String.valueOf(couponId)})
            > 0;
    sqLiteDatabase.close();
    return result;
  }
}
