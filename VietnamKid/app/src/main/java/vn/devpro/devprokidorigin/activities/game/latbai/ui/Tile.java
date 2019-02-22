package vn.devpro.devprokidorigin.activities.game.latbai.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.makeramen.roundedimageview.RoundedImageView;

import vn.devpro.devprokidorigin.R;


/**
 * Created by admin on 3/31/2018.
 */
// mặt sau của hình lật
public class Tile extends FrameLayout {


    private RelativeLayout mTopImage;

    public RoundedImageView getTileImage() {
        return mTileImage;
    }

    private RoundedImageView mTileImage;
    private boolean lat_nguoc = true;
    private String soundName ;



    public Tile(@NonNull Context context) {
        this( context, null );
    }

    public Tile(@NonNull Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
    }

    public static Tile fromXml(Context context, ViewGroup parent) {
        return (Tile) LayoutInflater.from( context ).inflate( R.layout.tile_matsau, parent, false );
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopImage = (RelativeLayout) findViewById( R.id.image_top );
        mTileImage = (RoundedImageView) findViewById( R.id.image );
    }

    public void setTileImage(Bitmap bitmap) {
        mTileImage.setImageBitmap( bitmap );
    }

    public String getSoundName() {
        return soundName;
    }

    public  void  setSoundName(String soundName)
    {
        this.soundName = soundName ;
    }

    public void flipUp() {
        lat_nguoc = false;
        flip();
    }

    public void flipDown() {
        lat_nguoc = true;
        flip();
    }

    private void flip() {
        FlipAnimation flipAnimation = new FlipAnimation( mTopImage, mTileImage );
        if (mTopImage.getVisibility() == View.GONE) {
            flipAnimation.reverse();
        }
        startAnimation( flipAnimation );
    }

    public boolean isFlippedDown() {
        return lat_nguoc;
    }

    public class FlipAnimation extends Animation {
        private Camera camera;

        private View fromView;
        private View toView;

        private float centerX;
        private float centerY;

        private boolean forward = true;
//        Tạo ra một hình ảnh động 3D lật giữa hai chế độ xem.
//                *
//                * @param fromView
//* Quan điểm đầu tiên trong quá trình chuyển đổi.
//* @param toView
//* Thứ hai xem trong quá trình chuyển đổi.

        public FlipAnimation(View fromView, View toView) {
            this.fromView = fromView;
            this.toView = toView;

            setDuration( 700 );
            setFillAfter( false );
            setInterpolator( new AccelerateDecelerateInterpolator() );
        }

//        đảo ngược
        public void reverse() {
            forward = false;
            View switchView = toView;
            toView = fromView;
            fromView = switchView;
        }

        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize( width, height, parentWidth, parentHeight );
            centerX = width / (2.2f);
            centerY = height / (2.2f);
            camera = new Camera();
        }

        protected void applyTransformation(float interpolatedTime, Transformation t) {
            // Góc xung quanh trục y của vòng xoay tại thời gian đã định
//			 tính bằng radian và độ.
            final double radians = Math.PI * interpolatedTime;
            float degrees = (float) (180.0 * radians / Math.PI);
//            	/ / Khi  đạt đến điểm giữa trong hoạt hình, chúng ta cần ẩn
////// xem mã nguồn và hiển thị chế độ xem đích. Chúng ta cũng cần phải thay đổi
////// góc bằng 180 độ sao cho điểm đến không đi vào
////					/ / xoay 1 nửa
            if (interpolatedTime >= 0.5f) {
                degrees -= 180.f;
                fromView.setVisibility( View.GONE );
                toView.setVisibility( View.VISIBLE );
            }

            if (forward)
                degrees = -degrees;
            // xác định hương quay
            // bắt đầu lật
            final Matrix matrix = t.getMatrix();
            camera.save();
            camera.rotateY( degrees );
            camera.getMatrix( matrix );
            camera.restore();
            matrix.preTranslate( -centerX, -centerY );
            matrix.postTranslate( centerX, centerY );

        }
    }
}