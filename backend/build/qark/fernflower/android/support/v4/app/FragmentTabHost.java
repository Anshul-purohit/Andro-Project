package android.support.v4.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import java.util.ArrayList;

public class FragmentTabHost extends TabHost implements OnTabChangeListener {
   private boolean mAttached;
   private int mContainerId;
   private Context mContext;
   private FragmentManager mFragmentManager;
   private FragmentTabHost.TabInfo mLastTab;
   private OnTabChangeListener mOnTabChangeListener;
   private FrameLayout mRealTabContent;
   private final ArrayList mTabs = new ArrayList();

   public FragmentTabHost(Context var1) {
      super(var1, (AttributeSet)null);
      this.initFragmentTabHost(var1, (AttributeSet)null);
   }

   public FragmentTabHost(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.initFragmentTabHost(var1, var2);
   }

   private FragmentTransaction doTabChanged(String var1, FragmentTransaction var2) {
      FragmentTabHost.TabInfo var4 = null;

      for(int var3 = 0; var3 < this.mTabs.size(); ++var3) {
         FragmentTabHost.TabInfo var5 = (FragmentTabHost.TabInfo)this.mTabs.get(var3);
         if (var5.tag.equals(var1)) {
            var4 = var5;
         }
      }

      if (var4 == null) {
         throw new IllegalStateException("No tab known for tag " + var1);
      } else {
         FragmentTransaction var6 = var2;
         if (this.mLastTab != var4) {
            var6 = var2;
            if (var2 == null) {
               var6 = this.mFragmentManager.beginTransaction();
            }

            if (this.mLastTab != null && this.mLastTab.fragment != null) {
               var6.detach(this.mLastTab.fragment);
            }

            if (var4 != null) {
               if (var4.fragment == null) {
                  var4.fragment = Fragment.instantiate(this.mContext, var4.clss.getName(), var4.args);
                  var6.add(this.mContainerId, var4.fragment, var4.tag);
               } else {
                  var6.attach(var4.fragment);
               }
            }

            this.mLastTab = var4;
         }

         return var6;
      }
   }

   private void ensureContent() {
      if (this.mRealTabContent == null) {
         this.mRealTabContent = (FrameLayout)this.findViewById(this.mContainerId);
         if (this.mRealTabContent == null) {
            throw new IllegalStateException("No tab content FrameLayout found for id " + this.mContainerId);
         }
      }

   }

   private void ensureHierarchy(Context var1) {
      if (this.findViewById(16908307) == null) {
         LinearLayout var2 = new LinearLayout(var1);
         var2.setOrientation(1);
         this.addView(var2, new LayoutParams(-1, -1));
         TabWidget var3 = new TabWidget(var1);
         var3.setId(16908307);
         var3.setOrientation(0);
         var2.addView(var3, new android.widget.LinearLayout.LayoutParams(-1, -2, 0.0F));
         FrameLayout var5 = new FrameLayout(var1);
         var5.setId(16908305);
         var2.addView(var5, new android.widget.LinearLayout.LayoutParams(0, 0, 0.0F));
         FrameLayout var4 = new FrameLayout(var1);
         this.mRealTabContent = var4;
         this.mRealTabContent.setId(this.mContainerId);
         var2.addView(var4, new android.widget.LinearLayout.LayoutParams(-1, 0, 1.0F));
      }

   }

   private void initFragmentTabHost(Context var1, AttributeSet var2) {
      TypedArray var3 = var1.obtainStyledAttributes(var2, new int[]{16842995}, 0, 0);
      this.mContainerId = var3.getResourceId(0, 0);
      var3.recycle();
      super.setOnTabChangedListener(this);
   }

   public void addTab(TabSpec var1, Class var2, Bundle var3) {
      var1.setContent(new FragmentTabHost.DummyTabFactory(this.mContext));
      String var4 = var1.getTag();
      FragmentTabHost.TabInfo var5 = new FragmentTabHost.TabInfo(var4, var2, var3);
      if (this.mAttached) {
         var5.fragment = this.mFragmentManager.findFragmentByTag(var4);
         if (var5.fragment != null && !var5.fragment.isDetached()) {
            FragmentTransaction var6 = this.mFragmentManager.beginTransaction();
            var6.detach(var5.fragment);
            var6.commit();
         }
      }

      this.mTabs.add(var5);
      this.addTab(var1);
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      String var4 = this.getCurrentTabTag();
      FragmentTransaction var2 = null;

      FragmentTransaction var3;
      for(int var1 = 0; var1 < this.mTabs.size(); var2 = var3) {
         FragmentTabHost.TabInfo var5 = (FragmentTabHost.TabInfo)this.mTabs.get(var1);
         var5.fragment = this.mFragmentManager.findFragmentByTag(var5.tag);
         var3 = var2;
         if (var5.fragment != null) {
            var3 = var2;
            if (!var5.fragment.isDetached()) {
               if (var5.tag.equals(var4)) {
                  this.mLastTab = var5;
                  var3 = var2;
               } else {
                  var3 = var2;
                  if (var2 == null) {
                     var3 = this.mFragmentManager.beginTransaction();
                  }

                  var3.detach(var5.fragment);
               }
            }
         }

         ++var1;
      }

      this.mAttached = true;
      var2 = this.doTabChanged(var4, var2);
      if (var2 != null) {
         var2.commit();
         this.mFragmentManager.executePendingTransactions();
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.mAttached = false;
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      FragmentTabHost.SavedState var2 = (FragmentTabHost.SavedState)var1;
      super.onRestoreInstanceState(var2.getSuperState());
      this.setCurrentTabByTag(var2.curTab);
   }

   protected Parcelable onSaveInstanceState() {
      FragmentTabHost.SavedState var1 = new FragmentTabHost.SavedState(super.onSaveInstanceState());
      var1.curTab = this.getCurrentTabTag();
      return var1;
   }

   public void onTabChanged(String var1) {
      if (this.mAttached) {
         FragmentTransaction var2 = this.doTabChanged(var1, (FragmentTransaction)null);
         if (var2 != null) {
            var2.commit();
         }
      }

      if (this.mOnTabChangeListener != null) {
         this.mOnTabChangeListener.onTabChanged(var1);
      }

   }

   public void setOnTabChangedListener(OnTabChangeListener var1) {
      this.mOnTabChangeListener = var1;
   }

   @Deprecated
   public void setup() {
      throw new IllegalStateException("Must call setup() that takes a Context and FragmentManager");
   }

   public void setup(Context var1, FragmentManager var2) {
      this.ensureHierarchy(var1);
      super.setup();
      this.mContext = var1;
      this.mFragmentManager = var2;
      this.ensureContent();
   }

   public void setup(Context var1, FragmentManager var2, int var3) {
      this.ensureHierarchy(var1);
      super.setup();
      this.mContext = var1;
      this.mFragmentManager = var2;
      this.mContainerId = var3;
      this.ensureContent();
      this.mRealTabContent.setId(var3);
      if (this.getId() == -1) {
         this.setId(16908306);
      }

   }

   static class DummyTabFactory implements TabContentFactory {
      private final Context mContext;

      public DummyTabFactory(Context var1) {
         this.mContext = var1;
      }

      public View createTabContent(String var1) {
         View var2 = new View(this.mContext);
         var2.setMinimumWidth(0);
         var2.setMinimumHeight(0);
         return var2;
      }
   }

   static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public FragmentTabHost.SavedState createFromParcel(Parcel var1) {
            return new FragmentTabHost.SavedState(var1);
         }

         public FragmentTabHost.SavedState[] newArray(int var1) {
            return new FragmentTabHost.SavedState[var1];
         }
      };
      String curTab;

      private SavedState(Parcel var1) {
         super(var1);
         this.curTab = var1.readString();
      }

      // $FF: synthetic method
      SavedState(Parcel var1, Object var2) {
         this(var1);
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public String toString() {
         return "FragmentTabHost.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " curTab=" + this.curTab + "}";
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeString(this.curTab);
      }
   }

   static final class TabInfo {
      private final Bundle args;
      private final Class clss;
      private Fragment fragment;
      private final String tag;

      TabInfo(String var1, Class var2, Bundle var3) {
         this.tag = var1;
         this.clss = var2;
         this.args = var3;
      }
   }
}
