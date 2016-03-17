package eu.alfred.navigationapp.model;

/**
 * Created by Gary on 25.02.2016.
 */
public class LocationData {

    String town;
    String street;
    String street_nr;

    public LocationData() {

    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet_nr() {
        return street_nr;
    }

    public void setStreet_nr(String street_nr) {
        this.street_nr = street_nr;
    }


}
