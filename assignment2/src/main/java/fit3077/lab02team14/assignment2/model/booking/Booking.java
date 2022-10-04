
package fit3077.lab02team14.assignment2.model.booking;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import fit3077.lab02team14.assignment2.model.test.CovidTest;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSite;
import fit3077.lab02team14.assignment2.model.actor.Customer;
import fit3077.lab02team14.assignment2.model.actor.User;

import java.util.Arrays;
import java.util.Date;

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
    }

    public Booking(){}

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
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
