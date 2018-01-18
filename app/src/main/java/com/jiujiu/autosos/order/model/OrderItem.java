package com.jiujiu.autosos.order.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/12.
 */

public class OrderItem implements Serializable {
    /**
     * calculationType : null
     * itemId : 0
     * specialEnd : null
     * itemName : 拖车
     * perPrice : 0
     * price : null
     * additional : 0
     * perKilometre : 0
     * baseKilometre : 0
     * specialStart : null
     * basePrice : 0
     */

    private Integer calculationType;
    private String itemId;
    private String specialEnd;
    private String itemName;
    private double perPrice;
    private double price;
    private double additional;
    private double perKilometre;
    private double baseKilometre;
    private String specialStart;
    private double basePrice;

    public Integer getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(Integer calculationType) {
        this.calculationType = calculationType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSpecialEnd() {
        return specialEnd;
    }

    public void setSpecialEnd(String specialEnd) {
        this.specialEnd = specialEnd;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(double perPrice) {
        this.perPrice = perPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAdditional() {
        return additional;
    }

    public void setAdditional(double additional) {
        this.additional = additional;
    }

    public double getPerKilometre() {
        return perKilometre;
    }

    public void setPerKilometre(double perKilometre) {
        this.perKilometre = perKilometre;
    }

    public double getBaseKilometre() {
        return baseKilometre;
    }

    public void setBaseKilometre(double baseKilometre) {
        this.baseKilometre = baseKilometre;
    }

    public String getSpecialStart() {
        return specialStart;
    }

    public void setSpecialStart(String specialStart) {
        this.specialStart = specialStart;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
}
