package me.twodee.friendlyneighbor;

public class OnGoingDetails {
    private String ongoingPerson, ongoingPhoneNumber, ongoingItem, ongoingTime, ongoingProfilePicture, ongoing_id;

    public OnGoingDetails(String ongoingPerson, String ongoingPhoneNumber, String ongoingItem, String ongoingTime, String ongoingProfilePicture, String ongoing_id) {
        this.ongoingPerson = ongoingPerson;
        this.ongoingItem = ongoingItem;
        this.ongoingTime = ongoingTime;
        this.ongoingProfilePicture = ongoingProfilePicture;
        this.ongoingPhoneNumber = ongoingPhoneNumber;
        this.ongoing_id = ongoing_id;
    }

    public String getOngoingPerson() {
        return ongoingPerson;
    }

    public String getOngoingPhoneNumber() {
        return ongoingPhoneNumber;
    }

    public String getOngoingItem() {
        return ongoingItem;
    }

    public String getOngoingTime() {
        return ongoingTime;
    }

    public String getOngoingProfilePicture() {
        return ongoingProfilePicture;
    }

    public String getOngoing_id() {
        return ongoing_id;
    }
}
