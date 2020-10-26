package com.b07.users;

import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.exceptions.DatabaseInsertException;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * A class allowing administrators to perform operations on the application, database, and users.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class Admin extends User implements Serializable {

  /** Serial Version ID of Admin. */
  private static final long serialVersionUID = 1L;

  /**
   * Create a new admin, without setting their authentication value.
   *
   * @param id the id of the admin to be created.
   * @param name the name of the admin to be created.
   * @param age the age of the admin to be created.
   * @param address the address of the admin to be created.
   * @throws SQLException
   */
  public Admin(int id, String name, int age, String address) throws SQLException {
    setId(id);
    setName(name);
    setAge(age);
    setAddress(address);
    setRole();
  }

  /**
   * Create a new admin, and set their authentication value.
   *
   * @param id id the id of the admin to be created.
   * @param name the name of the admin to be created.
   * @param age the age of the admin to be created.
   * @param address the address of the admin to be created.
   * @param authenticated whether the admin should be created authenticated or not.
   * @throws SQLException
   */
  public Admin(int id, String name, int age, String address, boolean authenticated)
      throws SQLException {
    setId(id);
    setName(name);
    setAge(age);
    setAddress(address);
    setRole();
  }

  /**
   * use the user's id to an admin id
   *
   * @throws SQLException if the role id annot be retreived
   */
  private void setRole() throws SQLException {
    super.roleId = DatabaseHelperAdapter.getRoleIdByName("ADMIN");
  }

  /**
   * Changes an employee's role in the database to Admin.
   *
   * @param employee the employee to be promoted.
   * @return true if the role change is successful, false otherwise.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public boolean promoteEmployee(Employee employee) throws SQLException {
    int id = employee.getId();
    try {
      int adminId = DatabaseHelperAdapter.getUserRoleId(getId());
      if (adminId == -1) {
        return false;
      }
      return DatabaseHelperAdapter.updateUserRole(adminId, id);
    } catch (DatabaseInsertException e) {
      return false;
    }
  }
}
