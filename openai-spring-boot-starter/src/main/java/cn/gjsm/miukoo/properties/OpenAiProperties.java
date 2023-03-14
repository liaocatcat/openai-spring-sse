package cn.gjsm.miukoo.properties;

import cn.gjsm.miukoo.utils.OpenAiUtils;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "openai")
public class OpenAiProperties implements InitializingBean {
    // 秘钥
    String token;
    String domain;
    // 超时时间
    Integer timeout;

    // 设置属性时同时设置给OpenAiUtils
    @Override
    public void afterPropertiesSet() throws Exception {
        OpenAiUtils.OPENAPI_TOKEN = token;
        OpenAiUtils.DOMAIN = domain;
        OpenAiUtils.TIMEOUT = timeout;
    }
}
