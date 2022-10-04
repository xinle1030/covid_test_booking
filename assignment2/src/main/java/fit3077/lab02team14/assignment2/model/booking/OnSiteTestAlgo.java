package fit3077.lab02team14.assignment2.model.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import fit3077.lab02team14.assignment2.constants.CovidTestMode;
import fit3077.lab02team14.assignment2.model.actor.User;
import fit3077.lab02team14.assignment2.model.modalInfo.CovidTestInfo;
import fit3077.lab02team14.assignment2.model.test.CovidTestAdditionalInfo;
import fit3077.lab02team14.assignment2.services.NetworkHelper;

import java.net.http.HttpResponse;

public class OnSiteTestAlgo implements BookingStrategy{

    @Override
    public boolean makeBooking(Booking booking, CovidTestInfo covidTestInfo, CovidTestAdditionalInfo covidTestAdditionalInfo, String patientId) {
        // TODO Auto-generated method stub

        covidTestInfo.setPostCovidTest(patientId, booking.getId());

        HttpResponse<String> postCovidTestRes = NetworkHelper.postRequest("/covid-test",
                covidTestInfo.getPostCovidTest());
        System.out.println(postCovidTestRes.body() + "\n");

        if (postCovidTestRes.statusCode() == 201){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public Booking[] viewBooking(CovidTestMode covidTestMode, User logInUser) {
        return logInUser.getBookings(covidTestMode);
    }

    @Override
    public Booking searchBooking(String searchStr){
        // TODO Auto-generated method stub
        String smsCode = searchStr;
        HttpResponse<String> response = NetworkHelper.getData("/booking/" + "?fields=covidTests");
        Booking[] bookings = new Booking[0];
        try {
            bookings = new ObjectMapper().readValue(response.body(), Booking[].class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        BookingList bookingList = new BookingList(bookings);
        Booking filteredBooking = bookingList.smsCodeFilter(smsCode);
        return filteredBooking;
    }

}
