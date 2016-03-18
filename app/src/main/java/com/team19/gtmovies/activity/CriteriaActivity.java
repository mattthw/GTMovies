package com.team19.gtmovies.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team19.gtmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Austin Leal on 3/7/2016.
 *
 * @author Austin Leal
 * @version 1.0
 */
public class CriteriaActivity extends RelativeLayout {

    private List<Chip> chipList;

    /**
     * Public constructor for ChipView.
     * @param context current context
     */
    public CriteriaActivity(Context context) {
        super(context);
        setupChipList();
    }

    public CriteriaActivity(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setupChipList();
    }

    private void setupChipList() {
        String[] stringArray = getResources().getStringArray(R.array.criteria);
        chipList = new ArrayList<>(stringArray.length);
        for (String text : getResources().getStringArray(R.array.criteria)) {
            chipList.add(new Chip(text));
        }
    }

    /**
     * Public inner class of chips
     */
    public class Chip implements OnClickListener {
        private View mView;
        private TextView mTextView;

        private String mText;

        /**
         * Constructor for chip
         * @param text text to display on chip
         */
        public Chip(String text) {
            mText = text;
        }


        /**
         * getter for view
         * @return chip view
         */
        public View getView() {
            if (mView == null) {
                mView = inflate(getContext(), R.layout.chip, null);
                mTextView = (TextView) mView.findViewById(R.id.chip_text_view);
            }

            LinearLayout linearLayout = new LinearLayout(getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(0, 0);
            linearLayout.setLayoutParams(params);
            linearLayout.setFocusable(true);
            linearLayout.setFocusableInTouchMode(true);

            addView(linearLayout);
            updateViews();
            return mView;
        }

        /**
         * Updates the view
         */
        private void updateViews() {
            if (mTextView != null) {
                mTextView.setText(mText);
            }
        }

        @Override
        public void onClick(View v) {
            //implement
        }
    }

}
