package fit3077.lab02team14.assignment2.model.booking;

import java.util.Date;

import fit3077.lab02team14.assignment2.constants.BookingAction;

public class BookingNotiTemplate {

    static String createTitleTemplate = "New Booking Creation: %s";
    static String modifyTitleTemplate = "Booking Modification: %s";
    static String deleteTitleTemplate = "Booking Deletion: %s";
    static String processTitleTemplate = "Booking Processing: %s";
    static String cancelTitleTemplate = "Booking Cancellation: %s";

    static String createMsgTemplate = "A new booking is created by a customer, %s, at testing site, %s.";
    static String modifyMsgTemplate = "A booking for customer, %s, is modified with the new following details: Testing Site: %s, Start DateTime: %s.";
    static String deleteMsgTemplate = "A booking for customer, %s, is deleted from the testing site, %s.";
    static String processMsgTemplate = "A booking is processed by an administerer, %s, by conducting an interview with the patient, %s, at testing site, %s, with the Final Test Type: %s.";
    static String cancelMsgTemplate = "A booking for customer, %s, at testing site, %s, is cancelled.";

    public static String[] setNotiTemplate(BookingAction bookingAction, Booking booking){
        String title = "";
        String msg = "";
        String bookingId = booking.getId();
        String customerName = booking.getCustomer().getGivenName() + " " + booking.getCustomer().getFamilyName();
        String testingSiteName = booking.getTestingSite().getName();
        switch(bookingAction) {
            case CREATE:
              title = String.format(createTitleTemplate, bookingId);
              msg = String.format(createMsgTemplate, customerName, testingSiteName);
              break;
            case MODIFY:
              Date startTime = booking.getStartTime();
              title = String.format(modifyTitleTemplate, bookingId);
              msg = String.format(modifyMsgTemplate, customerName, testingSiteName, startTime);
              break;
            case CANCEL:
              title = String.format(cancelTitleTemplate, bookingId);
              msg = String.format(cancelMsgTemplate, customerName, testingSiteName);
              break;
            case PROCESS:
              String administererName = booking.getCovidTests()[0].getAdministerer().getGivenName() + ' ' + booking.getCovidTests()[0].getAdministerer().getFamilyName();
              String patientName = booking.getCovidTests()[0].getPatient().getGivenName() + ' ' + booking.getCovidTests()[0].getPatient().getFamilyName();
              String testType = booking.getCovidTests()[0].getType();
              title = String.format(processTitleTemplate, bookingId);
              msg = String.format(processMsgTemplate, administererName, patientName, testingSiteName, testType);
              break;
            case DELETE:
              title = String.format(deleteTitleTemplate, bookingId);
              msg = String.format(deleteMsgTemplate, customerName, testingSiteName);
              break;
        }
        String[] retStr = {title, msg};
        return retStr;
    }
}
