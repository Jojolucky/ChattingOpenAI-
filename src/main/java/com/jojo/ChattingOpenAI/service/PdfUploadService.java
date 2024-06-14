package com.jojo.ChattingOpenAI.service;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
//import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
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

//    private final ChatClient aiClient;
    @Autowired
    private VectorStore vectorStore;
//    private final PdfFileReader pdfFileReader;
    private final ChatModel aiModel;

    @Autowired
    public PdfUploadService(VectorStore vectorStore, ChatModel aiModel) {

//        this.aiClient = aiClient;
        this.vectorStore = vectorStore;
//        this.pdfFileReader = pdfFileReader;
        this.aiModel = aiModel;
    }

    // convert dpf
//    private Resource convertMultiPartToFile(MultipartFile file) throws IOException {
//        String var10002 = System.getProperty("java.io.tmpdir");
//        File convFile = new File(var10002 + "/" + file.getOriginalFilename());
//        Throwable var3 = null;
//        Object var4 = null;
//
//        try {
//            FileOutputStream fos = new FileOutputStream(convFile);
//
//            try {
//                fos.write(file.getBytes());
//            } finally {
//                if (fos != null) {
//                    fos.close();
//                }
//            }
//        } catch (Throwable var11) {
//            if (var3 == null) {
//                var3 = var11;
//            } else if (var3 != var11) {
//                var3.addSuppressed(var11);
//            }
//
//            try {
//                throw var3;
//            } catch (Throwable e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return new FileSystemResource(convFile);
//    }

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

//    // chunk pdf
//    public void processPDF(MultipartFile pdfFile) {
//        try {
//            Resource convFile = this.convertMultiPartToFile(pdfFile);
//            PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder().withPageExtractedTextFormatter((new ExtractedTextFormatter.Builder()).build()).build();
//            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(convFile, config);
//            TokenTextSplitter textSplitter = new TokenTextSplitter();
//            this.vectorStore.accept(textSplitter.apply(pdfReader.get()));
//        } catch (Exception var6) {
//            var6.printStackTrace();
//        }
//    }
//
//    // create answer
//    public String generateAnswer(String query) {
////        List<Document> similarDocuments = this.vectorStore.similaritySearch(query);
////        System.out.println(similarDocuments);
////        String information = similarDocuments.stream()
////                .map(Document::getContent)
////                .collect(Collectors.joining(System.lineSeparator()));
////        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(
////                "    You are a helpful assistant.\n    Use only the following information to answer the question.\n    Do not use any other information. If you do not know, simply answer: Unknown.\n\n    {information}\n");
////        Message systemMessage = systemPromptTemplate.createMessage(Map.of("information", information));
////        PromptTemplate userPromptTemplate = new PromptTemplate("{query}");
////        Message userMessage = userPromptTemplate.createMessage(Map.of("query", query));
////        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
////        return aiModel.call(prompt).getResult().getOutput().getContent();
//        try {
//            // Perform similarity search in the vector store
//            List<Document> similarDocuments = vectorStore.similaritySearch(query);
//            System.out.println(similarDocuments);
//
//            // Collect content from similar documents
//            String information = similarDocuments.stream()
//                    .map(Document::getContent)
//                    .collect(Collectors.joining(System.lineSeparator()));
//
//            // Define system prompt template
//            SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(
//                    "    You are a helpful assistant.\n" +
//                            "    Use only the following information to answer the question.\n" +
//                            "    Do not use any other information. If you do not know, simply answer: Unknown.\n\n" +
//                            "    {information}\n");
//
//            // Create system message with the gathered information
//            Message systemMessage = systemPromptTemplate.createMessage(Map.of("information", information));
//
//            // Define user prompt template
//            PromptTemplate userPromptTemplate = new PromptTemplate("{query}");
//
//            // Create user message with the query
//            Message userMessage = userPromptTemplate.createMessage(Map.of("query", query));
//
//            // Create prompt with both system and user messages
//            Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
//
//            // Get the response from the AI model
//            return aiModel.call(prompt).getResult().getOutput().getContent();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error generating answer.";
//        }
//    }
}
