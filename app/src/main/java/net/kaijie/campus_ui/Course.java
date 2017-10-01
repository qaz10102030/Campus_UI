package net.kaijie.campus_ui;


import java.io.Serializable;

public class Course implements Serializable  {



    /* 要上課的節次*/
    private int Classtimes;  //課程節次
    private int Day;        //課程星期幾
    private String Classroom; //課程教室
    private int Num;        //課程課號
    private String Name;    //課程名稱
    private String Teacher;  //課程老師
    private String des;
    private int spanNum ;// 默認跨每個課程跨越兩節(待修改)

    private String ClassRoomName;
    private String ClassTypeName;
    public Course (int Classtimes, int Day, String Classroom,int Num,String Name, String des,int spanNum){
        this.Classtimes = Classtimes;
        this.Day=Day;
        this.Classroom=Classroom;
        this.Num=Num;
        this.Name=Name;
      //  this.Teacher=Teacher;
        this.des = des;
        this.spanNum = spanNum;

    }
    public Course(){
   }
    public int getClasstimes() {
        return Classtimes;
    }

    public void setClasstimes(int Classtimes) {this.Classtimes = Classtimes;}

    public int getDay() {
        return Day;
    }

    public void setDay(int Day) {
        this.Day = Day;
    }

    public String getClassroom() {
        return Classroom;
    }

    public void setClassroom(String Classroom) {
        this.Classroom = Classroom;
    }

    public int getNum() {
        return Num;
    }

    public void setNum(int Num) {
        this.Num = Num;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }


    public void setSpanNum(int spanNum) {this.spanNum = spanNum;}

    public int getSpanNum( ) {return spanNum;}

    @Override
    public String toString() {
        return "Course [Classtimes=" + Classtimes + ", Day=" + Day + ", des=" + des
                +",Classroom="+ Classroom+", spanNun=" + spanNum + "]";
    }


}
