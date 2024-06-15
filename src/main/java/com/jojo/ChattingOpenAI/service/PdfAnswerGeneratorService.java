package com.jojo.ChattingOpenAI.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PdfAnswerGeneratorService {
    @Autowired
    private final VectorStore vectorStore;
    private final ChatModel chatModel;

    public PdfAnswerGeneratorService(VectorStore vectorStore, ChatModel chatModel) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
    }

    // Generate answer based on query
    public String generateAnswer(String query) {
        // Perform similarity search in the vector store
        List<Document> similarDocuments = vectorStore.similaritySearch(query);
        // Collect content from similar documents
        String information = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));

        // Define system prompt template
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(
                "    You are a helpful assistant.\n" +
                        "    Use only the following information to answer the question.\n" +
                        "    Do not use any other information. If you do not know, simply answer: Unknown\n\n" +
                        "    {information}\n");

        // Create system message with the gathered information
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("information", information));

        // Define user prompt template
        PromptTemplate userPromptTemplate = new PromptTemplate("{query}");

        // Create user message with the query
        Message userMessage = userPromptTemplate.createMessage(Map.of("query", query));

        // Create prompt with both system and user messages
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        // Get the response from the AI model
        try {
            return chatModel.call(prompt).getResult().getOutput().getContent();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating answer.";
        }
    }
}
