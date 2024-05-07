package com.example.timesheet.teams;

import java.util.ArrayList;
import java.util.List;

public class Telematics {
    public List<List<String>> names = new ArrayList<>();

    public Telematics(){

        List<String> varinder = List.of(new String[]{"Varinder Singh","Rajeev Kumar","Tarun Rawal","Anjan Umashankar","varinder.singh@nagarro.com","rajeev.kumar@nagarro.com","tarun.rawal@nagarro.com","anjan.umashankar@coxautoinc.com","3149252"});
        List<String> venkata = List.of(new String[]{"Venkata Valavala","Bhaskar Venugopal","Tarun Rawal","Anjan Umashankar","venkata.valavala@nagarro.com","bhaskar.venugopal@nagarro.com","tarun.rawal@nagarro.com","anjan.umashankar@coxautoinc.com","3167873"});

        names.add(varinder);
        names.add(venkata);

    }
}
