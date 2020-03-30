package com.example.mrcherif.nmap_auto;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.util.ArrayList;

public class hidtroy extends AppCompatActivity {


    DatabaseHelper myDb;
    ListView lv;
    ImageButton retur;

    int j;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidtroy);
        retur=(ImageButton)findViewById(R.id.res);


        lv=  (ListView) findViewById(R.id.lv);
        myDb = new DatabaseHelper(this);





        final ArrayList<String>arrayList = new ArrayList<>();
        final Cursor cursor = myDb.getListContent();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "Not deleted", Toast.LENGTH_SHORT).show();

        }else {
            while (cursor.moveToNext()){
                arrayList.add(cursor.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
                lv.setAdapter(listAdapter);

            }
        }
        retur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(hidtroy.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               int i=position+1;
                int jj=1;

                while (!(myDb.getList(i))){
                    i++;
                     jj = i;
                }
                String id =String.valueOf(jj);

               String txt = myDb.getListContent(id);



                openDialog(txt,id);
            }
            });

        }



    private void openDialog(final String txt, final String id) {
        AlertDialog.Builder builder=new AlertDialog.Builder(hidtroy.this);
        builder.setTitle("Supprimer ?").setMessage(txt).setPositiveButton("oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Log.d("ii",id);
                Integer d= myDb.deletedata(id);

                if (d>0){
                    Toast.makeText(hidtroy.this, "deleted", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(hidtroy.this,MainActivity.class);
                    startActivity(intent);
                finish();}
                else {Toast.makeText(hidtroy.this, "not deleted", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(hidtroy.this,MainActivity.class);
                startActivity(intent);
                finish();}


            }
        }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


}




