package fit3077.lab02team14.assignment2.model.booking;
import java.util.ArrayList;
import java.util.List;

import fit3077.lab02team14.assignment2.constants.BookingAction;

public class BookingNotification {
    private List<BookingUpdate> bookingChanges = new ArrayList<BookingUpdate>();
    private List<String> titles = new ArrayList<String>();
    private List<String> messages = new ArrayList<String>();

    public void buildNotifications(BookingAction bookingAction, BookingUpdate bookingUpdate){
        bookingChanges.add(0, bookingUpdate);
        Booking booking = bookingUpdate.getUpdatedBooking();
        String[] returnStr = BookingNotiTemplate.setNotiTemplate(bookingAction, booking);
        String title = returnStr[0];
        String msg = returnStr[1];
        if (title.length() > 0 && msg.length() > 0){
            titles.add(0, title);
            messages.add(0, msg);
        }
    }

    public List<BookingUpdate> getBookingChanges() {
        return bookingChanges;
    }

    public void setBookingChanges(List<BookingUpdate> bookingChanges) {
        this.bookingChanges = bookingChanges;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

}
