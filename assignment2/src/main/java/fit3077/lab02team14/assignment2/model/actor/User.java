
package fit3077.lab02team14.assignment2.model.actor;


import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fit3077.lab02team14.assignment2.constants.CovidTestMode;
import fit3077.lab02team14.assignment2.constants.UserRole;
import fit3077.lab02team14.assignment2.model.booking.Booking;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Generated("net.hexar.json2pojo")
@JsonIgnoreProperties({"iss", "jti", "iat", "exp"})
@JsonDeserialize(using = UserDeserializer.class)
public interface User extends Model {

    @Override
    Model addAttribute(String attributeName, Object attributeValue);

    @Override
    Model addAttribute(Object attributeValue);

    @Override
    Model addAllAttributes(Collection<?> attributeValues);

    @Override
    Model addAllAttributes(Map<String, ?> attributes);

    @Override
    Model mergeAttributes(Map<String, ?> attributes);

    @Override
    boolean containsAttribute(String attributeName);

    @Override
    Object getAttribute(String attributeName);

    @Override
    Map<String, Object> asMap();

    public ArrayList<UserRole> getRoles();

    void setRoles(ArrayList<UserRole> roles);

    String getSub();

    void setSub(String sub);

    UserAdditionalInfo getAdditionalInfo();

    void setAdditionalInfo(UserAdditionalInfo userAdditionalInfo);

    String getFamilyName();

    void setFamilyName(String familyName);

    String getGivenName();

    void setGivenName(String givenName);

    String getId();

    void setId(String id);

    Boolean getIsCustomer();

    void setIsCustomer(Boolean isCustomer);

    Boolean getIsHealthcareWorker();

    void setIsHealthcareWorker(Boolean isHealthcareWorker);

    Boolean getIsReceptionist();

    void setIsReceptionist(Boolean isReceptionist);

    String getPhoneNumber();

    void setPhoneNumber(String phoneNumber);

    String getUserName();

    void setUserName(String userName);

    Booking[] getBookings();

    Booking[] getBookings(CovidTestMode covidTestMode);

    void setBookings(Booking[] bookings);

    boolean hasRole(String role);

    boolean isAdmin();
}
