package fit3077.lab02team14.assignment2.model.booking;

import fit3077.lab02team14.assignment2.constants.BookingAction;

public class BookingNotiObserver extends BookingObserver{

  private BookingNotification bookingNotification = new BookingNotification();

    public BookingNotiObserver(BookingSubject bookingSubject){
        this.bookingSubject = bookingSubject;
        this.bookingSubject.attach(this);
    }

    @Override
    public void update() {
      BookingUpdate bookingUpdate = bookingSubject.getBookingUpdate();
      BookingAction bookingAction = bookingSubject.getBookingUpdate().getBookingAction();
      bookingNotification.buildNotifications(bookingAction, bookingUpdate);
    }

  public BookingNotification getBookingNotification() {
    return bookingNotification;
  }
}
