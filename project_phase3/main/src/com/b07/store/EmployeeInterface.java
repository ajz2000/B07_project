package com.b07.store;

import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.users.Employee;
import com.b07.users.Roles;
import java.sql.SQLException;

/**
 * Class allowing authenticated employees to perform operations on the database.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class EmployeeInterface {

  private Employee currentEmployee = null;
  private Inventory inventory;

  /**
   * Constructor for EmployeeInterface.
   *
   * @param employee the associated employee.
   * @param inventory the associated inventory.
   */
  public EmployeeInterface(Employee employee, Inventory inventory) {
    this.inventory = inventory;
    currentEmployee = employee;
  }

  /**
   * Constructor for EmployeeInterface.
   *
   * @param inventory the inventory to be set to
   */
  public EmployeeInterface(Inventory inventory) {
    this.inventory = inventory;
  }

  /**
   * Sets the current employee.
   *
   * @param employee the employee to be set.
   */
  public void setCurrentEmployee(Employee employee) {
    currentEmployee = employee;
  }

  /**
   * Checks if the interface is currently associated with some employee.
   *
   * @return true if the interface is associated with an employee, false otherwise.
   */
  public boolean hasCurrentEmployee() {
    return currentEmployee != null;
  }

  /**
   * Re-stock an item in the local inventory and database.
   *
   * @param item the item to be re-stocked.
   * @param quantity the quantity to re-stock.
   * @return true if the operation succeeds.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public boolean restockInventory(Item item, int quantity) throws SQLException {
    try {
      DatabaseHelperAdapter.updateInventoryQuantity(quantity, item.getId());
    } catch (DatabaseInsertException e) {
      return false;
    }
    inventory.updateMap(item, quantity);
    return true;
  }

  /**
   * Create a new customer and add it to the database.
   *
   * @param name the customer's name
   * @param age the customer's name.
   * @param address the customer's address.
   * @param password the customer's password.
   * @return the customer's user ID.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the customer cannot be inserted into the database.
   */
  public int createCustomer(String name, int age, String address, String password)
      throws SQLException, DatabaseInsertException {

    int userId = -1;
    userId = DatabaseHelperAdapter.insertNewUser(name, age, address, password);
    int customerId = DatabaseHelperAdapter.getRoleIdByName("CUSTOMER");
    DatabaseHelperAdapter.updateUserRole(customerId, userId);
    return userId;
  }

  /**
   * Create a new employee and add it to the database.
   *
   * @param name the employee's name.
   * @param age the employee's age.
   * @param address the employee's address.
   * @param password the employee's password.
   * @return the employee's user ID.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if the employee cannot be inserted into the database.
   */
  public int createEmployee(String name, int age, String address, String password)
      throws SQLException, DatabaseInsertException {

    int userId = -1;
    userId = DatabaseHelperAdapter.insertNewUser(name, age, address, password);
    int employeeId = DatabaseHelperAdapter.getRoleIdByName("EMPLOYEE");
    DatabaseHelperAdapter.updateUserRole(employeeId, userId);
    return userId;
  }

  /**
   * Create an account for a customer.
   *
   * @param customerId the customer for which to make the account.
   * @return the account id if successful, -1 otherwise.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public int createAccount(int customerId) throws SQLException {

    try {
      if (DatabaseHelperAdapter.getUserRoleId(customerId) != -1
          && DatabaseHelperAdapter.getUserRoleId(customerId)
              == DatabaseHelperAdapter.getRoleIdByName(Roles.CUSTOMER.name())) {
        return DatabaseHelperAdapter.insertAccount(customerId, true);
      }
    } catch (DatabaseInsertException e) {
      return -1;
    }

    return -1;
  }
}
