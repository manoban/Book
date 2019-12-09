package com.example.book;

public class Words {

    private String id;
    private String word;
    private String meaning;
    private String sample;

    public Words(String id, String word, String meaning, String sample) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.sample = sample;
    }

    public Words(){}
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    @Override
    public String toString() {
        return "Words{" +
                "id='" + id + '\'' +
                ", word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                ", sample='" + sample + '\'' +
                '}';
    }
}
