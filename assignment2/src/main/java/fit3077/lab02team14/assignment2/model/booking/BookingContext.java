package fit3077.lab02team14.assignment2.model.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import fit3077.lab02team14.assignment2.constants.CovidTestMode;
import fit3077.lab02team14.assignment2.constants.UserRole;
import fit3077.lab02team14.assignment2.model.actor.User;
import fit3077.lab02team14.assignment2.model.modalInfo.BookingInfo;
import fit3077.lab02team14.assignment2.model.modalInfo.CovidTestInfo;
import fit3077.lab02team14.assignment2.model.test.CovidTestAdditionalInfo;
import fit3077.lab02team14.assignment2.services.NetworkHelper;

import java.net.http.HttpResponse;

public class BookingContext {
    private BookingStrategy bookingStrategy;

    public BookingContext(BookingStrategy bookingStrategy){
        this.bookingStrategy = bookingStrategy;
     }
  
     public boolean executeBookStrategy(BookingInfo bookingInfo, CovidTestInfo covidTestInfo, CovidTestAdditionalInfo covidTestAdditionalInfo, String patientId){
         HttpResponse<String> postBookingRes = NetworkHelper.postRequest("/booking", bookingInfo.getPostBooking());

         System.out.println(postBookingRes.body() + "\n");
         boolean bookingResult = false;

         if (postBookingRes.statusCode() == 201){
             Booking booking = new Booking();
             try {
                 booking = new ObjectMapper().readValue(postBookingRes.body(), Booking.class);
                 bookingResult = bookingStrategy.makeBooking(booking, covidTestInfo, covidTestAdditionalInfo, patientId);
             } catch (Exception e) {
                 System.out.println(e.getMessage());
                 bookingResult = false;
             }
         }
         else{
             bookingResult = false;
         }
         System.out.println(bookingResult);
         return bookingResult;
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
}
