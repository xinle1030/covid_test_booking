
package fit3077.lab02team14.assignment2.model.test;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fit3077.lab02team14.assignment2.model.actor.Patient;
import fit3077.lab02team14.assignment2.model.actor.User;

import java.util.Date;
import java.util.HashMap;

@Generated("net.hexar.json2pojo")
@JsonIgnoreProperties({ "booking"})
public class CovidTest {
    private CovidTestAdditionalInfo additionalInfo;
    private User administerer;
    private Date createdAt;
    private Date dateOfResults;
    private Date datePerformed;
    private String id;
    private String notes;
    private Patient patient;
    private String result;
    private String status;
    private String type;
    private Date updatedAt;
    private HashMap<String, Object> postCovidTest;
    private TestModeState testModeState;

    public CovidTest(){}

    public CovidTest(@JsonProperty("additionalInfo") CovidTestAdditionalInfo additionalInfo, @JsonProperty("administerer") User administerer, @JsonProperty("createdAt") Date createdAt, @JsonProperty("dateOfResults") Date dateOfResults, @JsonProperty("datePerformed") Date datePerformed, @JsonProperty("id") String id, @JsonProperty("notes") String notes, @JsonProperty("patient") Patient patient, @JsonProperty("result") String result, @JsonProperty("status") String status, @JsonProperty("type") String type, @JsonProperty("updatedAt") Date updatedAt) {
        this.additionalInfo = additionalInfo;
        this.administerer = administerer;
        this.createdAt = createdAt;
        this.dateOfResults = dateOfResults;
        this.datePerformed = datePerformed;
        this.id = id;
        this.notes = notes;
        this.patient = patient;
        this.result = result;
        this.status = status;
        this.type = type;
        this.updatedAt = updatedAt;
    }

    public CovidTestAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(CovidTestAdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public User getAdministerer() {
        return administerer;
    }

    public void setAdministerer(User administerer) {
        this.administerer = administerer;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDateOfResults() {
        return dateOfResults;
    }

    public void setDateOfResults(Date dateOfResults) {
        this.dateOfResults = dateOfResults;
    }

    public Date getDatePerformed() {
        return datePerformed;
    }

    public void setDatePerformed(Date datePerformed) {
        this.datePerformed = datePerformed;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "CovidTest{" +
                ", additionalInfo=" + additionalInfo +
                ", administerer=" + administerer +
                ", createdAt=" + createdAt +
                ", dateOfResults=" + dateOfResults +
                ", datePerformed=" + datePerformed +
                ", id='" + id + '\'' +
                ", notes='" + notes + '\'' +
                ", patient=" + patient +
                ", result='" + result + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public HashMap<String, Object> getPostCovidTest() {
        return postCovidTest;
    }

    public void setPostCovidTest(String type, User administerer) {
        setType(type);
        setAdministerer(administerer);
        this.postCovidTest = new HashMap<String, Object>(){{
            put("type", type);
            put("administererId", administerer.getId());
        }};
    }

    public TestModeState getTestModeState() {
        return testModeState;
    }

    public void setTestModeState(TestModeState testModeState) {
        this.testModeState = testModeState;
    }
}
