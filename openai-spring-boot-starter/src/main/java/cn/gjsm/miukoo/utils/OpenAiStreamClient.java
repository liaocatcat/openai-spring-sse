package cn.gjsm.miukoo.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Proxy;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OpenAiStreamClient {
    private static final Logger log = LoggerFactory.getLogger(OpenAiStreamClient.class);
    private @NotNull String apiKey;
    private OkHttpClient okHttpClient;
    private long connectTimeout;
    private long writeTimeout;
    private long readTimeout;
    private Proxy proxy;

    public OpenAiStreamClient(String apiKey, long connectTimeout, long writeTimeout, long readTimeout, Proxy proxy) {
        this.apiKey = apiKey;
        this.connectTimeout = connectTimeout;
        this.writeTimeout = writeTimeout;
        this.readTimeout = readTimeout;
        this.proxy = proxy;
        this.okHttpClient(connectTimeout, writeTimeout, readTimeout, proxy);
    }

    public OpenAiStreamClient(String apiKey, long connectTimeout, long writeTimeout, long readTimeout) {
        this.apiKey = apiKey;
        this.connectTimeout = connectTimeout;
        this.writeTimeout = writeTimeout;
        this.readTimeout = readTimeout;
        this.okHttpClient(connectTimeout, writeTimeout, readTimeout, (Proxy)null);
    }

    public OpenAiStreamClient(String apiKey) {
        this.apiKey = apiKey;
        this.okHttpClient(30L, 30L, 30L, (Proxy)null);
    }

    public OpenAiStreamClient(String apiKey, Proxy proxy) {
        this.apiKey = apiKey;
        this.proxy = proxy;
        this.okHttpClient(30L, 30L, 30L, proxy);
    }

    private OpenAiStreamClient(OpenAiStreamClient.Builder builder) {
        if (StrUtil.isBlank(builder.apiKey)) {
            throw new BaseException(CommonError.API_KEYS_NOT_NUL);
        } else {
            this.apiKey = builder.apiKey;
            if (Objects.isNull(builder.connectTimeout)) {
                builder.connectTimeout(30L);
            }

            this.connectTimeout = builder.connectTimeout;
            if (Objects.isNull(builder.writeTimeout)) {
                builder.writeTimeout(30L);
            }

            this.writeTimeout = builder.writeTimeout;
            if (Objects.isNull(builder.readTimeout)) {
                builder.readTimeout(30L);
            }

            this.readTimeout = builder.readTimeout;
            this.proxy = builder.proxy;
            this.okHttpClient(this.connectTimeout, this.writeTimeout, this.readTimeout, this.proxy);
        }
    }

    private void okHttpClient(long connectTimeout, long writeTimeout, long readTimeout, Proxy proxy) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        client.writeTimeout(writeTimeout, TimeUnit.SECONDS);
        client.readTimeout(readTimeout, TimeUnit.SECONDS);
        if (Objects.nonNull(proxy)) {
            client.proxy(proxy);
        }

        this.okHttpClient = client.build();
    }

    public void streamCompletions(Completion completion, EventSourceListener eventSourceListener) {
        if (Objects.isNull(eventSourceListener)) {
            log.error("参数异常：EventSourceListener不能为空，可以参考：com.unfbx.chatgpt.sse.ConsoleEventSourceListener");
            throw new BaseException(CommonError.PARAM_ERROR);
        } else if (StrUtil.isBlank(completion.getPrompt())) {
            log.error("参数异常：Prompt不能为空");
            throw new BaseException(CommonError.PARAM_ERROR);
        } else {
            if (!completion.isStream()) {
                completion.setStream(true);
            }

            try {
                EventSource.Factory factory = EventSources.createFactory(this.okHttpClient);
                ObjectMapper mapper = new ObjectMapper();
                String requestBody = mapper.writeValueAsString(completion);
                Request request = (new Request.Builder()).url("https://api.liaocatcat.xyz/v1/completions").post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody)).header("Authorization", "Bearer " + this.apiKey).build();
                factory.newEventSource(request, eventSourceListener);
            } catch (JsonProcessingException var8) {
                log.error("请求参数解析异常：{}", var8);
                var8.printStackTrace();
            } catch (Exception var9) {
                log.error("请求参数解析异常：{}", var9);
                var9.printStackTrace();
            }

        }
    }

    public void streamCompletions(String question, EventSourceListener eventSourceListener) {
        Completion q = Completion.builder().prompt(question).stream(true).build();
        this.streamCompletions(q, eventSourceListener);
    }

    public void streamChatCompletion(ChatCompletion chatCompletion, EventSourceListener eventSourceListener) {
        if (Objects.isNull(eventSourceListener)) {
            log.error("参数异常：EventSourceListener不能为空，可以参考：com.unfbx.chatgpt.sse.ConsoleEventSourceListener");
            throw new BaseException(CommonError.PARAM_ERROR);
        } else {
            if (!chatCompletion.isStream()) {
                chatCompletion.setStream(true);
            }

            try {
                EventSource.Factory factory = EventSources.createFactory(this.okHttpClient);
                ObjectMapper mapper = new ObjectMapper();
                String requestBody = mapper.writeValueAsString(chatCompletion);
                Request request = (new Request.Builder()).url("https://api.liaocatcat.xyz/v1/chat/completions").post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody)).header("Authorization", "Bearer " + this.apiKey).build();
                factory.newEventSource(request, eventSourceListener);
            } catch (JsonProcessingException var8) {
                log.error("请求参数解析异常：{}", var8);
                var8.printStackTrace();
            } catch (Exception var9) {
                log.error("请求参数解析异常：{}", var9);
                var9.printStackTrace();
            }

        }
    }

    public void streamChatCompletion(List<Message> messages, EventSourceListener eventSourceListener) {
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(messages).stream(true).build();
        this.streamChatCompletion(chatCompletion, eventSourceListener);
    }

    public static OpenAiStreamClient.Builder builder() {
        return new OpenAiStreamClient.Builder();
    }

    public @NotNull String getApiKey() {
        return this.apiKey;
    }

    public OkHttpClient getOkHttpClient() {
        return this.okHttpClient;
    }

    public long getConnectTimeout() {
        return this.connectTimeout;
    }

    public long getWriteTimeout() {
        return this.writeTimeout;
    }

    public long getReadTimeout() {
        return this.readTimeout;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public static final class Builder {
        private @NotNull String apiKey;
        private long connectTimeout;
        private long writeTimeout;
        private long readTimeout;
        private Proxy proxy;

        public Builder() {
        }

        public OpenAiStreamClient.Builder apiKey(@NotNull String val) {
            this.apiKey = val;
            return this;
        }

        public OpenAiStreamClient.Builder connectTimeout(long val) {
            this.connectTimeout = val;
            return this;
        }

        public OpenAiStreamClient.Builder writeTimeout(long val) {
            this.writeTimeout = val;
            return this;
        }

        public OpenAiStreamClient.Builder readTimeout(long val) {
            this.readTimeout = val;
            return this;
        }

        public OpenAiStreamClient.Builder proxy(Proxy val) {
            this.proxy = val;
            return this;
        }

        public OpenAiStreamClient build() {
            return new OpenAiStreamClient(this);
        }
    }
}
