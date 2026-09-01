package com.sd.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sd/bank/login")
public class LoginController {

    @GetMapping()
    public String login(){
        return "User logged in Successfully";
    }
}
