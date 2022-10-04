package fit3077.lab02team14.assignment2.model.booking;

import fit3077.lab02team14.assignment2.constants.BookingAction;

public class BookingUpdate {
    private BookingAction bookingAction;
    private Booking updatedBooking;
    private Booking prevBooking;

    public BookingUpdate(BookingAction bookingAction, Booking updatedBooking){
        this.bookingAction = bookingAction;
        this.updatedBooking = updatedBooking;
    }

    public BookingUpdate(BookingAction bookingAction, Booking updatedBooking, Booking prevBooking){
        this.bookingAction = bookingAction;
        this.updatedBooking = updatedBooking;
        this.prevBooking = prevBooking;
    }
    
    public BookingAction getBookingAction() {
        return bookingAction;
    }

    public void setBookingAction(BookingAction bookingAction) {
        this.bookingAction = bookingAction;
    }

    public Booking getUpdatedBooking() {
        return updatedBooking;
    }

    public void setUpdatedBooking(Booking updatedBooking) {
        this.updatedBooking = updatedBooking;
    }

    public Booking getPrevBooking() {
        return prevBooking;
    }

    public void setPrevBooking(Booking prevBooking) {
        this.prevBooking = prevBooking;
    }

    @Override
    public String toString() {
        return "BookingUpdate{" +
                "bookingAction=" + bookingAction +
                ", updatedBooking=" + updatedBooking +
                ", prevBooking=" + prevBooking +
                '}';
    }
}
