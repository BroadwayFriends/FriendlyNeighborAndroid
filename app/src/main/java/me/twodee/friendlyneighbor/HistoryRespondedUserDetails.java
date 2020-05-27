package me.twodee.friendlyneighbor;

public class HistoryRespondedUserDetails {
    private String RUName, RUContactNumber, RUProfilePicture, RU_id;
    private boolean RUAccepted;

    public HistoryRespondedUserDetails(String RUName, String RUContactNumber, String RUProfilePicture, String RU_id, boolean RUAccepted) {
        this.RUName = RUName;
        this.RUContactNumber = RUContactNumber;
        this.RUProfilePicture = RUProfilePicture;
        this.RU_id = RU_id;
        this.RUAccepted = false;
    }

    public String getRUName() {
        return RUName;
    }

    public String getRUContactNumber() {
        return RUContactNumber;
    }

    public String getRUProfilePicture() {
        return RUProfilePicture;
    }

    public String getRU_id() {
        return RU_id;
    }

    public boolean getRUAccepted() {
        return RUAccepted;
    }
}
