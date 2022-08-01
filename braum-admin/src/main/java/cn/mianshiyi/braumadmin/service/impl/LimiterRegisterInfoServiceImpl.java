package cn.mianshiyi.braumadmin.service.impl;

import cn.mianshiyi.braumadmin.entity.APIResponse;
import cn.mianshiyi.braumadmin.entity.LimiterRegisterInfoEntity;
import cn.mianshiyi.braumadmin.entity.ListResponse;
import cn.mianshiyi.braumadmin.entity.qo.LimiterRegisterDataQo;
import cn.mianshiyi.braumadmin.mapper.LimiterRegisterInfoMapper;
import cn.mianshiyi.braumadmin.service.LimiterRegisterInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shangqing.liu
 */
@Service
public class LimiterRegisterInfoServiceImpl implements LimiterRegisterInfoService {

    @Resource
    private LimiterRegisterInfoMapper limiterRegisterInfoMapper;

    /**
     * 根据Id查询
     *
     * @param id 主键
     * @return LimiterRegisterInfoEntity
     */
    @Override
    public LimiterRegisterInfoEntity findById(Long id) {
        return limiterRegisterInfoMapper.findById(id);
    }

    @Override
    public LimiterRegisterInfoEntity findByName(String name) {
        return limiterRegisterInfoMapper.findByName(name);
    }

    @Override
    public List<LimiterRegisterInfoEntity> findAll() {
        return limiterRegisterInfoMapper.findAll();
    }

    /**
     * 添加
     *
     * @param limiterRegisterInfoEntity LimiterRegisterInfoEntity
     * @return int 插入数量
     */
    @Override
    public int insert(LimiterRegisterInfoEntity limiterRegisterInfoEntity) {
        return limiterRegisterInfoMapper.insert(limiterRegisterInfoEntity);
    }

    /**
     * 批量插入信息
     *
     * @return int 插入数量
     */
    @Override
    public int insertBatch(List<LimiterRegisterInfoEntity> listEntity) {
        return limiterRegisterInfoMapper.insertBatch(listEntity);
    }

    /**
     * 根据ID更新信息
     *
     * @param limiterRegisterInfoEntity LimiterRegisterInfoEntity
     * @return int
     */
    @Override
    public int updateById(LimiterRegisterInfoEntity limiterRegisterInfoEntity) {
        return limiterRegisterInfoMapper.updateById(limiterRegisterInfoEntity);
    }

    /**
     * 根据ID删除信息
     *
     * @param id 主键
     * @return int
     */
    @Override
    public int deleteById(Long id) {
        return limiterRegisterInfoMapper.deleteById(id);
    }

    @Override
    public APIResponse<ListResponse<LimiterRegisterInfoEntity>> findPage(LimiterRegisterDataQo qo) {
        int count = limiterRegisterInfoMapper.findCount(qo);
        ListResponse<LimiterRegisterInfoEntity> listResult = new ListResponse<>(null, count);
        if (count == 0) {
            return APIResponse.returnSuccess(listResult);
        }
        qo.setOffset((qo.getOffset() - 1) * qo.getLimit());
        List<LimiterRegisterInfoEntity> page = limiterRegisterInfoMapper.findPage(qo);
        if (CollectionUtils.isEmpty(page)) {
            return APIResponse.returnSuccess(listResult);
        }
        listResult.setList(page);
        return APIResponse.returnSuccess(listResult);
    }
}

