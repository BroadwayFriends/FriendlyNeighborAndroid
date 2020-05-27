package me.twodee.friendlyneighbor;

public class HistoryRespondedUserDetails {
    private String RUName, RUContactNumber, RUProfilePicture;
    private boolean RUAccepted;

    public HistoryRespondedUserDetails(String RUName, String RUContactNumber, String RUProfilePicture, boolean RUAccepted) {
        this.RUName = RUName;
        this.RUContactNumber = RUContactNumber;
        this.RUProfilePicture = RUProfilePicture;
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

    public boolean getRUAccepted() {
        return RUAccepted;
    }
}
