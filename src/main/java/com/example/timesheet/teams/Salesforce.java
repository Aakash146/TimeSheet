package com.example.timesheet.teams;

import java.util.ArrayList;
import java.util.List;

public class Salesforce {
    public List<List<String>> names = new ArrayList<>();

    public Salesforce(){

        List<String> amiya = List.of(new String[]{"Amiya Das","Sunny Mohla","Tarun Rawal","Rex Sheridan","amiya.das@nagarro.com","sunny.mohla@nagarro.com","tarun.rawal@nagarro.com","rex.sheridan@coxautoinc.com","3186895"});
        List<String> kaushal = List.of(new String[]{"Kaushal Chawla","Sunny Mohla","Tarun Rawal","Rex Sheridan","kaushal.chawla@nagarro.com","sunny.mohla@nagarro.com","tarun.rawal@nagarro.com","rex.sheridan@coxautoinc.com","3195618"});
        List<String> richie = List.of(new String[]{"Richie Aigbe","Sunny Mohla","Tarun Rawal","Rex Sheridan","richie.aigbe@nagarro.com","sunny.mohla@nagarro.com","tarun.rawal@nagarro.com","rex.sheridan@coxautoinc.com","3199258"});
        List<String> litty = List.of(new String[]{"Litty Philip","Sunny Mohla","Tarun Rawal","Rex Sheridan","litty.philip@nagarro.com","sunny.mohla@nagarro.com","tarun.rawal@nagarro.com","rex.sheridan@coxautoinc.com","3211785"});
        List<String> apurva = List.of(new String[]{"Apurva Sinha","Sunny Mohla","Tarun Rawal","Rex Sheridan","apurva.sinha@nagarro.com","sunny.mohla@nagarro.com","tarun.rawal@nagarro.com","rex.sheridan@coxautoinc.com","3143158"});
        List<String> lokesh = List.of(new String[]{"Lokesh Ranganna","Sunny Mohla","Tarun Rawal","Rex Sheridan","lokesh.ranganna@nagarro.com","sunny.mohla@nagarro.com","tarun.rawal@nagarro.com","rex.sheridan@coxautoinc.com","3213896"});
        //List<String> richie = List.of(new String[]{"Richie Aigbe","Bhaskar Venugopal","Tarun Rawal","Rex Sheridan","richie.aigbe@nagarro.com","bhaskar.venugopal@nagarro.com","tarun.rawal@nagarro.com","rex.sheridan@coxautoinc.com","3199258"});

        names.add(amiya);
        names.add(apurva);
        names.add(kaushal);
        names.add(litty);
        names.add(lokesh);
        names.add(richie);

    }
}
