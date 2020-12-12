package com.nsu.ferdouszislam.cse486l.sec01.dateTimePickerApp.models;

public class Time {

    private int hh, mm;
    private String amPM;

    public Time(int hh, int mm) {
        this.hh = hh;
        this.mm = mm;

        if(hh>=12) {
            if(hh>12)
                this.hh -= 12;
            this.amPM = "PM";
        }

        else this.amPM = "AM";
    }

    public Time(int hh, int mm, String amPM) {
        this.hh = hh;
        this.mm = mm;
        this.amPM = amPM;
    }

    public static boolean isValidTime(int hh, int mm){

        return hh>=0&&hh<=23&&mm>=0&&mm<=60;
    }

    public int getHh() {
        return hh;
    }

    public void setHh(int hh) {
        this.hh = hh;
    }

    public int getMm() {
        return mm;
    }

    public void setMm(int mm) {
        this.mm = mm;
    }

    public String getAmPM() {
        return amPM;
    }

    public void setAmPM(String amPM) {
        this.amPM = amPM;
    }

    @Override
    public String toString() {

        String ret = hh+":"+mm+" "+amPM;

        if(mm/10==0)
            ret = hh+":0"+mm+" "+amPM;

        return ret;
    }
}
