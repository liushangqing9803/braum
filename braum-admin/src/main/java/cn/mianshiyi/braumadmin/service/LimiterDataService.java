package cn.mianshiyi.braumadmin.service;

import cn.mianshiyi.braumadmin.entity.LimiterDataEntity;
import cn.mianshiyi.braumadmin.entity.qo.LimiterDataQo;
import cn.mianshiyi.braumadmin.entity.vo.LimiterDataView;

import java.util.List;

/**
 * @author shangqing.liu
 */
public interface LimiterDataService {

    LimiterDataView findByQo(LimiterDataQo qo);

    LimiterDataEntity findByNameAndTime(String name , Long time);

    /**
     * 添加
     * @return int 插入数量
     */
    int insert(LimiterDataEntity limiterDataEntity);

    /**
     * 批量插入信息
     * @return int 插入数量
     */
    int insertBatch(List<LimiterDataEntity> listEntity);
}
