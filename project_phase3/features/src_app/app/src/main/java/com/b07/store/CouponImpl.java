package com.b07.store;

import java.math.BigDecimal;

/**
 * A class that implements coupon interface
 *
 * @author lingf
 */
public class CouponImpl implements Coupon {

  int item;
  int uses;
  String type;
  BigDecimal discount;
  String code;

  /**
   * constructs a coupon
   *
   * @param item
   * @param uses
   * @param type
   * @param discount
   * @param code
   */
  public CouponImpl(int item, int uses, String type, BigDecimal discount, String code) {
    this.item = item;
    this.uses = uses;
    this.type = type;
    this.discount = discount;
    this.code = code;
  }

  /**
   * gets the item id of the coupon
   *
   * @return item id
   */
  @Override
  public int getItemId() {
    return item;
  }

  /**
   * sets the itemId of the coupon
   *
   * @param item id
   */
  @Override
  public void setItemId(int item) {
    this.item = item;
  }

  /**
   * get the discount amount for the coupon
   *
   * @return discount amount
   */
  @Override
  public BigDecimal getDiscount() {
    return this.discount;
  }

  /**
   * set the discount amount for the coupon
   *
   * @param discount the discount amount
   */
  @Override
  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }

  /**
   * get the code of the coupon
   *
   * @return code of the coupon
   */
  @Override
  public String getCode() {
    return this.code;
  }

  /**
   * set the code of the coupon
   *
   * @param code of the coupon
   */
  @Override
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * get the type of the coupon
   *
   * @return coupon type
   */
  @Override
  public String getType() {
    return this.type;
  }

  /**
   * set the type of the coupon
   *
   * @param type of coupon
   */
  @Override
  public void setType(String type) {
    this.type = type;
  }

  /**
   * get the number of uses the coupon has
   *
   * @return number of uses
   */
  @Override
  public int getUses() {
    return this.uses;
  }

  /**
   * set the number of uses the coupon has
   *
   * @param uses the coupon has
   */
  @Override
  public void setUses(int uses) {
    this.uses = uses;
  }
}
