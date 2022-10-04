package fit3077.lab02team14.assignment2.controllers;

import java.io.IOException;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import fit3077.lab02team14.assignment2.constants.BookingAction;
import fit3077.lab02team14.assignment2.constants.CovidTestType;
import fit3077.lab02team14.assignment2.model.actor.User;
import fit3077.lab02team14.assignment2.model.booking.Booking;
import fit3077.lab02team14.assignment2.model.booking.BookingContext;
import fit3077.lab02team14.assignment2.model.booking.HomeTestAlgo;
import fit3077.lab02team14.assignment2.model.booking.OnSiteTestAlgo;
import fit3077.lab02team14.assignment2.model.test.CovidTest;
import fit3077.lab02team14.assignment2.model.test.HomeState;
import fit3077.lab02team14.assignment2.model.test.OnSiteState;
import fit3077.lab02team14.assignment2.model.modalInfo.CovidTestSuggestion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fit3077.lab02team14.assignment2.model.modalInfo.UserInfo;
import fit3077.lab02team14.assignment2.services.NetworkHelper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.google.zxing.NotFoundException;

@Controller
public class CovidTestController {
    @GetMapping("/searchOnSiteTesting")
    public String searchOnSiteTesting(Model model, HttpSession session) {
        model.addAttribute("userInfo", new UserInfo());
        model.addAttribute("filteredBooking", new Booking());
        session.setAttribute("suggestedTestType", "");
        model.addAttribute("suggestedTestType", session.getAttribute("suggestedTestType"));
        model.addAttribute("smsCode", "");
        model.addAttribute("isSearch", true);
        return "searchOnSiteTesting";
    }

    @RequestMapping(value = "/searchOnSiteTestingBySms", method = RequestMethod.GET)
    public static String searchOnSiteBooking(Model model, HttpSession session, @RequestParam(value = "smsCode") String smsCode,
            RedirectAttributes attributes, HttpServletRequest request, Boolean isSearch) {

        BookingContext bookingContext = new BookingContext(new OnSiteTestAlgo());
        Booking filteredBooking = bookingContext.executeSearchStrategy(smsCode);
        User logInUser = (User) session.getAttribute("logInUser");
        String bookingSiteId = (filteredBooking != null) ? filteredBooking.getTestingSite().getId() : "";

        // If PIN Code entered is invalid
        if (filteredBooking == null) {
            model.addAttribute("userInfo", new UserInfo());
            model.addAttribute("isSearch", isSearch);
            attributes.addFlashAttribute("error","Please enter a valid PIN Code or Booking ID.");
            return "redirect:/searchOnSiteTesting/";
        }
        // if user does not work at the testing site
        // else if (filteredBooking != null && !(logInUser.getAdditionalInfo().doesTestingSiteMatch(bookingSiteId))){
        //     model.addAttribute("userInfo", new UserInfo());
        //     model.addAttribute("isSearch", isSearch);
        //     attributes.addFlashAttribute("error","You are not allowed to view the booking details as you work in a different testing site.");
        //     return "redirect:/searchOnSiteTesting/";
        // }

        model.addAttribute("userInfo", new UserInfo());
        model.addAttribute("filteredBooking", filteredBooking);
        String suggestedTestType = (session.getAttribute("suggestedTestType") != null)
                ? (String) session.getAttribute("suggestedTestType")
                : "";
        model.addAttribute("suggestedTestType", suggestedTestType);
        model.addAttribute("smsCode", smsCode);
        model.addAttribute("isSearch", isSearch);
        return "searchOnSiteTesting";
    }

    @RequestMapping(value = "/searchOnSiteTestingBySms/{smsCode}", method = RequestMethod.POST)
    public String interview(Model model, HttpSession session, @PathVariable String smsCode,
            @RequestParam(value = "symptoms", required = false) String[] symptoms,
            @RequestParam(value = "closeContact", required = true) String closeContact,
            @RequestParam(value = "closeContactPeriod", required = false) String closeContactPeriod,
            @RequestParam(value = "symptomsPeriod", required = false) String symptomsPeriod,
            @RequestParam(value = "suffers", required = false) String[] suffers, RedirectAttributes attributes, HttpServletRequest request) {

        CovidTestSuggestion covidTestSuggestion = new CovidTestSuggestion(symptoms, closeContact, closeContactPeriod,
                symptomsPeriod, suffers);
        CovidTestType suggestedCovidTestType = covidTestSuggestion.suggestTestType();
        session.setAttribute("suggestedTestType", suggestedCovidTestType.getValue());

        return searchOnSiteBooking(model, session, smsCode, attributes, request, true);
    }

    @PutMapping("/searchOnSiteTestingBySms/{smsCode}/{bookingId}/{covidTestId}")
    public String updateFinalTestType(@PathVariable String smsCode, @PathVariable String bookingId,
            @PathVariable String covidTestId, @RequestParam(value = "finalTestType") String finalTestType, Model model,
            HttpSession session, HttpServletRequest request, RedirectAttributes attributes) {
        User administerer = (User) session.getAttribute("logInUser");
        HttpResponse<String> response = NetworkHelper.getData("/covid-test/" + covidTestId);
        CovidTest covidTest = new CovidTest();
        try {
            covidTest = new ObjectMapper().readValue(response.body(), CovidTest.class);
            covidTest.setPostCovidTest(finalTestType, administerer);
            OnSiteState onSiteState = new OnSiteState();
            onSiteState.updateTestState(covidTest);

            HttpResponse<String> response2 = NetworkHelper.getData("/booking/" + bookingId + "?fields=covidTests");
            Booking targetBooking = new Booking();
            try {
                targetBooking = new ObjectMapper().readValue(response2.body(), Booking.class);
                BookingController.bookingSubject.setBookingUpdate(BookingAction.PROCESS, targetBooking);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        session.setAttribute("suggestedTestType", "");
        return searchOnSiteBooking(model, session, smsCode, attributes, request, true);
    }

    @GetMapping("/searchHomeTesting")
    public String searchHomeTesting(Model model, HttpSession session) {
        model.addAttribute("userInfo", new UserInfo());
        model.addAttribute("targetBooking", null);
        model.addAttribute("isSearch", true);
        return "searchHomeTesting";
    }

    @GetMapping("/searchHomeTesting/{id}")
    public static String searchHomeTestingBooking(Model model, @PathVariable String id, Booking targetBooking, Boolean isSearch) {
        targetBooking.getAdditionalInfo().setImgPath(id);
        model.addAttribute("userInfo", new UserInfo());
        model.addAttribute("targetBooking", targetBooking);
        model.addAttribute("isSearch", isSearch);
        return "searchHomeTesting";
    }

    @PostMapping("/searchHomeTesting")
    public String searchHomeTestingByFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,
            HttpServletRequest request, HttpSession session, Model model) throws NotFoundException, IOException {

        String fileName = "";
        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        } else {
            fileName = file.getOriginalFilename();
            attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');
        }

        BookingContext bookingContext = new BookingContext(new HomeTestAlgo());
        Booking targetBooking = bookingContext.executeSearchStrategy(fileName);

        // If invalid QR Code is uploaded
        if (targetBooking.getAdditionalInfo() == null) {
            attributes.addFlashAttribute("message", "Invalid QR Code uploaded");
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        return searchHomeTestingBooking(model, targetBooking.getId(), targetBooking, true);
    }

    @PutMapping("/searchHomeTesting/{bookingId}/{covidTestId}")
    public String updateTestKitStatus(@PathVariable String bookingId, @PathVariable String covidTestId,
            @RequestParam(value = "testKitStatus") String testKitStatus, Model model) {
        HttpResponse<String> response1 = NetworkHelper.getData("/covid-test/" + covidTestId);
        CovidTest covidTest = new CovidTest();
        try {
            covidTest = new ObjectMapper().readValue(response1.body(), CovidTest.class);
            HomeState homeState = new HomeState();
            String latestTestKitStatus = (testKitStatus.equals("0")) ? "0" : "1";
            covidTest.getAdditionalInfo().setTestKitStatus(latestTestKitStatus);
            homeState.updateTestState(covidTest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        HttpResponse<String> response2 = NetworkHelper.getData("/booking/" + bookingId + "?fields=covidTests");
        Booking targetBooking = new Booking();
        try {
            targetBooking = new ObjectMapper().readValue(response2.body(), Booking.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return searchHomeTestingBooking(model, bookingId, targetBooking, true);
    }
}
