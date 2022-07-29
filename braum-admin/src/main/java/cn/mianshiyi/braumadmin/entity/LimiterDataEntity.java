package cn.mianshiyi.braumadmin.entity;

import java.util.Date;

/**
 * @author shangqing.liu
 */
public class LimiterDataEntity {

    /**
     *
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

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
    private Long execCount;

    /**
     * 限流数
     */
    private Long blockCount;

    /**
     * 创建时间
     */
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
}


