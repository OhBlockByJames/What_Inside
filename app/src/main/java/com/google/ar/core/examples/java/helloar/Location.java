package com.google.ar.core.examples.java.helloar;

public class Location  {

    String location, name, time;

    public Location(){}
    public Location(String location, String name, String time){
        this.location = location;
        this.name = name;
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }
}
