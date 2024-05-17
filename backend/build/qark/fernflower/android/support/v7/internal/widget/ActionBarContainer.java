package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Build.VERSION;
import android.support.v7.appcompat.R$id;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.view.ActionMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class ActionBarContainer extends FrameLayout {
   private ActionBarView mActionBarView;
   private Drawable mBackground;
   private boolean mIsSplit;
   private boolean mIsStacked;
   private boolean mIsTransitioning;
   private Drawable mSplitBackground;
   private Drawable mStackedBackground;
   private View mTabContainer;

   public ActionBarContainer(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ActionBarContainer(Context var1, AttributeSet var2) {
      boolean var3 = true;
      super(var1, var2);
      this.setBackgroundDrawable((Drawable)null);
      TypedArray var4 = var1.obtainStyledAttributes(var2, R$styleable.ActionBar);
      this.mBackground = var4.getDrawable(10);
      this.mStackedBackground = var4.getDrawable(11);
      if (this.getId() == R$id.split_action_bar) {
         this.mIsSplit = true;
         this.mSplitBackground = var4.getDrawable(12);
      }

      var4.recycle();
      if (this.mIsSplit) {
         if (this.mSplitBackground != null) {
            var3 = false;
         }
      } else if (this.mBackground != null || this.mStackedBackground != null) {
         var3 = false;
      }

      this.setWillNotDraw(var3);
   }

   private void drawBackgroundDrawable(Drawable var1, Canvas var2) {
      Rect var3 = var1.getBounds();
      if (var1 instanceof ColorDrawable && !var3.isEmpty() && VERSION.SDK_INT < 11) {
         var2.save();
         var2.clipRect(var3);
         var1.draw(var2);
         var2.restore();
      } else {
         var1.draw(var2);
      }
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      if (this.mBackground != null && this.mBackground.isStateful()) {
         this.mBackground.setState(this.getDrawableState());
      }

      if (this.mStackedBackground != null && this.mStackedBackground.isStateful()) {
         this.mStackedBackground.setState(this.getDrawableState());
      }

      if (this.mSplitBackground != null && this.mSplitBackground.isStateful()) {
         this.mSplitBackground.setState(this.getDrawableState());
      }

   }

   public View getTabContainer() {
      return this.mTabContainer;
   }

   public void onDraw(Canvas var1) {
      if (this.getWidth() != 0 && this.getHeight() != 0) {
         if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
               this.drawBackgroundDrawable(this.mSplitBackground, var1);
               return;
            }
         } else {
            if (this.mBackground != null) {
               this.drawBackgroundDrawable(this.mBackground, var1);
            }

            if (this.mStackedBackground != null && this.mIsStacked) {
               this.drawBackgroundDrawable(this.mStackedBackground, var1);
               return;
            }
         }
      }

   }

   public void onFinishInflate() {
      super.onFinishInflate();
      this.mActionBarView = (ActionBarView)this.findViewById(R$id.action_bar);
   }

   public boolean onHoverEvent(MotionEvent var1) {
      return true;
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      return this.mIsTransitioning || super.onInterceptTouchEvent(var1);
   }

   public void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      boolean var10;
      if (this.mTabContainer != null && this.mTabContainer.getVisibility() != 8) {
         var10 = true;
      } else {
         var10 = false;
      }

      if (this.mTabContainer != null && this.mTabContainer.getVisibility() != 8) {
         var5 = this.getMeasuredHeight();
         int var6 = this.mTabContainer.getMeasuredHeight();
         if ((this.mActionBarView.getDisplayOptions() & 2) == 0) {
            int var7 = this.getChildCount();

            for(var5 = 0; var5 < var7; ++var5) {
               View var8 = this.getChildAt(var5);
               if (var8 != this.mTabContainer && !this.mActionBarView.isCollapsed()) {
                  var8.offsetTopAndBottom(var6);
               }
            }

            this.mTabContainer.layout(var2, 0, var4, var6);
         } else {
            this.mTabContainer.layout(var2, var5 - var6, var4, var5);
         }
      }

      boolean var11 = false;
      boolean var9 = false;
      if (this.mIsSplit) {
         if (this.mSplitBackground != null) {
            this.mSplitBackground.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
            var9 = true;
         }
      } else {
         var9 = var11;
         if (this.mBackground != null) {
            this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
            var9 = true;
         }

         if (var10 && this.mStackedBackground != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         this.mIsStacked = var1;
         if (var1) {
            this.mStackedBackground.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
            var9 = true;
         }
      }

      if (var9) {
         this.invalidate();
      }

   }

   public void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      if (this.mActionBarView != null) {
         LayoutParams var3 = (LayoutParams)this.mActionBarView.getLayoutParams();
         if (this.mActionBarView.isCollapsed()) {
            var1 = 0;
         } else {
            var1 = this.mActionBarView.getMeasuredHeight() + var3.topMargin + var3.bottomMargin;
         }

         if (this.mTabContainer != null && this.mTabContainer.getVisibility() != 8 && MeasureSpec.getMode(var2) == Integer.MIN_VALUE) {
            var2 = MeasureSpec.getSize(var2);
            this.setMeasuredDimension(this.getMeasuredWidth(), Math.min(this.mTabContainer.getMeasuredHeight() + var1, var2));
            return;
         }
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      super.onTouchEvent(var1);
      return true;
   }

   public void setPrimaryBackground(Drawable var1) {
      boolean var2 = true;
      if (this.mBackground != null) {
         this.mBackground.setCallback((Callback)null);
         this.unscheduleDrawable(this.mBackground);
      }

      this.mBackground = var1;
      if (var1 != null) {
         var1.setCallback(this);
         if (this.mActionBarView != null) {
            this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
         }
      }

      if (this.mIsSplit) {
         if (this.mSplitBackground != null) {
            var2 = false;
         }
      } else if (this.mBackground != null || this.mStackedBackground != null) {
         var2 = false;
      }

      this.setWillNotDraw(var2);
      this.invalidate();
   }

   public void setSplitBackground(Drawable var1) {
      boolean var2 = true;
      if (this.mSplitBackground != null) {
         this.mSplitBackground.setCallback((Callback)null);
         this.unscheduleDrawable(this.mSplitBackground);
      }

      this.mSplitBackground = var1;
      if (var1 != null) {
         var1.setCallback(this);
         if (this.mIsSplit && this.mSplitBackground != null) {
            this.mSplitBackground.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
         }
      }

      if (this.mIsSplit) {
         if (this.mSplitBackground != null) {
            var2 = false;
         }
      } else if (this.mBackground != null || this.mStackedBackground != null) {
         var2 = false;
      }

      this.setWillNotDraw(var2);
      this.invalidate();
   }

   public void setStackedBackground(Drawable var1) {
      boolean var2 = true;
      if (this.mStackedBackground != null) {
         this.mStackedBackground.setCallback((Callback)null);
         this.unscheduleDrawable(this.mStackedBackground);
      }

      this.mStackedBackground = var1;
      if (var1 != null) {
         var1.setCallback(this);
         if (this.mIsStacked && this.mStackedBackground != null) {
            this.mStackedBackground.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
         }
      }

      if (this.mIsSplit) {
         if (this.mSplitBackground != null) {
            var2 = false;
         }
      } else if (this.mBackground != null || this.mStackedBackground != null) {
         var2 = false;
      }

      this.setWillNotDraw(var2);
      this.invalidate();
   }

   public void setTabContainer(ScrollingTabContainerView var1) {
      if (this.mTabContainer != null) {
         this.removeView(this.mTabContainer);
      }

      this.mTabContainer = var1;
      if (var1 != null) {
         this.addView(var1);
         android.view.ViewGroup.LayoutParams var2 = var1.getLayoutParams();
         var2.width = -1;
         var2.height = -2;
         var1.setAllowCollapse(false);
      }

   }

   public void setTransitioning(boolean var1) {
      this.mIsTransitioning = var1;
      int var2;
      if (var1) {
         var2 = 393216;
      } else {
         var2 = 262144;
      }

      this.setDescendantFocusability(var2);
   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      boolean var2;
      if (var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (this.mBackground != null) {
         this.mBackground.setVisible(var2, false);
      }

      if (this.mStackedBackground != null) {
         this.mStackedBackground.setVisible(var2, false);
      }

      if (this.mSplitBackground != null) {
         this.mSplitBackground.setVisible(var2, false);
      }

   }

   public ActionMode startActionModeForChild(View var1, ActionMode.Callback var2) {
      return null;
   }

   protected boolean verifyDrawable(Drawable var1) {
      return var1 == this.mBackground && !this.mIsSplit || var1 == this.mStackedBackground && this.mIsStacked || var1 == this.mSplitBackground && this.mIsSplit || super.verifyDrawable(var1);
   }
}
