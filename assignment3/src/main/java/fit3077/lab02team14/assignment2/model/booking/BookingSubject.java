package fit3077.lab02team14.assignment2.model.booking;

import java.util.ArrayList;
import java.util.List;

import fit3077.lab02team14.assignment2.constants.BookingAction;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSite;

public class BookingSubject {

    private List<BookingObserver> observers = new ArrayList<BookingObserver>();
    private BookingUpdate bookingUpdate;

    public BookingUpdate getBookingUpdate(){
        return bookingUpdate;
    }

    public void setBookingUpdate(BookingAction bookingAction, Booking updatedBooking) {
        this.bookingUpdate = new BookingUpdate(bookingAction, updatedBooking);
        notifyAllObservers();
     }
  
     public void setBookingUpdate(BookingAction bookingAction, Booking updatedBooking, Booking prevBooking) {
      TestingSite newTestingSite = updatedBooking.getTestingSite();
      TestingSite previousTestingSite = prevBooking.getTestingSite();
      // need to notify more than 1 related testing sites
      if (! (newTestingSite.getId().equals(previousTestingSite.getId()))){
         this.bookingUpdate = new BookingUpdate(bookingAction, updatedBooking, prevBooking);
      }
      else{
         this.bookingUpdate = new BookingUpdate(bookingAction, updatedBooking);
      }
      notifyAllObservers();
   }

     public void attach(BookingObserver observer){
        observers.add(observer);
     }
  
     public void notifyAllObservers(){
        for (BookingObserver observer : observers) {
           observer.update();
        }
     }

}
