package cn.mianshiyi.braumadmin.entity;

import java.util.List;

public class ListResponse<T> {

    /**
     * 列表数据
     */
    private List<T> list;

    /**
     * 总数据量
     */
    private int totalCount;
    /**
     * 返回状态码
     */
    private int code;
    /**
     * 错误信息
     */
    private String errorMsg;

    public ListResponse(List<T> list, int totalCount) {
        this.list = list;
        this.totalCount = totalCount;
    }

    public ListResponse(List<T> list, int totalCount, int code) {
        this.list = list;
        this.totalCount = totalCount;
        this.code = code;
    }

    public ListResponse(List<T> list, int totalCount, int code, String errorMsg) {
        this.list = list;
        this.totalCount = totalCount;
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
