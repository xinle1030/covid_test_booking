package fit3077.lab02team14.assignment2.model.booking;

import com.fasterxml.jackson.databind.ObjectMapper;

import fit3077.lab02team14.assignment2.constants.BookingStatus;
import fit3077.lab02team14.assignment2.constants.CovidTestMode;
import fit3077.lab02team14.assignment2.model.actor.User;
import fit3077.lab02team14.assignment2.model.modalInfo.CovidTestInfo;
import fit3077.lab02team14.assignment2.model.test.CovidTestAdditionalInfo;
import fit3077.lab02team14.assignment2.services.NetworkHelper;

import java.net.http.HttpResponse;

public class OnSiteTestAlgo implements BookingStrategy{

    @Override
    public boolean makeBooking(Booking booking, CovidTestInfo covidTestInfo, CovidTestAdditionalInfo covidTestAdditionalInfo, String patientId) {

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

    public Booking[] viewBooking(CovidTestMode covidTestMode, User logInUser, Boolean isActive) {
        Booking[] allBookings = logInUser.getBookings(covidTestMode);
        BookingList bookingList = new BookingList(allBookings);
        Booking[] targetBookings = bookingList.filterByState(isActive);

        return targetBookings;
    }

    @Override
    public Booking searchBooking(String searchStr) {

        HttpResponse<String> response = NetworkHelper.getData("/booking/" + "?fields=covidTests");
        Booking[] bookings = new Booking[0];
        try {
            bookings = new ObjectMapper().readValue(response.body(), Booking[].class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Booking filteredBooking = null;

        //If input string is a sms pin
        if (searchStr.length() == 6) {
            String smsCode = searchStr;
            BookingList bookingList = new BookingList(bookings);
            filteredBooking = bookingList.smsCodeFilter(smsCode);
        }
        //If input string is a booking id
        else if (searchStr.length() == 36) {
            String bookingID = searchStr;
            BookingList bookingList = new BookingList(bookings);
            filteredBooking = bookingList.bookingIdFilter(bookingID);
        }
        return filteredBooking;
    }

    @Override
    public Booking modifyBooking(Booking booking) {
        HttpResponse<String> response = NetworkHelper.patchRequest("/booking/" + booking.getId(), booking.getPostBooking(), false);
        HttpResponse<String> response1 = NetworkHelper.patchRequest("/booking/" + booking.getId(), booking.getAdditionalInfo().getPostBookingAdditionalInfo(), true);
        Booking updatedBooking = null;
        if(response1.statusCode() == 200){
            try {
                updatedBooking = new ObjectMapper().readValue(response1.body(), Booking.class);
                return updatedBooking;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }   
        }
        return updatedBooking;
    }
    @Override
    public Booking revertBooking(Booking booking) {
        HttpResponse<String> response = NetworkHelper.patchRequest("/booking/" + booking.getId(), booking.getPostBooking(), false);
        HttpResponse<String> response1 = NetworkHelper.patchRequest("/booking/" + booking.getId(), booking.getAdditionalInfo().getPostBookingAdditionalInfo(), true);
        Booking updatedBooking = null;
        if(response1.statusCode() == 200){
            try {
                updatedBooking = new ObjectMapper().readValue(response1.body(), Booking.class);
                return updatedBooking;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }   
        }
        return updatedBooking;
    }

    @Override
    public Booking cancelBooking(Booking booking) {
        booking.setPostBooking(BookingStatus.CANCELLED);
        HttpResponse<String> response = NetworkHelper.patchRequest("/booking/" + booking.getId(), booking.getPostBooking(), false);
        Booking updatedBooking = null;
        if(response.statusCode() == 200){
            try {
                updatedBooking = new ObjectMapper().readValue(response.body(), Booking.class);
                return updatedBooking;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }    
        }
        return updatedBooking;
    }

    @Override
    public Booking deleteBooking(Booking booking) {
        if (booking.getCovidTests().length > 0){
            HttpResponse<String> response1 = NetworkHelper.deleteRequest("/covid-test/" + booking.getCovidTests()[0].getId());
        }

        HttpResponse<String> response2 = NetworkHelper.deleteRequest("/booking/" + booking.getId());
        Booking updatedBooking = null;
        if(response2.statusCode() == 204){
            return booking;
        }
        return updatedBooking;
    }
}
