package com.b07.users;

import com.b07.database.helper.DatabaseHelperAdapter;
import java.sql.SQLException;

/**
 * Object factory for users.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class UserFactory {

  /**
   * Creates a new user of the correct type based on their ID.
   *
   * @param id the user's ID.
   * @param name the user's name.
   * @param age the user's age.
   * @param address the user's address
   * @return a user of the correct type, based on their ID, null if no such user exists.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static User createUser(int id, String name, int age, String address) throws SQLException {
    int roleId = DatabaseHelperAdapter.getUserRoleId(id);
    String roleName = DatabaseHelperAdapter.getRoleName(roleId);

    if (roleName.equals(Roles.ADMIN.name())) {
      return new Admin(id, name, age, address);
    } else if (roleName.equals(Roles.EMPLOYEE.name())) {
      return new Employee(id, name, age, address);
    } else if (roleName.equals(Roles.CUSTOMER.name())) {
      return new Customer(id, name, age, address);
    }
    return null;
  }
}
