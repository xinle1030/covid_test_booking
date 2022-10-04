package fit3077.lab02team14.assignment2.model.booking;

import fit3077.lab02team14.assignment2.constants.CovidTestMode;
import fit3077.lab02team14.assignment2.model.actor.User;
import fit3077.lab02team14.assignment2.model.modalInfo.CovidTestInfo;
import fit3077.lab02team14.assignment2.model.test.CovidTestAdditionalInfo;

public interface BookingStrategy {
    public boolean makeBooking(Booking booking, CovidTestInfo covidTestInfo, CovidTestAdditionalInfo covidTestAdditionalInfo, String patientId);
    public Booking[] viewBooking(CovidTestMode covidTestMode, User logInUser);
    public Booking searchBooking(String searchStr);
}
