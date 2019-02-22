package vn.devpro.devprokidorigin.models.entity.game.latbai;

import android.graphics.Bitmap;


import java.util.Map;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.activities.game.latbai.commom.Shared;
import vn.devpro.devprokidorigin.activities.game.latbai.uitls.Utils_PairGame;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;


/**
 * Created by admin on 3/31/2018.
 */
// trước khi bắt đầu bảng game mới , Engine  xây dựng bảng mới

public class BoardXep {


    public Map<Integer, Integer> pairs;
    //     cặp bài khi click vào 2 lá
    public Map<Integer, String> tileUrls;
//     click 1 lá chỉ rõ ví trí cũng như ở đâu

    //   của mình dùng ở đây
    public Map<Integer, Image_n_Sound_Name> tileImageNames;


    // FIXME: 4/18/2018
    public Bitmap getTitleBitmap(int id, int size) {
        //String string = tileUrls.get( id );
        String mString = tileImageNames.get(id).imageName;

        if (true/*string.contains(Themes.URI_DRAWABLE*/) {
            //String drawableResourceName = string.substring(Themes.URI_DRAWABLE.length());
            //int drawableResourceId = Shared.context.getResources().getIdentifier(drawableResourceName,
            //        "drawable", Shared.context.getPackageName());
            Bitmap bitmap = null;// = Utils.scaleDown(drawableResourceId, size, size);
            String suffix;
            if (Shared.engine.getCurrentopicId() == 1) {
                suffix = ".png";
            } else {
                suffix = ".jpg";
            }
            byte byteArrayFromSD[] = Utils.getByteArrayFromSD(Global.pathImages, mString, suffix);
            if (byteArrayFromSD != null) {
                bitmap = Utils.displayImageForItem(Utils.decodeFileImage(byteArrayFromSD));
            } else {
                bitmap = Utils_PairGame.scaleDown(R.drawable.no_image_available, (int) (size * (1.3f)), (int) (size));
            }
            if (bitmap == null) return null;
            return Utils_PairGame.crop(bitmap, size, (int) ((int) (size) * (1.0f)));
        }
        return null;
    }

    public String getTileSoundName(int id) {
        return tileImageNames.get(id).soundName;

    }

    public boolean isPair(int id1, int id2) {
//         so sánh 2 giá trị xem có  giống nhau không
        Integer integer = pairs.get(id1);
        if (integer == null) {
//              báo cáo lỗi này khi chưa nhận giá trị
            return false;
        }
        return integer.equals(id2);
    }

}
