package com.cal.yughistore.presentation;

import com.cal.yughistore.services.YughioCardService;
import com.cal.yughistore.services.api.ApiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class ApiController {

    private final ApiService apiService;

    //TODO [] : going to change so it returns a DTO instead of String (but for now it returns String)
    @GetMapping("/get-all-cards")
    public ResponseEntity<String> getAllCardsInformation(){
        return ResponseEntity.ok(apiService.getInformationForAllCards().asText());
    }

}
