package cn.mianshiyi.braumadmin.entity.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shangqing.liu
 */
public class LimiterDataDetailValue {
    private String name;
    private String type = "line";
    private List<String> data = new ArrayList<>();

    public LimiterDataDetailValue(String name) {
        this.name = name;
    }

    public LimiterDataDetailValue() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
