package com.wisnu.datetimerangepickerandroid;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MonthView extends LinearLayout {

    TextView title;
    TextView currentmonth;
    TextView year;

    CalendarGridView grid;
    View dayNamesHeaderRowView;
    private Listener listener;
    private List<CalendarCellDecorator> decorators;
    private boolean isRtl;
    private Locale locale;
    private boolean alwaysDigitNumbers;

    public static MonthView create(ViewGroup parent, LayoutInflater inflater,
        DateFormat weekdayNameFormat, Listener listener, Calendar today, int dividerColor,
        int dayBackgroundResId, int dayTextColorResId, int titleTextStyle, boolean displayHeader,
        int headerTextColor, int dayHeaderTextColor, boolean showDayNamesHeaderRowView, Locale locale,
        boolean showAlwaysDigitNumbers, DayViewAdapter adapter) {
        return create(parent, inflater, weekdayNameFormat, listener, today, dividerColor,
            dayBackgroundResId, dayTextColorResId, titleTextStyle, displayHeader, headerTextColor, dayHeaderTextColor,
            showDayNamesHeaderRowView, showAlwaysDigitNumbers, null, locale, adapter);
    }

    public static MonthView create(ViewGroup parent, LayoutInflater inflater,
        DateFormat weekdayNameFormat, Listener listener, Calendar today, int dividerColor,
        int dayBackgroundResId, int dayTextColorResId, int titleTextStyle, boolean displayHeader,
        int headerTextColor, int dayHeaderTextColor, boolean displayDayNamesHeaderRowView, boolean showAlwaysDigitNumbers,
        List<CalendarCellDecorator> decorators, Locale locale, DayViewAdapter adapter) {
        final MonthView view = (MonthView) inflater.inflate(R.layout.month, parent, false);

        // Set the views
        view.title = new TextView(new ContextThemeWrapper(view.getContext(), titleTextStyle));
        view.currentmonth = new TextView(new ContextThemeWrapper(view.getContext(), titleTextStyle));
        view.year = new TextView(new ContextThemeWrapper(view.getContext(), titleTextStyle));

        view.grid = (CalendarGridView) view.findViewById(R.id.calendar_grid);
        view.dayNamesHeaderRowView = view.findViewById(R.id.day_names_header_row);
        //view.dayNamesHeaderRowView.setVisibility(GONE);

//        view.month = view.findViewById(R.id.month);
//        view.year = view.findViewById(R.id.year);

        // Add the month title as the first child of MonthView

        RelativeLayout temp = new RelativeLayout(view.getContext());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        view.title.setTextColor(Color.BLACK);
        view.currentmonth.setTextColor(Color.BLACK);

        view.title.setTypeface(null, Typeface.BOLD);
        view.currentmonth.setTypeface(null, Typeface.BOLD);

        view.title.setBackgroundColor(Color.WHITE);
        view.currentmonth.setBackgroundColor(Color.WHITE);

        temp.addView(view.title, params);
        temp.addView(view.currentmonth, params1);
        view.addView(temp, 0);



//        int dividerHeight = getResources().getDisplayMetrics().density * 1; // 1dp to pixels
//        viewDivider.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight));
        //view.addView(view.currentmonth, 1);



        view.setDayViewAdapter(adapter);
        view.setDividerColor(dividerColor);
        view.setDayTextColor(dayTextColorResId);
        view.setDisplayHeader(displayHeader);
        view.setHeaderTextColor(headerTextColor);

        if (dayBackgroundResId != 0) {
            view.setDayBackground(dayBackgroundResId);
        }

        view.isRtl = isRtl(locale);
        view.locale = locale;
        view.alwaysDigitNumbers = showAlwaysDigitNumbers;
        int firstDayOfWeek = today.getFirstDayOfWeek();
        final CalendarRowView headerRow = (CalendarRowView) view.grid.getChildAt(0);

        if (displayDayNamesHeaderRowView) {
            final int originalDayOfWeek = today.get(Calendar.DAY_OF_WEEK);
            for (int offset = 0; offset < 7; offset++) {
                today.set(Calendar.DAY_OF_WEEK, getDayOfWeek(firstDayOfWeek, offset, view.isRtl));
                final TextView textView = (TextView) headerRow.getChildAt(offset);
                textView.setText(weekdayNameFormat.format(today.getTime()));
                textView.setTextColor(dayHeaderTextColor);
            }
            today.set(Calendar.DAY_OF_WEEK, originalDayOfWeek);
        }
//        } else {
//            view.dayNamesHeaderRowView.setVisibility(View.GONE);
//        }

        view.listener = listener;
        view.decorators = decorators;
        view.dayNamesHeaderRowView.setVisibility(View.GONE);
        return view;
    }

    private static int getDayOfWeek(int firstDayOfWeek, int offset, boolean isRtl) {
        int dayOfWeek = firstDayOfWeek + offset;
        if (isRtl) {
            return 8 - dayOfWeek;
        }
        return dayOfWeek;
    }

    private static boolean isRtl(Locale locale) {
        // TODO convert the build to gradle and use getLayoutDirection instead of this (on 17+)?
        final int directionality = Character.getDirectionality(locale.getDisplayName(locale).charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT
            || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDecorators(List<CalendarCellDecorator> decorators) {
        this.decorators = decorators;
    }

    public List<CalendarCellDecorator> getDecorators() {
        return decorators;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init(MonthDescriptor month, List<List<MonthCellDescriptor>> cells,
                     boolean displayOnly, Typeface titleTypeface, Typeface dateTypeface) {
        Logr.d("Initializing MonthView (%d) for %s", System.identityHashCode(this), month);
        long start = System.currentTimeMillis();
        title.setText(month.getLabel().split(" ")[0]);
        currentmonth.setText(month.getLabel().split(" ")[1]);



        NumberFormat numberFormatter;
        if (alwaysDigitNumbers) {
            numberFormatter = NumberFormat.getInstance(Locale.US);
        } else {
            numberFormatter = NumberFormat.getInstance(locale);
        }

        final int numRows = cells.size();
        grid.setNumRows(numRows);
        for (int i = 0; i < 6; i++) {
            if(grid.getChildAt(i + 1) instanceof CalendarRowView){
            CalendarRowView weekRow = (CalendarRowView) grid.getChildAt(i + 1);
          //  weekRow.setPadding(10,-20,10,-20);
//                LayoutParams params = new LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    );
//                params.setMargins(10, 10, 10, 10);
//                weekRow.setLayoutParams(params);

                        weekRow.setListener(listener);
            if (i < numRows) {
                weekRow.setVisibility(VISIBLE);
                List<MonthCellDescriptor> week = cells.get(i);
                for (int c = 0; c < week.size(); c++) {
                    MonthCellDescriptor cell = week.get(isRtl ? 6 - c : c);
                    CalendarCellView cellView = (CalendarCellView) weekRow.getChildAt(c);

                    String cellDate = numberFormatter.format(cell.getValue());
                    if (!cellView.getDayOfMonthTextView().getText().equals(cellDate)) {
                        cellView.getDayOfMonthTextView().setText(cellDate);
                    }
                    cellView.setEnabled(cell.isCurrentMonth());
                    cellView.setClickable(!displayOnly);

                    Calendar calendar = Calendar.getInstance();

                    // We'll set the date of the calendar to the following
                    // date. We can use constant variable in the calendar
                    // for months value (JANUARY - DECEMBER). Be informed that
                    // month in Java started from 0 instead of 1.
                    int year = 2021;
                    int monthNew = Calendar.FEBRUARY;
                    int date = 1;
                    // We have a new date of 2021-02-01
                    calendar.set(Integer.parseInt(cell.getDate().toGMTString().split(" ")[2]), cell.getDate().getMonth(), 1);

                    // Here we get the maximum days for the date specified
                    // in the calendar. In this case we want to get the number
                    // of days for february 2021
                    int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);


                    if("sat".equalsIgnoreCase(cell.getDate().toString().split(" ")[0])){
                        cellView.setIsSat(true);
                    }

                    if("sun".equalsIgnoreCase(cell.getDate().toString().split(" ")[0])){
                        cellView.setSun(true);
                    }

//                    if(1 == Integer.parseInt(cellView.getDayOfMonthTextView().getText().toString())){
//                        cellView.setFirstDay(true);
//                    }
                    // case not working
//                    if(maxDay == Integer.parseInt(cell.getDate().toGMTString().split(" ")[0])){
//                        cellView.setLastDay(true);
//                    }



                    cellView.setMonthYear(cell.getMonthYear());
                    cellView.setEnabled(cell.isSelectable());
                    cellView.setSelectable(cell.isSelectable());
                    cellView.setSelected(cell.isSelected());
                    cellView.setCurrentMonth(cell.isCurrentMonth());
                    cellView.setToday(cell.isToday());
                    cellView.setRangeState(cell.getRangeState());
                    cellView.setHighlighted(cell.isHighlighted());
                    cellView.setTag(cell);
//                    View temp = new View(getContext());
//                    temp.setLayoutParams(new LinearLayout.LayoutParams(
//                            15,
//                            15));
//                    cellView.addView(temp);
                    //cellView.sp
//                    cellView.setPadding(-40,-40,-40,-40);

//                    LayoutParams params = new LayoutParams(
//                            10,
//                            10
//                    );
//                    params.setMargins(10, 10, 10, 10);
//                    cellView.setLayoutParams(params);



                    if (null != decorators) {
                        for (CalendarCellDecorator decorator : decorators) {
                            decorator.decorate(cellView, cell.getDate());
                        }
                    }
                }

            } else {
                weekRow.setVisibility(GONE);
            }
        }

        if (titleTypeface != null) {
            title.setTypeface(titleTypeface);
        }
        if (dateTypeface != null) {
            grid.setTypeface(dateTypeface);
        }

        Logr.d("MonthView.init took %d ms", System.currentTimeMillis() - start);
    }}

    public void setDividerColor(int color) {
        grid.setDividerColor(color);
    }

    public void setDayBackground(int resId) {
        grid.setDayBackground(resId);
    }

    public void setDayTextColor(int resId) {
        grid.setDayTextColor(resId);
    }

    public void setDayViewAdapter(DayViewAdapter adapter) {
        grid.setDayViewAdapter(adapter);
    }

    public void setDisplayHeader(boolean displayHeader) {
        grid.setDisplayHeader(displayHeader);
    }

    public void setHeaderTextColor(int color) {
        grid.setHeaderTextColor(color);
    }

    public interface Listener {

        void handleClick(MonthCellDescriptor cell);
    }
}
