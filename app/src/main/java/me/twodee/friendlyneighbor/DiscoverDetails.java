package me.twodee.friendlyneighbor;

public class DiscoverDetails {
    private String discoverType, discoverPerson, discoverTime;
    private float discoverDistance;

    public DiscoverDetails(String discoverType, String discoverPerson, String discoverTime, float discoverDistance) {
        this.discoverType = discoverType;
        this.discoverPerson = discoverPerson;
        this.discoverTime = discoverTime;
        this.discoverDistance = discoverDistance;
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

    public float getDiscoverDistance() {
        return discoverDistance;
    }
}
