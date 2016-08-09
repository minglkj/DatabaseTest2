package com.example.lovem.databasetest2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity
{

    public static void backupDatabase() throws IOException {
        //Open your local db as the input stream 
        String inFileName = "/data/data/com.example.lovem.databasetest2/databases/BookStore.db";
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStorageDirectory()
                + "/BookStore.db";
        //Open the empty db as the output stream 
        OutputStream output = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile 
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }
        //Close the streams 
        output.flush();
        output.close();
        fis.close();
    }


    private MyDatabaseHelper myDatabaseHelper;
    private Button createDatabase;

    public static void mLog(String string){
        Log.d("MainActivity",string);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getBookstoreDB=(Button)findViewById(R.id.bookstore_db);
        getBookstoreDB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                handleBackupDatabase();
                Toast.makeText(MainActivity.this,"got database",Toast.LENGTH_SHORT).show();
            }
        });


        createDatabase=(Button) findViewById(R.id.create_database);

        myDatabaseHelper=new MyDatabaseHelper(this,"BookStore.db",null,4);
        createDatabase.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                myDatabaseHelper.getWritableDatabase();
                handleBackupDatabase();
            }
        });

        Button addData=(Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                SQLiteDatabase sqLiteDatabase=myDatabaseHelper.getWritableDatabase();
                ContentValues contentValues=new ContentValues();
                //the first data
                contentValues.put("name","the da vinci code");
                contentValues.put("author","dan brown");
                contentValues.put("pages",454);
                contentValues.put("price",16.56);
                sqLiteDatabase.insert("Book",null,contentValues);
                contentValues.clear();
                //the second data
                contentValues.put("name","the lost symbol");
                contentValues.put("author","dan brown");
                contentValues.put("pages",510);
                contentValues.put("price",19.95);
                sqLiteDatabase.insert("Book",null,contentValues);
                contentValues.clear();



                handleBackupDatabase();
            }
        });

        Button updateData=(Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                SQLiteDatabase sqLiteDatabase=myDatabaseHelper.getWritableDatabase();
                ContentValues contentValues=new ContentValues();
                contentValues.put("price",10.99);
                sqLiteDatabase.update("Book",contentValues,"name=?",new String[]{
                        "the da vinci code"
                });


            }
        });

        Button deleteData=(Button) findViewById(R.id.delete_data);
        Button queryData=(Button)findViewById(R.id.query_data);
        deleteData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                SQLiteDatabase sqLiteDatabase=myDatabaseHelper.getWritableDatabase();
                sqLiteDatabase.delete("Book","pages>?",new String[]{
                        "500"
                });
            }
        });

        queryData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                SQLiteDatabase sqLiteDatabase=myDatabaseHelper.getWritableDatabase();
                //query all data
                Cursor cursor=sqLiteDatabase.query("Book",null,null,null,null,null,null);

                if (cursor.moveToFirst()){
                    do {
                        //get all object and print it
                        String name=cursor.getString(cursor.getColumnIndex("name"));
                        String author =cursor.getString(cursor.getColumnIndex("author"));
                        int pages=cursor.getInt(cursor.getColumnIndex("pages"));
                        double price=cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("Main","book name is "+name
                        +"\nbook author is "+ author
                        +"\n book pages is "+ pages
                                +"\n book price is "+price
                        );

                    }while (cursor.moveToNext());
                }
                cursor.close();

            }
        });

        Button replaceData=(Button) findViewById(R.id.replace_data);
        replaceData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                SQLiteDatabase sqLiteDatabase=myDatabaseHelper.getWritableDatabase();
                        sqLiteDatabase.beginTransaction();//开启事务
                try{
                    sqLiteDatabase.delete("Book",null,null);
                    if(true)
                    {
                        //在这里手动抛出了一个异常，让事务失败。
//                        throw new NullPointerException();

                    }
                    ContentValues contentValues=new ContentValues();
                    contentValues.put("name","Game of Thrones");
                    contentValues.put("author","george martin");
                    contentValues.put("pages",720);
                    contentValues.put ("price",20.86);
                    sqLiteDatabase.insert("Book",null,contentValues);
                    sqLiteDatabase.setTransactionSuccessful();//事务已经执行成功


                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    sqLiteDatabase.endTransaction();//结束事务
                }

            }
        });

    }



    void handleBackupDatabase(){
        try{
            backupDatabase();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
