package fit3077.lab02team14.assignment2;

import java.io.IOException;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import fit3077.lab02team14.assignment2.constants.CovidTestMode;
import fit3077.lab02team14.assignment2.constants.CovidTestType;
import fit3077.lab02team14.assignment2.model.actor.User;
import fit3077.lab02team14.assignment2.model.actor.UserList;
import fit3077.lab02team14.assignment2.model.booking.Booking;
import fit3077.lab02team14.assignment2.model.booking.BookingAlgoFactory;
import fit3077.lab02team14.assignment2.model.booking.BookingContext;
import fit3077.lab02team14.assignment2.model.booking.BookingStrategy;
import fit3077.lab02team14.assignment2.model.booking.HomeTestAlgo;
import fit3077.lab02team14.assignment2.model.booking.OnSiteTestAlgo;
import fit3077.lab02team14.assignment2.model.test.CovidTest;
import fit3077.lab02team14.assignment2.model.test.CovidTestAdditionalInfo;
import fit3077.lab02team14.assignment2.model.test.HomeState;
import fit3077.lab02team14.assignment2.model.test.OnSiteState;
import fit3077.lab02team14.assignment2.model.modalInfo.CovidTestSuggestion;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSite;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSiteList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fit3077.lab02team14.assignment2.model.modalInfo.BookingInfo;
import fit3077.lab02team14.assignment2.model.modalInfo.CovidTestInfo;
import fit3077.lab02team14.assignment2.model.modalInfo.UserInfo;
import fit3077.lab02team14.assignment2.services.NetworkHelper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.google.zxing.NotFoundException;

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

    @RequestMapping(value = "/testingSites", method = RequestMethod.GET)
    public String searchTestingSites(Model model, HttpSession session,
            @RequestParam(value = "suburb", required = false) String suburb,
            @RequestParam(value = "postcode", required = false) String postcode,
            @RequestParam(value = "facilityType", required = false) String facilityType) {

        HttpResponse<String> response = NetworkHelper.getData("/testing-site");
        TestingSiteList testingSiteList = new TestingSiteList(response);
        TestingSite[] allTestingSites = testingSiteList.filter(suburb, postcode, facilityType);

        model.addAttribute("userInfo", new UserInfo());
        model.addAttribute("allTestingSites", allTestingSites);
        session.setAttribute("isSearch", true);

        return "index";
    }

    @GetMapping("/testingSites/{id}")
    public String testingSite(Model model, @PathVariable String id, HttpSession session) {
        Boolean isAuth = (Boolean) session.getAttribute("isAuth");

        if (isAuth != null && isAuth == true) {
            HttpResponse<String> response = NetworkHelper.getData("/testing-site/" + id + "?fields=bookings");
            TestingSite targetTestingSite = new TestingSite();
            try {
                targetTestingSite = new ObjectMapper().readValue(response.body(), TestingSite.class);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            model.addAttribute("userInfo", new UserInfo());
            model.addAttribute("targetTestingSite", targetTestingSite);
            model.addAttribute("covidTestInfo", new CovidTestInfo());
            model.addAttribute("bookingInfo", new BookingInfo());
            model.addAttribute("covidTestAdditionalInfo", new CovidTestAdditionalInfo());
            session.setAttribute("isSearch", false);
            return "testingSite";
        } else {
            session.setAttribute("isSearch", false);
            return "redirect:/";
        }
    }

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
            BookingStrategy bookingStrategy = bookingAlgoFactory.getBookingAlgo(CovidTestMode.valueOf(covidTestInfo.getCovidTestAdditionalInfo().getTestMode().toUpperCase()));

            BookingContext bookingContext = new BookingContext(bookingStrategy);
            bookingContext.executeBookStrategy(bookingInfo, covidTestInfo, covidTestAdditionalInfo, patientId);

            if (covidTestInfo.getCovidTestAdditionalInfo().getTestMode().equals(CovidTestMode.HOME.getValue())) {
                return "redirect:/homeTesting";
            } else {
                return "redirect:/onSiteTesting";
            }
        } else {
            attributes.addFlashAttribute("error",
                    "Incorrect patient username or the patient has not been registered on the system. Please try again.");
            return "redirect:/testingSites/" + testingSiteId;
        }
    }

    @GetMapping("/onSiteTesting")
    public String onSiteTesting(Model model, HttpSession session) {
        User logInUser = (User) session.getAttribute("logInUser");
        String logInUserId = (logInUser != null) ? logInUser.getId() : "";
        BookingContext bookingContext = new BookingContext(new OnSiteTestAlgo());
        Booking[] bookings = bookingContext.executeViewStrategy(CovidTestMode.ONSITE, logInUserId);

        model.addAttribute("userInfo", new UserInfo());
        model.addAttribute("bookings", bookings);
        return "onSiteTesting";
    }

    @GetMapping("/homeTesting")
    public String homeTesting(Model model, HttpSession session) {
        User logInUser = (User) session.getAttribute("logInUser");
        String logInUserId = (logInUser != null) ? logInUser.getId() : "";

        BookingContext bookingContext = new BookingContext(new HomeTestAlgo());
        Booking[] bookings = bookingContext.executeViewStrategy(CovidTestMode.HOME, logInUserId);

        model.addAttribute("userInfo", new UserInfo());
        model.addAttribute("bookings", bookings);
        return "homeTesting";
    }

    @GetMapping("/searchOnSiteTesting")
    public String searchOnSiteTesting(Model model, HttpSession session) {
        model.addAttribute("userInfo", new UserInfo());
        session.setAttribute("suggestedTestType", "");
        model.addAttribute("suggestedTestType", session.getAttribute("suggestedTestType"));
        model.addAttribute("smsCode", "");
        return "searchOnSiteTesting";
    }

    @RequestMapping(value = "/searchOnSiteTestingBySms", method = RequestMethod.GET)
    public String searchOnSiteBooking(Model model, HttpSession session, @RequestParam(value = "smsCode") String smsCode,
            RedirectAttributes attributes) {

        BookingContext bookingContext = new BookingContext(new OnSiteTestAlgo());
        Booking filteredBooking = bookingContext.executeSearchStrategy(smsCode);

        // If PIN Code entered is invalid
        if (filteredBooking == null) {
            model.addAttribute("message", "Please enter a valid PIN Code.");
            model.addAttribute("userInfo", new UserInfo());
            return "searchOnSiteTesting";
        }

        model.addAttribute("userInfo", new UserInfo());
        model.addAttribute("filteredBooking", filteredBooking);
        model.addAttribute("suggestedTestType", session.getAttribute("suggestedTestType"));
        model.addAttribute("smsCode", smsCode);

        return "searchOnSiteTesting";
    }

    @RequestMapping(value = "/searchOnSiteTestingBySms/{smsCode}", method = RequestMethod.POST)
    public String interview(Model model, HttpSession session, @PathVariable String smsCode,
            @RequestParam(value = "symptoms", required = false) String[] symptoms,
            @RequestParam(value = "closeContact", required = true) String closeContact,
            @RequestParam(value = "closeContactPeriod", required = false) String closeContactPeriod,
            @RequestParam(value = "symptomsPeriod", required = false) String symptomsPeriod,
            @RequestParam(value = "suffers", required = false) String[] suffers, RedirectAttributes attributes) {

        CovidTestSuggestion covidTestSuggestion = new CovidTestSuggestion(symptoms, closeContact, closeContactPeriod,
                symptomsPeriod, suffers);
        CovidTestType suggestedCovidTestType = covidTestSuggestion.suggestTestType();
        session.setAttribute("suggestedTestType", suggestedCovidTestType.getValue());

        return searchOnSiteBooking(model, session, smsCode, attributes);
    }

    @PutMapping("/searchOnSiteTestingBySms/{smsCode}/{bookingId}/{covidTestId}")
    public String updateFinalTestType(@PathVariable String smsCode, @PathVariable String bookingId,
            @PathVariable String covidTestId, @RequestParam(value = "finalTestType") String finalTestType, Model model,
            HttpSession session, RedirectAttributes attributes) {
        User administerer = (User) session.getAttribute("logInUser");
        HttpResponse<String> response = NetworkHelper.getData("/covid-test/" + covidTestId);
        CovidTest covidTest = new CovidTest();
        try {
            covidTest = new ObjectMapper().readValue(response.body(), CovidTest.class);
            covidTest.setPostCovidTest(finalTestType, administerer);
            OnSiteState onSiteState = new OnSiteState();
            onSiteState.updateTestState(covidTest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        session.setAttribute("suggestedTestType", "");
        return searchOnSiteBooking(model, session, smsCode, attributes);
    }

    // public void patchAdministererForOnSiteTesting(CovidTest covidTest, String finalTestType, User administerer) {

    // }

    @GetMapping("/searchHomeTesting")
    public String searchHomeTesting(Model model, HttpSession session) {
        model.addAttribute("userInfo", new UserInfo());
        session.setAttribute("qrCodePath", "");
        model.addAttribute("targetBooking", null);
        return "searchHomeTesting";
    }

    @GetMapping("/searchHomeTesting/{id}")
    public String searchHomeTestingBooking(Model model, @PathVariable String id, Booking targetBooking) {

        model.addAttribute("userInfo", new UserInfo());
        model.addAttribute("targetBooking", targetBooking);
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

        //If invalid QR Code is uploaded
        if(targetBooking.getAdditionalInfo() == null){
            attributes.addFlashAttribute("message", "Invalid QR Code uploaded");
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        session.setAttribute("qrCodePath", "./image/qrCodes/" + fileName);

        return searchHomeTestingBooking(model, targetBooking.getId(), targetBooking);
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

        return searchHomeTestingBooking(model, bookingId, targetBooking);
    }
    
    @PostMapping("/logins")
    public String login(@ModelAttribute("userInfo") UserInfo userInfo, HttpSession session, HttpServletRequest request,
            RedirectAttributes attributes) throws IOException {

        HttpResponse<String> response = NetworkHelper.authUser("/user/login", userInfo);
        System.out.println(response);
        String decodedJWT = NetworkHelper.getUser(response);

        Boolean isAuth = false;

        if (decodedJWT.length() > 0) {
            User logInUser = new ObjectMapper().readValue(decodedJWT, User.class);
            session.setAttribute("logInUser", logInUser);
            System.out.println(logInUser.toString());
            System.out.println(logInUser.getClass().getName());
            attributes.addFlashAttribute("success",
                    "You have now successfully logged in as " + logInUser.getUserName() + ".");
            isAuth = true;
        } else {
            attributes.addFlashAttribute("error", "Your username or password is incorrect. Please try again.");
            isAuth = false;
        }

        session.setAttribute("isAuth", isAuth);

        return "redirect:/";
    }

    @GetMapping("/logouts")
    public String logout(HttpServletRequest request, HttpSession session) {

        session.removeAttribute("isAuth");
        session.removeAttribute("logInUser");

        return "redirect:/";
    }
}
