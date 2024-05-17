package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$id;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class ActionBarOverlayLayout extends FrameLayout {
   static final int[] mActionBarSizeAttr;
   private ActionBar mActionBar;
   private View mActionBarBottom;
   private int mActionBarHeight;
   private View mActionBarTop;
   private ActionBarView mActionView;
   private ActionBarContainer mContainerView;
   private View mContent;
   private final Rect mZeroRect = new Rect(0, 0, 0, 0);

   static {
      mActionBarSizeAttr = new int[]{R$attr.actionBarSize};
   }

   public ActionBarOverlayLayout(Context var1) {
      super(var1);
      this.init(var1);
   }

   public ActionBarOverlayLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init(var1);
   }

   private boolean applyInsets(View var1, Rect var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      boolean var8 = false;
      LayoutParams var9 = (LayoutParams)var1.getLayoutParams();
      boolean var7 = var8;
      if (var3) {
         var7 = var8;
         if (var9.leftMargin != var2.left) {
            var7 = true;
            var9.leftMargin = var2.left;
         }
      }

      var3 = var7;
      if (var4) {
         var3 = var7;
         if (var9.topMargin != var2.top) {
            var3 = true;
            var9.topMargin = var2.top;
         }
      }

      var4 = var3;
      if (var6) {
         var4 = var3;
         if (var9.rightMargin != var2.right) {
            var4 = true;
            var9.rightMargin = var2.right;
         }
      }

      var3 = var4;
      if (var5) {
         var3 = var4;
         if (var9.bottomMargin != var2.bottom) {
            var3 = true;
            var9.bottomMargin = var2.bottom;
         }
      }

      return var3;
   }

   private void init(Context var1) {
      TypedArray var2 = this.getContext().getTheme().obtainStyledAttributes(mActionBarSizeAttr);
      this.mActionBarHeight = var2.getDimensionPixelSize(0, 0);
      var2.recycle();
   }

   void pullChildren() {
      if (this.mContent == null) {
         this.mContent = this.findViewById(R$id.action_bar_activity_content);
         if (this.mContent == null) {
            this.mContent = this.findViewById(16908290);
         }

         this.mActionBarTop = this.findViewById(R$id.top_action_bar);
         this.mContainerView = (ActionBarContainer)this.findViewById(R$id.action_bar_container);
         this.mActionView = (ActionBarView)this.findViewById(R$id.action_bar);
         this.mActionBarBottom = this.findViewById(R$id.split_action_bar);
      }

   }

   public void setActionBar(ActionBar var1) {
      this.mActionBar = var1;
   }
}
