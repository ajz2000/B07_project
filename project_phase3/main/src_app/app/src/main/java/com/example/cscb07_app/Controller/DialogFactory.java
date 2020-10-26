package com.example.cscb07_app.Controller;

import android.app.AlertDialog;
import android.content.Context;
import com.b07.store.ShoppingCart;
import com.b07.users.Customer;

/** Factory to create alert dialog boxes. */
public class DialogFactory {

  /**
   * Creates an alert dialog.
   *
   * @param appContext the context of interest
   * @param title the dialog's title
   * @param message the dialog's message
   * @param btnText the dialog's button's text
   * @param id the dialog's id
   * @return the alert dialog
   */
  public static AlertDialog createAlertDialog(
      Context appContext, String title, String message, String btnText, DialogId id) {

    AlertDialog dialog = new AlertDialog.Builder(appContext).create();
    dialog.setTitle(title);
    dialog.setMessage(message);
    dialog.setButton(AlertDialog.BUTTON_NEUTRAL, btnText, new DialogController(appContext, id));
    return dialog;
  }

  /**
   * Create an yes/no dialog box.
   *
   * @param appContext the context
   * @param title the dialog's title
   * @param message the dialog's message
   * @param btnYesText the dialog's yes message
   * @param btnNoText the dialog's no message
   * @param id the dialog's id
   * @return the alert dialog
   */
  public static AlertDialog createAlertDialogYesNo(
      Context appContext,
      String title,
      String message,
      String btnYesText,
      String btnNoText,
      DialogId id) {

    AlertDialog dialog = new AlertDialog.Builder(appContext).create();
    dialog.setTitle(title);
    dialog.setMessage(message);
    dialog.setButton(AlertDialog.BUTTON_NEGATIVE, btnNoText, new DialogController(appContext, id));
    dialog.setButton(AlertDialog.BUTTON_POSITIVE, btnYesText, new DialogController(appContext, id));
    return dialog;
  }

  /**
   * Create an yes/no dialog box.
   *
   * @param appContext the context
   * @param title the dialog's title
   * @param message the dialog's message
   * @param btnYesText the dialog's yes message
   * @param btnNoText the dialog's no message
   * @param id the dialog's id
   * @return the alert dialog
   */
  public static AlertDialog createAlertDialogYesNoCart(
      Context appContext,
      String title,
      String message,
      String btnYesText,
      String btnNoText,
      DialogId id,
      ShoppingCart cart) {

    AlertDialog dialog = new AlertDialog.Builder(appContext).create();
    dialog.setTitle(title);
    dialog.setMessage(message);
    dialog.setButton(
        AlertDialog.BUTTON_POSITIVE, btnYesText, new DialogController(appContext, id, cart));
    dialog.setButton(
        AlertDialog.BUTTON_NEGATIVE, btnNoText, new DialogController(appContext, id, cart));
    return dialog;
  }

  /**
   * Create an yes/no dialog box.
   *
   * @param appContext the context
   * @param title the dialog's title
   * @param message the dialog's message
   * @param id the dialog's id
   * @return the alert dialog
   */
  public static AlertDialog createAlertDialogFailedCart(
      Context appContext,
      String title,
      String message,
      String btnText,
      DialogId id,
      Customer customer) {

    AlertDialog dialog = new AlertDialog.Builder(appContext).create();
    dialog.setTitle(title);
    dialog.setMessage(message);
    dialog.setButton(
        AlertDialog.BUTTON_NEUTRAL, btnText, new DialogController(appContext, id, customer));
    return dialog;
  }
}
