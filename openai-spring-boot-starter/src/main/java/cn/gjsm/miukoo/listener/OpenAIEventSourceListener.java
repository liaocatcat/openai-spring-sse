package cn.gjsm.miukoo.listener;

import cn.gjsm.miukoo.pojos.ComletionResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Objects;

/**
 * 描述：OpenAIEventSourceListener
 *
 * @author https:www.unfbx.com
 * @date 2023-02-22
 */
@Slf4j
public class OpenAIEventSourceListener extends EventSourceListener {

    private SseEmitter sseEmitter;

    public OpenAIEventSourceListener(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOpen(EventSource eventSource, Response response) {
        log.info("OpenAI建立sse连接...");
        try {
            sseEmitter.send(SseEmitter.event()
                    .id("[DONE]")
                    .name("open")
                    .data("[DONE]")
                    .reconnectTime(3000));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        log.info("OpenAI返回数据：{}", data);
        if (data.equals("[DONE]")) {
            log.info("OpenAI返回数据结束了");
            sseEmitter.send(SseEmitter.event()
                    .id("[DONE]")
                    .name("close")
                    .data("[DONE]")
                    .reconnectTime(3000));
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        ComletionResponseBody completionResponse = mapper.readValue(data, ComletionResponseBody.class); // 读取Json
        JSONObject dataj = new JSONObject();
        dataj.put("msg",completionResponse.getChoices()[0].getDelta().containsKey("content")?completionResponse.getChoices()[0].getDelta().get("content"):"");
//        Thread.sleep(50);//可以去掉，加这个只是为了页面逐字打印效果更明显。
        sseEmitter.send(SseEmitter.event()
                .id(completionResponse.getId())
                .name("message")
                .data(JSONObject.toJSONString(dataj))
                .reconnectTime(3000));
    }


    @Override
    public void onClosed(EventSource eventSource) {
        log.info("OpenAI关闭sse连接...");
    }


    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        if(Objects.isNull(response)){
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            String resp=body.string();
            log.error("OpenAI  sse连接异常data：{}，异常：{}", resp, t);
            if(resp.contains("tokens")&&resp.contains("error")){
                sseEmitter.send(SseEmitter.event()
                    .id("[DONE]")
                    .name("close")
                    .data(resp.replace("\n",""))
                    .reconnectTime(3000));
            }else{
                sseEmitter.send(SseEmitter.event()
                        .id("[DONE]")
                        .name("error")
                        .data("[DONE]")
                        .reconnectTime(3000));
            }
        } else {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", response, t);
            sseEmitter.send(SseEmitter.event()
                    .id("[DONE]")
                    .name("error")
                    .data("[DONE]")
                    .reconnectTime(3000));
        }
        eventSource.cancel();
    }
}