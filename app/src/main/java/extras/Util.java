package extras;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import java.util.List;

/**
 * Created by knighthood on 4/1/2016.
 */
public class Util {

    public static void showViews(List<View> views) {
        for (View view : views)
            view.setVisibility(View.VISIBLE);
    }

    public static void hideViews(List<View> views) {
        for (View view : views)
            view.setVisibility(View.GONE);
    }

    public static boolean beyondJellyBean(){
        if (Build.VERSION.SDK_INT > 15)
            return true;
        else
            return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBackground(View view, Drawable drawable){
        if (beyondJellyBean())
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }
}
