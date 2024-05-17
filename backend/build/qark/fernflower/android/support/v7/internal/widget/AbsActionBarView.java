package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.v7.appcompat.R$anim;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$bool;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.internal.view.menu.ActionMenuPresenter;
import android.support.v7.internal.view.menu.ActionMenuView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

abstract class AbsActionBarView extends ViewGroup {
   private static final int FADE_DURATION = 200;
   protected ActionMenuPresenter mActionMenuPresenter;
   protected int mContentHeight;
   protected ActionMenuView mMenuView;
   protected boolean mSplitActionBar;
   protected ActionBarContainer mSplitView;
   protected boolean mSplitWhenNarrow;

   AbsActionBarView(Context var1) {
      super(var1);
   }

   AbsActionBarView(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   AbsActionBarView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   public void animateToVisibility(int var1) {
      this.clearAnimation();
      if (var1 != this.getVisibility()) {
         Context var3 = this.getContext();
         int var2;
         if (var1 == 0) {
            var2 = R$anim.abc_fade_in;
         } else {
            var2 = R$anim.abc_fade_out;
         }

         Animation var4 = AnimationUtils.loadAnimation(var3, var2);
         this.startAnimation(var4);
         this.setVisibility(var1);
         if (this.mSplitView != null && this.mMenuView != null) {
            this.mMenuView.startAnimation(var4);
            this.mMenuView.setVisibility(var1);
         }
      }

   }

   public void dismissPopupMenus() {
      if (this.mActionMenuPresenter != null) {
         this.mActionMenuPresenter.dismissPopupMenus();
      }

   }

   public int getAnimatedVisibility() {
      return this.getVisibility();
   }

   public int getContentHeight() {
      return this.mContentHeight;
   }

   public boolean hideOverflowMenu() {
      return this.mActionMenuPresenter != null ? this.mActionMenuPresenter.hideOverflowMenu() : false;
   }

   public boolean isOverflowMenuShowing() {
      return this.mActionMenuPresenter != null ? this.mActionMenuPresenter.isOverflowMenuShowing() : false;
   }

   public boolean isOverflowReserved() {
      return this.mActionMenuPresenter != null && this.mActionMenuPresenter.isOverflowReserved();
   }

   protected int measureChildView(View var1, int var2, int var3, int var4) {
      var1.measure(MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE), var3);
      return Math.max(0, var2 - var1.getMeasuredWidth() - var4);
   }

   protected void onConfigurationChanged(Configuration var1) {
      if (VERSION.SDK_INT >= 8) {
         super.onConfigurationChanged(var1);
      }

      TypedArray var2 = this.getContext().obtainStyledAttributes((AttributeSet)null, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
      this.setContentHeight(var2.getLayoutDimension(1, 0));
      var2.recycle();
      if (this.mSplitWhenNarrow) {
         this.setSplitActionBar(this.getContext().getResources().getBoolean(R$bool.abc_split_action_bar_is_narrow));
      }

      if (this.mActionMenuPresenter != null) {
         this.mActionMenuPresenter.onConfigurationChanged(var1);
      }

   }

   protected int positionChild(View var1, int var2, int var3, int var4) {
      int var5 = var1.getMeasuredWidth();
      int var6 = var1.getMeasuredHeight();
      var3 += (var4 - var6) / 2;
      var1.layout(var2, var3, var2 + var5, var3 + var6);
      return var5;
   }

   protected int positionChildInverse(View var1, int var2, int var3, int var4) {
      int var5 = var1.getMeasuredWidth();
      int var6 = var1.getMeasuredHeight();
      var3 += (var4 - var6) / 2;
      var1.layout(var2 - var5, var3, var2, var3 + var6);
      return var5;
   }

   public void postShowOverflowMenu() {
      this.post(new Runnable() {
         public void run() {
            AbsActionBarView.this.showOverflowMenu();
         }
      });
   }

   public void setContentHeight(int var1) {
      this.mContentHeight = var1;
      this.requestLayout();
   }

   public void setSplitActionBar(boolean var1) {
      this.mSplitActionBar = var1;
   }

   public void setSplitView(ActionBarContainer var1) {
      this.mSplitView = var1;
   }

   public void setSplitWhenNarrow(boolean var1) {
      this.mSplitWhenNarrow = var1;
   }

   public void setVisibility(int var1) {
      if (var1 != this.getVisibility()) {
         super.setVisibility(var1);
      }

   }

   public boolean showOverflowMenu() {
      return this.mActionMenuPresenter != null ? this.mActionMenuPresenter.showOverflowMenu() : false;
   }
}
