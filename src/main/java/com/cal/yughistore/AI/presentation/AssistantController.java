package com.cal.yughistore.AI.presentation;

import com.cal.yughistore.AI.service.AiChatBotService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequestMapping("/api/v1/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class AssistantController {

    final AiChatBotService chatBotService;

    public AssistantController(
            AiChatBotService chatBotService
    ) {
        this.chatBotService = chatBotService;
    }

    /// description : ask the chatbot a question using a cleaner REST-style query parameter
    /// example of use : /{userName}/ask?question=what card do you recommend
    @GetMapping(value = "/{userName}/ask", produces = MediaType.TEXT_PLAIN_VALUE)
    public String askQuestion(
            @PathVariable String userName,
            @RequestParam("question") String question
    ) {
        return chatBotService.generateResponse(userName, question);
    }

    /// description : legacy endpoint kept for backward compatibility
    /// example of use : /{userName}/ask=what card do you recommend
    @GetMapping("/{userName}/ask={question}")
    public String question(@PathVariable String userName, @PathVariable String question) {
        return chatBotService.generateResponse(userName, question);
    }

}
