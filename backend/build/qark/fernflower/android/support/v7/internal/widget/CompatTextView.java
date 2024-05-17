package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v7.appcompat.R$styleable;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;

public class CompatTextView extends TextView {
   public CompatTextView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public CompatTextView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public CompatTextView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      TypedArray var5 = var1.obtainStyledAttributes(var2, R$styleable.CompatTextView, var3, 0);
      boolean var4 = var5.getBoolean(0, false);
      var5.recycle();
      if (var4) {
         this.setTransformationMethod(new CompatTextView.AllCapsTransformationMethod(var1));
      }

   }

   private static class AllCapsTransformationMethod implements TransformationMethod {
      private final Locale mLocale;

      public AllCapsTransformationMethod(Context var1) {
         this.mLocale = var1.getResources().getConfiguration().locale;
      }

      public CharSequence getTransformation(CharSequence var1, View var2) {
         return var1 != null ? var1.toString().toUpperCase(this.mLocale) : null;
      }

      public void onFocusChanged(View var1, CharSequence var2, boolean var3, int var4, Rect var5) {
      }
   }
}
