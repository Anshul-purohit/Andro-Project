package android.support.v4.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

final class SwipeProgressBar {
   private static final int ANIMATION_DURATION_MS = 2000;
   private static final int COLOR1 = -1291845632;
   private static final int COLOR2 = Integer.MIN_VALUE;
   private static final int COLOR3 = 1291845632;
   private static final int COLOR4 = 436207616;
   private static final int FINISH_ANIMATION_DURATION_MS = 1000;
   private static final Interpolator INTERPOLATOR = BakedBezierInterpolator.getInstance();
   private Rect mBounds = new Rect();
   private final RectF mClipRect = new RectF();
   private int mColor1;
   private int mColor2;
   private int mColor3;
   private int mColor4;
   private long mFinishTime;
   private final Paint mPaint = new Paint();
   private View mParent;
   private boolean mRunning;
   private long mStartTime;
   private float mTriggerPercentage;

   public SwipeProgressBar(View var1) {
      this.mParent = var1;
      this.mColor1 = -1291845632;
      this.mColor2 = Integer.MIN_VALUE;
      this.mColor3 = 1291845632;
      this.mColor4 = 436207616;
   }

   private void drawCircle(Canvas var1, float var2, float var3, int var4, float var5) {
      this.mPaint.setColor(var4);
      var1.save();
      var1.translate(var2, var3);
      var3 = INTERPOLATOR.getInterpolation(var5);
      var1.scale(var3, var3);
      var1.drawCircle(0.0F, 0.0F, var2, this.mPaint);
      var1.restore();
   }

   private void drawTrigger(Canvas var1, int var2, int var3) {
      this.mPaint.setColor(this.mColor1);
      var1.drawCircle((float)var2, (float)var3, (float)var2 * this.mTriggerPercentage, this.mPaint);
   }

   void draw(Canvas var1) {
      int var4 = this.mBounds.width();
      int var9 = this.mBounds.height();
      int var7 = var4 / 2;
      int var8 = var9 / 2;
      boolean var6 = false;
      int var5 = var1.save();
      var1.clipRect(this.mBounds);
      if (!this.mRunning && this.mFinishTime <= 0L) {
         var4 = var5;
         if (this.mTriggerPercentage > 0.0F) {
            var4 = var5;
            if ((double)this.mTriggerPercentage <= 1.0D) {
               this.drawTrigger(var1, var7, var8);
               var4 = var5;
            }
         }
      } else {
         long var10 = AnimationUtils.currentAnimationTimeMillis();
         long var12 = this.mStartTime;
         long var14 = (var10 - this.mStartTime) / 2000L;
         float var2 = (float)((var10 - var12) % 2000L) / 20.0F;
         float var3;
         if (!this.mRunning) {
            if (var10 - this.mFinishTime >= 1000L) {
               this.mFinishTime = 0L;
               return;
            }

            var3 = (float)((var10 - this.mFinishTime) % 1000L) / 10.0F / 100.0F;
            var3 = (float)(var4 / 2) * INTERPOLATOR.getInterpolation(var3);
            this.mClipRect.set((float)var7 - var3, 0.0F, (float)var7 + var3, (float)var9);
            var1.saveLayerAlpha(this.mClipRect, 0, 0);
            var6 = true;
         }

         if (var14 == 0L) {
            var1.drawColor(this.mColor1);
         } else if (var2 >= 0.0F && var2 < 25.0F) {
            var1.drawColor(this.mColor4);
         } else if (var2 >= 25.0F && var2 < 50.0F) {
            var1.drawColor(this.mColor1);
         } else if (var2 >= 50.0F && var2 < 75.0F) {
            var1.drawColor(this.mColor2);
         } else {
            var1.drawColor(this.mColor3);
         }

         if (var2 >= 0.0F && var2 <= 25.0F) {
            var3 = (25.0F + var2) * 2.0F / 100.0F;
            this.drawCircle(var1, (float)var7, (float)var8, this.mColor1, var3);
         }

         if (var2 >= 0.0F && var2 <= 50.0F) {
            var3 = 2.0F * var2 / 100.0F;
            this.drawCircle(var1, (float)var7, (float)var8, this.mColor2, var3);
         }

         if (var2 >= 25.0F && var2 <= 75.0F) {
            var3 = (var2 - 25.0F) * 2.0F / 100.0F;
            this.drawCircle(var1, (float)var7, (float)var8, this.mColor3, var3);
         }

         if (var2 >= 50.0F && var2 <= 100.0F) {
            var3 = (var2 - 50.0F) * 2.0F / 100.0F;
            this.drawCircle(var1, (float)var7, (float)var8, this.mColor4, var3);
         }

         if (var2 >= 75.0F && var2 <= 100.0F) {
            var2 = (var2 - 75.0F) * 2.0F / 100.0F;
            this.drawCircle(var1, (float)var7, (float)var8, this.mColor1, var2);
         }

         var4 = var5;
         if (this.mTriggerPercentage > 0.0F) {
            var4 = var5;
            if (var6) {
               var1.restoreToCount(var5);
               var4 = var1.save();
               var1.clipRect(this.mBounds);
               this.drawTrigger(var1, var7, var8);
            }
         }

         ViewCompat.postInvalidateOnAnimation(this.mParent);
      }

      var1.restoreToCount(var4);
   }

   boolean isRunning() {
      return this.mRunning || this.mFinishTime > 0L;
   }

   void setBounds(int var1, int var2, int var3, int var4) {
      this.mBounds.left = var1;
      this.mBounds.top = var2;
      this.mBounds.right = var3;
      this.mBounds.bottom = var4;
   }

   void setColorScheme(int var1, int var2, int var3, int var4) {
      this.mColor1 = var1;
      this.mColor2 = var2;
      this.mColor3 = var3;
      this.mColor4 = var4;
   }

   void setTriggerPercentage(float var1) {
      this.mTriggerPercentage = var1;
      this.mStartTime = 0L;
      ViewCompat.postInvalidateOnAnimation(this.mParent);
   }

   void start() {
      if (!this.mRunning) {
         this.mTriggerPercentage = 0.0F;
         this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
         this.mRunning = true;
         this.mParent.postInvalidate();
      }

   }

   void stop() {
      if (this.mRunning) {
         this.mTriggerPercentage = 0.0F;
         this.mFinishTime = AnimationUtils.currentAnimationTimeMillis();
         this.mRunning = false;
         this.mParent.postInvalidate();
      }

   }
}
