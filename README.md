Date Time Range Picker for Android by [android-times-square](https://github.com/square/android-times-square) added some improvement
==========================

## Improvement
- Added dropdown for year selection and synced with calendar to update its view
- Updated range selection view along with some other screen enhancements
- Added EditTextFields to display the start and end date and made it editable to sync with calendar and dropdown view
- Fixed previous UI issues
- Code for EditTextFields is currently not public as I used custom EditTextFields. Inbox me if anyone wants to access the code.

Standalone Android widget for picking a single date from a calendar view.

<img src="https://raw.githubusercontent.com/arsalyou/Calendar-Widget/main/pic1.png" />
<img src="https://raw.githubusercontent.com/arsalyou/Calendar-Widget/main/pic2.png"  />
<img src="https://raw.githubusercontent.com/arsalyou/Calendar-Widget/main/pic3.png" />
<img src="https://raw.githubusercontent.com/arsalyou/Calendar-Widget/main/pic4.png" />
<img src="https://raw.githubusercontent.com/arsalyou/Calendar-Widget/main/pic5.png"  />
<img src="https://raw.githubusercontent.com/arsalyou/Calendar-Widget/main/pic6.png"  />
<img src="https://raw.githubusercontent.com/arsalyou/Calendar-Widget/main/pic7.png"  />


## Installation
### Via Gradle
Add it in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency:

```
dependencies {
	implementation "com.github.arsalyou:date-time-range-picker-android:${version}"
}

```

## Usage
-----

Include `CalendarPickerView` in your layout XML.

```xml
<com.arsalyou.datetimerangepickerandroid.CalendarPickerView
    android:id="@+id/calendar_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    />
```

Default implementation to show Calendar

```java
calendar.init(today, nextYear.getTime())
    .inMode(RANGE);
```


To show the Calendar

```java
Calendar nextYear = Calendar.getInstance();
nextYear.add(Calendar.YEAR, 1);

CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
Date today = new Date();
calendar.init(today, nextYear.getTime())
    .withSelectedDate(today);
```

## Proguard
No configuration needed.
