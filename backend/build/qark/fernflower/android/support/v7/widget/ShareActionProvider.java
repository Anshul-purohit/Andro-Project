package android.support.v7.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$string;
import android.support.v7.internal.widget.ActivityChooserModel;
import android.support.v7.internal.widget.ActivityChooserView;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;

public class ShareActionProvider extends ActionProvider {
   private static final int DEFAULT_INITIAL_ACTIVITY_COUNT = 4;
   public static final String DEFAULT_SHARE_HISTORY_FILE_NAME = "share_history.xml";
   private final Context mContext;
   private int mMaxShownActivityCount = 4;
   private ActivityChooserModel.OnChooseActivityListener mOnChooseActivityListener;
   private final ShareActionProvider.ShareMenuItemOnMenuItemClickListener mOnMenuItemClickListener = new ShareActionProvider.ShareMenuItemOnMenuItemClickListener();
   private ShareActionProvider.OnShareTargetSelectedListener mOnShareTargetSelectedListener;
   private String mShareHistoryFileName = "share_history.xml";

   public ShareActionProvider(Context var1) {
      super(var1);
      this.mContext = var1;
   }

   private void setActivityChooserPolicyIfNeeded() {
      if (this.mOnShareTargetSelectedListener != null) {
         if (this.mOnChooseActivityListener == null) {
            this.mOnChooseActivityListener = new ShareActionProvider.ShareActivityChooserModelPolicy();
         }

         ActivityChooserModel.get(this.mContext, this.mShareHistoryFileName).setOnChooseActivityListener(this.mOnChooseActivityListener);
      }
   }

   public boolean hasSubMenu() {
      return true;
   }

   public View onCreateActionView() {
      ActivityChooserModel var2 = ActivityChooserModel.get(this.mContext, this.mShareHistoryFileName);
      ActivityChooserView var1 = new ActivityChooserView(this.mContext);
      var1.setActivityChooserModel(var2);
      TypedValue var3 = new TypedValue();
      this.mContext.getTheme().resolveAttribute(R$attr.actionModeShareDrawable, var3, true);
      var1.setExpandActivityOverflowButtonDrawable(this.mContext.getResources().getDrawable(var3.resourceId));
      var1.setProvider(this);
      var1.setDefaultActionButtonContentDescription(R$string.abc_shareactionprovider_share_with_application);
      var1.setExpandActivityOverflowButtonContentDescription(R$string.abc_shareactionprovider_share_with);
      return var1;
   }

   public void onPrepareSubMenu(SubMenu var1) {
      var1.clear();
      ActivityChooserModel var5 = ActivityChooserModel.get(this.mContext, this.mShareHistoryFileName);
      PackageManager var6 = this.mContext.getPackageManager();
      int var3 = var5.getActivityCount();
      int var4 = Math.min(var3, this.mMaxShownActivityCount);

      int var2;
      ResolveInfo var7;
      for(var2 = 0; var2 < var4; ++var2) {
         var7 = var5.getActivity(var2);
         var1.add(0, var2, var2, var7.loadLabel(var6)).setIcon(var7.loadIcon(var6)).setOnMenuItemClickListener(this.mOnMenuItemClickListener);
      }

      if (var4 < var3) {
         var1 = var1.addSubMenu(0, var4, var4, this.mContext.getString(R$string.abc_activity_chooser_view_see_all));

         for(var2 = 0; var2 < var3; ++var2) {
            var7 = var5.getActivity(var2);
            var1.add(0, var2, var2, var7.loadLabel(var6)).setIcon(var7.loadIcon(var6)).setOnMenuItemClickListener(this.mOnMenuItemClickListener);
         }
      }

   }

   public void setOnShareTargetSelectedListener(ShareActionProvider.OnShareTargetSelectedListener var1) {
      this.mOnShareTargetSelectedListener = var1;
      this.setActivityChooserPolicyIfNeeded();
   }

   public void setShareHistoryFileName(String var1) {
      this.mShareHistoryFileName = var1;
      this.setActivityChooserPolicyIfNeeded();
   }

   public void setShareIntent(Intent var1) {
      ActivityChooserModel.get(this.mContext, this.mShareHistoryFileName).setIntent(var1);
   }

   public interface OnShareTargetSelectedListener {
      boolean onShareTargetSelected(ShareActionProvider var1, Intent var2);
   }

   private class ShareActivityChooserModelPolicy implements ActivityChooserModel.OnChooseActivityListener {
      private ShareActivityChooserModelPolicy() {
      }

      // $FF: synthetic method
      ShareActivityChooserModelPolicy(Object var2) {
         this();
      }

      public boolean onChooseActivity(ActivityChooserModel var1, Intent var2) {
         if (ShareActionProvider.this.mOnShareTargetSelectedListener != null) {
            ShareActionProvider.this.mOnShareTargetSelectedListener.onShareTargetSelected(ShareActionProvider.this, var2);
         }

         return false;
      }
   }

   private class ShareMenuItemOnMenuItemClickListener implements OnMenuItemClickListener {
      private ShareMenuItemOnMenuItemClickListener() {
      }

      // $FF: synthetic method
      ShareMenuItemOnMenuItemClickListener(Object var2) {
         this();
      }

      public boolean onMenuItemClick(MenuItem var1) {
         Intent var2 = ActivityChooserModel.get(ShareActionProvider.this.mContext, ShareActionProvider.this.mShareHistoryFileName).chooseActivity(var1.getItemId());
         if (var2 != null) {
            var2.addFlags(524288);
            ShareActionProvider.this.mContext.startActivity(var2);
         }

         return true;
      }
   }
}
