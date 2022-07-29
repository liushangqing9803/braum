package cn.mianshiyi.braumclient.monitor;


/**
 * @author shangqing.liu
 */
public class LimiterMonitorRegisterEntity {

    /**
     * 名称
     */
    private String name;

    public LimiterMonitorRegisterEntity() {
    }

    public LimiterMonitorRegisterEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


