package cn.mianshiyi.braumadmin.entity.vo;

/**
 * @author shangqing.liu
 */
public class LimiterDataView {
    private LimiterDataTotalVo limiterDataTotalVo;
    private LimiterDataDetailVo limiterDataDetailVo;

    public LimiterDataTotalVo getLimiterDataTotalVo() {
        return limiterDataTotalVo;
    }

    public void setLimiterDataTotalVo(LimiterDataTotalVo limiterDataTotalVo) {
        this.limiterDataTotalVo = limiterDataTotalVo;
    }

    public LimiterDataDetailVo getLimiterDataDetailVo() {
        return limiterDataDetailVo;
    }

    public void setLimiterDataDetailVo(LimiterDataDetailVo limiterDataDetailVo) {
        this.limiterDataDetailVo = limiterDataDetailVo;
    }
}
