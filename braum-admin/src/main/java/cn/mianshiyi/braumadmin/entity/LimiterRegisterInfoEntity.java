package cn.mianshiyi.braumadmin.entity;

/**
 * @author shangqing.liu
 */
public class LimiterRegisterInfoEntity {

    /**
     *
     */
    private Long id;

    /**
     * 限流名称
     */
    private String name;


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
}
