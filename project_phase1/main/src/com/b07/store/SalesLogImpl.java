package com.b07.store;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple implementation of the salesLog interface.
 * 
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public class SalesLogImpl implements SalesLog {

  private ArrayList<Sale> sales = new ArrayList<Sale>();

  /**
   * Return the sales log.
   * 
   * @return list of sales
   */
  @Override
  public List<Sale> getSales() {
    return sales;
  }

  /**
   * Add a sale to the log.
   */
  @Override
  public void addSale(Sale sale) {
    // TODO: there might be a bug with contains
    if (!sales.contains(sale)) {
      sales.add(sale);
    }
  }

  /**
   * Get total value of sales in sale log.
   */
  @Override
  public BigDecimal getTotalValueOfSales() {

    BigDecimal value = null;

    for (Sale sale : sales) {
      value = value.add(sale.getTotalPrice());
    }

    return value;
  }

  /**
   * Returns the total number of sales.
   * 
   * @return total number of sales
   */
  @Override
  public int getTotalSalesCount() {
    return sales.size();
  }
}
