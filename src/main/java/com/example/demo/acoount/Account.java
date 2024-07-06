package com.example.demo.acoount;


import com.example.demo.transaction.MyTransaction;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "Account")
@Table(
        name = "account",
        uniqueConstraints = {
                @UniqueConstraint(name = "student_email_unique", columnNames = "email")
        }

)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "accountHolderName", nullable = false, columnDefinition = "TEXT")
    private String accountHolderName;

    @Column(name = "balance", nullable = false)
    private double balance;

    @Column(name = "email", nullable = false,  length = 255)
    private String email;

    @Column(name = "age", nullable = false)
    private int age;


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MyTransaction> transactions = new ArrayList<>();









    public Account() {

    }

    public Account(Long id, String accountHolderName, double balance, int age, String email) {
        this.id = id;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.age = age;
        this.email = email;
    }

    public Account(String accountHolderName, double balance, int age, String email) {
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.age = age;
        this.email = email;
    }
}
