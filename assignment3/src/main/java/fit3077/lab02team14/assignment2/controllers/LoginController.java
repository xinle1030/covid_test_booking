package fit3077.lab02team14.assignment2.controllers;

import java.io.IOException;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import fit3077.lab02team14.assignment2.model.actor.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import fit3077.lab02team14.assignment2.model.modalInfo.UserInfo;
import fit3077.lab02team14.assignment2.services.NetworkHelper;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @PostMapping("/logins")
    public String login(@ModelAttribute("userInfo") UserInfo userInfo, HttpSession session, HttpServletRequest request,
            RedirectAttributes attributes) throws IOException {

        HttpResponse<String> response = NetworkHelper.authUser("/user/login", userInfo);
        String decodedJWT = NetworkHelper.getUser(response);

        Boolean isAuth = false;

        if (decodedJWT.length() > 0) {
            User authUser = new ObjectMapper().readValue(decodedJWT, User.class);

            HttpResponse<String> userRes = NetworkHelper.getData("/user/" + authUser.getId());
            User logInUser = new ObjectMapper().readValue(userRes.body(), User.class);

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
}
