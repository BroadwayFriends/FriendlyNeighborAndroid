
package me.twodee.friendlyneighbor;

public class HistoryDetails {
    private String choicePageTitle, choicePageType, jsonUsersArray;

    public HistoryDetails(String choicePageTitle, String choicePageType, String jsonUsersArray) {
        this.choicePageTitle = choicePageTitle;
        this.choicePageType = choicePageType;
        this.jsonUsersArray = jsonUsersArray;
    }

    public String getChoicePageTitle() {
        return choicePageTitle;
    }

    public String getChoicePageType() {
        return choicePageType;
    }

    public String getJsonUsersArray() {
        return jsonUsersArray;
    }
}
