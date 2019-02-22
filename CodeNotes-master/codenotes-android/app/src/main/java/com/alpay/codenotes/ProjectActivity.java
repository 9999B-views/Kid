package com.alpay.codenotes;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.alpay.codenotes.adapter.ImagePickerAdapter;
import com.alpay.codenotes.adapter.ProgramAdapter;
import com.alpay.codenotes.models.Program;
import com.alpay.codenotes.models.ProgramHelper;
import com.alpay.codenotes.utils.interpreter.TextRecognition;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProjectActivity extends AppCompatActivity {

    ImagePickerAdapter imagePickerAdapter;
    ProgramAdapter programAdapter;

    @BindView(R.id.program_image_list)
    RecyclerView imageList;

    @BindView(R.id.program_list)
    RecyclerView programList;

    @BindView(R.id.program_name_et)
    AppCompatEditText programNameET;

    @OnClick(R.id.program_image_select)
    public void selectImage(){
        Pix.start(ProjectActivity.this, Options.init().setRequestCode(100).setCount(5).setFrontfacing(false));
    }

    @OnClick(R.id.program_save)
    public void saveProgram(){
        Program program = new Program();
        program.setProgramName(programNameET.getText().toString());
        program.setProgramImages(imagePickerAdapter.getImageList());
        TextRecognition.saveTextRecogResult(program, imagePickerAdapter.getImageBitmapList(this));
        programAdapter.addNewProgram(program);
        programNameET.getText().clear();
        imagePickerAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        ButterKnife.bind(this);
        populateRecyclerView();
        hideSoftKeyboard();
    }

    private void populateRecyclerView(){
        imageList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        programList.setLayoutManager(new LinearLayoutManager(this));
        imagePickerAdapter = new ImagePickerAdapter(this);
        programAdapter = new ProgramAdapter(this);
        imageList.setAdapter(imagePickerAdapter);
        programList.setAdapter(programAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (100): {
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    imagePickerAdapter.addImage(returnValue);
                }
            }
            break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(ProjectActivity.this, Options.init().setRequestCode(100).setCount(1));
                } else {
                    Toast.makeText(ProjectActivity.this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void showSoftKeyboard(View view) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ProgramHelper.saveProgramList(this);
    }
}