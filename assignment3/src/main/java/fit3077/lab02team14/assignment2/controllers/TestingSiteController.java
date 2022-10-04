package fit3077.lab02team14.assignment2.controllers;

import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;


import fit3077.lab02team14.assignment2.model.test.CovidTestAdditionalInfo;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSite;
import fit3077.lab02team14.assignment2.model.testingsite.TestingSiteList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fit3077.lab02team14.assignment2.model.modalInfo.BookingInfo;
import fit3077.lab02team14.assignment2.model.modalInfo.CovidTestInfo;
import fit3077.lab02team14.assignment2.model.modalInfo.UserInfo;
import fit3077.lab02team14.assignment2.services.NetworkHelper;

import javax.servlet.http.HttpSession;

@Controller
public class TestingSiteController {
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
}
