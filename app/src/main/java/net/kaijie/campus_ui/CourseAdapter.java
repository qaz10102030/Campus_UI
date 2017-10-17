package net.kaijie.campus_ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by User on 2017/10/6.
 */
public class CourseAdapter extends ArrayAdapter<Course> {
    public List<Course> courseList = new ArrayList<>();
    private CourseFilter courseFilter;

    public CourseAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Course object) {
        if(object.getForSearch()) {
            super.add(object);
        }
        else {
            courseList.add(object);
            super.add(object);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Course courseObj = getItem(position);
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(courseObj.getIsNoClasss())
        {
            convertView = inflater.inflate(R.layout.no_course_item,parent,false);
            TextView tv_noClass = (TextView) convertView.findViewById(R.id.tv_no_class);
            tv_noClass.setText(courseObj.getname());
        }else{
            convertView = inflater.inflate(R.layout.add_course_item,parent,false);
            TextView tv_day = (TextView) convertView.findViewById(R.id.tv_day);
            TextView tv_serial = (TextView) convertView.findViewById(R.id.tv_serial);
            TextView tv_schedule = (TextView) convertView.findViewById(R.id.tv_schedule);
            TextView tv_require = (TextView) convertView.findViewById(R.id.tv_require);
            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            TextView tv_teacher = (TextView) convertView.findViewById(R.id.tv_teacher);
            TextView tv_classfor = (TextView) convertView.findViewById(R.id.tv_classfor);
            String forDay = courseObj.getday() + "";
            tv_day.setText(forDay);
            tv_serial.setText(courseObj.getserial());
            tv_schedule.setText(courseObj.getschedule_display());
            tv_require.setText(courseObj.getrequire());
            tv_name.setText(courseObj.getname());
            tv_teacher.setText(courseObj.getteacher());
            tv_classfor.setText(courseObj.getclass_for());
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (courseFilter == null) {
            courseFilter = new CourseFilter();
        }
        return courseFilter;
    }

    private class CourseFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Course> filterList = new ArrayList<>();
            FilterResults filterResults = new FilterResults();

            for(int i = 0;i<courseList.size();i++) {
                Course searchCourse = courseList.get(i);
                if (searchCourse.getserial().contains(constraint.toString()))
                    filterList.add(courseList.get(i));
            }
            if(filterList.size() == 0){
                Course NoCourse = new Course();
                NoCourse.setname(": ( 沒有符合條件的結果");
                NoCourse.setIsNoClass(true);
                NoCourse.setForSearch(true);
                filterList.add(NoCourse);
            }

            filterResults.values = filterList;
            filterResults.count = filterList.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            List<Course> resultCourse = (List<Course>) results.values;
            for (int i = 0; i < results.count; i++) {
                resultCourse.get(i).setForSearch(true);
                add(resultCourse.get(i));
            }
            notifyDataSetChanged();

        }
    }
}
