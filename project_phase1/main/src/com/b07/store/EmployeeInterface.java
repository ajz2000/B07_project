package com.b07.store;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.NotAuthenticatedException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.users.Employee;
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
   * @throws NotAuthenticatedException if the associated employee is not authenticated.
   */
  public EmployeeInterface(Employee employee, Inventory inventory)
      throws NotAuthenticatedException {

    if (employee.getAuthenticated()) {
      this.inventory = inventory;
      currentEmployee = employee;
    } else {
      throw new NotAuthenticatedException();
    }
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
    if (employee.getAuthenticated()) {
      currentEmployee = employee;
    }
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
   * @throws DatabaseInsertException on failure with insertion.
   */
  public boolean restockInventory(Item item, int quantity) throws SQLException {
    try {
      DatabaseUpdateHelper.updateInventoryQuantity(quantity, item.getId());
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
   * @throws CreationFailedException if the customer cannot be created.
   */
  public int createCustomer(String name, int age, String address, String password)
      throws SQLException{

    int userId = -1;

    try {
      userId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
      int customerId = DatabaseSelectHelper.getRoleIdByName("CUSTOMER");
      DatabaseUpdateHelper.updateUserRole(customerId, userId);
    } catch (Exception e) {
      return -1;
    }

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
   * @throws CreationFailedException if the employee cannot be created.
   */
  public int createEmployee(String name, int age, String address, String password)
      throws SQLException {

    int userId = -1;

    try {
      userId = DatabaseInsertHelper.insertNewUser(name, age, address, password);
      int employeeId = DatabaseSelectHelper.getRoleIdByName("EMPLOYEE");
      DatabaseUpdateHelper.updateUserRole(employeeId, userId);
    } catch (Exception e) {
      return -1;
    }

    return userId;
  }
}
