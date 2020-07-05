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
//        HashMap<String, String > sample=new HashMap<>();
//        sample.put("student","학생");
//        sample.put("school","학교");
//        sample.put("teacher","선생님");
//        sample.put("doctor","의사");
//        sample.put("desk","책상");
//        sample.put("chair","의자");
//        sample.put("friend","친구");
//
//        HashMap<String, String > sample2=new HashMap<>();
//        sample2.put("one","하나");
//        sample2.put("two","둘");
//        sample2.put("three","셋");
//        sample2.put("four","넷");
//        sample2.put("five","다섯");
//        sample2.put("six","여섯");
//        sample2.put("seven","일곱");
//
//
//        HashMap<String, String > sample3=new HashMap<>();
//        sample3.put("Sunday","일요일");
//        sample3.put("Monday","월요일");
//        sample3.put("Tuesday","화요일");
//        sample3.put("Wednesday","수요일");
//        sample3.put("Thursday","목요일");
//        sample3.put("Friday","금요일");
//        sample3.put("Saturday","토요일");
//
//        HashMap<String, String > sample4=new HashMap<>();
//
//        for(int i=1;i<40;i++){
//            sample4.put("key"+i,"value"+i);
//        }

//        list.add(new WordSet("sample1","EN","", sample));
//        list.add(new WordSet("sample2","FR","", sample2));
//        list.add(new WordSet("sample3","KR","", sample3));
//        list.add(new WordSet("sample4","KR","2020.06.07", sample4));
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
