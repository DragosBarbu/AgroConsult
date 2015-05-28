package com.agro.gusutri.agroconsult;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agro.gusutri.agroconsult.Service.Service;
import com.agro.gusutri.agroconsult.model.ProblemEvent;
import com.agro.gusutri.agroconsult.model.Solution;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by dragos on 5/21/15.
 */
public class ProblemListAdapter extends BaseAdapter {
    private ArrayList<ProblemEvent> items = new ArrayList<>();
    private LayoutInflater mInflater;

    public ProblemListAdapter(Context context, ArrayList<ProblemEvent> events) {
        items = events;
        mInflater = LayoutInflater.from(context);

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = mInflater.inflate(R.layout.row_problems, null);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.row_problem_header);
        TextView txtSubtitle = (TextView) convertView.findViewById(R.id.row_problem_sub_header);
        TextView txtRightSubtitle = (TextView) convertView.findViewById(R.id.row_problem_surface);
        TextView txtSolutionHeader = (TextView) convertView.findViewById(R.id.row_problem_solution_header);
        final RelativeLayout relSolution = (RelativeLayout) convertView.findViewById(R.id.row_problem_solution_layout);
        RelativeLayout expandLayout = (RelativeLayout) convertView.findViewById(R.id.row_problem_expand_layout);
        final ImageView toggleIcon = (ImageView) convertView.findViewById(R.id.row_problem_toggle_icon);
        LinearLayout itemLayout= (LinearLayout) convertView.findViewById(R.id.row_field_full_layout);

        ProblemEvent problemEvent = items.get(position);

        txtTitle.setText(problemEvent.getCategoryName());
        txtSubtitle.setText(problemEvent.getDetails());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        txtRightSubtitle.setText(sdf.format(problemEvent.getDate()));
        expandLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (relSolution.isShown()) {
                    collapse(relSolution);
                    toggleIcon.setImageResource(R.drawable.ic_action_expand);
                } else {
                    expand(relSolution);
                    toggleIcon.setImageResource(R.drawable.ic_action_collapse);
                }
            }
        });
        ArrayList<Solution> solutions= problemEvent.getSolutions();
        txtSolutionHeader.setText(Service.getInstance().getSolutionsAsString(solutions));

        collapse(relSolution);
        if (position % 2 == 1)
            itemLayout.setBackgroundColor(Color.argb(255,235,235,235));
        else
            itemLayout.setBackgroundColor(Color.argb(255,215,215,215));

        return convertView;
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

}
