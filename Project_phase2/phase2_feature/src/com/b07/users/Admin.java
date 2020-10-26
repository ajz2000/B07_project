package com.b07.users;

import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.DatabaseInsertException;
import java.sql.SQLException;

/**
 * A class allowing administrators to perform operations on the application, database, and users.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class Admin extends User {

  /**
   * Create a new admin, without setting their authentication value.
   *
   * @param id the id of the admin to be created.
   * @param name the name of the admin to be created.
   * @param age the age of the admin to be created.
   * @param address the address of the admin to be created.
   */
  public Admin(int id, String name, int age, String address) {
    setId(id);
    setName(name);
    setAge(age);
    setAddress(address);
  }

  /**
   * Create a new admin, and set their authentication value.
   *
   * @param id id the id of the admin to be created.
   * @param name the name of the admin to be created.
   * @param age the age of the admin to be created.
   * @param address the address of the admin to be created.
   * @param authenticated whether the admin should be created authenticated or not.
   */
  public Admin(int id, String name, int age, String address, boolean authenticated) {
    setId(id);
    setName(name);
    setAge(age);
    setAddress(address);
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
      int adminId = DatabaseSelectHelper.getUserRoleId(getId());
      if (adminId == -1) {
        return false;
      }
      return DatabaseUpdateHelper.updateUserRole(adminId, id);
    } catch (DatabaseInsertException e) {
      return false;
    }
  }
}
