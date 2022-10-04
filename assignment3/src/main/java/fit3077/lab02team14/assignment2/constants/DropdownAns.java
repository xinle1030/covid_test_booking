package fit3077.lab02team14.assignment2.constants;

public enum DropdownAns {
    INVALID("-1"),
    YES("1"),
    NO("0");

    private String dropdownAns;

    public String getValue() {
        return dropdownAns;
    }

    DropdownAns(String dropdownAns) {
        this.dropdownAns = dropdownAns;
    }
}
