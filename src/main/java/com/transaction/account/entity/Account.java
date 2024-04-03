package com.transaction.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String firstName;
    private String lastName;
    private Double amount;
    private String email;
    private boolean status;
    private Date lastDebited;
   
    @Column(name = "mobile_number")
    private String mobileNumber;


    public String getMobileNumber() {
        return mobileNumber;
    }
    

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
