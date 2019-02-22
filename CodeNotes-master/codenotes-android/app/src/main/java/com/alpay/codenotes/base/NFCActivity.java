package com.alpay.codenotes.base;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NFCActivity extends AppCompatActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";

    private final String TAG_PROCESSOR = "processor";
    private final String TAG_MOTHERBOARD = "motherboard";
    private final String TAG_HARDDRIVE = "harddrive";

    @BindView(R.id.nfc_home_layout)
    RelativeLayout homeLayout;

    @BindView(R.id.nfc_harddisk_layout)
    RelativeLayout harddiskLayout;

    @BindView(R.id.nfc_motherboard_layout)
    RelativeLayout motherboardLayout;

    @BindView(R.id.nfc_processor_layout)
    RelativeLayout processorLayout;

    private NfcAdapter mNfcAdapter;

    private enum TAGVIEW {
        HOME, PROCESSOR, MOTHERBOARD, HARDDRIVE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        ButterKnife.bind(this);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Utils.showWarningToast(this, R.string.no_nfc_warning, Toast.LENGTH_LONG);
            finish();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            Utils.showWarningToast(this, R.string.nfc_disabled_warning, Toast.LENGTH_LONG);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        } else {
            openView(TAGVIEW.HOME);
        }
        handleIntent(getIntent());
    }

    private void openView(TAGVIEW tagview) {
        switch (tagview) {
            case HOME:
                homeLayout.setVisibility(View.VISIBLE);
                harddiskLayout.setVisibility(View.GONE);
                motherboardLayout.setVisibility(View.GONE);
                processorLayout.setVisibility(View.GONE);
                break;
            case MOTHERBOARD:
                homeLayout.setVisibility(View.GONE);
                harddiskLayout.setVisibility(View.GONE);
                motherboardLayout.setVisibility(View.VISIBLE);
                processorLayout.setVisibility(View.GONE);
                break;
            case HARDDRIVE:
                homeLayout.setVisibility(View.GONE);
                harddiskLayout.setVisibility(View.VISIBLE);
                motherboardLayout.setVisibility(View.GONE);
                processorLayout.setVisibility(View.GONE);
                break;
            case PROCESSOR:
                homeLayout.setVisibility(View.GONE);
                harddiskLayout.setVisibility(View.GONE);
                motherboardLayout.setVisibility(View.GONE);
                processorLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);
            } else {
                Utils.showErrorToast(this, R.string.error_text, Toast.LENGTH_SHORT);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        // unsupported encoding
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
            byte[] payload = record.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageCodeLength = payload[0] & 0063;
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.contentEquals(TAG_HARDDRIVE)) {
                    openView(TAGVIEW.HARDDRIVE);
                } else if (result.contentEquals(TAG_MOTHERBOARD)) {
                    openView(TAGVIEW.MOTHERBOARD);
                } else if (result.contentEquals(TAG_PROCESSOR)) {
                    openView(TAGVIEW.PROCESSOR);
                } else {
                    openView(TAGVIEW.HOME);
                }
            }
        }
    }

    public static void setupForegroundDispatch(final AppCompatActivity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }
        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final AppCompatActivity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
}
