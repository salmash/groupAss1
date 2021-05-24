package edu.cs.birzeit.groupass1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import edu.cs.birzeit.groupass1.Models.student;


public class MainActivity3 extends AppCompatActivity {
    private EditText idTxt;
    private EditText nameTxt;
    private EditText emailTxt;
    private EditText phoneTxt;
    Spinner genderSpn ;
    Spinner courseSpn;
    private EditText UniversityTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        setUpViews();


    }

    private void populateCourseSpinner() {
        ArrayList<String> data = new ArrayList<>();
        data.add("Java - Comp233");
        data.add("Software - Comp433");
        data.add("Mobile Development - Comp438");
        ArrayAdapter<String > adpter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,data);
        courseSpn.setAdapter(adpter);
    }

    private void populateGenderSpinner() {
        ArrayList<String> data = new ArrayList<>();
        data.add("Male");
        data.add("Female");
        ArrayAdapter<String > adpter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,data);
        genderSpn.setAdapter(adpter);
    }

    private void setUpViews() {
        idTxt =  findViewById(R.id.IdTxt);
        nameTxt =  findViewById(R.id.nameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        phoneTxt = findViewById(R.id.phoneTxt);

        genderSpn = findViewById(R.id.genderSpn);
        populateGenderSpinner();

        UniversityTxt =findViewById(R.id.UniversityTxt);

        courseSpn = findViewById(R.id.courseSpn);
        populateCourseSpinner();
    }
    public void backOnClick(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    public void btnAddOnClick(View view) {
        if(checkvalidation()==true) {
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
            String restUrl = "http://10.0.2.2/groupAss1/addStudent.php";
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        123);

            } else {
                SendPostRequest runner = new SendPostRequest();
                runner.execute(restUrl);
            }
        }
        else {
            Toast.makeText(MainActivity3.this, "Data Isn't Completed", Toast.LENGTH_LONG).show();
        }
    }

    student getStudentData (){
        student student = new student();
        student.setID(idTxt.getText().toString().trim());
        student.setName(nameTxt.getText().toString().trim());
        student.setEmail(emailTxt.getText().toString().trim());
        student.setPhone(phoneTxt.getText().toString().trim());
        student.setGender(genderSpn.getSelectedItem().toString().trim());
        student.setUniversity(UniversityTxt.getText().toString().trim());

        String s = courseSpn.getSelectedItem().toString();
        String [] str = s.split("-");

        student.setCourseID(str[1].trim());

        return student;
    }


    private String processRequest(String restUrl) throws UnsupportedEncodingException {
        student student = getStudentData();

        String data = URLEncoder.encode("ID", "UTF-8")
                + "=" + URLEncoder.encode(student.getID(), "UTF-8");

        data += "&" + URLEncoder.encode("Name", "UTF-8") + "="
                + URLEncoder.encode(student.getName(), "UTF-8");

        data += "&" + URLEncoder.encode("Email", "UTF-8") + "="
                + URLEncoder.encode(student.getEmail(), "UTF-8");

        data += "&" + URLEncoder.encode("Phone", "UTF-8")
                + "=" + URLEncoder.encode(student.getPhone(), "UTF-8");

        data += "&" + URLEncoder.encode("Gender", "UTF-8") + "="
                + URLEncoder.encode(student.getGender(), "UTF-8");

        data += "&" + URLEncoder.encode("University", "UTF-8")
                + "=" + URLEncoder.encode(student.getUniversity(), "UTF-8");

        data += "&" + URLEncoder.encode("courseID", "UTF-8")
                + "=" + URLEncoder.encode(student.getCourseID(), "UTF-8");

        String text = "";
        BufferedReader reader=null;

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL(restUrl);

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }


            text = sb.toString();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        // Show response on activity
        return text;



    }

    private class SendPostRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return processRequest(urls[0]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(MainActivity3.this, "Student added successfully", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkvalidation(){
        boolean check =true ;
        final String id =idTxt.getText().toString();
        final String Name = nameTxt.getText().toString();
        final String Email = emailTxt.getText().toString();
        final String Phone = phoneTxt.getText().toString();
        if(id.length()==0) {
            idTxt.requestFocus();
            idTxt.setError("ID Cannot Be Empty");
            check=false;
        } else if(!id.matches("[0-9]+")){
            idTxt.requestFocus();
            idTxt.setError("Please Enter Numbers Only");
            check=false;
        }  if(Name.length()==0) {
            nameTxt.requestFocus();
            nameTxt.setError("Name Field Cannot Be Empty");
            check=false;
        } else if(!Name.matches("^[a-zA-Z\\s]+$")){
            nameTxt.requestFocus();
            nameTxt.setError("Please Enter Only Characters");
            check=false;
        }  if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            emailTxt.setError("Enter a valid Email Address");
            check=false;
        }  if(!(Phone.length()==10) || !(Phone.matches("[0-9]+"))) {
            phoneTxt.requestFocus();
            phoneTxt.setError("The Phone Number Incorrect!");
            check=false;
        }

        return check;
    }


}