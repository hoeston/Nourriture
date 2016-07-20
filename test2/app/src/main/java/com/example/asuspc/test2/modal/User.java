package com.example.asuspc.test2.modal;

import java.util.List;

/**
 * Created by niezeshu on 16/7/16.
 */
public class User {
    private int id;
    private String name;
    List<Dish> data;

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

    public List<Dish> getData() {
        return data;
    }

    public void setData(List<Dish> data) {
        this.data = data;
    }


    public User(int id, String name, List<Dish> data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }


}
