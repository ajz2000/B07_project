package com.b07.store;

import com.b07.database.helper.DatabaseDesktopHelper;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.exceptions.ConnectionFailedException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.DifferentEnumException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.inventory.ItemTypes;
import com.b07.serialize.SerializationPasswordHelper;
import com.b07.serialize.SerializeDatabase;
import com.b07.users.Account;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.Employee;
import com.b07.users.Roles;
import com.b07.users.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SalesApplication {
  /**
   * This is the main method to run your entire program! Follow the "Pulling it together"
   * instructions to finish this off.
   *
   * @param argv the mode the user would like to enter.
   */
  public static void main(String[] argv) {

    DatabaseHelperAdapter.setPlatformHelper(new DatabaseDesktopHelper());
    Connection connection = DatabaseDriverExtender.connectOrCreateDataBase();
    if (connection == null) {
      System.out.print("Error establishing initial database connection");
    }
    try {
      String selection = "0";
      if (argv.length != 0) {
        selection = argv[0];
      }

      if (selection.equals("-1")) {
        // mode
        firstTimeSetup(connection);

      } else if (selection.equals("1")) {
        // Admin mode
        adminMode(new BufferedReader(new InputStreamReader(System.in)));

      } else {

        // Customer/Employee mode
        System.out.println("Welcome to Sales Application");
        System.out.println("----------------------------");
        String[] loginOptions = {
          "1 - Employee Login", "2 - Customer Login", "0 - Exit", "Enter Selection:"
        };
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // input will contain the first line of input from the user
        int input = StoreHelpers.choicePrompt(loginOptions, reader);
        while (input != 0) {
          if (input == 1) {
            employeeMode(reader);
          } else if (input == 2) {
            customerMode(reader);
          } else {
            System.out.println("Invalid selection");
          }
          input = StoreHelpers.choicePrompt(loginOptions, reader);
        }
        System.out.println("Exiting");
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
      throws SQLException, ConnectionFailedException, IOException {

    System.out.println("First run setup");
    System.out.println("---------------");
    DatabaseDriverExtender.initialize(connection);

    // Creating roles

    try {
      DatabaseHelperAdapter.insertRole("ADMIN");
      DatabaseHelperAdapter.insertRole("CUSTOMER");
      DatabaseHelperAdapter.insertRole("EMPLOYEE");
    } catch (DatabaseInsertException e1) {
      System.out.println("Unable to insert role into database");
      exitOnFailure(connection);
    }

    // Creating discount types
    try {
      for (DiscountTypes type : DiscountTypes.values()) {
        DatabaseHelperAdapter.insertDiscountType(type.name());
      }
    } catch (DatabaseInsertException e1) {
      System.out.println("Unable to insert discount type into database");
      exitOnFailure(connection);
    }

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
        id = DatabaseHelperAdapter.insertNewUser(name, ageInt, address, password);
        exit = true;
        System.out.println("Created new Admin, ID: " + id);
        int adminRoleId = DatabaseHelperAdapter.getRoleIdByName("ADMIN");
        DatabaseHelperAdapter.insertUserRole(id, adminRoleId);
      } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number");
      } catch (DatabaseInsertException e1) {
        System.out.println("Unable to insert user or user role into database");
      }
    }

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
        id = DatabaseHelperAdapter.insertNewUser(name, ageInt, address, password);
        exit = true;
        int employeeRoleId = DatabaseHelperAdapter.getRoleIdByName("EMPLOYEE");
        DatabaseHelperAdapter.insertUserRole(id, employeeRoleId);
        System.out.println("Created new Empolyee, ID: " + id);
      } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number");
      } catch (DatabaseInsertException e1) {
        System.out.println("Unable to insert user or user role into database");
      }
    }

    try {
      int itemId;
      for (ItemTypes itemType : ItemTypes.values()) {
        itemId = DatabaseHelperAdapter.insertItem(itemType.toString(), new BigDecimal("10.00"));
        DatabaseHelperAdapter.insertInventory(itemId, 0);
      }
    } catch (DatabaseInsertException e) {
      System.out.println("An issue occured while populating database Items!");
      System.out.println("Creation could not be completed, program will now exit");
      e.printStackTrace();
      exitOnFailure(connection);
    }

    System.out.println("Database creation was successfull!");
  }

  /**
   * Allow administrators to promote users to admin status.
   *
   * @throws SQLException if there is an issue communicating with the database.
   * @throws IOException if there is an issue obtaining user input.
   */
  public static void adminMode(BufferedReader bufferedReader) throws SQLException, IOException {
    System.out.println("Admin Mode: Log in");
    System.out.println("---------------");

    Admin admin = null;
    User user = StoreHelpers.loginPrompt(bufferedReader, Roles.ADMIN);
    admin = null;
    if (user != null && user instanceof Admin) {
      admin = (Admin) user;
    }
    if (admin == null) {
      System.out.println("Incorrect role. Please use the correct login for your role.");
      return;
    }

    String[] adminOptions = {
      "1. Promote employee to admin",
      "2. View Books",
      "3. Generate Coupons",
      "4. View Customer Accounts",
      "5. Backup database",
      "6. Restore database",
      "0. Exit"
    };
    int input = StoreHelpers.choicePrompt(adminOptions, bufferedReader);
    while (input != 0) {
      if (input == 1) {
        System.out.println("Enter the id of the user you would like to promote:");
        try {
          int employeeId = Integer.parseInt(bufferedReader.readLine());
          User toPromote = DatabaseHelperAdapter.getUserDetails(employeeId);
          if (toPromote instanceof Employee) {
            admin.promoteEmployee((Employee) toPromote);
            System.out.println("Employee promoted successfully!");
          } else {
            System.out.println("Only employees may be promoted!");
          }
        } catch (NumberFormatException e) {
          System.out.println("Please enter an ID number.");
        }
      } else if (input == 2) {
        SalesLog salesLog = DatabaseHelperAdapter.getSales();
        System.out.println(salesLog.viewBooks());
      } else if (input == 3) {
        System.out.println("Enter an item ID to add a coupon for:");
        String itemIdString = bufferedReader.readLine();
        System.out.println("Enter number of uses");
        String usesString = bufferedReader.readLine();
        System.out.println("Enter discount type. " + "Valid options are:");
        for (DiscountTypes type : DiscountTypes.values()) {
          System.out.println(type.name());
        }
        String type = bufferedReader.readLine();
        System.out.println("Enter discount amount:");
        String discountString = bufferedReader.readLine();
        System.out.println("Please ensure that the coupon code has not been used before");
        // if the coupon code is not unique, the first coupon with the given code is retrieved upon
        // using the code, which may be the incorrect coupon
        System.out.println("Enter coupon code:");
        String code = bufferedReader.readLine();

        try {
          int itemId = Integer.parseInt(itemIdString);
          int uses = Integer.parseInt(usesString);
          BigDecimal discount = new BigDecimal(discountString);
          int couponId = DatabaseHelperAdapter.insertCoupon(itemId, uses, type, discount, code);
          System.out.println("Created new coupon, ID: " + couponId);
        } catch (NumberFormatException e) {
          System.out.println("Please enter a valid number");
        } catch (DatabaseInsertException e1) {
          System.out.println("The item doesn't exist or the number of uses is invalid.");
        }

      } else if (input == 4) {
        System.out.println("Input a customer ID");
        String idString = bufferedReader.readLine();
        String[] customerCartChoices = {"1. Active", "2. Inactive"};
        int active = StoreHelpers.choicePrompt(customerCartChoices, bufferedReader);
        try {
          int userId = Integer.parseInt(idString);
          if (active == 1) {
            System.out.println(DatabaseHelperAdapter.getUserActiveAccounts(userId));
          } else if (active == 0) {
            System.out.println(DatabaseHelperAdapter.getUserInactiveAccounts(userId));
          } else {
            System.out.println("Unknown option");
          }
        } catch (Exception e) {
          // TODO Auto-generated catch blo
          e.printStackTrace();
        }
      } else if (input == 5) {
        System.out.println("Enter a directory to save the file to, do not add a trailing slash");
        String location = bufferedReader.readLine();
        try {
          SerializeDatabase.serializeToFile(location);
        } catch (IOException e) {
          System.out.println("Unable to save file");
        } catch (SQLException e) {
          System.out.println("Something went wrong while retreiving data");
        }

      } else if (input == 6) {
        System.out.println(
            "Please manually make a backup of the database file before proceeding incase reverting fails");
        System.out.println(
            "Enter a directory to retreive the backup from, do not add a trailing slash");
        String location = bufferedReader.readLine();
        String adminHashedPassword = DatabaseHelperAdapter.getPassword(admin.getId());
        try {
          SerializeDatabase.populateFromFile(location);
          List<User> allUsers = DatabaseHelperAdapter.getUsersDetails();
          List<Admin> adminUsers = new ArrayList<Admin>();
          for (User currUser : allUsers) {
            if (currUser instanceof Admin) {
              adminUsers.add((Admin) currUser);
            }
          }
          boolean reinsert = true;
          for (Admin currAdmin : adminUsers) {
            if (currAdmin.getName() == admin.getName()
                && currAdmin.getAge() == admin.getAge()
                && currAdmin.getAddress() == admin.getAddress()) {
              if (DatabaseHelperAdapter.getPassword(currAdmin.getId()) == adminHashedPassword) {
                reinsert = false;
              }
            }
          }
          if (reinsert) {
            System.out.println(
                "You are not currently part of the database, do you wish to reinsert yourself? Y/n");
            String reinsertChoice = bufferedReader.readLine();
            while (!(reinsertChoice.equals("Y") || reinsertChoice.equals("n"))) {
              System.out.println("Please input a valid choice");
              reinsertChoice = bufferedReader.readLine();
            }
            if (reinsertChoice.equals("Y")) {
              // TODO: temp password
              int adminId = 0;
              adminId =
                  SerializationPasswordHelper.insertUserNoHash(
                      admin.getName(), admin.getAge(), admin.getAddress(), adminHashedPassword);
              DatabaseHelperAdapter.insertUserRole(
                  adminId, DatabaseHelperAdapter.getRoleIdByName("ADMIN"));
              System.out.println("Your new ID is: " + adminId);
            } else if (reinsertChoice.equals("n")) {
              System.out.println("Exiting");
              return;
            }
          }
        } catch (IOException e) {
          System.out.println("Unable to read data from file");
          e.printStackTrace();
        } catch (SQLException e) {
          System.out.println("Something went wrong while setting data in the database");
          // e.printStackTrace();
        } catch (ClassNotFoundException e) {
          System.out.println("Unable to find a necessary part of the application");
          // e.printStackTrace();
        } catch (DifferentEnumException e) {
          System.out.println("This application does not support the given data");
          // e.printStackTrace();
        } catch (DatabaseInsertException e) {
          // TODO Auto-generated catch block
          System.out.println("Failed to reinsert admin");
          // e.printStackTrace();
        }
      } else if (input == 0) {
        System.out.println("Exiting");
        return;
      }
      input = StoreHelpers.choicePrompt(adminOptions, bufferedReader);
    }
    System.out.println("Exiting");
  }

  /**
   * Allows employees to perform operations on the database.
   *
   * @throws IOException if there is an issue obtaining user input.
   * @throws SQLException if there is an issue communicating with the database.
   * @throws DatabaseInsertException if there is an issue inserting into the database.
   */
  public static void employeeMode(BufferedReader reader)
      throws IOException, SQLException, DatabaseInsertException {

    System.out.print("Employee Login:");
    User user = StoreHelpers.loginPrompt(reader, Roles.EMPLOYEE);
    Employee employee = null;
    if (user != null && user instanceof Employee) {
      employee = (Employee) user;
    }
    if (employee == null) {
      return;
    }
    Inventory inventory = DatabaseHelperAdapter.getInventory();
    EmployeeInterface employeeInterface = new EmployeeInterface(employee, inventory);
    System.out.println("Employee options:");

    String[] employeeOptions = {
      "1 - authenticate new employee",
      "2 - Make new User",
      "3 - Make new account",
      "4 - Make new Employee",
      "5 - Restock Inventory",
      "0- Exit",
      "Enter Selection:"
    };
    int input = StoreHelpers.choicePrompt(employeeOptions, reader);
    while (input != 0) {
      if (input == 1) {
        employee = (Employee) StoreHelpers.loginPrompt(reader, Roles.EMPLOYEE);
        if (employee != null) {
          employeeInterface.setCurrentEmployee(employee);
        } else {
          System.out.println("Failed to authenticate new employee");
        }

      } else if (input == 2) {
        System.out.println("Creating a new customer");
        System.out.println("Enter a name");
        String name = reader.readLine();
        boolean validAge = false;
        int age = 0;
        while (!validAge) {
          System.out.println("Enter an age");
          try {
            age = Integer.parseInt(reader.readLine());
            validAge = true;
          } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number");
          }
        }
        System.out.println("Enter an address");
        String address = reader.readLine();
        System.out.println("Enter a password");
        String password = reader.readLine();
        // Here, an employee is being created and then having it's role changed
        // to a a customer. this is bad, it must be changed.
        insertUser:
        try {
          int userId = employeeInterface.createEmployee(name, age, address, password);
          if (userId == -1) {
            break insertUser;
          }
          int roleId = DatabaseHelperAdapter.getRoleIdByName("CUSTOMER");
          if (userId == -1) {
            // should never run: included here due to UML-unstable EmployeeInterface change
            System.out.println("Unable to retrieve the role ID.");
            break insertUser;
          }
          DatabaseHelperAdapter.insertUserRole(userId, roleId);
          System.out.println("New Customer created with ID: " + userId);
        } catch (DatabaseInsertException e) {
          System.out.println("Unable to create an employee with the given parameters.");
        }

      } else if (input == 3) {
        System.out.println("Creating a new account for a customer");
        System.out.println("Enter the ID of the customer with which the account is associated");
        String id = reader.readLine();
        try {
          int idInt = Integer.parseInt(id);
          int result = employeeInterface.createAccount(idInt);
          if (result == -1) {
            System.out.println("Account could not be created.");
          } else {
            System.out.println("New account created for user: " + idInt);
            System.out.println("Account ID is: " + result);
          }
        } catch (NumberFormatException e) {
          System.out.println("Must enter a valid numeric ID!");
        }
      } else if (input == 4) {
        System.out.println("Creating a new Employee");
        System.out.println("Enter a name");
        String name = reader.readLine();
        boolean validAge = false;
        int age = 0;
        while (!validAge) {
          System.out.println("Enter an age");
          try {
            age = Integer.parseInt(reader.readLine());
            validAge = true;
          } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number");
          }
        }
        System.out.println("Enter an address");
        String address = reader.readLine();
        System.out.println("Enter a password");
        String password = reader.readLine();
        insertUser:
        try {
          int userId = employeeInterface.createEmployee(name, age, address, password);
          if (userId == -1) {
            // should never run: included here due to UML-unstable EmployeeInterface change
            break insertUser;
          }
          int roleId = DatabaseHelperAdapter.getRoleIdByName("EMPLOYEE");
          if (userId == -1) {
            System.out.println("Unable to retrieve the role ID.");
            break insertUser;
          }
          DatabaseHelperAdapter.insertUserRole(userId, roleId);
          System.out.println("New Employee created with ID: " + userId);
        } catch (DatabaseInsertException e) {
          System.out.println("Unable to create an employee with the given parameters.");
        }
      } else if (input == 5) {

        try {
          System.out.println("Enter the ID of the item to restock");
          int id = Integer.parseInt(reader.readLine());
          Item item = DatabaseHelperAdapter.getItem(id);
          System.out.println("Enter a quantity of the item to restock");
          int quantity = Integer.parseInt(reader.readLine());
          employeeInterface.restockInventory(item, quantity);
        } catch (NumberFormatException e) {
          System.out.println("Please input a number");
        }
      }
      input = StoreHelpers.choicePrompt(employeeOptions, reader);
    }
  }

  /**
   * Allows customers to use a cart and make purchases.
   *
   * @throws IOException if there is an issue obtaining user input.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public static void customerMode(BufferedReader reader) throws IOException, SQLException {

    try {
      System.out.print("Customer Login:");
      User user = StoreHelpers.loginPrompt(reader, Roles.CUSTOMER);

      Customer customer = null;
      if (user != null && user instanceof Customer) {
        customer = (Customer) user;
      }
      if (customer == null) {
        System.out.println("Incorrect role. Please use the correct login for your role.");
        return;
      }
      ShoppingCart shoppingCart;

      // Checking user accounts for existing items.
      /*
       * if (AccountHelper.customerHasShoppingCarts(customer.getId())) {
       * System.out.println("It appears you have an existing shopping cart!");
       * System.out.println("Would you like to restore and check out the existing shopping cart?");
       * System.out.println("Enter '1' to check out, anything else to create a new cart"); String
       * restore = reader.readLine(); if (restore.equals("1")) { shoppingCart =
       * AccountHelper.retrieveCustomerCart(customer.getId()); if (shoppingCart != null) {
       * System.out.println("Your total is:"); System.out.println("$" + shoppingCart.getTotal());
       * System.out.println("You will also pay taxes to the order of:"); System.out.println("$" +
       * shoppingCart.getTotal().multiply(shoppingCart.getTaxRate()) .setScale(2,
       * RoundingMode.CEILING)); boolean checkedOut = false; checkedOut =
       * shoppingCart.checkOutCart(); if (checkedOut) {
       * System.out.println("Your order has been checked out!"); } else {
       * System.out.println("Sorry, your cart could not be checked out at this time");
       * System.out.println("There may not be enough of certain items in your cart in stock"); } }
       * else { System.out.println("Your shopping cart could not be recovered!"); } } }
       */

      // Checking user accounts for existing carts.
      List<Integer> accs = DatabaseHelperAdapter.getUserActiveAccounts(customer.getId());

      if (accs != null && !accs.isEmpty()) {
        System.out.println("It appears you have existing accounts!");
        System.out.println("Would you like to restore and check out an existing shopping cart?");
        System.out.println("Your currently open account IDs are:");
        for (int i = 0; i < accs.size(); i++) {
          System.out.println(accs.get(i));
        }
        System.out.println("Enter the ID of the account you would like to restore,");
        System.out.println("enter anything else to create a new cart and begin shopping");
        String restore = reader.readLine();
        try {
          int toRestore = Integer.parseInt(restore);
          if (accs.contains(toRestore)) {
            Account account = new Account(customer.getId(), toRestore, true);
            if (!account.retrieveCustomerCart()) {
              System.out.println("Something went wrong restoring your cart, sorry!");
            } else {
              shoppingCart = account.getCart();
              if (shoppingCart != null && !shoppingCart.getItems().isEmpty()) {
                System.out.println("Your total is:");
                System.out.println("$" + shoppingCart.getTotal());
                System.out.println("You will also pay taxes to the order of:");
                System.out.println(
                    "$"
                        + shoppingCart
                            .getTotal()
                            .multiply(shoppingCart.getTaxRate().setScale(2, RoundingMode.CEILING)));
                boolean checkedOut = false;
                checkedOut = shoppingCart.checkOutCart();
                if (checkedOut) {
                  System.out.println("Your order has been checked out!");
                  if (!account.deactivate()) {
                    System.out.println("Something went wrong deactivating your account!");
                  }
                } else {
                  System.out.println("Sorry, your cart could not be checked out at this time");
                }
              } else {
                System.out.println("Cannot checkout, cart is empty!");
              }
            }
          } else {
            System.out.println("Ok, continuing to shopping!");
          }
        } catch (NumberFormatException e) {
          System.out.println("Not a number! Continuing to shopping");
        }
      }

      shoppingCart = new ShoppingCart(customer);
      System.out.println("Customer options:");
      String[] customerOptions = {
        "1 - List items in cart",
        "2 - Add item to cart",
        "3 - Check price",
        "4 - Remove item from cart",
        "5 - Check out",
        "6 - Apply coupon",
        "0 - Exit",
        "Enter Selection:"
      };
      int input = StoreHelpers.choicePrompt(customerOptions, reader);
      while (input != 0) {
        if (input == 1) {
          System.out.println("Current Cart:");
          HashMap<Item, Integer> items = shoppingCart.getItemsWithQuantity();
          for (Item item : items.keySet()) {
            System.out.println(item.getName() + " Quantity: " + items.get(item));
          }

        } else if (input == 2) {
          // Allow user to add items to cart
          List<Item> items = DatabaseHelperAdapter.getAllItems();
          System.out.println("The following are the current items and their " + "IDs");
          for (int i = 0; i < items.size(); i++) {
            System.out.println(items.get(i).getName());
            System.out.println("ID: " + items.get(i).getId());
          }
          System.out.println("Enter the ID of the item you would like to add");
          String toStock = reader.readLine();
          System.out.println("Enter the quantity of the item you would like to add");
          String quantity = reader.readLine();
          try {
            int itemId = Integer.parseInt(toStock);
            int quantityInt = Integer.parseInt(quantity);
            Item item = DatabaseHelperAdapter.getItem(itemId);
            if (item != null) {
              shoppingCart.addItem(item, quantityInt);
              System.out.println("Successfully added!");
            } else {
              System.out.println("No Such Item!");
            }

          } catch (NumberFormatException e) {
            System.out.println("Must enter a valid number!");
          }
        } else if (input == 3) {
          // Display cart total price
          System.out.println("Your total is:");
          System.out.println(shoppingCart.getTotal());

        } else if (input == 4) {
          System.out.println("Note, your current cart is as follows:");
          HashMap<Item, Integer> items = shoppingCart.getItemsWithQuantity();
          for (Item item : items.keySet()) {
            System.out.println(item.getName() + " Quantity: " + items.get(item));
          }
          System.out.println("Enter the ID of the item you would like to remove");
          String toStock = reader.readLine();
          System.out.println("Enter the quantity of the item you would like to remove");
          String quantity = reader.readLine();
          try {
            int itemId = Integer.parseInt(toStock);
            int quantityInt = Integer.parseInt(quantity);
            Item item = DatabaseHelperAdapter.getItem(itemId);
            if (item == null) {
              System.out.println("No such item!");
            } else if (shoppingCart.removeItem(item, quantityInt)) {
              System.out.println("Successfully removed!");
            } else {
              System.out.println("Failed to remove item.");
            }
          } catch (NumberFormatException e) {
            System.out.println("Must enter a valid number!");
          }

        } else if (input == 5) {
          // Allow user to check out
          if (!shoppingCart.getItems().isEmpty()) {
            System.out.println("Your total is:");
            System.out.println("$" + shoppingCart.getTotal());
            System.out.println("You will also pay taxes to the order of:");
            System.out.println(
                "$"
                    + shoppingCart
                        .getTotal()
                        .multiply(shoppingCart.getTaxRate().setScale(2, RoundingMode.CEILING)));
            boolean checkedOut = false;
            checkedOut = shoppingCart.checkOutCart();
            if (checkedOut) {
              System.out.println("Your order has been checked out!");
            } else {
              System.out.println("Sorry, your cart could not be checked out at this time");
            }
          } else {
            System.out.println("Cannot checkout, cart is empty!");
          }

        } else if (input == 6) {
          // apply coupon
          System.out.println("Note, your current cart is as follows:");
          HashMap<Item, Integer> items = shoppingCart.getItemsWithQuantity();
          for (Item item : items.keySet()) {
            System.out.println(item.getName() + " Quantity: " + items.get(item));
          }
          System.out.println("Enter the coupon code");
          String code = reader.readLine();
          shoppingCart.applyCoupon(code);

        } else if (input == -1) {
          System.out.println("Please choose one of the options");
        }
        input = StoreHelpers.choicePrompt(customerOptions, reader);
      }

      // Store cart in account for later
      accs = DatabaseHelperAdapter.getUserActiveAccounts(customer.getId());

      if (accs != null && !accs.isEmpty() && !shoppingCart.getItems().isEmpty()) {
        System.out.println("Would you like to save your cart to your account?");
        System.out.println("Your currently open account IDs are:");
        for (int i = 0; i < accs.size(); i++) {
          System.out.println(accs.get(i));
        }
        System.out.println("Enter the ID of the account you would like to save to,");
        System.out.println("enter anything else to exit");

        String response = reader.readLine();
        try {
          int saveTo = Integer.parseInt(response);
          if (accs.contains(saveTo)) {
            Account account = new Account(customer.getId(), saveTo, true);
            if (account.saveCustomerCart(shoppingCart)) {
              System.out.println("Customer cart saved to specified account!");
            } else {
              System.out.println("There was an issue saving your cart!");
            }
          } else {
            System.out.println("Exiting!");
          }
        } catch (NumberFormatException e) {
          System.out.println("Exiting!");
        }
      }

    } catch (IOException e) {
      System.out.println("Unable to read input from console");
      e.printStackTrace();
    } catch (SQLException e) {
      System.out.println("There was an error connecting to the database");
      e.printStackTrace();
    }
  }
}
