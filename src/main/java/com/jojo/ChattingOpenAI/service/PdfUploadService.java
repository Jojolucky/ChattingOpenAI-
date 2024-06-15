package com.jojo.ChattingOpenAI.service;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import com.jojo.ChattingOpenAI.config.*;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfUploadService {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    public PdfUploadService(VectorStore vectorStore) {

        this.vectorStore = vectorStore;
    }

    // Convert MultipartFile to FileSystemResource
    private Resource convertMultiPartToFile(MultipartFile file) throws IOException {
        String resourcesPath = "src/main/resources/docs";
        File directory = new File(resourcesPath);
        if (!directory.exists()) {
            Files.createDirectories(Paths.get(resourcesPath));
        }

        File convFile = new File(directory, Objects.requireNonNull(file.getOriginalFilename()));
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

            // Save the processed result
            saveProcessedResult(textSplitter.apply(pdfReader.get()).toString(), pdfFile.getOriginalFilename());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to save the processed result to a file
    private void saveProcessedResult(String processedResult, String originalFilename) throws IOException {
        File resultFile = getProcessedResultFile(originalFilename);
        try (FileOutputStream fos = new FileOutputStream(resultFile)) {
            fos.write(processedResult.getBytes());
        }
    }

    // Get the file where the processed result should be saved
    public File getProcessedResultFile(String originalFilename) {
        String baseName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        String jsonFilename = baseName + ".json";
        Path path = Paths.get("src", "main", "resources", "data");
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String absolutePath = path.toFile().getAbsolutePath() + "/" + jsonFilename;
        return new File(absolutePath);
    }

}