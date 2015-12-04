package ru.bmstu.iu6.hackatonmobile.models;

/**
 * Created by mikrut on 04.12.15.
 */
public class FoodModel {

    private short maxPrice;
    private int id;


    public FoodModel() {}

    public short getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(short maxPrice) {
        this.maxPrice = maxPrice;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
