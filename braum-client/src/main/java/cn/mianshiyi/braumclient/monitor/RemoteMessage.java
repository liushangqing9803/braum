package cn.mianshiyi.braumclient.monitor;

import java.io.Serializable;

public class RemoteMessage<T> implements Serializable {
    private static final long serialVersionUID = 5241526151768786391L;
    private int code;
    private T data;

    public RemoteMessage() {
    }

    public RemoteMessage(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
