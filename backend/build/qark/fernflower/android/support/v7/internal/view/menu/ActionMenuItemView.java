package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R$bool;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.internal.widget.CompatTextView;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Toast;
import java.util.Locale;

public class ActionMenuItemView extends CompatTextView implements MenuView.ItemView, OnClickListener, OnLongClickListener, ActionMenuView.ActionMenuChildView {
   private static final String TAG = "ActionMenuItemView";
   private boolean mAllowTextWithIcon;
   private boolean mExpandedFormat;
   private Drawable mIcon;
   private MenuItemImpl mItemData;
   private MenuBuilder.ItemInvoker mItemInvoker;
   private int mMinWidth;
   private int mSavedPaddingLeft;
   private CharSequence mTitle;

   public ActionMenuItemView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ActionMenuItemView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ActionMenuItemView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mAllowTextWithIcon = var1.getResources().getBoolean(R$bool.abc_config_allowActionMenuItemTextWithIcon);
      TypedArray var4 = var1.obtainStyledAttributes(var2, R$styleable.ActionMenuItemView, 0, 0);
      this.mMinWidth = var4.getDimensionPixelSize(0, 0);
      var4.recycle();
      this.setOnClickListener(this);
      this.setOnLongClickListener(this);
      this.setTransformationMethod(new ActionMenuItemView.AllCapsTransformationMethod());
      this.mSavedPaddingLeft = -1;
   }

   private void updateTextButtonVisibility() {
      boolean var3 = false;
      boolean var1;
      if (!TextUtils.isEmpty(this.mTitle)) {
         var1 = true;
      } else {
         var1 = false;
      }

      boolean var2;
      label25: {
         if (this.mIcon != null) {
            var2 = var3;
            if (!this.mItemData.showsTextAsAction()) {
               break label25;
            }

            if (!this.mAllowTextWithIcon) {
               var2 = var3;
               if (!this.mExpandedFormat) {
                  break label25;
               }
            }
         }

         var2 = true;
      }

      CharSequence var4;
      if (var1 & var2) {
         var4 = this.mTitle;
      } else {
         var4 = null;
      }

      this.setText(var4);
   }

   public MenuItemImpl getItemData() {
      return this.mItemData;
   }

   public boolean hasText() {
      return !TextUtils.isEmpty(this.getText());
   }

   public void initialize(MenuItemImpl var1, int var2) {
      this.mItemData = var1;
      this.setIcon(var1.getIcon());
      this.setTitle(var1.getTitleForItemView(this));
      this.setId(var1.getItemId());
      byte var3;
      if (var1.isVisible()) {
         var3 = 0;
      } else {
         var3 = 8;
      }

      this.setVisibility(var3);
      this.setEnabled(var1.isEnabled());
   }

   public boolean needsDividerAfter() {
      return this.hasText();
   }

   public boolean needsDividerBefore() {
      return this.hasText() && this.mItemData.getIcon() == null;
   }

   public void onClick(View var1) {
      if (this.mItemInvoker != null) {
         this.mItemInvoker.invokeItem(this.mItemData);
      }

   }

   public boolean onLongClick(View var1) {
      if (this.hasText()) {
         return false;
      } else {
         int[] var9 = new int[2];
         Rect var7 = new Rect();
         this.getLocationOnScreen(var9);
         this.getWindowVisibleDisplayFrame(var7);
         Context var8 = this.getContext();
         int var2 = this.getWidth();
         int var3 = this.getHeight();
         int var4 = var9[1];
         int var5 = var3 / 2;
         int var6 = var8.getResources().getDisplayMetrics().widthPixels;
         Toast var10 = Toast.makeText(var8, this.mItemData.getTitle(), 0);
         if (var4 + var5 < var7.height()) {
            var10.setGravity(53, var6 - var9[0] - var2 / 2, var3);
         } else {
            var10.setGravity(81, 0, var3);
         }

         var10.show();
         return true;
      }
   }

   protected void onMeasure(int var1, int var2) {
      boolean var5 = this.hasText();
      if (var5 && this.mSavedPaddingLeft >= 0) {
         super.setPadding(this.mSavedPaddingLeft, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
      }

      super.onMeasure(var1, var2);
      int var3 = MeasureSpec.getMode(var1);
      var1 = MeasureSpec.getSize(var1);
      int var4 = this.getMeasuredWidth();
      if (var3 == Integer.MIN_VALUE) {
         var1 = Math.min(var1, this.mMinWidth);
      } else {
         var1 = this.mMinWidth;
      }

      if (var3 != 1073741824 && this.mMinWidth > 0 && var4 < var1) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(var1, 1073741824), var2);
      }

      if (!var5 && this.mIcon != null) {
         super.setPadding((this.getMeasuredWidth() - this.mIcon.getIntrinsicWidth()) / 2, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
      }

   }

   public boolean prefersCondensedTitle() {
      return true;
   }

   public void setCheckable(boolean var1) {
   }

   public void setChecked(boolean var1) {
   }

   public void setExpandedFormat(boolean var1) {
      if (this.mExpandedFormat != var1) {
         this.mExpandedFormat = var1;
         if (this.mItemData != null) {
            this.mItemData.actionFormatChanged();
         }
      }

   }

   public void setIcon(Drawable var1) {
      this.mIcon = var1;
      this.setCompoundDrawablesWithIntrinsicBounds(var1, (Drawable)null, (Drawable)null, (Drawable)null);
      this.updateTextButtonVisibility();
   }

   public void setItemInvoker(MenuBuilder.ItemInvoker var1) {
      this.mItemInvoker = var1;
   }

   public void setPadding(int var1, int var2, int var3, int var4) {
      this.mSavedPaddingLeft = var1;
      super.setPadding(var1, var2, var3, var4);
   }

   public void setShortcut(boolean var1, char var2) {
   }

   public void setTitle(CharSequence var1) {
      this.mTitle = var1;
      this.setContentDescription(this.mTitle);
      this.updateTextButtonVisibility();
   }

   public boolean showsIcon() {
      return true;
   }

   private class AllCapsTransformationMethod implements TransformationMethod {
      private Locale mLocale;

      public AllCapsTransformationMethod() {
         this.mLocale = ActionMenuItemView.this.getContext().getResources().getConfiguration().locale;
      }

      public CharSequence getTransformation(CharSequence var1, View var2) {
         return var1 != null ? var1.toString().toUpperCase(this.mLocale) : null;
      }

      public void onFocusChanged(View var1, CharSequence var2, boolean var3, int var4, Rect var5) {
      }
   }
}
