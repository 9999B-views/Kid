package com.alpay.wesapiens.models;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.alpay.wesapiens.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class FrameHelper {

    static List<Frame> frameList = new ArrayList<>();
    static Gson gson = new GsonBuilder().create();
    static Type frameListType = new TypeToken<ArrayList<Frame>>(){}.getType();
    static Resources resources;

    private static void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("frameList.json", Context.MODE_PRIVATE));
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
            InputStream inputStream = context.openFileInput("frameList.json");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
            }
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return bufferedReader;
    }

    public static void saveFrameList(Context context){
        String frameListString = gson.toJson(frameList);
        writeToFile(frameListString, context);
    }

    public static List<Frame> readFrameList(Context context) throws FileNotFoundException {
        BufferedReader bufferedReader = readFromFile(context);
        List<Frame> data = new ArrayList<>();
        if(bufferedReader != null){
            JsonReader reader = new JsonReader(bufferedReader);
            data = gson.fromJson(reader, frameListType);
        }
        frameList = data;
        return data;
    }

    public static int getFrameListSize(){
        return frameList.size();
    }

    public static String toJson(List<Frame> frameList) {
        return gson.toJson(frameList);
    }

    public static List<Frame> listFromJSON(String jsonString){
        ArrayList<Frame> frameList= gson.fromJson(jsonString, frameListType);
        return frameList;
    }

    public static List<Frame> listAll(AppCompatActivity appCompatActivity){
        resources = appCompatActivity.getResources();
        if(frameList.isEmpty()){
            frameList.add(new Frame(1,
                    resources.getString(R.string.question1_title),
                    resources.getString(R.string.question1_time),
                    resources.getString(R.string.question1_place),
                    "1.png", "2.png",
                    resources.getString(R.string.question1_context),
                    resources.getString(R.string.question1_question),
                    resources.getString(R.string.question1_answer)));
            frameList.add(new Frame(2,
                    resources.getString(R.string.question2_title),
                    resources.getString(R.string.question2_time),
                    resources.getString(R.string.question2_place),
                    "3.png", "4.png",
                    resources.getString(R.string.question2_context),
                    resources.getString(R.string.question2_question),
                    resources.getString(R.string.question2_answer)));
            frameList.add(new Frame(3,
                    resources.getString(R.string.question3_title),
                    resources.getString(R.string.question3_time),
                    resources.getString(R.string.question3_place),
                    "5.png", "6.png",
                    resources.getString(R.string.question3_context),
                    resources.getString(R.string.question3_question),
                    resources.getString(R.string.question3_answer)));
            frameList.add(new Frame(4,
                    resources.getString(R.string.question4_title),
                    resources.getString(R.string.question4_time),
                    resources.getString(R.string.question4_place),
                    "7.png", "8.png",
                    resources.getString(R.string.question4_context),
                    resources.getString(R.string.question4_question),
                    resources.getString(R.string.question4_answer)));
            frameList.add(new Frame(5,
                    resources.getString(R.string.question5_title),
                    resources.getString(R.string.question5_time),
                    resources.getString(R.string.question5_place),
                    "9.png", "50.png",
                    resources.getString(R.string.question5_context),
                    resources.getString(R.string.question5_question),
                    resources.getString(R.string.question5_answer)));
        }
        return frameList;
    }

    public static void addNewFrame(Frame frame){
        frameList.add(frame);
    }

}
