package com.buka.pojo;

import java.io.Serializable;

public class TSetmeal implements Serializable {
    private Integer id;

    private String name;

    private String code;

    private String helpcode;

    private String sex;

    private String age;

    private Float price;

    private String remark;

    private String attention;

    private String img;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getHelpcode() {
        return helpcode;
    }

    public void setHelpcode(String helpcode) {
        this.helpcode = helpcode == null ? null : helpcode.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age == null ? null : age.trim();
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention == null ? null : attention.trim();
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img == null ? null : img.trim();
    }

    @Override
    public String toString() {
        return "TSetmeal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", helpcode='" + helpcode + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", price=" + price +
                ", remark='" + remark + '\'' +
                ", attention='" + attention + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}