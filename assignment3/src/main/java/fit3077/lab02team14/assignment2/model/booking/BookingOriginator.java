package fit3077.lab02team14.assignment2.model.booking;

import fit3077.lab02team14.assignment2.model.modalInfo.BookingState;

public class BookingOriginator {
    private String testingSiteId;
    private String startTime;

    public String getTestingSiteId() {
        return testingSiteId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setMemento(BookingState bookingState) {
        this.testingSiteId = bookingState.getTestingSiteId();
        this.startTime = bookingState.getStartTime();
    }

    public BookingState createMemento(String testingSiteId, String startTime){
        return new BookingState(testingSiteId, startTime);
    }
}
