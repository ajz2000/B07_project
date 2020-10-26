package com.example.cscb07_app.Activity.Customer;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.store.ShoppingCart;
import com.b07.users.Customer;
import com.example.cscb07_app.Controller.CustomerController;
import com.example.cscb07_app.R;

/** Customer checkout. */
public class CustomerCheckout extends AppCompatActivity {

  /**
   * Creation of activity.
   *
   * @param savedInstanceState the state of the app
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_checkout);

    Button plusItem1 = findViewById(R.id.plus1);
    Button plusItem2 = findViewById(R.id.plus2);
    Button plusItem3 = findViewById(R.id.plus3);
    Button plusItem4 = findViewById(R.id.plus4);
    Button plusItem5 = findViewById(R.id.plus5);
    Button minusItem1 = findViewById(R.id.minus1);
    Button minusItem2 = findViewById(R.id.minus2);
    Button minusItem3 = findViewById(R.id.minus3);
    Button minusItem4 = findViewById(R.id.minus4);
    Button minusItem5 = findViewById(R.id.minus5);

    Customer customer = (Customer) getIntent().getSerializableExtra("customer");
    ShoppingCart cart = new ShoppingCart(customer);

    plusItem1.setOnClickListener(new CustomerController(this, cart));
    plusItem2.setOnClickListener(new CustomerController(this, cart));
    plusItem3.setOnClickListener(new CustomerController(this, cart));
    plusItem4.setOnClickListener(new CustomerController(this, cart));
    plusItem5.setOnClickListener(new CustomerController(this, cart));

    minusItem1.setOnClickListener(new CustomerController(this, cart));
    minusItem2.setOnClickListener(new CustomerController(this, cart));
    minusItem3.setOnClickListener(new CustomerController(this, cart));
    minusItem4.setOnClickListener(new CustomerController(this, cart));
    minusItem5.setOnClickListener(new CustomerController(this, cart));

    Button checkoutBtn = findViewById(R.id.checkOutButton);
    checkoutBtn.setOnClickListener(new CustomerController(this, cart));

    Button exitBtn = findViewById(R.id.checkOutExitButton);
    exitBtn.setOnClickListener(new CustomerController(this, cart));

    Button applyCoupon = findViewById(R.id.applyCouponBtn);
    applyCoupon.setOnClickListener(new CustomerController(this, cart));
  }

  @Override
  public void onBackPressed() {
    return;
  }
}
