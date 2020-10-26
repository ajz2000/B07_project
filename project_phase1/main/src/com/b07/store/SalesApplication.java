package com.b07.store;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.ConnectionFailedException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.NotAuthenticatedException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.Employee;
import com.b07.users.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class SalesApplication {
  /**
   * This is the main method to run your entire program! Follow the "Pulling it together"
   * instructions to finish this off.
   * 
   * @param argv the mode the user would like to enter.
   */
  public static void main(String[] argv) {

    Connection connection = DatabaseDriverExtender.connectOrCreateDataBase();
    if (connection == null) {
      System.out.print("Erorr establishing initial database connection");
    }
    try {
      String selection = "0";
      if (argv.length != 0) {
        selection = argv[0];
      }

      if (selection.equals("-1")) {
        // Setup mode
        firstTimeSetup(connection);

      } else if (selection.equals("-1")) {
        // Admin mode
        adminMode();

      } else {
        // Customer/Employee mode
        System.out.println("Welcome to Sales Application");
        System.out.println("----------------------------");
        boolean exit = false;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        while (!exit) {
          System.out.println("what would you like to do?");
          System.out.println("1 - Employee Login");
          System.out.println("2 - Customer Login");
          System.out.println("0 - Exit");
          input = bufferedReader.readLine();
          if (input.equals("1")) {
            try {
              // Enter employee mode
              employeeMode();
            } catch (NotAuthenticatedException e) {
              System.out.println("Employee ID/Password Invalid!");
            }
          } else if (input.equals("2")) {
            // Enter customer mode
            customerMode();
          } else if (input.equals("0")) {
            // Terminate the program
            System.out.println("Goodbye!");
            exit = true;
          } else {
            System.out.println("Invalid Selection!");
          }

        }

      }
    } catch (SQLException e) {
      System.out.println("An issue occured while communicating with the database");
      System.out.println("The program will now exit.");
    } catch (DatabaseInsertException e) {
      System.out.println("An unknown issue occured while modifying the database");
      System.out.println("The program will now exit.");
    } catch (IOException e) {
      System.out.println("An unknown issue occured while obtaining user input");
      System.out.println("The program will now exit.");
    } catch (ConnectionFailedException e) {
      System.out.println("An issue occured while creating the database");
      System.out.println("The program will now exit.");
    } finally {
      try {
        connection.close();
      } catch (Exception e) {
        System.out.println("Looks like it was closed already :)");
      }
    }
  }

  /**
   * Exit the program and close the database, worst case scenario.
   * 
   * @param connection the connection to be closed.
   */
  public static void exitOnFailure(Connection connection) {
    try {
      connection.close();
    } catch (SQLException e) {
      System.out.println("Database was already closed");
      System.exit(0);
    }
  }

  /**
   * Initialize the database.
   * 
   * @param connection the connection to the database.
   * @throws DatabaseInsertException if there is an issue inserting into the database.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws ConnectionFailedException if there is an issue connecting to the database.
   * @throws IOException if there is an issue obtaining user input.
   */
  public static void firstTimeSetup(Connection connection)
      throws DatabaseInsertException, SQLException, ConnectionFailedException, IOException {

    System.out.println("First run setup");
    System.out.println("---------------");
    DatabaseDriverExtender.initialize(connection);
    // Creating roles

    DatabaseInsertHelper.insertRole("ADMIN");
    DatabaseInsertHelper.insertRole("CUSTOMER");
    DatabaseInsertHelper.insertRole("EMPLOYEE");


    System.out.println("Creating administrator account");

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    boolean exit = false;
    int id = 0;
    while (!exit) {

      // getting administrator's data
      System.out.println("Enter administrator name:");
      String name = bufferedReader.readLine();
      System.out.println("Enter administrator age:");
      String age = bufferedReader.readLine();
      System.out.println("Enter administrator address:");
      String address = bufferedReader.readLine();
      System.out.println("Enter administrator password:");
      String password = bufferedReader.readLine();

      // Creating database admin
      try {
        int ageInt = Integer.parseInt(age);
        id = DatabaseInsertHelper.insertNewUser(name, ageInt, address, password);
        exit = true;
        System.out.println("Created new Admin, ID: " + id);

      } catch (NumberFormatException e) {
        System.out.println("Invalid input:");
        System.out.println("A valid number must be entered for age");
      }

    }

    int adminRoleId = DatabaseSelectHelper.getRoleIdByName("ADMIN");
    DatabaseInsertHelper.insertUserRole(id, adminRoleId);

    System.out.println("Creating employee account");

    exit = false;
    id = 0;

    while (!exit) {
      // getting employee data
      System.out.println("Enter employee name:");
      String name = bufferedReader.readLine();
      System.out.println("Enter employee age:");
      String age = bufferedReader.readLine();
      System.out.println("Enter employee address:");
      String address = bufferedReader.readLine();
      System.out.println("Enter employee password:");
      String password = bufferedReader.readLine();

      // creating first employee
      try {
        int ageInt = Integer.parseInt(age);
        id = DatabaseInsertHelper.insertNewUser(name, ageInt, address, password);
        exit = true;
        System.out.println("Created new Empolyee, ID: " + id);

      } catch (NumberFormatException e) {
        System.out.println("Invalid input:");
        System.out.println("A valid number must be entered for age");
      }

    }

    int employeeRoleId = DatabaseSelectHelper.getRoleIdByName("EMPLOYEE");
    DatabaseInsertHelper.insertUserRole(id, employeeRoleId);


    // Adding required items to database
    // Creating dummy stock

    int itemId;
    itemId = DatabaseInsertHelper.insertItem("FISHING_ROD", new BigDecimal("10.00"));
    DatabaseInsertHelper.insertInventory(itemId, 100);
    itemId = DatabaseInsertHelper.insertItem("HOCKEY_STICK", new BigDecimal("10.00"));
    DatabaseInsertHelper.insertInventory(itemId, 100);
    itemId = DatabaseInsertHelper.insertItem("SKATES", new BigDecimal("10.00"));
    DatabaseInsertHelper.insertInventory(itemId, 100);
    itemId = DatabaseInsertHelper.insertItem("RUNNING_SHOES", new BigDecimal("10.00"));
    DatabaseInsertHelper.insertInventory(itemId, 100);
    itemId = DatabaseInsertHelper.insertItem("PROTEIN_BAR", new BigDecimal("10.00"));
    DatabaseInsertHelper.insertInventory(itemId, 100);


    System.out.println("Database creation was successfull!");

  }

  /**
   * Allow administrators to promote users to admin status.
   * 
   * @throws SQLException if there is an issue communicating with the database.
   * @throws IOException if there is an issue obtaining user input.
   */
  public static void adminMode() throws SQLException, IOException {
    boolean exit = false;
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String input = "";
    Admin admin = null;

    System.out.println("Admin Mode: Log in");
    System.out.println("---------------");

    // Obtain a valid admin ID
    while (!exit) {
      System.out.println("Enter admin ID");
      String adminId = bufferedReader.readLine();
      try {
        int adminIdInt = Integer.parseInt(adminId);

        User user = DatabaseSelectHelper.getUserDetails(adminIdInt);
        if (user instanceof Admin) {
          admin = (Admin) user;
          exit = true;
        } else {
          System.out.println("Not an admin!");
        }
      } catch (NumberFormatException e) {
        System.out.println("A valid number must be entered");
      }
    }

    // Get password in order to authenticate admin
    exit = false;
    while (!exit) {
      System.out.println("Enter password");
      String password = bufferedReader.readLine();
      if (admin.authenticate(password) == true) {
        exit = true;
      } else {
        System.out.println("Invalid password!");
      }
    }

    System.out.println("Admin validated!");

    // Allow admin to promote employees
    exit = false;
    while (!exit) {
      System.out.println("Type '1' to promote an employee to admin");
      System.out.println("Type anything else to exit");
      input = bufferedReader.readLine();
      if (input.equals("1")) {
        System.out.println("Enter the id of the user you would like to promote:");
        input = bufferedReader.readLine();

        int employeeId = Integer.parseInt(input);
        User toPromote = DatabaseSelectHelper.getUserDetails(employeeId);
        if (toPromote instanceof Employee) {
          admin.promoteEmployee((Employee) toPromote);
          System.out.println("Employee promoted succesfully!");
        } else {
          System.out.println("Not an employee!");
        }

      } else {
        exit = true;
      }
    }
  }

  /**
   * Allows employees to perform operations on the database.
   * 
   * @throws IOException if there is an issue obtaining user input.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws NotAuthenticatedException if the employee cannot be authenticated.
   * @throws DatabaseInsertException if there is an issue inserting into the database.
   */
  public static void employeeMode()
      throws IOException, SQLException, NotAuthenticatedException, DatabaseInsertException {

    // Authenticate and get employeeInterface
    EmployeeInterface inter = employeeModeAuthenticate();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    // Allow employee to perform operations
    System.out.println("Welcome to the employee interface");
    System.out.println("---------------------------------");
    boolean exit = false;
    while (!exit) {
      System.out.println("What would you like to do?");
      System.out.println("1. authenticate new employee");
      System.out.println("2. Make new User");
      System.out.println("3. Make new account");
      System.out.println("4. Make new Employee");
      System.out.println("5. Restock Inventory");
      System.out.println("6. Exit");

      String input = bufferedReader.readLine();

      if (input.equals("1")) {
        // Authenticate new employee
        inter = employeeModeAuthenticate();

      } else if (input.equals("2") || input.equals("3")) {
        // Create new customer

        // get customer data
        System.out.println("Enter customer name:");
        String name = bufferedReader.readLine();
        System.out.println("Enter customer age:");
        String age = bufferedReader.readLine();
        System.out.println("Enter customer address:");
        String address = bufferedReader.readLine();
        System.out.println("Enter customer password:");
        String password = bufferedReader.readLine();


        int ageInt = Integer.parseInt(age);
        int id = DatabaseInsertHelper.insertNewUser(name, ageInt, address, password);
        int customerRoleId = DatabaseSelectHelper.getRoleIdByName("CUSTOMER");
        DatabaseInsertHelper.insertUserRole(id, customerRoleId);
        System.out.println("New customer created, ID: " + id);


      } else if (input.equals("4")) {
        // Create new employee

        // get employee data
        System.out.println("Enter employee name:");
        String name = bufferedReader.readLine();
        System.out.println("Enter employee age:");
        String age = bufferedReader.readLine();
        System.out.println("Enter employee address:");
        String address = bufferedReader.readLine();
        System.out.println("Enter employee password:");
        String password = bufferedReader.readLine();

        // Create employee and set role
        try {
          int ageInt = Integer.parseInt(age);
          int id = DatabaseInsertHelper.insertNewUser(name, ageInt, address, password);
          int employeeRoleId = DatabaseSelectHelper.getRoleIdByName("EMPLOYEE");
          DatabaseInsertHelper.insertUserRole(id, employeeRoleId);
          System.out.println("New Employee created, ID: " + id);
        } catch (NumberFormatException e) {
          System.out.println("Could not create new customer");
          System.out.println("Invalid input:");
          System.out.println("Age must be a valid number");
        }
      } else if (input.equals("5")) {
        // Allow user to restock items
        List<Item> items = DatabaseSelectHelper.getAllItems();
        System.out.println("The following are the current items and their " + "IDs");
        for (int i = 0; i < items.size(); i++) {
          System.out.println(items.get(i).getName());
          System.out.println("ID: " + items.get(i).getId());
        }
        System.out.println("Enter the ID of the item you would like to restock");
        String toStock = bufferedReader.readLine();
        System.out.println("Enter the qauntity of the item you would like to restock");
        String quantity = bufferedReader.readLine();
        try {
          int itemId = Integer.parseInt(toStock);
          int quantityInt = Integer.parseInt(quantity);
          int current = DatabaseSelectHelper.getInventoryQuantity(itemId);
          Item item = DatabaseSelectHelper.getItem(itemId);
          inter.restockInventory(item, quantityInt + current);
          System.out.println("Successfully restocked!");
        } catch (NumberFormatException e) {
          System.out.println("Must enter a valid number!");
        }
      } else if (input.equals("6")) {
        // Exit
        exit = true;
      }
    }

  }

  /**
   * Attempt to authenticate a user for employee mode.
   * 
   * @return an EmployeeInterface for the authenticated user.
   * @throws NotAuthenticatedException if the user cannot be authenticated.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws IOException if there is an issue obtaining user input.
   */
  public static EmployeeInterface employeeModeAuthenticate()
      throws NotAuthenticatedException, SQLException, IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    Employee employee = null;


    // Employee Login
    System.out.println("Enter employee ID");
    String employeeId = bufferedReader.readLine();
    System.out.println("Enter password");
    String password = bufferedReader.readLine();

    // Validate and authenticate an employee
    try {
      int employeeIdInt = Integer.parseInt(employeeId);
      User user = DatabaseSelectHelper.getUserDetails(employeeIdInt);
      if (user instanceof Employee) {
        employee = (Employee) user;
        employee.authenticate(password);
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid ID, must be a number!");
    }
    if (employee == null) {
      throw new NotAuthenticatedException();
    }
    Inventory inventory = DatabaseSelectHelper.getInventory();
    return new EmployeeInterface(employee, inventory);
  }

  /**
   * Allows customers to use a cart and make purchases.
   * 
   * @throws IOException if there is an issue obtaining user input.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static void customerMode() throws IOException, SQLException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    Customer customer = null;
    ShoppingCart cart = null;

    // Allow user to log in
    boolean exit = false;
    while (!exit) {
      System.out.println("Enter customer ID");
      String customerId = bufferedReader.readLine();
      System.out.println("Enter password");
      String password = bufferedReader.readLine();

      // Validate and authenticate user
      try {
        int customerIdInt = Integer.parseInt(customerId);
        User user = DatabaseSelectHelper.getUserDetails(customerIdInt);
        if (user instanceof Customer) {
          customer = (Customer) user;
        } else {
          System.out.println("Not a customer!");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid ID, must be a number!");
      }
      if (customer != null && customer.authenticate(password)) {

        try {
          cart = new ShoppingCart(customer);
          exit = true;
        } catch (NotAuthenticatedException e) {
          System.out.println("Authentication failed, try again!");
        }
      } else {
        System.out.println("Authentication failed, try again!");
      }
    }

    // Allow user to interact with cart, database
    exit = false;
    while (!exit) {
      System.out.println("Welcome to the customer interface");
      System.out.println("---------------------------------");
      System.out.println("What would you like to do?");
      System.out.println("1. List current items in cart");
      System.out.println("2. Add a quantity of an item to the cart");
      System.out.println("3. Check total price of items in the cart");
      System.out.println("4. Remove a quantity of an item from the cart");
      System.out.println("5. check out ");
      System.out.println("6. Exit");

      String input = bufferedReader.readLine();
      if (input.equals("1")) {
        // List items in cart
        System.out.println("Current Cart:");
        List<Item> items = cart.getItems();
        for (int i = 0; i < items.size(); i++) {
          System.out.println(items.get(i).getName());
        }
      } else if (input.equals("2")) {
        // Allow user to add items to cart
        List<Item> items = DatabaseSelectHelper.getAllItems();
        System.out.println("The following are the current items and their " + "IDs");
        for (int i = 0; i < items.size(); i++) {
          System.out.println(items.get(i).getName());
          System.out.println("ID: " + items.get(i).getId());
        }
        System.out.println("Enter the ID of the item you would like to add");
        String toStock = bufferedReader.readLine();
        System.out.println("Enter the qauntity of the item you would like to add");
        String quantity = bufferedReader.readLine();
        try {
          int itemId = Integer.parseInt(toStock);
          int quantityInt = Integer.parseInt(quantity);
          Item item = DatabaseSelectHelper.getItem(itemId);
          cart.addItem(item, quantityInt);
          System.out.println("Successfully added!");
        } catch (NumberFormatException e) {
          System.out.println("Must enter a valid number!");
        }
      } else if (input.equals("3")) {
        // Display cart total price
        System.out.println("Your total is:");
        System.out.println(cart.getTotal());
      } else if (input.equals("4")) {
        // Allow users to remove items from cart
        List<Item> items = DatabaseSelectHelper.getAllItems();
        System.out.println("The following are the current items and their " + "IDs");
        for (int i = 0; i < items.size(); i++) {
          System.out.println(items.get(i).getName());
          System.out.println("ID: " + items.get(i).getId());
        }
        System.out.println("Enter the ID of the item you would like to remove");
        String toStock = bufferedReader.readLine();
        System.out.println("Enter the qauntity of the item you would like to remove");
        String quantity = bufferedReader.readLine();
        try {
          int itemId = Integer.parseInt(toStock);
          int quantityInt = Integer.parseInt(quantity);
          Item item = DatabaseSelectHelper.getItem(itemId);
          cart.removeItem(item, quantityInt);
          System.out.println("Successfully removed!");
        } catch (NumberFormatException e) {
          System.out.println("Must enter a valid number!");
        }
      } else if (input.equals("5")) {
        // Allow user to check out
        System.out.println("Your total is:");
        System.out.println("$" + cart.getTotal());
        System.out.println("You will also pay taxes to the order of:");
        System.out.println("$" + cart.getTotal().multiply(cart.getTaxRate()));
        boolean checkedOut = cart.checkOutCart();
        if (checkedOut) {
          // Prompt user to continue shopping
          System.out.println("Your order has been checked out!");
          System.out.println("Enter 1 if you would like to continue shopping");
          System.out.println("Enter any other value if you would like to exit");
          input = bufferedReader.readLine();
          if (!input.equals("1")) {
            exit = true;
            cart.clearCart();
          }
        } else {
          System.out.println("Sorry, your cart could not be checked out at this time");
        }
      } else if (input.equals("6")) {
        // exit
        exit = true;
      }
    }
  }
}
