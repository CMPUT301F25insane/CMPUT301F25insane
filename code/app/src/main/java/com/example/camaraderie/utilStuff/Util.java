package com.example.camaraderie.utilStuff;//

import com.example.camaraderie.Event;
import com.example.camaraderie.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class Util {
    public static void clearAndAddDummyEvents() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventsRef = db.collection("Events");
        CollectionReference usersRef = db.collection("Users");

        // TODO: put the real delete functions here, these dont work lol
        eventsRef.document().delete();
        usersRef.document().delete();

        for (int i = 0; i < 10; i++) {

            User user = new User(
                    "firstname" + i,
                    "phoneNumber" + i,
                    "email" + i + "@gmail.com",
                    "address" + i,
                    "usrId" + i,
                    null        // this just wont exist lol
            );


            usersRef.add(user);



            Event event = new Event(
                    "event" + i,
                    "location" + i,
                    new Date(),
                    "desc" + i,
                    new Date(),
                    "time" + i,
                    i,
                    usersRef.document("usrId" + i),
                    "id" + i
            );

            eventsRef.add(event);

            System.out.println("added user and event " + i);
        }
    }
}
