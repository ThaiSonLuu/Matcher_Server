package com.sanryoo.matcher.modal;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "messages")
public class MatcherMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageid", nullable = false)
    private Long messageid;

    @OneToOne
    @JoinColumn(name = "usersend")
    private User usersend;

    @OneToOne
    @JoinColumn(name = "userreceive")
    private User userreceive;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "type", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int type = 0;//0: Text, 1: Image

    @Column(name = "data", columnDefinition = "VARCHAR(250) DEFAULT ''", nullable = false)
    private String data = "";

    @Column(name = "width", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int width = 0;

    @Column(name = "height", columnDefinition = "INT DEFAULT 0", nullable = false)
    private int height = 0;


}

