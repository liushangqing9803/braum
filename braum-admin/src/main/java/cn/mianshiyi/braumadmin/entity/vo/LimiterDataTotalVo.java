package cn.mianshiyi.braumadmin.entity.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shangqing.liu
 */
public class LimiterDataTotalVo {
    private String name;
    private List<String> times = new ArrayList<>();
    private List<String> limiterTotal = new ArrayList<>();
    private List<String> limiterBlock = new ArrayList<>();

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

    public List<String> getLimiterTotal() {
        return limiterTotal;
    }

    public void setLimiterTotal(List<String> limiterTotal) {
        this.limiterTotal = limiterTotal;
    }

    public List<String> getLimiterBlock() {
        return limiterBlock;
    }

    public void setLimiterBlock(List<String> limiterBlock) {
        this.limiterBlock = limiterBlock;
    }
}
