package com.cristiancustodio.usmapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class BachelorsDegreeAuditResultsActivity extends AppCompatActivity {

    //Courses taken counter
    int courseCounter = 0;
    float creditsCounter = 0;
    boolean missionTripCompleted = false;

    //Create Array to hold courses remaining
    StringBuilder builder = new StringBuilder();

    //Variable to hold graduation string status for body email
    String graduationStatus;


    //Create SharedPreferences
    public static final String MySharedPrefs = "MyPrefs";
    SharedPreferences sharedPref;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bachelors_degree_audit_results);

        //Inflate the actionbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.admissionsToolbar);
        setSupportActionBar(myToolbar);

        //Declare SharedPref
        Context context = this;
        sharedPref = context.getSharedPreferences(MySharedPrefs, Context.MODE_PRIVATE);

        //Populate the counters for the Audit Report
        setReportCounters();

        //Populate the Audit Report
        populateReport();

        //Prevent landscape orientation
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    //Inflate the action bar menu options
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        // Inflate and initialize the bottom menu
        ActionMenuView bottomBar = (ActionMenuView)findViewById(R.id.bottom_toolbar_Bachelors_Degree_Results);
        Menu bottomMenu = bottomBar.getMenu();
        getMenuInflater().inflate(R.menu.menu_botttom, bottomMenu);
        for (int i = 0; i < bottomMenu.size(); i++) {
            bottomMenu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onOptionsItemSelected(item);
                }
            });
        }
        return true;
    }
    //Actionbar Link Options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_student_login:
                Intent intent2 = new Intent(this, StudentPortalActivity.class);
                startActivity(intent2);
                return true;

            case R.id.action_admissions:
                Intent intent3 = new Intent(this, AdmissionsActivity.class);
                startActivity(intent3);
                return true;

            case R.id.action_home_bottom:
                Intent intent4 = new Intent(this, HomeActivity.class);
                startActivity(intent4);
                return true;

            case R.id.action_register_bottom:
                Intent intent5 = new Intent(this, AdmissionApplicationActivity.class);
                startActivity(intent5);
                return true;

            case R.id.action_graduation_bottom:
                Intent intent6 = new Intent(this, DegreeAuditSelectionActivity.class);
                startActivity(intent6);
                return true;

            //case R.id.action_aboutUs:
            // Intent intent4 = new Intent(this, StudentPortalActivity.class);
            //startActivity(intent4);
            //return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.

                return super.onOptionsItemSelected(item);

        }
    }
    public void editCourseSelection (View view) {
        Intent intent = new Intent(this, DegreeAuditSelectionBachelorsActivity.class);
        startActivity(intent);
    }

    public void saveReport (View view) {

        //Gather report data from sharedPrefs
        int totalCreditsInt = Math.round(creditsCounter/120 * 100);
        int totalCreditsInt2 = Math.round(creditsCounter);

        final Intent emailIntent = new Intent( android.content.Intent.ACTION_SEND);

        emailIntent.setType("plain/text");

        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[] { "abc@gmail.com" });

        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "Bachelors Degree Audit");

        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                "Total Completion: " + String.valueOf((totalCreditsInt) + "%") + "\n" + "Credits Remaining: "
                        + String.valueOf(120-totalCreditsInt2) + "\n" + "Graduation Ready: " + graduationStatus + "\n" + "\n" + "Courses Remaining:" + "\n" + "\n" + builder );

        startActivity(Intent.createChooser(
                emailIntent, "Save Report to File..."));
    }
    private void populateReport() {

        //Connect TextViews
        TextView percentage = (TextView) findViewById(R.id.percentCompletedResultView2);
        TextView creditsRemaining = (TextView) findViewById(R.id.creditsRemainingResultsView2);
        TextView graduationReady = (TextView) findViewById(R.id.graduationReadyResultsView2);
        TextView coursesRemaining = (TextView) findViewById(R.id.listOfCoursesRemainingResultView2);
        TextView courseRemainingTitleView = (TextView) findViewById(R.id.coursesRemainingTitleView2);


        int totalCreditsInt = Math.round(creditsCounter/120 * 100);
        percentage.setText(String.valueOf("  " + (totalCreditsInt) + "%"));

        int totalCreditsInt2 = Math.round(creditsCounter);
        creditsRemaining.setText(String.valueOf(120-totalCreditsInt2));

        if (creditsCounter == 120 && missionTripCompleted == true) {
            graduationReady.setTextColor(Color.GREEN);
            graduationReady.setText("Yes!");
            coursesRemaining.setText("All academics requirements have been fullfilled for the Associate in Ministry Degree.");
            graduationStatus = "All academics requirements have been fullfilled for the Associate in Ministry Degree.";
            courseRemainingTitleView.setText("Congratulations!");
            courseRemainingTitleView.setTextColor(Color.GREEN);


        } else if (creditsCounter == 120 && missionTripCompleted == false) {
            graduationReady.setTextColor(Color.RED);
            graduationReady.setText("Missing Missionary Trip");
            graduationStatus = "Missing Missionary Trip";
            percentage.setText("99%");
            coursesRemaining.setText(builder);
        } else if (creditsCounter > 64) {
            graduationReady.setText("Almost There!");
            graduationStatus = "Almost There!";
            coursesRemaining.setText(builder);
        } else {
            graduationReady.setText("Not Yet, Keep Going!");
            graduationStatus = "Not Yet, Keep Going!";
            coursesRemaining.setText(builder);
        }




    }
    private void setReportCounters() {
        if (sharedPref.getBoolean("sup101",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 101 - Revelation and Power of the Work of Jesus Christ on the Cross I" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup201",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 201 - Revelation and Power of the Work of Jesus Christ on the Cross II" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup110",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 110 - Revelation and Power of the Resurrection of Jesus Christ" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup120",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 120 - Faith I" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup220",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 220 - Faith II" + "\n"+ "\n");
        }
        if (sharedPref.getBoolean("sup130",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        } else {
            builder.append("SUP 130 - Prayer I" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup230",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 230 - Prayer II" + "\n"+ "\n");
        }
        if (sharedPref.getBoolean("sup140",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        }else {
            builder.append("SUP 140 - Evangelism with Miracles I" + "\n"+  "\n");
        }
        if (sharedPref.getBoolean("sup260",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        } else {
            builder.append("SUP 260 - Evangelism with Miracles II" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup210",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 210 - Inner Healing and Deliverance" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup240",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 240 - Divine Health and Healing" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup250",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 250 - How to Walk in the Supernatural Power of God I" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup350",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 350 - How to Walk in the Supernatural Power of God II" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("kpg101",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        } else {
            builder.append("KPG 101 - The Apostolic Vision of the House I" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("kpg201",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        } else {
            builder.append("KPG 201 - The Apostolic Vision of the House II" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("kpg210",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("KPG 210 - Kingdom Economic Principles" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("kpg330",false) == true)  {
            courseCounter = courseCounter + 1;
            missionTripCompleted = true;
        } else {
            builder.append("KPG 330 - Kingdom Advancement Through Mission" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("pmi110",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        } else {
            builder.append("PMI 110 - Introduction to the Five-fold Ministry" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("pmi120",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("PMI 120 - The Formation of the Character and Ministry of a Leader I" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("pmi130",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("PMI 130 - How to Find your Purpose and Calling for your Life I" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("pmi210",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        } else {
            builder.append("PMI 210 - Fathering, Family, Marriage and Children I" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("pmi220",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("PMI 220 - Transformation through the Renewing of the Mind" + "\n"+ "\n");
        }
        if (sharedPref.getBoolean("ged260",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("GED 260 - The Three Pillars of Health: Diet, Exercise and Rest I" + "\n"+ "\n");
        }
        if (sharedPref.getBoolean("ged220",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("GED 220 - English Composition" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("rkg210",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("RKG 210 - How to Hear the Voice of God" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("rkg310",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("RKG 310 - The Holy Spirit in the Now I" + "\n" + "\n");
        }

        if (sharedPref.getBoolean("brv120",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("BRV 120 - Foundation of the Christian Faith" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("brv220",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        } else {
            builder.append("RKG 220 - Preaching the Gospel of the Kingdom with Revelation and Authority" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup310",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        } else {
            builder.append("SUP 310 - Deliverance, the Children's bread" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup301",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 301 - Fasting as a Spiritual Weapon" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup320",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 320 - The Ministry of Intercession" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup340",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 340 - The Gifts of the Holy Spirit, Here and Now I" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("sup360",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("SUP 360 - The Gifts of the Holy Spirit, Here and Now II" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("kpg220",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 4;
        } else {
            builder.append("KPG 220 - The Kingdom of Power, Dominion, Expansion and Demonstration I" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("kpg320",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        } else {
            builder.append("KPG 320 - The Kingdom of Power, Dominion, Expansion and Demonstration II" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("pmi230",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("PMI 230 - The Formation of the Character and Ministry of a Leader II" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("pmi240",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("PMI 240 - How to Find your Purpose and Calling ofr your Life II" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("pmi310",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        } else {
            builder.append("PMI 310 - Fathering, Family, Marriage and Children II" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("pmi320",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        } else {
            builder.append("PMI 320 - A Covenant of Commitment with God" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("pmi330",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        } else {
            builder.append("PMI 330 - Counseling, Pastoral Care, and Love" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("rkg320",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("RKG 320 - The Holy Spirit in the Now II" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("brv101",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("BRV 101 - Old Testament I" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("brv102",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("BRV 102 - Old Testament II" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("brv201",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("BRV 201 - New Testament I" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("brv202",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("BRV 202 - New Testament II" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("brv420",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("BRV 420 - End Times and the Book of Revelation" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("mus101",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("MUS 101 - Introduction to Praise and Worship" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("mus110",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("MUS 110 - Introduction to Music" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("mus220",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("MUS 220 - How to Raise a Prophetic Praise and Worship Team" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("ged110",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 4;
        } else {
            builder.append("GED 110 - Financial, Legal and Administrative Processes and Procedures for Churches and Ministries" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("ged460",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 2;
        } else {
            builder.append("GED 460 - The Three Pillars of Health: Diet, Exercise and Rest II" + "\n" + "\n");
        }
        if (sharedPref.getBoolean("ged320",false) == true)  {
            courseCounter = courseCounter + 1;
            creditsCounter = creditsCounter + 3;
        } else {
            builder.append("GED 320 - Introduction to Computers and Technology" + "\n" + "\n");
        }
    }

}
