package fit3077.lab02team14.assignment2.model.modalInfo;

import fit3077.lab02team14.assignment2.model.booking.BookingAdditionalInfo;
import fit3077.lab02team14.assignment2.services.Util;

import java.util.HashMap;

public class BookingInfo {

    private String username;
    private String customerId;
    private String testingSiteId;
    private String startTime;
    private String notes;
    private BookingAdditionalInfo bookingAdditionalInfo;
    private String givenName;
    private String familyName;
    private String phoneNumber;
    private HashMap<String, Object> postBooking;

    public BookingInfo(){
        bookingAdditionalInfo = new BookingAdditionalInfo();
    }

    public BookingInfo(String username, String customerId, String testingSiteId, String startTime, String notes, BookingAdditionalInfo bookingAdditionalInfo, String givenName, String familyName, String phoneNumber) {
        this.username = username;
        this.customerId = customerId;
        this.testingSiteId = testingSiteId;
        this.startTime = startTime;
        this.notes = notes;
        this.bookingAdditionalInfo = bookingAdditionalInfo;
        this.givenName = givenName;
        this.familyName = familyName;
        this.phoneNumber = phoneNumber;
    }

    public HashMap<String, Object> getPostBooking() {
        return postBooking;
    }

    public void setPostBooking(String customerId, String testingSiteId) {
        setCustomerId(customerId);
        setTestingSiteId(testingSiteId);
        bookingAdditionalInfo.setPostBookingAdditionalInfo();
        this.postBooking = new HashMap<String, Object>() {{
            put("customerId", customerId);
            put("testingSiteId", testingSiteId);
            put("startTime", Util.parseIsoDateTime(startTime));
            put("notes", notes);
            put("additionalInfo", bookingAdditionalInfo.getPostBookingAdditionalInfo());
        }};
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTestingSiteId() {
        return testingSiteId;
    }

    public void setTestingSiteId(String testingSiteId) {
        this.testingSiteId = testingSiteId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BookingAdditionalInfo getBookingAdditionalInfo() {
        return bookingAdditionalInfo;
    }

    public void setBookingAdditionalInfo(BookingAdditionalInfo bookingAdditionalInfo) {
        this.bookingAdditionalInfo = bookingAdditionalInfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "BookingInfo{" +
                "username='" + username + '\'' +
                ", customerId='" + customerId + '\'' +
                ", testingSiteId='" + testingSiteId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", notes='" + notes + '\'' +
                ", bookingAdditionalInfo=" + bookingAdditionalInfo +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
