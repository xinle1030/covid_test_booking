package fit3077.lab02team14.assignment2.controllers;

import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import fit3077.lab02team14.assignment2.constants.BookingAction;
import fit3077.lab02team14.assignment2.constants.BookingStatus;
import fit3077.lab02team14.assignment2.constants.CovidTestMode;
import fit3077.lab02team14.assignment2.model.actor.User;
import fit3077.lab02team14.assignment2.model.actor.UserList;
import fit3077.lab02team14.assignment2.model.booking.Booking;
import fit3077.lab02team14.assignment2.model.booking.BookingAlgoFactory;
import fit3077.lab02team14.assignment2.model.booking.BookingContext;
import fit3077.lab02team14.assignment2.model.booking.BookingNotiObserver;
import fit3077.lab02team14.assignment2.model.booking.BookingStrategy;
import fit3077.lab02team14.assignment2.model.booking.BookingSubject;
import fit3077.lab02team14.assignment2.model.test.CovidTestAdditionalInfo;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSite;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSiteList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fit3077.lab02team14.assignment2.model.modalInfo.BookingInfo;
import fit3077.lab02team14.assignment2.model.modalInfo.CovidTestInfo;
import fit3077.lab02team14.assignment2.model.modalInfo.UserInfo;
import fit3077.lab02team14.assignment2.services.NetworkHelper;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class BookingController {

    static BookingSubject bookingSubject = new BookingSubject();
    static BookingNotiObserver bookingNotiObserver = new BookingNotiObserver(bookingSubject);

    @PostMapping("/testingSites/{testingSiteId}/book")
    public String booking(Model model, @PathVariable String testingSiteId, HttpSession session,
            @ModelAttribute("bookingInfo") BookingInfo bookingInfo,
            @ModelAttribute("covidTestInfo") CovidTestInfo covidTestInfo,
            @ModelAttribute("covidTestAdditionalInfo") CovidTestAdditionalInfo covidTestAdditionalInfo,
            RedirectAttributes attributes) {

        HttpResponse<String> getUserRes = NetworkHelper.getData("/user");
        UserList userList = new UserList(getUserRes);
        User patient = userList.findUserByUsername(bookingInfo.getUsername());
        String patientId = (patient != null) ? patient.getId() : ""; // get patientId

        if (patientId.length() > 0) {
            User logInUser = (User) session.getAttribute("logInUser");
            String customerId = (logInUser != null) ? logInUser.getId() : ""; // get customerId

            covidTestInfo.setCovidTestAdditionalInfo(covidTestAdditionalInfo);
            bookingInfo.setPostBooking(customerId, testingSiteId);

            BookingAlgoFactory bookingAlgoFactory = new BookingAlgoFactory();
            BookingStrategy bookingStrategy = bookingAlgoFactory.getBookingAlgo(
                    CovidTestMode.valueOf(covidTestInfo.getCovidTestAdditionalInfo().getTestMode().toUpperCase()));

            BookingContext bookingContext = new BookingContext(bookingStrategy);
            Booking newBooking = bookingContext.executeBookStrategy(bookingInfo, covidTestInfo, covidTestAdditionalInfo, patientId);

            if (newBooking != null){
                bookingSubject.setBookingUpdate(BookingAction.CREATE, newBooking);
            }

            return "redirect:/myProfile";
        } else {
            attributes.addFlashAttribute("error",
                    "Incorrect patient username or the patient has not been registered on the system. Please try again.");
            return "redirect:/testingSites/" + testingSiteId;
        }
    }

    @GetMapping("/booking/{id}")
    public String viewBooking(Model model, HttpSession session, @PathVariable String id, RedirectAttributes attributes, HttpServletRequest request,
            Boolean isSearch) {

        String referer = request.getHeader("Referer");
        if (referer.contains("adminPanel")){
            isSearch = false;
        }

        HttpResponse<String> response = NetworkHelper.getData("/booking/" + id + "?fields=covidTests");
        Booking filteredBooking = new Booking();
        try {
            filteredBooking = new ObjectMapper().readValue(response.body(), Booking.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (filteredBooking.getCovidTests()[0].getAdditionalInfo().getTestMode()
                .equals(CovidTestMode.HOME.getValue())) {

            return CovidTestController.searchHomeTestingBooking(model, id, filteredBooking, isSearch);
        } else {
            
            return CovidTestController.searchOnSiteBooking(model, session, filteredBooking.getSmsPin(), attributes, request, isSearch);
        }
    }

    @GetMapping("/booking/{bookingId}/edit")
    public String editBooking(Model model, @PathVariable String bookingId, HttpSession session) {
        model.addAttribute("userInfo", new UserInfo());

        HttpResponse<String> response = NetworkHelper.getData("/booking/" + bookingId + "?fields=covidTests");
        Booking targetBooking = new Booking();
        try {
            targetBooking = new ObjectMapper().readValue(response.body(), Booking.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        HttpResponse<String> response1 = NetworkHelper.getData("/testing-site");
        TestingSiteList testingSiteList = new TestingSiteList(response1);
        TestingSite[] testingSites = testingSiteList.getTestingSiteList();

        model.addAttribute("testingSites", testingSites);
        model.addAttribute("targetBooking", targetBooking);

        return "editBooking";
    }

    @PutMapping("/booking/{bookingId}/edit")
    public String patchBooking(Model model, @PathVariable String bookingId,
            @RequestParam(value = "testingSiteName", required = false) String testingSiteName,
            @RequestParam(value = "startTime", required = false) String startTime, HttpSession session, HttpServletRequest request, RedirectAttributes attributes) {

        HttpResponse<String> response = NetworkHelper.getData("/booking/" + bookingId + "?fields=covidTests");
        Booking targetBooking = new Booking();
        try {
            targetBooking = new ObjectMapper().readValue(response.body(), Booking.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String bookingStatus = targetBooking.getStatus();
        // Only if booking has not been performed yet
        if (bookingStatus.equals(BookingStatus.INITIATED.getValue())
                || bookingStatus.equals(BookingStatus.LAPSED.getValue())) {
            BookingAlgoFactory bookingAlgoFactory = new BookingAlgoFactory();
            // Only ONSITE
            BookingStrategy bookingStrategy = bookingAlgoFactory.getBookingAlgo(CovidTestMode.ONSITE);
            BookingContext bookingContext = new BookingContext(bookingStrategy);
            // ParseNeeded = True because the datetime format is not in Java format
            Booking modifiedBooking = bookingContext.executeModifyStrategy(testingSiteName, bookingId, startTime, true);
            if (modifiedBooking != null) {
                targetBooking.setStatus(targetBooking.getStatus());
                bookingSubject.setBookingUpdate(BookingAction.MODIFY, modifiedBooking, targetBooking);
                model.addAttribute("message", "There are no changes made, please try again later.");
            }
        }
        model.addAttribute("message", "Your changes have been made to the booking!");
        return viewBooking(model, session, bookingId, attributes, request, false);
    }

    @GetMapping("/booking/{bookingId}/revert")
    public String revertBooking(Model model, @PathVariable String bookingId, HttpSession session,
            HttpServletRequest request) {

        model.addAttribute("userInfo", new UserInfo());

        HttpResponse<String> response = NetworkHelper.getData("/booking/" + bookingId + "?fields=covidTests");
        Booking targetBooking = new Booking();
        try {
            targetBooking = new ObjectMapper().readValue(response.body(), Booking.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("targetBooking", targetBooking);

        return "revertBooking";
    }

    @PutMapping("/booking/{bookingId}/revert/{bookingVersion}")
    public String revertBookingChanges(Model model, @PathVariable String bookingId, @PathVariable String bookingVersion,
            HttpSession session, HttpServletRequest request, RedirectAttributes attributes) {

        model.addAttribute("userInfo", new UserInfo());

        HttpResponse<String> response = NetworkHelper.getData("/booking/" + bookingId + "?fields=covidTests");
        Booking targetBooking = new Booking();
        try {
            targetBooking = new ObjectMapper().readValue(response.body(), Booking.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String bookingStatus = targetBooking.getStatus();

        if (bookingStatus.equals(BookingStatus.INITIATED.getValue())
                || bookingStatus.equals(BookingStatus.LAPSED.getValue())) {
            BookingAlgoFactory bookingAlgoFactory = new BookingAlgoFactory();
            // Only ONSITE
            BookingStrategy bookingStrategy = bookingAlgoFactory.getBookingAlgo(CovidTestMode.ONSITE);
            BookingContext bookingContext = new BookingContext(bookingStrategy);
            Booking revertedBooking = bookingContext.executeRevertStrategy(bookingId, true, Integer.parseInt(bookingVersion));
            if (revertedBooking != null) {
                targetBooking.setStatus(targetBooking.getStatus());
                bookingSubject.setBookingUpdate(BookingAction.MODIFY, revertedBooking, targetBooking);
            } else {
                model.addAttribute("message", "The chosen version of Booking has a lapsed start time, " +
                        "please revert to another version or edit the booking.");
            }
        }
        model.addAttribute("targetBooking", targetBooking);
        Integer realBookingVer = Integer.parseInt(bookingVersion) + 1;
        model.addAttribute("message", "You have successfully reverted to Booking Version " + realBookingVer + "!");
        return viewBooking(model, session, bookingId, attributes, request, false);
    }

    @PutMapping("/booking/{bookingId}/cancel")
    public String cancelBooking(Model model, @PathVariable String bookingId, HttpSession session,
            RedirectAttributes attributes, HttpServletRequest request) {

        model.addAttribute("userInfo", new UserInfo());

        BookingAlgoFactory bookingAlgoFactory = new BookingAlgoFactory();
        // Only ONSITE
        BookingStrategy bookingStrategy = bookingAlgoFactory.getBookingAlgo(CovidTestMode.ONSITE);
        BookingContext bookingContext = new BookingContext(bookingStrategy);
        Booking cancelledBooking = bookingContext.executeCancelStrategy(bookingId);
        bookingSubject.setBookingUpdate(BookingAction.CANCEL, cancelledBooking);

        model.addAttribute("targetBooking", cancelledBooking);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @DeleteMapping("/booking/{bookingId}")
    public String deleteBooking(Model model, @PathVariable String bookingId, HttpSession session,
            RedirectAttributes attributes, HttpServletRequest request) {

        model.addAttribute("userInfo", new UserInfo());

        BookingAlgoFactory bookingAlgoFactory = new BookingAlgoFactory();
        // Only ONSITE
        BookingStrategy bookingStrategy = bookingAlgoFactory.getBookingAlgo(CovidTestMode.ONSITE);
        BookingContext bookingContext = new BookingContext(bookingStrategy);
        Booking deletedBooking = bookingContext.executeDeleteStrategy(bookingId);

        if (deletedBooking != null){
            bookingSubject.setBookingUpdate(BookingAction.DELETE, deletedBooking);
            attributes.addFlashAttribute("success",
                    "You have successfully deleted a booking!");
        }
        else{
            attributes.addFlashAttribute("error",
                    "Failed to delete a booking. Please try again.");
        }

        String referer = request.getHeader("Referer");

        if (referer.contains("myProfile")){
            return "redirect:" + referer;
        }
        else{
            return "redirect:/";
        }
    }
}
