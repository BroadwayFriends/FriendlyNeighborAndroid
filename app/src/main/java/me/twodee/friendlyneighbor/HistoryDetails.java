
package me.twodee.friendlyneighbor;

public class HistoryDetails {
    private String choicePageTitle, choicePageType, choicePageTime, jsonUsersArray, choicePageAcceptedUser, choicePageRequestId;
    private boolean choicePageCompleted;

    public HistoryDetails(String choicePageTitle, String choicePageType, String choicePageTime, String jsonUsersArray, boolean choicePageCompleted, String choicePageAcceptedUser, String choicePageRequestId) {
        this.choicePageTitle = choicePageTitle;
        this.choicePageType = choicePageType;
        this.choicePageTime = choicePageTime;
        this.jsonUsersArray = jsonUsersArray;
        this.choicePageCompleted = choicePageCompleted;
        this.choicePageAcceptedUser = choicePageAcceptedUser;
        this.choicePageRequestId = choicePageRequestId;
    }

    public String getChoicePageTitle() {
        return choicePageTitle;
    }

    public String getChoicePageType() {
        return choicePageType;
    }

    public String getChoicePageTime() {
        return choicePageTime;
    }

    public String getJsonUsersArray() {
        return jsonUsersArray;
    }
    public boolean getChoicePageCompleted() {
        return choicePageCompleted;
    }
    public String getChoicePageAcceptedUser() {
        return choicePageAcceptedUser;
    }
    public String getChoicePageRequestId() {
        return choicePageRequestId;
    }
}
