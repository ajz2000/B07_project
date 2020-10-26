package com.b07.store;

import com.b07.database.helper.DatabaseHelperAdapter;
import com.b07.exceptions.WrongRoleException;
import com.b07.users.Roles;
import com.b07.users.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * A series of helper methods for use by the store application.
 *
 * @author Alex Efimov
 */
public class StoreHelpers {

  /**
   * check whether password matches a user's password.
   *
   * @param role the user's role
   * @param id the user's id
   * @param password the password
   * @return whether the login was successful
   * @throws SQLException if there was an issue communicating with the database.
   * @throws WrongRoleException if the user entered the correct credentials, but for the wrong role
   */
  public static final User login(Roles role, int id, String password)
      throws SQLException, WrongRoleException {
    User user = DatabaseHelperAdapter.getUserDetails(id);
    if (user == null) {
      return null;
    }
    int roleId = user.getRoleId();
    if (!Roles.valueOf(DatabaseHelperAdapter.getRoleName(roleId)).equals(role)) {
      throw new WrongRoleException();
    }
    if (user.authenticate(password)) {
      return user;
    }
    return null;
  }

  /**
   * Creates a prompt for a user to login with their ID and password, to the given role.
   *
   * @param reader to read the input
   * @param role the role to log in as
   * @return the User if login is successful, null otherwise
   * @throws IOException if there is an issue getting user input
   * @throws SQLException if there is an issue communicating with the database
   */
  public static final User loginPrompt(BufferedReader reader, Roles role)
      throws IOException, SQLException {
    System.out.println("Enter your ID");
    int id;
    String password;
    try {
      id = Integer.parseInt(reader.readLine());
      System.out.println("Enter your password");
      password = reader.readLine();
      User user = login(role, id, password);
      if (user == null) {
        System.out.println("Incorrect password");
        return null;
      }
      System.out.println(
          String.format(
              "Successfully logged in to role ID %d with user ID %d",
              user.getRoleId(), user.getId()));
      System.out.println(String.format("Welcome, %s", user.getName()));
      return user;
    } catch (NumberFormatException e) {
      System.out.println("Unable to read ID. Please input only the number.");
    } catch (WrongRoleException e) {
      System.out.println("Please log in to the correct role");
    }
    return null;
  }

  /**
   * This method presents the user with a list of choices and returns the user's choice as an int.
   * Prints an error message if the user did not make a valid choice.
   *
   * @param choices an array of choices as strings that will be printed for the user
   * @param reader a reader to get the user's input from
   * @return the user's choice, -1 if the user did not make a valid choice
   * @throws IOException if there is an issue getting user input
   */
  public static int choicePrompt(String[] choices, BufferedReader reader) throws IOException {
    for (String string : choices) {
      System.out.println(string);
    }
    int response = -1;
    try {
      response = Integer.parseInt(reader.readLine());
    } catch (NumberFormatException e) {
      System.out.println("Please choose a number.");
    }
    return response;
  }
}
