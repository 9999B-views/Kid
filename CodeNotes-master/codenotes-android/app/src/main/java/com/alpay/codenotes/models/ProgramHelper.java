package com.alpay.codenotes.models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProgramHelper {

    static List<Program> programList = new ArrayList<>();
    static Gson gson = new GsonBuilder().create();
    static Type programListType = new TypeToken<ArrayList<Program>>(){}.getType();
    static Resources resources;

    private static void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("programList.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private static BufferedReader readFromFile(Context context) {
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = context.openFileInput("programList.json");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
            }
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return bufferedReader;
    }

    public static void saveProgramList(Context context){
        String programListString = gson.toJson(programList);
        writeToFile(programListString, context);
    }

    public static List<Program> readProgramList(Context context) throws FileNotFoundException {
        BufferedReader bufferedReader = readFromFile(context);
        List<Program> data = new ArrayList<>();
        if(bufferedReader != null){
            JsonReader reader = new JsonReader(bufferedReader);
            data = gson.fromJson(reader, programListType);
        }
        programList = data;
        return data;
    }

    public static int getProgramListSize(){
        return programList.size();
    }

    public static String toJson(List<Program> programList) {
        return gson.toJson(programList);
    }

    public static List<Program> listFromJSON(String jsonString){
        ArrayList<Program> programList= gson.fromJson(jsonString, programListType);
        return programList;
    }

    public static List<Program> listAll(AppCompatActivity appCompatActivity){
        resources = appCompatActivity.getResources();
        if(programList.isEmpty()){
        }
        return programList;
    }

    public static void addNewProgram(Program program){
        programList.add(program);
    }

}