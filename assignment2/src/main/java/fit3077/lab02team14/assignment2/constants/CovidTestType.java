package fit3077.lab02team14.assignment2.constants;

public enum CovidTestType {
    PCR("PCR"),
    RAT("RAT");

    private String testType;

    public String getValue() {
        return testType;
    }

    CovidTestType(String testType) {
        this.testType = testType;
    }
}


