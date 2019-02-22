package vn.devpro.devprokidorigin.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by NamQuoc on 3/15/2018.
 */

public class ShareApp {
    public static void shareTo(Context context, String subject, String body) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body + " --> "+Global.RATE_APP_LINK+"?id="+context.getPackageName());
        context.startActivity(Intent.createChooser(sharingIntent, "Chia sẻ"));
    }
}
