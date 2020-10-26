package com.b07.store;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Interface for object representation of a coupon
 *
 * @author lingf
 */
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

  /**
   * get the code of the coupon
   *
   * @return code of the coupon
   */
  public String getCode();

  /**
   * set the code of the coupon
   *
   * @param code of the coupon
   */
  public void setCode(String code);

  /**
   * get the type of the coupon
   *
   * @return coupon type
   */
  public String getType();

  /**
   * set the type of the coupon
   *
   * @param type of coupon
   */
  public void setType(String type);

  /**
   * get the number of uses the coupon has
   *
   * @return the number of uses
   */
  public int getUses();

  /**
   * set the number of uses the coupon has
   *
   * @param uses the coupon has
   */
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
