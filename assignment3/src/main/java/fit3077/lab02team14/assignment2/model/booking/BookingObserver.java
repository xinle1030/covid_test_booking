package fit3077.lab02team14.assignment2.model.booking;

public abstract class BookingObserver {
    protected BookingSubject bookingSubject;
    public abstract void update();
}
