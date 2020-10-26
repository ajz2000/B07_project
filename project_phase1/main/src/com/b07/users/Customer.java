package com.b07.users;

/**
 * A class representing a customer within the sales application system.
 * 
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class Customer extends User {
  /**
   * Create a new customer, without setting their authentication value.
   * 
   * @param id the id of the customer to be created.
   * @param name the name of the customer to be created.
   * @param age the age of the customer to be created.
   * @param address the address of the customer to be created.
   */
  public Customer(int id, String name, int age, String address) {
    setId(id);
    setName(name);
    setAge(age);
    setAddress(address);
  }

  /**
   * Create a new customer, and set their authentication value.
   * 
   * @param id id the id of the customer to be created.
   * @param name the name of the customer to be created.
   * @param age the age of the customer to be created.
   * @param address the address of the customer to be created.
   * @param authenticated whether the customer should be created authenticated or not.
   */
  public Customer(int id, String name, int age, String address, boolean authenticated) {
    setId(id);
    setName(name);
    setAge(age);
    setAddress(address);
    setAuthenticated(authenticated);
  }
}
