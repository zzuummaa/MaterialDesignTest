package ru.zuma.materialdesigntest.view;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.Gravity;

import ru.zuma.materialdesigntest.R;

public class RoundedEditText extends android.support.v7.widget.AppCompatEditText {
    /**
     * float px = someDpValue * DENSITY;
     * float dp = somePxValue / DENSITY;
     */
    private float DENSITY;

    private ShapeDrawable background;
    private int color;

    public RoundedEditText(Context context) {
        super(context);
        initRoundedEditText(context);
    }

    public RoundedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRoundedEditText(context);
    }

    public RoundedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRoundedEditText(context);
    }

    private void initRoundedEditText(Context context) {
        DENSITY = context.getResources().getDisplayMetrics().density;

        background = new ShapeDrawable();

        color = getResources().getColor(R.color.colorCommiBlue);
        Drawable background = getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();

        resizeShape();

        int paddHoriz = (int) (8 * DENSITY);
        if (getPaddingLeft() < paddHoriz) {
            setPadding(paddHoriz, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
        if (getPaddingRight() < paddHoriz) {
            setPadding(getPaddingLeft(), getPaddingTop(), paddHoriz, getPaddingBottom());
        }

        setGravity(Gravity.CENTER);
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
        //setBackground(background);
    }
}
