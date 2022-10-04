package fit3077.lab02team14.assignment2.model.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.EncodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import fit3077.lab02team14.assignment2.constants.CovidTestMode;
import fit3077.lab02team14.assignment2.constants.UserRole;
import fit3077.lab02team14.assignment2.model.actor.User;
import fit3077.lab02team14.assignment2.model.actor.UserList;
import fit3077.lab02team14.assignment2.model.modalInfo.CovidTestInfo;
import fit3077.lab02team14.assignment2.model.test.CovidTestAdditionalInfo;
import fit3077.lab02team14.assignment2.services.NetworkHelper;
import fit3077.lab02team14.assignment2.services.QRCode;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class HomeTestAlgo implements BookingStrategy{

    @Override
    public boolean makeBooking(Booking booking, CovidTestInfo covidTestInfo, CovidTestAdditionalInfo covidTestAdditionalInfo, String patientId) {
        // TODO Auto-generated method stub

        patchHomeTestBooking(booking);

        HttpResponse<String> getUserRes = NetworkHelper.getData("/user");
        UserList userList = new UserList(getUserRes);

        User[] administerers = userList.findUsersbyRoles(UserRole.HEALTHCAREWORKER);
        covidTestInfo.assignAdministerer(administerers);

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

    public void patchHomeTestBooking(Booking booking) {
        System.out.println("here1");
        BookingAdditionalInfo bookingAdditionalInfo = new BookingAdditionalInfo(
                booking.getId());
        booking.setAdditionalInfo(bookingAdditionalInfo);
        System.out.println(booking.toString());
        HttpResponse<String> patchBookingRes = NetworkHelper.patchRequest("/booking/" + booking.getId(),
                bookingAdditionalInfo.getPostBookingAdditionalInfo());
        System.out.println(patchBookingRes.body() + "\n");
        System.out.println("here2");
    }

    @Override
    public Booking[] viewBooking(CovidTestMode covidTestMode, User logInUser) {
        // TODO Auto-generated method stub
        Booking[] bookings = logInUser.getBookings(covidTestMode);
        for (int i = 0; i < bookings.length; i++) {
            String bookingId = bookings[i].getId();
            String encodedQRCode = bookings[i].getAdditionalInfo().getqRCode();
            String imgPath = QRCode.byteArrToImgPath(encodedQRCode, bookingId);
            bookings[i].getAdditionalInfo().setImgPath(imgPath);
        }
        return bookings;
    }

    @Override
    public Booking searchBooking(String searchStr){
        // TODO Auto-generated method stub

        String fileName = searchStr;

        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        String decodedText = null;
        try {
            decodedText = QRCode.readQRCode("./src/main/resources/static/image/qrCodes/" + fileName, "UTF-8",
                    hintMap);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(decodedText);
        String id = decodedText;

        HttpResponse<String> response = NetworkHelper.getData("/booking/" + id + "?fields=covidTests");
        Booking targetBooking = new Booking();
        try {
            targetBooking = new ObjectMapper().readValue(response.body(), Booking.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return targetBooking;
    }

}
