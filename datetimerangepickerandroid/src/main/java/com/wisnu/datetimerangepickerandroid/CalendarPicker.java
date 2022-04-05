//package com.wisnu.datetimerangepickerandroid;
//
//import static java.util.Calendar.DATE;
//import static java.util.Calendar.DAY_OF_MONTH;
//import static java.util.Calendar.DAY_OF_WEEK;
//import static java.util.Calendar.HOUR_OF_DAY;
//import static java.util.Calendar.MILLISECOND;
//import static java.util.Calendar.MINUTE;
//import static java.util.Calendar.MONTH;
//import static java.util.Calendar.SECOND;
//import static java.util.Calendar.YEAR;
//
//import android.graphics.Typeface;
//import android.text.format.DateUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Date;
//import java.util.Formatter;
//import java.util.List;
//import java.util.Locale;
//import java.util.TimeZone;
//
//public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
//
//    public enum SelectionMode {
//        SINGLE,
//        MULTIPLE,
//        RANGE
//    }
//    private final IndexedLinkedHashMap<String, List<List<MonthCellDescriptor>>> cells =
//            new IndexedLinkedHashMap<>();
//    final MonthView.Listener listener = new CalendarPickerView.CellClickedListener();
//    final List<MonthDescriptor> months = new ArrayList<>();
//    final List<MonthCellDescriptor> selectedCells = new ArrayList<>();
//    final List<MonthCellDescriptor> highlightedCells = new ArrayList<>();
//    final List<Calendar> selectedCals = new ArrayList<>();
//    final List<Calendar> highlightedCals = new ArrayList<>();
//    private Locale locale;
//    private TimeZone timeZone;
//    private DateFormat weekdayNameFormat;
//    private DateFormat fullDateFormat;
//    private Calendar minCal;
//    private Calendar maxCal;
//    private Calendar monthCounter;
//    private boolean displayOnly;
//    CalendarPickerView.SelectionMode selectionMode;
//    Calendar today;
//    private int dividerColor;
//    private int dayBackgroundResId;
//    private int dayTextColorResId;
//    private int titleTextStyle;
//    private boolean displayHeader;
//    private int headerTextColor;
//    private int dayHeaderTextColor;
//    private boolean displayDayNamesHeaderRow;
//    private boolean displayAlwaysDigitNumbers;
//    private Typeface titleTypeface;
//    private Typeface dateTypeface;
//
//  // Add listeners
//  private OnDateSelectedListener dateListener;
//    private DateSelectableFilter dateConfiguredListener;
//    private OnInvalidDateSelectedListener invalidDateListener =
//            new DefaultOnInvalidDateSelectedListener();
//    private CellClickInterceptor cellClickInterceptor;
//    private OnDateResolvedListener dateResolvedListener;
//    private List<CalendarCellDecorator> decorators;
//    private DayViewAdapter dayViewAdapter = new DefaultDayViewAdapter();
//
//    private boolean monthsReverseOrder;
//
//    private final StringBuilder monthBuilder = new StringBuilder(50);
//    private Formatter monthFormatter;
//
//    private static final ArrayList<String> explicitlyNumericYearLocaleLanguages =
//            new ArrayList<>(Arrays.asList("ar", "my"));
//    private List<String> data;
//    public CustomAdapter (List<String> data){
//        this.data = data;
//    }
//
//    @Override
//    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);
//        return new ViewHolder(rowItem);
//    }
//
//    @Override
//    public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {
//        holder.textView.setText(this.data.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return this.data.size();
//    }
//
//
//    private String monthKey(Calendar cal) {
//        return cal.get(YEAR) + "-" + cal.get(MONTH);
//    }
//
//    private String monthKey(MonthDescriptor month) {
//        return month.getYear() + "-" + month.getMonth();
//    }
//
//    private void clearOldSelections() {
//        for (MonthCellDescriptor selectedCell : selectedCells) {
//            // De-select the currently-selected cell.
//            selectedCell.setSelected(false);
//
//            if (dateListener != null) {
//                Date selectedDate = selectedCell.getDate();
//
//                if (selectionMode == CalendarPickerView.SelectionMode.RANGE) {
//                    int index = selectedCells.indexOf(selectedCell);
//                    if (index == 0 || index == selectedCells.size() - 1) {
//                        dateListener.onDateUnselected(selectedDate);
//                    }
//                } else {
//                    dateListener.onDateUnselected(selectedDate);
//                }
//            }
//        }
//        selectedCells.clear();
//        selectedCals.clear();
//    }
//
//    private Date applyMultiSelect(Date date, Calendar selectedCal) {
//        for (MonthCellDescriptor selectedCell : selectedCells) {
//            if (selectedCell.getDate().equals(date)) {
//                // De-select the currently-selected cell.
//                selectedCell.setSelected(false);
//                selectedCells.remove(selectedCell);
//                date = null;
//                break;
//            }
//        }
//        for (Calendar cal : selectedCals) {
//            if (sameDate(cal, selectedCal)) {
//                selectedCals.remove(cal);
//                break;
//            }
//        }
//        return date;
//    }
//
//    private void validateAndUpdate() {
//        if (getAdapter() == null) {
//            setAdapter(adapter);
//        }
//        adapter.notifyDataSetChanged();
//    }
//
//    private void scrollToSelectedMonth(final int selectedIndex) {
//        scrollToSelectedMonth(selectedIndex, true);
//    }
//
//    private void scrollToSelectedMonth(final int selectedIndex, final boolean smoothScroll) {
//        post(new Runnable() {
//            @Override
//            public void run() {
//                Logr.d("Scrolling to position %d", selectedIndex);
//
//                if (smoothScroll) {
//                    smoothScrollToPosition(selectedIndex);
//                } else {
//                    setSelection(selectedIndex);
//                }
//            }
//        });
//    }
//
//    private void scrollToSelectedDates() {
//        Integer selectedIndex = null;
//        Integer todayIndex = null;
//        Calendar today = Calendar.getInstance(timeZone, locale);
//        for (int c = 0; c < months.size(); c++) {
//            MonthDescriptor month = months.get(c);
//            if (selectedIndex == null) {
//                for (Calendar selectedCal : selectedCals) {
//                    if (sameMonth(selectedCal, month)) {
//                        selectedIndex = c;
//                        break;
//                    }
//                }
//                if (selectedIndex == null && todayIndex == null && sameMonth(today, month)) {
//                    todayIndex = c;
//                }
//            }
//        }
//        if (selectedIndex != null) {
//            scrollToSelectedMonth(selectedIndex);
//        } else if (todayIndex != null) {
//            scrollToSelectedMonth(todayIndex);
//        }
//    }
//
//    public boolean scrollToDate(Date date) {
//        Integer selectedIndex = null;
//
//        Calendar cal = Calendar.getInstance(timeZone, locale);
//        cal.setTime(date);
//        for (int c = 0; c < months.size(); c++) {
//            MonthDescriptor month = months.get(c);
//            if (sameMonth(cal, month)) {
//                selectedIndex = c;
//                break;
//            }
//        }
//        if (selectedIndex != null) {
//            scrollToSelectedMonth(selectedIndex);
//            return true;
//        }
//        return false;
//    }
//
//    public boolean scrollToYear(int year) {
//        Integer selectedIndex = null;
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_YEAR, 1);
//        calendar.set(Calendar.YEAR, year);
//
//        //Calendar cal = Calendar.getInstance(timeZone, locale);
//        //cal.setTime(date);
//        for (int c = 0; c < months.size(); c++) {
//            MonthDescriptor month = months.get(c);
//            if (sameMonth(calendar, month)) {
//                selectedIndex = c;
//                break;
//            }
//        }
//        if (selectedIndex != null) {
//            scrollToSelectedMonth(selectedIndex);
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * This method should only be called if the calendar is contained in a dialog, and it should only be called once, right after the dialog is shown (using {@link
//     * android.content.DialogInterface.OnShowListener} or {@link android.app.DialogFragment#onStart()}).
//     */
//
//
//    /**
//     * Set the typeface to be used for month titles.
//     */
//    public void setTitleTypeface(Typeface titleTypeface) {
//        this.titleTypeface = titleTypeface;
//        validateAndUpdate();
//    }
//
//    /**
//     * Sets the typeface to be used within the date grid.
//     */
//    public void setDateTypeface(Typeface dateTypeface) {
//        this.dateTypeface = dateTypeface;
//        validateAndUpdate();
//    }
//
//    /**
//     * Sets the typeface to be used for all text within this calendar.
//     */
//    public void setTypeface(Typeface typeface) {
//        setTitleTypeface(typeface);
//        setDateTypeface(typeface);
//    }
//
//    /**
//     * This method should only be called if the calendar is contained in a dialog, and it should only be called when the screen has been rotated and the dialog should be re-measured.
//     */
//    public void unfixDialogDimens() {
//        Logr.d("Reset the fixed dimensions to allow for re-measurement");
//        // Fix the layout height/width after the dialog has been shown.
//        getLayoutParams().height = AbsListView.LayoutParams.MATCH_PARENT;
//        getLayoutParams().width = AbsListView.LayoutParams.MATCH_PARENT;
//        requestLayout();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if (months.isEmpty()) {
//            throw new IllegalStateException(
//                    "Must have at least one month to display.  Did you forget to call init()?");
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
//
//    public Date getSelectedDate() {
//        return (selectedCals.size() > 0 ? selectedCals.get(0).getTime() : null);
//    }
//
//    public List<Date> getSelectedDates() {
//        List<Date> selectedDates = new ArrayList<>();
//        for (MonthCellDescriptor cal : selectedCells) {
//            selectedDates.add(cal.getDate());
//        }
//        Collections.sort(selectedDates);
//        return selectedDates;
//    }
//
//    /**
//     * Returns a string summarizing what the client sent us for init() params.
//     */
//    private static String dbg(Date minDate, Date maxDate) {
//        return "minDate: " + minDate + "\nmaxDate: " + maxDate;
//    }
//
//    /**
//     * Clears out the hours/minutes/seconds/millis of a Calendar.
//     */
//    static void setMidnight(Calendar cal) {
//        cal.set(HOUR_OF_DAY, 0);
//        cal.set(MINUTE, 0);
//        cal.set(SECOND, 0);
//        cal.set(MILLISECOND, 0);
//    }
//
//    private class CellClickedListener implements MonthView.Listener {
//
//        @Override
//        public void handleClick(MonthCellDescriptor cell) {
//            Date clickedDate = cell.getDate();
//
//            if (cellClickInterceptor != null && cellClickInterceptor.onCellClicked(clickedDate)) {
//                return;
//            }
//            if (!betweenDates(clickedDate, minCal, maxCal) || !isDateSelectable(clickedDate)) {
//                if (invalidDateListener != null) {
//                    invalidDateListener.onInvalidDateSelected(clickedDate);
//                }
//            } else {
//                boolean wasSelected = doSelectDate(clickedDate, cell);
//
//                if (dateListener != null) {
//                    if (wasSelected) {
//                        dateListener.onDateSelected(clickedDate);
//                    } else {
//                        dateListener.onDateUnselected(clickedDate);
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * Select a new date.  Respects the {@link CalendarPickerView.SelectionMode} this CalendarPickerView is configured with: if you are in {@link CalendarPickerView.SelectionMode#SINGLE}, the previously selected date will be un-selected.
//     * In {@link CalendarPickerView.SelectionMode#MULTIPLE}, the new date will be added to the list of selected dates.
//     * <p>
//     * If the selection was made (selectable date, in range), the view will scroll to the newly selected date if it's not already visible.
//     *
//     * @return - whether we were able to set the date
//     */
//    public boolean selectDate(Date date) {
//        return selectDate(date, false);
//    }
//
//    /**
//     * Select a new date.  Respects the {@link CalendarPickerView.SelectionMode} this CalendarPickerView is configured with: if you are in {@link CalendarPickerView.SelectionMode#SINGLE}, the previously selected date will be un-selected.
//     * In {@link CalendarPickerView.SelectionMode#MULTIPLE}, the new date will be added to the list of selected dates.
//     * <p>
//     * If the selection was made (selectable date, in range), the view will scroll to the newly selected date if it's not already visible.
//     *
//     * @return - whether we were able to set the date
//     */
//    public boolean selectDate(Date date, boolean smoothScroll) {
//        Date newDate = validateAndUpdate(date);
//
//        MonthCellWithMonthIndex monthCellWithMonthIndex = getMonthCellWithIndexByDate(newDate);
//        if (monthCellWithMonthIndex == null || !isDateSelectable(newDate)) {
//            return false;
//        }
//        boolean wasSelected = doSelectDate(newDate, monthCellWithMonthIndex.cell);
//        if (wasSelected) {
//            scrollToSelectedMonth(monthCellWithMonthIndex.monthIndex, smoothScroll);
//        }
//        return wasSelected;
//    }
//
//    /**
//     * Use {@link DateUtils} to format the dates.
//     *
//     * @see DateUtils
//     */
//    private String formatMonthDate(Date date) {
//        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
//                | DateUtils.FORMAT_NO_MONTH_DAY;
//
//        // Save default Locale
//        Locale defaultLocale = Locale.getDefault();
//
//        // Set new default Locale, the reason to do that is DateUtils.formatDateTime uses
//        // internally this method DateIntervalFormat.formatDateRange to format the date. And this
//        // method uses the default locale.
//        //
//        // More details about the methods:
//        // - DateUtils.formatDateTime: https://goo.gl/3YW52Q
//        // - DateIntervalFormat.formatDateRange: https://goo.gl/RRmfK7
//        Locale.setDefault(locale);
//
//        String dateFormatted;
//        if (displayAlwaysDigitNumbers
//                && explicitlyNumericYearLocaleLanguages.contains(locale.getLanguage())) {
//            StringBuilder sb = new StringBuilder();
//            SimpleDateFormat sdfMonth = new SimpleDateFormat(getContext()
//                    .getString(R.string.month_only_name_format), locale);
//            SimpleDateFormat sdfYear = new SimpleDateFormat(getContext()
//                    .getString(R.string.year_only_format), Locale.ENGLISH);
//            dateFormatted = sb.append(sdfMonth.format(date.getTime())).append(" ")
//                    .append(sdfYear.format(date.getTime())).toString();
//        } else {
//            // Format date using the new Locale
//            dateFormatted = DateUtils.formatDateRange(getContext(), monthFormatter,
//                    date.getTime(), date.getTime(), flags, timeZone.getID()).toString();
//        }
//        // Call setLength(0) on StringBuilder passed to the Formatter constructor to not accumulate
//        // the results
//        monthBuilder.setLength(0);
//
//        // Restore default Locale to avoid generating any side effects
//        Locale.setDefault(defaultLocale);
//
//        return dateFormatted;
//    }
//
//    private Date validateAndUpdate(Date date) {
//        if (date == null) {
//            throw new IllegalArgumentException("Selected date must be non-null.");
//        }
//        if (date.before(minCal.getTime())) {
//            if (dateResolvedListener != null) {
//                dateResolvedListener.onMinDateResolved(minCal.getTime());
//            }
//            return minCal.getTime();
//        }
//        if (date.after(maxCal.getTime())) {
//            if (dateResolvedListener != null) {
//                dateResolvedListener.onMaxDateResolved(maxCal.getTime());
//            }
//            return maxCal.getTime();
//        }
//        return date;
//    }
//
//    private boolean doSelectDate(Date date, MonthCellDescriptor cell) {
//        Calendar newlySelectedCal = Calendar.getInstance(timeZone, locale);
//        newlySelectedCal.setTime(date);
//        // Sanitize input: clear out the hours/minutes/seconds/millis.
//        setMidnight(newlySelectedCal);
//
//        // Clear any remaining range state.
//        for (MonthCellDescriptor selectedCell : selectedCells) {
//            selectedCell.setRangeState(RangeState.NONE);
//        }
//
//        switch (selectionMode) {
//            case RANGE:
//                if (selectedCals.size() > 1) {
//                    // We've already got a range selected: clear the old one.
//                    clearOldSelections();
//                } else if (selectedCals.size() == 1 && newlySelectedCal.before(selectedCals.get(0))) {
//                    // We're moving the start of the range back in time: clear the old start date.
//                    clearOldSelections();
//                }
//                break;
//
//            case MULTIPLE:
//                date = applyMultiSelect(date, newlySelectedCal);
//                break;
//
//            case SINGLE:
//                clearOldSelections();
//                break;
//            default:
//                throw new IllegalStateException("Unknown selectionMode " + selectionMode);
//        }
//
//        if (date != null) {
//            // Select a new cell.
//            if (selectedCells.size() == 0 || !selectedCells.get(0).equals(cell)) {
//                selectedCells.add(cell);
//                cell.setSelected(true);
//            }
//            selectedCals.add(newlySelectedCal);
//
//            if (selectionMode == CalendarPickerView.SelectionMode.RANGE && selectedCells.size() > 1) {
//                // Select all days in between start and end.
//                Date start = selectedCells.get(0).getDate();
//                Date end = selectedCells.get(1).getDate();
//                selectedCells.get(0).setRangeState(RangeState.FIRST);
//                selectedCells.get(1).setRangeState(RangeState.LAST);
//
//
//                int startMonthIndex = cells.getIndexOfKey(monthKey(selectedCals.get(0)));
//                int endMonthIndex = cells.getIndexOfKey(monthKey(selectedCals.get(1)));
//                for (int monthIndex = startMonthIndex; monthIndex <= endMonthIndex; monthIndex++) {
//                    List<List<MonthCellDescriptor>> month = cells.getValueAtIndex(monthIndex);
//                    for (List<MonthCellDescriptor> week : month) {
//                        for (MonthCellDescriptor singleCell : week) {
//                            if (singleCell.getDate().after(start)
//                                    && singleCell.getDate().before(end)
//                                    && singleCell.isSelectable()) {
//                                singleCell.setSelected(true);
//                                singleCell.setRangeState(RangeState.MIDDLE);
//                                selectedCells.add(singleCell);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        // Update the adapter.
//        validateAndUpdate();
//        return date != null;
//    }
//
//
//
//
//
//
//    public void highlightDates(Collection<Date> dates) {
//        for (Date date : dates) {
//            Date newDate = validateAndUpdate(date);
//
//            MonthCellWithMonthIndex monthCellWithMonthIndex = getMonthCellWithIndexByDate(newDate);
//            if (monthCellWithMonthIndex != null) {
//                Calendar newlyHighlightedCal = Calendar.getInstance(timeZone, locale);
//                newlyHighlightedCal.setTime(newDate);
//                MonthCellDescriptor cell = monthCellWithMonthIndex.cell;
//
//                highlightedCells.add(cell);
//                highlightedCals.add(newlyHighlightedCal);
//                cell.setHighlighted(true);
//            }
//        }
//
//        validateAndUpdate();
//    }
//
//    public void clearSelectedDates() {
//        for (MonthCellDescriptor selectedCell : selectedCells) {
//            selectedCell.setRangeState(RangeState.NONE);
//        }
//
//        clearOldSelections();
//        validateAndUpdate();
//    }
//
//    public void clearHighlightedDates() {
//        for (MonthCellDescriptor cal : highlightedCells) {
//            cal.setHighlighted(false);
//        }
//        highlightedCells.clear();
//        highlightedCals.clear();
//
//        validateAndUpdate();
//    }
//
//    private static class MonthCellWithMonthIndex {
//
//        MonthCellDescriptor cell;
//        int monthIndex;
//
//        MonthCellWithMonthIndex(MonthCellDescriptor cell, int monthIndex) {
//            this.cell = cell;
//            this.monthIndex = monthIndex;
//        }
//    }
//
//    private MonthCellWithMonthIndex getMonthCellWithIndexByDate(Date date) {
//        Calendar searchCal = Calendar.getInstance(timeZone, locale);
//        searchCal.setTime(date);
//        String monthKey = monthKey(searchCal);
//        Calendar actCal = Calendar.getInstance(timeZone, locale);
//
//        int index = cells.getIndexOfKey(monthKey);
//        List<List<MonthCellDescriptor>> monthCells = cells.get(monthKey);
//        for (List<MonthCellDescriptor> weekCells : monthCells) {
//            for (MonthCellDescriptor actCell : weekCells) {
//                actCal.setTime(actCell.getDate());
//                if (sameDate(actCal, searchCal) && actCell.isSelectable()) {
//                    return new MonthCellWithMonthIndex(actCell, index);
//                }
//            }
//        }
//        return null;
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private TextView textView;
//
//        public ViewHolder(View view) {
//            super(view);
//            view.setOnClickListener(this);
//            //this.textView = view.findViewById(R.id.textview);
//        }
//
//        @Override
//        public void onClick(View view) {
//            Toast.makeText(view.getContext(), "position : " + getLayoutPosition() + " text : " + this.textView.getText(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    List<List<MonthCellDescriptor>> getMonthCells(MonthDescriptor month, Calendar startCal) {
//        Calendar cal = Calendar.getInstance(timeZone, locale);
//        cal.setTime(startCal.getTime());
//        List<List<MonthCellDescriptor>> cells = new ArrayList<>();
//        cal.set(DAY_OF_MONTH, 1);
//        int firstDayOfWeek = cal.get(DAY_OF_WEEK);
//        int offset = cal.getFirstDayOfWeek() - firstDayOfWeek;
//        if (offset > 0) {
//            offset -= 7;
//        }
//        cal.add(Calendar.DATE, offset);
//
//        Calendar minSelectedCal = minDate(selectedCals);
//        Calendar maxSelectedCal = maxDate(selectedCals);
//
//        while ((cal.get(MONTH) < month.getMonth() + 1 || cal.get(YEAR) < month.getYear()) //
//                && cal.get(YEAR) <= month.getYear()) {
//            Logr.d("Building week row starting at %s", cal.getTime());
//            List<MonthCellDescriptor> weekCells = new ArrayList<>();
//            cells.add(weekCells);
//            for (int c = 0; c < 7; c++) {
//                Date date = cal.getTime();
//                boolean isCurrentMonth = cal.get(MONTH) == month.getMonth();
//                boolean isSelected = isCurrentMonth && containsDate(selectedCals, cal);
//                boolean isSelectable =
//                        isCurrentMonth && betweenDates(cal, minCal, maxCal) && isDateSelectable(date);
//                boolean isToday = sameDate(cal, today);
//                boolean isHighlighted = containsDate(highlightedCals, cal);
//                int value = cal.get(DAY_OF_MONTH);
//
//                RangeState rangeState = RangeState.NONE;
//                if (selectedCals.size() > 1) {
//                    if (sameDate(minSelectedCal, cal)) {
//                        rangeState = RangeState.FIRST;
//                    } else if (sameDate(maxDate(selectedCals), cal)) {
//                        rangeState = RangeState.LAST;
//                    } else if (betweenDates(cal, minSelectedCal, maxSelectedCal)) {
//                        rangeState = RangeState.MIDDLE;
//                    }
//                }
//
//                weekCells.add(
//                        new MonthCellDescriptor(date, isCurrentMonth, isSelectable, isSelected, isToday,
//                                isHighlighted, value, rangeState));
//                cal.add(DATE, 1);
//            }
//        }
//        return cells;
//    }
//
//    private boolean containsDate(List<Calendar> selectedCals, Date date) {
//        Calendar cal = Calendar.getInstance(timeZone, locale);
//        cal.setTime(date);
//        return containsDate(selectedCals, cal);
//    }
//
//    private static boolean containsDate(List<Calendar> selectedCals, Calendar cal) {
//        for (Calendar selectedCal : selectedCals) {
//            if (sameDate(cal, selectedCal)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static Calendar minDate(List<Calendar> selectedCals) {
//        if (selectedCals == null || selectedCals.size() == 0) {
//            return null;
//        }
//        Collections.sort(selectedCals);
//        return selectedCals.get(0);
//    }
//
//    private static Calendar maxDate(List<Calendar> selectedCals) {
//        if (selectedCals == null || selectedCals.size() == 0) {
//            return null;
//        }
//        Collections.sort(selectedCals);
//        return selectedCals.get(selectedCals.size() - 1);
//    }
//
//    private static boolean sameDate(Calendar cal, Calendar selectedDate) {
//        return cal.get(MONTH) == selectedDate.get(MONTH)
//                && cal.get(YEAR) == selectedDate.get(YEAR)
//                && cal.get(DAY_OF_MONTH) == selectedDate.get(DAY_OF_MONTH);
//    }
//
//    private static boolean betweenDates(Calendar cal, Calendar minCal, Calendar maxCal) {
//        final Date date = cal.getTime();
//        return betweenDates(date, minCal, maxCal);
//    }
//
//    static boolean betweenDates(Date date, Calendar minCal, Calendar maxCal) {
//        final Date min = minCal.getTime();
//        return (date.equals(min) || date.after(min)) // >= minCal
//                && date.before(maxCal.getTime()); // && < maxCal
//    }
//
//    private static boolean sameMonth(Calendar cal, MonthDescriptor month) {
//        return (cal.get(MONTH) == month.getMonth() && cal.get(YEAR) == month.getYear());
//    }
//
//    private boolean isDateSelectable(Date date) {
//        return dateConfiguredListener == null || dateConfiguredListener.isDateSelectable(date);
//    }
//
//    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
//        dateListener = listener;
//    }
//
//    /**
//     * Set a listener to react to user selection of a disabled date.
//     *
//     * @param listener the listener to set, or null for no reaction
//     */
//    public void setOnInvalidDateSelectedListener(OnInvalidDateSelectedListener listener) {
//        invalidDateListener = listener;
//    }
//
//    /**
//     * Set a listener used to discriminate between selectable and unselectable dates. Set this to disable arbitrary dates as they are rendered.
//     * <p>
//
//    public void setDateSelectableFilter(DateSelectableFilter listener) {
//        dateConfiguredListener = listener;
//    }
//
//    /**
//     * Set an adapter used to initialize {@link CalendarCellView} with custom layout.
//     * <p>
//
//    public void setCustomDayView(DayViewAdapter dayViewAdapter) {
//        this.dayViewAdapter = dayViewAdapter;
//        if (null != adapter) {
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    /**
//     * Set a listener to intercept clicks on calendar cells.
//     */
//    public void setCellClickInterceptor(CellClickInterceptor listener) {
//        cellClickInterceptor = listener;
//    }
//
//    /**
//     * Set a listener to react to user after selected date based on min or max date resolved.
//     */
//    public void setOnDateResolvedListener(OnDateResolvedListener listener) {
//        dateResolvedListener = listener;
//    }
//
//    /**
//     * Interface to be notified when a new date is selected or unselected. This will only be called when the user initiates the date selection.  If you call {@link #selectDate(Date)} this listener
//     * will not be notified.
//     *
//
//     */
//    public interface OnDateSelectedListener {
//        void onDateSelected(Date date);
//        void onDateUnselected(Date date);
//    }
//
//    /**
//     * Interface to be notified when an invalid date is selected by the user. This will only be called when the user initiates the date selection. If you call {@link #selectDate(Date)} this listener
//     * will not be notified.
//     *
//
//     */
//    public interface OnInvalidDateSelectedListener {
//        void onInvalidDateSelected(Date date);
//    }
//
//    /**
//     * Interface used for determining the selectability of a date cell when it is configured for display on the calendar.
//     *
//
//     */
//    public interface DateSelectableFilter {
//        boolean isDateSelectable(Date date);
//    }
//
//    /**
//     * Interface to be notified when a cell is clicked and possibly intercept the click.  Return true to intercept the click and prevent any selections from changing.
//     *
//
//     */
//    public interface CellClickInterceptor {
//        boolean onCellClicked(Date date);
//    }
//
//    /**
//     * Interface to be notified when a date out of range.
//     *
//
//     */
//    public interface OnDateResolvedListener {
//        void onMinDateResolved(Date date);
//        void onMaxDateResolved(Date date);
//    }
//
//    private class DefaultOnInvalidDateSelectedListener implements OnInvalidDateSelectedListener {
//
//        @Override
//        public void onInvalidDateSelected(Date date) {
////      String errMessage =
////          getResources().getString(R.string.invalid_date, fullDateFormat.format(minCal.getTime()),
////              fullDateFormat.format(maxCal.getTime()));
////      Toast.makeText(getContext(), errMessage, Toast.LENGTH_SHORT).show();
//        }
//    }
//}