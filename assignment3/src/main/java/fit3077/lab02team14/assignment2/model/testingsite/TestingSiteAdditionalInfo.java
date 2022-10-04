
package fit3077.lab02team14.assignment2.model.testingsite;

import java.util.Date;

import javax.annotation.Generated;

import fit3077.lab02team14.assignment2.constants.TestingSiteStatus;
import fit3077.lab02team14.assignment2.services.Util;

@Generated("net.hexar.json2pojo")
public class TestingSiteAdditionalInfo {

    private String facilityType;
    private Date openAt;
    private Date closedAt;
    private int waitingTime = 0;
    private TestingSiteStatus testingSiteStatus;
    private Boolean hasOnSiteTesting;
    private Boolean hasOnSiteBooking;

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public String getOpenAtTime() {
        return Util.timeFormatting(openAt);
    }

    public Date getOpenAt() {
        return openAt;
    }

    public void setOpenAt(Date openAt) {
        this.openAt = openAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public String getClosedAtTime() {
        return Util.timeFormatting(closedAt);
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public String getWaitingTimeStr() {
        if (this.waitingTime == 0) {
            return "-";
        } else {
            return String.format("%s hour(s)", waitingTime);
        }
    }

    public void setWaitingTime(int noOfBookings) {
        this.waitingTime = 1 * noOfBookings;
    }


    public TestingSiteStatus getTestingSiteStatus() {
        return testingSiteStatus;
    }


    public void setTestingSiteStatus() {
        if (Util.nowInZone(this.openAt, this.closedAt)) {
            this.testingSiteStatus = TestingSiteStatus.OPEN;
        } else {
            this.testingSiteStatus = TestingSiteStatus.CLOSED;
        }
    }

    public Boolean getHasOnSiteTesting() {
        return hasOnSiteTesting;
    }

    public void setHasOnSiteTesting(Boolean hasOnSiteTesting) {
        this.hasOnSiteTesting = hasOnSiteTesting;
    }

    public Boolean getHasOnSiteBooking() {
        return hasOnSiteBooking;
    }

    public void setHasOnSiteBooking(Boolean hasOnSiteBooking) {
        this.hasOnSiteBooking = hasOnSiteBooking;
    }

    @Override
    public String toString() {
        return "TestingSiteAdditionalInfo{" +
                "facilityType='" + facilityType + '\'' +
                ", openAt=" + openAt +
                ", closedAt=" + closedAt +
                ", waitingTime=" + waitingTime +
                ", testingSiteStatus=" + testingSiteStatus +
                ", hasOnSiteTesting=" + hasOnSiteTesting +
                ", hasOnSiteBooking=" + hasOnSiteBooking +
                '}';
    }
}