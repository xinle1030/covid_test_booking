package fit3077.lab02team14.assignment2.constants;

public enum BookingStatus {
    INVALID("INVALID"),
    INITIATED("INITIATED"),
    COMPLETED("COMPLETED"),
    LAPSED("LAPSED"),
    CANCELLED("CANCELLED");

    private String bookingStatus;

    public String getValue() {
        return bookingStatus;
    }

    BookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
