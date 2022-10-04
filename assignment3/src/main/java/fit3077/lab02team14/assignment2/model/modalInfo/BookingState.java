package fit3077.lab02team14.assignment2.model.modalInfo;

import fit3077.lab02team14.assignment2.model.testingsite.TestingSite;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSiteList;
import fit3077.lab02team14.assignment2.services.NetworkHelper;
import fit3077.lab02team14.assignment2.services.Util;

import java.net.http.HttpResponse;
import java.util.Date;

public class BookingState {
    private String testingSiteId;
    private String startTime;

    public BookingState(String testingSiteId, String startTime) {
        this.testingSiteId = testingSiteId;
        //Format obtained from web input "2022-05-04T06:33", so needed to be changed to ISO format
        startTime = Util.parseIsoDateTime(startTime);
        this.startTime = startTime;
    }
    public BookingState(){}

    public String getStartTime() {
        return startTime;}

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTestingSiteId() {
        return testingSiteId;
    }

    public void setTestingSiteId(String testingSiteId) {
        this.testingSiteId = testingSiteId;
    }

    public String getTestingSiteName(){
        HttpResponse<String> response = NetworkHelper.getData("/testing-site" + "?fields=bookings");
        TestingSiteList testingSiteList = new TestingSiteList(response);
        TestingSite testingSite = testingSiteList.filterById(testingSiteId);
        return testingSite.getName();
    }
    public String getStartTimeInJava(){
        return Util.convertToJavaStringDateTime(startTime);
    }

    public Date getStartTimeInJavaDateTime(){
        return Util.convertToJavaDateTime(startTime);
    }

}
