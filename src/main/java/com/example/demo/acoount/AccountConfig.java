package com.example.demo.acoount;


import com.example.demo.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AccountConfig {

         @Bean
         CommandLineRunner commandLineRunner(
                 AccountRepository accountRepository){
             return args -> {

                 Account Malina = new Account(

                        "Alina",
                         339.300,
                          28,
                         "svudova6666@gmail.com"

                 );



                 Account Oksana = new Account(

                         "Maria",
                         239.300,
                         30,
                         "tovste8888@gmail.com"

                 );


                    accountRepository.saveAll(
                         List.of(Malina,Oksana)

                    );

             };





         }













}
