package com.b07.store;

import java.math.BigDecimal;
import java.util.List;

/**
 * A log to keep track of all transactions.
 * 
 * @author Aidan Zorbas
 * @author Alex Efimov
 * @author Lingfeng Su
 * @author Payam Yektamaram
 */
public interface SalesLog {


  /**
   * Get the log of all the sale transactions.
   * 
   * @return list of sales
   */
  public List<Sale> getSales();


  /**
   * Add a sale to the log.
   * 
   * @param sale the sale to be added
   */
  public void addSale(Sale sale);

  /**
   * Get the total number of sales.
   * 
   * @return total number of sales
   */
  public int getTotalSalesCount();

  /**
   * Get the total value of all the sales.
   * 
   * @return total sales
   */
  public BigDecimal getTotalValueOfSales();

}
