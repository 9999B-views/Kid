package vn.devpro.devprokidorigin.activities.game.latbai.themes;


import android.graphics.Bitmap;
import android.util.Log;

;

import java.util.ArrayList;

import vn.devpro.devprokidorigin.activities.game.latbai.commom.Shared;
import vn.devpro.devprokidorigin.activities.game.latbai.uitls.Utils_PairGame;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.models.entity.TopicItem;
import vn.devpro.devprokidorigin.utils.Global;

/**
 * Created by admin on 3/31/2018.
 */


//  ốp data ở đây
//     truyền data

public class Themes {

    // thử chạy trên 1 layout rỗng chưa có  gì rồi sau đó  gọi lên xem ảnh có được hiển thị

    // gọi ảnh của ng khác
    public static String URI_DRAWABLE = "drawable://";

// xử lý hàm for lấy ra set ảnh các background các chủ đề cũng như dựa vào các ID đó lấy ảnh bên trong cách chủ đê
public static Theme createThemeById(int topicId) {
    Theme theme = new Theme();
    theme.id =1 ;
    theme.name = "";
    theme.backgroundImageUrl = URI_DRAWABLE + "screens_09";
    Log.i( "AABBCC", "ảnh 1 chủ đề" );

    ArrayList<TopicItem> listImageForGame = new ArrayList<>();
    listImageForGame = DBHelper.getInstance( Global.mContext ).getTopicByid( topicId );
    theme.listImageForGame = listImageForGame;
    return theme;
}



    public static Bitmap getBackgroundImage(Theme theme) {
        String drawableResourceName = theme.backgroundImageUrl.substring( Themes.URI_DRAWABLE.length() );
        int drawableResourceId = Shared.context.getResources().getIdentifier( drawableResourceName, "drawable",
                Shared.context.getPackageName() );
        Bitmap bitmap = Utils_PairGame.scaleDown( drawableResourceId, Utils_PairGame.screenWidth(), Utils_PairGame.screenHeight() );
        return bitmap;
    }

}

