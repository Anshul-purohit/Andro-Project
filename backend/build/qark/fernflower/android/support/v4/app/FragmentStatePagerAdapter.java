package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class FragmentStatePagerAdapter extends PagerAdapter {
   private static final boolean DEBUG = false;
   private static final String TAG = "FragmentStatePagerAdapter";
   private FragmentTransaction mCurTransaction = null;
   private Fragment mCurrentPrimaryItem = null;
   private final FragmentManager mFragmentManager;
   private ArrayList mFragments = new ArrayList();
   private ArrayList mSavedState = new ArrayList();

   public FragmentStatePagerAdapter(FragmentManager var1) {
      this.mFragmentManager = var1;
   }

   public void destroyItem(ViewGroup var1, int var2, Object var3) {
      Fragment var4 = (Fragment)var3;
      if (this.mCurTransaction == null) {
         this.mCurTransaction = this.mFragmentManager.beginTransaction();
      }

      while(this.mSavedState.size() <= var2) {
         this.mSavedState.add((Object)null);
      }

      this.mSavedState.set(var2, this.mFragmentManager.saveFragmentInstanceState(var4));
      this.mFragments.set(var2, (Object)null);
      this.mCurTransaction.remove(var4);
   }

   public void finishUpdate(ViewGroup var1) {
      if (this.mCurTransaction != null) {
         this.mCurTransaction.commitAllowingStateLoss();
         this.mCurTransaction = null;
         this.mFragmentManager.executePendingTransactions();
      }

   }

   public abstract Fragment getItem(int var1);

   public Object instantiateItem(ViewGroup var1, int var2) {
      Fragment var3;
      if (this.mFragments.size() > var2) {
         var3 = (Fragment)this.mFragments.get(var2);
         if (var3 != null) {
            return var3;
         }
      }

      if (this.mCurTransaction == null) {
         this.mCurTransaction = this.mFragmentManager.beginTransaction();
      }

      var3 = this.getItem(var2);
      if (this.mSavedState.size() > var2) {
         Fragment.SavedState var4 = (Fragment.SavedState)this.mSavedState.get(var2);
         if (var4 != null) {
            var3.setInitialSavedState(var4);
         }
      }

      while(this.mFragments.size() <= var2) {
         this.mFragments.add((Object)null);
      }

      var3.setMenuVisibility(false);
      var3.setUserVisibleHint(false);
      this.mFragments.set(var2, var3);
      this.mCurTransaction.add(var1.getId(), var3);
      return var3;
   }

   public boolean isViewFromObject(View var1, Object var2) {
      return ((Fragment)var2).getView() == var1;
   }

   public void restoreState(Parcelable var1, ClassLoader var2) {
      if (var1 != null) {
         Bundle var6 = (Bundle)var1;
         var6.setClassLoader(var2);
         Parcelable[] var7 = var6.getParcelableArray("states");
         this.mSavedState.clear();
         this.mFragments.clear();
         int var3;
         if (var7 != null) {
            for(var3 = 0; var3 < var7.length; ++var3) {
               this.mSavedState.add((Fragment.SavedState)var7[var3]);
            }
         }

         Iterator var8 = var6.keySet().iterator();

         while(true) {
            while(true) {
               String var4;
               do {
                  if (!var8.hasNext()) {
                     return;
                  }

                  var4 = (String)var8.next();
               } while(!var4.startsWith("f"));

               var3 = Integer.parseInt(var4.substring(1));
               Fragment var5 = this.mFragmentManager.getFragment(var6, var4);
               if (var5 != null) {
                  while(this.mFragments.size() <= var3) {
                     this.mFragments.add((Object)null);
                  }

                  var5.setMenuVisibility(false);
                  this.mFragments.set(var3, var5);
               } else {
                  Log.w("FragmentStatePagerAdapter", "Bad fragment at key " + var4);
               }
            }
         }
      }
   }

   public Parcelable saveState() {
      Bundle var2 = null;
      if (this.mSavedState.size() > 0) {
         var2 = new Bundle();
         Fragment.SavedState[] var3 = new Fragment.SavedState[this.mSavedState.size()];
         this.mSavedState.toArray(var3);
         var2.putParcelableArray("states", var3);
      }

      Bundle var6;
      for(int var1 = 0; var1 < this.mFragments.size(); var2 = var6) {
         Fragment var4 = (Fragment)this.mFragments.get(var1);
         var6 = var2;
         if (var4 != null) {
            var6 = var2;
            if (var2 == null) {
               var6 = new Bundle();
            }

            String var5 = "f" + var1;
            this.mFragmentManager.putFragment(var6, var5, var4);
         }

         ++var1;
      }

      return var2;
   }

   public void setPrimaryItem(ViewGroup var1, int var2, Object var3) {
      Fragment var4 = (Fragment)var3;
      if (var4 != this.mCurrentPrimaryItem) {
         if (this.mCurrentPrimaryItem != null) {
            this.mCurrentPrimaryItem.setMenuVisibility(false);
            this.mCurrentPrimaryItem.setUserVisibleHint(false);
         }

         if (var4 != null) {
            var4.setMenuVisibility(true);
            var4.setUserVisibleHint(true);
         }

         this.mCurrentPrimaryItem = var4;
      }

   }

   public void startUpdate(ViewGroup var1) {
   }
}
