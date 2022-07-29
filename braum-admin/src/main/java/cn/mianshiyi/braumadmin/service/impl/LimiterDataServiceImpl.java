package cn.mianshiyi.braumadmin.service.impl;

import cn.mianshiyi.braumadmin.entity.LimiterDataEntity;
import cn.mianshiyi.braumadmin.entity.qo.LimiterDataQo;
import cn.mianshiyi.braumadmin.entity.vo.LimiterDataVo;
import cn.mianshiyi.braumadmin.mapper.LimiterDataMapper;
import cn.mianshiyi.braumadmin.service.LimiterDataService;
import cn.mianshiyi.braumadmin.utils.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author shangqing.liu
 */
@Service
public class LimiterDataServiceImpl implements LimiterDataService {

    @Resource
    private LimiterDataMapper limiterDataMapper;

    @Override
    public LimiterDataVo findByQo(LimiterDataQo qo) {
        List<LimiterDataEntity> byQo = limiterDataMapper.findByQo(qo);
        if (CollectionUtils.isEmpty(byQo)) {
            return null;
        }
        Map<Long, LimiterDataEntity> collect = byQo.stream().collect(Collectors.toMap(LimiterDataEntity::getOccSecond, Function.identity(), (key1, key2) -> key2));
        LimiterDataVo vo = new LimiterDataVo();
        vo.setName(qo.getName());
        Long start = qo.getStart();
        Long end = qo.getEnd();

        while (end >= start) {
            vo.getTimes().add(DateUtil.format(new Date(start * 1000)));
            LimiterDataEntity limiterDataEntity = collect.get(start);
            if (limiterDataEntity != null) {
                vo.getLimiterTotal().add(String.valueOf(limiterDataEntity.getExecCount()));
                vo.getLimiterBlock().add(String.valueOf(limiterDataEntity.getBlockCount()));
            } else {
                vo.getLimiterTotal().add("0");
                vo.getLimiterBlock().add("0");
            }
            start++;
        }
        return vo;
    }

    @Override
    public LimiterDataEntity findByNameAndTime(String name, Long time) {
        return limiterDataMapper.findByNameAndTime(name,time);
    }

    /**
     * 添加
     *
     * @param limiterDataEntity LimiterDataEntity
     * @return int 插入数量
     */
    @Override
    public int insert(LimiterDataEntity limiterDataEntity) {
        return limiterDataMapper.insert(limiterDataEntity);
    }

    /**
     * 批量插入信息
     *
     * @param listEntity
     * @return int 插入数量
     */
    @Override
    public int insertBatch(List<LimiterDataEntity> listEntity) {
        return limiterDataMapper.insertBatch(listEntity);
    }


}
