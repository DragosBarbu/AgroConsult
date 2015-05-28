package com.agro.gusutri.agroconsult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agro.gusutri.agroconsult.model.Dao;
import com.agro.gusutri.agroconsult.model.Field;
import com.agro.gusutri.agroconsult.model.Task;

import java.util.ArrayList;

/**
 * Created by dragos on 4/16/15.
 */
public class FieldListAdapter extends BaseAdapter {

    private ArrayList items = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    // private int listID;

    public FieldListAdapter(Context context, ArrayList items) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.items = items;
        // listID = listType;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.

        // When convertView is not null, we can reuse it directly, there is no need
        // to re inflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        ViewHolderItem viewHolder;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.row_fields, null);
            viewHolder = new ViewHolderItem();
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.row_field_header);
            viewHolder.txtSubtitle = (TextView) convertView.findViewById(R.id.row_field_sub_header);
            viewHolder.txtRightSubtitle = (TextView) convertView.findViewById(R.id.row_field_surface);

            convertView.setTag(viewHolder);

        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            viewHolder = (ViewHolderItem) convertView.getTag();

        }

        // switch (listID) {
        //     case UserInfoFragment.USER_INFO_LIST:

        //         break;
        //     case FieldFragment.FIELD_LIST:
        final Field field = (Field) items.get(position);
        viewHolder.txtTitle.setText(field.getSirupCode());
        viewHolder.txtSubtitle.setText(field.getCrop().getCropType() + "");
        viewHolder.txtRightSubtitle.setText(String.format("%.2f", field.getArea()/10000 )+ " ha");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FieldDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Dao.FIELD, field);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        //       break;
        // }


        return convertView;
    }

    static class ViewHolderItem {
        TextView txtTitle;
        TextView txtSubtitle;
        TextView txtRightSubtitle;
    }

}