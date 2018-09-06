package com.daakit.map;

public class registermap {
    String fullname,email,phone;

    public registermap()
    {

    }

    public registermap(String fullname,String email,String phone)
    {
        this.fullname=fullname;
        this.email=email;
        this.phone=phone;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}

