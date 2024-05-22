package com.staticnur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParserServiceApplication.class, args);
    }

}

    /*
    select * from russian_address_data.room_type;
    select * from russian_address_data.room;
    select * from russian_address_data.house_type;
    select * from russian_address_data.house;
    select * from russian_address_data.apartment_type;
    select * from russian_address_data.apartment;
    select * from russian_address_data.adm_hierarchy;
    select * from russian_address_data.addr_obj_type;
    select * from russian_address_data.addr_obj;

    drop table russian_address_data.room_type;
    drop table russian_address_data.room;
    drop table russian_address_data.house_type;
    drop table russian_address_data.house;
    drop table russian_address_data.apartment_type;
    drop table russian_address_data.apartment;
    drop table russian_address_data.adm_hierarchy;
    drop table russian_address_data.addr_obj_type;
    drop table russian_address_data.addr_obj;

    drop table public.databasechangelog;
    drop table public.databasechangeloglock;

    drop schema russian_address_data;

    commit
   */
