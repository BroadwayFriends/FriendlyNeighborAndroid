package me.twodee.friendlyneighbor;

public class DiscoverDetails {
    private String discoverType, discoverPerson, discoverTime;
    private float discoverPrice;

    public DiscoverDetails(String discoverType, String discoverPerson, String discoverTime, float discoverPrice) {
        this.discoverType = discoverType;
        this.discoverPerson = discoverPerson;
        this.discoverTime = discoverTime;
        this.discoverPrice = discoverPrice;
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
