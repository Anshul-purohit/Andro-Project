package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class ProgressBarICS extends View {
   private static final int ANIMATION_RESOLUTION = 200;
   private static final int MAX_LEVEL = 10000;
   private static final int[] android_R_styleable_ProgressBar = new int[]{16843062, 16843063, 16843064, 16843065, 16843066, 16843067, 16843068, 16843069, 16843070, 16843071, 16843039, 16843072, 16843040, 16843073};
   private AlphaAnimation mAnimation;
   private int mBehavior;
   private Drawable mCurrentDrawable;
   private int mDuration;
   private boolean mInDrawing;
   private boolean mIndeterminate;
   private Drawable mIndeterminateDrawable;
   private Interpolator mInterpolator;
   private long mLastDrawTime;
   private int mMax;
   int mMaxHeight;
   int mMaxWidth;
   int mMinHeight;
   int mMinWidth;
   private boolean mNoInvalidate;
   private boolean mOnlyIndeterminate;
   private int mProgress;
   private Drawable mProgressDrawable;
   private ProgressBarICS.RefreshProgressRunnable mRefreshProgressRunnable;
   Bitmap mSampleTile;
   private int mSecondaryProgress;
   private boolean mShouldStartAnimationDrawable;
   private Transformation mTransformation;
   private long mUiThreadId;

   public ProgressBarICS(Context var1, AttributeSet var2, int var3, int var4) {
      boolean var5 = false;
      super(var1, var2, var3);
      this.mUiThreadId = Thread.currentThread().getId();
      this.initProgressBar();
      TypedArray var8 = var1.obtainStyledAttributes(var2, android_R_styleable_ProgressBar, var3, var4);
      this.mNoInvalidate = true;
      this.setMax(var8.getInt(0, this.mMax));
      this.setProgress(var8.getInt(1, this.mProgress));
      this.setSecondaryProgress(var8.getInt(2, this.mSecondaryProgress));
      boolean var6 = var8.getBoolean(3, this.mIndeterminate);
      this.mOnlyIndeterminate = var8.getBoolean(4, this.mOnlyIndeterminate);
      Drawable var7 = var8.getDrawable(5);
      if (var7 != null) {
         this.setIndeterminateDrawable(this.tileifyIndeterminate(var7));
      }

      var7 = var8.getDrawable(6);
      if (var7 != null) {
         this.setProgressDrawable(this.tileify(var7, false));
      }

      this.mDuration = var8.getInt(7, this.mDuration);
      this.mBehavior = var8.getInt(8, this.mBehavior);
      this.mMinWidth = var8.getDimensionPixelSize(9, this.mMinWidth);
      this.mMaxWidth = var8.getDimensionPixelSize(10, this.mMaxWidth);
      this.mMinHeight = var8.getDimensionPixelSize(11, this.mMinHeight);
      this.mMaxHeight = var8.getDimensionPixelSize(12, this.mMaxHeight);
      var3 = var8.getResourceId(13, 17432587);
      if (var3 > 0) {
         this.setInterpolator(var1, var3);
      }

      var8.recycle();
      this.mNoInvalidate = false;
      if (this.mOnlyIndeterminate || var6) {
         var5 = true;
      }

      this.setIndeterminate(var5);
   }

   private void doRefreshProgress(int var1, int var2, boolean var3, boolean var4) {
      synchronized(this){}

      Throwable var10000;
      label344: {
         float var5;
         boolean var10001;
         label332: {
            try {
               if (this.mMax > 0) {
                  var5 = (float)var2 / (float)this.mMax;
                  break label332;
               }
            } catch (Throwable var37) {
               var10000 = var37;
               var10001 = false;
               break label344;
            }

            var5 = 0.0F;
         }

         Drawable var7;
         try {
            var7 = this.mCurrentDrawable;
         } catch (Throwable var36) {
            var10000 = var36;
            var10001 = false;
            break label344;
         }

         if (var7 != null) {
            Drawable var6 = null;

            try {
               if (var7 instanceof LayerDrawable) {
                  var6 = ((LayerDrawable)var7).findDrawableByLayerId(var1);
               }
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label344;
            }

            var1 = (int)(10000.0F * var5);
            if (var6 == null) {
               var6 = var7;
            }

            try {
               var6.setLevel(var1);
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label344;
            }
         } else {
            try {
               this.invalidate();
            } catch (Throwable var35) {
               var10000 = var35;
               var10001 = false;
               break label344;
            }
         }

         return;
      }

      Throwable var38 = var10000;
      throw var38;
   }

   private void initProgressBar() {
      this.mMax = 100;
      this.mProgress = 0;
      this.mSecondaryProgress = 0;
      this.mIndeterminate = false;
      this.mOnlyIndeterminate = false;
      this.mDuration = 4000;
      this.mBehavior = 1;
      this.mMinWidth = 24;
      this.mMaxWidth = 48;
      this.mMinHeight = 24;
      this.mMaxHeight = 48;
   }

   private void refreshProgress(int var1, int var2, boolean var3) {
      synchronized(this){}

      Throwable var10000;
      label205: {
         boolean var10001;
         try {
            if (this.mUiThreadId == Thread.currentThread().getId()) {
               this.doRefreshProgress(var1, var2, var3, true);
               return;
            }
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label205;
         }

         ProgressBarICS.RefreshProgressRunnable var4;
         label206: {
            try {
               if (this.mRefreshProgressRunnable != null) {
                  var4 = this.mRefreshProgressRunnable;
                  this.mRefreshProgressRunnable = null;
                  var4.setup(var1, var2, var3);
                  break label206;
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label205;
            }

            try {
               var4 = new ProgressBarICS.RefreshProgressRunnable(var1, var2, var3);
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label205;
            }
         }

         label187:
         try {
            this.post(var4);
            return;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label187;
         }
      }

      Throwable var25 = var10000;
      throw var25;
   }

   private Drawable tileify(Drawable var1, boolean var2) {
      Object var9;
      if (var1 instanceof LayerDrawable) {
         LayerDrawable var7 = (LayerDrawable)var1;
         int var4 = var7.getNumberOfLayers();
         Drawable[] var8 = new Drawable[var4];

         int var3;
         for(var3 = 0; var3 < var4; ++var3) {
            int var5 = var7.getId(var3);
            Drawable var6 = var7.getDrawable(var3);
            if (var5 != 16908301 && var5 != 16908303) {
               var2 = false;
            } else {
               var2 = true;
            }

            var8[var3] = this.tileify(var6, var2);
         }

         LayerDrawable var12 = new LayerDrawable(var8);
         var3 = 0;

         while(true) {
            var9 = var12;
            if (var3 >= var4) {
               break;
            }

            var12.setId(var3, var7.getId(var3));
            ++var3;
         }
      } else {
         if (!(var1 instanceof BitmapDrawable)) {
            return var1;
         }

         Bitmap var10 = ((BitmapDrawable)var1).getBitmap();
         if (this.mSampleTile == null) {
            this.mSampleTile = var10;
         }

         ShapeDrawable var13 = new ShapeDrawable(this.getDrawableShape());
         BitmapShader var11 = new BitmapShader(var10, TileMode.REPEAT, TileMode.CLAMP);
         var13.getPaint().setShader(var11);
         var9 = var13;
         if (var2) {
            var9 = new ClipDrawable(var13, 3, 1);
         }
      }

      return (Drawable)var9;
   }

   private Drawable tileifyIndeterminate(Drawable var1) {
      Object var4 = var1;
      if (var1 instanceof AnimationDrawable) {
         AnimationDrawable var6 = (AnimationDrawable)var1;
         int var3 = var6.getNumberOfFrames();
         var4 = new AnimationDrawable();
         ((AnimationDrawable)var4).setOneShot(var6.isOneShot());

         for(int var2 = 0; var2 < var3; ++var2) {
            Drawable var5 = this.tileify(var6.getFrame(var2), true);
            var5.setLevel(10000);
            ((AnimationDrawable)var4).addFrame(var5, var6.getDuration(var2));
         }

         ((AnimationDrawable)var4).setLevel(10000);
      }

      return (Drawable)var4;
   }

   private void updateDrawableBounds(int var1, int var2) {
      int var7 = var1 - this.getPaddingRight() - this.getPaddingLeft();
      int var8 = var2 - this.getPaddingBottom() - this.getPaddingTop();
      byte var11 = 0;
      byte var12 = 0;
      int var5 = var8;
      int var6 = var7;
      if (this.mIndeterminateDrawable != null) {
         var5 = var8;
         int var10 = var12;
         var6 = var7;
         int var9 = var11;
         if (this.mOnlyIndeterminate) {
            var5 = var8;
            var10 = var12;
            var6 = var7;
            var9 = var11;
            if (!(this.mIndeterminateDrawable instanceof AnimationDrawable)) {
               var5 = this.mIndeterminateDrawable.getIntrinsicWidth();
               var6 = this.mIndeterminateDrawable.getIntrinsicHeight();
               float var3 = (float)var5 / (float)var6;
               float var4 = (float)var1 / (float)var2;
               var5 = var8;
               var10 = var12;
               var6 = var7;
               var9 = var11;
               if (var3 != var4) {
                  if (var4 > var3) {
                     var2 = (int)((float)var2 * var3);
                     var10 = (var1 - var2) / 2;
                     var6 = var10 + var2;
                     var9 = var11;
                     var5 = var8;
                  } else {
                     var1 = (int)((float)var1 * (1.0F / var3));
                     var9 = (var2 - var1) / 2;
                     var5 = var9 + var1;
                     var10 = var12;
                     var6 = var7;
                  }
               }
            }
         }

         this.mIndeterminateDrawable.setBounds(var10, var9, var6, var5);
      }

      if (this.mProgressDrawable != null) {
         this.mProgressDrawable.setBounds(0, 0, var6, var5);
      }

   }

   private void updateDrawableState() {
      int[] var1 = this.getDrawableState();
      if (this.mProgressDrawable != null && this.mProgressDrawable.isStateful()) {
         this.mProgressDrawable.setState(var1);
      }

      if (this.mIndeterminateDrawable != null && this.mIndeterminateDrawable.isStateful()) {
         this.mIndeterminateDrawable.setState(var1);
      }

   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      this.updateDrawableState();
   }

   Shape getDrawableShape() {
      return new RoundRectShape(new float[]{5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F}, (RectF)null, (float[])null);
   }

   public Drawable getIndeterminateDrawable() {
      return this.mIndeterminateDrawable;
   }

   public Interpolator getInterpolator() {
      return this.mInterpolator;
   }

   public int getMax() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.mMax;
      } finally {
         ;
      }

      return var1;
   }

   public int getProgress() {
      synchronized(this){}

      Throwable var10000;
      label79: {
         boolean var10001;
         boolean var2;
         try {
            var2 = this.mIndeterminate;
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label79;
         }

         int var1;
         if (var2) {
            var1 = 0;
            return var1;
         }

         label69:
         try {
            var1 = this.mProgress;
            return var1;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label69;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public Drawable getProgressDrawable() {
      return this.mProgressDrawable;
   }

   public int getSecondaryProgress() {
      synchronized(this){}

      Throwable var10000;
      label79: {
         boolean var10001;
         boolean var2;
         try {
            var2 = this.mIndeterminate;
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label79;
         }

         int var1;
         if (var2) {
            var1 = 0;
            return var1;
         }

         label69:
         try {
            var1 = this.mSecondaryProgress;
            return var1;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label69;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public final void incrementProgressBy(int var1) {
      synchronized(this){}

      try {
         this.setProgress(this.mProgress + var1);
      } finally {
         ;
      }

   }

   public final void incrementSecondaryProgressBy(int var1) {
      synchronized(this){}

      try {
         this.setSecondaryProgress(this.mSecondaryProgress + var1);
      } finally {
         ;
      }

   }

   public void invalidateDrawable(Drawable var1) {
      if (!this.mInDrawing) {
         if (!this.verifyDrawable(var1)) {
            super.invalidateDrawable(var1);
            return;
         }

         Rect var4 = var1.getBounds();
         int var2 = this.getScrollX() + this.getPaddingLeft();
         int var3 = this.getScrollY() + this.getPaddingTop();
         this.invalidate(var4.left + var2, var4.top + var3, var4.right + var2, var4.bottom + var3);
      }

   }

   public boolean isIndeterminate() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.mIndeterminate;
      } finally {
         ;
      }

      return var1;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.mIndeterminate) {
         this.startAnimation();
      }

   }

   protected void onDetachedFromWindow() {
      if (this.mIndeterminate) {
         this.stopAnimation();
      }

      if (this.mRefreshProgressRunnable != null) {
         this.removeCallbacks(this.mRefreshProgressRunnable);
      }

      super.onDetachedFromWindow();
   }

   protected void onDraw(Canvas var1) {
      synchronized(this){}

      Throwable var10000;
      label371: {
         boolean var10001;
         Drawable var5;
         try {
            super.onDraw(var1);
            var5 = this.mCurrentDrawable;
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label371;
         }

         if (var5 == null) {
            return;
         }

         label372: {
            float var2;
            try {
               var1.save();
               var1.translate((float)this.getPaddingLeft(), (float)this.getPaddingTop());
               long var3 = this.getDrawingTime();
               if (this.mAnimation == null) {
                  break label372;
               }

               this.mAnimation.getTransformation(var3, this.mTransformation);
               var2 = this.mTransformation.getAlpha();
            } catch (Throwable var43) {
               var10000 = var43;
               var10001 = false;
               break label371;
            }

            try {
               this.mInDrawing = true;
               var5.setLevel((int)(10000.0F * var2));
            } finally {
               try {
                  this.mInDrawing = false;
               } catch (Throwable var40) {
                  var10000 = var40;
                  var10001 = false;
                  break label371;
               }
            }

            if (SystemClock.uptimeMillis() - this.mLastDrawTime >= 200L) {
               this.mLastDrawTime = SystemClock.uptimeMillis();
               this.postInvalidateDelayed(200L);
            }
         }

         label353:
         try {
            var5.draw(var1);
            var1.restore();
            if (this.mShouldStartAnimationDrawable && var5 instanceof Animatable) {
               ((Animatable)var5).start();
               this.mShouldStartAnimationDrawable = false;
            }

            return;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label353;
         }
      }

      Throwable var45 = var10000;
      throw var45;
   }

   protected void onMeasure(int var1, int var2) {
      synchronized(this){}

      Throwable var10000;
      label116: {
         Drawable var9;
         boolean var10001;
         try {
            var9 = this.mCurrentDrawable;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label116;
         }

         int var4 = 0;
         int var3 = 0;
         if (var9 != null) {
            try {
               var4 = Math.max(this.mMinWidth, Math.min(this.mMaxWidth, var9.getIntrinsicWidth()));
               var3 = Math.max(this.mMinHeight, Math.min(this.mMaxHeight, var9.getIntrinsicHeight()));
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label116;
            }
         }

         label104:
         try {
            this.updateDrawableState();
            int var5 = this.getPaddingLeft();
            int var6 = this.getPaddingRight();
            int var7 = this.getPaddingTop();
            int var8 = this.getPaddingBottom();
            this.setMeasuredDimension(resolveSize(var4 + var5 + var6, var1), resolveSize(var3 + var7 + var8, var2));
            return;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label104;
         }
      }

      Throwable var22 = var10000;
      throw var22;
   }

   public void onRestoreInstanceState(Parcelable var1) {
      ProgressBarICS.SavedState var2 = (ProgressBarICS.SavedState)var1;
      super.onRestoreInstanceState(var2.getSuperState());
      this.setProgress(var2.progress);
      this.setSecondaryProgress(var2.secondaryProgress);
   }

   public Parcelable onSaveInstanceState() {
      ProgressBarICS.SavedState var1 = new ProgressBarICS.SavedState(super.onSaveInstanceState());
      var1.progress = this.mProgress;
      var1.secondaryProgress = this.mSecondaryProgress;
      return var1;
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      this.updateDrawableBounds(var1, var2);
   }

   protected void onVisibilityChanged(View var1, int var2) {
      super.onVisibilityChanged(var1, var2);
      if (this.mIndeterminate) {
         if (var2 != 8 && var2 != 4) {
            this.startAnimation();
            return;
         }

         this.stopAnimation();
      }

   }

   public void postInvalidate() {
      if (!this.mNoInvalidate) {
         super.postInvalidate();
      }

   }

   public void setIndeterminate(boolean var1) {
      synchronized(this){}

      Throwable var10000;
      label214: {
         boolean var10001;
         try {
            if (this.mOnlyIndeterminate && this.mIndeterminate) {
               return;
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label214;
         }

         try {
            if (var1 == this.mIndeterminate) {
               return;
            }

            this.mIndeterminate = var1;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label214;
         }

         if (var1) {
            try {
               this.mCurrentDrawable = this.mIndeterminateDrawable;
               this.startAnimation();
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label214;
            }
         } else {
            try {
               this.mCurrentDrawable = this.mProgressDrawable;
               this.stopAnimation();
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label214;
            }
         }

         return;
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public void setIndeterminateDrawable(Drawable var1) {
      if (var1 != null) {
         var1.setCallback(this);
      }

      this.mIndeterminateDrawable = var1;
      if (this.mIndeterminate) {
         this.mCurrentDrawable = var1;
         this.postInvalidate();
      }

   }

   public void setInterpolator(Context var1, int var2) {
      this.setInterpolator(AnimationUtils.loadInterpolator(var1, var2));
   }

   public void setInterpolator(Interpolator var1) {
      this.mInterpolator = var1;
   }

   public void setMax(int var1) {
      synchronized(this){}
      int var2 = var1;
      if (var1 < 0) {
         var2 = 0;
      }

      try {
         if (var2 != this.mMax) {
            this.mMax = var2;
            this.postInvalidate();
            if (this.mProgress > var2) {
               this.mProgress = var2;
            }

            this.refreshProgress(16908301, this.mProgress, false);
         }
      } finally {
         ;
      }

   }

   public void setProgress(int var1) {
      synchronized(this){}

      try {
         this.setProgress(var1, false);
      } finally {
         ;
      }

   }

   void setProgress(int var1, boolean var2) {
      synchronized(this){}

      Throwable var10000;
      label178: {
         boolean var10001;
         boolean var4;
         try {
            var4 = this.mIndeterminate;
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label178;
         }

         if (var4) {
            return;
         }

         int var3 = var1;
         if (var1 < 0) {
            var3 = 0;
         }

         var1 = var3;

         try {
            if (var3 > this.mMax) {
               var1 = this.mMax;
            }
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label178;
         }

         label165:
         try {
            if (var1 != this.mProgress) {
               this.mProgress = var1;
               this.refreshProgress(16908301, this.mProgress, var2);
            }

            return;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label165;
         }
      }

      Throwable var5 = var10000;
      throw var5;
   }

   public void setProgressDrawable(Drawable var1) {
      boolean var2;
      if (this.mProgressDrawable != null && var1 != this.mProgressDrawable) {
         this.mProgressDrawable.setCallback((Callback)null);
         var2 = true;
      } else {
         var2 = false;
      }

      if (var1 != null) {
         var1.setCallback(this);
         int var3 = var1.getMinimumHeight();
         if (this.mMaxHeight < var3) {
            this.mMaxHeight = var3;
            this.requestLayout();
         }
      }

      this.mProgressDrawable = var1;
      if (!this.mIndeterminate) {
         this.mCurrentDrawable = var1;
         this.postInvalidate();
      }

      if (var2) {
         this.updateDrawableBounds(this.getWidth(), this.getHeight());
         this.updateDrawableState();
         this.doRefreshProgress(16908301, this.mProgress, false, false);
         this.doRefreshProgress(16908303, this.mSecondaryProgress, false, false);
      }

   }

   public void setSecondaryProgress(int var1) {
      synchronized(this){}

      Throwable var10000;
      label178: {
         boolean var10001;
         boolean var3;
         try {
            var3 = this.mIndeterminate;
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label178;
         }

         if (var3) {
            return;
         }

         int var2 = var1;
         if (var1 < 0) {
            var2 = 0;
         }

         var1 = var2;

         try {
            if (var2 > this.mMax) {
               var1 = this.mMax;
            }
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label178;
         }

         label165:
         try {
            if (var1 != this.mSecondaryProgress) {
               this.mSecondaryProgress = var1;
               this.refreshProgress(16908303, this.mSecondaryProgress, false);
            }

            return;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label165;
         }
      }

      Throwable var4 = var10000;
      throw var4;
   }

   public void setVisibility(int var1) {
      if (this.getVisibility() != var1) {
         super.setVisibility(var1);
         if (this.mIndeterminate) {
            if (var1 != 8 && var1 != 4) {
               this.startAnimation();
               return;
            }

            this.stopAnimation();
         }
      }

   }

   void startAnimation() {
      if (this.getVisibility() == 0) {
         if (this.mIndeterminateDrawable instanceof Animatable) {
            this.mShouldStartAnimationDrawable = true;
            this.mAnimation = null;
         } else {
            if (this.mInterpolator == null) {
               this.mInterpolator = new LinearInterpolator();
            }

            this.mTransformation = new Transformation();
            this.mAnimation = new AlphaAnimation(0.0F, 1.0F);
            this.mAnimation.setRepeatMode(this.mBehavior);
            this.mAnimation.setRepeatCount(-1);
            this.mAnimation.setDuration((long)this.mDuration);
            this.mAnimation.setInterpolator(this.mInterpolator);
            this.mAnimation.setStartTime(-1L);
         }

         this.postInvalidate();
      }
   }

   void stopAnimation() {
      this.mAnimation = null;
      this.mTransformation = null;
      if (this.mIndeterminateDrawable instanceof Animatable) {
         ((Animatable)this.mIndeterminateDrawable).stop();
         this.mShouldStartAnimationDrawable = false;
      }

      this.postInvalidate();
   }

   protected boolean verifyDrawable(Drawable var1) {
      return var1 == this.mProgressDrawable || var1 == this.mIndeterminateDrawable || super.verifyDrawable(var1);
   }

   private class RefreshProgressRunnable implements Runnable {
      private boolean mFromUser;
      private int mId;
      private int mProgress;

      RefreshProgressRunnable(int var2, int var3, boolean var4) {
         this.mId = var2;
         this.mProgress = var3;
         this.mFromUser = var4;
      }

      public void run() {
         ProgressBarICS.this.doRefreshProgress(this.mId, this.mProgress, this.mFromUser, true);
         ProgressBarICS.this.mRefreshProgressRunnable = this;
      }

      public void setup(int var1, int var2, boolean var3) {
         this.mId = var1;
         this.mProgress = var2;
         this.mFromUser = var3;
      }
   }

   static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public ProgressBarICS.SavedState createFromParcel(Parcel var1) {
            return new ProgressBarICS.SavedState(var1);
         }

         public ProgressBarICS.SavedState[] newArray(int var1) {
            return new ProgressBarICS.SavedState[var1];
         }
      };
      int progress;
      int secondaryProgress;

      private SavedState(Parcel var1) {
         super(var1);
         this.progress = var1.readInt();
         this.secondaryProgress = var1.readInt();
      }

      // $FF: synthetic method
      SavedState(Parcel var1, Object var2) {
         this(var1);
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.progress);
         var1.writeInt(this.secondaryProgress);
      }
   }
}
