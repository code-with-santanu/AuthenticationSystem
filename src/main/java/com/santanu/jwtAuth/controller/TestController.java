package com.santanu.jwtAuth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/allUser")
    public ResponseEntity<String> demo() {
        return new ResponseEntity<String>("All users working", HttpStatus.valueOf(200));
    }

    @GetMapping("/adminOnly")
    public ResponseEntity<String> adminOnly() {
        return new ResponseEntity<String>("Admin is working", HttpStatus.valueOf(200));
    }
}
