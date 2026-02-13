package com.cal.yughistore.presentation;

import com.cal.yughistore.services.DTOs.DTOYughioCard;
import com.cal.yughistore.services.YughioCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class CardApiController {

    private final YughioCardService cardService;
    private final int pagination_default_number_of_elements_per_page = 10;

    ///
    ///
    ///
    /// You can rework this to fit the front end and make it more easier
    ///
    ///
    ///

    @GetMapping("")
    public ResponseEntity<String> getAllCardsInformation() {
        return ResponseEntity.ok("/api/v1");
    }

    /// get by id ///

    @GetMapping("/get-card/id={cardID}")
    public ResponseEntity<DTOYughioCard> getCardInformationByID(@PathVariable int cardID) {
        return ResponseEntity.ok(cardService.getById((long) cardID));
    }

    /// ///

    /// get by name ///
    @GetMapping("/get-card/name={cardName}")
    public ResponseEntity<DTOYughioCard> getNamedCardInformation(@PathVariable String cardName) {
        return ResponseEntity.ok(cardService.getByName(cardName));
    }

    /// ///

    /// search by name + pagination ///

    @GetMapping("/get-cards/search={cardName}/page={pageNumber}/num={numberOfCards}")
    public ResponseEntity<List<DTOYughioCard>> getCardInformationBySearchName(@PathVariable String cardName, @PathVariable int pageNumber, @PathVariable int numberOfCards) {
        return ResponseEntity.ok(cardService.getBySearchName(cardName, pageNumber, numberOfCards));
    }

    @GetMapping("/get-cards/search={cardName}/page={pageNumber}")
    public ResponseEntity<List<DTOYughioCard>> getCardInformationBySearchName(@PathVariable String cardName, @PathVariable int pageNumber) {
        return ResponseEntity.ok(cardService.getBySearchName(cardName, pageNumber, pagination_default_number_of_elements_per_page));
    }

    ///  ///

    /// get all cards + pagination ///

    @GetMapping("/get-all-cards/page={pageNumber}/num={numberOfCards}")
    public ResponseEntity<List<DTOYughioCard>> getAllCardsPagedInformation(@PathVariable int pageNumber, @PathVariable int numberOfCards) {
        return ResponseEntity.ok(cardService.getAllPaged(pageNumber, numberOfCards));
    }

    @GetMapping("/get-all-cards/num={numberOfCards}")
    public ResponseEntity<List<DTOYughioCard>> getAllCardsPagedInformation_numberOfCardsWithSetPage(@PathVariable int numberOfCards) {
        return ResponseEntity.ok(cardService.getAllPaged(0, numberOfCards));
    }

    @GetMapping("/get-all-cards/page={pageNumber}")
    public ResponseEntity<List<DTOYughioCard>> getAllCardsPagedInformation_pageWithSetNumberOfCards(@PathVariable int pageNumber) {
        return ResponseEntity.ok(cardService.getAllPaged(pageNumber, pagination_default_number_of_elements_per_page));
    }

    /// ///

    /// get all cards by frameType + pagination ///

    @GetMapping("/get-all-cards/frame={frameType}/page={pageNumber}/num={numberOfCards}")
    public ResponseEntity<List<DTOYughioCard>> getAllCardsPagedInformationByFrameType(@PathVariable String frameType, @PathVariable int pageNumber, @PathVariable int numberOfCards) {
        return ResponseEntity.ok(cardService.getByFrameTypePaged(frameType, pageNumber, numberOfCards));
    }

    @GetMapping("/get-all-cards/frame={frameType}/page={pageNumber}")
    public ResponseEntity<List<DTOYughioCard>> getAllCardsPagedInformationByFrameType(@PathVariable String frameType, @PathVariable int pageNumber) {
        return ResponseEntity.ok(cardService.getByFrameTypePaged(frameType, pageNumber, pagination_default_number_of_elements_per_page));
    }

    /// ///

    /// get all cards by type + pagination ///

    @GetMapping("/get-all-cards/type={cardType}/page={pageNumber}/num={numberOfCards}")
    public ResponseEntity<List<DTOYughioCard>> getAllCardsPagedInformationByCardType(@PathVariable String cardType, @PathVariable int pageNumber, @PathVariable int numberOfCards) {
        return ResponseEntity.ok(cardService.getByTypePaged(cardType, pageNumber, numberOfCards));
    }

    @GetMapping("/get-all-cards/type={cardType}/page={pageNumber}")
    public ResponseEntity<List<DTOYughioCard>> getAllCardsPagedInformationByCardType(@PathVariable String cardType, @PathVariable int pageNumber) {
        return ResponseEntity.ok(cardService.getByTypePaged(cardType, pageNumber, pagination_default_number_of_elements_per_page));
    }

    /// ///

}
