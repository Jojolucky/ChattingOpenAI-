package com.jojo.ChattingOpenAI.service;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfUploadService {

    @Autowired
    private VectorStore vectorStore;
//    private final ChatModel aiModel;

    @Autowired
    public PdfUploadService(VectorStore vectorStore) {

        this.vectorStore = vectorStore;
//        this.aiModel = aiModel;
    }

    // Convert MultipartFile to FileSystemResource
    private Resource convertMultiPartToFile(MultipartFile file) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        File convFile = new File(tempDir, Objects.requireNonNull(file.getOriginalFilename()));

        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }

        return new FileSystemResource(convFile);
    }

    // Process PDF by chunking it
    public void processPDF(MultipartFile pdfFile) {
        try {
            Resource convFile = convertMultiPartToFile(pdfFile);
            PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().build())
                    .build();
            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(convFile, config);
            TokenTextSplitter textSplitter = new TokenTextSplitter();
            vectorStore.accept(textSplitter.apply(pdfReader.get()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}