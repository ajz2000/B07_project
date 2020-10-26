package com.b07.store;

import java.math.BigDecimal;

public class CouponImpl implements Coupon {

  int item;
  int uses;
  String type;
  BigDecimal discount;
  String code;

  public CouponImpl(int item, int uses, String type, BigDecimal discount, String code) {
    this.item = item;
    this.uses = uses;
    this.type = type;
    this.discount = discount;
    this.code = code;
  }

  @Override
  public int getItemId() {
    return item;
  }

  @Override
  public void setItemId(int item) {
    this.item = item;
  }

  @Override
  public BigDecimal getDiscount() {
    return this.discount;
  }

  @Override
  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }

  @Override
  public String getCode() {
    return this.code;
  }

  @Override
  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String getType() {
    return this.type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public int getUses() {
    return this.uses;
  }

  @Override
  public void setUses(int uses) {
    this.uses = uses;
  }
}
