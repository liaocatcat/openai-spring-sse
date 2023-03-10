package cn.gjsm.miukoo.pojos;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unfbx.chatgpt.entity.common.OpenAiResponse;
import com.unfbx.chatgpt.entity.common.Usage;

import java.io.Serializable;
import java.util.Arrays;

//{"id":"chatcmpl-6repavmy8H19QHC1mP6TXH9C2R0U9","object":"chat.completion.chunk",
//"created":1678246358,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{"role":"assistant"},"index":0,"finish_reason":null}]}
public class ComletionResponseBody  extends OpenAiResponse {
        private String id;
        private String object;
        private long created;
        private String model;
        private Choice[] choices;
        private Usage usage;

    public static class Choice implements Serializable {

        private JSONObject delta;
        private String text;
        private long index;
        private Object logprobs;
        @JsonProperty("finish_reason")
        private String finishReason;

        public Choice() {
        }

        public JSONObject getDelta() {
            return delta;
        }

        public void setDelta(JSONObject delta) {
            this.delta = delta;
        }

        public String getText() {
            return this.text;
        }

        public long getIndex() {
            return this.index;
        }

        public Object getLogprobs() {
            return this.logprobs;
        }

        public String getFinishReason() {
            return this.finishReason;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setIndex(long index) {
            this.index = index;
        }

        public void setLogprobs(Object logprobs) {
            this.logprobs = logprobs;
        }

        @JsonProperty("finish_reason")
        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Choice)) {
                return false;
            } else {
                Choice other = (Choice)o;
                if (!other.canEqual(this)) {
                    return false;
                } else if (this.getIndex() != other.getIndex()) {
                    return false;
                } else {
                    label49: {
                        Object this$text = this.getText();
                        Object other$text = other.getText();
                        if (this$text == null) {
                            if (other$text == null) {
                                break label49;
                            }
                        } else if (this$text.equals(other$text)) {
                            break label49;
                        }

                        return false;
                    }

                    Object this$logprobs = this.getLogprobs();
                    Object other$logprobs = other.getLogprobs();
                    if (this$logprobs == null) {
                        if (other$logprobs != null) {
                            return false;
                        }
                    } else if (!this$logprobs.equals(other$logprobs)) {
                        return false;
                    }

                    Object this$finishReason = this.getFinishReason();
                    Object other$finishReason = other.getFinishReason();
                    if (this$finishReason == null) {
                        if (other$finishReason != null) {
                            return false;
                        }
                    } else if (!this$finishReason.equals(other$finishReason)) {
                        return false;
                    }

                    return true;
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof Choice;
        }

        public int hashCode() {
            boolean PRIME = true;
            int result = 1;
            long $index = this.getIndex();
            result = result * 59 + (int)($index >>> 32 ^ $index);
            Object $text = this.getText();
            result = result * 59 + ($text == null ? 43 : $text.hashCode());
            Object $logprobs = this.getLogprobs();
            result = result * 59 + ($logprobs == null ? 43 : $logprobs.hashCode());
            Object $finishReason = this.getFinishReason();
            result = result * 59 + ($finishReason == null ? 43 : $finishReason.hashCode());
            return result;
        }

        public String toString() {
            return "Choice(text=" + this.getText() + ", index=" + this.getIndex() + ", logprobs=" + this.getLogprobs() + ", finishReason=" + this.getFinishReason() + ")";
        }
    }
    public ComletionResponseBody() {
    }

        public String getId() {
            return this.id;
        }

        public String getObject() {
            return this.object;
        }

        public long getCreated() {
            return this.created;
        }

        public String getModel() {
            return this.model;
        }

        public Choice[] getChoices() {
            return this.choices;
        }

        public Usage getUsage() {
            return this.usage;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public void setCreated(long created) {
            this.created = created;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public void setChoices(Choice[] choices) {
            this.choices = choices;
        }

        public void setUsage(Usage usage) {
            this.usage = usage;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof ComletionResponseBody)) {
                return false;
            } else {
                ComletionResponseBody other = (ComletionResponseBody)o;
                if (!other.canEqual(this)) {
                    return false;
                } else if (this.getCreated() != other.getCreated()) {
                    return false;
                } else {
                    label65: {
                        Object this$id = this.getId();
                        Object other$id = other.getId();
                        if (this$id == null) {
                            if (other$id == null) {
                                break label65;
                            }
                        } else if (this$id.equals(other$id)) {
                            break label65;
                        }

                        return false;
                    }

                    Object this$object = this.getObject();
                    Object other$object = other.getObject();
                    if (this$object == null) {
                        if (other$object != null) {
                            return false;
                        }
                    } else if (!this$object.equals(other$object)) {
                        return false;
                    }

                    Object this$model = this.getModel();
                    Object other$model = other.getModel();
                    if (this$model == null) {
                        if (other$model != null) {
                            return false;
                        }
                    } else if (!this$model.equals(other$model)) {
                        return false;
                    }

                    if (!Arrays.deepEquals(this.getChoices(), other.getChoices())) {
                        return false;
                    } else {
                        Object this$usage = this.getUsage();
                        Object other$usage = other.getUsage();
                        if (this$usage == null) {
                            if (other$usage != null) {
                                return false;
                            }
                        } else if (!this$usage.equals(other$usage)) {
                            return false;
                        }

                        return true;
                    }
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof ComletionResponseBody;
        }

        public int hashCode() {
            boolean PRIME = true;
            int result = 1;
            long $created = this.getCreated();
            result = result * 59 + (int)($created >>> 32 ^ $created);
            Object $id = this.getId();
            result = result * 59 + ($id == null ? 43 : $id.hashCode());
            Object $object = this.getObject();
            result = result * 59 + ($object == null ? 43 : $object.hashCode());
            Object $model = this.getModel();
            result = result * 59 + ($model == null ? 43 : $model.hashCode());
            result = result * 59 + Arrays.deepHashCode(this.getChoices());
            Object $usage = this.getUsage();
            result = result * 59 + ($usage == null ? 43 : $usage.hashCode());
            return result;
        }

        public String toString() {
            return "ComletionResponseBody(id=" + this.getId() + ", object=" + this.getObject() + ", created=" + this.getCreated() + ", model=" + this.getModel() + ", choices=" + Arrays.deepToString(this.getChoices()) + ", usage=" + this.getUsage() + ")";
        }
}
