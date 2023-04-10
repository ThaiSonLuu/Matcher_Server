package com.sanryoo.matcher.modal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", columnDefinition = "VARCHAR(50) DEFAULT ''", nullable = false, unique = true)
    private String username = "";
    @Column(name = "password", columnDefinition = "VARCHAR(200) DEFAULT ''", nullable = false)
    private String password = "";

    @Column(name = "idgoogle", columnDefinition = "VARCHAR(100) DEFAULT ''", nullable = false)
    private String idgoogle = "";

    @Column(name = "idfacebook", columnDefinition = "VARCHAR(100) DEFAULT ''", nullable = false)
    private String idfacebook = "";


    @Column(name = "fcmtoken", columnDefinition = "VARCHAR(255) DEFAULT ''", nullable = false)
    private String fcmtoken = "";

    @Column(name = "fullname", columnDefinition = "VARCHAR(50) DEFAULT ''", nullable = false)
    private String fullname = "";

    @Column(name = "sex", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int sex = 0;

    @Column(name = "status", columnDefinition = "INT DEFAULT 1", nullable = false)
    private int status = 1;

    @Column(name = "status1", columnDefinition = "INT DEFAULT 1", nullable = false)
    private int status1 = 1;

    @Column(name = "searching", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int searching = 0;

    @Column(name = "avatar", columnDefinition = "VARCHAR(255) DEFAULT ''", nullable = false)
    private String avatar = "";

    @Column(name = "image1", columnDefinition = "VARCHAR(255) DEFAULT ''", nullable = false)
    private String image1 = "";

    @Column(name = "image2", columnDefinition = "VARCHAR(255) DEFAULT ''", nullable = false)
    private String image2 = "";

    @Column(name = "image3", columnDefinition = "VARCHAR(255) DEFAULT ''", nullable = false)
    private String image3 = "";

    @Column(name = "age", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int age = 0;

    @Column(name = "height", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int height = 0;

    @Column(name = "weight", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int weight = 0;

    @Column(name = "income", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int income = 0;

    @Column(name = "latitude", columnDefinition = "DOUBLE DEFAULT 0.0", nullable = false)
    private double latitude = 0.0;

    @Column(name = "longitude", columnDefinition = "DOUBLE DEFAULT 0.0", nullable = false)
    private double longitude = 0.0;

    @Column(name = "appearance", columnDefinition = "DOUBLE DEFAULT 0.0", nullable = false)
    private double appearance = 0.0;

    @Column(name = "easy", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int easy = 0;


    @Column(name = "age1", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int age1 = 0;

    @Column(name = "age2", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int age2 = 0;

    @Column(name = "height1", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int height1 = 0;

    @Column(name = "height2", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int height2 = 0;

    @Column(name = "weight1", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int weight1 = 0;

    @Column(name = "weight2", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int weight2 = 0;

    @Column(name = "income1", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int income1 = 0;

    @Column(name = "distance", columnDefinition = "int DEFAULT 10", nullable = false)
    private int distance = 10;

    @Column(name = "appearance1", columnDefinition = "DOUBLE DEFAULT 0.0", nullable = false)
    private double appearance1 = 0.0;

    @Column(name = "matched", columnDefinition = "INT DEFAULT 0", nullable = false)
    private Long matched = 0L;

    @Column(name = "currentdistance", columnDefinition = "DOUBLE DEFAULT 0.0", nullable = false)
    private double currentdistance = 0.0;

    @Column(name = "banned", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int banned = 0;

}
