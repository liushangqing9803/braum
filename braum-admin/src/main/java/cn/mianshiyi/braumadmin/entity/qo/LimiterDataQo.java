package cn.mianshiyi.braumadmin.entity.qo;

/**
 * @author shangqing.liu
 */
public class LimiterDataQo {
    private String name;
    private Long start;
    private Long end;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
