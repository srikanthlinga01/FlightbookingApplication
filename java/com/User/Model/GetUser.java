package com.User.Model;

import java.io.Serializable;

public class GetUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String fullname;
    private String email;
    private String mobile;
    private String password;

    // Constructors
    public GetUser() {}

    public GetUser(int id, String fullname, String email, String mobile, String password) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }

    public GetUser(String fullname, String email, String mobile, String password) {
        this.fullname = fullname;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }

    public GetUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public GetUser(int id, String fullname, String email, String mobile) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.mobile = mobile;
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // toString() for debugging (does NOT include password)
    @Override
    public String toString() {
        return "GetUser [id=" + id + ", fullname=" + fullname + ", email=" + email + ", mobile=" + mobile + "]";
    }
}
