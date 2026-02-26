package com.cal.yughistore.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:5173")
public class BasePresentationController {

    @GetMapping("")
    public ResponseEntity<String> getIndex() {
        return ResponseEntity.ok("connected");
    }

    @GetMapping("/api/v1")
    public ResponseEntity<String> getApi() {
        return ResponseEntity.ok("/api/v1");
    }
}
