package com.ferdouszislam.nsu.cse486.sec01.signup_login_application;

public class User {
// model class for MainActivity

    public static final String ALLOWED_USERNAME = "mr_Snow";
    public static final String ALLOWED_PASSWORD = "kinginthenorth";

    private String username="";
    private String password="";

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean validate(){

        this.username = this.username.trim();

        if(username.isEmpty() || password.isEmpty())
            return false;

        return true;
    }

    public boolean authenticate(){

        if(this.username.equals(ALLOWED_USERNAME) && this.password.equals(ALLOWED_PASSWORD))
            return true;

        return false;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
