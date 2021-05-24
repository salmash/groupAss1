


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
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

import edu.cs.birzeit.groupass1.Models.student;


public class MainActivity4 extends AppCompatActivity {
    public ArrayList <student> studentsArray = new ArrayList<student>();
//    ArrayAdapter<String> itemsAdapter;

    public Spinner studentsName;
    private EditText idTxt;
    private EditText nameTxt;
    private EditText emailTxt;
    private EditText phoneTxt;
    private Spinner genderSpn ;
    private Spinner courseSpn;
    private EditText UniversityTxt;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        getData();
        setUpViews();



    }

    public void getData (){
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
    }

    public void setUpViews() {
        studentsName = findViewById(R.id.spinner);
        populateSpinner();
        idTxt =  findViewById(R.id.IdTxt);
        nameTxt =  findViewById(R.id.nameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        phoneTxt = findViewById(R.id.phoneTxt);
        genderSpn = findViewById(R.id.genderSpn);
        populateGenderSpinner();
        UniversityTxt =findViewById(R.id.UniversityTxt);
    }


    public void populateSpinner() {
        ArrayList<String> spinnerData = new ArrayList<>();
        spinnerData.add("Choose the name");

        for(int i=0 ; i<studentsArray.size();i++){
            spinnerData.add(studentsArray.get(i).getName() );
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity4.this,
                android.R.layout.simple_spinner_item, spinnerData);

        studentsName.setAdapter(adapter);
    }

    public void populateGenderSpinner() {
        ArrayList<String> spinnerData = new ArrayList<>();
        spinnerData.add("Female");
        spinnerData.add("Male");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity4.this,
                android.R.layout.simple_spinner_item, spinnerData);

        genderSpn.setAdapter(adapter);
    }


    private InputStream OpenHttpConnectionGet(String urlString) throws IOException {
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
            in = OpenHttpConnectionGet(URL);
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
    public void backOnClick(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }


    private class DownloadstudentInformation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }

        @Override
        protected void onPostExecute(String result2) {
            String[] array = result2.split("/");


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
                    studentsArray.add(student);
                }


            }

            populateSpinner();

            studentsName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    for(int i=0 ; i<studentsArray.size();i++){
                        if((Objects.equals(studentsArray.get(i).getName(),selectedItem))){
                            idTxt.setText(studentsArray.get(i).getID());
                            nameTxt.setText(studentsArray.get(i).getName());
                            emailTxt.setText(studentsArray.get(i).getEmail());
                            phoneTxt.setText(studentsArray.get(i).getPhone());


                            if(Objects.equals(studentsArray.get(i).getGender(),"Male")) {
                                genderSpn.setSelection(1);
                            }

                            else if(Objects.equals(studentsArray.get(i).getGender(),"Female")) {
                                genderSpn.setSelection(2);
                            }

                            UniversityTxt.setText(studentsArray.get(i).getUniversity());

                        }



                    }

                }

                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });

        }

    }


    public void updateOnClick(View view) {
        if(checkvalidation()==true) {
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
            String restUrl = "http://10.0.2.2/groupAss1/update.php";
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
            Toast.makeText(MainActivity4.this, "Data Isn't Completed", Toast.LENGTH_LONG).show();

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


        return student;
    }


    private String processRequest(String restUrl) throws UnsupportedEncodingException {
        student student = getStudentData();

        String data = URLEncoder.encode("Name", "UTF-8") + "="
                + URLEncoder.encode(student.getName(), "UTF-8");

        data += "&" + URLEncoder.encode("Email", "UTF-8") + "="
                + URLEncoder.encode(student.getEmail(), "UTF-8");

        data += "&" + URLEncoder.encode("Phone", "UTF-8")
                + "=" + URLEncoder.encode(student.getPhone(), "UTF-8");

        data += "&" + URLEncoder.encode("Gender", "UTF-8") + "="
                + URLEncoder.encode(student.getGender(), "UTF-8");

        data += "&" + URLEncoder.encode("University", "UTF-8")
                + "=" + URLEncoder.encode(student.getUniversity(), "UTF-8");

        data += "&" + URLEncoder.encode("ID", "UTF-8")
                + "=" + URLEncoder.encode(student.getID(), "UTF-8");

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
            Toast.makeText(MainActivity4.this, "Information updated successfully", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkvalidation(){
        boolean check =true ;
        final String id =idTxt.getText().toString().trim();
        final String Name = nameTxt.getText().toString().trim();
        final String Email = emailTxt.getText().toString().trim();
        final String Phone = phoneTxt.getText().toString().trim();
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
