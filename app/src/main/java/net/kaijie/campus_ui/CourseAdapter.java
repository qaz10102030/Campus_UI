package net.kaijie.campus_ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/10/6.
 */
public class CourseAdapter extends ArrayAdapter<Course> {
    private List<Course> courseList = new ArrayList<>();

    public CourseAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Course object) {
        courseList.add(object);
        super.add(object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Course courseObj = getItem(position);
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.add_course_item,parent,false);
        TextView tv_day = (TextView) convertView.findViewById(R.id.tv_day);
        TextView tv_serial = (TextView) convertView.findViewById(R.id.tv_serial);
        TextView tv_schedule = (TextView) convertView.findViewById(R.id.tv_schedule);
        TextView tv_require = (TextView) convertView.findViewById(R.id.tv_require);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_teacher = (TextView) convertView.findViewById(R.id.tv_teacher);
        String forDay = courseObj.getday() + "";
        tv_day.setText(forDay);
        tv_serial.setText(courseObj.getserial());
        tv_schedule.setText(courseObj.getschedule_display());
        tv_require.setText(courseObj.getrequire());
        tv_name.setText(courseObj.getname());
        tv_teacher.setText(courseObj.getteacher());
        return convertView;
    }
}
