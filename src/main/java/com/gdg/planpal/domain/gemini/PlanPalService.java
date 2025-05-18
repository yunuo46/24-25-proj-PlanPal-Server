package com.gdg.planpal.domain.gemini;


import com.gdg.planpal.domain.gemini.functionCall.Spot.AddSpotList;
import com.gdg.planpal.domain.gemini.functionCall.Spot.SpotListRepo;
import com.gdg.planpal.domain.gemini.functionCall.schedule.*;
import com.gdg.planpal.domain.map.application.MapService;
import com.gdg.planpal.domain.schedule.domain.StarMapPinSchedule;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.*;
import com.google.cloud.vertexai.generativeai.*;
import com.google.protobuf.Struct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanPalService {


    @Value("${spring.ai.vertex.ai.gemini.location}")
    private String location;
    @Value("${spring.ai.vertex.ai.gemini.project-id}")
    private String projectId;
    @Value("${spring.ai.vertex.ai.gemini.chat.options.model}")
    private String modelId;

    private final SpotListRepo spotListRepo;
    private final AddSpotList addSpotList;
    private final GeminiRestService geminiRestService;
    private final ScheduleRepo scheduleRepo;
    private final AddSchedule addSchedule;
    private final MapService mapService;
    private Long mapId; ;
    public String chat(String userId,Long chatRoomId, String prompt) {



        VertexAI vertexAI = new VertexAI(projectId, location);
        String addSpotPrompt = """
                이는 여행을 계획하는 사용자의 대화 혹은 질문이야.
                우리는 이 사람의 여행 계획을 도와주는 서비스야\n
                우리는 "장소 리스트" 기능, "일정표 생성" 기능이 있어
                다음 대화에서 위 기능과 사용자의 요구를 충족하기 위해 어떤 정보가 더 필요할 것 같아?
                저장된 장소와 일정을 불러오기 
                그리고 google을 사용해 검색하기가 있어(왠만하면 검색 기능을 사용해서 정확한 정보(예를 들면 주소)를 알려줘
                되묻기 보다는 함수를 사용해줘
                영어로 대답해줘
                """;
        FunctionDeclaration spotRepoFunction = FunctionDeclaration.newBuilder()
                .setName("getSpotList")
                .setDescription("현재 사용자가 저장 중인 장소 가져오기")
                .setParameters(
                        Schema.newBuilder()
                                .setType(Type.OBJECT)
                                .build()
                )
                .build();
        FunctionDeclaration scheduleRepoFunction = FunctionDeclaration.newBuilder()
                .setName("getSchedule")
                .setDescription("현재 사용자의 일정표 가져오기")
                .setParameters(
                        Schema.newBuilder()
                                .setType(Type.OBJECT)
                                .build()
                )
                .build();
        FunctionDeclaration googleFunction = FunctionDeclaration.newBuilder()
                .setName("SearchOnGoogle")
                .setDescription("관련된 내용 중, 검색이 필요한 사항이 있다면 이 함수를 호출")
                .setParameters(
                        Schema.newBuilder()
                                .setType(Type.OBJECT)
                                .build()
                )
                .build();
        //FunctionDeclaration addSpotListFunction = addSpotList.getFunctionDeclaration();
        //FunctionDeclaration spotQuestionFunction = SpotQuestion.getFunctionDeclaration();

        /**
        **  1) 추가 정보 요청
         **/
        Tool tool = Tool.newBuilder()
                .addAllFunctionDeclarations(List.of(spotRepoFunction,scheduleRepoFunction,googleFunction))
                //.addAllFunctionDeclarations(List.of(spotRepoFunction,addSpotListFunction))
                //.addFunctionDeclarations(spotQuestionFunction)
                .build();

        GenerativeModel model = new GenerativeModel(modelId, vertexAI)
                .withTools(List.of(tool));
        ChatSession chat = model.startChat();

        GenerateContentResponse response=null;
        try {
            response = chat.sendMessage(addSpotPrompt+prompt);
            System.out.println("1차 응답: " + ResponseHandler.getContent(response));
        } catch (Exception e) {
            System.out.println("error(1)" + e);
        }
        if(response==null){
            return "ai 연결에 문제가 있습니다. ai 모델 응답이 없습니다";
        }
        /**
         * 2) 추가 정보 요청 처리
         */
        // SpotListRepo는 Spring bean이므로 Autowired 받는다고 가정
        String toolRecall = "";
        Content toolResponse=null;
        if (response!=null && ResponseHandler.getFunctionCalls(response).stream().anyMatch(fun -> fun.getName().equals("getSpotList"))){
            Map<Long, String> result=Map.of();
            try {
                result = spotListRepo.getSpotList(getMapId(chatRoomId));
//            toolResponse= ContentMaker.fromMultiModalData(
//                    PartMaker.fromFunctionResponse("getSpotList", Collections.singletonMap("current spot list", result.get("spot-address")))
//            );
            }catch (Exception e){
                log.error("get spot list error : "+e.getMessage());
            }
            System.out.println("List to string \n:"+result.toString());
            toolRecall+="users spot List"+result.toString();
        }

        if (response!=null &&ResponseHandler.getFunctionCalls(response).stream().anyMatch(fun -> fun.getName().equals("getSchedule"))){
            String result = "";

            try {
                result = scheduleRepo.getSchedule(getMapId(chatRoomId)).stream()
                        .map(StarMapPinSchedule::toString)
                        .reduce((s1, s2) -> s1 + "\n" + s2)
                        .orElse("일정 아직 없음");
            }catch(Exception e){
                log.error("get schedule error : "+e.getMessage());
            }

            System.out.println("List to string \n:"+result);
            toolRecall+="users schedule in trip"+result;
//            toolResponse= ContentMaker.fromMultiModalData(
//                    PartMaker.fromFunctionResponse("getSpotList", Collections.singletonMap("current spot list", result.get("spot-address")))
//            );
        }
        String searchResult=null;
        if (response!=null &&ResponseHandler.getFunctionCalls(response).stream().anyMatch(fun -> fun.getName().equals("SearchOnGoogle"))){
            System.out.println("\n\nGoogle Searching...\n\n");

            String googleSearchQuestion = """
                    The following is a conversation or question from a user who is planning a trip.
                    In this conversation, search for any content related to locations.
                    If the user asks directly about a place, or requests help finding related places,
                    list relevant information based on the place name, the exact address as it appears on Google Maps, and description. in English\n
                    """;
            try {
                searchResult = geminiRestService.chatWithDynamicSearch(googleSearchQuestion + prompt + toolRecall, 0.1).block();
            }catch(Exception e ){
                log.error("google search : "+ e.getMessage());
            }


            System.out.println("google search result"+searchResult);


        }


        /**
         * 2) 정보를 추가 해서 다시 요청
         */

        String finalPrompt= """
                This is a conversation between users planning a trip, followed by additional information such as stored data or search results. Your role is to help them by understanding their intentions and providing meaningful responses.
                
                Main Goals:
                - If users mention locations or places of interest, identify and extract them. Add them to the spot list when appropriate.
                - If users request a travel schedule or itinerary, help construct one based on the mentioned locations, times, or preferences.
                - When possible, call the relevant functions such as `addSpotList` or `addSchedule`. If you're not sure, provide helpful suggestions or explanations instead.
                - When user intent is unclear or partially expressed, use your best judgment to make reasonable assumptions and proceed accordingly.
                - If the conversation already includes schedule or location information, utilize it to assist the user, even if they didn’t give a direct command.
                - Don’t ask for additional clarification unless absolutely necessary. Prioritize action and helpfulness.
                - If you don’t have user data or function access, explain what would happen or give users advice based on the available information.
                
                Be helpful, flexible, and proactive — your goal is to support the trip planning experience smoothly.
                
                in english
                -----------
                """;

        GenerateContentResponse toolRequestResponse=null;

        FunctionDeclaration addSpotListFunction = addSpotList.getFunctionDeclaration();
        FunctionDeclaration addScheduleFunction = addSchedule.getFunctionDeclaration();
        Tool functionTool = Tool.newBuilder()
                //.addAllFunctionDeclarations(List.of(spotRepoFunction,spotQuestionFunction))
                .addAllFunctionDeclarations(List.of(addSpotListFunction,addScheduleFunction))
                //.addFunctionDeclarations(addSpotListFunction)
                .build();
        GenerativeModel functionModel = new GenerativeModel(modelId, vertexAI)
                .withTools(List.of(functionTool));
        ChatSession functionChat = functionModel.startChat();
        try {
            System.out.println("3차 요청: " +finalPrompt+":"+prompt+":"+toolRecall+":"+searchResult);
            toolRequestResponse = functionChat.sendMessage(finalPrompt+prompt+toolRecall+searchResult);
            System.out.println("3차 응답: " + ResponseHandler.getContent(toolRequestResponse));
        } catch (Exception e) {
            System.out.println("error(4)" + e);
        }

        /**
         * 함수 요청 처리
         */

        List<Part> parts = new ArrayList<>();
        if (toolRequestResponse!=null&&ResponseHandler.getFunctionCalls(toolRequestResponse).stream().anyMatch(fun -> fun.getName().equals("addSpotList"))){
            System.out.println("addSpotList called");
            Struct struct = ResponseHandler.getFunctionCalls(toolRequestResponse).stream().filter(fun -> fun.getName().equals("addSpotList")).findFirst().get()
                    .getArgs();
            try {
                addSpotList.addSpotList(userId, getMapId(chatRoomId), addSpotList.structToSpotList(struct));
            }catch(Exception e ){
                log.error("addSpotList error : "+ e.getMessage());
            }
            toolResponse= ContentMaker.fromMultiModalData(
                    PartMaker.fromFunctionResponse("addSpotList", Collections.emptyMap())
            );
            parts.add(PartMaker.fromFunctionResponse("addSpotList", Collections.singletonMap("status","success")));
        }

        if (toolRequestResponse!=null&&ResponseHandler.getFunctionCalls(toolRequestResponse).stream().anyMatch(fun -> fun.getName().equals("addSchedule"))){
            System.out.println("addSchedule");
            Struct struct = ResponseHandler.getFunctionCalls(toolRequestResponse).stream().filter(fun -> fun.getName().equals("addSchedule")).findFirst().get()
                    .getArgs();
            try {
                addSchedule.addSchedule(getMapId(chatRoomId), addSchedule.structToScheduleList(struct), userId);
            }catch(Exception e ){
                log.error("addSchedule error : "+ e.getMessage());
            }
            parts.add(PartMaker.fromFunctionResponse("addSchedule", Collections.singletonMap("status","success")));

        }
        if(parts.isEmpty()){
            return ResponseHandler.getText(toolRequestResponse);
        }
        toolResponse= ContentMaker
                .fromMultiModalData(parts.toArray(new Part[0]));


//        if(toolResponse==null){
//            return ResponseHandler.getText(toolRequestResponse);
//        }
        System.out.println("search result is null");
        try{
            GenerateContentResponse finalResponse = functionChat.sendMessage(toolResponse);
            System.out.println("result answer : \n"+ResponseHandler.getText(finalResponse));
            System.out.println("toolRequestResponse:"+ResponseHandler.getText(toolRequestResponse));
            return searchResult+ResponseHandler.getText(finalResponse);

        } catch (Exception e) {
            System.out.println("error(2)" + e);
        }

        return "error";
    }

    private Long getMapId(Long chatRoomId){
        if(this.mapId==null) {
            try {
                return mapService.getMapInfo(chatRoomId).id();
            } catch (RuntimeException e) {
                log.error("getMapId error : "+ e.getMessage());
                return null;
            }
        }
        return mapId;
    }
}
