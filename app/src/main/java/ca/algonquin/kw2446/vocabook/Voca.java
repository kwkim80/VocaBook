package ca.algonquin.kw2446.vocabook;

import java.io.Serializable;

public class Voca implements Serializable {
    private int id;
    private int wordSet_id;
    private String word;
    private String mean;
    private String past_word;
    private String pp_word;

    public String getPast_word() {
        return past_word;
    }

    public void setPast_word(String past_word) {
        this.past_word = past_word;
    }

    public String getPp_word() {
        return pp_word;
    }

    public void setPp_word(String pp_word) {
        this.pp_word = pp_word;
    }

    private boolean isHard;


    public Voca() {
        this(0, 0, "","");
    }

    public Voca(String word, String mean) {
        this(0,0, word,mean);
    }

    public Voca(int wordSet_id, String word, String mean) {
        this(0,wordSet_id, word,mean);
    }

    public Voca(int id, int wordSet_id, String word, String mean) {
        setId(id);setWordSet_id(wordSet_id);setWord(word);setMean(mean);setHard(false);
    }

    public int getId() { return id; }
    public void setId(int id) {this.id = id; }

    public int getWordSet_id() { return wordSet_id; }
    public void setWordSet_id(int wordSet_id) { this.wordSet_id = wordSet_id; }

    public String getWord() { return word; }
    public void setWord(String word) {this.word = word; }

    public String getMean() {return mean;}
    public void setMean(String mean) { this.mean = mean; }

    public boolean isHard() {return isHard; }
    public void setHard(boolean hard) {isHard = hard; }


}
