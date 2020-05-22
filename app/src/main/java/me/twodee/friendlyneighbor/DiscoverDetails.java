package me.twodee.friendlyneighbor;

public class DiscoverDetails {
    private String discoverTitle, discoverType, discoverPerson, discoverTime, jsonResponse;
    private float discoverPrice;

    public DiscoverDetails(String discoverTitle, String discoverType, String discoverPerson, String discoverTime, float discoverPrice, String jsonResponse) {
        this.discoverTitle = discoverTitle;
        this.discoverType = discoverType;
        this.discoverPerson = discoverPerson;
        this.discoverTime = discoverTime;
        this.discoverPrice = discoverPrice;
        this.jsonResponse = jsonResponse;
    }

    public String getDiscoverTitle() {
        return discoverTitle;
    }

    public String getDiscoverType() {
        return discoverType;
    }

    public String getDiscoverPerson() {
        return discoverPerson;
    }

    public String getDiscoverTime() {
        return discoverTime;
    }

    public float getDiscoverPrice() {
        return discoverPrice;
    }

    public String getDiscoverJsonResponse() {
        return jsonResponse;
    }
}
