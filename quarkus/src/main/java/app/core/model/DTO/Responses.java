package app.core.model.DTO;

import app.core.utils.BasicFunctions;

import java.util.List;

public class Responses {

    public Integer status;

    public Object data;

    public List<Object> datas;

    public List<String> messages;

    public Responses() {

    }

    public Responses(int i, Object data, List<Object> datas, List<String> messages) {
        this.status = i;
        this.data = data;
        this.datas = datas;
        this.messages = messages;

    }

    public Boolean hasMessages() {
        return BasicFunctions.isNotEmpty(this.messages);
    }
}