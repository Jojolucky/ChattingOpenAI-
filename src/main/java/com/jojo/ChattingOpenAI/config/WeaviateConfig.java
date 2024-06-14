package com.jojo.ChattingOpenAI.config;
import io.weaviate.client.Config;
import io.weaviate.client.WeaviateAuthClient;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.auth.exception.AuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeaviateConfig {

//    @Value("${weaviate.url}")
//    private String weaviateUrl;
//
//    @Value("${weaviate.apiKey}")
//    private String weaviateApiKey;
//
//    public void checkWeaviate() throws AuthException {
//        Config config = new Config("https", weaviateUrl);
//        WeaviateClient client = WeaviateAuthClient.apiKey(config, weaviateApiKey);
//        Result<Boolean> result = client.misc().readyChecker().run();
//    }
}