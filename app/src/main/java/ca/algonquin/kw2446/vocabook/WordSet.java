package ca.algonquin.kw2446.vocabook;

import java.util.ArrayList;
import java.util.HashMap;

public class WordSet {
    private int id;
    private String title;
    private String category;
    private String regDate;

    private ArrayList<Voca> vocaList;

    public WordSet() { this(0,"","","",null); }

    public WordSet(String title, String category, String regDate) {
        this(0,title,category,regDate, null);
    }

    public WordSet( String title, String category, String regDate, ArrayList<Voca> vocaList ) {
        this(0,title,category,regDate, vocaList);
    }

    public WordSet(int id, String title, String category, String regDate, ArrayList<Voca> vocaList) {
        setTitle(title);setCategory(category);setRegDate(regDate);setVocaList(vocaList);
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id; }

    public String getRegDate() {
        return regDate;
    }
    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<Voca> getVocaList() { return vocaList; }
    public void setVocaList(ArrayList<Voca> vocaList) { this.vocaList = vocaList; }
}
