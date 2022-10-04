
package fit3077.lab02team14.assignment2.model.booking;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import fit3077.lab02team14.assignment2.constants.BookingStatus;
import fit3077.lab02team14.assignment2.model.test.CovidTest;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSite;
import fit3077.lab02team14.assignment2.model.actor.Customer;
import fit3077.lab02team14.assignment2.model.actor.User;
import fit3077.lab02team14.assignment2.services.NetworkHelper;
import fit3077.lab02team14.assignment2.services.Util;

import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

@Generated("net.hexar.json2pojo")
public class Booking {

    private BookingAdditionalInfo additionalInfo;
    private Date createdAt;
    private Customer customer;
    private String id;
    private String notes;
    private String smsPin;
    private Date startTime;
    private String status;
    private TestingSite testingSite;
    private Date updatedAt;
    private CovidTest[] covidTests = {};
    private HashMap<String, Object> postBooking;

    public Booking(@JsonProperty("additionalInfo") BookingAdditionalInfo additionalInfo, @JsonProperty("createdAt") Date createdAt, @JsonProperty("customer") Customer customer, @JsonProperty("id") String id, @JsonProperty("notes") String notes, @JsonProperty("smsPin") String smsPin, @JsonProperty("startTime") Date startTime, @JsonProperty("status") String status, @JsonProperty("testingSite") TestingSite testingSite, @JsonProperty("updatedAt") Date updatedAt, @JsonProperty("covidTests") CovidTest[] covidTests) {
        this.additionalInfo = additionalInfo;
        this.createdAt = createdAt;
        this.customer = customer;
        this.id = id;
        this.notes = notes;
        this.smsPin = smsPin;
        this.startTime = startTime;
        this.status = status;
        this.testingSite = testingSite;
        this.updatedAt = updatedAt;
        this.covidTests = covidTests;
        this.setStatus(status);
    }

    public Booking(){
        this.id = "";
    }

    public BookingAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(BookingAdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSmsPin() {
        return smsPin;
    }

    public void setSmsPin(String smsPin) {
        this.smsPin = smsPin;
    }

    public Date getStartTime() {
        //3600 * 1000(milliseconds) * 8 hours to convert from MYT (UTC + 8) to UTC Time
        Date newStartTime = startTime;
        newStartTime.setTime(startTime.getTime() - (3600 * 8 * 1000));
        return newStartTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        
        if (Util.checkIfLapsed(startTime) && status.equals(BookingStatus.INITIATED.getValue())){
            this.setPostBooking(BookingStatus.LAPSED);
            HttpResponse<String> response = NetworkHelper.patchRequest("/booking/" + this.getId(), this.getPostBooking(), false);
            this.status = BookingStatus.LAPSED.getValue();
        }
        else if (!Util.checkIfLapsed(startTime) && status.equals(BookingStatus.LAPSED.getValue())){
            this.setPostBooking(BookingStatus.INITIATED);
            HttpResponse<String> response = NetworkHelper.patchRequest("/booking/" + this.getId(), this.getPostBooking(), false);
            this.status = BookingStatus.INITIATED.getValue();
        }
        else{
            this.status = status;
        }
    }

    public TestingSite getTestingSite() {
        return testingSite;
    }

    public void setTestingSite(TestingSite testingSite) {
        this.testingSite = testingSite;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CovidTest[] getCovidTests() {
        return covidTests;
    }

    public void setCovidTests(CovidTest[] covidTests) {
        if (covidTests != null){
            this.covidTests = covidTests;
        }
    }

    @Override
    public String toString() {
        return "Booking{" +
                "additionalInfo=" + additionalInfo +
                ", createdAt=" + createdAt +
                ", customer=" + customer +
                ", id='" + id + '\'' +
                ", notes='" + notes + '\'' +
                ", smsPin='" + smsPin + '\'' +
                ", startTime=" + startTime +
                ", status='" + status + '\'' +
                ", testingSite=" + testingSite +
                ", updatedAt=" + updatedAt +
                ", covidTests=" + Arrays.toString(covidTests) +
                '}';
    }

    public HashMap<String, Object> getPostBooking() {
        return postBooking;
    }

    public void setPostBooking(BookingStatus bookingStatus){
        setStatus(bookingStatus.getValue());
        this.postBooking = new HashMap<String, Object>() {{
            put("status", status);
        }};
    }

    public void setPostBooking(String newStatus, String newStartTime, TestingSite newTestingSite, Boolean parseNeeded) {
        if(parseNeeded){
            this.postBooking = new HashMap<String, Object>(){{
                if(newStatus != null){
                    put("status", newStatus);
                }
                if(newStartTime != null){
                    String isoStartDateTime = Util.parseIsoDateTime(newStartTime);
                    System.out.println("newStartTime: " + newStartTime);
                    System.out.println("isoStartTimeInPostbooking: " + isoStartDateTime);
                    put("startTime", isoStartDateTime);
                }
                if(newTestingSite != null){
                    String testingSiteId = newTestingSite.getId();
                    put("testingSiteId", testingSiteId);
                }
            }};
        }
        else {
            this.postBooking = new HashMap<String, Object>(){{
                if(newStatus != null){
                    put("status", newStatus);
                }
                if(newStartTime != null){
                    //If date is in JAVA Format
                    SimpleDateFormat javaFormat = new SimpleDateFormat(Util.JAVA_DATETIME_FORMAT);
                    SimpleDateFormat isoFormat = new SimpleDateFormat(Util.ISO_8601BASIC_DATE_PATTERN);
                    Date date = new Date();
                    try{
                        date = javaFormat.parse(newStartTime);
                    } catch (ParseException e){
                        e.printStackTrace();
                    }
                    String isoStartDateTime = isoFormat.format(date);
                    put("startTime", isoStartDateTime);
                }
                if(newTestingSite != null){
                    String testingSiteId = newTestingSite.getId();
                    put("testingSiteId", testingSiteId);
                }
            }};
        }
    }

    public boolean isCancelled(){
        return this.getStatus().equals(BookingStatus.CANCELLED.getValue());
    }

    public boolean isValid(){
        return this.getStatus().equals(BookingStatus.INITIATED.getValue());
    }

    public boolean isCompleted(){
        return this.getStatus().equals(BookingStatus.COMPLETED.getValue());
    }

}
