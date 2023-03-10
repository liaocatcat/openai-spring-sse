### 项目介绍

#### 介绍
关于chatGPT的东拼西凑，内含三个调用版本的冗余代码，现在运行的是最新的

启动模块在openai-starter-test,配置也是

各种调用方法demo可查看openai-spring-boot-starter模块中的工具类 src\main\java\cn\gjsm\miukoo\utils\OpenAiUtils.java



### 使用步骤

#### 1、导入依赖


#### 2、配置秘钥

在application.yml中配置如下参数：

```yml
openai:
  token: 你的秘钥
  timeout: 5000 // 超时时间
```

