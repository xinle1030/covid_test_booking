package fit3077.lab02team14.assignment2.model.modalInfo;

import fit3077.lab02team14.assignment2.model.test.CovidTestAdditionalInfo;
import fit3077.lab02team14.assignment2.model.actor.User;

import java.util.HashMap;
import java.util.Random;

public class CovidTestInfo {

    private String type;
    private String patientId;
    private String administererId;
    private String bookingId = "";
    private String result = "PENDING";
    private String status = "CREATED";
    private String notes;
    private CovidTestAdditionalInfo covidTestAdditionalInfo;
    private HashMap<String, Object> postCovidTest;

    public CovidTestInfo() {
        covidTestAdditionalInfo = new CovidTestAdditionalInfo();
    }

    public CovidTestInfo(String type, String patientId, String administererId, String bookingId, String result,
            CovidTestAdditionalInfo covidTestAdditionalInfo) {
        this.type = type;
        this.patientId = patientId;
        this.administererId = administererId;
        this.bookingId = bookingId;
        this.result = result;
        this.covidTestAdditionalInfo = covidTestAdditionalInfo;
    }

    public HashMap<String, Object> getPostCovidTest() {
        return postCovidTest;
    }

    public void setPostCovidTest(String patientId, String bookingId) {
        setPatientId(patientId);
        setBookingId(bookingId);
        covidTestAdditionalInfo.setPostCovidTestAdditionalInfo();
        this.postCovidTest = new HashMap<String, Object>() {
            {
                put("type", type);
                put("patientId", patientId);
                put("administererId", administererId);
                put("bookingId", bookingId);
                put("result", result);
                put("status", status);
                put("notes", notes);
                put("additionalInfo", covidTestAdditionalInfo.getPostCovidTestAdditionalInfo());
            }
        };
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAdministererId() {
        return administererId;
    }

    public void setAdministererId(String administererId) {
        this.administererId = administererId;
    }

    public void assignAdministerer(User[] administerers) {
        if (administerers.length > 0){
            Random generator = new Random();
            int randomIndex = generator.nextInt(administerers.length);
            String administererId = administerers[randomIndex].getId();
            setAdministererId(administererId);
        }
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public CovidTestAdditionalInfo getCovidTestAdditionalInfo() {
        return covidTestAdditionalInfo;
    }

    public void setCovidTestAdditionalInfo(CovidTestAdditionalInfo covidTestAdditionalInfo) {
        this.covidTestAdditionalInfo = covidTestAdditionalInfo;
    }

    @Override
    public String toString() {
        return "CovidTestInfo{" +
                "type='" + type + '\'' +
                ", patientId='" + patientId + '\'' +
                ", administererId='" + administererId + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", result='" + result + '\'' +
                ", covidTestAdditionalInfo=" + covidTestAdditionalInfo +
                '}';
    }
}
