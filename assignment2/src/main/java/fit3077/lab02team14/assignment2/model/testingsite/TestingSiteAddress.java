
package fit3077.lab02team14.assignment2.model.testingsite;
import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
public class TestingSiteAddress {

    private AddressAdditionalInfo additionalInfo;
    private Long latitude;
    private Long longitude;
    private String postcode;
    private String state;
    private String street;
    private String street2;
    private String suburb;
    private String unitNumber;

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public AddressAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(AddressAdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String toString() {
        return (unitNumber == null ? "" : (unitNumber + ", ")) + (street == null ? "" : (street + ", ")) + (street2 == null ? "" : (street2 + ", ")) + (suburb == null ? "" : (suburb));
    }
}
