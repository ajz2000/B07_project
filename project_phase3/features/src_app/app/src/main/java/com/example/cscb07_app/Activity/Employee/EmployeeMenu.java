package com.example.cscb07_app.Activity.Employee;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.store.EmployeeInterface;
import com.b07.users.Employee;
import com.example.cscb07_app.Controller.EmployeeController;
import com.example.cscb07_app.R;
import java.sql.SQLException;

/** Class of the employeemenu activity */
public class EmployeeMenu extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_employee_menu);

    Employee currentEmployee = (Employee) getIntent().getSerializableExtra("employee");
    EmployeeInterface employeeInterface = null;
    try {
      employeeInterface =
          new EmployeeInterface(currentEmployee, DatabaseHelperAdapter.getInventory());
    } catch (SQLException e) {
      // Should never occur, holdover from cross platform adapter pattern.
    }

    Button authenticateEmployee = findViewById(R.id.menuAuthenticateEmployeeBtn);
    authenticateEmployee.setOnClickListener(new EmployeeController(this, employeeInterface));

    Button makeUser = findViewById(R.id.menuMakeNewUserBtn);
    makeUser.setOnClickListener(new EmployeeController(this, employeeInterface));

    Button makeAccount = findViewById(R.id.menuMakeNewAccountBtn);
    makeAccount.setOnClickListener(new EmployeeController(this, employeeInterface));

    Button makeEmployee = findViewById(R.id.menuMakeNewEmployeeBtn);
    makeEmployee.setOnClickListener(new EmployeeController(this, employeeInterface));

    Button restockInventory = findViewById(R.id.menuRestockInventoryBtn);
    restockInventory.setOnClickListener(new EmployeeController(this, employeeInterface));

    Button exitButton = findViewById(R.id.menuExitBtn);
    exitButton.setOnClickListener(new EmployeeController(this, employeeInterface));
  }

  /** overrides the back button to do nothing */
  @Override
  public void onBackPressed() {
    return;
  }
}
