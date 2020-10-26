package com.b07.store;

import java.math.BigDecimal;

/**
 * A class that implements coupon interface
 *
 * @author lingf
 */
public class CouponImpl implements Coupon {

  /**
   * The Serial Version ID of CouponImpl.
   */
  private static final long serialVersionUID = 1L;
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
    // TODO Auto-generated method stub
    return item;
  }

  /**
   * sets the itemId of the coupon
   *
   * @param item id
   */
  @Override
  public void setItemId(int item) {
    // TODO Auto-generated method stub
    this.item = item;
  }

  /**
   * get the discount amount for the coupon
   *
   * @return discount amount
   */
  @Override
  public BigDecimal getDiscount() {
    // TODO Auto-generated method stub
    return this.discount;
  }

  /**
   * set the discount amount for the coupon
   *
   * @param discount amount
   */
  @Override
  public void setDiscount(BigDecimal discount) {
    // TODO Auto-generated method stub
    this.discount = discount;
  }

  /**
   * get the code of the coupon
   *
   * @return code of the coupon
   */
  @Override
  public String getCode() {
    // TODO Auto-generated method stub
    return this.code;
  }

  /**
   * set the code of the coupon
   *
   * @param code of the coupon
   */
  @Override
  public void setCode(String code) {
    // TODO Auto-generated method stub
    this.code = code;
  }

  /**
   * get the type of the coupon
   *
   * @return coupon type
   */
  @Override
  public String getType() {
    // TODO Auto-generated method stub
    return this.type;
  }

  /**
   * set the type of the coupon
   *
   * @param type of coupon
   */
  @Override
  public void setType(String type) {
    // TODO Auto-generated method stub
    this.type = type;
  }

  /**
   * get the number of uses the coupon has
   *
   * @return the number of uses
   */
  @Override
  public int getUses() {
    // TODO Auto-generated method stub
    return this.uses;
  }

  /**
   * set the number of uses the coupon has
   *
   * @param uses the coupon has
   */
  @Override
  public void setUses(int uses) {
    // TODO Auto-generated method stub
    this.uses = uses;
  }
}
