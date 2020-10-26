package com.example.cscb07_app.Controller;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import com.b07.store.ShoppingCart;
import com.b07.users.Customer;
import com.example.cscb07_app.Activity.Customer.CustomerCheckout;
import com.example.cscb07_app.Activity.Customer.CustomerLoadShoppingCart;
import com.example.cscb07_app.Activity.Customer.CustomerSaveShoppingCart;
import com.example.cscb07_app.Activity.Employee.EmployeeMenu;
import com.example.cscb07_app.Activity.Initialization.InitializationCreateFirstAdmin;
import com.example.cscb07_app.Activity.Initialization.InitializationCreateFirstEmployee;
import com.example.cscb07_app.Activity.Login.LoginMenu;

public class DialogController implements DialogInterface.OnClickListener {

  private Context appContext;
  private DialogId id;
  private ShoppingCart cart;
  private Customer customer;

  /**
   * constructor for DialogController
   *
   * @param context of function calling it
   * @param id of the dialog
   */
  public DialogController(Context context, DialogId id) {
    this.appContext = context;
    this.id = id;
  }

  /**
   * Constructor to pass in a shoppingcart object
   *
   * @param context of the function calling it
   * @param id of the dialog
   * @param cart the existing shopping cart that needs to be passed in
   */
  public DialogController(Context context, DialogId id, ShoppingCart cart) {
    this.appContext = context;
    this.id = id;
    this.cart = cart;
  }

  /**
   * constructor to pass in a customer object
   *
   * @param context of the function calling it
   * @param id of the dialog
   * @param customer that needs to be passed in
   */
  public DialogController(Context context, DialogId id, Customer customer) {
    this.appContext = context;
    this.id = id;
    this.customer = customer;
  }

  /**
   * Implements button functionality of customer
   *
   * @param dialog a dialog to pass in
   * @param which a placeholder
   */
  @Override
  public void onClick(DialogInterface dialog, int which) {
    switch (id) {
      case CREATE_FIRST_ADMIN_DETAILS:
        ((InitializationCreateFirstAdmin) appContext).finish();
        appContext.startActivity(new Intent(appContext, InitializationCreateFirstEmployee.class));
        break;
      case CREATE_FIRST_EMPLOYEE_DETAILS:
        ((InitializationCreateFirstEmployee) appContext).finish();
        appContext.startActivity(new Intent(appContext, LoginMenu.class));
        break;
      case LOGIN_INCORRECT_CREDENTIALS:
        break;
      case AGE_EMPTY_DIALOG:
        break;
      case CREATE_COUPON_DIALOG:
        appContext.startActivity(new Intent(appContext, AdminController.class));
        break;
      case CREATE__NEW_USER_DETAILS:
        appContext.startActivity(new Intent(appContext, EmployeeMenu.class));
        break;
      case NULL_DIALOG:
        break;
      case CHECKOUT_CART:
        ((CustomerCheckout) appContext).recreate();
        break;
      case SAVE_SHOPPING_CART:
        ((CustomerCheckout) appContext).finish();
        if (which == Dialog.BUTTON_POSITIVE) {
          Intent intent = new Intent(appContext, CustomerSaveShoppingCart.class);
          intent.putExtra("cart", cart);
          appContext.startActivity(intent);
        } else {
          appContext.startActivity(new Intent(appContext, LoginMenu.class));
        }
        break;
      case SAVED_SHOPPING_CART:
        ((CustomerSaveShoppingCart) appContext).finish();
        appContext.startActivity(new Intent(appContext, LoginMenu.class));
        break;
      case LOAD_CART_FAILED:
        ((CustomerLoadShoppingCart) appContext).finish();
        Intent intent = new Intent(appContext, CustomerCheckout.class);
        intent.putExtra("customer", customer);
        appContext.startActivity(intent);
        break;
      case CHECKOUT_LOADED_CART:
        ((CustomerLoadShoppingCart) appContext).finish();
        Intent intent2 = new Intent(appContext, CustomerCheckout.class);
        intent2.putExtra("customer", customer);
        appContext.startActivity(intent2);
        break;
    }
  }
}
