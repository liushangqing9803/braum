package cn.mianshiyi.braumadmin.service.impl;

import cn.mianshiyi.braumadmin.entity.LimiterDataEntity;
import cn.mianshiyi.braumadmin.entity.qo.LimiterDataQo;
import cn.mianshiyi.braumadmin.entity.vo.LimiterDataDetailValue;
import cn.mianshiyi.braumadmin.entity.vo.LimiterDataDetailVo;
import cn.mianshiyi.braumadmin.entity.vo.LimiterDataTotalVo;
import cn.mianshiyi.braumadmin.entity.vo.LimiterDataView;
import cn.mianshiyi.braumadmin.mapper.LimiterDataMapper;
import cn.mianshiyi.braumadmin.service.LimiterDataService;
import cn.mianshiyi.braumadmin.utils.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
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
    public LimiterDataView findByQo(LimiterDataQo qo) {
        List<LimiterDataEntity> byQo = limiterDataMapper.findByQo(qo);
        if (CollectionUtils.isEmpty(byQo)) {
            return null;
        }
        Map<Long, List<LimiterDataEntity>> mapList = new HashMap<>();
        Set<String> clientIp = new HashSet<>();

        for (LimiterDataEntity entity : byQo) {
            if (!mapList.containsKey(entity.getOccSecond())) {
                List<LimiterDataEntity> entities = new ArrayList<>();
                mapList.put(entity.getOccSecond(), entities);
            }
            mapList.get(entity.getOccSecond()).add(entity);
            clientIp.add(StringUtils.isEmpty(entity.getClientIp()) ? "unknown" : entity.getClientIp());
        }

        LimiterDataTotalVo vo = new LimiterDataTotalVo();
        vo.setName(qo.getName() + "-total");

        LimiterDataDetailVo detailVo = new LimiterDataDetailVo();
        detailVo.setName(qo.getName() + "-detail");


        Long start = qo.getStart();
        Long end = qo.getEnd();

        Map<String, LimiterDataDetailValue> valueMap = new HashMap<>();

        for (String ip : clientIp) {
            LimiterDataDetailValue total = new LimiterDataDetailValue("limiterTotal" + ip);
            LimiterDataDetailValue block = new LimiterDataDetailValue("limiterBlock" + ip);
            valueMap.put("limiterTotal-" + ip, total);
            valueMap.put("limiterBlock-" + ip, block);
            detailVo.getLineName().add("limiterTotal-" + ip);
            detailVo.getLineName().add("limiterBlock-" + ip);
            detailVo.getData().add(total);
            detailVo.getData().add(block);
        }


        while (end >= start) {
            vo.getTimes().add(DateUtil.format(new Date(start * 1000)));
            detailVo.getTimes().add(DateUtil.format(new Date(start * 1000)));

            List<LimiterDataEntity> entities = mapList.get(start);
            if (CollectionUtils.isNotEmpty(entities)) {
                long exeCount = entities.stream().mapToLong(LimiterDataEntity::getExecCount).sum();
                long blockCount = entities.stream().mapToLong(LimiterDataEntity::getBlockCount).sum();
                vo.getLimiterTotal().add(String.valueOf(exeCount));
                vo.getLimiterBlock().add(String.valueOf(blockCount));

                for (LimiterDataEntity entity : entities) {
                    String entityClientIp = StringUtils.isEmpty(entity.getClientIp()) ? "unknown" : entity.getClientIp();
                    LimiterDataDetailValue totalValue = valueMap.get(("limiterTotal-" + entityClientIp));
                    LimiterDataDetailValue blockValue = valueMap.get(("limiterBlock-" + entityClientIp));

                    totalValue.getData().add(String.valueOf(entity.getExecCount()));
                    blockValue.getData().add(String.valueOf(entity.getBlockCount()));
                }


            } else {
                vo.getLimiterTotal().add("0");
                vo.getLimiterBlock().add("0");
                for (String key : valueMap.keySet()) {
                    valueMap.get(key).getData().add("0");
                }
            }
            start++;
        }
        LimiterDataView limiterDataView = new LimiterDataView();
        limiterDataView.setLimiterDataDetailVo(detailVo);
        limiterDataView.setLimiterDataTotalVo(vo);
        return limiterDataView;
    }


    @Override
    public LimiterDataEntity findByNameAndTime(String name, Long time) {
        return limiterDataMapper.findByNameAndTime(name, time);
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
