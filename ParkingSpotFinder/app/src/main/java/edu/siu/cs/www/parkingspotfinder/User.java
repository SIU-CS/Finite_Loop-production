package edu.siu.cs.www.parkingspotfinder;

/**
 * Created by jacobreed on 2/22/17.
 */

public class User {
    public String name;
    public String email;

    public User(){}

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){ return name; }

    public String getEmail(){ return email; }

}
