package net.kaijie.campus_ui;


import java.io.Serializable;

public class Course implements Serializable  {



    /* 要上課的節次*/
    private int schedule;  //課程節次
    private int day;//課程星期幾
    private String room; //課程教室
    private String serial;        //課程課號
    private String name;    //課程名稱
    private String name_eng;    //課程名稱英文
    private String class_for; //開課班級
    private String require; //必修選修
    private String require_eng;  //英文必修選修
    private String credits; //學分組合
    private String teacher;      //課程老師
    private int spanNum ;// 默認跨每個課程跨越兩節(待修改)
    private String schedule_display;
    private boolean isNoClass = false;
    private boolean forSearch = false;

    public Course (int schedule, int day, String room,String serial, String name, String name_eng, String class_for, String require, String require_eng, String credits, String teacher,int spanNum,String schedule_display){
        this.schedule = schedule;
        this.day=day;
        this.room=room;
        this.serial=serial;
        this.name =name;
        this.name_eng =name_eng;
        this.class_for=class_for;
        this.require=require;
        this.require_eng=require_eng;
        this.credits=credits;
        this.teacher = teacher;
        this.spanNum = spanNum;
        this.schedule_display = schedule_display;
    }
    public Course(){
   }
    public int getschedule() {
        return schedule;
    }

    public Course setschedule(int schedule) {this.schedule = schedule; return Course.this;}

    public String getschedule_display() {
        return schedule_display;
    }

    public Course setschedule_display(String schedule_display) {this.schedule_display = schedule_display; return Course.this;}

    public int getday() {
        return day;
    }

    public Course setday(int day) {
        this.day = day; return Course.this;
    }

    public String getroom() {
        return room;
    }

    public Course setroom(String room) {this.room = room; return Course.this;}

    public String getserial() {return serial;}
    public Course setserial(String serial) {this.serial = serial; return Course.this;}

    public String getname() {
        return name;
    }

    public Course setname(String name) {this.name = name; return Course.this;}

    public String getname_eng() {
        return name_eng;
    }

    public Course setname_eng(String name_eng) {
        this.name_eng = name_eng; return Course.this;
    }

    public String getclass_for() {
        return class_for;
    }

    public Course setclassfor(String class_for) {
        this.class_for = class_for; return Course.this;
    }

    public String getrequire() {
        return require;
    }

    public Course setrequire(String require) {this.require = require; return Course.this; }

    public String getrequire_eng() {
        return require_eng;
    }

    public Course setrequire_eng(String require_eng) {this.require_eng = require_eng; return Course.this;}

    public String getcredits() {return credits;}

    public Course setcredits(String credits) {
        this.credits = credits; return Course.this;
    }

    public String getteacher() {
        return teacher;
    }

    public Course setteacher(String teacher) {
        this.teacher = teacher; return Course.this;
    }

    public int getSpanNum( ) {return spanNum;}

    public Course setSpanNum(int spanNum) {this.spanNum = spanNum; return Course.this;}

    public boolean getIsNoClasss(){return isNoClass;}

    public void setIsNoClass(boolean isNoClass){
        this.isNoClass = isNoClass;
    }

    public boolean getForSearch(){return forSearch;}

    public void setForSearch(boolean forSearch){
        this.forSearch = forSearch;
    }

  /*  @Override
    public String toString() {
        return "Course [Classtimes=" + schedule + ", Day=" + day + ", des=" + teacher
                +",Classroom="+room+", spanNun=" + spanNum + "]";
    }
*/

}
