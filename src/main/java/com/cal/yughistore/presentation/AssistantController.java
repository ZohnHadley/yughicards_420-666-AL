package com.cal.yughistore.presentation;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequestMapping("/api/v1/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class AssistantController {

    private final ChatClient chatClient;

    public AssistantController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/ask")
    public String question(String quesiton){
        return this.chatClient.prompt()
                .user(quesiton)
                .call()
                .content()
                ;
    }

}
