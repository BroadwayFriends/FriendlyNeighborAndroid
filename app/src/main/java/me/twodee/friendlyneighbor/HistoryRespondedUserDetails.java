package me.twodee.friendlyneighbor;

public class HistoryRespondedUserDetails {
    private String RUName, RUContactNumber, RUProfilePicture;

    public HistoryRespondedUserDetails(String RUName, String RUContactNumber, String RUProfilePicture) {
        this.RUName = RUName;
        this.RUContactNumber = RUContactNumber;
        this.RUProfilePicture = RUProfilePicture;
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
}
