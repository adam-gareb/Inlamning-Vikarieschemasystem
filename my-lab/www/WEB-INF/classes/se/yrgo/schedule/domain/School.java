package se.yrgo.schedule.domain;

/**
 *  A school class, to use and get school names and addresses
 */
public class School {
    private String name;
    private String address;

    public School(String name, String address){
        this.name = name;
        this.address = address;
    }

    public String name(){
        return name;
    }

    public String address(){
        return address;
    }
}
