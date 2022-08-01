package cn.mianshiyi.braumadmin.service;

import cn.mianshiyi.braumadmin.entity.APIResponse;
import cn.mianshiyi.braumadmin.entity.LimiterRegisterInfoEntity;
import cn.mianshiyi.braumadmin.entity.ListResponse;
import cn.mianshiyi.braumadmin.entity.qo.LimiterRegisterDataQo;

import java.util.List;

/**
 * @author shangqing.liu
 */
public interface LimiterRegisterInfoService {

    /**
     * 根据Id查询
     *
     * @param id 主键
     * @return LimiterRegisterInfoEntity
     */
    LimiterRegisterInfoEntity findById(Long id);

    LimiterRegisterInfoEntity findByName(String name);

    List<LimiterRegisterInfoEntity> findAll();

    /**
     * 添加
     *
     * @param limiterRegisterInfoEntity LimiterRegisterInfoEntity
     * @return int 插入数量
     */
    int insert(LimiterRegisterInfoEntity limiterRegisterInfoEntity);

    /**
     * 批量插入信息
     *
     * @return int 插入数量
     */
    int insertBatch(List<LimiterRegisterInfoEntity> listEntity);

    /**
     * 根据ID更新信息
     *
     * @param limiterRegisterInfoEntity LimiterRegisterInfoEntity
     * @return int
     */
    int updateById(LimiterRegisterInfoEntity limiterRegisterInfoEntity);

    /**
     * 根据ID删除信息
     *
     * @param id 主键
     * @return int
     */
    int deleteById(Long id);

    APIResponse<ListResponse<LimiterRegisterInfoEntity>> findPage(LimiterRegisterDataQo qo);
}
