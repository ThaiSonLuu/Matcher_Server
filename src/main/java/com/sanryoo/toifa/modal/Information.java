package com.sanryoo.toifa.modal;

import javax.persistence.*;

@Entity
@Table(name = "informations")
public class Information {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname = "";
    private int sex;
    private int status;
    private int searching;
    private String avatar = "";
    private String image1 = "";
    private String image2 = "";
    private String image3 = "";
    private int age;
    private int height;
    private int weight;
    private int income;
    private String province = "Hà Nội";
    private double appearance;
    private int easy;
    private int age1;
    private int age2;
    private int height1;
    private int height2;
    private int weight1;
    private int weight2;
    private int income1;
    private int province1;
    private double appearance1;

    public Information() {
    }

    public Long getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSearching() {
        return searching;
    }

    public void setSearching(int searching) {
        this.searching = searching;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public double getAppearance() {
        return appearance;
    }

    public void setAppearance(double appearance) {
        this.appearance = appearance;
    }

    public int getEasy() {
        return easy;
    }

    public void setEasy(int easy) {
        this.easy = easy;
    }

    public int getAge1() {
        return age1;
    }

    public void setAge1(int age1) {
        this.age1 = age1;
    }

    public int getAge2() {
        return age2;
    }

    public void setAge2(int age2) {
        this.age2 = age2;
    }

    public int getHeight1() {
        return height1;
    }

    public void setHeight1(int height1) {
        this.height1 = height1;
    }

    public int getHeight2() {
        return height2;
    }

    public void setHeight2(int height2) {
        this.height2 = height2;
    }

    public int getWeight1() {
        return weight1;
    }

    public void setWeight1(int weight1) {
        this.weight1 = weight1;
    }

    public int getWeight2() {
        return weight2;
    }

    public void setWeight2(int weight2) {
        this.weight2 = weight2;
    }

    public int getIncome1() {
        return income1;
    }

    public void setIncome1(int income1) {
        this.income1 = income1;
    }

    public int getProvince1() {
        return province1;
    }

    public void setProvince1(int province1) {
        this.province1 = province1;
    }

    public double getAppearance1() {
        return appearance1;
    }

    public void setAppearance1(double appearance1) {
        this.appearance1 = appearance1;
    }

    @Override
    public String toString() {
        return "Information{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", sex=" + sex +
                ", status=" + status +
                ", searching=" + searching +
                ", avatar='" + avatar + '\'' +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", income=" + income +
                ", province='" + province + '\'' +
                ", appearance=" + appearance +
                ", easy=" + easy +
                ", age1=" + age1 +
                ", age2=" + age2 +
                ", height1=" + height1 +
                ", height2=" + height2 +
                ", weight1=" + weight1 +
                ", weight2=" + weight2 +
                ", income1=" + income1 +
                ", province1=" + province1 +
                ", appearance1=" + appearance1 +
                '}';
    }
}
