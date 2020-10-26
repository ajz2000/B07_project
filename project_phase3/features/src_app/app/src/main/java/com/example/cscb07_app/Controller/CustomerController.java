package com.example.cscb07_app.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.inventory.Item;
import com.b07.store.ShoppingCart;
import com.b07.users.Account;
import com.b07.users.Customer;
import com.example.cscb07_app.Activity.Customer.CustomerCheckout;
import com.example.cscb07_app.Activity.Customer.CustomerLoadShoppingCart;
import com.example.cscb07_app.Activity.Login.LoginMenu;
import com.example.cscb07_app.R;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Controller for customer activities. */
public class CustomerController implements View.OnClickListener {

  private Context appContext;
  private Customer customer;
  private ShoppingCart cart;

  /**
   * Constructor for CustomerController
   *
   * @param context the context of interest
   * @param customer the customer
   */
  public CustomerController(Context context, Customer customer) {
    this.appContext = context;
    this.customer = customer;
  }

  /**
   * Constructor for CustomerController.
   *
   * @param context the context of interest
   * @param cart the cart of interest
   */
  public CustomerController(Context context, ShoppingCart cart) {
    this.appContext = context;
    this.customer = cart.getCustomer();
    this.cart = cart;
  }

  /**
   * Controller functionality.
   *
   * @param view the view that was clicked
   */
  @Override
  public void onClick(View view) {

    int temp = 0;

    TextView amount1 = null;
    TextView amount2 = null;
    TextView amount3 = null;
    TextView amount4 = null;
    TextView amount5 = null;

    TextView item1 = null;
    TextView item2 = null;
    TextView item3 = null;
    TextView item4 = null;
    TextView item5 = null;

    String item1Name = null;
    String item2Name = null;
    String item3Name = null;
    String item4Name = null;
    String item5Name = null;

    if (view.getId() != R.id.saveShoppingCartBtn
        && view.getId() != R.id.loadShoppingCartBtn
        && view.getId() != R.id.skipCartLoading) {
      amount1 = ((Activity) appContext).findViewById(R.id.amount1);
      amount2 = ((Activity) appContext).findViewById(R.id.amount2);
      amount3 = ((Activity) appContext).findViewById(R.id.amount3);
      amount4 = ((Activity) appContext).findViewById(R.id.amount4);
      amount5 = ((Activity) appContext).findViewById(R.id.amount5);

      item1 = ((Activity) appContext).findViewById(R.id.cartItem1);
      item2 = ((Activity) appContext).findViewById(R.id.cartItem2);
      item3 = ((Activity) appContext).findViewById(R.id.cartItem3);
      item4 = ((Activity) appContext).findViewById(R.id.cartItem4);
      item5 = ((Activity) appContext).findViewById(R.id.cartItem5);

      item1Name = item1.getText().toString();
      item2Name = item2.getText().toString();
      item3Name = item3.getText().toString();
      item4Name = item4.getText().toString();
      item5Name = item5.getText().toString();
    }

    Toast toastAdd =
        Toast.makeText(appContext, "There are no more items in stock", Toast.LENGTH_SHORT);

    Toast toastRemove =
        Toast.makeText(appContext, "Cannot remove any more items", Toast.LENGTH_SHORT);

    switch (view.getId()) {
      case R.id.plus1:
        temp = Integer.parseInt(amount1.getText().toString()) + 1;
        if (temp <= getMaxQuantity(item1Name)) {
          amount1.setText(String.valueOf(temp));
          addItemToCart(item1Name);
        } else {
          toastAdd.show();
        }
        break;
      case R.id.plus2:
        temp = Integer.parseInt(amount2.getText().toString()) + 1;
        if (temp <= getMaxQuantity(item2Name)) {
          amount2.setText(String.valueOf(temp));
          addItemToCart(item2Name);
        } else {
          toastAdd.show();
        }
        break;
      case R.id.plus3:
        temp = Integer.parseInt(amount3.getText().toString()) + 1;
        if (temp <= getMaxQuantity(item3Name)) {
          amount3.setText(String.valueOf(temp));
          addItemToCart(item3Name);
        } else {
          toastAdd.show();
        }
        break;
      case R.id.plus4:
        temp = Integer.parseInt(amount4.getText().toString()) + 1;
        if (temp <= getMaxQuantity(item4Name)) {
          amount4.setText(String.valueOf(temp));
          addItemToCart(item4Name);
        } else {
          toastAdd.show();
        }
        break;
      case R.id.plus5:
        temp = Integer.parseInt(amount5.getText().toString()) + 1;
        if (temp <= getMaxQuantity(item5Name)) {
          amount5.setText(String.valueOf(temp));
          addItemToCart(item5Name);
        } else {
          toastAdd.show();
        }
        break;
      case R.id.minus1:
        temp = Integer.parseInt(amount1.getText().toString()) - 1;
        if (temp >= 0) {
          amount1.setText(String.valueOf(temp));
          removeItemFromCart(item1Name);
        } else {
          toastRemove.show();
        }
        break;
      case R.id.minus2:
        temp = Integer.parseInt(amount2.getText().toString()) - 1;
        if (temp >= 0) {
          amount2.setText(String.valueOf(temp));
          removeItemFromCart(item2Name);
        } else {
          toastRemove.show();
        }
        break;
      case R.id.minus3:
        temp = Integer.parseInt(amount3.getText().toString()) - 1;
        if (temp >= 0) {
          amount3.setText(String.valueOf(temp));
          removeItemFromCart(item3Name);
        } else {
          toastRemove.show();
        }
        break;
      case R.id.minus4:
        temp = Integer.parseInt(amount4.getText().toString()) - 1;
        if (temp >= 0) {
          amount4.setText(String.valueOf(temp));
          removeItemFromCart(item4Name);
        } else {
          toastRemove.show();
        }
        break;
      case R.id.minus5:
        temp = Integer.parseInt(amount5.getText().toString()) - 1;
        if (temp >= 0) {
          amount5.setText(String.valueOf(temp));
          removeItemFromCart(item5Name);
        } else {
          toastRemove.show();
        }
        break;
      case R.id.checkOutButton:
        if (cartIsEmpty()) {
          DialogFactory.createAlertDialog(
                  appContext,
                  "Purchase Failed",
                  "Please add items to your cart!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
        } else {
          checkoutCart();
        }
        break;
      case R.id.checkOutExitButton:
        if (cartIsEmpty() || !customerHasActiveAccounts()) {
          ((CustomerCheckout) appContext).finish();
          appContext.startActivity(new Intent(this.appContext, LoginMenu.class));
        } else if (!cartIsEmpty() && customerHasActiveAccounts()) {
          DialogFactory.createAlertDialogYesNoCart(
                  appContext,
                  "Save Shopping Cart",
                  "Would you like to save" + " your shopping cart?",
                  "Yes",
                  "No",
                  DialogId.SAVE_SHOPPING_CART,
                  cart)
              .show();
        }
        break;
      case R.id.saveShoppingCartBtn:
        EditText saveAccountIdEntry = ((Activity) appContext).findViewById(R.id.saveAccountIdEntry);
        int accountId = 0;
        boolean isValidAccountId = true;

        try {
          accountId = Integer.parseInt(saveAccountIdEntry.getText().toString());
        } catch (NumberFormatException e) {
          isValidAccountId = false;
        }

        if (!isValidAccountId) {
          DialogFactory.createAlertDialog(
                  appContext,
                  "Account ID Format Error",
                  "Account ID can't be empty!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
        } else {
          saveShoppingCart(accountId);
        }
        break;
      case R.id.loadShoppingCartBtn:
        EditText loadAccountIdEntry = ((Activity) appContext).findViewById(R.id.loadAccountIdEntry);
        int loadAccountId = 0;
        boolean isValidLoadAccountId = true;
        try {
          loadAccountId = Integer.parseInt(loadAccountIdEntry.getText().toString());
        } catch (NumberFormatException e) {
          isValidLoadAccountId = false;
        }
        if (!isValidLoadAccountId) {
          DialogFactory.createAlertDialog(
                  appContext,
                  "Account ID Format Error",
                  "Account ID can't be empty!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
        } else {
          loadShoppingCart(loadAccountId);
        }
        break;
      case R.id.skipCartLoading:
        Intent intent = new Intent(appContext, CustomerCheckout.class);
        intent.putExtra("customer", customer);
        ((CustomerLoadShoppingCart) appContext).finish();
        appContext.startActivity(intent);
        break;
      case R.id.applyCouponBtn:
        EditText couponCode = ((Activity) appContext).findViewById(R.id.couponCodeEntry);
        String couponText = couponCode.getText().toString();
        if (isValidCouponName(couponText)) {
          applyCoupon(couponText);
        } else {
          DialogFactory.createAlertDialog(
                  appContext,
                  "Invalid Coupon",
                  "Please Enter a Valid Coupon Code!",
                  "Ok",
                  DialogId.NULL_DIALOG)
              .show();
        }
        break;
    }
  }

  /**
   * Checks if a coupon name is valid.
   *
   * @param name the coupon name of interest
   * @return true if yes, false otherwise
   */
  public boolean isValidCouponName(String name) {
    List<Integer> couponIds = null;
    try {
      couponIds = DatabaseHelperAdapter.getCouponIds();

      for (Integer id : couponIds) {
        if (DatabaseHelperAdapter.getCouponCode(id).equals(name)) {
          return true;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Apply coupon to the cart.
   *
   * @param couponText the coupon's name
   */
  public void applyCoupon(String couponText) {
    int result = cart.applyCoupon(couponText);

    if (result == -1) {
      DialogFactory.createAlertDialog(
              appContext,
              "Coupon Code Failed",
              "Make sure it's a valid coupon code"
                  + " or the max redemption limit has been exceeded!",
              "Ok",
              DialogId.NULL_DIALOG)
          .show();
    } else {
      updatePrice();
      DialogFactory.createAlertDialog(
              appContext,
              "Coupon Successful",
              "The discount was applied successfully!",
              "Ok",
              DialogId.NULL_DIALOG)
          .show();
    }
  }

  /**
   * Load shopping cart.
   *
   * @param accountId the account associated with the shopping cart.
   */
  public void loadShoppingCart(int accountId) {
    List<Integer> accts = new ArrayList<>();
    try {
      accts = DatabaseHelperAdapter.getUserActiveAccounts(customer.getId());
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (!accts.contains(accountId)) {
      DialogFactory.createAlertDialog(
              appContext,
              "Invalid Account Id",
              "Please input a valid account id!",
              "Ok",
              DialogId.NULL_DIALOG)
          .show();
    } else {

      Account account = new Account(customer.getId(), accountId, true);
      boolean failed = false;

      try {
        if (!account.retrieveCustomerCart()) {
          DialogFactory.createAlertDialogFailedCart(
              appContext,
              "Failure",
              "Something went wrong with" + " retrieving your cart",
              "Continue",
              DialogId.LOAD_CART_FAILED,
              customer);
          failed = true;
        }
      } catch (SQLException e) {
        failed = true;
      }

      ShoppingCart loadCart;
      if (!failed) {
        loadCart = account.getCart();
        if (loadCart != null && !loadCart.getItems().isEmpty()) {

          BigDecimal price =
              (loadCart.getTotal().multiply(loadCart.getTaxRate()))
                  .setScale(2, RoundingMode.CEILING);
          try {
            account.deactivate();
          } catch (SQLException e) {
          }
          loadCart.checkOutCart();
          DialogFactory.createAlertDialogFailedCart(
                  appContext,
                  "Check Out",
                  "The total price with tax is $" + price.toString(),
                  "Check out",
                  DialogId.CHECKOUT_LOADED_CART,
                  customer)
              .show();
        } else {
          DialogFactory.createAlertDialogFailedCart(
                  appContext,
                  "Empty Account",
                  "This account has no items, you will" + " be taken to the store!",
                  "Continue",
                  DialogId.LOAD_CART_FAILED,
                  customer)
              .show();
        }
      } else {
        DialogFactory.createAlertDialogFailedCart(
                appContext,
                "Failure",
                "Something went wrong with" + " retrieving your cart",
                "Continue",
                DialogId.LOAD_CART_FAILED,
                customer)
            .show();
      }
    }
  }

  /**
   * Save the shopping cart.
   *
   * @param accountId the account of interest.
   */
  public void saveShoppingCart(int accountId) {
    List<Integer> accts = new ArrayList<>();
    try {
      accts = DatabaseHelperAdapter.getUserActiveAccounts(cart.getCustomer().getId());
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (!accts.contains(accountId)) {
      DialogFactory.createAlertDialog(
              appContext,
              "Invalid Account Id",
              "Please input a valid account id!",
              "Ok",
              DialogId.NULL_DIALOG)
          .show();
    } else {
      Account account = new Account(cart.getCustomer().getId(), accountId, true);
      try {
        account.saveCustomerCart(cart);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      DialogFactory.createAlertDialog(
              appContext,
              "Success",
              "Cart has been successfully " + "saved!",
              "Ok",
              DialogId.SAVED_SHOPPING_CART)
          .show();
    }
  }

  /**
   * Check if customer has active accounts.
   *
   * @return true if yes, false otherwise
   */
  public boolean customerHasActiveAccounts() {
    List<Integer> accounts = null;

    try {
      accounts = DatabaseHelperAdapter.getUserActiveAccounts(customer.getId());
    } catch (SQLException e) {
    }

    return accounts != null && !accounts.isEmpty();
  }

  /**
   * Add a single item to the cart.
   *
   * @param itemName the item of interest
   */
  public void addItemToCart(String itemName) {
    int itemId = getItemIdByName(itemName);
    Item item = null;

    try {
      item = DatabaseHelperAdapter.getItem(itemId);
    } catch (SQLException e) {
    }

    cart.addItem(item, 1);
    updatePrice();
  }

  /**
   * Add a single item to the cart.
   *
   * @param itemName the item of interest
   */
  public void removeItemFromCart(String itemName) {
    int itemId = getItemIdByName(itemName);
    Item item = null;

    try {
      item = DatabaseHelperAdapter.getItem(itemId);
    } catch (SQLException e) {
    }

    cart.removeItem(item, 1);
    updatePrice();
  }

  /** Updates the price in the cart. */
  public void updatePrice() {
    BigDecimal price =
        (cart.getTotal().multiply(cart.getTaxRate())).setScale(2, RoundingMode.CEILING);

    TextView priceText = ((Activity) appContext).findViewById(R.id.priceTotal);
    priceText.setText("Total Price (with tax): $" + price.toString());
  }

  /** Checks out customer's cart. */
  public void checkoutCart() {
    BigDecimal price =
        (cart.getTotal().multiply(cart.getTaxRate())).setScale(2, RoundingMode.CEILING);
    cart.checkOutCart();

    DialogFactory.createAlertDialog(
            appContext,
            "Checkout",
            "You have successfully checked out with " + "total $" + price.toString(),
            "Ok",
            DialogId.CHECKOUT_CART)
        .show();
  }

  /**
   * Checks if customer's cart is empty
   *
   * @return true if empty, false otherwise
   */
  public boolean cartIsEmpty() {
    return cart.getTotal().compareTo(BigDecimal.ZERO) == 0;
  }

  /**
   * Get total number of items left for an item
   *
   * @param itemName the item of interest
   * @return quantity
   */
  public int getMaxQuantity(String itemName) {
    int max = 0;

    try {
      for (int x = 1; x < 6; x++) {
        Item item = DatabaseHelperAdapter.getItem(x);
        if (item.getName().equals(itemName)) {
          max = DatabaseHelperAdapter.getInventoryQuantity(x);
        }
      }
    } catch (SQLException e) {
    }
    return max;
  }

  /**
   * Get the item id of an item given it's name.
   *
   * @param itemName the item's name
   * @return id of item
   */
  public int getItemIdByName(String itemName) {
    int id = 0;

    try {
      for (int x = 1; x < 6; x++) {
        if (DatabaseHelperAdapter.getItem(x).getName().equals(itemName)) {
          return x;
        }
      }
    } catch (SQLException e) {
    }
    return id;
  }
}
