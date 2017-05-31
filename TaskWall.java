package com.example.ashutosh.testrreminderui;

/**
 * Created by Ashutosh on 5/31/2017.
 */

public class TaskWall {


    private int id;
    private String name;
    private String amount;
    private byte[] photos;

    public TaskWall(int id, String name, String amount, byte[] photos) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.photos = photos;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public byte[] getPhotos() {
        return photos;
    }

    public void setPhotos(byte[] photos) {
        this.photos = photos;
    }
}
