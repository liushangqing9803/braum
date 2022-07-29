package cn.mianshiyi.braumclient.monitor;


/**
 * @author shangqing.liu
 */
public class LimiterMonitorValue {

    /**
     * 名称
     */
    private String name;
    /**
     * 是否通过
     */
    private boolean acquire;

    public LimiterMonitorValue() {

    }

    public LimiterMonitorValue(String name, boolean acquire) {
        this.name = name;
        this.acquire = acquire;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAcquire() {
        return acquire;
    }

    public void setAcquire(boolean acquire) {
        this.acquire = acquire;
    }
}


