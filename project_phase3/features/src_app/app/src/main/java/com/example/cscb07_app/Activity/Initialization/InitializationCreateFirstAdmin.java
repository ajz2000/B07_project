package com.example.cscb07_app.Activity.Initialization;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.ItemTypes;
import com.b07.store.DiscountTypes;
import com.b07.users.Roles;
import com.example.cscb07_app.Controller.InitializationController;
import com.example.cscb07_app.R;
import java.math.BigDecimal;
import java.sql.SQLException;

/** Class of the create first admin activity */
public class InitializationCreateFirstAdmin extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_initialization_create_first_admin);

    try {
      // To avoid duplicate initialization
      if (DatabaseHelperAdapter.getUserDetails(1) == null) {
        setUpDatabase();
      }
    } catch (DatabaseInsertException e) {
    } catch (SQLException e) {
    }

    Button createAdmin = findViewById(R.id.initializationCreateAdminButton);
    createAdmin.setOnClickListener(new InitializationController(this));
  }

  /** Overrides back button to not do anything */
  @Override
  public void onBackPressed() {
    return;
  }

  /**
   * Sets up the database
   *
   * @throws DatabaseInsertException
   * @throws SQLException
   */
  public void setUpDatabase() throws DatabaseInsertException, SQLException {

    for (DiscountTypes type : DiscountTypes.values()) {
      DatabaseHelperAdapter.insertDiscountType(type.name());
    }

    for (Roles role : Roles.values()) {
      DatabaseHelperAdapter.insertRole(role.name());
    }

    for (ItemTypes item : ItemTypes.values()) {
      int itemId = DatabaseHelperAdapter.insertItem(item.name(), new BigDecimal("10.00"));
      DatabaseHelperAdapter.insertInventory(itemId, 0);
    }
  }
}
