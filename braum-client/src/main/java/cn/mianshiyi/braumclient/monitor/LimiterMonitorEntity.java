package cn.mianshiyi.braumclient.monitor;

import java.util.Date;

/**
 * @author shangqing.liu
 */
public class LimiterMonitorEntity {

    /**
     * 名称
     */
    private String name;
    /**
     * 限流类型 local、dist
     */
    private String limitType;
    /**
     * 上报机器ip
     */
    private String clientIp;

    /**
     * 发生时间-秒数
     */
    private Long occSecond;

    /**
     * 总数
     */
    private Long execCount = 0L;

    /**
     * 限流数
     */
    private Long blockCount = 0L;

    /**
     * 创建时间
     */
    private Date createTime;

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOccSecond() {
        return occSecond;
    }

    public void setOccSecond(Long occSecond) {
        this.occSecond = occSecond;
    }

    public Long getExecCount() {
        return execCount;
    }

    public void setExecCount(Long execCount) {
        this.execCount = execCount;
    }

    public Long getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(Long blockCount) {
        this.blockCount = blockCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}


