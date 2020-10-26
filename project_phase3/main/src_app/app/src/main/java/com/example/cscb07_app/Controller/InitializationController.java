package com.example.cscb07_app.Controller;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.users.Roles;
import com.example.cscb07_app.R;
import java.sql.SQLException;

/** Class that controls all actions for events on initialization activities. */
public class InitializationController implements View.OnClickListener {

  private Context appContext;
  private String name;
  private int age = 0;
  private String address;
  private String password;
  private EditText nameEntry;
  private EditText ageEntry;
  private EditText addressEntry;
  private EditText passwordEntry;

  /**
   * Constructor for InitializationController.
   *
   * @param context the context it is in
   */
  public InitializationController(Context context) {
    this.appContext = context;
  }

  /**
   * Performs actions based on events.
   *
   * @param view the view of interest
   */
  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      case R.id.initializationCreateAdminButton:
        createFirstAdmin();
        break;
      case R.id.initializationCreateEmployeeButton:
        createFirstEmployee();
        break;
    }
  }

  /** Creates first admin. */
  public void createFirstAdmin() {
    boolean adminAgeValid = true;
    boolean adminNameValid = true;
    boolean adminAddressValid = true;
    boolean adminPasswordValid = true;

    ageEntry = ((Activity) appContext).findViewById(R.id.initializationAdminAgeEntry);
    nameEntry = ((Activity) appContext).findViewById(R.id.initializationAdminNameEntry);
    name = nameEntry.getText().toString();
    addressEntry = ((Activity) appContext).findViewById(R.id.initializationAdminAddressEntry);
    address = addressEntry.getText().toString();
    passwordEntry = ((Activity) appContext).findViewById(R.id.initializationAdminPassword);
    password = passwordEntry.getText().toString();

    if (name.isEmpty()) {
      adminNameValid = false;
    }
    if (address.isEmpty()) {
      adminAddressValid = false;
    }
    if (password.isEmpty()) {
      adminPasswordValid = false;
    }

    try {
      age = Integer.parseInt(ageEntry.getText().toString());
    } catch (NumberFormatException e) {
      adminAgeValid = false;
    }

    if (adminAgeValid && adminNameValid && adminAddressValid && adminPasswordValid) {
      int adminId = insertAdmin(name, age, address, password);

      DialogFactory.createAlertDialog(
              appContext,
              "Admin Details",
              "Admin Id: " + adminId,
              "Continue",
              DialogId.CREATE_FIRST_ADMIN_DETAILS)
          .show();
    } else {
      DialogFactory.createAlertDialog(
              appContext,
              "Fields Format Error",
              "Fields cannot be empty",
              "Continue",
              DialogId.NULL_DIALOG)
          .show();
    }
  }

  /** Creates first employee. */
  public void createFirstEmployee() {
    boolean employeeAgeValid = true;
    boolean employeeNameValid = true;
    boolean employeeAddressValid = true;
    boolean employeePasswordValid = true;

    ageEntry = ((Activity) appContext).findViewById(R.id.initializationEmployeeAgeEntry);
    nameEntry = ((Activity) appContext).findViewById(R.id.initializationEmployeeNameEntry);
    name = nameEntry.getText().toString();

    addressEntry = ((Activity) appContext).findViewById(R.id.initializationEmployeeAddressEntry);
    address = addressEntry.getText().toString();

    passwordEntry = ((Activity) appContext).findViewById(R.id.initializationEmployeePassword);
    password = passwordEntry.getText().toString();

    if (name.isEmpty()) {
      employeeNameValid = false;
    }
    if (address.isEmpty()) {
      employeeAddressValid = false;
    }
    if (password.isEmpty()) {
      employeePasswordValid = false;
    }

    try {
      age = Integer.parseInt(ageEntry.getText().toString());
    } catch (NumberFormatException e) {
      employeeAgeValid = false;
    }

    if (employeeAgeValid && employeeAddressValid && employeeNameValid && employeePasswordValid) {
      int employeeId = insertEmployee(name, age, address, password);

      DialogFactory.createAlertDialog(
              appContext,
              "Employee Details",
              "Employee Id: " + employeeId,
              "Continue",
              DialogId.CREATE_FIRST_EMPLOYEE_DETAILS)
          .show();

    } else {
      DialogFactory.createAlertDialog(
              appContext,
              "Fields Format Error",
              "Fields cannot be empty",
              "Continue",
              DialogId.NULL_DIALOG)
          .show();
    }
  }

  /**
   * Insert admin into database.
   *
   * @param name the name of the admin
   * @param age the age of the admin
   * @param address the address of the admin
   * @param password the password of the admin
   * @return admins's id
   */
  public int insertAdmin(String name, int age, String address, String password) {

    int adminRoleId = 0;
    int adminId = 1;

    try {
      adminId = DatabaseHelperAdapter.insertNewUser(name, age, address, password);
      adminRoleId = DatabaseHelperAdapter.getRoleIdByName(Roles.ADMIN.name());
      DatabaseHelperAdapter.insertUserRole(adminId, adminRoleId);
    } catch (DatabaseInsertException e) {
      // TODO: catch it later
    } catch (SQLException e) {
    }

    return adminId;
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

    int employeeRoleId = 0;
    int employeeId = -1;

    try {
      employeeId = DatabaseHelperAdapter.insertNewUser(name, age, address, password);
      employeeRoleId = DatabaseHelperAdapter.getRoleIdByName(Roles.EMPLOYEE.name());
      DatabaseHelperAdapter.insertUserRole(employeeId, employeeRoleId);
    } catch (DatabaseInsertException e) {
      // TODO: catch it later
    } catch (SQLException e) {
    }
    return employeeId;
  }
}
