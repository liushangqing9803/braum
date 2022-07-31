package cn.mianshiyi.braumadmin.mapper;

import cn.mianshiyi.braumadmin.entity.LimiterRegisterInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shangqing.liu
 */
@Mapper
public interface LimiterRegisterInfoMapper {

    /**
     * 根据主键查询表数据
     *
     * @param id LimiterRegisterInfoEntity
     * @return LimiterRegisterInfoEntity
     */
    LimiterRegisterInfoEntity findById(@Param("id") Long id);

    /**
     * 插入信息
     *
     * @param limiterRegisterInfoEntity LimiterRegisterInfoEntity
     * @return int
     */
    int insert(LimiterRegisterInfoEntity limiterRegisterInfoEntity);

    /**
     * 批量插入信息
     *
     * @return int
     */
    int insertBatch(@Param("listEntity") List<LimiterRegisterInfoEntity> listEntity);

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
     * @param id Long
     * @return int
     */
    int deleteById(@Param("id") Long id);

    LimiterRegisterInfoEntity findByName(@Param("name") String name);

    List<LimiterRegisterInfoEntity> findAll();
}
