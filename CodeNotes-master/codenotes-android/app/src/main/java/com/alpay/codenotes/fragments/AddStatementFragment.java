package com.alpay.codenotes.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddStatementFragment extends Fragment {

    boolean truthValue;
    static final int PICK_IMAGE = 1;
    public View view;
    private Unbinder unbinder;
    Bitmap bitmap;
    RadioGroup radioGroup;

    @BindView(R.id.newConditionalEditText)
    EditText newConditionalEditText;
    @BindView(R.id.newOutputEditText)
    EditText newOutputEditText;
    @BindView(R.id.selectedImageView)
    ImageView selectedImageView;

    public AddStatementFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_statement, container, false);
        unbinder = ButterKnife.bind(this,view);
        setTruthValueClickListener();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    // Butterknife does not work very well with radio groups.
    protected void setTruthValueClickListener() {
        radioGroup = (RadioGroup) view.findViewById(R.id.truthRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.trueButton) {
                    truthValue = true;
                } else if (checkedId == R.id.falseButton) {
                    truthValue = false;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            if (data == null) {
                Utils.printToastShort((AppCompatActivity) getActivity(),R.string.select_image_error);
                return;
            }
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                bitmap = decodeSampledBitmapFromStream(inputStream, 300, 300);
                selectedImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.selectImageFromGallery)
    protected void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @OnClick(R.id.saveNewOutputStatement)
    protected void saveNewOutputStatement() {
        String outputText = newOutputEditText.getText().toString();
        if (!outputText.equals("") && bitmap != null) {
            outputText = outputText.replace(" ", "");
            outputText = outputText.toUpperCase();
            checkIfOutputExistsAndSave(outputText, bitmapToBase64(bitmap));
        } else {
            Utils.printToastShort((AppCompatActivity) getActivity(), R.string.save_output_statement_error);
        }
    }

    @OnClick(R.id.saveNewLogicStatement)
    protected void saveNewLogicStatement() {
        String conditionalText = newConditionalEditText.getText().toString();
        if (!conditionalText.equals("")) {
            conditionalText = conditionalText.replace(" ", "");
            conditionalText = conditionalText.toUpperCase();
            checkIfConditionalExistsAndSave(conditionalText, truthValue);
        } else {
            Utils.printToastShort((AppCompatActivity) getActivity(),R.string.save_logic_statement_error);
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    protected Bitmap decodeSampledBitmapFromStream(InputStream inputStream, int reqWidth, int reqHeight) {
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return getResizedBitmap(bitmap, reqWidth, reqHeight);
    }

    protected String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    boolean conditionalChecker = true;

    protected void checkIfConditionalExistsAndSave(final String conditionalText, final boolean value) {

    }

    boolean outputChecker = true;

    protected void checkIfOutputExistsAndSave(final String outputText, final String encodedImage) {

    }

}
