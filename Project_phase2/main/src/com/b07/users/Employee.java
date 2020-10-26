package com.b07.users;

/**
 * A class representing an employee within the sales application system.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class Employee extends User {
  /**
   * Create a new employee, without setting their authentication value.
   *
   * @param id the id of the employee to be created.
   * @param name the name of the employee to be created.
   * @param age the age of the employee to be created.
   * @param address the address of the employee to be created.
   */
  public Employee(int id, String name, int age, String address) {
    setId(id);
    setName(name);
    setAge(age);
    setAddress(address);
  }

  /**
   * Create a new employee, and set their authentication value.
   *
   * @param id id the id of the employee to be created.
   * @param name the name of the employee to be created.
   * @param age the age of the employee to be created.
   * @param address the address of the employee to be created.
   * @param authenticated whether the employee should be created authenticated or not.
   */
  public Employee(int id, String name, int age, String address, boolean authenticated) {
    setId(id);
    setName(name);
    setAge(age);
    setAddress(address);
  }
}
