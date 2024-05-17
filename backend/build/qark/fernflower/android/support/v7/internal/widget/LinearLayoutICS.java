package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R$styleable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class LinearLayoutICS extends LinearLayout {
   private static final int SHOW_DIVIDER_BEGINNING = 1;
   private static final int SHOW_DIVIDER_END = 4;
   private static final int SHOW_DIVIDER_MIDDLE = 2;
   private static final int SHOW_DIVIDER_NONE = 0;
   private final Drawable mDivider;
   private final int mDividerHeight;
   private final int mDividerPadding;
   private final int mDividerWidth;
   private final int mShowDividers;

   public LinearLayoutICS(Context var1, AttributeSet var2) {
      boolean var3 = true;
      super(var1, var2);
      TypedArray var4 = var1.obtainStyledAttributes(var2, R$styleable.LinearLayoutICS);
      this.mDivider = var4.getDrawable(0);
      if (this.mDivider != null) {
         this.mDividerWidth = this.mDivider.getIntrinsicWidth();
         this.mDividerHeight = this.mDivider.getIntrinsicHeight();
      } else {
         this.mDividerWidth = 0;
         this.mDividerHeight = 0;
      }

      this.mShowDividers = var4.getInt(1, 0);
      this.mDividerPadding = var4.getDimensionPixelSize(2, 0);
      var4.recycle();
      if (this.mDivider != null) {
         var3 = false;
      }

      this.setWillNotDraw(var3);
   }

   void drawSupportDividersHorizontal(Canvas var1) {
      int var3 = this.getChildCount();

      int var2;
      View var4;
      for(var2 = 0; var2 < var3; ++var2) {
         var4 = this.getChildAt(var2);
         if (var4 != null && var4.getVisibility() != 8 && this.hasSupportDividerBeforeChildAt(var2)) {
            LayoutParams var5 = (LayoutParams)var4.getLayoutParams();
            this.drawSupportVerticalDivider(var1, var4.getLeft() - var5.leftMargin);
         }
      }

      if (this.hasSupportDividerBeforeChildAt(var3)) {
         var4 = this.getChildAt(var3 - 1);
         if (var4 == null) {
            var2 = this.getWidth() - this.getPaddingRight() - this.mDividerWidth;
         } else {
            var2 = var4.getRight();
         }

         this.drawSupportVerticalDivider(var1, var2);
      }

   }

   void drawSupportDividersVertical(Canvas var1) {
      int var3 = this.getChildCount();

      int var2;
      View var4;
      for(var2 = 0; var2 < var3; ++var2) {
         var4 = this.getChildAt(var2);
         if (var4 != null && var4.getVisibility() != 8 && this.hasSupportDividerBeforeChildAt(var2)) {
            LayoutParams var5 = (LayoutParams)var4.getLayoutParams();
            this.drawSupportHorizontalDivider(var1, var4.getTop() - var5.topMargin);
         }
      }

      if (this.hasSupportDividerBeforeChildAt(var3)) {
         var4 = this.getChildAt(var3 - 1);
         if (var4 == null) {
            var2 = this.getHeight() - this.getPaddingBottom() - this.mDividerHeight;
         } else {
            var2 = var4.getBottom();
         }

         this.drawSupportHorizontalDivider(var1, var2);
      }

   }

   void drawSupportHorizontalDivider(Canvas var1, int var2) {
      this.mDivider.setBounds(this.getPaddingLeft() + this.mDividerPadding, var2, this.getWidth() - this.getPaddingRight() - this.mDividerPadding, this.mDividerHeight + var2);
      this.mDivider.draw(var1);
   }

   void drawSupportVerticalDivider(Canvas var1, int var2) {
      this.mDivider.setBounds(var2, this.getPaddingTop() + this.mDividerPadding, this.mDividerWidth + var2, this.getHeight() - this.getPaddingBottom() - this.mDividerPadding);
      this.mDivider.draw(var1);
   }

   public int getSupportDividerWidth() {
      return this.mDividerWidth;
   }

   protected boolean hasSupportDividerBeforeChildAt(int var1) {
      if (var1 == 0) {
         if ((this.mShowDividers & 1) == 0) {
            return false;
         }
      } else {
         if (var1 != this.getChildCount()) {
            if ((this.mShowDividers & 2) == 0) {
               return false;
            }

            boolean var3 = false;
            --var1;

            boolean var2;
            while(true) {
               var2 = var3;
               if (var1 < 0) {
                  break;
               }

               if (this.getChildAt(var1).getVisibility() != 8) {
                  var2 = true;
                  break;
               }

               --var1;
            }

            return var2;
         }

         if ((this.mShowDividers & 4) == 0) {
            return false;
         }
      }

      return true;
   }

   protected void measureChildWithMargins(View var1, int var2, int var3, int var4, int var5) {
      if (this.mDivider != null) {
         int var6 = this.indexOfChild(var1);
         int var7 = this.getChildCount();
         LayoutParams var8 = (LayoutParams)var1.getLayoutParams();
         if (this.getOrientation() == 1) {
            if (this.hasSupportDividerBeforeChildAt(var6)) {
               var8.topMargin = this.mDividerHeight;
            } else if (var6 == var7 - 1 && this.hasSupportDividerBeforeChildAt(var7)) {
               var8.bottomMargin = this.mDividerHeight;
            }
         } else if (this.hasSupportDividerBeforeChildAt(var6)) {
            var8.leftMargin = this.mDividerWidth;
         } else if (var6 == var7 - 1 && this.hasSupportDividerBeforeChildAt(var7)) {
            var8.rightMargin = this.mDividerWidth;
         }
      }

      super.measureChildWithMargins(var1, var2, var3, var4, var5);
   }

   protected void onDraw(Canvas var1) {
      if (this.mDivider != null) {
         if (this.getOrientation() == 1) {
            this.drawSupportDividersVertical(var1);
         } else {
            this.drawSupportDividersHorizontal(var1);
         }
      }
   }
}
