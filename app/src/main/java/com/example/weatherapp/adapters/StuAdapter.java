package com.example.weatherapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.R;
import com.example.weatherapp.model.Stu;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StuAdapter extends ArrayAdapter<Stu> {
    private static final String TAG = "StuAdapter";
    // View lookup cache
    private static class ViewHolder {
        public ImageView ivCover;
        public TextView tvName;
        public TextView tvId;
        public Button stu_delete;
    }

    public StuAdapter(Context context, ArrayList<Stu> aStus) {
        super(context, 0, aStus);
    }

    // Translates a particular `Book` given a position
    // into a relevant row within an AdapterView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Stu stu = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_stu, parent, false);
            viewHolder.ivCover = (ImageView)convertView.findViewById(R.id.ivStuCover);
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.tvStuName);
            viewHolder.tvId = (TextView)convertView.findViewById(R.id.tvStuId);
            // item 中的按键
            final TextView id = (TextView) viewHolder.tvId;
            viewHolder.stu_delete = (Button)convertView.findViewById(R.id.stu_delete);
            viewHolder.stu_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Delete onClick: "+id.getText());
                    // 删除
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /*
        // Populate the data into the template view using the data object
        viewHolder.tvTitle.setText(stu.getTitle());
        viewHolder.tvAuthor.setText(stu.getAuthor());
        Picasso.with(getContext()).load(Uri.parse(stu.getCoverUrl())).error(R.drawable.ic_nocover).into(viewHolder.ivCover);
         */
        Picasso.with(getContext()).load(Uri.parse(stu.getCoverUrl())).error(R.drawable.ic_nocover).into(viewHolder.ivCover);
        viewHolder.tvName.setText(stu.getName());
        viewHolder.tvId.setText(stu.getId());
        // Return the completed view to render on screen
        return convertView;
    }
}
