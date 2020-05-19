package me.twodee.friendlyneighbor;

public class DiscoverDetails {
    private String discoverTitle, discoverType, discoverPerson, discoverTime;
    private float discoverPrice;

    public DiscoverDetails(String discoverTitle, String discoverType, String discoverPerson, String discoverTime, float discoverPrice) {
        this.discoverTitle = discoverTitle;
        this.discoverType = discoverType;
        this.discoverPerson = discoverPerson;
        this.discoverTime = discoverTime;
        this.discoverPrice = discoverPrice;
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
}
