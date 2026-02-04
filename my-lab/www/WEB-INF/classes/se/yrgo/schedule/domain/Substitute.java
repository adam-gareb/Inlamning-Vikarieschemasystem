package se.yrgo.schedule.domain;

/**
 * A class for substitutes, to be able to use and get substitute name
 */
public class Substitute {
    private String name;

    public Substitute(String name){
        this.name = name;
    }

    public String name(){
        return name;
    }
}
