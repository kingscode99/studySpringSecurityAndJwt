package kr.co.realiv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "/home.html";
    }

    @GetMapping("/login-page")
    public String loginPage() {
        return "/logIn.html";
    }

    @GetMapping("/sign-up-page")
    public String signUpPage() {
        return "/signUp.html";
    }

    @GetMapping("/member-detail-page")
    public String memberDetailPage() {
        return "/memberDetail.html";
    }

    @GetMapping("/member-update-page/{userId}")
    public String memberUpdatePage() {
        return "/memberUpdate.html";
    }

}
