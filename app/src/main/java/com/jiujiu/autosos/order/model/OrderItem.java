package com.jiujiu.autosos.order.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/12.
 */

public class OrderItem implements Serializable {

    /**
     * additional : 0
     * baseKilometre : 0
     * basePrice : 0
     * itemId : 0
     * itemName : 拖车
     * perKilometre : 0
     * perPrice : 0
     */

    private int additional;
    private int baseKilometre;
    private int basePrice;
    private String itemId;
    private String itemName;
    private int perKilometre;
    private int perPrice;

    public int getAdditional() {
        return additional;
    }

    public void setAdditional(int additional) {
        this.additional = additional;
    }

    public int getBaseKilometre() {
        return baseKilometre;
    }

    public void setBaseKilometre(int baseKilometre) {
        this.baseKilometre = baseKilometre;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPerKilometre() {
        return perKilometre;
    }

    public void setPerKilometre(int perKilometre) {
        this.perKilometre = perKilometre;
    }

    public int getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(int perPrice) {
        this.perPrice = perPrice;
    }
}
