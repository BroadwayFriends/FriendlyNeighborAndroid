
package me.twodee.friendlyneighbor;

public class RespondToRequestDetails {
    private String choicePageTitle, choicePageType, choicePagePerson, choicePageTime, jsonResponse;
    private float choicePagePrice;

    public RespondToRequestDetails(String choicePageTitle, String choicePageType, String choicePagePerson, String choicePageTime, float choicePagePrice, String jsonResponse) {
        this.choicePageTitle = choicePageTitle;
        this.choicePageType = choicePageType;
        this.choicePagePerson = choicePagePerson;
        this.choicePageTime = choicePageTime;
        this.choicePagePrice = choicePagePrice;
        this.jsonResponse = jsonResponse;
    }

    public String getChoicePageTitle() {
        return choicePageTitle;
    }

    public String getChoicePageType() {
        return choicePageType;
    }

    public String getChoicePagePerson() {
        return choicePagePerson;
    }

    public String getChoicePageTime() {
        return choicePageTime;
    }

    public float getChoicePagePrice() {
        return choicePagePrice;
    }

    public String getChoicePageJsonResponse() {
        return jsonResponse;
    }
}
