package cn.gjsm.miukoo.controller;

import cn.gjsm.miukoo.listener.OpenAIEventSourceListener;
import cn.gjsm.miukoo.pojos.OpenAi;
import cn.gjsm.miukoo.utils.OpenAiStreamClient;
import cn.gjsm.miukoo.utils.OpenAiUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.unfbx.chatgpt.entity.chat.Message;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class OpenAIController {
    @GetMapping("/sse")
    @CrossOrigin
    public SseEmitter sseEmitter(@RequestParam("uid") String uid) throws IOException {
        //创建流式输出客户端
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10809));
        OpenAiStreamClient openAiStreamClient = OpenAiStreamClient.builder()
                .connectTimeout(50)
                .readTimeout(50)
                .writeTimeout(50)
                .apiKey(OpenAiUtils.OPENAPI_TOKEN)
//                .proxy(proxy)
                .build();
        SseEmitter sseEmitter = new SseEmitter(-1L);
        sseEmitter.send(SseEmitter.event().id("root").name("连接成功！！！！").data(LocalDateTime.now()).reconnectTime(3000));
        sseEmitter.onCompletion(() -> {
            System.out.println(LocalDateTime.now() + ", uid#" + uid + ", on completion");
        });
        sseEmitter.onTimeout(() -> System.out.println(LocalDateTime.now() + ", uid#" + uid + ", on timeout#" + sseEmitter.getTimeout()));
        sseEmitter.onError(throwable -> System.out.println(LocalDateTime.now() + ", uid#" + uid + ", on error#" + throwable.toString()));
        OpenAIEventSourceListener openAIEventSourceListener = new OpenAIEventSourceListener(sseEmitter);
//        openAiStreamClient.streamCompletions("写一句话描述下开心的心情",openAIEventSourceListener);
        openAiStreamClient.streamChatCompletion(OpenAiUtils.userMessge.get(uid), openAIEventSourceListener);
        return sseEmitter;
    }
    @PostMapping("/completions")
    @CrossOrigin
    @ResponseBody
    public JSONObject completions(@RequestBody JSONObject body) throws Exception {
        JSONObject jsonObject = new JSONObject();
        OpenAi openAi = OpenAiUtils.PARMS.get(body.get("model"));
        switch (openAi.getId()){
            case "OpenAi50":
                jsonObject.put("choices",OpenAiUtils.toCreateImg(body.get("prompt").toString()));
//                System.out.println(jsonObject.get("choices"));
                break;
            default:
//                String[] str=openAi.getPrompt().split("%s");
//                String postStr = "";
//                if(str!=null&&str.length<=2){
//                    postStr = String.format(openAi.getPrompt(), body.get("prompt").toString());
//                }else{
//                    String[] strs = body.get("prompt").toString().split("；");
//                    postStr = String.format(openAi.getPrompt(),strs[0],strs[1]);
//                }
//                List<CompletionChoice> questionAnswer = OpenAiUtils.getAiResult(openAi,postStr);
//                StringBuffer choices = new StringBuffer();
//                for (CompletionChoice completionChoice : questionAnswer) {
//                    System.out.println(completionChoice.getText());
//                    choices.append(completionChoice.getText()+"\n");
//                }
//                jsonObject.put("choices",choices);

//                jsonObject = OpenAiUtils.chat(body);
                JSONArray jsonArray = body.getJSONArray("messages");
                List<Message> messages = new ArrayList<>();
                jsonArray.forEach(obj -> {
                    messages.add(BeanUtil.copyProperties(obj,Message.class));
                });
                jsonObject.put("uid", IdUtil.fastSimpleUUID());
                OpenAiUtils.userMessge.put(jsonObject.getString("uid"),messages);
                break;
        }
        return jsonObject;
    }
    @PostMapping("/models")
    @CrossOrigin
    @ResponseBody
    public JSONObject models() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        List<OpenAi> list = new ArrayList();
        OpenAiUtils.PARMS.values().forEach(a->{
            list.add(a);
        });
        list.sort(Comparator.comparing(OpenAi::getId));;
        jsonObject.put("models",list);
        return jsonObject;
    }
}
