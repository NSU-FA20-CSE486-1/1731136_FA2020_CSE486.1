package com.ferdouszislam.nsu.cse486.sec01.bangladictonary.models;

public class DictonaryItem {

    private String wordInEnglish;
    private String wordInBangla;

    public DictonaryItem() {
    }

    public DictonaryItem(String wordInEnglish, String wordInBangla) {
        this.wordInEnglish = wordInEnglish;
        this.wordInBangla = wordInBangla;
    }

    /*
    validate member variables of this object
     */
    public boolean validateLanguage(){

        return wordInBangla!=null && wordInEnglish !=null && !wordInEnglish.isEmpty() && !wordInBangla.isEmpty()
                && isValidEnglishWord() && isValidBanglaWord();
    }

    /*
    validate if 'wordInEnglish' is actually english
     */
    public boolean isValidEnglishWord(){

        if(this.wordInEnglish==null) return false;

        // check if 'wordInEnglish is actually in English'
        for(char c: this.wordInEnglish.toCharArray()) {

            if(Character.UnicodeBlock.of(c) != Character.UnicodeBlock.BASIC_LATIN) return false;
        }

        return true;
    }

    /*
    validate if 'wordInBangla' is in Bangla
     */
    public boolean isValidBanglaWord(){

        if(this.wordInBangla==null) return false;

        // check if 'wordInBangla is actually in Bangla'
        for(char c: this.wordInBangla.toCharArray()) {

            if(Character.UnicodeBlock.of(c) != Character.UnicodeBlock.BENGALI) return false;
        }

        return true;
    }

    public String getWordInEnglish() {
        return wordInEnglish;
    }

    public void setWordInEnglish(String wordInEnglish) {
        this.wordInEnglish = wordInEnglish;
    }

    public String getWordInBangla() {
        return wordInBangla;
    }

    public void setWordInBangla(String wordInBangla) {
        this.wordInBangla = wordInBangla;
    }
}
