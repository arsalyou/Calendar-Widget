package com.wisnu.datetimerangepickerandroid;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.Calendar;

public class CalendarCellView extends FrameLayout {

    private static final int[] STATE_SELECTABLE = {
        R.attr.state_selectable
    };
    private static final int[] STATE_CURRENT_MONTH = {
        R.attr.state_current_month
    };
    private static final int[] STATE_TODAY = {
        R.attr.state_today
    };
    private static final int[] STATE_HIGHLIGHTED = {
        R.attr.state_highlighted
    };
    private static final int[] STATE_RANGE_FIRST = {
        R.attr.state_range_first
    };
    private static final int[] STATE_RANGE_MIDDLE = {
        R.attr.state_range_middle
    };
    private static final int[] STATE_RANGE_LAST = {
        R.attr.state_range_last
    };

    private static final int[] STATE_RANGE_UNSELECTED = {
            R.attr.state_bg_unselected
    };

    private static final int[] STATE_RIGHT_EDGE = {
            R.attr.state_right_edge
    };

    private static final int[] STATE_LEFT_EDGE = {
            R.attr.state_left_edge
    };

    private boolean isSelectable = false;
    private boolean isCurrentMonth = false;
    private boolean isToday = false;
    private boolean isHighlighted = false;
    private RangeState rangeState = RangeState.NONE;
    private TextView dayOfMonthTextView;
    private boolean isSat = false;
    private boolean isSun = false;
    private boolean isFirstDay = false;
    private boolean isLastDay = false;
    private MonthDescriptor monthYear;

    @SuppressWarnings("UnusedDeclaration") //
    public CalendarCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSelectable(boolean isSelectable) {
        if (this.isSelectable != isSelectable) {
            this.isSelectable = isSelectable;
            refreshDrawableState();
        }
    }

    public void setCurrentMonth(boolean isCurrentMonth) {
        if (this.isCurrentMonth != isCurrentMonth) {
            this.isCurrentMonth = isCurrentMonth;
            refreshDrawableState();
        }
    }

    public void setToday(boolean isToday) {
        if (this.isToday != isToday) {
            this.isToday = isToday;
            //dayOfMonthTextView.setBackgroundResource(android.R.color.transparent);
            refreshDrawableState();
        }
    }

    public void setMonthYear(MonthDescriptor monthYear) {
        this.monthYear = monthYear;
    }

    public void setRangeState(RangeState rangeState) {
        if (this.rangeState != rangeState) {
            this.rangeState = rangeState;
            refreshDrawableState();
        }
    }

    public void setIsSat(Boolean isSat){
        this.isSat = isSat;
    }

    public void setSun(boolean sun) {
        isSun = sun;
    }

    public void setFirstDay(boolean firstDay) {
        isFirstDay = firstDay;
    }

    public void setLastDay(boolean lastDay) {
        isLastDay = lastDay;
    }

    public void setHighlighted(boolean isHighlighted) {
        if (this.isHighlighted != isHighlighted) {
            this.isHighlighted = isHighlighted;
            refreshDrawableState();
        }
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public boolean isToday() {
        return isToday;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public RangeState getRangeState() {
        return rangeState;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 5);


//        GradientDrawable gd1 = new GradientDrawable();
//        gd1.setShape(GradientDrawable.OVAL);
//        gd1.setColor(Color.TRANSPARENT);
//        gd1.setStroke(1, Color.WHITE);
//        gd1.setSize(10,dayOfMonthTextView.getHeight()-10);
//        //Log.d("DB121","Width "+tv.getHeight()+ "Height ="+tv.getHeight());
//        dayOfMonthTextView.setBackground(gd1);
        if (isSelectable) {
            mergeDrawableStates(drawableState, STATE_SELECTABLE);
            //dayOfMonthTextView.setTypeface(null, Typeface.BOLD);
        }

        if (isCurrentMonth) {
            mergeDrawableStates(drawableState, STATE_CURRENT_MONTH);
        }

        if (isToday) {
            mergeDrawableStates(drawableState, STATE_TODAY);
            //dayOfMonthTextView.setBackgroundResource(android.R.color.transparent);
        }

        if (isHighlighted) {
            mergeDrawableStates(drawableState, STATE_HIGHLIGHTED);
            dayOfMonthTextView.setTypeface(null, Typeface.BOLD);
        }
        dayOfMonthTextView.setBackgroundResource(android.R.color.transparent);
        dayOfMonthTextView.setTypeface(null, Typeface.NORMAL);
        if (rangeState == RangeState.FIRST) {
            mergeDrawableStates(drawableState, STATE_RANGE_FIRST);
            dayOfMonthTextView.setTypeface(null, Typeface.BOLD);
            //dayOfMonthTextView.setBackgroundResource(android.R.color.transparent);
//            Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.state_range_first);
//            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
//            DrawableCompat.setTint(wrappedDrawable, Color.RED);



            //this.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.state_range_first));
        }
        else if (rangeState == RangeState.MIDDLE) {
            Calendar calendar = Calendar.getInstance();
            int year = monthYear.getYear();
            int month = monthYear.getMonth();
            int date = 1;
            // We have a new date of 2021-02-01
            calendar.set(year, month, 1);

            // Here we get the maximum days for the date specified
            // in the calendar. In this case we want to get the number
            // of days for february 2021
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            if(isSat || (maxDay ==  Integer.parseInt(dayOfMonthTextView.getText().toString()))) {
                mergeDrawableStates(drawableState, STATE_RIGHT_EDGE);



            }
            else if(isSun || (1 == Integer.parseInt(dayOfMonthTextView.getText().toString())))
                mergeDrawableStates(drawableState, STATE_LEFT_EDGE);
            else
                mergeDrawableStates(drawableState, STATE_RANGE_MIDDLE);
//            Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.state_range_middle);
//            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
//            DrawableCompat.setTint(wrappedDrawable, Color.RED);
//            this.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.state_range_middle));
        }
        else if (rangeState == RangeState.LAST) {
            mergeDrawableStates(drawableState, STATE_RANGE_LAST);
            dayOfMonthTextView.setTypeface(null, Typeface.BOLD);


//            Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.state_range_last);
//            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
//            DrawableCompat.setTint(wrappedDrawable, Color.RED);
//            this.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.state_range_last));


//            Drawable unwrappedDrawable1 = AppCompatResources.getDrawable(getContext(), R.drawable.test);
//            Drawable wrappedDrawable1= DrawableCompat.wrap(unwrappedDrawable1);
//            DrawableCompat.setTint(wrappedDrawable1, Color.YELLOW);
//            this.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.test));

        }
        else if(isSelectable && !isHighlighted )
            {
                GradientDrawable gd = new GradientDrawable();
                gd.setShape(GradientDrawable.OVAL);
                gd.setColor(Color.TRANSPARENT);
                gd.setStroke(1, Color.LTGRAY);
                gd.setSize(10,dayOfMonthTextView.getHeight()-10);
                dayOfMonthTextView.setBackground(gd);

                if (rangeState == RangeState.FIRST) {
                    dayOfMonthTextView.setBackgroundResource(android.R.color.transparent);
                    dayOfMonthTextView.setTypeface(null, Typeface.BOLD);

                }


               // this.setBackgroundColor(ContextCompat.getDrawable(getContext(), R.drawable.state_range_middle));

//                Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.state_range_first);
//                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
//                DrawableCompat.setTint(wrappedDrawable, Color.RED);
//
//
//
//                this.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.state_range_middle));

            }
        else if(isSelectable && isHighlighted )
        {
            dayOfMonthTextView.setBackgroundResource(android.R.color.transparent);
        }

               //mergeDrawableStates(drawableState, STATE_RANGE_UNSELECTED);



        //dayOfMonthTextView.setBackgroundColor(Color.BLUE);


//        Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.state_range_middle);
//        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
//        DrawableCompat.set(wrappedDrawable, Color.RED);

//        View view = findViewById(R.drawable.state_range_first);
//
//        GradientDrawable drawable = (GradientDrawable) view.getBackground();
//        drawable.setStroke(3, Color.RED); // set stroke width and stroke color

        return drawableState;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setDayOfMonthTextView(TextView textView) {
        dayOfMonthTextView = textView;
        dayOfMonthTextView.setTextColor(Color.BLACK);

//        GradientDrawable gd = new GradientDrawable();
//        gd.setShape(GradientDrawable.OVAL);
//        gd.setColor(Color.TRANSPARENT);
//        gd.setStroke(1, Color.GRAY);
//        gd.setSize(10,dayOfMonthTextView.getHeight()-10);
//        //Log.d("DB121","Width "+tv.getHeight()+ "Height ="+tv.getHeight());
//        dayOfMonthTextView.setBackground(gd);
        //dayOfMonthTextView.setPadding(-10,-10,-10,10);
    }

    public TextView getDayOfMonthTextView() {
        if (dayOfMonthTextView == null) {
            throw new IllegalStateException(
                "You have to setDayOfMonthTextView in your custom DayViewAdapter."
            );
        }
        return dayOfMonthTextView;
    }

}
