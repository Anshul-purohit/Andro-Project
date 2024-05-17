package android.support.v7.internal.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$bool;
import android.support.v7.appcompat.R$dimen;
import android.support.v7.appcompat.R$integer;
import android.support.v7.appcompat.R$styleable;
import android.util.AttributeSet;

public class ActionBarPolicy {
   private Context mContext;

   private ActionBarPolicy(Context var1) {
      this.mContext = var1;
   }

   public static ActionBarPolicy get(Context var0) {
      return new ActionBarPolicy(var0);
   }

   public boolean enableHomeButtonByDefault() {
      return this.mContext.getApplicationInfo().targetSdkVersion < 14;
   }

   public int getEmbeddedMenuWidthLimit() {
      return this.mContext.getResources().getDisplayMetrics().widthPixels / 2;
   }

   public int getMaxActionButtons() {
      return this.mContext.getResources().getInteger(R$integer.abc_max_action_buttons);
   }

   public int getStackedTabMaxWidth() {
      return this.mContext.getResources().getDimensionPixelSize(R$dimen.abc_action_bar_stacked_tab_max_width);
   }

   public int getTabContainerHeight() {
      TypedArray var3 = this.mContext.obtainStyledAttributes((AttributeSet)null, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
      int var2 = var3.getLayoutDimension(1, 0);
      Resources var4 = this.mContext.getResources();
      int var1 = var2;
      if (!this.hasEmbeddedTabs()) {
         var1 = Math.min(var2, var4.getDimensionPixelSize(R$dimen.abc_action_bar_stacked_max_height));
      }

      var3.recycle();
      return var1;
   }

   public boolean hasEmbeddedTabs() {
      return this.mContext.getResources().getBoolean(R$bool.abc_action_bar_embed_tabs_pre_jb);
   }

   public boolean showsOverflowMenuButton() {
      return VERSION.SDK_INT >= 11;
   }
}
