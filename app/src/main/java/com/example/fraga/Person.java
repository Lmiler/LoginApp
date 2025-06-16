package com.example.fraga;

public class Person {
//    public String name;
    public String email;
    public String phone;
    public String address;
    public String personStr;

    public Person () {}

    public Person(String email, String phone, String address, String personStr) {
//        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.personStr = personStr;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPersonStr() {
        return personStr;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPersonStr(String personStr) {
        this.personStr = personStr;
    }
}
