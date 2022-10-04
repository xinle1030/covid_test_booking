
package fit3077.lab02team14.assignment2.model.test;

import java.util.HashMap;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
public class CovidTestAdditionalInfo {

    private String testMode;
    private String isTestKitPickedUp = "0";
    private String testKitStatus = "0";
    private HashMap<String, Object> postCovidTestAdditionalInfo;
    private String photo;

    public void setPostCovidTestAdditionalInfo(HashMap<String, Object> postCovidTestAdditionalInfo) {
        this.postCovidTestAdditionalInfo = postCovidTestAdditionalInfo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public HashMap<String, Object> getPostCovidTestAdditionalInfo() {
        return postCovidTestAdditionalInfo;
    }

    public void setPostCovidTestAdditionalInfo() {
        this.postCovidTestAdditionalInfo = new HashMap<String, Object>(){{
            put("testMode", testMode);
            put("isTestKitPickedUp", isTestKitPickedUp);
            put("testKitStatus", testKitStatus);
        }};
    }

    public CovidTestAdditionalInfo() {
        this.setTestMode("onsite");
        this.setIsTestKitPickedUp("0");
        this.setTestKitStatus("0");
    }

    public CovidTestAdditionalInfo(String testMode, String isTestKitPickedUp) {
        this.testMode = testMode;
        this.isTestKitPickedUp = isTestKitPickedUp;
    }

    public String getTestMode() {
        return testMode;
    }

    public void setTestMode(String testMode) {
        this.testMode = testMode;
    }

    public String getIsTestKitPickedUp() {
        return isTestKitPickedUp;
    }

    public void setIsTestKitPickedUp(String isTestKitPickedUp) {
        this.isTestKitPickedUp = isTestKitPickedUp;
    }

    public String getTestKitStatus() {
        return testKitStatus;
    }

    public void setTestKitStatus(String testKitStatus) {
        this.testKitStatus = testKitStatus;
        this.setPostCovidTestAdditionalInfo();
    }

    @Override
    public String toString() {
        return "CovidTestAdditionalInfo{" +
                "testMode='" + testMode + '\'' +
                ", isTestKitPickedUp='" + isTestKitPickedUp + '\'' +
                ", testKitStatus='" + testKitStatus + '\'' +
                ", postCovidTestAdditionalInfo=" + postCovidTestAdditionalInfo +
                ", photo='" + photo + '\'' +
                '}';
    }
}
