//package com.jojo.ChattingOpenAI.service;
//
//import org.springframework.ai.reader.ExtractedTextFormatter;
//import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
//import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
//import org.springframework.ai.transformer.splitter.TokenTextSplitter;
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//@Component
//public class PdfFileReader {
//    @Autowired
//    private final VectorStore vectorStore;
//
//    @Autowired
//    public PdfFileReader(VectorStore vectorStore) {
//
//        this.vectorStore = vectorStore;
//    }
//}
//
