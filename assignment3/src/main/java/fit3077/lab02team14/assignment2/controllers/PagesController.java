package fit3077.lab02team14.assignment2.controllers;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import fit3077.lab02team14.assignment2.constants.CovidTestMode;
import fit3077.lab02team14.assignment2.model.actor.User;
import fit3077.lab02team14.assignment2.model.booking.Booking;
import fit3077.lab02team14.assignment2.model.booking.BookingContext;
import fit3077.lab02team14.assignment2.model.booking.BookingUpdate;
import fit3077.lab02team14.assignment2.model.booking.HomeTestAlgo;
import fit3077.lab02team14.assignment2.model.booking.OnSiteTestAlgo;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSite;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSiteList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import fit3077.lab02team14.assignment2.model.modalInfo.UserInfo;
import fit3077.lab02team14.assignment2.services.NetworkHelper;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;

@Controller
public class PagesController {

    @GetMapping("/")
    public String index(Model model, HttpSession session, RedirectAttributes attributes) {
        Boolean isAuth = (Boolean) session.getAttribute("isAuth");
        if (isAuth == null) {
            session.setAttribute("isAuth", false);
        }
        HttpResponse<String> response = NetworkHelper.getData("/testing-site" + "?fields=bookings");

        TestingSiteList testingSiteList = new TestingSiteList(response);
        TestingSite[] allTestingSites = testingSiteList.getTestingSiteList();

        model.addAttribute("allTestingSites", allTestingSites);
        model.addAttribute("userInfo", new UserInfo());

        return "index";
    }

    @GetMapping("/adminPanel")
    public String adminPanel(Model model, HttpSession session) {
        User logInUser = (User) session.getAttribute("logInUser");
        TestingSite testingSite = (logInUser != null) ? logInUser.getAdditionalInfo().getTestingSite() : null;

        String testingSiteId = (testingSite != null) ? testingSite.getId() : "";
        TestingSite targetTestingSite = new TestingSite();
        Booking[] bookings = {};

        if (testingSiteId != null && testingSiteId.length() > 0) {
            HttpResponse<String> response = NetworkHelper
                    .getData("/testing-site/" + testingSiteId + "?fields=bookings.covidTests");
            try {
                targetTestingSite = new ObjectMapper().readValue(response.body(), TestingSite.class);
                bookings = targetTestingSite.getBookings();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("userInfo", new UserInfo());

        return "adminPanel";
    }

    @GetMapping("/myProfile")
    public String myProfile(Model model, HttpSession session) {
        User logInUser = (User) session.getAttribute("logInUser");
        String logInUserId = (logInUser != null) ? logInUser.getId() : "";

        BookingContext bookingContext1 = new BookingContext(new HomeTestAlgo());
        Booking[] homeBookings = bookingContext1.executeViewStrategy(CovidTestMode.HOME, logInUserId);

        BookingContext bookingContext2 = new BookingContext(new OnSiteTestAlgo());
        Booking[] activeOnSiteBookings = bookingContext2.executeViewStrategy(CovidTestMode.ONSITE, logInUserId, true);
        Booking[] inactiveOnSiteBookings = bookingContext2.executeViewStrategy(CovidTestMode.ONSITE, logInUserId,
                false);

        model.addAttribute("userInfo", new UserInfo());
        model.addAttribute("homeBookings", homeBookings);
        model.addAttribute("activeOnSiteBookings", activeOnSiteBookings);
        model.addAttribute("inactiveOnSiteBookings", inactiveOnSiteBookings);
        model.addAttribute("userInfo", new UserInfo());
        return "myProfile";
    }



    @GetMapping("/notifications")
    public String getNotificationsPage(Model model, HttpSession session) {
        User logInUser = (User) session.getAttribute("logInUser");
        List<BookingUpdate> bookingChanges = BookingController.bookingNotiObserver.getBookingNotification().getBookingChanges();
        List<String> titles = BookingController.bookingNotiObserver.getBookingNotification().getTitles();
        List<String> messages = BookingController.bookingNotiObserver.getBookingNotification().getMessages();

        List<BookingUpdate> filteredBookingChangesList = new ArrayList<BookingUpdate>();
        List<String> filteredTitlesList = new ArrayList<String>();
        List<String> filteredMsgList = new ArrayList<String>();

        BookingUpdate[] filteredBookingChanges = new BookingUpdate[0];
        String[] filteredTitles = new String[0];
        String[] filteredMsg = new String[0];

        for (int i = 0; i < bookingChanges.size(); i++){
            String testingSiteId = bookingChanges.get(i).getUpdatedBooking().getTestingSite().getId();
            Booking prevBooking = bookingChanges.get(i).getPrevBooking();
            String prevTestingSiteId = (prevBooking != null) ? prevBooking.getTestingSite().getId() : "";

            if (logInUser.getAdditionalInfo().doesTestingSiteMatch(testingSiteId) || logInUser.getAdditionalInfo().doesTestingSiteMatch(prevTestingSiteId)){
                filteredBookingChangesList.add(bookingChanges.get(i));
                filteredTitlesList.add(titles.get(i));
                filteredMsgList.add(messages.get(i));
            }
        }

        filteredBookingChanges = filteredBookingChangesList.toArray(new BookingUpdate[0]);
        filteredTitles = filteredTitlesList.toArray(new String[0]);
        filteredMsg = filteredMsgList.toArray(new String[0]);

        model.addAttribute("userInfo", new UserInfo());

        model.addAttribute("filteredBookingChanges", filteredBookingChanges);
        model.addAttribute("filteredTitles", filteredTitles);
        model.addAttribute("filteredMsg", filteredMsg);
        return "notifications";
    }
}
