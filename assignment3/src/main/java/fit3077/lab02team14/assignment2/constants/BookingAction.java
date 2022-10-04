package fit3077.lab02team14.assignment2.constants;

public enum BookingAction {
    CREATE("CREATE"),
    MODIFY("MODIFY"),
    DELETE("DELETE"),
    CANCEL("CANCEL"),
    PROCESS("PROCESS");

    private String bookingAction;

    public String getValue() {
        return bookingAction;
    }

    BookingAction(String bookingAction) {
        this.bookingAction = bookingAction;
    }
}
