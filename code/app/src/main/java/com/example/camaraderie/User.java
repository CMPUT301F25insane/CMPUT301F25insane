package com.example.camaraderie;

public class User {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String username;
    private String address;
    private final String userId;

    private String bankNumber;  // REQUIRED to sign up for events, but not to create account (we probably don't need this)


    public User(String firstName, String lastName, String phone, String email, String username, String address, String userId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phone;
        this.email = email;
        this.username = username;
        this.address = address;
        this.userId = userId;
    }

    /***
     * Adds the use to the waitlist to the database
     * @param event
     */
    public void addToEvent(Event event){
        Waitlist waitlist = event.getWaitlist();
        waitlist.addUserToWaitlist(this);
    }

    public void setFirstName(String name) {this.firstName = name;}
    public void setLastName(String name) {this.lastName = name;}
    public void setPhoneNumber(String number) {this.phoneNumber = number;}
    public void setEmail(String email1) {this.email = email1;}
    public void setUsername(String name) {this.username = name;}
    public void setAddress(String address1) {this.address = address1;}

    public String getFirstName() {return this.firstName;}
    public String getLastName() {return this.lastName;}
    public String getPhoneNumber() {return this.phoneNumber;}
    public String getEmail() {return this.email;}
    public String getUsername() {return this.username;}
    public String getAddress() {return this.address;}
    public String getUserId() {return this.userId;}

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }
}
