package pl.dsquare.home;

import android.content.Context;
import android.util.TypedValue;


/**
 * Created by Daniel on 2017-06-25.
 */

public class Units {
    public static final int VERSION=11;
    public static final int POLISH = 1;

    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp, context.getResources().getDisplayMetrics());
    }
    public static int pxToDp(Context context,int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }
}
