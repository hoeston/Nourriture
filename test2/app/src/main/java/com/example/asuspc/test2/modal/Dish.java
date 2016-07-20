package com.example.asuspc.test2.modal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niezeshu on 16/7/16.
 */
public class Dish {
    private String name;
    private String level;
    private String time;
    private String userName;
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private List<DishProcess> dishProcesses = new ArrayList<>();
    private List<DishIngredient> dishIngredients = new ArrayList<>();


    public List<DishIngredient> getDishIngredients() {
        return dishIngredients;
    }

    public void setDishIngredients(List<DishIngredient> dishIngredients) {
        this.dishIngredients = dishIngredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<DishProcess> getDishProcesses() {
        return dishProcesses;
    }

    public void setDishProcesses(List<DishProcess> dishProcesses) {
        this.dishProcesses = dishProcesses;
    }
}
