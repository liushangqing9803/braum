package cn.mianshiyi.braumadmin.entity.qo;

/**
 * @author shangqing.liu
 */
public class LimiterDataQo {
    private String limiterName;
    private String startTime;
    private Long start;
    private String endTime;
    private Long end;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLimiterName() {
        return limiterName;
    }

    public void setLimiterName(String limiterName) {
        this.limiterName = limiterName;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }
}
