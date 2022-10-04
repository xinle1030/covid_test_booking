package fit3077.lab02team14.assignment2.model.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import fit3077.lab02team14.assignment2.constants.CovidTestMode;
import fit3077.lab02team14.assignment2.model.actor.User;
import fit3077.lab02team14.assignment2.model.modalInfo.BookingInfo;
import fit3077.lab02team14.assignment2.model.modalInfo.BookingState;
import fit3077.lab02team14.assignment2.model.modalInfo.CovidTestInfo;
import fit3077.lab02team14.assignment2.model.test.CovidTestAdditionalInfo;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSite;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSiteList;
import fit3077.lab02team14.assignment2.services.NetworkHelper;
import fit3077.lab02team14.assignment2.services.Util;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;

public class BookingContext {
    private BookingStrategy bookingStrategy;

    public BookingContext(BookingStrategy bookingStrategy){
        this.bookingStrategy = bookingStrategy;
     }

     public Booking executeBookStrategy(BookingInfo bookingInfo, CovidTestInfo covidTestInfo, CovidTestAdditionalInfo covidTestAdditionalInfo, String patientId){
        HttpResponse<String> postBookingRes = NetworkHelper.postRequest("/booking", bookingInfo.getPostBooking());

        System.out.println(postBookingRes.body() + "\n");
        boolean bookingResult = false;
        Booking booking = null;

        if (postBookingRes.statusCode() == 201){
            try {
                booking = new ObjectMapper().readValue(postBookingRes.body(), Booking.class);
                bookingResult = bookingStrategy.makeBooking(booking, covidTestInfo, covidTestAdditionalInfo, patientId);
                if (bookingResult){
                    return booking;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                bookingResult = false;
            }
        }
        else{
            bookingResult = false;
        }
        return booking;
    }


     public Booking executeSearchStrategy(String searchStr){
        return bookingStrategy.searchBooking(searchStr);
     }

     public Booking[] executeViewStrategy(CovidTestMode covidTestMode, String logInUserId){
        User targetUser;
         if (!logInUserId.isEmpty()) {
             HttpResponse<String> response = NetworkHelper
                     .getData("/user/" + logInUserId + "?fields=bookings.covidTests");
             try {
                 targetUser = new ObjectMapper().readValue(response.body(), User.class);
                 return bookingStrategy.viewBooking(covidTestMode, targetUser);
             } catch (Exception e) {
                 System.out.println(e.getMessage());
             }
         }
        return new Booking[0];
     }

     public Booking[] executeViewStrategy(CovidTestMode covidTestMode, String logInUserId, Boolean isActive){
        User targetUser;
         if (!logInUserId.isEmpty()) {
             HttpResponse<String> response = NetworkHelper
                     .getData("/user/" + logInUserId + "?fields=bookings.covidTests");
             try {
                 targetUser = new ObjectMapper().readValue(response.body(), User.class);
                 return bookingStrategy.viewBooking(covidTestMode, targetUser, isActive);
             } catch (Exception e) {
                 System.out.println(e.getMessage());
             }
         }
        return new Booking[0];
     }

     public Booking executeModifyStrategy(String testingSiteName, String bookingId, String startTime, Boolean parseNeeded){
         System.out.println("startTime In BookingContext: " + startTime);
        //Get desired Testing Site by its name
         HttpResponse<String> response = NetworkHelper.getData("/testing-site");
         TestingSiteList testingSiteList = new TestingSiteList(response);
         TestingSite targetTestingSite = testingSiteList.filterByName(testingSiteName);

         //Get the booking wishes to be modified from the API endpoint
         HttpResponse<String> response1 = NetworkHelper.getData("/booking/" + bookingId + "?fields=covidTests");
         Booking targetBooking = new Booking();
         try {
             targetBooking = new ObjectMapper().readValue(response1.body(), Booking.class);
         } catch (Exception e) {
             System.out.println(e.getMessage());
         }

         //IF there are no changes made
         if(startTime.equals("") && targetTestingSite.equals(targetBooking.getTestingSite())){
             return null;
         }
         else if (startTime.equals("")) {
             startTime = targetBooking.getStartTime().toString();
         }

         //To be able to change the testingSite (with the id) and the start time of the booking
         //Check if the chosen version is lapsed
         Date date = Util.convertToJavaDateTime(startTime);
         boolean result = Util.checkIfLapsed(date);
         System.out.println(result);
         String newStatus = null;
         if(result){
             newStatus = "LAPSED";
         }

         //Setting up memento
         BookingAdditionalInfo bookingAdditionalInfo = targetBooking.getAdditionalInfo();
         BookingOriginator bookingOriginator = new BookingOriginator();
         BookingClient bookingClient = new BookingClient(bookingOriginator, bookingAdditionalInfo);
         //If there are already 3 states that are saved
         ArrayList<BookingState> bookingVersions = bookingClient.getBookingAdditionalInfo().getBookingVersions();
         if(bookingVersions.size() == 3){
             //Pop out first BookingState as it is always the first(earliest) made changes
            bookingVersions.remove(0);
            bookingClient.getBookingAdditionalInfo().setBookingVersions(bookingVersions);
         }
         String isoStartTime = Util.parseIsoDateTime(targetBooking.getStartTime().toString());
         bookingClient.saveCheckpoint(targetBooking.getTestingSite().getId(), isoStartTime);

         //To be able to change the testingSite (with the id) and the start time of the booking
         targetBooking.setPostBooking(newStatus, startTime, targetTestingSite, parseNeeded);

         //Save the booking versions
         targetBooking.getAdditionalInfo().setPostBookingAdditionalInfo();

         //Call modifyBooking on OnSiteTestAlgo
         return bookingStrategy.modifyBooking(targetBooking);
     }

     public Booking executeRevertStrategy(String bookingId, Boolean parseNeeded, Integer versionNumber){
         //Get the booking wishes to be modified from the API endpoint
         HttpResponse<String> response1 = NetworkHelper.getData("/booking/" + bookingId + "?fields=covidTests");
         Booking targetBooking = new Booking();
         try {
             targetBooking = new ObjectMapper().readValue(response1.body(), Booking.class);
         } catch (Exception e) {
             System.out.println(e.getMessage());
         }
         //Setting up memento
         BookingAdditionalInfo bookingAdditionalInfo = targetBooking.getAdditionalInfo();
         BookingOriginator bookingOriginator = new BookingOriginator();
         BookingClient bookingClient = new BookingClient(bookingOriginator, bookingAdditionalInfo);
         //Retrieve desired booking version
         BookingState bookingState = bookingClient.restoreCheckpoint(versionNumber);
         ArrayList<BookingState> bookingVersions = bookingClient.getBookingAdditionalInfo().getBookingVersions();

         //To be able to change the testingSite (with the id) and the start time of the booking
         //Check if the chosen version is lapsed
         Date date = Util.convertToJavaDateTime(bookingState.getStartTime());
         boolean result = Util.checkIfLapsed(date);
         if(result){
             targetBooking.setPostBooking("LAPSED", null, null, parseNeeded);
             targetBooking.getAdditionalInfo().setPostBookingAdditionalInfo();
             bookingStrategy.modifyBooking(targetBooking);
             return null;
         }

         //Save the current state (in this implementation revert is still a changes)
         BookingOriginator bookingOriginator1 = new BookingOriginator();
         BookingState bookingState1 = bookingOriginator1.createMemento(targetBooking.getTestingSite().getId(), targetBooking.getStartTime().toString());
         bookingVersions.remove((int) versionNumber);
         bookingClient.getBookingAdditionalInfo().setBookingVersions(bookingVersions);
         bookingClient.saveCheckpoint(bookingState1.getTestingSiteId(), bookingState1.getStartTime());

//         if(bookingVersions.size() == 3){
//             //Pop out first BookingState as it is always the first(earliest) made changes
//             bookingVersions.remove(0);
//             bookingClient.getBookingAdditionalInfo().setBookingVersions(bookingVersions);
//         }


         //Get desired Testing Site by its id from retrieved booking state of the desired version
         HttpResponse<String> response = NetworkHelper.getData("/testing-site");
         TestingSiteList testingSiteList = new TestingSiteList(response);
         TestingSite targetTestingSite = testingSiteList.filterById(bookingState.getTestingSiteId());

         targetBooking.setPostBooking(null, bookingState.getStartTime(), targetTestingSite, parseNeeded);

         //Save the booking versions
         targetBooking.getAdditionalInfo().setPostBookingAdditionalInfo();

         //Call modifyBooking on OnSiteTestAlgo
         return bookingStrategy.revertBooking(targetBooking);
     }

     public Booking executeCancelStrategy(String bookingId){
        HttpResponse<String> response = NetworkHelper.getData("/booking/" + bookingId);
        Booking targetBooking = new Booking();
        try {
            targetBooking = new ObjectMapper().readValue(response.body(), Booking.class);
            return bookingStrategy.cancelBooking(targetBooking);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return targetBooking;
     }

     public Booking executeDeleteStrategy(String bookingId){
        HttpResponse<String> response = NetworkHelper.getData("/booking/" + bookingId + "?fields=covidTests");
        Booking targetBooking =null;
        try {
            targetBooking = new ObjectMapper().readValue(response.body(), Booking.class);
            return bookingStrategy.deleteBooking(targetBooking);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return targetBooking;
     }
}
