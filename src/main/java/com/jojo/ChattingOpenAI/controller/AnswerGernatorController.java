package com.jojo.ChattingOpenAI.controller;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jojo.ChattingOpenAI.service.PdfAnswerGeneratorService;


import java.util.Map;

@RestController
public class AnswerGernatorController {

    private final OpenAiChatModel chatModel;
//    private final PdfUploadService pdfUploadService;
    private final PdfAnswerGeneratorService pdfAnswerGeneratorService;


    @Autowired
    public AnswerGernatorController(OpenAiChatModel chatModel, PdfAnswerGeneratorService pdfAnswerGeneratorService) {
//        this.pdfUploadService = pdfUploadService;
        this.chatModel = chatModel;
        this.pdfAnswerGeneratorService = pdfAnswerGeneratorService;
    }

    // chat with openAI
    @GetMapping("/ai/chat")
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", chatModel.call(message));
    }

    // chat with pdf
    @GetMapping({"/ai/generate"})
    public Map handleQuestion(@RequestParam String message, Model model) {
        String answer = pdfAnswerGeneratorService.generateAnswer(message);
        model.addAttribute("message", message);
        model.addAttribute("answer", answer);
        return Map.of("generation", answer);
    }
}
