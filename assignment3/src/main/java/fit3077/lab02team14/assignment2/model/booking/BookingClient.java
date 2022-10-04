package fit3077.lab02team14.assignment2.model.booking;

import fit3077.lab02team14.assignment2.model.modalInfo.BookingState;

public class BookingClient {
    private BookingOriginator bookingOriginator;
    private BookingAdditionalInfo bookingAdditionalInfo;

    public BookingClient(BookingOriginator bookingOriginator, BookingAdditionalInfo bookingAdditionalInfo) {
        this.bookingOriginator = bookingOriginator;
        this.bookingAdditionalInfo = bookingAdditionalInfo;
    }

    public BookingOriginator getBookingOriginator() {
        return bookingOriginator;
    }

    public void setBookingOriginator(BookingOriginator bookingOriginator) {
        this.bookingOriginator = bookingOriginator;
    }

    public BookingAdditionalInfo getBookingAdditionalInfo() {
        return bookingAdditionalInfo;
    }

    public void setBookingAdditionalInfo(BookingAdditionalInfo bookingAdditionalInfo) {
        this.bookingAdditionalInfo = bookingAdditionalInfo;
    }

    public void saveCheckpoint(String testingSiteId, String startTime){
        bookingAdditionalInfo.addMemento(bookingOriginator.createMemento(testingSiteId, startTime));
    }

    public BookingState restoreCheckpoint(Integer stateVersion){
        BookingState bookingState = bookingAdditionalInfo.getMemento(stateVersion);
        bookingOriginator.setMemento(bookingState);
        return bookingState;
    }
}
