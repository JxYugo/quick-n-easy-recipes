package com.softwareengineering.recipeapp.requestManager;

import java.util.List;

public class ReadWriteUserDetails {
    public String email;
    public List<String> allergies;

    public ReadWriteUserDetails(){}

    public ReadWriteUserDetails(String email, List<String> allergies){
        this.email = email;
        this.allergies = allergies;
    }
}
