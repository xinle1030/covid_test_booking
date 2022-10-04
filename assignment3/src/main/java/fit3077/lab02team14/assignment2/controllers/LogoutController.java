package fit3077.lab02team14.assignment2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {
    @GetMapping("/logouts")
    public String logout(HttpServletRequest request, HttpSession session) {

        session.removeAttribute("isAuth");
        session.removeAttribute("logInUser");

        return "redirect:/";
    }
}
