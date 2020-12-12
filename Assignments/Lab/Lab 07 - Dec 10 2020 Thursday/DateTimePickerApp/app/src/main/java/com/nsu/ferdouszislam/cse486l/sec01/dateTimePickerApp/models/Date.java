package com.nsu.ferdouszislam.cse486l.sec01.dateTimePickerApp.models;

public class Date {

    private int mm, dd, yyyy;
    private String month;

    public Date(int mm, int dd, int yyyy) {
        this.mm = mm;
        this.dd = dd;
        this.yyyy = yyyy;

        this.month = computeMonth(this.mm);
    }

    private String computeMonth(int mm) {

        String _month = "";

        switch (this.mm){

            case 1:
                _month = "Jan";
                break;

            case 2:
                _month = "Feb";
                break;

            case 3:
                _month = "Mar";
                break;

            case 4:
                _month = "Apr";
                break;

            case 5:
                _month = "May";
                break;

            case 6:
                _month = "Jun";
                break;

            case 7:
                _month = "Jul";
                break;

            case 8:
                _month = "Aug";
                break;

            case 9:
                _month = "Sep";
                break;

            case 10:
                _month = "Oct";
                break;

            case 11:
                _month = "Nov";
                break;

            case 12:
                _month = "Dec";
                break;
        }

        return _month;
    }

    public static boolean isValidDate(int dd, int mm, int yyyy){

        boolean isValid = false;

        switch (mm){

            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:

                if(dd>=1&&dd<=31)
                    isValid = true;

                break;

            case 2:

                if(isLeapYear(yyyy) && (dd>=0 && dd<=29))
                    isValid = true;
                else if(dd>=1 && dd<=29)
                    isValid = true;

                break;

            case 4:
            case 6:
            case 9:
            case 11:

                if(dd>=1&&dd<=30)
                    isValid = true;

                break;
        }

        return isValid;
    }

    private static boolean isLeapYear(int yyyy){

        return yyyy%400==0 || (yyyy%4==0&&yyyy%100!=0);
    }

    public int getMm() {
        return mm;
    }

    public void setMm(int mm) {
        this.mm = mm;
    }

    public int getDd() {
        return dd;
    }

    public void setDd(int dd) {
        this.dd = dd;
    }

    public int getYyyy() {
        return yyyy;
    }

    public void setYyyy(int yyyy) {
        this.yyyy = yyyy;
    }

    @Override
    public String toString() {

        return month + " " + dd + ", " + yyyy;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
