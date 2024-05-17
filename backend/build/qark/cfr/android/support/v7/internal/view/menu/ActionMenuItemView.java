/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.text.TextUtils
 *  android.text.method.TransformationMethod
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.View$OnLongClickListener
 *  android.widget.Toast
 *  java.lang.CharSequence
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Locale
 */
package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.menu.ActionMenuView;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.widget.CompatTextView;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;
import java.util.Locale;

public class ActionMenuItemView
extends CompatTextView
implements MenuView.ItemView,
View.OnClickListener,
View.OnLongClickListener,
ActionMenuView.ActionMenuChildView {
    private static final String TAG = "ActionMenuItemView";
    private boolean mAllowTextWithIcon;
    private boolean mExpandedFormat;
    private Drawable mIcon;
    private MenuItemImpl mItemData;
    private MenuBuilder.ItemInvoker mItemInvoker;
    private int mMinWidth;
    private int mSavedPaddingLeft;
    private CharSequence mTitle;

    public ActionMenuItemView(Context context) {
        this(context, null);
    }

    public ActionMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ActionMenuItemView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mAllowTextWithIcon = context.getResources().getBoolean(R.bool.abc_config_allowActionMenuItemTextWithIcon);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.ActionMenuItemView, 0, 0);
        this.mMinWidth = context.getDimensionPixelSize(0, 0);
        context.recycle();
        this.setOnClickListener((View.OnClickListener)this);
        this.setOnLongClickListener((View.OnLongClickListener)this);
        this.setTransformationMethod((TransformationMethod)new AllCapsTransformationMethod());
        this.mSavedPaddingLeft = -1;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateTextButtonVisibility() {
        boolean bl;
        boolean bl2;
        block3 : {
            block2 : {
                boolean bl3 = false;
                bl = !TextUtils.isEmpty((CharSequence)this.mTitle);
                if (this.mIcon == null) break block2;
                bl2 = bl3;
                if (!this.mItemData.showsTextAsAction()) break block3;
                if (this.mAllowTextWithIcon) break block2;
                bl2 = bl3;
                if (!this.mExpandedFormat) break block3;
            }
            bl2 = true;
        }
        CharSequence charSequence = bl & bl2 ? this.mTitle : null;
        this.setText(charSequence);
    }

    @Override
    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public boolean hasText() {
        if (!TextUtils.isEmpty((CharSequence)this.getText())) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void initialize(MenuItemImpl menuItemImpl, int n) {
        this.mItemData = menuItemImpl;
        this.setIcon(menuItemImpl.getIcon());
        this.setTitle(menuItemImpl.getTitleForItemView(this));
        this.setId(menuItemImpl.getItemId());
        n = menuItemImpl.isVisible() ? 0 : 8;
        this.setVisibility(n);
        this.setEnabled(menuItemImpl.isEnabled());
    }

    @Override
    public boolean needsDividerAfter() {
        return this.hasText();
    }

    @Override
    public boolean needsDividerBefore() {
        if (this.hasText() && this.mItemData.getIcon() == null) {
            return true;
        }
        return false;
    }

    public void onClick(View view) {
        if (this.mItemInvoker != null) {
            this.mItemInvoker.invokeItem(this.mItemData);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean onLongClick(View arrn) {
        if (this.hasText()) {
            return false;
        }
        arrn = new int[2];
        Rect rect = new Rect();
        this.getLocationOnScreen(arrn);
        this.getWindowVisibleDisplayFrame(rect);
        Context context = this.getContext();
        int n = this.getWidth();
        int n2 = this.getHeight();
        int n3 = arrn[1];
        int n4 = n2 / 2;
        int n5 = context.getResources().getDisplayMetrics().widthPixels;
        context = Toast.makeText((Context)context, (CharSequence)this.mItemData.getTitle(), (int)0);
        if (n3 + n4 < rect.height()) {
            context.setGravity(53, n5 - arrn[0] - n / 2, n2);
        } else {
            context.setGravity(81, 0, n2);
        }
        context.show();
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n, int n2) {
        boolean bl = this.hasText();
        if (bl && this.mSavedPaddingLeft >= 0) {
            super.setPadding(this.mSavedPaddingLeft, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
        }
        super.onMeasure(n, n2);
        int n3 = View.MeasureSpec.getMode((int)n);
        n = View.MeasureSpec.getSize((int)n);
        int n4 = this.getMeasuredWidth();
        n = n3 == Integer.MIN_VALUE ? Math.min((int)n, (int)this.mMinWidth) : this.mMinWidth;
        if (n3 != 1073741824 && this.mMinWidth > 0 && n4 < n) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec((int)n, (int)1073741824), n2);
        }
        if (!bl && this.mIcon != null) {
            super.setPadding((this.getMeasuredWidth() - this.mIcon.getIntrinsicWidth()) / 2, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
        }
    }

    @Override
    public boolean prefersCondensedTitle() {
        return true;
    }

    @Override
    public void setCheckable(boolean bl) {
    }

    @Override
    public void setChecked(boolean bl) {
    }

    public void setExpandedFormat(boolean bl) {
        if (this.mExpandedFormat != bl) {
            this.mExpandedFormat = bl;
            if (this.mItemData != null) {
                this.mItemData.actionFormatChanged();
            }
        }
    }

    @Override
    public void setIcon(Drawable drawable2) {
        this.mIcon = drawable2;
        this.setCompoundDrawablesWithIntrinsicBounds(drawable2, null, null, null);
        this.updateTextButtonVisibility();
    }

    public void setItemInvoker(MenuBuilder.ItemInvoker itemInvoker) {
        this.mItemInvoker = itemInvoker;
    }

    public void setPadding(int n, int n2, int n3, int n4) {
        this.mSavedPaddingLeft = n;
        super.setPadding(n, n2, n3, n4);
    }

    @Override
    public void setShortcut(boolean bl, char c) {
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        this.setContentDescription(this.mTitle);
        this.updateTextButtonVisibility();
    }

    @Override
    public boolean showsIcon() {
        return true;
    }

    private class AllCapsTransformationMethod
    implements TransformationMethod {
        private Locale mLocale;

        public AllCapsTransformationMethod() {
            this.mLocale = ActionMenuItemView.this.getContext().getResources().getConfiguration().locale;
        }

        public CharSequence getTransformation(CharSequence charSequence, View view) {
            if (charSequence != null) {
                return charSequence.toString().toUpperCase(this.mLocale);
            }
            return null;
        }

        public void onFocusChanged(View view, CharSequence charSequence, boolean bl, int n, Rect rect) {
        }
    }

}

