package ca.algonquin.kw2446.vocabook.util;

import android.app.Application;

import java.util.ArrayList;

import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.model.WordSet;

public class ApplicationClass extends Application {

    public static ArrayList<WordSet> list;
    public static ArrayList<Voca> vocas;
    public static String password;
    @Override
    public void onCreate() {
        super.onCreate();
        list=new ArrayList<>();

        ArrayList<Voca> sample=new ArrayList<>();
        sample.add(new Voca("student","학생"));
        sample.add(new Voca("school","학교"));
        sample.add(new Voca("teacher","선생님"));
        sample.add(new Voca("doctor","의사"));
        sample.add(new Voca("desk","책상"));
        sample.add(new Voca("chair","의자"));
        sample.add(new Voca("friend","친구"));
        sample.add(new Voca("hospital","병원"));
        sample.add(new Voca("rainbow","무지개"));
        sample.add(new Voca("always","항상"));
        sample.add(new Voca("student","학생"));
        sample.add(new Voca("school","학교"));
        sample.add(new Voca("teacher","선생님"));
        sample.add(new Voca("doctor","의사"));
        sample.add(new Voca("desk","책상"));
        sample.add(new Voca("chair","의자"));
        sample.add(new Voca("friend","친구"));
        sample.add(new Voca("hospital","병원"));
        sample.add(new Voca("rainbow","무지개"));
        sample.add(new Voca("always","항상"));
        sample.add(new Voca("student","학생"));
        sample.add(new Voca("school","학교"));
        sample.add(new Voca("teacher","선생님"));
        sample.add(new Voca("doctor","의사"));
        sample.add(new Voca("desk","책상"));
        sample.add(new Voca("chair","의자"));
        sample.add(new Voca("friend","친구"));
        sample.add(new Voca("hospital","병원"));
        sample.add(new Voca("rainbow","무지개"));
        sample.add(new Voca("always","항상"));



        list.add(new WordSet("sample","english","", sample));
        vocas=sample;
        setInitialPwd();

    }

    private void setInitialPwd(){
        password = PreferenceManager.getString(getApplicationContext(), "pwd");

        if (password.equals("")) {
            password="0000";
            PreferenceManager.setString(getApplicationContext(), "pwd", "0000");

        }

    }
}
