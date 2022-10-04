package fit3077.lab02team14.assignment2.constants;

public enum IsTestKitPickedUp {
    INVALID("-1"),
    YES("1"),
    NO("0");

    private String isTestKitPickedUp;

    public String getValue() {
        return isTestKitPickedUp;
    }

    IsTestKitPickedUp(String isTestKitPickedUp) {
        this.isTestKitPickedUp = isTestKitPickedUp;
    }
}