package cn.mianshiyi.example.model;

/**
 * @author shangqing.liu
 */
public class User {
    private String name;
    private String idNo;
    private Integer age;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", idNo='" + idNo + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }
}
