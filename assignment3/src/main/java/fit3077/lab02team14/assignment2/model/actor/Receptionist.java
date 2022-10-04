package fit3077.lab02team14.assignment2.model.actor;

import fit3077.lab02team14.assignment2.constants.CovidTestMode;
import fit3077.lab02team14.assignment2.constants.UserRole;
import fit3077.lab02team14.assignment2.model.booking.Booking;
import fit3077.lab02team14.assignment2.model.booking.BookingList;

import org.springframework.ui.Model;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Generated("net.hexar.json2pojo")
@JsonIgnoreProperties({"iss", "jti", "iat", "exp"})
@JsonDeserialize
public class Receptionist implements User {
    private UserAdditionalInfo userAdditionalInfo;
    private String familyName;
    private String givenName;
    private String id;
    private Boolean isCustomer;
    private Boolean isHealthcareWorker;
    private Boolean isReceptionist;
    private String phoneNumber;
    private String userName;
    private String sub;
    private Booking[] bookings = {};
    private ArrayList<UserRole> roles;

    public Receptionist(UserAdditionalInfo userAdditionalInfo, String familyName, String givenName, String id, Boolean isCustomer, Boolean isHealthcareWorker, Boolean isReceptionist, String phoneNumber,String userName, String sub) {
        this.userAdditionalInfo = userAdditionalInfo;
        this.familyName = familyName;
        this.givenName = givenName;
        this.id = id;
        this.isCustomer = isCustomer;
        this.isHealthcareWorker = isHealthcareWorker;
        this.isReceptionist = isReceptionist;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.sub = sub;
        this.roles = new ArrayList<>();
        if(isCustomer){
            this.roles.add(UserRole.CUSTOMER);
        }
        if(isHealthcareWorker){
            this.roles.add(UserRole.HEALTHCAREWORKER);
        }
        if(isReceptionist){
            this.roles.add(UserRole.RECEPTIONIST);
        }
    }

    public Receptionist(UserAdditionalInfo userAdditionalInfo, String familyName, String givenName, String id, Boolean isCustomer, Boolean isHealthcareWorker, Boolean isReceptionist, String phoneNumber,String userName) {
        this.userAdditionalInfo = userAdditionalInfo;
        this.familyName = familyName;
        this.givenName = givenName;
        this.id = id;
        this.isCustomer = isCustomer;
        this.isHealthcareWorker = isHealthcareWorker;
        this.isReceptionist = isReceptionist;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.roles = new ArrayList<>();
    }

    public Receptionist() {
        this.roles = new ArrayList<>();
    }

    public ArrayList<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<UserRole> roles) {
        this.roles = roles;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
        this.id = sub;
    }

    public UserAdditionalInfo getAdditionalInfo() {
        return userAdditionalInfo;
    }

    public void setAdditionalInfo(UserAdditionalInfo userAdditionalInfo) {
        this.userAdditionalInfo = userAdditionalInfo;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsCustomer() {
        return isCustomer;
    }

    public void setIsCustomer(Boolean isCustomer) {
        if(isCustomer){
            this.roles.add(UserRole.CUSTOMER);
        }
        this.isCustomer = isCustomer;
    }

    public Boolean getIsHealthcareWorker() {
        return isHealthcareWorker;
    }

    public void setIsHealthcareWorker(Boolean isHealthcareWorker) {
        if(isHealthcareWorker){
            this.roles.add(UserRole.HEALTHCAREWORKER);
        }
        this.isHealthcareWorker = isHealthcareWorker;
    }

    public Boolean getIsReceptionist() {
        return isReceptionist;
    }

    public void setIsReceptionist(Boolean isReceptionist) {
        if(isReceptionist){
            this.roles.add(UserRole.RECEPTIONIST);
        }
        this.isReceptionist = isReceptionist;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Model addAttribute(String attributeName, Object attributeValue) {
        return null;
    }

    @Override
    public Model addAttribute(Object attributeValue) {
        return null;
    }

    @Override
    public Model addAllAttributes(Collection<?> attributeValues) {
        return null;
    }

    @Override
    public Model addAllAttributes(Map<String, ?> attributes) {
        return null;
    }

    @Override
    public Model mergeAttributes(Map<String, ?> attributes) {
        return null;
    }

    @Override
    public boolean containsAttribute(String attributeName) {
        return false;
    }

    @Override
    public Object getAttribute(String attributeName) {
        return null;
    }

    @Override
    public Map<String, Object> asMap() {
        return null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Booking[] getBookings() {
        return bookings;
    }

    public void setBookings(Booking[] bookings) {
        if (bookings != null){
            this.bookings = bookings;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userAdditionalInfo=" + userAdditionalInfo +
                ", familyName='" + familyName + '\'' +
                ", givenName='" + givenName + '\'' +
                ", id='" + id + '\'' +
                ", isCustomer=" + isCustomer +
                ", isHealthcareWorker=" + isHealthcareWorker +
                ", isReceptionist=" + isReceptionist +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    public boolean hasRole(String role){
        for (UserRole r : roles){
            if (role.equals(r.getValue())){
                return true;
            }
        }
        return false;
    }

    public boolean isAdmin(){
        if(isReceptionist){
            return true;
        }
        else if(isHealthcareWorker){
            return true;
        }
        return false;
    }

    @Override
    public Booking[] getBookings(CovidTestMode covidTestMode) {
        BookingList bookingList = new BookingList(this.getBookings());
        return bookingList.filter(covidTestMode);
    }
}