package vn.devpro.devprokidorigin.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by NamQuoc on 3/15/2018.
 */

public class Feedback {
    public static void sendTo(Context context, String email, String subject, String body) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(emailIntent, "Phản hồi"));
    }
}
