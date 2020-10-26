package com.example.cscb07_app.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Item;
import com.b07.store.EmployeeInterface;
import com.b07.users.Employee;
import com.b07.users.Roles;
import com.b07.users.User;
import com.example.cscb07_app.Activity.Employee.EmployeeAuthenticateEmployee;
import com.example.cscb07_app.Activity.Employee.EmployeeMakeAccount;
import com.example.cscb07_app.Activity.Employee.EmployeeMakeEmployee;
import com.example.cscb07_app.Activity.Employee.EmployeeMakeUser;
import com.example.cscb07_app.Activity.Employee.EmployeeRestockInventory;
import com.example.cscb07_app.Activity.Login.LoginMenu;
import com.example.cscb07_app.R;
import java.sql.SQLException;

public class EmployeeController implements View.OnClickListener {

  private Context appContext;
  private static EmployeeInterface employeeInterface;

  public EmployeeController(Context context, EmployeeInterface e) {
    this.appContext = context;
    employeeInterface = e;
  }

  public EmployeeController(Context context) {
    this.appContext = context;
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.menuAuthenticateEmployeeBtn:
        appContext.startActivity(new Intent(this.appContext, EmployeeAuthenticateEmployee.class));
        break;
      case R.id.menuMakeNewUserBtn:
        appContext.startActivity(new Intent(this.appContext, EmployeeMakeUser.class));
        break;
      case R.id.menuMakeNewAccountBtn:
        appContext.startActivity(new Intent(this.appContext, EmployeeMakeAccount.class));
        break;
      case R.id.menuMakeNewEmployeeBtn:
        appContext.startActivity(new Intent(this.appContext, EmployeeMakeEmployee.class));
        break;
      case R.id.menuRestockInventoryBtn:
        appContext.startActivity(new Intent(this.appContext, EmployeeRestockInventory.class));
        break;
      case R.id.menuExitBtn:
        appContext.startActivity(new Intent(this.appContext, LoginMenu.class));
        break;
      case R.id.authenticateEmployeeBtn:
        EditText newEmployeeId =
            ((Activity) appContext).findViewById(R.id.authenticateEmployeeIdEntry);
        EditText newEmployeePassword =
            ((Activity) appContext).findViewById(R.id.authenticateEmployeePassword);

        boolean userIdValid = true;
        int newEmployeeIdNum = 0;
        String newEmployeePasswordString = newEmployeePassword.getText().toString();

        try {
          newEmployeeIdNum = Integer.parseInt(newEmployeeId.getText().toString());
        } catch (NumberFormatException e) {
          userIdValid = false;
        }

        if (userIdValid) {
          authenticateNewEmployee(newEmployeeIdNum, newEmployeePasswordString);
        } else {
          DialogFactory.createAlertDialog(
                  appContext,
                  "User ID Format Error",
                  "User ID cannot be empty!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
        }
        break;
      case R.id.makeAccountBtn:
        AlertDialog invalidIdAlertDialog = new AlertDialog.Builder(appContext).create();
        invalidIdAlertDialog.setTitle("ID Format Error");
        invalidIdAlertDialog.setMessage("ID Cannot be empty!");
        invalidIdAlertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL,
            "Ok",
            new DialogController(appContext, DialogId.NULL_DIALOG));

        boolean validId = true;
        EditText makeAccountCustomerId =
            ((Activity) appContext).findViewById(R.id.makeAccountCustomerIdEntry);
        int makeAccountCustomerIdAsNumber = -1;
        try {
          try {
            makeAccountCustomerIdAsNumber =
                Integer.parseInt(makeAccountCustomerId.getText().toString());
          } catch (NumberFormatException e) {
            validId = false;
          }

          int result = -1;
          if (validId) {
            result = employeeInterface.createAccount(makeAccountCustomerIdAsNumber);
            if (result == -1) {
              AlertDialog noInsert = new AlertDialog.Builder(appContext).create();
              noInsert.setTitle("Account not created");
              noInsert.setMessage(
                  "This likely occured because the given user ID is not a valid customer");
              invalidIdAlertDialog.setButton(
                  AlertDialog.BUTTON_NEUTRAL,
                  "Ok",
                  new DialogController(appContext, DialogId.NULL_DIALOG));
              noInsert.show();
            } else {
              AlertDialog accountAlert = new AlertDialog.Builder(appContext).create();
              accountAlert.setTitle("Account Details");
              accountAlert.setMessage("Account Id: " + result);
              accountAlert.setButton(
                  AlertDialog.BUTTON_NEUTRAL,
                  "Continue",
                  new DialogController(appContext, DialogId.CREATE__NEW_USER_DETAILS));
              accountAlert.show();
            }
          } else {
            invalidIdAlertDialog.show();
          }
        } catch (SQLException e) {
          // Will Never Occur, holdover from cross platform adapter structure.
        }

        break;
      case R.id.makeEmployeeBtn:
        AlertDialog employeeAgeAlertDialog = new AlertDialog.Builder(appContext).create();
        employeeAgeAlertDialog.setTitle("Fields Format Error");
        employeeAgeAlertDialog.setMessage("Fields cannot be empty!");
        employeeAgeAlertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL,
            "Ok",
            new DialogController(appContext, DialogId.NULL_DIALOG));

        String employeeName;
        int employeeAge = 0;
        String employeeAddress;
        String employeePassword;

        EditText employeeNameEntry;
        EditText employeeAgeEntry;
        EditText employeeAddressEntry;
        EditText employeePasswordEntry;

        boolean employeeAgeValid = true;
        boolean employeeNameValid = true;
        boolean employeeAddressValid = true;
        boolean employeePasswordValid = true;

        employeeAgeEntry = ((Activity) appContext).findViewById(R.id.makeEmployeeAgeEntry);
        employeeNameEntry = ((Activity) appContext).findViewById(R.id.makeEmployeeNameEntry);
        employeeName = employeeNameEntry.getText().toString();

        employeeAddressEntry = ((Activity) appContext).findViewById(R.id.makeEmployeeAddressEntry);
        employeeAddress = employeeAddressEntry.getText().toString();

        employeePasswordEntry = ((Activity) appContext).findViewById(R.id.makeEmployeePassword);
        employeePassword = employeePasswordEntry.getText().toString();

        if (employeeName.isEmpty()) {
          employeeNameValid = false;
        }
        if (employeeAddress.isEmpty()) {
          employeeAddressValid = false;
        }
        if (employeePassword.isEmpty()) {
          employeePasswordValid = false;
        }

        try {
          employeeAge = Integer.parseInt(employeeAgeEntry.getText().toString());
        } catch (NumberFormatException e) {
          employeeAgeValid = false;
        }

        if (employeeAgeValid
            && employeeNameValid
            && employeeAddressValid
            && employeePasswordValid) {

          int employeeId =
              insertEmployee(employeeName, employeeAge, employeeAddress, employeePassword);

          AlertDialog employeeAlertDialog = new AlertDialog.Builder(appContext).create();
          employeeAlertDialog.setTitle("Employee Details");
          employeeAlertDialog.setMessage("Employee Id: " + employeeId);
          employeeAlertDialog.setButton(
              AlertDialog.BUTTON_NEUTRAL,
              "Continue",
              new DialogController(appContext, DialogId.CREATE__NEW_USER_DETAILS));
          employeeAlertDialog.show();
        } else {
          employeeAgeAlertDialog.show();
        }

        break;
      case R.id.makeUserBtn:
        AlertDialog customerAgeAlertDialog = new AlertDialog.Builder(appContext).create();
        customerAgeAlertDialog.setTitle("Age Format Error");
        customerAgeAlertDialog.setMessage("Age cannot be empty!");
        customerAgeAlertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL,
            "Ok",
            new DialogController(appContext, DialogId.AGE_EMPTY_DIALOG));

        String customerName;
        int customerAge = 0;
        String customerAddress;
        String customerPassword;

        EditText customerNameEntry;
        EditText customerAgeEntry;
        EditText customerAddressEntry;
        EditText customerPasswordEntry;

        boolean userAgeValid = true;
        customerAgeEntry = ((Activity) appContext).findViewById(R.id.makeUserAgeEntry);

        try {
          customerAge = Integer.parseInt(customerAgeEntry.getText().toString());
        } catch (NumberFormatException e) {
          userAgeValid = false;
        }

        if (userAgeValid) {
          customerNameEntry = ((Activity) appContext).findViewById(R.id.makeUserNameEntry);
          customerName = customerNameEntry.getText().toString();

          customerAddressEntry = ((Activity) appContext).findViewById(R.id.makeUserAddressEntry);
          customerAddress = customerAddressEntry.getText().toString();

          customerPasswordEntry = ((Activity) appContext).findViewById(R.id.makeUserPassword);
          customerPassword = customerPasswordEntry.getText().toString();

          int userId = insertCustomer(customerName, customerAge, customerAddress, customerPassword);

          AlertDialog customerAlertDialog = new AlertDialog.Builder(appContext).create();
          customerAlertDialog.setTitle("Customer Details");
          customerAlertDialog.setMessage("Customer Id: " + userId);
          customerAlertDialog.setButton(
              AlertDialog.BUTTON_NEUTRAL,
              "Continue",
              new DialogController(appContext, DialogId.CREATE__NEW_USER_DETAILS));
          customerAlertDialog.show();
        } else {
          customerAgeAlertDialog.show();
        }
        break;
      case R.id.restockInventoryBtn:
        EditText itemId = ((Activity) appContext).findViewById(R.id.restockInventoryItemIdEntry);
        EditText quantity =
            ((Activity) appContext).findViewById(R.id.restockInventoryQuantityEntry);

        int itemIdNumber = -1;
        int quantityNumber = 0;
        boolean restocked = false;
        boolean bothNumbersValid = true;
        // Check validity of textfield data.
        try {
          itemIdNumber = Integer.parseInt(itemId.getText().toString());
          quantityNumber = Integer.parseInt(quantity.getText().toString());
          if (quantityNumber < 0) {
            bothNumbersValid = false;
          }
        } catch (NumberFormatException e) {
          bothNumbersValid = false;
        }
        try {
          // Attempt restock, if valid.
          if (bothNumbersValid) {
            if (DatabaseHelperAdapter.itemExists(itemIdNumber)) {
              Item item = DatabaseHelperAdapter.getItem(itemIdNumber);
              restocked = employeeInterface.restockInventory(item, quantityNumber);
            } else {
              AlertDialog noItemDialog = new AlertDialog.Builder(appContext).create();
              noItemDialog.setTitle("No such item ID!");
              noItemDialog.setMessage("The entered Item ID is invalid!");
              noItemDialog.setButton(
                  AlertDialog.BUTTON_NEUTRAL,
                  "Ok",
                  new DialogController(appContext, DialogId.AGE_EMPTY_DIALOG));
              noItemDialog.show();
            }
          } else {
            AlertDialog restockAlertDialog = new AlertDialog.Builder(appContext).create();
            restockAlertDialog.setTitle("Format Error");
            restockAlertDialog.setMessage(
                "Make sure to enter a value for both the ID and quantity, "
                    + "and ensure that the quantity is positive!");
            restockAlertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL,
                "Ok",
                new DialogController(appContext, DialogId.AGE_EMPTY_DIALOG));
            restockAlertDialog.show();
          }

          if (restocked) {
            AlertDialog restockSuccessfulDialog = new AlertDialog.Builder(appContext).create();
            restockSuccessfulDialog.setTitle("Restock successful!");
            restockSuccessfulDialog.setMessage(
                "Item: "
                    + DatabaseHelperAdapter.getItem(itemIdNumber).getName()
                    + "\nQuantity:"
                    + quantityNumber);
            restockSuccessfulDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL,
                "Continue",
                new DialogController(appContext, DialogId.CREATE__NEW_USER_DETAILS));
            restockSuccessfulDialog.show();
          }
        } catch (SQLException e) {
          // Will Never Occur, holdover from cross platform adapter structure.
        }
        break;
    }
  }

  /**
   * Insert employee into database.
   *
   * @param name the name of the employee
   * @param age the age of the employee
   * @param address the address of the employee
   * @param password the password of the employee
   * @return employee's id
   */
  public int insertEmployee(String name, int age, String address, String password) {
    int employeeId = -1;
    int employeeRoleId = 0;
    try {
      employeeId = employeeInterface.createEmployee(name, age, address, password);
      employeeRoleId = DatabaseHelperAdapter.getRoleIdByName(Roles.EMPLOYEE.name());
      DatabaseHelperAdapter.insertUserRole(employeeId, employeeRoleId);
    } catch (DatabaseInsertException e) {
      // TODO: catch it later
    } catch (SQLException e) {
      // Will Never Occur, holdover from cross platform adapter structure.
    }
    return employeeId;
  }

  /**
   * Insert customer into database.
   *
   * @param name the name of the customer
   * @param age the age of the customer
   * @param address the address of the customer
   * @param password the password of the customer
   * @return employee's id
   */
  public int insertCustomer(String name, int age, String address, String password) {

    int customerId = -1;
    int customerRoleId = 0;
    try {
      customerId = employeeInterface.createCustomer(name, age, address, password);
      customerRoleId = DatabaseHelperAdapter.getRoleIdByName(Roles.CUSTOMER.name());
      DatabaseHelperAdapter.insertUserRole(customerId, customerRoleId);
    } catch (DatabaseInsertException e) {
      // TODO: catch it later
    } catch (SQLException e) {
      // Will Never Occur, holdover from cross platform adapter structure.
    }
    return customerId;
  }

  /**
   * Authenticates employee and logs them in.
   *
   * @param userId the employee's user id
   * @param password the employee's password
   */
  public void authenticateNewEmployee(int userId, String password) {

    Employee currentEmployee = null;
    boolean authenticated = false;

    try {
      User currentUser = DatabaseHelperAdapter.getUserDetails(userId);

      if (userId < 1 || currentUser == null || !(currentUser instanceof Employee)) {
        authenticated = false;
      } else {
        currentEmployee = (Employee) DatabaseHelperAdapter.getUserDetails(userId);
        authenticated = currentEmployee.authenticate(password);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (authenticated) {
      employeeInterface.setCurrentEmployee(currentEmployee);
      DialogFactory.createAlertDialog(
              appContext,
              "Success!",
              "Current Employee Set to " + currentEmployee.getName(),
              "Ok",
              DialogId.CREATE__NEW_USER_DETAILS)
          .show();
    } else {
      DialogFactory.createAlertDialog(
              appContext,
              "Incorrect Credentials",
              "Double check your id and password and make sure they belong to an employee account!",
              "Ok",
              DialogId.NULL_DIALOG)
          .show();
    }
  }
}
