package com.gdg.planpal.domain.gemini;


import com.google.auth.oauth2.GoogleCredentials;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class GeminiRestService {
    private static final String BASE_URL = "https://{location}-aiplatform.googleapis.com";
    private static final String ENDPOINT = "/v1beta1/projects/{project}/locations/{location}/publishers/google/models/{model}:generateContent";

    private final WebClient webClient;

    @Value("${spring.ai.vertex.ai.gemini.api-key}")
    private String apiKey;
    //@Value("${spring.ai.vertex.ai.gemini.location}")
    private String location="us-central1";
    @Value("${spring.ai.vertex.ai.gemini.project-id}")
    private String projectId;

    private String modelId="gemini-2.0-flash";

    public GeminiRestService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(BASE_URL).build();
    }

    public Mono<String> chatWithDynamicSearch(String prompt,
                                              double dynamicThreshold) {

        String accessToken="";
        try {
            ClassPathResource resource = new ClassPathResource("gemini-ai-455106-770953e73198.json");

            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(resource.getInputStream())
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));

            credentials.refreshIfExpired();
            accessToken= credentials.getAccessToken().getTokenValue();
        }catch (Exception e){
            System.out.println("credential process has error"+e);
            return Mono.just("google cloud auth error: ");
        }
        System.out.println("token:"+accessToken);

        // tools 섹션에 googleSearchRetrieval + dynamicRetrievalConfig 추가


        Map<String, Object> dynamicConfig = Map.of(
                "mode", "MODE_DYNAMIC"
        );
        Map<String, Object> googleSearchRetrieval = Map.of(
                "dynamicRetrievalConfig", dynamicConfig
        );

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(Map.of("text", prompt))
                        )
                ),
                //"tools", List.of(Map.of("googleSearchRetrieval", googleSearchRetrieval)),
                //"tools", List.of(Map.of("googleSearch", "{}")),
                "tools", List.of(
                        Map.of("google_search", new HashMap<>()) // or Collections.emptyMap()
                ),
                // model 필드는 full resource name
                "model", String.format(
                        "projects/%s/locations/%s/publishers/google/models/%s",
                        projectId, location, modelId
                )
        );



        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(location + "-aiplatform.googleapis.com")
                        .path("/v1beta1/projects/{project}/locations/{location}/publishers/google/models/{model}:generateContent")
                        //.queryParam("key", apiKey)
                        .build(projectId, location, modelId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            System.err.println("에러 발생: " + errorBody); // or log.error(...)
                            return Mono.error(new RuntimeException("API 호출 실패: " + errorBody));
                        })
                )
                .bodyToMono(JsonNode.class)
                .map(json -> {
                    JsonNode candidates = json.path("candidates");
                    if (candidates.isArray() && candidates.size() > 0) {
                        return candidates.get(0)
                                .path("content")
                                .path("parts")
                                .get(0)
                                .path("text")
                                .asText();
                    }
                    return "";
                });
    }
}
