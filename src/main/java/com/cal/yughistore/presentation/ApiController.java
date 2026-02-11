package com.cal.yughistore.presentation;

import com.cal.yughistore.services.DTOs.DTOYughioCard;
import com.cal.yughistore.services.YughioCardService;
import com.cal.yughistore.services.api.ApiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class ApiController {

    private final ApiService apiService;
    @GetMapping("/")
    public ResponseEntity<String> getAllCardsInformation(){
        return ResponseEntity.ok("Connected");
    }

    //TODO [] : going to change so it returns a DTO instead of String (but for now it returns String)
    @GetMapping("/get-cards-all")
    public ResponseEntity<List<DTOYughioCard>> index(){
        return ResponseEntity.ok(apiService.getInformationForAllCards());
    }

    @GetMapping("/get-cards/num={numberOfCards}")
    public ResponseEntity<List<DTOYughioCard>> getAllCardsInformation(@PathVariable int numberOfCards){
        return ResponseEntity.ok(apiService.getInformationForAllCards(numberOfCards));
    }

    @GetMapping("/get-cards/num={numberOfCards}/offset={listOffset}")
    public ResponseEntity<List<DTOYughioCard>> getAllCardsInformation(@PathVariable int numberOfCards, @PathVariable int listOffset){
        return ResponseEntity.ok(apiService.getInformationForAllCards(numberOfCards, listOffset));
    }

    @GetMapping("/get-card/name={cardName}")
    public ResponseEntity<DTOYughioCard> getAllCardsInformation(@PathVariable String cardName){
        return ResponseEntity.ok(apiService.getInformationForNamedCard(cardName));
    }

}
