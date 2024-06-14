//package com.jojo.ChattingOpenAI.controller;

import io.weaviate.client.v1.auth.exception.AuthException;
import com.jojo.ChattingOpenAI.config.WeaviateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;

//@RestController
//public class WeaviateController {

//    @Autowired
//    private WeaviateConfig weaviateConfig;
//
//    @GetMapping("/checkWeaviate")
//    public String checkWeaviate() throws AuthException {
//        weaviateConfig.checkWeaviate();
//        return "Check completed";
//    }
//}
