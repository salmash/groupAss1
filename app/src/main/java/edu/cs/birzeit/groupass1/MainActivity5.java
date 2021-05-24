package edu.cs.birzeit.groupass1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import edu.cs.birzeit.groupass1.Models.course;
import edu.cs.birzeit.groupass1.Models.student;


public class MainActivity5 extends AppCompatActivity {
    Spinner spinner;
    ListView result;
    Button search;
    ArrayList coursesArray = new ArrayList();
    ArrayList studentsArray = new ArrayList();
    ArrayAdapter<String> itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        spinner = findViewById(R.id.spinner);
        populateSpinner();

        search = findViewById(R.id.search);
        result = findViewById(R.id.result);

    }

    private void populateSpinner() {
        ArrayList<String> spinnerData = new ArrayList<>();
        spinnerData.add("Type of Search");
        spinnerData.add("Search for All Available Courses");
        spinnerData.add("Search for All Enrolled Students");
        spinnerData.add("Search for students enrolled in a chosen course");
        spinnerData.add("Search for courses registered by a chosen student");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerData);

        spinner.setAdapter(adapter);
    }

    public void searchOnClick(View view) {
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (Objects.equals(spinner.getSelectedItem().toString(), "Search for All Available Courses")) {
            EditText edtCat = findViewById(R.id.data);

            String url = "http://10.0.2.2/groupAss1/allCourses.php";
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        123);

            } else {
                DownloadcourseInformation runner = new DownloadcourseInformation();
                runner.execute(url);
            }

        } else if (Objects.equals(spinner.getSelectedItem().toString(), "Search for All Enrolled Students")) {
            EditText edtCat = findViewById(R.id.data);

            String url = "http://10.0.2.2/groupAss1/allStudents.php";
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        123);

            } else {
                DownloadstudentInformation runner = new DownloadstudentInformation();
                runner.execute(url);
            }

        } else if (Objects.equals(spinner.getSelectedItem().toString(), "Search for students enrolled in a chosen course")) {
            EditText edtCat = findViewById(R.id.data);

            String url = "http://10.0.2.2/groupAss1/courseStudentsSearch.php?cat=" + edtCat.getText();
            ;
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        123);

            } else {
                DownloadstudentInformation runner = new DownloadstudentInformation();
                runner.execute(url);
            }

        } else if (Objects.equals(spinner.getSelectedItem().toString(), "Search for courses registered by a chosen student")) {
            EditText edtCat = findViewById(R.id.data);

            String url = "http://10.0.2.2/groupAss1/studentCoursesSearch.php?cat=" + edtCat.getText();
            ;
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        123);

            } else {
                DownloadcourseInformation runner = new DownloadcourseInformation();
                runner.execute(url);
            }

        }

    }

    public void backOnClick(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }

    private String DownloadText(String URL) {
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }

        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer)) > 0) {
                //---convert the chars to a String---
                String readString =
                        String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }
        return str;
    }

    private class DownloadcourseInformation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }

        @Override
        protected void onPostExecute(String result2) {
            String[] array = result2.split("/");
            coursesArray.clear();

            for (int i = 0; i < array.length; i++) {
                if (i != array.length - 1) {
                    String[] info = array[i].split(",");
                    course course = new course();
                    course.setCourseID(info[0]);
                    course.setCourseName(info[1]);
//                    System.out.println(course);
                    coursesArray.add(course.getCourseName());
                }

                result.setAdapter(null);
                itemsAdapter = new ArrayAdapter<String>(MainActivity5.this, android.R.layout.simple_list_item_1,
                        coursesArray);
                result.setAdapter(itemsAdapter);
            }
        }
    }
    private class DownloadstudentInformation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }

        @Override
        protected void onPostExecute(String result2) {
            String[] array = result2.split("/");
            studentsArray.clear();

            for (int i = 0; i < array.length; i++) {
                if (i != array.length - 1) {
                    String[] info = array[i].split(",");
                    student student = new student ();
                    student.setID(info[0]);
                    student.setName(info[1]);
                    student.setEmail(info[2]);
                    student.setPhone(info[3]);
                    student.setGender(info[4]);
                    student.setUniversity(info[5]);
                    student.setCourseID(info[6]);
                    System.out.println(student);
                    studentsArray.add(student.getName());
                }

                result.setAdapter(null);
                itemsAdapter = new ArrayAdapter<String>(MainActivity5.this, android.R.layout.simple_list_item_1,
                        studentsArray);
                result.setAdapter(itemsAdapter);
            }
            Toast.makeText(MainActivity5.this, "Searched Successfully", Toast.LENGTH_LONG).show();
        }
    }
}