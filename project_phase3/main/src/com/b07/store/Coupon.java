package com.b07.store;

import java.io.Serializable;
import java.math.BigDecimal;

public interface Coupon extends Serializable {

  /**
   * Get the item the coupon is for
   *
   * @return the item, or null if there is no item for this coupon
   */
  public int getItemId();

  /**
   * set this coupon's item to item
   *
   * @param item
   */
  public void setItemId(int item);

  public String getCode();

  public void setCode(String code);

  public String getType();

  public void setType(String type);

  public int getUses();

  public void setUses(int uses);

  /**
   * Get the discount percentage as a BigDecimal
   *
   * @return the discount
   */
  public BigDecimal getDiscount();

  /**
   * set the discount of this coupon
   *
   * @param discount: the discount
   */
  public void setDiscount(BigDecimal discount);
}
