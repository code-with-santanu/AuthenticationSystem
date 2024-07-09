package com.santanu.jwtAuth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Table(name="token")
@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "logged_out")
    private boolean loggedOut;

    @Column(name = "logged_out_time")
    private Date loggedOutTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;




}
