package com.example.ian.timecardcapstone.data;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.ian.timecardcapstone.provider.rosterappsdata.RosterappsdataColumns;
import com.example.ian.timecardcapstone.provider.rosterappsdata.RosterappsdataContentValues;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RosterAppsLoginIntentService extends IntentService {
    public final static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0";
    public final static String rosterappsLogin = "https://jetblue.rosterapps.com/Login.aspx?ReturnUrl=/";
    public final static String rosterAppsCalender = "https://jetblue.rosterapps.com/calendar.month.aspx";
    public final static String eventTarget = "__EVENTTARGET";
    public final static String eventArgument = "__EVENTARGUMENT";
    public final static String lastFocus = "__LASTFOCUS";
    public final static String viewState = "__VIEWSTATE";
    public final static String viewStateGenerator = "__VIEWSTATEGENERATOR";
    public final static String txtUsername = "txtUsername";
    public final static String txtPassword = "txtPassword";
    public final static String btnLogin = "btnLogin";
    public final static String LOG_TAG = RosterAppsLoginIntentService.class.getSimpleName();

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_LOGIN_ROSTERAPPS = "com.example.ian.timecardcapstone.data.action.FOO";
    private static final String ACTION_BAZ = "com.example.ian.timecardcapstone.data.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_USERNAME = "com.example.ian.timecardcapstone.data.extra.PARAM1";
    private static final String EXTRA_PASSWORD = "com.example.ian.timecardcapstone.data.extra.PARAM2";
    private static Context mContext;

    public RosterAppsLoginIntentService() {
        super("RosterAppsLoginIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startActionLoginRosterapps(Context context, String username, String password) {
        Intent intent = new Intent(context, RosterAppsLoginIntentService.class);
        intent.setAction(ACTION_LOGIN_ROSTERAPPS);
        intent.putExtra(EXTRA_USERNAME, username);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startService(intent);
        mContext = context;
    }

// TODO: Implement a user interface to allow the user to input their username and password, and that is passed into this service via an intent
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOGIN_ROSTERAPPS.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_USERNAME);
                final String param2 = intent.getStringExtra(EXTRA_PASSWORD);
                Document returnedHTMLRosterAppsData = handleActionRosterappsLogin(param1, param2);
                if (returnedHTMLRosterAppsData != null) {
                    String HTMLRosterAppsText = returnedHTMLRosterAppsData.text();
                    if (HTMLRosterAppsText.contains("Forgot")){

                    }
                    organizeShiftHTMLData(returnedHTMLRosterAppsData);

                }

            }
        }
    }
// TODO: Get the HTML string from logging into Rosterapps. Organize the HTML data, sorting out all of the shift data to be organized week by week
    /**
     * Log into Rosterapps with the provided user name and password, and get back the HTML data from the calendar
     */
    private Document handleActionRosterappsLogin(String username, String password) {
        try {
            Connection.Response loginResponse = Jsoup.connect(rosterappsLogin)
                    .userAgent(userAgent)
                    .data(eventTarget, "")
                    .data(eventArgument, "")
                    .data(lastFocus, "")
                    .data(viewState, "/wEPDwULLTE0NTU3NDM0MzlkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYBBRBjaGtQZXJzaXN0Q29va2llI+gcwdzIyCAjhj2bPon1rUHC+z8=")
                    .data(viewStateGenerator, "C2EE9ABB")
                    .data(txtUsername, username)
                    .data(txtPassword, password)
                    .data(btnLogin, "Login")
                    .method(Connection.Method.POST)
                    .execute();
            Map<String, String> loginCookie = loginResponse.cookies();
            Log.d(LOG_TAG, "COOKIES: " + loginCookie.toString());
            //  Log.i(LOG_TAG," LOGIN_RESPONSE: " + loginResponse.parse().html());
            Document calendarDocument = Jsoup.connect(rosterAppsCalender)
                    .userAgent(userAgent)
                    .cookies(loginCookie)
                    .execute().parse();
           // System.out.print(calendarDocument.html());

            return calendarDocument;
        } catch (IOException exception) {
            Log.e(LOG_TAG, "EXCEPTION ERROR: " + exception);
        }
        // TODO: Handle action Foo
      return null;
    }

    private void organizeShiftHTMLData(Document calenderHTML) {
        String shiftDay;
        ArrayList<String> listOfShiftTypes = new ArrayList<>();
        if (calenderHTML != null) {
            Elements entireMonth = calenderHTML.getElementsByClass("week");

            for (int weekIndex = 0; weekIndex < entireMonth.size(); weekIndex++) {
                Element singleWeek = entireMonth.get(weekIndex);
                //Log.i(LOG_TAG, singleWeek.toString());
                Elements days = singleWeek.getElementsByTag("td");

                for (int dayIndex = 0; dayIndex < days.size(); dayIndex++) {

                    Element singleDay = days.get(dayIndex);
                    Log.e(LOG_TAG,"ENTIRE DAY: " +  singleDay.text());
                    String dayOfShifts = singleDay.text();
                    listOfShiftTypes.add(dayOfShifts);
                   // Log.i(LOG_TAG,  singleDay.toString());
                    //+ " TYPE OF SHIFT: " +  singleDay.getElementsByTag("div").attr("class")

                    Elements shiftsInDay = singleDay.getElementsByTag("div");
                    //Log.i(LOG_TAG, shiftsInDay.toString());
                    //Log.i(LOG_TAG, "SHIFTS IN DAY: " + shiftsInDay.text());
                    Log.i(LOG_TAG, listOfShiftTypes.toString());

                    /*for (int shiftIndex = 0; shiftIndex < shiftsInDay.size(); shiftIndex++) {
                        Element singleShift =  shiftsInDay.get(shiftIndex);

                        String typeOfShift = singleShift.attr("class");

                        listOfShiftTypes.add(typeOfShift);
                        Log.i(LOG_TAG, listOfShiftTypes.toString());
                    }*/


                }
            }
            RosterappsdataContentValues rosterappsdataContentValues = new RosterappsdataContentValues();
            for (int thingInListIndex = 0; thingInListIndex < listOfShiftTypes.size(); thingInListIndex++) {
                rosterappsdataContentValues.putRosterappsData(listOfShiftTypes.get(thingInListIndex));

                mContext.getContentResolver().insert(RosterappsdataColumns.CONTENT_URI, rosterappsdataContentValues.values());

            }
        }

    }

}
