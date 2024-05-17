package android.support.v7.internal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuItemWrapperICS;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import java.io.IOException;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SupportMenuInflater extends MenuInflater {
   private static final Class[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE;
   private static final Class[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE = new Class[]{Context.class};
   private static final String LOG_TAG = "SupportMenuInflater";
   private static final int NO_ID = 0;
   private static final String XML_GROUP = "group";
   private static final String XML_ITEM = "item";
   private static final String XML_MENU = "menu";
   private final Object[] mActionProviderConstructorArguments;
   private final Object[] mActionViewConstructorArguments;
   private Context mContext;
   private Object mRealOwner;

   static {
      ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
   }

   public SupportMenuInflater(Context var1) {
      super(var1);
      this.mContext = var1;
      this.mRealOwner = var1;
      this.mActionViewConstructorArguments = new Object[]{var1};
      this.mActionProviderConstructorArguments = this.mActionViewConstructorArguments;
   }

   private void parseMenu(XmlPullParser var1, AttributeSet var2, Menu var3) throws XmlPullParserException, IOException {
      SupportMenuInflater.MenuState var10 = new SupportMenuInflater.MenuState(var3);
      int var4 = var1.getEventType();
      boolean var6 = false;
      String var9 = null;

      String var12;
      while(true) {
         if (var4 == 2) {
            var12 = var1.getName();
            if (!var12.equals("menu")) {
               throw new RuntimeException("Expecting menu, got " + var12);
            }

            var4 = var1.next();
            break;
         }

         int var5 = var1.next();
         var4 = var5;
         if (var5 == 1) {
            var4 = var5;
            break;
         }
      }

      boolean var13 = false;

      for(int var8 = var4; !var13; var9 = var12) {
         boolean var7;
         boolean var14;
         switch(var8) {
         case 1:
            throw new RuntimeException("Unexpected end of document");
         case 2:
            var14 = var6;
            var7 = var13;
            var12 = var9;
            if (!var6) {
               var12 = var1.getName();
               if (var12.equals("group")) {
                  var10.readGroup(var2);
                  var14 = var6;
                  var7 = var13;
                  var12 = var9;
               } else if (var12.equals("item")) {
                  var10.readItem(var2);
                  var14 = var6;
                  var7 = var13;
                  var12 = var9;
               } else if (var12.equals("menu")) {
                  this.parseMenu(var1, var2, var10.addSubMenuItem());
                  var14 = var6;
                  var7 = var13;
                  var12 = var9;
               } else {
                  var14 = true;
                  var7 = var13;
               }
            }
            break;
         case 3:
            String var11 = var1.getName();
            if (var6 && var11.equals(var9)) {
               var14 = false;
               var12 = null;
               var7 = var13;
            } else if (var11.equals("group")) {
               var10.resetGroup();
               var14 = var6;
               var7 = var13;
               var12 = var9;
            } else if (var11.equals("item")) {
               var14 = var6;
               var7 = var13;
               var12 = var9;
               if (!var10.hasAddedItem()) {
                  if (var10.itemActionProvider != null && var10.itemActionProvider.hasSubMenu()) {
                     var10.addSubMenuItem();
                     var14 = var6;
                     var7 = var13;
                     var12 = var9;
                  } else {
                     var10.addItem();
                     var14 = var6;
                     var7 = var13;
                     var12 = var9;
                  }
               }
            } else {
               var14 = var6;
               var7 = var13;
               var12 = var9;
               if (var11.equals("menu")) {
                  var7 = true;
                  var14 = var6;
                  var12 = var9;
               }
            }
            break;
         default:
            var12 = var9;
            var7 = var13;
            var14 = var6;
         }

         var8 = var1.next();
         var6 = var14;
         var13 = var7;
      }

   }

   public void inflate(int param1, Menu param2) {
      // $FF: Couldn't be decompiled
   }

   private static class InflatedOnMenuItemClickListener implements OnMenuItemClickListener {
      private static final Class[] PARAM_TYPES = new Class[]{MenuItem.class};
      private Method mMethod;
      private Object mRealOwner;

      public InflatedOnMenuItemClickListener(Object var1, String var2) {
         this.mRealOwner = var1;
         Class var3 = var1.getClass();

         try {
            this.mMethod = var3.getMethod(var2, PARAM_TYPES);
         } catch (Exception var4) {
            InflateException var5 = new InflateException("Couldn't resolve menu item onClick handler " + var2 + " in class " + var3.getName());
            var5.initCause(var4);
            throw var5;
         }
      }

      public boolean onMenuItemClick(MenuItem var1) {
         try {
            if (this.mMethod.getReturnType() == Boolean.TYPE) {
               return (Boolean)this.mMethod.invoke(this.mRealOwner, var1);
            } else {
               this.mMethod.invoke(this.mRealOwner, var1);
               return true;
            }
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }
   }

   private class MenuState {
      private static final int defaultGroupId = 0;
      private static final int defaultItemCategory = 0;
      private static final int defaultItemCheckable = 0;
      private static final boolean defaultItemChecked = false;
      private static final boolean defaultItemEnabled = true;
      private static final int defaultItemId = 0;
      private static final int defaultItemOrder = 0;
      private static final boolean defaultItemVisible = true;
      private int groupCategory;
      private int groupCheckable;
      private boolean groupEnabled;
      private int groupId;
      private int groupOrder;
      private boolean groupVisible;
      private ActionProvider itemActionProvider;
      private String itemActionProviderClassName;
      private String itemActionViewClassName;
      private int itemActionViewLayout;
      private boolean itemAdded;
      private char itemAlphabeticShortcut;
      private int itemCategoryOrder;
      private int itemCheckable;
      private boolean itemChecked;
      private boolean itemEnabled;
      private int itemIconResId;
      private int itemId;
      private String itemListenerMethodName;
      private char itemNumericShortcut;
      private int itemShowAsAction;
      private CharSequence itemTitle;
      private CharSequence itemTitleCondensed;
      private boolean itemVisible;
      private Menu menu;

      public MenuState(Menu var2) {
         this.menu = var2;
         this.resetGroup();
      }

      private char getShortcut(String var1) {
         return var1 == null ? '\u0000' : var1.charAt(0);
      }

      private Object newInstance(String var1, Class[] var2, Object[] var3) {
         try {
            Object var5 = SupportMenuInflater.this.mContext.getClassLoader().loadClass(var1).getConstructor(var2).newInstance(var3);
            return var5;
         } catch (Exception var4) {
            Log.w("SupportMenuInflater", "Cannot instantiate class: " + var1, var4);
            return null;
         }
      }

      private void setItem(MenuItem var1) {
         MenuItem var4 = var1.setChecked(this.itemChecked).setVisible(this.itemVisible).setEnabled(this.itemEnabled);
         boolean var3;
         if (this.itemCheckable >= 1) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.setCheckable(var3).setTitleCondensed(this.itemTitleCondensed).setIcon(this.itemIconResId).setAlphabeticShortcut(this.itemAlphabeticShortcut).setNumericShortcut(this.itemNumericShortcut);
         if (this.itemShowAsAction >= 0) {
            MenuItemCompat.setShowAsAction(var1, this.itemShowAsAction);
         }

         if (this.itemListenerMethodName != null) {
            if (SupportMenuInflater.this.mContext.isRestricted()) {
               throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
            }

            var1.setOnMenuItemClickListener(new SupportMenuInflater.InflatedOnMenuItemClickListener(SupportMenuInflater.this.mRealOwner, this.itemListenerMethodName));
         }

         if (var1 instanceof MenuItemImpl) {
            MenuItemImpl var5 = (MenuItemImpl)var1;
         }

         if (this.itemCheckable >= 2) {
            if (var1 instanceof MenuItemImpl) {
               ((MenuItemImpl)var1).setExclusiveCheckable(true);
            } else if (var1 instanceof MenuItemWrapperICS) {
               ((MenuItemWrapperICS)var1).setExclusiveCheckable(true);
            }
         }

         boolean var2 = false;
         if (this.itemActionViewClassName != null) {
            MenuItemCompat.setActionView(var1, (View)this.newInstance(this.itemActionViewClassName, SupportMenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionViewConstructorArguments));
            var2 = true;
         }

         if (this.itemActionViewLayout > 0) {
            if (!var2) {
               MenuItemCompat.setActionView(var1, this.itemActionViewLayout);
            } else {
               Log.w("SupportMenuInflater", "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
            }
         }

         if (this.itemActionProvider != null) {
            MenuItemCompat.setActionProvider(var1, this.itemActionProvider);
         }

      }

      public void addItem() {
         this.itemAdded = true;
         this.setItem(this.menu.add(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle));
      }

      public SubMenu addSubMenuItem() {
         this.itemAdded = true;
         SubMenu var1 = this.menu.addSubMenu(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle);
         this.setItem(var1.getItem());
         return var1;
      }

      public boolean hasAddedItem() {
         return this.itemAdded;
      }

      public void readGroup(AttributeSet var1) {
         TypedArray var2 = SupportMenuInflater.this.mContext.obtainStyledAttributes(var1, R$styleable.MenuGroup);
         this.groupId = var2.getResourceId(1, 0);
         this.groupCategory = var2.getInt(3, 0);
         this.groupOrder = var2.getInt(4, 0);
         this.groupCheckable = var2.getInt(5, 0);
         this.groupVisible = var2.getBoolean(2, true);
         this.groupEnabled = var2.getBoolean(0, true);
         var2.recycle();
      }

      public void readItem(AttributeSet var1) {
         TypedArray var3 = SupportMenuInflater.this.mContext.obtainStyledAttributes(var1, R$styleable.MenuItem);
         this.itemId = var3.getResourceId(2, 0);
         this.itemCategoryOrder = -65536 & var3.getInt(5, this.groupCategory) | '\uffff' & var3.getInt(6, this.groupOrder);
         this.itemTitle = var3.getText(7);
         this.itemTitleCondensed = var3.getText(8);
         this.itemIconResId = var3.getResourceId(0, 0);
         this.itemAlphabeticShortcut = this.getShortcut(var3.getString(9));
         this.itemNumericShortcut = this.getShortcut(var3.getString(10));
         if (var3.hasValue(11)) {
            byte var2;
            if (var3.getBoolean(11, false)) {
               var2 = 1;
            } else {
               var2 = 0;
            }

            this.itemCheckable = var2;
         } else {
            this.itemCheckable = this.groupCheckable;
         }

         this.itemChecked = var3.getBoolean(3, false);
         this.itemVisible = var3.getBoolean(4, this.groupVisible);
         this.itemEnabled = var3.getBoolean(1, this.groupEnabled);
         this.itemShowAsAction = var3.getInt(13, -1);
         this.itemListenerMethodName = var3.getString(12);
         this.itemActionViewLayout = var3.getResourceId(14, 0);
         this.itemActionViewClassName = var3.getString(15);
         this.itemActionProviderClassName = var3.getString(16);
         boolean var4;
         if (this.itemActionProviderClassName != null) {
            var4 = true;
         } else {
            var4 = false;
         }

         if (var4 && this.itemActionViewLayout == 0 && this.itemActionViewClassName == null) {
            this.itemActionProvider = (ActionProvider)this.newInstance(this.itemActionProviderClassName, SupportMenuInflater.ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionProviderConstructorArguments);
         } else {
            if (var4) {
               Log.w("SupportMenuInflater", "Ignoring attribute 'actionProviderClass'. Action view already specified.");
            }

            this.itemActionProvider = null;
         }

         var3.recycle();
         this.itemAdded = false;
      }

      public void resetGroup() {
         this.groupId = 0;
         this.groupCategory = 0;
         this.groupOrder = 0;
         this.groupCheckable = 0;
         this.groupVisible = true;
         this.groupEnabled = true;
      }
   }
}
