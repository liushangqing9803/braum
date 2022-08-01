package cn.mianshiyi.braumadmin.entity.qo;

/**
 * @author shangqing.liu
 */
public class LimiterRegisterDataQo {

    private String limiterName;

    /**
     * 偏移量
     * 页数
     */
    private int offset;

    /**
     * 查询数量
     */
    private int limit;

    public String getLimiterName() {
        return limiterName;
    }

    public void setLimiterName(String limiterName) {
        this.limiterName = limiterName;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
