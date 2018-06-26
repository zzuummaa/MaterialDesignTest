package ru.zuma.materialdesigntest.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.Gravity;

import ru.zuma.materialdesigntest.R;

/**
 * Created by Stephan on 26.06.2018.
 */

public class RoundedTextView extends android.support.v7.widget.AppCompatTextView {
    /**
     * float px = someDpValue * DENSITY;
     * float dp = somePxValue / DENSITY;
     */
    private float DENSITY;

    private ShapeDrawable background;
    private int color;

    private void initRoundedTextView(Context context) {
        DENSITY = context.getResources().getDisplayMetrics().density;

        background = new ShapeDrawable();

        color = getResources().getColor(R.color.colorCommiBlue);
        Drawable background = getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();

        resizeShape();

        setTextColor(0xFFFFFFFF);

        int paddHoriz = (int) (8 * DENSITY);
        setPadding(paddHoriz, getPaddingTop(), paddHoriz, getPaddingBottom());

        setGravity(Gravity.CENTER);
    }

    public RoundedTextView(Context context) {
        super(context);
        initRoundedTextView(context);
    }

    public RoundedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRoundedTextView(context);
    }

    public RoundedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRoundedTextView(context);
    }

    protected void resizeShape() {
        int r = (int) (getHeight() / 2 * DENSITY);
        float[] radii = new float[]{r, r, r, r, r, r, r, r};
        RoundRectShape shape = new RoundRectShape(radii, null, null);
        background.setShape(shape);
        background.getPaint().setColor(color);
        setBackground(background);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resizeShape();
    }

    @Override
    public void setBackgroundColor(int color) {
        this.color = color;
        background.getPaint().setColor(color);
        setBackground(background);
    }
}
