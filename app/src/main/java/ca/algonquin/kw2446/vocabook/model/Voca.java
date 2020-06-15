package ca.algonquin.kw2446.vocabook.model;

import java.io.Serializable;

public class Voca implements Serializable {
    private int id;
    private int wordSetId;
    private String word;
    private String mean;
    private String changedWord;
    private boolean hard;


    public Voca() { this("",""); }

    public Voca(String word, String mean) {
        this(0, word,mean);
    }

    public Voca(int wordSetId, String word, String mean) { this(wordSetId, word,mean,""); }

    public Voca(int wordSetId, String word, String mean, String changedWord) {
        this(0, wordSetId, word,mean, changedWord, false);
    }
    public Voca(int id, int wordSetId, String word, String mean, String changedWord , boolean hard ) {
        setId(id);
        setWordSetId(wordSetId);setWord(word);setMean(mean);setChangedWord(changedWord);setHard(hard);
    }

    public int getId() { return id; }
    public void setId(int id) {this.id = id; }

    public int getWordSetId() { return wordSetId; }
    public void setWordSetId(int wordSetId) { this.wordSetId = wordSetId; }

    public String getWord() { return word; }
    public void setWord(String word) {this.word = word; }

    public String getMean() {return mean;}
    public void setMean(String mean) { this.mean = mean; }

    public boolean isHard() {return hard; }
    public void setHard(boolean hard) { this.hard = hard; }

    public String getChangedWord() { return changedWord; }
    public void setChangedWord(String changedWord) { this.changedWord = changedWord; }
}
