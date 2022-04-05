package com.wisnu.date_range_picker_android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wisnu.datetimerangepickerandroid.CalendarPickerView;
import com.wisnu.datetimerangepickerandroid.CalendarRowView;
import com.wisnu.datetimerangepickerandroid.MonthCellDescriptor;

import com.wisnu.datetimerangepickerandroid.SharedViewModel;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    Date selectedDate = new Date();
    private TimeZone timeZone;
    private Locale locale;
    Calendar today;
    Spinner yearSpinner;
    private DateFormat weekdayNameFormat;
    private SharedViewModel viewModel;
    boolean click = true;
    EditText text;
    EditText text2;






    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);

//        final LayoutInflater inflater = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final Button b = (Button) findViewById(R.id.btn);
//        final View pview = inflater.inflate(R.layout.popup,
//                (ViewGroup) findViewById(R.layout.main));
//        final PopupWindow pw = new PopupWindow(pview);

        getSupportActionBar().hide();
        final CalendarPickerView cal = findViewById(R.id.calendar_view);
        cal.init(
                buildMinimumDate(),
                buildMaximumDate(),
                this
        ).inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDates(getSelectedDates());





                //cal.setOnDateSelectedListener()
//        cal.init(
//                DateTime.now(DateTimeZone.UTC).toDate(),
//                buildMaximumDate(),
//                this
//        )


        cal.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(List<MonthCellDescriptor> date) {
                Log.d("onMinDateResolved", date.toString());
                if(date.size() > 1 ){
                    int month = date.get(0).getDate().getMonth();
                    String[] dateElements = date.get(0).getDate().toString().split(" ");
                    String day = dateElements[2];
                    String monthName = dateElements[1];
                    String year = dateElements[5];

                    Toast.makeText(getApplicationContext(),date.get(1).getDateString(),
                            Toast.LENGTH_LONG).show();


                }else
                    Toast.makeText(getApplicationContext(),date.get(0).getDateString(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDateUnselected(List<MonthCellDescriptor> date) {
                Log.d("onMinDateResolved", date.toString());
            }
        });




//        final Button button1 = findViewById(R.id.button1);
//        final Button button2 = findViewById(R.id.btn);
        final String[] years = { "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027"};



        final MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems( years);
        spinner.getItems().get(0);
        spinner.setHintColor(R.color.colorPrimaryDark);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                cal.scrollToYear(Integer.parseInt(item.toString()));

            }
        });


        timeZone = TimeZone.getDefault();
        locale = Locale.getDefault();
        today = Calendar.getInstance(timeZone, locale);
//        yearSpinner = findViewById(R.id.yearSpinner);
//        ArrayAdapter ad = new ArrayAdapter(this, R.layout.year_filter_item, years);
//        yearSpinner.setAdapter(ad);

//        b.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (click) {
//                    // if onclick written here, it gives null pointer exception.
//                    // if onclick is written here it gives runtime exception.
//                    pw.showAtLocation(v, Gravity.CENTER_HORIZONTAL, 0, 0);
//                    pw.update(8, 0, 250, 200);
//                    String[] array = new String[] { "tushar", "pandey",
//                            "almora" };
//
//                    ListView lst = (ListView) pview.findViewById(R.id.listview);
//                    MyAdapter adapter = new MyAdapter(getApplicationContext());
//                    lst.setAdapter(adapter);
//                    lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> arg0, View arg1,
//                                                int arg2, long arg3) {
//                            Toast.makeText(MainActivity.this, "pandey",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                    });
//                    click = false ;
//                }
//                else
//                {
//                    pw.dismiss();
//                    click = true;
//                }
//
//            }
//        });

        viewModel.getDropDownyear().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //if state false
//        if(null != viewModel.getState().getValue() &&  !viewModel.getState().getValue() && (viewModel.getFirstVisiblePosition().getValue()% 12) == 0){
                if(null != viewModel.getState().getValue() &&  !viewModel.getState().getValue()){
//          Month month = calendarConstraints.getStart().monthsLater(viewModel.getFirstVisiblePosition().getValue());
//          String monthYear = month.getLongName();
//          String[] headerDetail = monthYear.split(" ");
//        viewHolder.monthTitle.setText(headerDetail[0]);
//        viewHolder.yearTitle.setText(headerDetail[1]);

//          int updatedIndex = getIndex(yearSpinner, headerDetail[1]);
//          yearSpinner.setSelection(updatedIndex);
//                    viewModel.setDropDownState(true);
//                    int updatedIndex = getIndex(yearSpinner, s);
//                    yearSpinner.setSelection(updatedIndex);
                }

            }
        });

//        cal.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                v.getfi
//            }
//        });





        cal.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.i("SCROLLING DOWN","TRUE");
//                String a = view.getAdapter().getItem(mLastFirstVisibleItem).toString();
//                a = a.replace("'","");
//                a = a.replace(",","");
//                String year = a.split(" ")[1];
//                spinner.setSelectedIndex(getIndex(years, year));

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(mLastFirstVisibleItem<firstVisibleItem)
                {
                    Log.i("SCROLLING DOWN","TRUE");
                }
                if(mLastFirstVisibleItem>firstVisibleItem)
                {
                    Log.i("SCROLLING UP","TRUE");
                }
                mLastFirstVisibleItem=firstVisibleItem;
                Log.i("SCROLLING DOWN","TRUE");

                int pos = cal.getFirstVisiblePosition();
                if (cal.getChildCount() > 1 && cal.getChildAt(0).getTop() < 0) pos++;
                String a = view.getAdapter().getItem(pos).toString();
                //String a = view.getAdapter().getItem(mLastFirstVisibleItem).toString();
                a = a.replace("'","");
                a = a.replace(",","");
                String year = a.split(" ")[1];
                spinner.setSelectedIndex(getIndex(years, year));
                spinner.getListView().setSelection(getIndex(years, year));
                spinner.scrollBy(0, 2);
                //spinner.

                Object item = cal.getItemAtPosition(pos);


            }
        });



//      cal.setOnScrollListener(new OnScrollListener(){
//          public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//              // TODO Auto-generated method stub
//          }
//          public void onScrollStateChanged(AbsListView view, int scrollState) {
//              // TODO Auto-generated method stub
//              final ListView lw = getListView();
//
//              if(scrollState == 0)
//                  Log.i("a", "scrolling stopped...");
//
//
//              if (view.getId() == lw.getId()) {
//                  final int currentFirstVisibleItem = lw.getFirstVisiblePosition();
//                  if (currentFirstVisibleItem > mLastFirstVisibleItem) {
//                      mIsScrollingUp = false;
//                      Log.i("a", "scrolling down...");
//                  } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
//                      mIsScrollingUp = true;
//                      Log.i("a", "scrolling up...");
//                  }
//
//                  mLastFirstVisibleItem = currentFirstVisibleItem;
//              }
//          }
//      });



//        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                //cal.scrollToYear(2020);
//                Object item = parent.getItemAtPosition(pos);
////                ((TextView) parent.getChildAt(pos)).setTextColor(Color.BLACK);
////                ((TextView) parent.getChildAt(pos)).setTextSize(5);
//                //cal.scrollToYear(Integer.parseInt(item.toString()));
//
////                if (null != viewModel.getState().getValue() && viewModel.getState().getValue()) {
////                    if (pos > spinnerId) {
////                        Object item = parent.getItemAtPosition(pos);
////                        Month current = Month.create(Integer.parseInt(item.toString()), Calendar.FEBRUARY);
////                        CalendarConstraints calendarConstraints = calendar.getCalendarConstraints();
////                        Month moveTo = calendarConstraints.clamp(current);
////                        calendar.setCurrentMonth(moveTo);
////                    } else if (pos < spinnerId) {
////                        Object item = parent.getItemAtPosition(pos);
////                        Month current = Month.create(Integer.parseInt(item.toString()), Calendar.JANUARY);
////                        CalendarConstraints calendarConstraints = calendar.getCalendarConstraints();
////                        Month moveTo = calendarConstraints.clamp(current);
////                        calendar.setCurrentMonth(moveTo);
////                    }
////                    spinnerId = pos;
////                }
//                //observedYear = Boolean.FALSE;
//
//
//                //calendar.setSelector(MaterialCalendar.CalendarSelector.DAY);
//
//
//                //setCurrentMonth(current);
//
//            }
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

        cal.getSelectedDates();

        final int originalDayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        final CalendarRowView headerRow = findViewById(R.id.day_names_header_row);
        int firstDayOfWeek = today.getFirstDayOfWeek();
        weekdayNameFormat = new SimpleDateFormat(this.getString(com.wisnu.datetimerangepickerandroid.R.string.day_name_format), locale);
        for (int offset = 0; offset < 7; offset++) {
            today.set(Calendar.DAY_OF_WEEK, getDayOfWeek(firstDayOfWeek, offset, false));
            final TextView textView = (TextView) headerRow.getChildAt(offset);
            textView.setText(today.getTime().toString().split(" ")[0]);
            //textView.setText(weekdayNameFormat.format(today.getTime()));
            textView.setTextColor(Color.BLACK);
            textView.setTypeface(null, Typeface.BOLD);
        }

        //button1.setText(selectedDate.toString());
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Date temp = new Date(2019, 2, 2);
//                cal.scrollToYear(2020);
//
////                cal.init(
////                        DateTime.now(DateTimeZone.UTC).toDate(),
////                        DateTime.now(DateTimeZone.UTC).plusDays(10).toDate()
////                )
////                        .inMode(CalendarPickerView.SelectionMode.RANGE)
////                        .withSelectedDate(selectedDate);
//
////                cal.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
////                    @Override
////                    public void onDateSelected(Date date) {
////                        Log.d("onDateSelected", date.toString());
////                        selectedDate = date;
////                        button1.setText(selectedDate.toString());
////                    }
////
////                    @Override
////                    public void onDateUnselected(Date date) {
////
////                    }
////                });
//            }
//        });

        cal.setOnDateResolvedListener(new CalendarPickerView.OnDateResolvedListener() {
            @Override
            public void onMinDateResolved(Date date) {
                selectedDate = date;
                //button1.setText(selectedDate.toString());
                Log.d("onMinDateResolved", date.toString());
            }

            @Override
            public void onMaxDateResolved(Date date) {
                selectedDate = date;
               // button1.setText(selectedDate.toString());
                Log.d("onMaxDateResolved", date.toString());
            }
        });
    }

    private Date buildMinimumDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.YEAR, 2020);

        return calendar.getTime();
    }

    private  Collection<Date> getSelectedDates() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, 1);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH, 10);
        calendar1.set(Calendar.MONTH, 2);
        calendar1.set(Calendar.YEAR, 2020);
        Collection<Date> selectedDates = new ArrayList<>() ;
        selectedDates.add(calendar.getTime());
        selectedDates.add(calendar1.getTime());



        return selectedDates;
    }



    private Date buildMaximumDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.YEAR, 2028);

        return calendar.getTime();
    }

    private int getIndex(String[] years, String myString){
        for (int i=0;i<years.length;i++){
            if (years[i].equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }


    private static int getDayOfWeek(int firstDayOfWeek, int offset, boolean isRtl) {
        int dayOfWeek = firstDayOfWeek + offset;
        if (isRtl) {
            return 8 - dayOfWeek;
        }
        return dayOfWeek;
    }

}
