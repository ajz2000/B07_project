package com.example.cscb07_app.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.DifferentEnumException;
import com.b07.serialize.SerializationPasswordHelper;
import com.b07.serialize.SerializeDatabase;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.Employee;
import com.b07.users.Roles;
import com.b07.users.User;
import com.example.cscb07_app.Activity.Admin.AdminCreateCoupon;
import com.example.cscb07_app.Activity.Admin.AdminLoadAppData;
import com.example.cscb07_app.Activity.Admin.AdminPromoteEmployee;
import com.example.cscb07_app.Activity.Admin.AdminSaveAppData;
import com.example.cscb07_app.Activity.Admin.AdminViewActiveAccounts;
import com.example.cscb07_app.Activity.Admin.AdminViewBooks;
import com.example.cscb07_app.Activity.Admin.AdminViewHistoricAccounts;
import com.example.cscb07_app.Activity.Login.LoginMenu;
import com.example.cscb07_app.R;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/** controls the buttons in admin package */
public class AdminController implements View.OnClickListener {

  private static Admin admin;
  private Context appContext;

  /**
   * Constructor for AdminController
   *
   * @param context of the function calling it
   */
  public AdminController(Context context) {
    this.appContext = context;
  }

  /**
   * Constructor for AdminController to pass in an currently logged in admin
   *
   * @param context of the function calling it
   * @param a the currently logged in admin object
   */
  public AdminController(Context context, Admin a) {
    this.appContext = context;
    admin = a;
  }

  /**
   * Implements functionality of buttons in admin
   *
   * @param view the button
   */
  @Override
  public void onClick(View view) {
    Intent intent;

    switch (view.getId()) {
      case R.id.adminMenuExitBtn:
        appContext.startActivity(new Intent(this.appContext, LoginMenu.class));
        break;
      case R.id.adminMenuViewBooks:
        intent = new Intent(this.appContext, AdminViewBooks.class);
        intent.putExtra("adminObject", admin);
        appContext.startActivity(intent);
        break;
      case R.id.adminMenuPromoteEmployee:
        intent = new Intent(this.appContext, AdminPromoteEmployee.class);
        intent.putExtra("adminObject", admin);
        appContext.startActivity(intent);
        break;
      case R.id.adminMenuHistoricAccounts:
        intent = new Intent(this.appContext, AdminViewHistoricAccounts.class);
        intent.putExtra("adminObject", admin);
        appContext.startActivity(intent);
        break;
      case R.id.adminMenuActiveAccounts:
        appContext.startActivity(new Intent(this.appContext, AdminViewActiveAccounts.class));
        break;
      case R.id.adminMenuSaveData:
        appContext.startActivity(new Intent(this.appContext, AdminSaveAppData.class));
        break;
      case R.id.adminMenuLoadData:
        appContext.startActivity(new Intent(this.appContext, AdminLoadAppData.class));
        break;
      case R.id.adminMenuCreateCoupon:
        appContext.startActivity(new Intent(this.appContext, AdminCreateCoupon.class));
        break;
      case R.id.addCouponBtn:
        Activity context = (Activity) appContext;
        EditText couponCodeEntry = context.findViewById(R.id.couponCodeEntry);
        String couponCode = couponCodeEntry.getText().toString();

        Spinner couponTypeEntry = context.findViewById(R.id.couponTypeEntry);
        String couponType = couponTypeEntry.getSelectedItem().toString();

        EditText couponDiscountEntry = context.findViewById(R.id.couponDiscountEntry);
        String couponDiscount = couponDiscountEntry.getText().toString();

        EditText couponItemIdEntry = context.findViewById(R.id.couponItemIdEntry);
        EditText couponQuantityEntry = context.findViewById(R.id.couponQuantityEntry);

        Resources res = context.getResources();
        String[] types = res.getStringArray(R.array.couponTypeArray);

        if (couponType.equals(types[0])) {
          couponType = "PERCENTAGE";
        } else if (couponType.equals(types[1])) {
          couponType = "FLAT_RATE";
        }

        int couponItemId = -1;
        int quantity = -1;
        BigDecimal couponDiscountDecimal = BigDecimal.ZERO;

        boolean isNumber = true;
        boolean validCouponType = true;
        int couponTypeId = -1;
        try {
          couponDiscountDecimal = new BigDecimal(couponDiscount);
          couponItemId = Integer.parseInt(couponItemIdEntry.getText().toString());
          quantity = Integer.parseInt(couponQuantityEntry.getText().toString());
          couponTypeId = DatabaseHelperAdapter.getDiscountTypeIdByName(couponType);
        } catch (NumberFormatException e) {
          isNumber = false;
        } catch (SQLException e) {
          validCouponType = false;
        }

        boolean isValidData = true;

        if (isNumber && validCouponType) {
          if (couponCode.isEmpty()) {
            isValidData = false;
          } else if (couponType.equals("PERCENTAGE")) {
            if (couponDiscountDecimal.compareTo(BigDecimal.ZERO) == -1
                || couponDiscountDecimal.compareTo(new BigDecimal("100")) == 1) {
              isValidData = false;
            }
          } else {
            BigDecimal itemPrice = BigDecimal.ZERO;
            try {
              itemPrice = DatabaseHelperAdapter.getItem(couponItemId).getPrice();
            } catch (SQLException e) {
            }

            if (itemPrice.compareTo(couponDiscountDecimal) == -1) {
              isValidData = false;
            }
          }
        }
        if (!isNumber) {
          DialogFactory.createAlertDialog(
                  appContext,
                  "Incorrect Input",
                  "Please input numbers" + " for all numeric entries!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
        } else if (!validCouponType) {
          DialogFactory.createAlertDialog(
                  appContext,
                  "Invalid Discount Type",
                  "This discount type is not valid",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
        } else if (!isValidData) {
          DialogFactory.createAlertDialog(
                  appContext,
                  "Invalid Coupon",
                  "Please make sure discounts"
                      + " don't exceed 100% or the item's price! Coupon code also can't be empty! ",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
        } else {
          int id = -1;
          try {
            id =
                DatabaseHelperAdapter.insertCoupon(
                    couponItemId, quantity, couponType, couponDiscountDecimal, couponCode);
          } catch (SQLException | DatabaseInsertException e) {
            // do nothing: failure is handled by the coupon id
          }
          if (id == -1) {
            DialogFactory.createAlertDialog(
                    appContext,
                    "Failed to Add Coupon",
                    "An error occurred" + " when adding coupon!",
                    "Ok",
                    DialogId.NULL_DIALOG)
                .show();
          } else {
            DialogFactory.createAlertDialog(
                    appContext,
                    "Successfully Added Coupon",
                    "Coupon was added to the database!",
                    "Ok",
                    DialogId.NULL_DIALOG)
                .show();
          }
        }
        break;
      case R.id.saveDataBtn:
        EditText saveLoc = ((Activity) appContext).findViewById(R.id.saveAppDataEntry);
        String saveLocString = saveLoc.getText().toString();
        try {
          SerializeDatabase.serializeToFile(saveLocString);
        } catch (IOException e) {
          DialogFactory.createAlertDialog(
                  appContext,
                  "Error!",
                  "Could not save data to this location!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
          Log.e("myApp", "exception", e);
          break;
        } catch (SQLException e) {
          DialogFactory.createAlertDialog(
                  appContext,
                  "Error!",
                  "There was an issue with the SQL database!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
          Log.e("myApp", "exception", e);
          break;
        }
        DialogFactory.createAlertDialog(
                appContext,
                "Success!",
                "Serialized to " + saveLocString + "/database_copy.ser",
                "Ok",
                DialogId.NULL_DIALOG)
            .show();

        break;
      case R.id.loadDataBtn:
        String hashedPassword = "temp";
        boolean hashedPassSaved = false;
        try {
          hashedPassword = DatabaseHelperAdapter.getPassword(admin.getId());
          if (hashedPassword != null) {
            hashedPassSaved = true;
          }
        } catch (SQLException e) {
          hashedPassSaved = false;
        }

        EditText restoreLoc = ((Activity) appContext).findViewById(R.id.loadAppDataEntry);
        String restoreLocString = restoreLoc.getText().toString();
        try {

          SerializeDatabase.populateFromFile(restoreLocString, appContext);

        } catch (IOException e) {
          Log.e("myApp", "exception", e);
          DialogFactory.createAlertDialog(
                  appContext,
                  "Error!",
                  "Could not restore from this location!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
          break;
        } catch (SQLException e) {
          Log.e("myApp", "exception", e);

          DialogFactory.createAlertDialog(
                  appContext,
                  "Error!",
                  "There was an issue with the SQL database!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
          break;
        } catch (DifferentEnumException e) {
          Log.e("myApp", "exception", e);

          DialogFactory.createAlertDialog(
                  appContext,
                  "Error!",
                  "The data on this device does not match the data in the stored db!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
          break;
        } catch (ClassNotFoundException e) {
          Log.e("myApp", "exception", e);
          DialogFactory.createAlertDialog(
                  appContext,
                  "Error!",
                  "One or more classes not found!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
          break;
        }

        int newAdminId;
        if (hashedPassSaved) {
          try {
            newAdminId =
                SerializationPasswordHelper.insertUserNoHash(
                    admin.getName(),
                    admin.getAge(),
                    admin.getAddress(),
                    hashedPassword,
                    appContext);
            int adminRoleId = DatabaseHelperAdapter.getRoleIdByName(Roles.ADMIN.name());
            DatabaseHelperAdapter.insertUserRole(newAdminId, adminRoleId);
            admin.setId(newAdminId);
            DialogFactory.createAlertDialog(
                    appContext,
                    "Admin Reinserted!",
                    "The current admin has been added to the database with new ID: " + newAdminId,
                    "Ok",
                    DialogId.NULL_DIALOG)
                .show();
          } catch (SQLException | DatabaseInsertException e) {
            DialogFactory.createAlertDialog(
                    appContext,
                    "An issue occurred!",
                    "The current admin could not be reinserted into the database!\n"
                        + "Upon logging out, the current admin will no longer be usable!",
                    "Ok",
                    DialogId.NULL_DIALOG)
                .show();
          }
        } else {
          DialogFactory.createAlertDialog(
                  appContext,
                  "An issue occurred!",
                  "The current admin could not be reinserted into the database!\n"
                      + "Upon logging out, the current admin will no longer be usable!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
        }
        DialogFactory.createAlertDialog(
                appContext, "Success!", "Database restored!", "Ok", DialogId.NULL_DIALOG)
            .show();

        break;
      case R.id.promoteEmployeeButton:
        EditText employeeIdEntry =
            ((Activity) appContext).findViewById(R.id.promoteEmployeeIdEntry);

        int employeeId = 0;
        boolean isValidNum = true;

        try {
          employeeId = Integer.parseInt(employeeIdEntry.getText().toString());
        } catch (NumberFormatException e) {
          isValidNum = false;
        }
        if (isValidNum) {

          promoteEmployee(employeeId);
        } else {
          DialogFactory.createAlertDialog(
                  appContext,
                  "Employee ID Format Error",
                  "Please enter an integer!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
        }
        break;
      case R.id.viewHistoricAccountsBtn:
        EditText customerIdEntry =
            ((Activity) appContext).findViewById(R.id.historicAccountsCustomerIdEntry);

        int customerId = 0;
        boolean isValidNumber = true;
        try {
          customerId = Integer.parseInt(customerIdEntry.getText().toString());
        } catch (NumberFormatException e) {
          isValidNumber = false;
        }
        if (isValidNumber) {
          viewHistoricAccounts(customerId);
        } else {
          DialogFactory.createAlertDialog(
                  appContext,
                  "Customer ID Format Error",
                  "Please enter an integer!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
        }
        break;
      case R.id.viewActiveAccountsBtn:
        EditText customerIdEntryActive =
            ((Activity) appContext).findViewById(R.id.activeAccountsCustomerIdEntry);

        int customerIdActive = 0;
        boolean isValidNumberActive = true;
        try {
          customerIdActive = Integer.parseInt(customerIdEntryActive.getText().toString());
        } catch (NumberFormatException e) {
          isValidNumberActive = false;
        }
        if (isValidNumberActive) {
          viewActiveAccounts(customerIdActive);
        } else {
          DialogFactory.createAlertDialog(
                  appContext,
                  "Customer ID Format Error",
                  "Please enter an integer!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
        }
        break;
    }
  }

  /**
   * View the historic accounts of a customer.
   *
   * @param customerId the customer of interst
   */
  public void viewActiveAccounts(int customerId) {

    TextView historicAccountsData =
        ((Activity) appContext).findViewById(R.id.viewActiveAccountsText);

    try {
      User user = DatabaseHelperAdapter.getUserDetails(customerId);

      if (user instanceof Customer) {

        List<Integer> accounts = DatabaseHelperAdapter.getUserActiveAccounts(customerId);
        StringBuilder data = new StringBuilder();

        data.append("Active Accounts\n");
        data.append("--------------------------\n");

        for (Integer accountId : accounts) {
          data.append("Account ID: " + accountId + "\n");
        }
        historicAccountsData.setText(data.toString());
      } else {
        DialogFactory.createAlertDialog(
                appContext,
                "Failure Displaying Data",
                "Account history " + "only exists for valid customers!",
                "Ok",
                DialogId.NULL_DIALOG)
            .show();
      }
    } catch (SQLException e) {
      DialogFactory.createAlertDialog(
              appContext,
              "Database Failure",
              "Something went wrong" + " with database functionality!",
              "Ok",
              DialogId.NULL_DIALOG)
          .show();
    }
  }

  /**
   * View the historic accounts of a customer.
   *
   * @param customerId the customer of interst
   */
  public void viewHistoricAccounts(int customerId) {

    TextView historicAccountsData =
        ((Activity) appContext).findViewById(R.id.viewHistoricAccountsText);

    try {
      User user = DatabaseHelperAdapter.getUserDetails(customerId);

      if (user instanceof Customer) {

        List<Integer> accounts = DatabaseHelperAdapter.getUserInactiveAccounts(customerId);
        StringBuilder data = new StringBuilder();

        data.append("Historic Accounts\n");
        data.append("--------------------------\n");

        for (Integer accountId : accounts) {
          data.append("Account ID: " + accountId + "\n");
        }
        historicAccountsData.setText(data.toString());
      } else {
        DialogFactory.createAlertDialog(
                appContext,
                "Failure Displaying Data",
                "Account history's " + "only exists for valid customers!",
                "Ok",
                DialogId.NULL_DIALOG)
            .show();
      }
    } catch (SQLException e) {
      DialogFactory.createAlertDialog(
              appContext,
              "Database Failure",
              "Something went wrong" + " with database functionality!",
              "Ok",
              DialogId.NULL_DIALOG)
          .show();
    }
  }

  /**
   * Promotes a customer to an admin.
   *
   * @param employeeId the user of interest.
   */
  public void promoteEmployee(int employeeId) {

    User toPromote = null;

    try {
      toPromote = DatabaseHelperAdapter.getUserDetails(employeeId);

      if (toPromote instanceof Employee) {
        admin.promoteEmployee((Employee) toPromote);
        DialogFactory.createAlertDialog(
                appContext,
                "Employee Promoted Successfully",
                "Employee has now been promoted to an Admin!",
                "Ok",
                DialogId.NULL_DIALOG)
            .show();
      } else {
        DialogFactory.createAlertDialog(
                appContext,
                "Employee Promotion Failed",
                "Only employees can be promoted to an admin!",
                "Ok",
                DialogId.NULL_DIALOG)
            .show();
      }
    } catch (SQLException e) {
      DialogFactory.createAlertDialog(
              appContext,
              "Database Failure",
              "Something went wrong" + " with database functionality!",
              "Ok",
              DialogId.NULL_DIALOG)
          .show();
    }
  }
}
