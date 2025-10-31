package com.example.camaraderie;//

public class User {

    private String firstName;
    private String phoneNumber;
    private String email;
    private String address;
    private String userId;

    private boolean admin = false;

    //private String bankNumber;  // REQUIRED to sign up for events, but not to create account (we probably don't need this)


    public User(String firstName, String phone, String email, String address, String userId){
        this.firstName = firstName;
        this.phoneNumber = phone;
        this.email = email;
        this.address = address;
        this.userId = userId;
    }


    public boolean isAdmin() {return admin;}
    public void setAdmin(boolean this_admin) {  // we MANUALLY create admins for the app
        admin = this_admin;
    }
    public void setFirstName(String name) {this.firstName = name;}
    public void setPhoneNumber(String number) {this.phoneNumber = number;}
    public void setEmail(String email1) {this.email = email1;}
    public void setAddress(String address1) {this.address = address1;}

    public String getFirstName() {return this.firstName;}
    //public String getLastName() {return this.lastName;}
    public String getPhoneNumber() {return this.phoneNumber;}
    public String getEmail() {return this.email;}
    public String getAddress() {return this.address;}
    public String getUserId() {return this.userId;}

//    public String getBankNumber() {
//        return bankNumber;
//    }
//
//    public void setBankNumber(String bankNumber) {
//        this.bankNumber = bankNumber;
//    }
}
