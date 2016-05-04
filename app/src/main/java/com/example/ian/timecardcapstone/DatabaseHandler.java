package com.example.ian.timecardcapstone;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.ian.timecardcapstone.provider.shift.ShiftColumns;
import com.example.ian.timecardcapstone.provider.shift.ShiftContentValues;

import hirondelle.date4j.DateTime;

/**
 * Created by ian on 4/28/2016.
 */
public class DatabaseHandler {
    private DateTime mNowDate;
    private Context mContext;
    private static final String LOG_TAG = DatabaseHandler.class.getSimpleName();

    DatabaseHandler (Context context){
        mContext = context;
    }

    /**
     * Inserts the following information for clocking in:
     * Current day
     * Current Time in hh:mm format
     * Current Time in UNIX format
     * Current month
     * Current day of week
     * Current year
     * Hourly Pay
     *
     * @param clockInTime The time in which the user clocked in at
     * @return The URI at which the clocked in data was inserted in
     */
    public Uri clockIn (DateTime clockInTime) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Float hourlyPayFloat = 11.25f;
        try {
             hourlyPayFloat = Float.valueOf(sharedPreferences.getString(mContext.getResources().getString(R.string.hourlyPay),"11.25"));
        } catch (NumberFormatException exception) {
            Log.e(LOG_TAG, exception.getMessage());
            Toast.makeText(mContext, "Hourly pay is in invalid format", Toast.LENGTH_SHORT).show();
        }



        ShiftContentValues clockInValues = new ShiftContentValues();

        clockInValues.putDayOfMonth(clockInTime.getDay());
        clockInValues.putStartTimeHhmm(clockInTime.format("hh:mm"));
        clockInValues.putDayOfWeek(clockInTime.getWeekDay().toString());
        clockInValues.putMonthName(clockInTime.getMonth().toString());
        clockInValues.putYear(clockInTime.getYear());
        clockInValues.putHourlyPay(hourlyPayFloat);
        long unixTime = (System.currentTimeMillis() / 1000);
        clockInValues.putStartTimeUnix((int) unixTime);


        return mContext.getContentResolver().insert(ShiftColumns.CONTENT_URI, clockInValues.values());
    }

    public int clockOut(Uri clockInUri, DateTime clockOutTime) {
        ShiftContentValues clockOutValues = new ShiftContentValues();

        clockOutValues.putEndTimeHhmm(clockOutTime.format("hh:mm"));
        long unixTime = (System.currentTimeMillis() / 1000);
        clockOutValues.putEndTimeUnix((int) unixTime);
        // TODO: calculate number of hours worked and gross pay based on the hourly pay

        Log.i(LOG_TAG, "CLOCKED IN CONTENT URI: " + clockInUri);
         mContext.getContentResolver().update(clockInUri, clockOutValues.values(),null, null);

        // TODO: REDO THIS CODE SO ITS CLEANER AND NOT A SLOPPY PIECE OF CRAP
       Cursor clockedOutCursor =  mContext.getContentResolver().query(clockInUri,
                new String[]{ShiftColumns.START_TIME_UNIX, ShiftColumns.END_TIME_UNIX, ShiftColumns.HOURLY_PAY},null, null, null);
        float[] hoursWorkedAndGrossPay = numOfHoursWorked(clockedOutCursor);
       Log.i(LOG_TAG, "NUMBER OF HOURS REPORTED WORKED FROM CALLED METHOD: " +  hoursWorkedAndGrossPay[0] +
               "GROSS PAY IN ARRAY: " + hoursWorkedAndGrossPay[1] + " AND QURIED CURSOR: " +
               DatabaseUtils.dumpCursorToString(clockedOutCursor));
        ShiftContentValues secondClockOutValues = new ShiftContentValues();
        secondClockOutValues.putNumHrsShift(hoursWorkedAndGrossPay[0]);
        secondClockOutValues.putGrossPay(hoursWorkedAndGrossPay[1]);
       return mContext.getContentResolver().update(clockInUri, secondClockOutValues.values(),null,null);
        //return numOfRowsUpdated;
    }

    private float[] numOfHoursWorked(Cursor clockedOutCursor) {
        Log.i(LOG_TAG, "CURSOR BEING WORKED ON: " + DatabaseUtils.dumpCursorToString(clockedOutCursor));
        clockedOutCursor.moveToFirst();
        int clockedInUnixTime = clockedOutCursor.getInt(clockedOutCursor.getColumnIndexOrThrow(ShiftColumns.START_TIME_UNIX));
        int clockedOutUnixTime = clockedOutCursor.getInt(clockedOutCursor.getColumnIndexOrThrow(ShiftColumns.END_TIME_UNIX));
        float hourlyPay = clockedOutCursor.getFloat(clockedOutCursor.getColumnIndexOrThrow(ShiftColumns.HOURLY_PAY));

        float totalHoursWorked = ((clockedOutUnixTime - clockedInUnixTime)/ 3600f);
        float calcGrossPay = (hourlyPay * totalHoursWorked);
        Log.i(LOG_TAG, "TOTAL HOURS WORKED: " + totalHoursWorked);
        //float[] grossPayAndHoursWorkedArray = {totalHoursWorked, calcGrossPay};

        return new float[] {totalHoursWorked, calcGrossPay};

    }


  /*  public void clockInTest (Uri clockInUri) {
        Cursor clockedInCursor = mContext.getContentResolver().query(clockInUri, null, null, null, null);

        if (clockedInCursor != null) {
            clockedInCursor.moveToFirst();


            clockedInCursor.close();
        }
    }*/

}