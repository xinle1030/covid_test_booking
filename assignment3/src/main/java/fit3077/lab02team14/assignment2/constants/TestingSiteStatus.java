package fit3077.lab02team14.assignment2.constants;

public enum TestingSiteStatus {
    OPEN("open"),
    CLOSED("closed");

    private String testingSiteStatus;

    public String getValue() {
        return testingSiteStatus;
    }

    TestingSiteStatus(String testingSiteStatus) {
        this.testingSiteStatus = testingSiteStatus;
    }
}