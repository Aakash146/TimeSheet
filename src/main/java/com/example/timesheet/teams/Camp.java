package com.example.timesheet.teams;

import java.util.ArrayList;
import java.util.List;

public class Camp {
    public List<List<String>> names = new ArrayList<>();
    public Camp(){
        List<String> aakash = List.of(new String[]{"Aakash Singh","Rajeev Kumar","Tarun Rawal","Leena Adhikary","aakash.singh@nagarro.com","rajeev.kumar@nagarro.com","tarun.rawal@nagarro.com","leena.adhikary@coxautoinc.com","3192321"});
        List<String> abhijit = List.of(new String[]{"Abhijit Honwalkar","Bhaskar Venugopal","Tarun Rawal","Leena Adhikary","abhijit.honwalkar@nagarro.com","bhaskar.venugopal@nagarro.com","tarun.rawal@nagarro.com","leena.adhikary@coxautoinc.com","3210232"});
        List<String> kamna = List.of(new String[]{"Kamna Sharma","Rajeev Kumar","Tarun Rawal","Leena Adhikary","kamna.sharma@nagarro.com","rajeev.kumar@nagarro.com","tarun.rawal@nagarro.com","leena.adhikary@coxautoinc.com","3151275"});
        List<String> syed = List.of(new String[]{"Syed Ali","Rajeev Kumar","Tarun Rawal","Leena Adhikary","syed01@nagarro.com","rajeev.kumar@nagarro.com","tarun.rawal@nagarro.com","leena.adhikary@coxautoinc.com","3193833"});

        names.add(aakash);
        names.add(abhijit);
        names.add(kamna);
        names.add(syed);
    }
}
