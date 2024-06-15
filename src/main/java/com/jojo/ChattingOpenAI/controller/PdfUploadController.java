package com.jojo.ChattingOpenAI.controller;

import com.jojo.ChattingOpenAI.service.PdfUploadService;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class PdfUploadController {

    private final PdfUploadService pdfUploadService;

    @Autowired
    public PdfUploadController(PdfUploadService pdfUploadService, OpenAiChatModel chatModel) {
        this.pdfUploadService = pdfUploadService;
//        this.chatModel = chatModel;
    }

    // chat with PDF
    @PostMapping({"/api/upload"})
    public Map uploadPdf(@RequestParam("file") MultipartFile file, Model model) {
        pdfUploadService.processPDF(file);
        model.addAttribute("message", "File uploaded and processed successfully");
        System.out.println("File uploaded and processed successfully");
        return Map.of("message", "chat");
    }

}
