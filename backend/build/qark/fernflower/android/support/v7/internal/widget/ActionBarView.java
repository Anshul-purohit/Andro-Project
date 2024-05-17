package android.support.v7.internal.widget;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$bool;
import android.support.v7.appcompat.R$id;
import android.support.v7.appcompat.R$layout;
import android.support.v7.appcompat.R$string;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.internal.view.menu.ActionMenuItem;
import android.support.v7.internal.view.menu.ActionMenuPresenter;
import android.support.v7.internal.view.menu.ActionMenuView;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.support.v7.view.CollapsibleActionView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window.Callback;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ActionBarView extends AbsActionBarView {
   private static final int DEFAULT_CUSTOM_GRAVITY = 19;
   public static final int DISPLAY_DEFAULT = 0;
   private static final int DISPLAY_RELAYOUT_MASK = 31;
   private static final String TAG = "ActionBarView";
   private ActionBar.OnNavigationListener mCallback;
   private Context mContext;
   private ActionBarContextView mContextView;
   private View mCustomNavView;
   private int mDisplayOptions = -1;
   View mExpandedActionView;
   private final OnClickListener mExpandedActionViewUpListener = new OnClickListener() {
      public void onClick(View var1) {
         MenuItemImpl var2 = ActionBarView.this.mExpandedMenuPresenter.mCurrentExpandedItem;
         if (var2 != null) {
            var2.collapseActionView();
         }

      }
   };
   private ActionBarView.HomeView mExpandedHomeLayout;
   private ActionBarView.ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
   private ActionBarView.HomeView mHomeLayout;
   private Drawable mIcon;
   private boolean mIncludeTabs;
   private int mIndeterminateProgressStyle;
   private ProgressBarICS mIndeterminateProgressView;
   private boolean mIsCollapsable;
   private boolean mIsCollapsed;
   private int mItemPadding;
   private LinearLayout mListNavLayout;
   private Drawable mLogo;
   private ActionMenuItem mLogoNavItem;
   private final AdapterViewICS.OnItemSelectedListener mNavItemSelectedListener = new AdapterViewICS.OnItemSelectedListener() {
      public void onItemSelected(AdapterViewICS var1, View var2, int var3, long var4) {
         if (ActionBarView.this.mCallback != null) {
            ActionBarView.this.mCallback.onNavigationItemSelected(var3, var4);
         }

      }

      public void onNothingSelected(AdapterViewICS var1) {
      }
   };
   private int mNavigationMode;
   private MenuBuilder mOptionsMenu;
   private int mProgressBarPadding;
   private int mProgressStyle;
   private ProgressBarICS mProgressView;
   private SpinnerICS mSpinner;
   private SpinnerAdapter mSpinnerAdapter;
   private CharSequence mSubtitle;
   private int mSubtitleStyleRes;
   private TextView mSubtitleView;
   private ScrollingTabContainerView mTabScrollView;
   private Runnable mTabSelector;
   private CharSequence mTitle;
   private LinearLayout mTitleLayout;
   private int mTitleStyleRes;
   private View mTitleUpView;
   private TextView mTitleView;
   private final OnClickListener mUpClickListener = new OnClickListener() {
      public void onClick(View var1) {
         ActionBarView.this.mWindowCallback.onMenuItemSelected(0, ActionBarView.this.mLogoNavItem);
      }
   };
   private boolean mUserTitle;
   Callback mWindowCallback;

   public ActionBarView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mContext = var1;
      this.setBackgroundResource(0);
      TypedArray var9 = var1.obtainStyledAttributes(var2, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
      ApplicationInfo var4 = var1.getApplicationInfo();
      PackageManager var5 = var1.getPackageManager();
      this.mNavigationMode = var9.getInt(2, 0);
      this.mTitle = var9.getText(0);
      this.mSubtitle = var9.getText(4);
      this.mLogo = var9.getDrawable(8);
      if (this.mLogo == null && VERSION.SDK_INT >= 9) {
         if (var1 instanceof Activity) {
            try {
               this.mLogo = var5.getActivityLogo(((Activity)var1).getComponentName());
            } catch (NameNotFoundException var8) {
               Log.e("ActionBarView", "Activity component name not found!", var8);
            }
         }

         if (this.mLogo == null) {
            this.mLogo = var4.loadLogo(var5);
         }
      }

      this.mIcon = var9.getDrawable(7);
      if (this.mIcon == null) {
         if (var1 instanceof Activity) {
            try {
               this.mIcon = var5.getActivityIcon(((Activity)var1).getComponentName());
            } catch (NameNotFoundException var7) {
               Log.e("ActionBarView", "Activity component name not found!", var7);
            }
         }

         if (this.mIcon == null) {
            this.mIcon = var4.loadIcon(var5);
         }
      }

      LayoutInflater var10 = LayoutInflater.from(var1);
      int var3 = var9.getResourceId(14, R$layout.abc_action_bar_home);
      this.mHomeLayout = (ActionBarView.HomeView)var10.inflate(var3, this, false);
      this.mExpandedHomeLayout = (ActionBarView.HomeView)var10.inflate(var3, this, false);
      this.mExpandedHomeLayout.setUp(true);
      this.mExpandedHomeLayout.setOnClickListener(this.mExpandedActionViewUpListener);
      this.mExpandedHomeLayout.setContentDescription(this.getResources().getText(R$string.abc_action_bar_up_description));
      this.mTitleStyleRes = var9.getResourceId(5, 0);
      this.mSubtitleStyleRes = var9.getResourceId(6, 0);
      this.mProgressStyle = var9.getResourceId(15, 0);
      this.mIndeterminateProgressStyle = var9.getResourceId(16, 0);
      this.mProgressBarPadding = var9.getDimensionPixelOffset(17, 0);
      this.mItemPadding = var9.getDimensionPixelOffset(18, 0);
      this.setDisplayOptions(var9.getInt(3, 0));
      var3 = var9.getResourceId(13, 0);
      if (var3 != 0) {
         this.mCustomNavView = var10.inflate(var3, this, false);
         this.mNavigationMode = 0;
         this.setDisplayOptions(this.mDisplayOptions | 16);
      }

      this.mContentHeight = var9.getLayoutDimension(1, 0);
      var9.recycle();
      this.mLogoNavItem = new ActionMenuItem(var1, 0, 16908332, 0, 0, this.mTitle);
      this.mHomeLayout.setOnClickListener(this.mUpClickListener);
      this.mHomeLayout.setClickable(true);
      this.mHomeLayout.setFocusable(true);
   }

   private void configPresenters(MenuBuilder var1) {
      if (var1 != null) {
         var1.addMenuPresenter(this.mActionMenuPresenter);
         var1.addMenuPresenter(this.mExpandedMenuPresenter);
      } else {
         this.mActionMenuPresenter.initForMenu(this.mContext, (MenuBuilder)null);
         this.mExpandedMenuPresenter.initForMenu(this.mContext, (MenuBuilder)null);
      }

      this.mActionMenuPresenter.updateMenuView(true);
      this.mExpandedMenuPresenter.updateMenuView(true);
   }

   private void initTitle() {
      boolean var4 = true;
      if (this.mTitleLayout == null) {
         this.mTitleLayout = (LinearLayout)LayoutInflater.from(this.getContext()).inflate(R$layout.abc_action_bar_title_item, this, false);
         this.mTitleView = (TextView)this.mTitleLayout.findViewById(R$id.action_bar_title);
         this.mSubtitleView = (TextView)this.mTitleLayout.findViewById(R$id.action_bar_subtitle);
         this.mTitleUpView = this.mTitleLayout.findViewById(R$id.field_4);
         this.mTitleLayout.setOnClickListener(this.mUpClickListener);
         if (this.mTitleStyleRes != 0) {
            this.mTitleView.setTextAppearance(this.mContext, this.mTitleStyleRes);
         }

         if (this.mTitle != null) {
            this.mTitleView.setText(this.mTitle);
         }

         if (this.mSubtitleStyleRes != 0) {
            this.mSubtitleView.setTextAppearance(this.mContext, this.mSubtitleStyleRes);
         }

         if (this.mSubtitle != null) {
            this.mSubtitleView.setText(this.mSubtitle);
            this.mSubtitleView.setVisibility(0);
         }

         boolean var2;
         if ((this.mDisplayOptions & 4) != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         boolean var3;
         if ((this.mDisplayOptions & 2) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         View var5 = this.mTitleUpView;
         byte var1;
         if (!var3) {
            if (var2) {
               var1 = 0;
            } else {
               var1 = 4;
            }
         } else {
            var1 = 8;
         }

         var5.setVisibility(var1);
         LinearLayout var6 = this.mTitleLayout;
         if (!var2 || var3) {
            var4 = false;
         }

         var6.setEnabled(var4);
      }

      this.addView(this.mTitleLayout);
      if (this.mExpandedActionView != null || TextUtils.isEmpty(this.mTitle) && TextUtils.isEmpty(this.mSubtitle)) {
         this.mTitleLayout.setVisibility(8);
      }

   }

   private void setTitleImpl(CharSequence var1) {
      byte var3 = 0;
      this.mTitle = var1;
      if (this.mTitleView != null) {
         this.mTitleView.setText(var1);
         boolean var2;
         if (this.mExpandedActionView != null || (this.mDisplayOptions & 8) == 0 || TextUtils.isEmpty(this.mTitle) && TextUtils.isEmpty(this.mSubtitle)) {
            var2 = false;
         } else {
            var2 = true;
         }

         LinearLayout var4 = this.mTitleLayout;
         byte var5;
         if (var2) {
            var5 = var3;
         } else {
            var5 = 8;
         }

         var4.setVisibility(var5);
      }

      if (this.mLogoNavItem != null) {
         this.mLogoNavItem.setTitle(var1);
      }

   }

   public void collapseActionView() {
      MenuItemImpl var1;
      if (this.mExpandedMenuPresenter == null) {
         var1 = null;
      } else {
         var1 = this.mExpandedMenuPresenter.mCurrentExpandedItem;
      }

      if (var1 != null) {
         var1.collapseActionView();
      }

   }

   protected LayoutParams generateDefaultLayoutParams() {
      return new ActionBar.LayoutParams(19);
   }

   public LayoutParams generateLayoutParams(AttributeSet var1) {
      return new ActionBar.LayoutParams(this.getContext(), var1);
   }

   public LayoutParams generateLayoutParams(LayoutParams var1) {
      LayoutParams var2 = var1;
      if (var1 == null) {
         var2 = this.generateDefaultLayoutParams();
      }

      return var2;
   }

   public View getCustomNavigationView() {
      return this.mCustomNavView;
   }

   public int getDisplayOptions() {
      return this.mDisplayOptions;
   }

   public SpinnerAdapter getDropdownAdapter() {
      return this.mSpinnerAdapter;
   }

   public int getDropdownSelectedPosition() {
      return this.mSpinner.getSelectedItemPosition();
   }

   public int getNavigationMode() {
      return this.mNavigationMode;
   }

   public CharSequence getSubtitle() {
      return this.mSubtitle;
   }

   public CharSequence getTitle() {
      return this.mTitle;
   }

   public boolean hasEmbeddedTabs() {
      return this.mIncludeTabs;
   }

   public boolean hasExpandedActionView() {
      return this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null;
   }

   public void initIndeterminateProgress() {
      this.mIndeterminateProgressView = new ProgressBarICS(this.mContext, (AttributeSet)null, 0, this.mIndeterminateProgressStyle);
      this.mIndeterminateProgressView.setId(R$id.progress_circular);
      this.mIndeterminateProgressView.setVisibility(8);
      this.addView(this.mIndeterminateProgressView);
   }

   public void initProgress() {
      this.mProgressView = new ProgressBarICS(this.mContext, (AttributeSet)null, 0, this.mProgressStyle);
      this.mProgressView.setId(R$id.progress_horizontal);
      this.mProgressView.setMax(10000);
      this.mProgressView.setVisibility(8);
      this.addView(this.mProgressView);
   }

   public boolean isCollapsed() {
      return this.mIsCollapsed;
   }

   public boolean isSplitActionBar() {
      return this.mSplitActionBar;
   }

   protected void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.mTitleView = null;
      this.mSubtitleView = null;
      this.mTitleUpView = null;
      if (this.mTitleLayout != null && this.mTitleLayout.getParent() == this) {
         this.removeView(this.mTitleLayout);
      }

      this.mTitleLayout = null;
      if ((this.mDisplayOptions & 8) != 0) {
         this.initTitle();
      }

      if (this.mTabScrollView != null && this.mIncludeTabs) {
         LayoutParams var2 = this.mTabScrollView.getLayoutParams();
         if (var2 != null) {
            var2.width = -2;
            var2.height = -1;
         }

         this.mTabScrollView.setAllowCollapse(true);
      }

      if (this.mProgressView != null) {
         this.removeView(this.mProgressView);
         this.initProgress();
      }

      if (this.mIndeterminateProgressView != null) {
         this.removeView(this.mIndeterminateProgressView);
         this.initIndeterminateProgress();
      }

   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.removeCallbacks(this.mTabSelector);
      if (this.mActionMenuPresenter != null) {
         this.mActionMenuPresenter.hideOverflowMenu();
         this.mActionMenuPresenter.hideSubMenus();
      }

   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      this.addView(this.mHomeLayout);
      if (this.mCustomNavView != null && (this.mDisplayOptions & 16) != 0) {
         ViewParent var1 = this.mCustomNavView.getParent();
         if (var1 != this) {
            if (var1 instanceof ViewGroup) {
               ((ViewGroup)var1).removeView(this.mCustomNavView);
            }

            this.addView(this.mCustomNavView);
         }
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var7 = this.getPaddingLeft();
      int var8 = this.getPaddingTop();
      int var9 = var5 - var3 - this.getPaddingTop() - this.getPaddingBottom();
      if (var9 > 0) {
         ActionBarView.HomeView var11;
         if (this.mExpandedActionView != null) {
            var11 = this.mExpandedHomeLayout;
         } else {
            var11 = this.mHomeLayout;
         }

         int var6 = var7;
         if (var11.getVisibility() != 8) {
            var3 = var11.getLeftOffset();
            var6 = var7 + this.positionChild(var11, var7 + var3, var8, var9) + var3;
         }

         var3 = var6;
         if (this.mExpandedActionView == null) {
            boolean var14;
            if (this.mTitleLayout != null && this.mTitleLayout.getVisibility() != 8 && (this.mDisplayOptions & 8) != 0) {
               var14 = true;
            } else {
               var14 = false;
            }

            var5 = var6;
            if (var14) {
               var5 = var6 + this.positionChild(this.mTitleLayout, var6, var8, var9);
            }

            var3 = var5;
            switch(this.mNavigationMode) {
            case 0:
               break;
            case 1:
               var3 = var5;
               if (this.mListNavLayout != null) {
                  var3 = var5;
                  if (var14) {
                     var3 = var5 + this.mItemPadding;
                  }

                  var3 += this.positionChild(this.mListNavLayout, var3, var8, var9) + this.mItemPadding;
               }
               break;
            case 2:
               var3 = var5;
               if (this.mTabScrollView != null) {
                  var3 = var5;
                  if (var14) {
                     var3 = var5 + this.mItemPadding;
                  }

                  var3 += this.positionChild(this.mTabScrollView, var3, var8, var9) + this.mItemPadding;
               }
               break;
            default:
               var3 = var5;
            }
         }

         var2 = var4 - var2 - this.getPaddingRight();
         var4 = var2;
         if (this.mMenuView != null) {
            var4 = var2;
            if (this.mMenuView.getParent() == this) {
               this.positionChildInverse(this.mMenuView, var2, var8, var9);
               var4 = var2 - this.mMenuView.getMeasuredWidth();
            }
         }

         var2 = var4;
         if (this.mIndeterminateProgressView != null) {
            var2 = var4;
            if (this.mIndeterminateProgressView.getVisibility() != 8) {
               this.positionChildInverse(this.mIndeterminateProgressView, var4, var8, var9);
               var2 = var4 - this.mIndeterminateProgressView.getMeasuredWidth();
            }
         }

         LayoutParams var12 = null;
         View var16;
         if (this.mExpandedActionView != null) {
            var16 = this.mExpandedActionView;
         } else {
            var16 = var12;
            if ((this.mDisplayOptions & 16) != 0) {
               var16 = var12;
               if (this.mCustomNavView != null) {
                  var16 = this.mCustomNavView;
               }
            }
         }

         if (var16 != null) {
            var12 = var16.getLayoutParams();
            ActionBar.LayoutParams var17;
            if (var12 instanceof ActionBar.LayoutParams) {
               var17 = (ActionBar.LayoutParams)var12;
            } else {
               var17 = null;
            }

            if (var17 != null) {
               var5 = var17.gravity;
            } else {
               var5 = 19;
            }

            int var10 = var16.getMeasuredWidth();
            var6 = 0;
            var7 = 0;
            var8 = var2;
            var4 = var3;
            if (var17 != null) {
               var4 = var3 + var17.leftMargin;
               var8 = var2 - var17.rightMargin;
               var6 = var17.topMargin;
               var7 = var17.bottomMargin;
            }

            var3 = var5 & 7;
            if (var3 == 1) {
               var2 = (this.getWidth() - var10) / 2;
               if (var2 < var4) {
                  var3 = 3;
               } else if (var2 + var10 > var8) {
                  var3 = 5;
               }
            } else if (var5 == -1) {
               var3 = 3;
            }

            byte var15 = 0;
            var2 = var15;
            switch(var3) {
            case 1:
               var2 = (this.getWidth() - var10) / 2;
            case 2:
            case 4:
               break;
            case 3:
               var2 = var4;
               break;
            case 5:
               var2 = var8 - var10;
               break;
            default:
               var2 = var15;
            }

            var3 = var5 & 112;
            if (var5 == -1) {
               var3 = 16;
            }

            byte var13 = 0;
            switch(var3) {
            case 16:
               var3 = this.getPaddingTop();
               var3 = (this.getHeight() - this.getPaddingBottom() - var3 - var16.getMeasuredHeight()) / 2;
               break;
            case 48:
               var3 = this.getPaddingTop() + var6;
               break;
            case 80:
               var3 = this.getHeight() - this.getPaddingBottom() - var16.getMeasuredHeight() - var7;
               break;
            default:
               var3 = var13;
            }

            var4 = var16.getMeasuredWidth();
            var16.layout(var2, var3, var2 + var4, var16.getMeasuredHeight() + var3);
         }

         if (this.mProgressView != null) {
            this.mProgressView.bringToFront();
            var2 = this.mProgressView.getMeasuredHeight() / 2;
            this.mProgressView.layout(this.mProgressBarPadding, -var2, this.mProgressBarPadding + this.mProgressView.getMeasuredWidth(), var2);
            return;
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      int var12 = this.getChildCount();
      int var3;
      int var4;
      int var5;
      View var16;
      if (this.mIsCollapsable) {
         var4 = 0;

         for(var3 = 0; var3 < var12; var4 = var5) {
            var16 = this.getChildAt(var3);
            var5 = var4;
            if (var16.getVisibility() != 8) {
               label203: {
                  if (var16 == this.mMenuView) {
                     var5 = var4;
                     if (this.mMenuView.getChildCount() == 0) {
                        break label203;
                     }
                  }

                  var5 = var4 + 1;
               }
            }

            ++var3;
         }

         if (var4 == 0) {
            this.setMeasuredDimension(0, 0);
            this.mIsCollapsed = true;
            return;
         }
      }

      this.mIsCollapsed = false;
      if (MeasureSpec.getMode(var1) != 1073741824) {
         throw new IllegalStateException(this.getClass().getSimpleName() + " can only be used " + "with android:layout_width=\"MATCH_PARENT\" (or fill_parent)");
      } else if (MeasureSpec.getMode(var2) != Integer.MIN_VALUE) {
         throw new IllegalStateException(this.getClass().getSimpleName() + " can only be used " + "with android:layout_height=\"wrap_content\"");
      } else {
         int var13 = MeasureSpec.getSize(var1);
         int var6;
         if (this.mContentHeight > 0) {
            var6 = this.mContentHeight;
         } else {
            var6 = MeasureSpec.getSize(var2);
         }

         int var14 = this.getPaddingTop() + this.getPaddingBottom();
         var1 = this.getPaddingLeft();
         var2 = this.getPaddingRight();
         int var10 = var6 - var14;
         int var8 = MeasureSpec.makeMeasureSpec(var10, Integer.MIN_VALUE);
         var5 = var13 - var1 - var2;
         var3 = var5 / 2;
         ActionBarView.HomeView var20;
         if (this.mExpandedActionView != null) {
            var20 = this.mExpandedHomeLayout;
         } else {
            var20 = this.mHomeLayout;
         }

         var2 = var5;
         LayoutParams var17;
         if (var20.getVisibility() != 8) {
            var17 = var20.getLayoutParams();
            if (var17.width < 0) {
               var1 = MeasureSpec.makeMeasureSpec(var5, Integer.MIN_VALUE);
            } else {
               var1 = MeasureSpec.makeMeasureSpec(var17.width, 1073741824);
            }

            var20.measure(var1, MeasureSpec.makeMeasureSpec(var10, 1073741824));
            var1 = var20.getMeasuredWidth() + var20.getLeftOffset();
            var2 = Math.max(0, var5 - var1);
            var3 = Math.max(0, var2 - var1);
         }

         var5 = var2;
         var1 = var3;
         if (this.mMenuView != null) {
            var5 = var2;
            var1 = var3;
            if (this.mMenuView.getParent() == this) {
               var5 = this.measureChildView(this.mMenuView, var2, var8, 0);
               var1 = Math.max(0, var3 - this.mMenuView.getMeasuredWidth());
            }
         }

         var4 = var5;
         int var7 = var1;
         if (this.mIndeterminateProgressView != null) {
            var4 = var5;
            var7 = var1;
            if (this.mIndeterminateProgressView.getVisibility() != 8) {
               var4 = this.measureChildView(this.mIndeterminateProgressView, var5, var8, 0);
               var7 = Math.max(0, var1 - this.mIndeterminateProgressView.getMeasuredWidth());
            }
         }

         boolean var19;
         if (this.mTitleLayout != null && this.mTitleLayout.getVisibility() != 8 && (this.mDisplayOptions & 8) != 0) {
            var19 = true;
         } else {
            var19 = false;
         }

         var1 = var4;
         var2 = var3;
         if (this.mExpandedActionView == null) {
            switch(this.mNavigationMode) {
            case 1:
               var1 = var4;
               var2 = var3;
               if (this.mListNavLayout != null) {
                  if (var19) {
                     var1 = this.mItemPadding * 2;
                  } else {
                     var1 = this.mItemPadding;
                  }

                  var2 = Math.max(0, var4 - var1);
                  var3 = Math.max(0, var3 - var1);
                  this.mListNavLayout.measure(MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(var10, 1073741824));
                  var4 = this.mListNavLayout.getMeasuredWidth();
                  var1 = Math.max(0, var2 - var4);
                  var2 = Math.max(0, var3 - var4);
               }
               break;
            case 2:
               var1 = var4;
               var2 = var3;
               if (this.mTabScrollView != null) {
                  if (var19) {
                     var1 = this.mItemPadding * 2;
                  } else {
                     var1 = this.mItemPadding;
                  }

                  var2 = Math.max(0, var4 - var1);
                  var3 = Math.max(0, var3 - var1);
                  this.mTabScrollView.measure(MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(var10, 1073741824));
                  var4 = this.mTabScrollView.getMeasuredWidth();
                  var1 = Math.max(0, var2 - var4);
                  var2 = Math.max(0, var3 - var4);
               }
               break;
            default:
               var2 = var3;
               var1 = var4;
            }
         }

         var17 = null;
         if (this.mExpandedActionView != null) {
            var16 = this.mExpandedActionView;
         } else {
            var16 = var17;
            if ((this.mDisplayOptions & 16) != 0) {
               var16 = var17;
               if (this.mCustomNavView != null) {
                  var16 = this.mCustomNavView;
               }
            }
         }

         var3 = var1;
         if (var16 != null) {
            LayoutParams var18 = this.generateLayoutParams(var16.getLayoutParams());
            ActionBar.LayoutParams var21;
            if (var18 instanceof ActionBar.LayoutParams) {
               var21 = (ActionBar.LayoutParams)var18;
            } else {
               var21 = null;
            }

            var4 = 0;
            var8 = 0;
            if (var21 != null) {
               var4 = var21.leftMargin + var21.rightMargin;
               var8 = var21.topMargin + var21.bottomMargin;
            }

            if (this.mContentHeight <= 0) {
               var3 = Integer.MIN_VALUE;
            } else if (var18.height != -2) {
               var3 = 1073741824;
            } else {
               var3 = Integer.MIN_VALUE;
            }

            int var9 = var10;
            if (var18.height >= 0) {
               var9 = Math.min(var18.height, var10);
            }

            int var15 = Math.max(0, var9 - var8);
            if (var18.width != -2) {
               var8 = 1073741824;
            } else {
               var8 = Integer.MIN_VALUE;
            }

            if (var18.width >= 0) {
               var9 = Math.min(var18.width, var1);
            } else {
               var9 = var1;
            }

            int var11 = Math.max(0, var9 - var4);
            if (var21 != null) {
               var9 = var21.gravity;
            } else {
               var9 = 19;
            }

            var10 = var11;
            if ((var9 & 7) == 1) {
               var10 = var11;
               if (var18.width == -1) {
                  var10 = Math.min(var2, var7) * 2;
               }
            }

            var16.measure(MeasureSpec.makeMeasureSpec(var10, var8), MeasureSpec.makeMeasureSpec(var15, var3));
            var3 = var1 - (var16.getMeasuredWidth() + var4);
         }

         if (this.mExpandedActionView == null && var19) {
            this.measureChildView(this.mTitleLayout, var3, MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824), 0);
            Math.max(0, var2 - this.mTitleLayout.getMeasuredWidth());
         }

         if (this.mContentHeight > 0) {
            this.setMeasuredDimension(var13, var6);
         } else {
            var2 = 0;

            for(var1 = 0; var1 < var12; var2 = var3) {
               var4 = this.getChildAt(var1).getMeasuredHeight() + var14;
               var3 = var2;
               if (var4 > var2) {
                  var3 = var4;
               }

               ++var1;
            }

            this.setMeasuredDimension(var13, var2);
         }

         if (this.mContextView != null) {
            this.mContextView.setContentHeight(this.getMeasuredHeight());
         }

         if (this.mProgressView != null && this.mProgressView.getVisibility() != 8) {
            this.mProgressView.measure(MeasureSpec.makeMeasureSpec(var13 - this.mProgressBarPadding * 2, 1073741824), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), Integer.MIN_VALUE));
         }
      }
   }

   public void onRestoreInstanceState(Parcelable var1) {
      ActionBarView.SavedState var3 = (ActionBarView.SavedState)var1;
      super.onRestoreInstanceState(var3.getSuperState());
      if (var3.expandedMenuItemId != 0 && this.mExpandedMenuPresenter != null && this.mOptionsMenu != null) {
         SupportMenuItem var2 = (SupportMenuItem)this.mOptionsMenu.findItem(var3.expandedMenuItemId);
         if (var2 != null) {
            var2.expandActionView();
         }
      }

      if (var3.isOverflowOpen) {
         this.postShowOverflowMenu();
      }

   }

   public Parcelable onSaveInstanceState() {
      ActionBarView.SavedState var1 = new ActionBarView.SavedState(super.onSaveInstanceState());
      if (this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null) {
         var1.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
      }

      var1.isOverflowOpen = this.isOverflowMenuShowing();
      return var1;
   }

   public void setCallback(ActionBar.OnNavigationListener var1) {
      this.mCallback = var1;
   }

   public void setCollapsable(boolean var1) {
      this.mIsCollapsable = var1;
   }

   public void setContextView(ActionBarContextView var1) {
      this.mContextView = var1;
   }

   public void setCustomNavigationView(View var1) {
      boolean var2;
      if ((this.mDisplayOptions & 16) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (this.mCustomNavView != null && var2) {
         this.removeView(this.mCustomNavView);
      }

      this.mCustomNavView = var1;
      if (this.mCustomNavView != null && var2) {
         this.addView(this.mCustomNavView);
      }

   }

   public void setDisplayOptions(int var1) {
      byte var6 = 8;
      int var3 = -1;
      boolean var8 = true;
      if (this.mDisplayOptions != -1) {
         var3 = var1 ^ this.mDisplayOptions;
      }

      this.mDisplayOptions = var1;
      if ((var3 & 31) != 0) {
         boolean var4;
         if ((var1 & 2) != 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         byte var2;
         if (var4 && this.mExpandedActionView == null) {
            var2 = 0;
         } else {
            var2 = 8;
         }

         this.mHomeLayout.setVisibility(var2);
         boolean var7;
         if ((var3 & 4) != 0) {
            if ((var1 & 4) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            this.mHomeLayout.setUp(var7);
            if (var7) {
               this.setHomeButtonEnabled(true);
            }
         }

         if ((var3 & 1) != 0) {
            boolean var11;
            if (this.mLogo != null && (var1 & 1) != 0) {
               var11 = true;
            } else {
               var11 = false;
            }

            ActionBarView.HomeView var10 = this.mHomeLayout;
            Drawable var9;
            if (var11) {
               var9 = this.mLogo;
            } else {
               var9 = this.mIcon;
            }

            var10.setIcon(var9);
         }

         if ((var3 & 8) != 0) {
            if ((var1 & 8) != 0) {
               this.initTitle();
            } else {
               this.removeView(this.mTitleLayout);
            }
         }

         if (this.mTitleLayout != null && (var3 & 6) != 0) {
            boolean var5;
            if ((this.mDisplayOptions & 4) != 0) {
               var5 = true;
            } else {
               var5 = false;
            }

            View var12 = this.mTitleUpView;
            var2 = var6;
            if (!var4) {
               if (var5) {
                  var2 = 0;
               } else {
                  var2 = 4;
               }
            }

            var12.setVisibility(var2);
            LinearLayout var13 = this.mTitleLayout;
            if (!var4 && var5) {
               var7 = var8;
            } else {
               var7 = false;
            }

            var13.setEnabled(var7);
         }

         if ((var3 & 16) != 0 && this.mCustomNavView != null) {
            if ((var1 & 16) != 0) {
               this.addView(this.mCustomNavView);
            } else {
               this.removeView(this.mCustomNavView);
            }
         }

         this.requestLayout();
      } else {
         this.invalidate();
      }

      if (!this.mHomeLayout.isEnabled()) {
         this.mHomeLayout.setContentDescription((CharSequence)null);
      } else if ((var1 & 4) != 0) {
         this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(R$string.abc_action_bar_up_description));
      } else {
         this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(R$string.abc_action_bar_home_description));
      }
   }

   public void setDropdownAdapter(SpinnerAdapter var1) {
      this.mSpinnerAdapter = var1;
      if (this.mSpinner != null) {
         this.mSpinner.setAdapter(var1);
      }

   }

   public void setDropdownSelectedPosition(int var1) {
      this.mSpinner.setSelection(var1);
   }

   public void setEmbeddedTabView(ScrollingTabContainerView var1) {
      if (this.mTabScrollView != null) {
         this.removeView(this.mTabScrollView);
      }

      this.mTabScrollView = var1;
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mIncludeTabs = var2;
      if (this.mIncludeTabs && this.mNavigationMode == 2) {
         this.addView(this.mTabScrollView);
         LayoutParams var3 = this.mTabScrollView.getLayoutParams();
         var3.width = -2;
         var3.height = -1;
         var1.setAllowCollapse(true);
      }

   }

   public void setHomeAsUpIndicator(int var1) {
      this.mHomeLayout.setUpIndicator(var1);
   }

   public void setHomeAsUpIndicator(Drawable var1) {
      this.mHomeLayout.setUpIndicator(var1);
   }

   public void setHomeButtonEnabled(boolean var1) {
      this.mHomeLayout.setEnabled(var1);
      this.mHomeLayout.setFocusable(var1);
      if (!var1) {
         this.mHomeLayout.setContentDescription((CharSequence)null);
      } else if ((this.mDisplayOptions & 4) != 0) {
         this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(R$string.abc_action_bar_up_description));
      } else {
         this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(R$string.abc_action_bar_home_description));
      }
   }

   public void setIcon(int var1) {
      this.setIcon(this.mContext.getResources().getDrawable(var1));
   }

   public void setIcon(Drawable var1) {
      this.mIcon = var1;
      if (var1 != null && ((this.mDisplayOptions & 1) == 0 || this.mLogo == null)) {
         this.mHomeLayout.setIcon(var1);
      }

      if (this.mExpandedActionView != null) {
         this.mExpandedHomeLayout.setIcon(this.mIcon.getConstantState().newDrawable(this.getResources()));
      }

   }

   public void setLogo(int var1) {
      this.setLogo(this.mContext.getResources().getDrawable(var1));
   }

   public void setLogo(Drawable var1) {
      this.mLogo = var1;
      if (var1 != null && (this.mDisplayOptions & 1) != 0) {
         this.mHomeLayout.setIcon(var1);
      }

   }

   public void setMenu(SupportMenu var1, MenuPresenter.Callback var2) {
      if (var1 != this.mOptionsMenu) {
         if (this.mOptionsMenu != null) {
            this.mOptionsMenu.removeMenuPresenter(this.mActionMenuPresenter);
            this.mOptionsMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
         }

         MenuBuilder var3 = (MenuBuilder)var1;
         this.mOptionsMenu = var3;
         if (this.mMenuView != null) {
            ViewGroup var4 = (ViewGroup)this.mMenuView.getParent();
            if (var4 != null) {
               var4.removeView(this.mMenuView);
            }
         }

         if (this.mActionMenuPresenter == null) {
            this.mActionMenuPresenter = new ActionMenuPresenter(this.mContext);
            this.mActionMenuPresenter.setCallback(var2);
            this.mActionMenuPresenter.setId(R$id.action_menu_presenter);
            this.mExpandedMenuPresenter = new ActionBarView.ExpandedActionViewMenuPresenter();
         }

         LayoutParams var6 = new LayoutParams(-2, -1);
         ActionMenuView var5;
         ViewGroup var7;
         if (!this.mSplitActionBar) {
            this.mActionMenuPresenter.setExpandedActionViewsExclusive(this.getResources().getBoolean(R$bool.abc_action_bar_expanded_action_views_exclusive));
            this.configPresenters(var3);
            var5 = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
            var5.initialize(var3);
            var7 = (ViewGroup)var5.getParent();
            if (var7 != null && var7 != this) {
               var7.removeView(var5);
            }

            this.addView(var5, var6);
         } else {
            this.mActionMenuPresenter.setExpandedActionViewsExclusive(false);
            this.mActionMenuPresenter.setWidthLimit(this.getContext().getResources().getDisplayMetrics().widthPixels, true);
            this.mActionMenuPresenter.setItemLimit(Integer.MAX_VALUE);
            var6.width = -1;
            this.configPresenters(var3);
            var5 = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
            if (this.mSplitView != null) {
               var7 = (ViewGroup)var5.getParent();
               if (var7 != null && var7 != this.mSplitView) {
                  var7.removeView(var5);
               }

               var5.setVisibility(this.getAnimatedVisibility());
               this.mSplitView.addView(var5, var6);
            } else {
               var5.setLayoutParams(var6);
            }
         }

         this.mMenuView = var5;
      }
   }

   public void setNavigationMode(int var1) {
      int var2 = this.mNavigationMode;
      if (var1 != var2) {
         switch(var2) {
         case 1:
            if (this.mListNavLayout != null) {
               this.removeView(this.mListNavLayout);
            }
            break;
         case 2:
            if (this.mTabScrollView != null && this.mIncludeTabs) {
               this.removeView(this.mTabScrollView);
            }
         }

         switch(var1) {
         case 1:
            if (this.mSpinner == null) {
               this.mSpinner = new SpinnerICS(this.mContext, (AttributeSet)null, R$attr.actionDropDownStyle);
               this.mListNavLayout = (LinearLayout)LayoutInflater.from(this.mContext).inflate(R$layout.abc_action_bar_view_list_nav_layout, (ViewGroup)null);
               android.widget.LinearLayout.LayoutParams var3 = new android.widget.LinearLayout.LayoutParams(-2, -1);
               var3.gravity = 17;
               this.mListNavLayout.addView(this.mSpinner, var3);
            }

            if (this.mSpinner.getAdapter() != this.mSpinnerAdapter) {
               this.mSpinner.setAdapter(this.mSpinnerAdapter);
            }

            this.mSpinner.setOnItemSelectedListener(this.mNavItemSelectedListener);
            this.addView(this.mListNavLayout);
            break;
         case 2:
            if (this.mTabScrollView != null && this.mIncludeTabs) {
               this.addView(this.mTabScrollView);
            }
         }

         this.mNavigationMode = var1;
         this.requestLayout();
      }

   }

   public void setSplitActionBar(boolean var1) {
      if (this.mSplitActionBar != var1) {
         if (this.mMenuView != null) {
            ViewGroup var3 = (ViewGroup)this.mMenuView.getParent();
            if (var3 != null) {
               var3.removeView(this.mMenuView);
            }

            if (var1) {
               if (this.mSplitView != null) {
                  this.mSplitView.addView(this.mMenuView);
               }

               this.mMenuView.getLayoutParams().width = -1;
            } else {
               this.addView(this.mMenuView);
               this.mMenuView.getLayoutParams().width = -2;
            }

            this.mMenuView.requestLayout();
         }

         if (this.mSplitView != null) {
            ActionBarContainer var4 = this.mSplitView;
            byte var2;
            if (var1) {
               var2 = 0;
            } else {
               var2 = 8;
            }

            var4.setVisibility(var2);
         }

         if (this.mActionMenuPresenter != null) {
            if (!var1) {
               this.mActionMenuPresenter.setExpandedActionViewsExclusive(this.getResources().getBoolean(R$bool.abc_action_bar_expanded_action_views_exclusive));
            } else {
               this.mActionMenuPresenter.setExpandedActionViewsExclusive(false);
               this.mActionMenuPresenter.setWidthLimit(this.getContext().getResources().getDisplayMetrics().widthPixels, true);
               this.mActionMenuPresenter.setItemLimit(Integer.MAX_VALUE);
            }
         }

         super.setSplitActionBar(var1);
      }

   }

   public void setSubtitle(CharSequence var1) {
      byte var3 = 0;
      this.mSubtitle = var1;
      if (this.mSubtitleView != null) {
         this.mSubtitleView.setText(var1);
         TextView var4 = this.mSubtitleView;
         byte var2;
         if (var1 != null) {
            var2 = 0;
         } else {
            var2 = 8;
         }

         var4.setVisibility(var2);
         boolean var6;
         if (this.mExpandedActionView != null || (this.mDisplayOptions & 8) == 0 || TextUtils.isEmpty(this.mTitle) && TextUtils.isEmpty(this.mSubtitle)) {
            var6 = false;
         } else {
            var6 = true;
         }

         LinearLayout var5 = this.mTitleLayout;
         if (var6) {
            var2 = var3;
         } else {
            var2 = 8;
         }

         var5.setVisibility(var2);
      }

   }

   public void setTitle(CharSequence var1) {
      this.mUserTitle = true;
      this.setTitleImpl(var1);
   }

   public void setWindowCallback(Callback var1) {
      this.mWindowCallback = var1;
   }

   public void setWindowTitle(CharSequence var1) {
      if (!this.mUserTitle) {
         this.setTitleImpl(var1);
      }

   }

   public boolean shouldDelayChildPressedState() {
      return false;
   }

   private class ExpandedActionViewMenuPresenter implements MenuPresenter {
      MenuItemImpl mCurrentExpandedItem;
      MenuBuilder mMenu;

      private ExpandedActionViewMenuPresenter() {
      }

      // $FF: synthetic method
      ExpandedActionViewMenuPresenter(Object var2) {
         this();
      }

      public boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2) {
         if (ActionBarView.this.mExpandedActionView instanceof CollapsibleActionView) {
            ((CollapsibleActionView)ActionBarView.this.mExpandedActionView).onActionViewCollapsed();
         }

         ActionBarView.this.removeView(ActionBarView.this.mExpandedActionView);
         ActionBarView.this.removeView(ActionBarView.this.mExpandedHomeLayout);
         ActionBarView.this.mExpandedActionView = null;
         if ((ActionBarView.this.mDisplayOptions & 2) != 0) {
            ActionBarView.this.mHomeLayout.setVisibility(0);
         }

         if ((ActionBarView.this.mDisplayOptions & 8) != 0) {
            if (ActionBarView.this.mTitleLayout == null) {
               ActionBarView.this.initTitle();
            } else {
               ActionBarView.this.mTitleLayout.setVisibility(0);
            }
         }

         if (ActionBarView.this.mTabScrollView != null && ActionBarView.this.mNavigationMode == 2) {
            ActionBarView.this.mTabScrollView.setVisibility(0);
         }

         if (ActionBarView.this.mSpinner != null && ActionBarView.this.mNavigationMode == 1) {
            ActionBarView.this.mSpinner.setVisibility(0);
         }

         if (ActionBarView.this.mCustomNavView != null && (ActionBarView.this.mDisplayOptions & 16) != 0) {
            ActionBarView.this.mCustomNavView.setVisibility(0);
         }

         ActionBarView.this.mExpandedHomeLayout.setIcon((Drawable)null);
         this.mCurrentExpandedItem = null;
         ActionBarView.this.requestLayout();
         var2.setActionViewExpanded(false);
         return true;
      }

      public boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2) {
         ActionBarView.this.mExpandedActionView = var2.getActionView();
         ActionBarView.this.mExpandedHomeLayout.setIcon(ActionBarView.this.mIcon.getConstantState().newDrawable(ActionBarView.this.getResources()));
         this.mCurrentExpandedItem = var2;
         if (ActionBarView.this.mExpandedActionView.getParent() != ActionBarView.this) {
            ActionBarView.this.addView(ActionBarView.this.mExpandedActionView);
         }

         if (ActionBarView.this.mExpandedHomeLayout.getParent() != ActionBarView.this) {
            ActionBarView.this.addView(ActionBarView.this.mExpandedHomeLayout);
         }

         ActionBarView.this.mHomeLayout.setVisibility(8);
         if (ActionBarView.this.mTitleLayout != null) {
            ActionBarView.this.mTitleLayout.setVisibility(8);
         }

         if (ActionBarView.this.mTabScrollView != null) {
            ActionBarView.this.mTabScrollView.setVisibility(8);
         }

         if (ActionBarView.this.mSpinner != null) {
            ActionBarView.this.mSpinner.setVisibility(8);
         }

         if (ActionBarView.this.mCustomNavView != null) {
            ActionBarView.this.mCustomNavView.setVisibility(8);
         }

         ActionBarView.this.requestLayout();
         var2.setActionViewExpanded(true);
         if (ActionBarView.this.mExpandedActionView instanceof CollapsibleActionView) {
            ((CollapsibleActionView)ActionBarView.this.mExpandedActionView).onActionViewExpanded();
         }

         return true;
      }

      public boolean flagActionItems() {
         return false;
      }

      public int getId() {
         return 0;
      }

      public MenuView getMenuView(ViewGroup var1) {
         return null;
      }

      public void initForMenu(Context var1, MenuBuilder var2) {
         if (this.mMenu != null && this.mCurrentExpandedItem != null) {
            this.mMenu.collapseItemActionView(this.mCurrentExpandedItem);
         }

         this.mMenu = var2;
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
      }

      public void onRestoreInstanceState(Parcelable var1) {
      }

      public Parcelable onSaveInstanceState() {
         return null;
      }

      public boolean onSubMenuSelected(SubMenuBuilder var1) {
         return false;
      }

      public void setCallback(MenuPresenter.Callback var1) {
      }

      public void updateMenuView(boolean var1) {
         if (this.mCurrentExpandedItem != null) {
            boolean var4 = false;
            boolean var3 = var4;
            if (this.mMenu != null) {
               int var5 = this.mMenu.size();
               int var2 = 0;

               while(true) {
                  var3 = var4;
                  if (var2 >= var5) {
                     break;
                  }

                  if ((SupportMenuItem)this.mMenu.getItem(var2) == this.mCurrentExpandedItem) {
                     var3 = true;
                     break;
                  }

                  ++var2;
               }
            }

            if (!var3) {
               this.collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
            }
         }

      }
   }

   private static class HomeView extends FrameLayout {
      private Drawable mDefaultUpIndicator;
      private ImageView mIconView;
      private int mUpIndicatorRes;
      private ImageView mUpView;
      private int mUpWidth;

      public HomeView(Context var1) {
         this(var1, (AttributeSet)null);
      }

      public HomeView(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
         CharSequence var2 = this.getContentDescription();
         if (!TextUtils.isEmpty(var2)) {
            var1.getText().add(var2);
         }

         return true;
      }

      public int getLeftOffset() {
         return this.mUpView.getVisibility() == 8 ? this.mUpWidth : 0;
      }

      protected void onConfigurationChanged(Configuration var1) {
         super.onConfigurationChanged(var1);
         if (this.mUpIndicatorRes != 0) {
            this.setUpIndicator(this.mUpIndicatorRes);
         }

      }

      protected void onFinishInflate() {
         this.mUpView = (ImageView)this.findViewById(R$id.field_4);
         this.mIconView = (ImageView)this.findViewById(R$id.home);
         this.mDefaultUpIndicator = this.mUpView.getDrawable();
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         int var6 = (var5 - var3) / 2;
         var3 = 0;
         var5 = var2;
         int var7;
         android.widget.FrameLayout.LayoutParams var8;
         if (this.mUpView.getVisibility() != 8) {
            var8 = (android.widget.FrameLayout.LayoutParams)this.mUpView.getLayoutParams();
            var3 = this.mUpView.getMeasuredHeight();
            var5 = this.mUpView.getMeasuredWidth();
            var7 = var6 - var3 / 2;
            this.mUpView.layout(0, var7, var5, var7 + var3);
            var3 = var8.leftMargin + var5 + var8.rightMargin;
            var5 = var2 + var3;
         }

         var8 = (android.widget.FrameLayout.LayoutParams)this.mIconView.getLayoutParams();
         var2 = this.mIconView.getMeasuredHeight();
         var7 = this.mIconView.getMeasuredWidth();
         var4 = (var4 - var5) / 2;
         var3 += Math.max(var8.leftMargin, var4 - var7 / 2);
         var4 = Math.max(var8.topMargin, var6 - var2 / 2);
         this.mIconView.layout(var3, var4, var3 + var7, var4 + var2);
      }

      protected void onMeasure(int var1, int var2) {
         this.measureChildWithMargins(this.mUpView, var1, 0, var2, 0);
         android.widget.FrameLayout.LayoutParams var7 = (android.widget.FrameLayout.LayoutParams)this.mUpView.getLayoutParams();
         this.mUpWidth = var7.leftMargin + this.mUpView.getMeasuredWidth() + var7.rightMargin;
         int var3;
         if (this.mUpView.getVisibility() == 8) {
            var3 = 0;
         } else {
            var3 = this.mUpWidth;
         }

         int var4 = var7.topMargin;
         int var5 = this.mUpView.getMeasuredHeight();
         int var6 = var7.bottomMargin;
         this.measureChildWithMargins(this.mIconView, var1, var3, var2, 0);
         var7 = (android.widget.FrameLayout.LayoutParams)this.mIconView.getLayoutParams();
         var3 += var7.leftMargin + this.mIconView.getMeasuredWidth() + var7.rightMargin;
         var4 = Math.max(var4 + var5 + var6, var7.topMargin + this.mIconView.getMeasuredHeight() + var7.bottomMargin);
         var6 = MeasureSpec.getMode(var1);
         var5 = MeasureSpec.getMode(var2);
         var1 = MeasureSpec.getSize(var1);
         var2 = MeasureSpec.getSize(var2);
         switch(var6) {
         case Integer.MIN_VALUE:
            var1 = Math.min(var3, var1);
         case 1073741824:
            break;
         default:
            var1 = var3;
         }

         switch(var5) {
         case Integer.MIN_VALUE:
            var2 = Math.min(var4, var2);
         case 1073741824:
            break;
         default:
            var2 = var4;
         }

         this.setMeasuredDimension(var1, var2);
      }

      public void setIcon(Drawable var1) {
         this.mIconView.setImageDrawable(var1);
      }

      public void setUp(boolean var1) {
         ImageView var3 = this.mUpView;
         byte var2;
         if (var1) {
            var2 = 0;
         } else {
            var2 = 8;
         }

         var3.setVisibility(var2);
      }

      public void setUpIndicator(int var1) {
         this.mUpIndicatorRes = var1;
         ImageView var3 = this.mUpView;
         Drawable var2;
         if (var1 != 0) {
            var2 = this.getResources().getDrawable(var1);
         } else {
            var2 = this.mDefaultUpIndicator;
         }

         var3.setImageDrawable(var2);
      }

      public void setUpIndicator(Drawable var1) {
         ImageView var2 = this.mUpView;
         if (var1 == null) {
            var1 = this.mDefaultUpIndicator;
         }

         var2.setImageDrawable(var1);
         this.mUpIndicatorRes = 0;
      }
   }

   static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public ActionBarView.SavedState createFromParcel(Parcel var1) {
            return new ActionBarView.SavedState(var1);
         }

         public ActionBarView.SavedState[] newArray(int var1) {
            return new ActionBarView.SavedState[var1];
         }
      };
      int expandedMenuItemId;
      boolean isOverflowOpen;

      private SavedState(Parcel var1) {
         super(var1);
         this.expandedMenuItemId = var1.readInt();
         boolean var2;
         if (var1.readInt() != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.isOverflowOpen = var2;
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
         var1.writeInt(this.expandedMenuItemId);
         byte var3;
         if (this.isOverflowOpen) {
            var3 = 1;
         } else {
            var3 = 0;
         }

         var1.writeInt(var3);
      }
   }
}
