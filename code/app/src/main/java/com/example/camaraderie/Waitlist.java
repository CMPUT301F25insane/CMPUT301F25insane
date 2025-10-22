package com.example.camaraderie;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/*ONLY GETS CREATED BY EVENT CLASS*/
public class Waitlist {

    private ArrayList<User> waitlist = new ArrayList<>();
    private Event event;

    public void addUserToWaitlist(User user) {
        if (!waitlist.contains(user)) {
            waitlist.add(user);
        }
    }

    public void removeUserFromWaitlist(User user) {
        this.waitlist.remove(user);  // does nothing if user not in waitlist
    }

    public User randomSelectUser() {
        int rand = new Random().nextInt(getSize());
        return waitlist.get(rand);
    }

    public User randomSelectUserAndRemove() {
        User user = randomSelectUser();
        waitlist.remove(user);
        return user;
    }

    public int getSize() {
        return this.waitlist.size();
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
