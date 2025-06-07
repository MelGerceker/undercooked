package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import object.OBJ_Cola;
import object.OBJ_donerl;
import object.OBJ_donerlt;
import object.OBJ_donert;
import object.OBJ_donerw;
import object.SuperObject;

public class CustomerRequest {

    private List<SuperObject> requestList;
    private SuperObject currentRequest; // Single request at a time

    public CustomerRequest() {
        // Initialize possible requests
        requestList = new ArrayList<>();

        requestList.add(new OBJ_donerw());
        requestList.add(new OBJ_Cola());
        requestList.add(new OBJ_donerl());
        requestList.add(new OBJ_donert());
        requestList.add(new OBJ_donerlt());

        // Generate the first request when game starts
        generateNewRequest();
    }

    // Randomly pick one request from the list
    public void generateNewRequest() {
        Random rand = new Random();
        currentRequest = requestList.get(rand.nextInt(requestList.size()));
    }

    // Check if the player's item matches the single request
    public boolean checkRequest(SuperObject playerItem) {
        if (playerItem == null)
            return false;
        return playerItem.getClass() == currentRequest.getClass();
    }

    // Get the current request as a string
    public String getCurrentRequest() {

        if (currentRequest != null) {
            String name = currentRequest.getClass().getSimpleName();
            String[] objectName = name.split("_");

            return objectName[1];

            //return currentRequest.getClass().getSimpleName();
        } else {
            return "No request";
        }

        // return currentRequest != null ? currentRequest.getClass().getSimpleName() :
        // "No request";
    }
}
