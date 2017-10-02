package net.kaijie.campus_ui;


import java.io.Serializable;

public class Course implements Serializable  {



    /* 要上課的節次*/
    private int schedule;  //課程節次
    private int day;//課程星期幾
    private String room; //課程教室
    private int serial;        //課程課號
    private String name;    //課程名稱
    private String name_eng;;    //課程名稱英文
    private String teacher;      //課程老師
    private int spanNum ;// 默認跨每個課程跨越兩節(待修改)

    public Course (int schedule, int day, String room,int serial, String name, String teacher,int spanNum){
        this.schedule = schedule;
        this.day=day;
        this.room=room;
        this.serial=serial;
        this.name =name;
        this.teacher = teacher;
        this.spanNum = spanNum;

    }
    public Course(){
   }
    public int getschedule() {
        return schedule;
    }

    public void setschedule(int schedule) {this.schedule = schedule;}

    public int getday() {
        return day;
    }

    public void setday(int day) {
        this.day = day;
    }

    public String getroom() {
        return room;
    }

    public void setroom(String room) {
        this.room = room;
    }

    public int getserial() {
        return serial;
    }

    public void setserial(int serial) {
        this.serial = serial;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getteacher() {
        return teacher;
    }

    public void setteacher(String teacher) {
        this.teacher = teacher;
    }


    public void setSpanNum(int spanNum) {this.spanNum = spanNum;}

    public int getSpanNum( ) {return spanNum;}

  /*  @Override
    public String toString() {
        return "Course [Classtimes=" + schedule + ", Day=" + day + ", des=" + teacher
                +",Classroom="+room+", spanNun=" + spanNum + "]";
    }
*/

}
