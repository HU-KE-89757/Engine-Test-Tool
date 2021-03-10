package com.mercury.demo.entity;

import lombok.Data;

@Data
public class Users {
    public int id;
    public String name;
    public int age;

    public Users(int id, String name, int age) {
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
