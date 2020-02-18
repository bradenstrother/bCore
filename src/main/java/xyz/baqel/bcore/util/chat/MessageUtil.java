package xyz.baqel.bcore.util.chat;

import com.google.common.collect.Maps;

import java.util.Map;

public class MessageUtil {
    private Map<String, String> variableMap;

    public MessageUtil() {
        this.variableMap = Maps.newHashMap();
    }

    public MessageUtil setVariable(final String variable, final String value) {
        if (variable != null && !variable.isEmpty()) {
            this.variableMap.put(variable, value);
        }
        return this;
    }

    public String format(String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }
        for (final Map.Entry<String, String> entry : this.variableMap.entrySet()) {
            final String s = entry.getKey();
            final String s2 = entry.getValue();
            message = message.replace(s, s2);
        }
        this.variableMap.clear();
        return ColorText.translate(message);
    }
}
