package fit3077.lab02team14.assignment2.constants;

import fit3077.lab02team14.assignment2.services.StringFormatter;

public enum UserRole {

    CUSTOMER("customer"),
    PATIENT("patient"),
    RECEPTIONIST("receptionist"),
    HEALTHCAREWORKER("healthcareWorker");

    private String userRoles;

    public String getValue() {
        return userRoles;
    }

    public String getFValue(){
        return StringFormatter.formatString(userRoles);
    }

    UserRole(String userRoles){
        this.userRoles = userRoles;
    }
}