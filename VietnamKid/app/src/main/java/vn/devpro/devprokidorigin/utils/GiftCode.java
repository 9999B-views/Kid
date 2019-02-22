package vn.devpro.devprokidorigin.utils;


import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.interfaces.UnlockTopicNameListener;

public class GiftCode {
    private Context context;
    private String code;
    private Boolean fistCheck = true;

    private UnlockTopicNameListener unlockTopicNameListener;

    public GiftCode(Context context, String code) {
        this.context = context;
        this.code = code;
    }

    public void checkGiftcode() {
        if (code != null && !code.equals("")) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference();
            myRef.child("giftcode").child(code).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Boolean value = dataSnapshot.getValue(Boolean.class);
                    if (value != null && value) {
                        unlockTopicNameListener.unlockTopicAll();
                        Utils.showToast(context, context.getString(R.string.da_mo_khoa_tat_ca_cac_chu_de));
                        myRef.child("giftcode").child(code).setValue(false);
                        fistCheck = false;
                    } else if (fistCheck) {
                        Utils.showToast(context, context.getString(R.string.code_khong_dung));
                    }
                    Log.d("DB Firebase", dataSnapshot.getKey() + " : " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.e("DB Firebase", "Failed to read value.", error.toException());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.khong_duoc_de_trong_code));
        }
    }

    public void setUnlockTopicNameListener(UnlockTopicNameListener unlockTopicNameListener) {
        this.unlockTopicNameListener = unlockTopicNameListener;
    }
}
