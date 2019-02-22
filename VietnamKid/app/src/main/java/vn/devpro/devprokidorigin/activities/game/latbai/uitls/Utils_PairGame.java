package vn.devpro.devprokidorigin.activities.game.latbai.uitls;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.media.ThumbnailUtils;

import vn.devpro.devprokidorigin.activities.game.latbai.commom.Shared;


/**
 * Created by admin on 4/2/2018.
 */
// biến chung . đều được gọi để sử dụng




public class Utils_PairGame {
    public static int px(int dp) {
        return (int) (Shared.context.getResources().getDisplayMetrics().density * dp);
    }

    public static int screenWidth() {
        return Shared.context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int screenHeight() {
        return Shared.context.getResources().getDisplayMetrics().heightPixels;
    }

    public static Bitmap crop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = (int) (source.getWidth()/(1.3f));
        int sourceHeight = source.getHeight();

        // Tính toán các yếu tố tỷ lệ để phù hợp với chiều cao và chiều rộng mới,
        // tương ứng.
        // Để bao quát hình ảnh cuối cùng, tỉ lệ cuối cùng sẽ lớn hơn
        // trong hai.

        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max( xScale, yScale );

        // Bây giờ lấy kích thước của bitmap nguồn khi mở rộng
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;


        // Tìm đoạ đọ bitmap mới khi được gọi ra
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

//        thu nhỏ bitmap theo mục tiêu
//         theo hình chữ nhật đã định
//
        RectF targetRect = new RectF( left, top, left + scaledWidth, top + scaledHeight );

//        cuối cùng tạo ra bitmap đã định
        Bitmap dest = Bitmap.createBitmap( newWidth, newHeight, source.getConfig() );
        Canvas canvas = new Canvas( dest );
        canvas.drawBitmap( source, null, targetRect, null );

        return dest;
    }

    public static Bitmap scaleDown(int resource, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(Shared.context.getResources(), resource);

//         TÍNH TOÁN
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // giải mã với bộ cài đặt
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(Shared.context.getResources(), resource, options);
    }

    /**
     * Downscales a bitmap by the specified factor
     */
    public static Bitmap downscaleBitmap(Bitmap wallpaper, int factor) {
        // chuyeerr=rn đổi bitmap ở vị trí trugn tâm
        int widthPixels = wallpaper.getWidth() / factor;
        int heightPixels = wallpaper.getHeight() / factor;
        return ThumbnailUtils.extractThumbnail(wallpaper, widthPixels, heightPixels);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // độ cao và chiều rộng của hình ảnh
        int rong ,cao ;
        rong= reqWidth ;
        cao =reqHeight;
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

//            tính tỉ lệ chiều cao, chiều rộng yêu cầu
            final int heightRatio = Math.round( (float) height / (float) reqHeight );
            final int widthRatio = Math.round( (float) width / (float) reqWidth );

//chọn tỉ lệ nhỏ nhất của insamplesize vì như vậy sẽ đảm bảo hình ảnh cuối cùng theo yêu cầu chiều cao , chiều rộng
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static int readDefaultTopicId() {
        SharedPreferences perferences = Shared.context.getSharedPreferences( "Id", 0 );
        return perferences.getInt( "topicId", 5 );
    }

    public static void saveDefaultTopicId(int topicId) {
        SharedPreferences sharedPreferences = Shared.context.getSharedPreferences( "Id", 0 );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt( "topicId", topicId );
        editor.commit();
    }
}
