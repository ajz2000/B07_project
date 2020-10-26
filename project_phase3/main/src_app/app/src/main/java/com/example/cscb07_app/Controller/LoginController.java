package com.example.cscb07_app.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.Employee;
import com.b07.users.User;
import com.example.cscb07_app.Activity.Admin.AdminMenu;
import com.example.cscb07_app.Activity.Customer.CustomerCheckout;
import com.example.cscb07_app.Activity.Customer.CustomerLoadShoppingCart;
import com.example.cscb07_app.Activity.Employee.EmployeeMenu;
import com.example.cscb07_app.Activity.Login.LoginMenu;
import com.example.cscb07_app.R;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Class that controls all actions for events on login activity */
public class LoginController implements View.OnClickListener {

  private Context appContext;

  /**
   * Constructor for LoginController.
   *
   * @param context the context it is called in
   */
  public LoginController(Context context) {
    this.appContext = context;
  }

  /**
   * Performs actions based on events in login activity.
   *
   * @param v the view
   */
  @Override
  public void onClick(View v) {
    Spinner mySpinner = ((LoginMenu) appContext).findViewById(R.id.rolePositionEntry);
    String rolePosition = mySpinner.getSelectedItem().toString();

    EditText userIdEntry = ((Activity) appContext).findViewById(R.id.loginUserIdEntry);
    EditText passwordEntry = ((Activity) appContext).findViewById(R.id.loginPassword);

    boolean userIdValid = true;
    int userId = 0;

    try {
      userId = Integer.parseInt(userIdEntry.getText().toString());
    } catch (NumberFormatException e) {
      userIdValid = false;
    }

    if (userIdValid) {
      String password = passwordEntry.getText().toString();
      if (rolePosition.equals("Admin")) {
        adminLogin(userId, password);
      } else if (rolePosition.equals("Employee")) {
        employeeLogin(userId, password);
      } else if (rolePosition.equals("Customer")) {
        customerLogin(userId, password);
      }
    } else {
      DialogFactory.createAlertDialog(
              appContext,
              "User ID Format Error",
              "User ID cannot be empty!",
              "Ok",
              DialogId.LOGIN_INCORRECT_CREDENTIALS)
          .show();
    }
  }

  /**
   * Authenticates admin and logs them in.
   *
   * @param userId the admin's user id
   * @param password the admin's password
   */
  public void adminLogin(int userId, String password) {

    boolean authenticated = true;
    Admin admin = null;

    try {
      User currentUser = DatabaseHelperAdapter.getUserDetails(userId);

      if (userId < 1 || currentUser == null || !(currentUser instanceof Admin)) {
        authenticated = false;
      } else {
        admin = (Admin) currentUser;
        authenticated = admin.authenticate(password);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (authenticated) {
      Intent intent = new Intent(this.appContext, AdminMenu.class);
      intent.putExtra("adminObject", admin);
      ((LoginMenu) appContext).finish();
      appContext.startActivity(intent);
    } else {
      DialogFactory.createAlertDialog(
              appContext,
              "Incorrect Credentials",
              "Double check your id and password and make sure its an admin account!",
              "Ok",
              DialogId.LOGIN_INCORRECT_CREDENTIALS)
          .show();
    }
  }

  /**
   * Authenticates customer and logs them in.
   *
   * @param userId the customer's user id
   * @param password the customer's password
   */
  public void customerLogin(int userId, String password) {

    boolean authenticated = true;
    Customer customer = null;

    try {
      User currentUser = DatabaseHelperAdapter.getUserDetails(userId);

      if (userId < 1 || currentUser == null || !(currentUser instanceof Customer)) {
        authenticated = false;
      } else {
        customer = (Customer) DatabaseHelperAdapter.getUserDetails(userId);
        authenticated = customer.authenticate(password);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (authenticated) {

      List<Integer> accts = new ArrayList<>();

      try {
        accts = DatabaseHelperAdapter.getUserActiveAccounts(customer.getId());
      } catch (SQLException e) {
      }

      if (accts == null || accts.isEmpty()) {
        Intent customerIntent = new Intent(this.appContext, CustomerCheckout.class);
        customerIntent.putExtra("customer", customer);
        ((LoginMenu) appContext).finish();
        appContext.startActivity(customerIntent);
      } else if (accts != null && !accts.isEmpty()) {
        Intent loadCartIntent = new Intent(this.appContext, CustomerLoadShoppingCart.class);
        loadCartIntent.putExtra("customer", customer);
        ((LoginMenu) appContext).finish();
        appContext.startActivity(loadCartIntent);
      }
    } else {
      DialogFactory.createAlertDialog(
              appContext,
              "Incorrect Credentials",
              "Double check your id and password and make sure its a customer account!",
              "Ok",
              DialogId.LOGIN_INCORRECT_CREDENTIALS)
          .show();
    }
  }

  /**
   * Authenticates employee and logs them in.
   *
   * @param userId the employee's user id
   * @param password the employee's password
   */
  public void employeeLogin(int userId, String password) {

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

    Intent employeeIntent = new Intent(this.appContext, EmployeeMenu.class);

    if (authenticated) {
      employeeIntent.putExtra("employee", currentEmployee);
      ((LoginMenu) appContext).finish();
      appContext.startActivity(employeeIntent);
    } else {
      DialogFactory.createAlertDialog(
              appContext,
              "Incorrect Credentials",
              "Double check your id and password and make sure its an employee account!",
              "Ok",
              DialogId.LOGIN_INCORRECT_CREDENTIALS)
          .show();
    }
  }
}
