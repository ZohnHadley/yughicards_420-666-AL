package com.cal.yughistore.presentation;

import com.cal.yughistore.model.yughiocard.enums.EnumCardType;
import com.cal.yughistore.model.yughiocard.enums.EnumFrameType;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.service.YughioCardService;
import com.cal.yughistore.utils.SimpleEnumUtils;
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
    public ResponseEntity<YughioCardDTO> getCardInformationByID(@PathVariable int cardID) {
        return ResponseEntity.ok(cardService.getById((long) cardID));
    }

    /// ///

    /// get by name ///
//    @GetMapping("/get-card/name={cardName}")
//    public ResponseEntity<YughioCardDTO> getNamedCardInformation(@PathVariable String cardName) {
//        return ResponseEntity.ok(cardService.getByName(cardName));
//    }

    /// ///

    /// search by name + pagination ///

    @GetMapping("/get-all-cards/search={cardName}/page={pageNumber}/num={numberOfCards}")
    public ResponseEntity<List<YughioCardDTO>> getCardInformationBySearchName(@PathVariable String cardName, @PathVariable int pageNumber, @PathVariable int numberOfCards) {
        return ResponseEntity.ok(cardService.getSearchByName(cardName, pageNumber, numberOfCards));
    }

    @GetMapping("/get-all-cards/search={cardName}/page={pageNumber}")
    public ResponseEntity<List<YughioCardDTO>> getCardInformationBySearchName(@PathVariable String cardName, @PathVariable int pageNumber) {
        return ResponseEntity.ok(cardService.getSearchByName(cardName, pageNumber, pagination_default_number_of_elements_per_page));
    }

    ///  ///

    /// get all cards + pagination ///

    @GetMapping("/get-all-cards/page={pageNumber}/num={numberOfCards}")
    public ResponseEntity<List<YughioCardDTO>> getAllCardsPagedInformation(@PathVariable int pageNumber, @PathVariable int numberOfCards) {
        return ResponseEntity.ok(cardService.getAllPaged(pageNumber, numberOfCards));
    }

    @GetMapping("/get-all-cards/num={numberOfCards}")
    public ResponseEntity<List<YughioCardDTO>> getAllCardsPagedInformation_numberOfCardsWithSetPage(@PathVariable int numberOfCards) {
        return ResponseEntity.ok(cardService.getAllPaged(0, numberOfCards));
    }

    @GetMapping("/get-all-cards/page={pageNumber}")
    public ResponseEntity<List<YughioCardDTO>> getAllCardsPagedInformation_pageWithSetNumberOfCards(@PathVariable int pageNumber) {
        return ResponseEntity.ok(cardService.getAllPaged(pageNumber, pagination_default_number_of_elements_per_page));
    }

    /// ///

    /// get all cards by frameType + pagination ///

    @GetMapping("/get-all-cards/frame={frameType}/page={pageNumber}/num={numberOfCards}")
    public ResponseEntity<List<YughioCardDTO>> getAllCardsPagedInformationByFrameType(@PathVariable String frameType, @PathVariable int pageNumber, @PathVariable int numberOfCards) {
        EnumFrameType type = SimpleEnumUtils.findEnumValue(EnumFrameType.class, frameType);
        if (type == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(cardService.getByFrameTypePaged(type, pageNumber, numberOfCards));
    }

    @GetMapping("/get-all-cards/frame={frameType}/page={pageNumber}")
    public ResponseEntity<List<YughioCardDTO>> getAllCardsPagedInformationByFrameType(@PathVariable String frameType, @PathVariable int pageNumber) {
        EnumFrameType type = SimpleEnumUtils.findEnumValue(EnumFrameType.class, frameType);
        if (type == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardService.getByFrameTypePaged(type, pageNumber, pagination_default_number_of_elements_per_page));
    }

    /// ///

    /// get all cards by type + pagination ///

    @GetMapping("/get-all-cards/type={cardType}/page={pageNumber}/num={numberOfCards}")
    public ResponseEntity<List<YughioCardDTO>> getAllCardsPagedInformationByCardType(@PathVariable String cardType, @PathVariable int pageNumber, @PathVariable int numberOfCards) {
        EnumCardType type = SimpleEnumUtils.findEnumValue(EnumCardType.class, cardType);
        if (type == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardService.getByTypePaged(type, pageNumber, numberOfCards));
    }

    @GetMapping("/get-all-cards/type={cardType}/page={pageNumber}")
    public ResponseEntity<List<YughioCardDTO>> getAllCardsPagedInformationByCardType(@PathVariable String cardType, @PathVariable int pageNumber) {
        EnumCardType type = SimpleEnumUtils.findEnumValue(EnumCardType.class, cardType);
        if (type == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardService.getByTypePaged(type, pageNumber, pagination_default_number_of_elements_per_page));
    }

    // Get all versions (sets + raretés) of a card by name
    @GetMapping("/get-card/all-versions/name={cardName}")
    public ResponseEntity<List<YughioCardDTO>> getAllVersionsOfCard(@PathVariable String cardName) {
        List<YughioCardDTO> cards = cardService.getAllVersionsOfCard(cardName);
        return ResponseEntity.ok(cards);
    }

    /// ///

}
