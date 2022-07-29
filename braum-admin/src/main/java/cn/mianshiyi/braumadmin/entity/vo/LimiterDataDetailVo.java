package cn.mianshiyi.braumadmin.entity.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author shangqing.liu
 */
public class LimiterDataDetailVo {
    private String name;
    private List<String> times = new ArrayList<>();
    private Set<String> lineName = new HashSet<>();
    private List<LimiterDataDetailValue> data = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public Set<String> getLineName() {
        return lineName;
    }

    public void setLineName(Set<String> lineName) {
        this.lineName = lineName;
    }

    public List<LimiterDataDetailValue> getData() {
        return data;
    }

    public void setData(List<LimiterDataDetailValue> data) {
        this.data = data;
    }
}
