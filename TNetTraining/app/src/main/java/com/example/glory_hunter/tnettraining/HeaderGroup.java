package com.example.glory_hunter.tnettraining;

import java.util.ArrayList;

/**
 * Created by glory-hunter on 15/01/2018.
 */

public class HeaderGroup {
    private String date;
    private String start;
    private String end;
    private String free;
    private String fare;
    private ArrayList<String> child = new ArrayList<>();

    public HeaderGroup(String date, String start, String end, String free, String fare) {
        this.date = date;
        this.start = start;
        this.end = end;
        this.free = free;
        this.fare = fare;
    }

    public ArrayList<String> getChild() {
        return child;
    }

    public void setChild(ArrayList<String> child) {
        this.child = child;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }
}
