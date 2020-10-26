package com.b07.database.helper;

import com.b07.database.DatabaseDriver;
import java.sql.Connection;

/**
 * A helper method allowing connections to the database to be made within the package.
 *
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Si
 * @author Payam Yektamaram
 */
public class DatabaseDriverHelper extends DatabaseDriver {

  protected static Connection connectOrCreateDataBase() {
    return DatabaseDriver.connectOrCreateDataBase();
  }
}
