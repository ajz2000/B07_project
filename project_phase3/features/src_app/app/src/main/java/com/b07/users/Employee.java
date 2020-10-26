package com.b07.users;

import com.b07.database.helper.DatabaseHelperAdapter;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * A class representing an employee within the sales application system.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class Employee extends User implements Serializable {
  /** Serial Version ID of Employee. */
  private static final long serialVersionUID = 1L;

  /**
   * Create a new employee, without setting their authentication value.
   *
   * @param id the id of the employee to be created.
   * @param name the name of the employee to be created.
   * @param age the age of the employee to be created.
   * @param address the address of the employee to be created.
   * @throws SQLException if the role id cannot be retreived
   */
  public Employee(int id, String name, int age, String address) throws SQLException {
    setId(id);
    setName(name);
    setAge(age);
    setAddress(address);
    setRole();
  }

  /**
   * Create a new employee, and set their authentication value.
   *
   * @param id id the id of the employee to be created.
   * @param name the name of the employee to be created.
   * @param age the age of the employee to be created.
   * @param address the address of the employee to be created.
   * @param authenticated whether the employee should be created authenticated or not.
   * @throws SQLException if the role id cannot be retreived
   */
  public Employee(int id, String name, int age, String address, boolean authenticated)
      throws SQLException {
    setId(id);
    setName(name);
    setAge(age);
    setAddress(address);
    setRole();
  }

  /**
   * set the user role
   *
   * @throws SQLException
   */
  private void setRole() throws SQLException {
    super.roleId = DatabaseHelperAdapter.getRoleIdByName("EMPLOYEE");
  }
}
