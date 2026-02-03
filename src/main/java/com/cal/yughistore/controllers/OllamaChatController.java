package com.cal.yughistore.controllers;

import com.cal.yughistore.services.OllamaModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OllamaChatController {

    private final OllamaModelService ollamaModelService;

    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        return ollamaModelService.prompt(prompt);
    }
}

