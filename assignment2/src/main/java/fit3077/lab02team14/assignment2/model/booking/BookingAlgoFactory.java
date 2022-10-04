package fit3077.lab02team14.assignment2.model.booking;

import fit3077.lab02team14.assignment2.constants.CovidTestMode;

public class BookingAlgoFactory {
    public BookingStrategy getBookingAlgo(CovidTestMode covidTestMode) {

        if (covidTestMode.equals(CovidTestMode.HOME)) {
            return new HomeTestAlgo();

        } else if (covidTestMode.equals(CovidTestMode.ONSITE)) {
            return new OnSiteTestAlgo();
        }
        return null;
    }
}
