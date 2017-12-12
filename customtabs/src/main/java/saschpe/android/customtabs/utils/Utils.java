package saschpe.android.customtabs.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;

import static saschpe.android.customtabs.CustomTabsHelper.UNDEFINED_RESOURCE;

/**
 * Created by luongvo on 12/13/17.
 */

public class Utils {

    /**
     * Get vector drawable with tint color
     *
     * @param context    context
     * @param drawableId The drawable ID
     * @param tintColor  The drawable tint color
     * @return Drawable
     */
    public static Drawable getDrawable(Context context, @DrawableRes int drawableId, @ColorInt int tintColor) {
        Drawable drawable = null;
        try {
            drawable = AppCompatResources.getDrawable(context, drawableId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        if (drawable == null) {
            return null;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // This should avoid tinting all the arrows
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        if (tintColor != UNDEFINED_RESOURCE) {
            DrawableCompat.setTint(drawable, tintColor);
        }

        return drawable;
    }

    /**
     * Converts a vector asset to a bitmap as required by {@link CustomTabsIntent.Builder#setCloseButtonIcon(Bitmap)}
     *
     * @param context    context
     * @param drawableId The drawable ID
     * @param tintColor  The drawable tint color
     * @return Bitmap equivalent
     */
    public static Bitmap getBitmapFromVectorDrawable(Context context, final @DrawableRes int drawableId, final @ColorInt int tintColor) {
        Drawable drawable = getDrawable(context, drawableId, tintColor);
        if (drawable == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
