package com.b07.users;

import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.security.PasswordHelpers;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * Class representing users within the sales application and database.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public abstract class User implements Serializable {

  /** Serial Version ID of User class. */
  private static final long serialVersionUID = 1L;

  private int id;
  private String name;
  private int age;
  private String address;
  protected int roleId;
  private boolean authenticated = false;

  /** Return user's id. */
  public int getId() {
    return id;
  }

  /** Set user's id. */
  public void setId(int id) {
    this.id = id;
  }

  /** Get user's name. */
  public String getName() {
    return name;
  }

  /**
   * Sets the user's name.
   *
   * @param name The name to be set
   */
  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException(
          "Attempted to set user" + this.name + "'s name to null value");
    }
    this.name = name;
  }

  /** Get user's age. */
  public int getAge() {
    return age;
  }

  /** Set user's age. */
  public void setAge(int age) {
    this.age = age;
  }

  /** Get user's address. */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the user's address.
   *
   * @param address the address to be set.
   */
  public void setAddress(String address) {
    if (address != null) {
      this.address = address;
    }
  }

  /** Get user's role id. */
  public int getRoleId() {
    return roleId;
  }

  /**
   * Attempts to validate that a user's given password is correct.
   *
   * @param password The password to be checked, in plaintext.
   * @return true if the user is authenticated, false otherwise.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public final boolean authenticate(String password) throws SQLException {
    String hashedPassword;
    hashedPassword = DatabaseHelperAdapter.getPassword(id);
    if (hashedPassword == null) {
      return false;
    }
    boolean result = PasswordHelpers.comparePassword(hashedPassword, password);
    authenticated = result;
    return result;
  }

  /** Returns true if user is authenticated, false otherwise. */
  public boolean getAuthenticated() {
    return authenticated;
  }
}
