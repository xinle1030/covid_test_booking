
package fit3077.lab02team14.assignment2.model.actor;

import fit3077.lab02team14.assignment2.constants.UserRole;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSite;
import fit3077.lab02team14.assignment2.services.NetworkHelper;

import java.net.http.HttpResponse;

import javax.annotation.Generated;

import com.fasterxml.jackson.databind.ObjectMapper;

@Generated("net.hexar.json2pojo")
public class UserAdditionalInfo {

    private TestingSite testingSite;
    private String userRole;

    public String getUserRole() {
        return userRole;
    }

    public String getFUserRole() {
        UserRole userRole = UserRole.valueOf(this.getUserRole().toUpperCase());
        return userRole.getFValue();
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public TestingSite getTestingSite() {
        return testingSite;
    }

    public void setTestingSite(String testingSiteId) {
        if (testingSiteId == null || testingSiteId.length() == 0) {
            this.testingSite = null;
        } else {
            HttpResponse<String> response = NetworkHelper.getData("/testing-site/" + testingSiteId);
            TestingSite targetTestingSite = new TestingSite();
            try {
                targetTestingSite = new ObjectMapper().readValue(response.body(), TestingSite.class);
                this.testingSite = targetTestingSite;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean doesTestingSiteMatch(String testingSiteId){
        return this.testingSite.getId().equals(testingSiteId);
    }

    @Override
    public String toString() {
        return "UserAdditionalInfo{" +
                "testingSite=" + testingSite +
                ", userRole='" + userRole + '\'' +
                '}';
    }
}
