package app.petkoul.help_us;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petkoul on 30-Dec-17.
 */

public class User implements Serializable {



    private String uid;
    private String email;
    private String name;
    private String surname;
    private String age;
    private String category;

    public User() {
    }

    public User(String uid, String email, String name, String surname, String age, String category) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.category = category;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age='" + age + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
