package com.example.cscb07_app.Activity.Customer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.store.ShoppingCart;
import com.example.cscb07_app.Controller.CustomerController;
import com.example.cscb07_app.R;
import java.sql.SQLException;
import java.util.List;

/** Class of the save shopping cart activity */
public class CustomerSaveShoppingCart extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_save_shopping_cart);

    ShoppingCart cart = (ShoppingCart) getIntent().getSerializableExtra("cart");
    loadAccountIds(cart);

    Button saveCart = findViewById(R.id.saveShoppingCartBtn);
    saveCart.setOnClickListener(new CustomerController(this, cart));
  }

  /**
   * Loads all account ads in a shoppingcart
   *
   * @param cart the shopping cart
   */
  public void loadAccountIds(ShoppingCart cart) {
    List<Integer> accounts = null;

    try {
      accounts = DatabaseHelperAdapter.getUserActiveAccounts(cart.getCustomer().getId());
    } catch (SQLException e) {
    }

    TextView saveAccountIds = findViewById(R.id.accountIdsData);

    StringBuilder data = new StringBuilder();
    data.append("Active Account IDs\n");
    data.append("------------------------------\n");

    for (Integer acct : accounts) {
      data.append("Account ID: " + acct + "\n");
    }

    saveAccountIds.setText(data.toString());
  }

  /** overrides the back button to do nothing */
  @Override
  public void onBackPressed() {
    return;
  }
}
