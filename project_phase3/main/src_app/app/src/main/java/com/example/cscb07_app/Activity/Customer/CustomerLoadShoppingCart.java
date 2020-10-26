package com.example.cscb07_app.Activity.Customer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.users.Customer;
import com.example.cscb07_app.Controller.CustomerController;
import com.example.cscb07_app.R;
import java.sql.SQLException;
import java.util.List;

/** Activity to load customer shopping cart. */
public class CustomerLoadShoppingCart extends AppCompatActivity {

  /**
   * Creation of activity.
   *
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_load_shopping_cart);

    Customer customer = (Customer) getIntent().getSerializableExtra("customer");
    loadAccountIds(customer);

    Button loadShoppingCart = findViewById(R.id.loadShoppingCartBtn);
    loadShoppingCart.setOnClickListener(new CustomerController(this, customer));

    Button skip = findViewById(R.id.skipCartLoading);
    skip.setOnClickListener(new CustomerController(this, customer));
  }

  /**
   * Loads all the customer's active accounts into the layout.
   *
   * @param customer the customer of interest
   */
  public void loadAccountIds(Customer customer) {
    List<Integer> accounts = null;

    try {
      accounts = DatabaseHelperAdapter.getUserActiveAccounts(customer.getId());
    } catch (SQLException e) {
    }

    TextView loadAccountIds = findViewById(R.id.loadAccountIdsData);

    StringBuilder data = new StringBuilder();
    data.append("Active Account IDs\n");
    data.append("------------------------------\n");

    for (Integer acct : accounts) {
      data.append("Account ID: " + acct + "\n");
    }

    loadAccountIds.setText(data.toString());
  }

  @Override
  public void onBackPressed() {
    return;
  }
}
