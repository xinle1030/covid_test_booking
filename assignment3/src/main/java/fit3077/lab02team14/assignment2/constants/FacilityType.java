package fit3077.lab02team14.assignment2.constants;

public enum FacilityType {
    DRIVETHROUGH("driveThrough"),
    WALKIN("walkIn"),
    CLINIC("clinic"),
    GP("gp"),
    HOSPITAL("hospital"),
    NOPREFERENCE("noPreference");

    private String facilityType;

    public String getValue() {
        return facilityType;
    }

    FacilityType(String facilityType) {
        this.facilityType = facilityType;
    }
}


