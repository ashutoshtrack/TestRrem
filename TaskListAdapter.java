package com.example.ashutosh.testrreminderui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ashutosh on 5/31/2017.
 */


public class TaskListAdapter extends BaseAdapter {

    static TaskListAdapter ta;

    private Context context;
    private int Layout;
    private ArrayList<TaskWall> TaskList;

    public TaskListAdapter(Context context, int layout, ArrayList<TaskWall> taskList) {
        this.context = context;
        Layout = layout;
        TaskList = taskList;
    }

    @Override
    public int getCount() {
        return TaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return TaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        ImageView img2;
        TextView txtName,txtAmount,dudateText,nteText;
        ImageButton imgeydu;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ta = this;
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(Layout,null);

            holder.txtName = (TextView)row.findViewById(R.id.txtNme);
            holder.txtAmount = (TextView)row.findViewById(R.id.txtAmt);
            holder.img2 = (ImageView)row.findViewById(R.id.imager);
            holder.imgeydu =(ImageButton) row.findViewById(R.id.dustbin);
            holder.dudateText = (TextView) row.findViewById(R.id.duedateText);
            holder.nteText = (TextView) row.findViewById(R.id.noteText);


            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }

        TaskWall taskwall = TaskList.get(position);

        holder.txtName.setText(taskwall.getName());
        holder.txtAmount.setText(taskwall.getAmount());
        holder.dudateText.setText(taskwall.getDuedate());
        holder.nteText.setText(taskwall.getNotes());

        byte[] taskImage = taskwall.getPhotos();
        Bitmap bitmap = BitmapFactory.decodeByteArray(taskImage, 0 , taskImage.length);

        holder.img2.setImageBitmap(bitmap);

        holder.imgeydu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainReminder.mr.cow(v);
            }
        });
        return row;
    }


}
