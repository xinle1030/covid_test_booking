package fit3077.lab02team14.assignment2.model.booking;

import java.util.ArrayList;

import fit3077.lab02team14.assignment2.constants.CovidTestMode;

public class BookingList {

    private Booking[] bookings = {};

    public BookingList(Booking[] bookings) {
        this.bookings = bookings;
    }

    public BookingList() {
    }

    public Booking[] getBookingList() {
        return bookings;
    }

    public Booking[] filter(CovidTestMode targetCovidTestMode) {
        ArrayList<Booking> retBookings = new ArrayList<>();

        for (int i = 0; i < bookings.length; i++) {
            Booking booking = bookings[i];
            if (booking.getCovidTests().length > 0) {
                String covidTestMode = booking.getCovidTests()[0].getAdditionalInfo().getTestMode();
                if (targetCovidTestMode.getValue().equals(covidTestMode)) {
                    if (targetCovidTestMode == CovidTestMode.HOME) {
                        if (booking.getAdditionalInfo().getqRCode().length() > 0) {
                            retBookings.add(booking);
                        }
                    } else {
                        retBookings.add(booking);
                    }
                }
            }
        }
        if (retBookings.size() > 0) {
            return retBookings.toArray(new Booking[0]);
        } else {
            return new Booking[0];
        }
    }

    public Booking[] filterByState(Boolean isActive) {
        ArrayList<Booking> retBookings = new ArrayList<>();

        for (int i = 0; i < bookings.length; i++) {
            Booking booking = bookings[i];

            if(isActive){
                if (booking.isValid()){
                    retBookings.add(booking);
                }
            }
            else{
                if (!booking.isValid()){
                    retBookings.add(booking);
                }
            }
        }
        if (retBookings.size() > 0) {
            return retBookings.toArray(new Booking[0]);
        } else {
            return new Booking[0];
        }
    }

    public Booking smsCodeFilter(String targetSmsCode) {

        for (Booking booking : bookings) {
            String smsPin = booking.getSmsPin();
            if (targetSmsCode.equals(smsPin)) {
                return booking;
            }
        }
        return null;
    }

    public Booking bookingIdFilter(String targetBookingId) {
        for (Booking booking : bookings) {
            String bookingId = booking.getId();
            if (targetBookingId.equals(bookingId)) {
                return booking;
            }
        }
        return null;
    }
}
