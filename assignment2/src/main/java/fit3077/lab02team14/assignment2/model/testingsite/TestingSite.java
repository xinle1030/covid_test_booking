
package fit3077.lab02team14.assignment2.model.testingsite;

import javax.annotation.Generated;

import fit3077.lab02team14.assignment2.model.booking.Booking;
import fit3077.lab02team14.assignment2.services.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Generated("net.hexar.json2pojo")
public class TestingSite {

    private Booking[] bookings = {};
    private String id;
    private String name;
    private String description;
    private String websiteUrl;
    private String phoneNumber;
    private TestingSiteAddress address;
    private Date createdAt;
    private Date updatedAt;
    private TestingSiteAdditionalInfo additionalInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public TestingSiteAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(TestingSiteAdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public TestingSiteAddress getAddress() {
        return address;
    }

    public void setAddress(TestingSiteAddress address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Booking[] getBookings() {
        return bookings;
    }

    public void setBookings(Booking[] bookings) {
        if (bookings != null){
            this.bookings = bookings;
        }
    }

    public Booking[] getBookingsByDate() {
        ArrayList<Booking> retBookings = new ArrayList<>();

        for (int i = 0; i < bookings.length; i++) {
            Booking booking = bookings[i];
            Date bookingDateTime = booking.getStartTime();
            if (Util.compareDateTime(bookingDateTime, this.getAdditionalInfo().getClosedAt())){
                retBookings.add(booking);
            }
        }
        if (retBookings.size() > 0){
            return retBookings.toArray(new Booking[0]);
        }
        else{
            return new Booking[0];
        }
    }

    @Override
    public String toString() {
        return "TestingSite{" +
                "bookings=" + Arrays.toString(bookings) +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address=" + address +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", additionalInfo=" + additionalInfo +
                '}';
    }
}
