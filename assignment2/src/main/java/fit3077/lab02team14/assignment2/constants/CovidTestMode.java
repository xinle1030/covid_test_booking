package fit3077.lab02team14.assignment2.constants;

public enum CovidTestMode {
    HOME("home"),
    ONSITE("onsite");

    private String testMode;

    public String getValue() {
        return testMode;
    }

    CovidTestMode(String testMode) {
        this.testMode = testMode;
    }
}
