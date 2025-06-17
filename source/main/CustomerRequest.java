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

/**
 * Manages customer requests in the game by generating and tracking a single
 * active request.
 * 
 * Supports random request generation and comparison against items given by the
 * player.
 */
public class CustomerRequest {

    private List<SuperObject> requestList;
    private SuperObject currentRequest; // Single request at a time

    /**
     * Constructs a CustomerRequest instance with a predefined list of possible
     * items.
     * Immediately generates the first request upon initialization.
     */
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

    /**
     * Randomly selects a new request from the list of possible items.
     * This updates the {@code currentRequest}.
     */
    public void generateNewRequest() {
        Random rand = new Random();
        currentRequest = requestList.get(rand.nextInt(requestList.size()));
    }

    /**
     * Checks whether the item provided by the player matches the current request.
     *
     * @param playerItem the item currently held or delivered by the player
     * @return true if the item matches the current request; false otherwise
     */
    public boolean checkRequest(SuperObject playerItem) {
        if (playerItem == null) {
            return false;
        }
        return playerItem.getClass() == currentRequest.getClass();
    }

    /**
     * Returns the name of the current customer request.
     *
     * @return the request name if one exists, or "No request" if
     *         {@code currentRequest} is null
     */
    public String getCurrentRequest() {

        if (currentRequest != null) {
            // String name = currentRequest.getClass().getSimpleName();
            // String[] objectName = name.split("_");
            // return objectName[1];
            return currentRequest.name;

        } else {
            return "No request";
        }

    }
}
