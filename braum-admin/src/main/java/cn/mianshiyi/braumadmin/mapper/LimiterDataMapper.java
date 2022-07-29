package cn.mianshiyi.braumadmin.mapper;

import cn.mianshiyi.braumadmin.entity.LimiterDataEntity;
import cn.mianshiyi.braumadmin.entity.qo.LimiterDataQo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shangqing.liu
 */
@Mapper
public interface LimiterDataMapper {
    /**
     * 根据主键查询表数据
     *
     * @param id LimiterDataEntity
     * @return LimiterDataEntity
     */
    LimiterDataEntity findById(@Param("id") Long id);

    List<LimiterDataEntity> findByQo(LimiterDataQo qo);

    /**
     * 插入信息
     *
     * @param limiterDataEntity LimiterDataEntity
     * @return int
     */
    int insert(LimiterDataEntity limiterDataEntity);

    /**
     * 批量插入信息
     *
     * @param listEntity
     * @return int
     */
    int insertBatch(@Param("listEntity") List<LimiterDataEntity> listEntity);

    /**
     * 根据ID更新信息
     *
     * @param limiterDataEntity LimiterDataEntity
     * @return int
     */
    int updateById(LimiterDataEntity limiterDataEntity);

    /**
     * 根据ID删除信息
     *
     * @param id Long
     * @return int
     */
    int deleteById(@Param("id") Long id);

    LimiterDataEntity findByNameAndTime(@Param("name") String name, @Param("time") Long time);
}
