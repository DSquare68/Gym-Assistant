package com.example.daniel.extraview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.daniel.gymassistant.R;

/**
 * Created by Daniel on 2018-02-05.
 */

public class SettingsCategory extends PreferenceCategory {
    ColorStateList textColor;
    Float fontSize;
    public SettingsCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.settings_category,
                0, 0);

        try {
            textColor = a.getColorStateList(R.styleable.settings_category_text_color);
            fontSize = a.getDimension(R.styleable.settings_category_font_size, TypedValue.COMPLEX_UNIT_DIP);
        } finally {
            a.recycle();
        }

    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        titleView.setTextColor(textColor);
        titleView.setTextSize(fontSize);
    }

}
