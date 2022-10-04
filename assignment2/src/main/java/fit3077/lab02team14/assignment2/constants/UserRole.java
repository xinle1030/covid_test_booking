package fit3077.lab02team14.assignment2.constants;

public enum UserRole {
    CUSTOMER("customer"),
    PATIENT("patient"),
    RECEPTIONIST("receptionist"),
    HEALTHCAREWORKER("healthcareWorker");

    private String userRoles;

    public String getValue() {
        return userRoles;
    }

    UserRole(String userRoles){
        this.userRoles = userRoles;
    }
}
