package com.example.mrcherif.nmap_auto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends Activity {

    String DEBUG_TAG = "myTag";
    ImageView doc;


    DatabaseHelper myDB;
    String DEFAULT_SHARED_PREFERENCES = "mySharedPrefs";
    String firstStartPref = "firstStart";
    String sav="";

    public static File appBinHome;
    String NMAP_COMMAND = "./nmap ";
    /*private static final String PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";*/
    Button save,his;

    public static TextView scanResult = null;
    Spinner s;
    String cmd,msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        s=(Spinner)findViewById(R.id.spinner);
       // save = (Button)findViewById(R.id.save);
        his = (Button) findViewById(R.id.history);
        doc=(ImageView)findViewById(R.id.doc);
        myDB = new DatabaseHelper(this);

        boolean firstInstall = true;
        appBinHome = getDir("bin", Context.MODE_MULTI_PROCESS);

        SharedPreferences mySharedPreferences = getSharedPreferences(DEFAULT_SHARED_PREFERENCES, MODE_MULTI_PROCESS);
        firstInstall = mySharedPreferences.getBoolean(firstStartPref, true);
        if(firstInstall) {
            NmapBinaryInstaller installer = new NmapBinaryInstaller(getApplicationContext());
            installer.installResources();
            Log.d(DEBUG_TAG, "Installing binaries");
            // TODO: Write some test code to see if the binaries are placed correctly and have the right permissions!
            mySharedPreferences.edit().putBoolean(firstStartPref, false).commit();
        }
        Button scan = (Button)findViewById(R.id.scan_BT);
        final EditText flags = (EditText)findViewById(R.id.flags_ET);
        scanResult = (TextView)findViewById(R.id.scan_output_TV);

        scanResult.setMovementMethod(new ScrollingMovementMethod());


        his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,hidtroy.class);
                startActivity(intent);
                finish();
            }
        });




        /*save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enregistrer(cmd+"\n"+sav);
                Log.d("res",sav);
            }
        });*/




        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String f = flags.getText().toString();
                //if (!(f.isEmpty()) && (validate(f))) {
                    if (!(f.isEmpty())) {


                    String ff = s.getSelectedItem().toString();
                    if (ff.equals("Scan a single host or an IP address")){
                        cmd="La commande: nmap "+f;
                        new AsyncCommandExecutor().execute(NMAP_COMMAND + f);}
                    else if (ff.equals("Scan a host when protected by the firewall")){
                        cmd="La commande: nmap -Pn "+f;
                        new AsyncCommandExecutor().execute(NMAP_COMMAND + "-Pn " + f);}
                    else if (ff.equals("Scan a network and find out which servers and devices are up and running")){
                        cmd="La commande: nmap -sP "+f;
                        new AsyncCommandExecutor().execute(NMAP_COMMAND + "-sP " + f);}
                    else if (ff.equals("Display the reason a port is in a particular state")){
                        cmd="La commande: nmap --reason "+f;
                        new AsyncCommandExecutor().execute(NMAP_COMMAND + "--reason " + f);}
                    else if (ff.equals("Only show open (or possibly open) ports")){
                        cmd="La commande: nmap --open "+f;
                        new AsyncCommandExecutor().execute(NMAP_COMMAND + "--open " + f);}
                    else if (ff.equals("The fastest way to scan all your devices/computers for open ports ever")){
                        cmd="La commande: nmap -T5 "+f;
                        new AsyncCommandExecutor().execute(NMAP_COMMAND + "-T5 " + f);}
                    else if (ff.equals("Scan a host using TCP ACK (PS) ")){

                        new AsyncCommandExecutor().execute(NMAP_COMMAND + "-PS " + f);}
                    else if (ff.equals("guess the operating system of the target machine.")){
                        new AsyncCommandExecutor().execute(NMAP_COMMAND + "-O --osscan-guess " + f);}

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String time = currentTime.format(calendar.getTime());
                     msg =time+ " \n" + cmd+" "+f;

                    //Log.d("res",sav);





                    //new AsyncCommandExecutor().execute( NMAP_COMMAND + f);
                } else{ Toast.makeText(MainActivity.this, "entrer l'@ SVP!", Toast.LENGTH_SHORT).show();}


            }
        });





    }

    private void ADDData(String msg, String sav) {
        Log.d("res",sav);
        boolean insertdta =  myDB.addData(msg,sav);
        if (insertdta){
            Toast.makeText(this, "Insered", Toast.LENGTH_SHORT).show();

        }else Toast.makeText(this, "error !", Toast.LENGTH_SHORT).show();
    }

   /* public static boolean validate(final String ip) {

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void enregistrer(String chiffr) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Resultat.txt");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(chiffr.getBytes());
            fileOutputStream.close();
            Toast.makeText(this, "Saved !", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "ficher non trouvé !", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(this, "erreur!", Toast.LENGTH_SHORT).show();

        }

    }


    public class AsyncCommandExecutor extends AsyncTask<String, Void, Void> {

        public String returnOutput;
        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            this.progressDialog.setTitle("NMAP");
            this.progressDialog.setMessage("Scanning...");
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
            return;
        }
        @Override
        protected Void doInBackground(String... params) {
            try {
                this.returnOutput = CommandRunner.execCommand(params[0], MainActivity.appBinHome.getAbsoluteFile());
            } catch (IOException e) {
                this.returnOutput = "IOException while trying to scan!";
                Log.d(DEBUG_TAG, e.getMessage());
            } catch (InterruptedException e) {
                this.returnOutput = "Nmap Scan Interrupted!";
                Log.d(DEBUG_TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            MainActivity.scanResult.setText(returnOutput);
            sav=returnOutput;

            ADDData(msg,sav);
            Toast.makeText(MainActivity.this,"Data Inserted Successfully", Toast.LENGTH_LONG).show();
            if(this.progressDialog.isShowing())
                this.progressDialog.dismiss();
        }
    }
}
