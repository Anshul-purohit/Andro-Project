package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class FragmentManagerImpl extends FragmentManager {
   static final Interpolator ACCELERATE_CUBIC;
   static final Interpolator ACCELERATE_QUINT;
   static final int ANIM_DUR = 220;
   public static final int ANIM_STYLE_CLOSE_ENTER = 3;
   public static final int ANIM_STYLE_CLOSE_EXIT = 4;
   public static final int ANIM_STYLE_FADE_ENTER = 5;
   public static final int ANIM_STYLE_FADE_EXIT = 6;
   public static final int ANIM_STYLE_OPEN_ENTER = 1;
   public static final int ANIM_STYLE_OPEN_EXIT = 2;
   static boolean DEBUG;
   static final Interpolator DECELERATE_CUBIC;
   static final Interpolator DECELERATE_QUINT;
   static final boolean HONEYCOMB;
   static final String TAG = "FragmentManager";
   static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
   static final String TARGET_STATE_TAG = "android:target_state";
   static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
   static final String VIEW_STATE_TAG = "android:view_state";
   ArrayList mActive;
   FragmentActivity mActivity;
   ArrayList mAdded;
   ArrayList mAvailBackStackIndices;
   ArrayList mAvailIndices;
   ArrayList mBackStack;
   ArrayList mBackStackChangeListeners;
   ArrayList mBackStackIndices;
   FragmentContainer mContainer;
   ArrayList mCreatedMenus;
   int mCurState = 0;
   boolean mDestroyed;
   Runnable mExecCommit = new Runnable() {
      public void run() {
         FragmentManagerImpl.this.execPendingActions();
      }
   };
   boolean mExecutingActions;
   boolean mHavePendingDeferredStart;
   boolean mNeedMenuInvalidate;
   String mNoTransactionsBecause;
   Fragment mParent;
   ArrayList mPendingActions;
   SparseArray mStateArray = null;
   Bundle mStateBundle = null;
   boolean mStateSaved;
   Runnable[] mTmpActions;

   static {
      boolean var0 = false;
      DEBUG = false;
      if (VERSION.SDK_INT >= 11) {
         var0 = true;
      }

      HONEYCOMB = var0;
      DECELERATE_QUINT = new DecelerateInterpolator(2.5F);
      DECELERATE_CUBIC = new DecelerateInterpolator(1.5F);
      ACCELERATE_QUINT = new AccelerateInterpolator(2.5F);
      ACCELERATE_CUBIC = new AccelerateInterpolator(1.5F);
   }

   private void checkStateLoss() {
      if (this.mStateSaved) {
         throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
      } else if (this.mNoTransactionsBecause != null) {
         throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause);
      }
   }

   static Animation makeFadeAnimation(Context var0, float var1, float var2) {
      AlphaAnimation var3 = new AlphaAnimation(var1, var2);
      var3.setInterpolator(DECELERATE_CUBIC);
      var3.setDuration(220L);
      return var3;
   }

   static Animation makeOpenCloseAnimation(Context var0, float var1, float var2, float var3, float var4) {
      AnimationSet var7 = new AnimationSet(false);
      ScaleAnimation var5 = new ScaleAnimation(var1, var2, var1, var2, 1, 0.5F, 1, 0.5F);
      var5.setInterpolator(DECELERATE_QUINT);
      var5.setDuration(220L);
      var7.addAnimation(var5);
      AlphaAnimation var6 = new AlphaAnimation(var3, var4);
      var6.setInterpolator(DECELERATE_CUBIC);
      var6.setDuration(220L);
      var7.addAnimation(var6);
      return var7;
   }

   public static int reverseTransit(int var0) {
      switch(var0) {
      case 4097:
         return 8194;
      case 4099:
         return 4099;
      case 8194:
         return 4097;
      default:
         return 0;
      }
   }

   private void throwException(RuntimeException var1) {
      Log.e("FragmentManager", var1.getMessage());
      Log.e("FragmentManager", "Activity state:");
      PrintWriter var2 = new PrintWriter(new LogWriter("FragmentManager"));
      if (this.mActivity != null) {
         try {
            this.mActivity.dump("  ", (FileDescriptor)null, var2, new String[0]);
         } catch (Exception var4) {
            Log.e("FragmentManager", "Failed dumping state", var4);
         }
      } else {
         try {
            this.dump("  ", (FileDescriptor)null, var2, new String[0]);
         } catch (Exception var3) {
            Log.e("FragmentManager", "Failed dumping state", var3);
         }
      }

      throw var1;
   }

   public static int transitToStyleIndex(int var0, boolean var1) {
      switch(var0) {
      case 4097:
         if (var1) {
            return 1;
         }

         return 2;
      case 4099:
         if (var1) {
            return 5;
         }

         return 6;
      case 8194:
         if (var1) {
            return 3;
         }

         return 4;
      default:
         return -1;
      }
   }

   void addBackStackState(BackStackRecord var1) {
      if (this.mBackStack == null) {
         this.mBackStack = new ArrayList();
      }

      this.mBackStack.add(var1);
      this.reportBackStackChanged();
   }

   public void addFragment(Fragment var1, boolean var2) {
      if (this.mAdded == null) {
         this.mAdded = new ArrayList();
      }

      if (DEBUG) {
         Log.v("FragmentManager", "add: " + var1);
      }

      this.makeActive(var1);
      if (!var1.mDetached) {
         if (this.mAdded.contains(var1)) {
            throw new IllegalStateException("Fragment already added: " + var1);
         }

         this.mAdded.add(var1);
         var1.mAdded = true;
         var1.mRemoving = false;
         if (var1.mHasMenu && var1.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
         }

         if (var2) {
            this.moveToState(var1);
         }
      }

   }

   public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener var1) {
      if (this.mBackStackChangeListeners == null) {
         this.mBackStackChangeListeners = new ArrayList();
      }

      this.mBackStackChangeListeners.add(var1);
   }

   public int allocBackStackIndex(BackStackRecord var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label521: {
         int var2;
         label522: {
            try {
               if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                  break label522;
               }
            } catch (Throwable var58) {
               var10000 = var58;
               var10001 = false;
               break label521;
            }

            try {
               if (this.mBackStackIndices == null) {
                  this.mBackStackIndices = new ArrayList();
               }
            } catch (Throwable var57) {
               var10000 = var57;
               var10001 = false;
               break label521;
            }

            try {
               var2 = this.mBackStackIndices.size();
               if (DEBUG) {
                  Log.v("FragmentManager", "Setting back stack index " + var2 + " to " + var1);
               }
            } catch (Throwable var56) {
               var10000 = var56;
               var10001 = false;
               break label521;
            }

            try {
               this.mBackStackIndices.add(var1);
               return var2;
            } catch (Throwable var55) {
               var10000 = var55;
               var10001 = false;
               break label521;
            }
         }

         try {
            var2 = (Integer)this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1);
            if (DEBUG) {
               Log.v("FragmentManager", "Adding back stack index " + var2 + " with " + var1);
            }
         } catch (Throwable var54) {
            var10000 = var54;
            var10001 = false;
            break label521;
         }

         label497:
         try {
            this.mBackStackIndices.set(var2, var1);
            return var2;
         } catch (Throwable var53) {
            var10000 = var53;
            var10001 = false;
            break label497;
         }
      }

      while(true) {
         Throwable var59 = var10000;

         try {
            throw var59;
         } catch (Throwable var52) {
            var10000 = var52;
            var10001 = false;
            continue;
         }
      }
   }

   public void attachActivity(FragmentActivity var1, FragmentContainer var2, Fragment var3) {
      if (this.mActivity != null) {
         throw new IllegalStateException("Already attached");
      } else {
         this.mActivity = var1;
         this.mContainer = var2;
         this.mParent = var3;
      }
   }

   public void attachFragment(Fragment var1, int var2, int var3) {
      if (DEBUG) {
         Log.v("FragmentManager", "attach: " + var1);
      }

      if (var1.mDetached) {
         var1.mDetached = false;
         if (!var1.mAdded) {
            if (this.mAdded == null) {
               this.mAdded = new ArrayList();
            }

            if (this.mAdded.contains(var1)) {
               throw new IllegalStateException("Fragment already added: " + var1);
            }

            if (DEBUG) {
               Log.v("FragmentManager", "add from attach: " + var1);
            }

            this.mAdded.add(var1);
            var1.mAdded = true;
            if (var1.mHasMenu && var1.mMenuVisible) {
               this.mNeedMenuInvalidate = true;
            }

            this.moveToState(var1, this.mCurState, var2, var3, false);
         }
      }

   }

   public FragmentTransaction beginTransaction() {
      return new BackStackRecord(this);
   }

   public void detachFragment(Fragment var1, int var2, int var3) {
      if (DEBUG) {
         Log.v("FragmentManager", "detach: " + var1);
      }

      if (!var1.mDetached) {
         var1.mDetached = true;
         if (var1.mAdded) {
            if (this.mAdded != null) {
               if (DEBUG) {
                  Log.v("FragmentManager", "remove from detach: " + var1);
               }

               this.mAdded.remove(var1);
            }

            if (var1.mHasMenu && var1.mMenuVisible) {
               this.mNeedMenuInvalidate = true;
            }

            var1.mAdded = false;
            this.moveToState(var1, 1, var2, var3, false);
         }
      }

   }

   public void dispatchActivityCreated() {
      this.mStateSaved = false;
      this.moveToState(2, false);
   }

   public void dispatchConfigurationChanged(Configuration var1) {
      if (this.mAdded != null) {
         for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null) {
               var3.performConfigurationChanged(var1);
            }
         }
      }

   }

   public boolean dispatchContextItemSelected(MenuItem var1) {
      if (this.mAdded != null) {
         for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null && var3.performContextItemSelected(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   public void dispatchCreate() {
      this.mStateSaved = false;
      this.moveToState(1, false);
   }

   public boolean dispatchCreateOptionsMenu(Menu var1, MenuInflater var2) {
      boolean var5 = false;
      boolean var4 = false;
      ArrayList var7 = null;
      ArrayList var6 = null;
      int var3;
      if (this.mAdded != null) {
         var3 = 0;

         while(true) {
            var7 = var6;
            var5 = var4;
            if (var3 >= this.mAdded.size()) {
               break;
            }

            Fragment var8 = (Fragment)this.mAdded.get(var3);
            var7 = var6;
            var5 = var4;
            if (var8 != null) {
               var7 = var6;
               var5 = var4;
               if (var8.performCreateOptionsMenu(var1, var2)) {
                  var5 = true;
                  var7 = var6;
                  if (var6 == null) {
                     var7 = new ArrayList();
                  }

                  var7.add(var8);
               }
            }

            ++var3;
            var6 = var7;
            var4 = var5;
         }
      }

      if (this.mCreatedMenus != null) {
         for(var3 = 0; var3 < this.mCreatedMenus.size(); ++var3) {
            Fragment var9 = (Fragment)this.mCreatedMenus.get(var3);
            if (var7 == null || !var7.contains(var9)) {
               var9.onDestroyOptionsMenu();
            }
         }
      }

      this.mCreatedMenus = var7;
      return var5;
   }

   public void dispatchDestroy() {
      this.mDestroyed = true;
      this.execPendingActions();
      this.moveToState(0, false);
      this.mActivity = null;
      this.mContainer = null;
      this.mParent = null;
   }

   public void dispatchDestroyView() {
      this.moveToState(1, false);
   }

   public void dispatchLowMemory() {
      if (this.mAdded != null) {
         for(int var1 = 0; var1 < this.mAdded.size(); ++var1) {
            Fragment var2 = (Fragment)this.mAdded.get(var1);
            if (var2 != null) {
               var2.performLowMemory();
            }
         }
      }

   }

   public boolean dispatchOptionsItemSelected(MenuItem var1) {
      if (this.mAdded != null) {
         for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null && var3.performOptionsItemSelected(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   public void dispatchOptionsMenuClosed(Menu var1) {
      if (this.mAdded != null) {
         for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null) {
               var3.performOptionsMenuClosed(var1);
            }
         }
      }

   }

   public void dispatchPause() {
      this.moveToState(4, false);
   }

   public boolean dispatchPrepareOptionsMenu(Menu var1) {
      boolean var4 = false;
      boolean var3 = false;
      if (this.mAdded != null) {
         int var2 = 0;

         while(true) {
            var4 = var3;
            if (var2 >= this.mAdded.size()) {
               break;
            }

            Fragment var5 = (Fragment)this.mAdded.get(var2);
            var4 = var3;
            if (var5 != null) {
               var4 = var3;
               if (var5.performPrepareOptionsMenu(var1)) {
                  var4 = true;
               }
            }

            ++var2;
            var3 = var4;
         }
      }

      return var4;
   }

   public void dispatchReallyStop() {
      this.moveToState(2, false);
   }

   public void dispatchResume() {
      this.mStateSaved = false;
      this.moveToState(5, false);
   }

   public void dispatchStart() {
      this.mStateSaved = false;
      this.moveToState(4, false);
   }

   public void dispatchStop() {
      this.mStateSaved = true;
      this.moveToState(3, false);
   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      String var7 = var1 + "    ";
      int var5;
      int var6;
      Fragment var8;
      if (this.mActive != null) {
         var6 = this.mActive.size();
         if (var6 > 0) {
            var3.print(var1);
            var3.print("Active Fragments in ");
            var3.print(Integer.toHexString(System.identityHashCode(this)));
            var3.println(":");

            for(var5 = 0; var5 < var6; ++var5) {
               var8 = (Fragment)this.mActive.get(var5);
               var3.print(var1);
               var3.print("  #");
               var3.print(var5);
               var3.print(": ");
               var3.println(var8);
               if (var8 != null) {
                  var8.dump(var7, var2, var3, var4);
               }
            }
         }
      }

      if (this.mAdded != null) {
         var6 = this.mAdded.size();
         if (var6 > 0) {
            var3.print(var1);
            var3.println("Added Fragments:");

            for(var5 = 0; var5 < var6; ++var5) {
               var8 = (Fragment)this.mAdded.get(var5);
               var3.print(var1);
               var3.print("  #");
               var3.print(var5);
               var3.print(": ");
               var3.println(var8.toString());
            }
         }
      }

      if (this.mCreatedMenus != null) {
         var6 = this.mCreatedMenus.size();
         if (var6 > 0) {
            var3.print(var1);
            var3.println("Fragments Created Menus:");

            for(var5 = 0; var5 < var6; ++var5) {
               var8 = (Fragment)this.mCreatedMenus.get(var5);
               var3.print(var1);
               var3.print("  #");
               var3.print(var5);
               var3.print(": ");
               var3.println(var8.toString());
            }
         }
      }

      if (this.mBackStack != null) {
         var6 = this.mBackStack.size();
         if (var6 > 0) {
            var3.print(var1);
            var3.println("Back Stack:");

            for(var5 = 0; var5 < var6; ++var5) {
               BackStackRecord var54 = (BackStackRecord)this.mBackStack.get(var5);
               var3.print(var1);
               var3.print("  #");
               var3.print(var5);
               var3.print(": ");
               var3.println(var54.toString());
               var54.dump(var7, var2, var3, var4);
            }
         }
      }

      synchronized(this){}

      label1082: {
         Throwable var10000;
         boolean var10001;
         label1083: {
            label1084: {
               try {
                  if (this.mBackStackIndices == null) {
                     break label1084;
                  }

                  var6 = this.mBackStackIndices.size();
               } catch (Throwable var50) {
                  var10000 = var50;
                  var10001 = false;
                  break label1083;
               }

               if (var6 > 0) {
                  try {
                     var3.print(var1);
                     var3.println("Back Stack Indices:");
                  } catch (Throwable var49) {
                     var10000 = var49;
                     var10001 = false;
                     break label1083;
                  }

                  for(var5 = 0; var5 < var6; ++var5) {
                     try {
                        BackStackRecord var52 = (BackStackRecord)this.mBackStackIndices.get(var5);
                        var3.print(var1);
                        var3.print("  #");
                        var3.print(var5);
                        var3.print(": ");
                        var3.println(var52);
                     } catch (Throwable var48) {
                        var10000 = var48;
                        var10001 = false;
                        break label1083;
                     }
                  }
               }
            }

            try {
               if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                  var3.print(var1);
                  var3.print("mAvailBackStackIndices: ");
                  var3.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
               }
            } catch (Throwable var47) {
               var10000 = var47;
               var10001 = false;
               break label1083;
            }

            label1024:
            try {
               break label1082;
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label1024;
            }
         }

         while(true) {
            Throwable var51 = var10000;

            try {
               throw var51;
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               continue;
            }
         }
      }

      if (this.mPendingActions != null) {
         var6 = this.mPendingActions.size();
         if (var6 > 0) {
            var3.print(var1);
            var3.println("Pending Actions:");

            for(var5 = 0; var5 < var6; ++var5) {
               Runnable var53 = (Runnable)this.mPendingActions.get(var5);
               var3.print(var1);
               var3.print("  #");
               var3.print(var5);
               var3.print(": ");
               var3.println(var53);
            }
         }
      }

      var3.print(var1);
      var3.println("FragmentManager misc state:");
      var3.print(var1);
      var3.print("  mActivity=");
      var3.println(this.mActivity);
      var3.print(var1);
      var3.print("  mContainer=");
      var3.println(this.mContainer);
      if (this.mParent != null) {
         var3.print(var1);
         var3.print("  mParent=");
         var3.println(this.mParent);
      }

      var3.print(var1);
      var3.print("  mCurState=");
      var3.print(this.mCurState);
      var3.print(" mStateSaved=");
      var3.print(this.mStateSaved);
      var3.print(" mDestroyed=");
      var3.println(this.mDestroyed);
      if (this.mNeedMenuInvalidate) {
         var3.print(var1);
         var3.print("  mNeedMenuInvalidate=");
         var3.println(this.mNeedMenuInvalidate);
      }

      if (this.mNoTransactionsBecause != null) {
         var3.print(var1);
         var3.print("  mNoTransactionsBecause=");
         var3.println(this.mNoTransactionsBecause);
      }

      if (this.mAvailIndices != null && this.mAvailIndices.size() > 0) {
         var3.print(var1);
         var3.print("  mAvailIndices: ");
         var3.println(Arrays.toString(this.mAvailIndices.toArray()));
      }

   }

   public void enqueueAction(Runnable var1, boolean var2) {
      if (!var2) {
         this.checkStateLoss();
      }

      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label413: {
         label406: {
            try {
               if (!this.mDestroyed && this.mActivity != null) {
                  break label406;
               }
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label413;
            }

            try {
               throw new IllegalStateException("Activity has been destroyed");
            } catch (Throwable var43) {
               var10000 = var43;
               var10001 = false;
               break label413;
            }
         }

         try {
            if (this.mPendingActions == null) {
               this.mPendingActions = new ArrayList();
            }
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label413;
         }

         try {
            this.mPendingActions.add(var1);
            if (this.mPendingActions.size() == 1) {
               this.mActivity.mHandler.removeCallbacks(this.mExecCommit);
               this.mActivity.mHandler.post(this.mExecCommit);
            }
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label413;
         }

         label390:
         try {
            return;
         } catch (Throwable var40) {
            var10000 = var40;
            var10001 = false;
            break label390;
         }
      }

      while(true) {
         Throwable var45 = var10000;

         try {
            throw var45;
         } catch (Throwable var39) {
            var10000 = var39;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean execPendingActions() {
      if (this.mExecutingActions) {
         throw new IllegalStateException("Recursive entry to executePendingTransactions");
      } else if (Looper.myLooper() != this.mActivity.mHandler.getLooper()) {
         throw new IllegalStateException("Must be called from main thread of process");
      } else {
         boolean var4 = false;

         int var1;
         while(true) {
            synchronized(this){}

            Throwable var10000;
            boolean var10001;
            label717: {
               label708: {
                  try {
                     if (this.mPendingActions != null && this.mPendingActions.size() != 0) {
                        break label708;
                     }
                  } catch (Throwable var47) {
                     var10000 = var47;
                     var10001 = false;
                     break label717;
                  }

                  try {
                     break;
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label717;
                  }
               }

               int var48;
               label697: {
                  try {
                     var48 = this.mPendingActions.size();
                     if (this.mTmpActions != null && this.mTmpActions.length >= var48) {
                        break label697;
                     }
                  } catch (Throwable var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label717;
                  }

                  try {
                     this.mTmpActions = new Runnable[var48];
                  } catch (Throwable var44) {
                     var10000 = var44;
                     var10001 = false;
                     break label717;
                  }
               }

               try {
                  this.mPendingActions.toArray(this.mTmpActions);
                  this.mPendingActions.clear();
                  this.mActivity.mHandler.removeCallbacks(this.mExecCommit);
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label717;
               }

               this.mExecutingActions = true;

               for(var1 = 0; var1 < var48; ++var1) {
                  this.mTmpActions[var1].run();
                  this.mTmpActions[var1] = null;
               }

               this.mExecutingActions = false;
               var4 = true;
               continue;
            }

            while(true) {
               Throwable var49 = var10000;

               try {
                  throw var49;
               } catch (Throwable var42) {
                  var10000 = var42;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (this.mHavePendingDeferredStart) {
            boolean var2 = false;

            boolean var3;
            for(var1 = 0; var1 < this.mActive.size(); var2 = var3) {
               Fragment var5 = (Fragment)this.mActive.get(var1);
               var3 = var2;
               if (var5 != null) {
                  var3 = var2;
                  if (var5.mLoaderManager != null) {
                     var3 = var2 | var5.mLoaderManager.hasRunningLoaders();
                  }
               }

               ++var1;
            }

            if (!var2) {
               this.mHavePendingDeferredStart = false;
               this.startPendingDeferredFragments();
            }
         }

         return var4;
      }
   }

   public boolean executePendingTransactions() {
      return this.execPendingActions();
   }

   public Fragment findFragmentById(int var1) {
      int var2;
      Fragment var3;
      if (this.mAdded != null) {
         for(var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
            var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null && var3.mFragmentId == var1) {
               return var3;
            }
         }
      }

      if (this.mActive != null) {
         for(var2 = this.mActive.size() - 1; var2 >= 0; --var2) {
            Fragment var4 = (Fragment)this.mActive.get(var2);
            if (var4 != null) {
               var3 = var4;
               if (var4.mFragmentId == var1) {
                  return var3;
               }
            }
         }
      }

      return null;
   }

   public Fragment findFragmentByTag(String var1) {
      int var2;
      Fragment var3;
      if (this.mAdded != null && var1 != null) {
         for(var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
            var3 = (Fragment)this.mAdded.get(var2);
            if (var3 != null && var1.equals(var3.mTag)) {
               return var3;
            }
         }
      }

      if (this.mActive == null || var1 == null) {
         return null;
      } else {
         for(var2 = this.mActive.size() - 1; var2 >= 0; --var2) {
            Fragment var4 = (Fragment)this.mActive.get(var2);
            if (var4 != null) {
               var3 = var4;
               if (var1.equals(var4.mTag)) {
                  return var3;
               }
            }
         }

         return null;
      }
   }

   public Fragment findFragmentByWho(String var1) {
      if (this.mActive != null && var1 != null) {
         for(int var2 = this.mActive.size() - 1; var2 >= 0; --var2) {
            Fragment var3 = (Fragment)this.mActive.get(var2);
            if (var3 != null) {
               var3 = var3.findFragmentByWho(var1);
               if (var3 != null) {
                  return var3;
               }
            }
         }
      }

      return null;
   }

   public void freeBackStackIndex(int var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label196: {
         try {
            this.mBackStackIndices.set(var1, (Object)null);
            if (this.mAvailBackStackIndices == null) {
               this.mAvailBackStackIndices = new ArrayList();
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label196;
         }

         try {
            if (DEBUG) {
               Log.v("FragmentManager", "Freeing back stack index " + var1);
            }
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label196;
         }

         label186:
         try {
            this.mAvailBackStackIndices.add(var1);
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label186;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   public FragmentManager.BackStackEntry getBackStackEntryAt(int var1) {
      return (FragmentManager.BackStackEntry)this.mBackStack.get(var1);
   }

   public int getBackStackEntryCount() {
      return this.mBackStack != null ? this.mBackStack.size() : 0;
   }

   public Fragment getFragment(Bundle var1, String var2) {
      int var3 = var1.getInt(var2, -1);
      Fragment var5;
      if (var3 == -1) {
         var5 = null;
      } else {
         if (var3 >= this.mActive.size()) {
            this.throwException(new IllegalStateException("Fragement no longer exists for key " + var2 + ": index " + var3));
         }

         Fragment var4 = (Fragment)this.mActive.get(var3);
         var5 = var4;
         if (var4 == null) {
            this.throwException(new IllegalStateException("Fragement no longer exists for key " + var2 + ": index " + var3));
            return var4;
         }
      }

      return var5;
   }

   public List getFragments() {
      return this.mActive;
   }

   public void hideFragment(Fragment var1, int var2, int var3) {
      if (DEBUG) {
         Log.v("FragmentManager", "hide: " + var1);
      }

      if (!var1.mHidden) {
         var1.mHidden = true;
         if (var1.mView != null) {
            Animation var4 = this.loadAnimation(var1, var2, false, var3);
            if (var4 != null) {
               var1.mView.startAnimation(var4);
            }

            var1.mView.setVisibility(8);
         }

         if (var1.mAdded && var1.mHasMenu && var1.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
         }

         var1.onHiddenChanged(true);
      }

   }

   public boolean isDestroyed() {
      return this.mDestroyed;
   }

   Animation loadAnimation(Fragment var1, int var2, boolean var3, int var4) {
      Animation var5 = var1.onCreateAnimation(var2, var3, var1.mNextAnim);
      if (var5 != null) {
         return var5;
      } else {
         if (var1.mNextAnim != 0) {
            Animation var6 = AnimationUtils.loadAnimation(this.mActivity, var1.mNextAnim);
            if (var6 != null) {
               return var6;
            }
         }

         if (var2 == 0) {
            return null;
         } else {
            var2 = transitToStyleIndex(var2, var3);
            if (var2 < 0) {
               return null;
            } else {
               switch(var2) {
               case 1:
                  return makeOpenCloseAnimation(this.mActivity, 1.125F, 1.0F, 0.0F, 1.0F);
               case 2:
                  return makeOpenCloseAnimation(this.mActivity, 1.0F, 0.975F, 1.0F, 0.0F);
               case 3:
                  return makeOpenCloseAnimation(this.mActivity, 0.975F, 1.0F, 0.0F, 1.0F);
               case 4:
                  return makeOpenCloseAnimation(this.mActivity, 1.0F, 1.075F, 1.0F, 0.0F);
               case 5:
                  return makeFadeAnimation(this.mActivity, 0.0F, 1.0F);
               case 6:
                  return makeFadeAnimation(this.mActivity, 1.0F, 0.0F);
               default:
                  var2 = var4;
                  if (var4 == 0) {
                     var2 = var4;
                     if (this.mActivity.getWindow() != null) {
                        var2 = this.mActivity.getWindow().getAttributes().windowAnimations;
                     }
                  }

                  return var2 == 0 ? null : null;
               }
            }
         }
      }
   }

   void makeActive(Fragment var1) {
      if (var1.mIndex < 0) {
         if (this.mAvailIndices != null && this.mAvailIndices.size() > 0) {
            var1.setIndex((Integer)this.mAvailIndices.remove(this.mAvailIndices.size() - 1), this.mParent);
            this.mActive.set(var1.mIndex, var1);
         } else {
            if (this.mActive == null) {
               this.mActive = new ArrayList();
            }

            var1.setIndex(this.mActive.size(), this.mParent);
            this.mActive.add(var1);
         }

         if (DEBUG) {
            Log.v("FragmentManager", "Allocated fragment index " + var1);
            return;
         }
      }

   }

   void makeInactive(Fragment var1) {
      if (var1.mIndex >= 0) {
         if (DEBUG) {
            Log.v("FragmentManager", "Freeing fragment index " + var1);
         }

         this.mActive.set(var1.mIndex, (Object)null);
         if (this.mAvailIndices == null) {
            this.mAvailIndices = new ArrayList();
         }

         this.mAvailIndices.add(var1.mIndex);
         this.mActivity.invalidateSupportFragment(var1.mWho);
         var1.initState();
      }
   }

   void moveToState(int var1, int var2, int var3, boolean var4) {
      if (this.mActivity == null && var1 != 0) {
         throw new IllegalStateException("No activity");
      } else {
         if (var4 || this.mCurState != var1) {
            this.mCurState = var1;
            if (this.mActive != null) {
               boolean var6 = false;

               boolean var7;
               for(int var5 = 0; var5 < this.mActive.size(); var6 = var7) {
                  Fragment var8 = (Fragment)this.mActive.get(var5);
                  var7 = var6;
                  if (var8 != null) {
                     this.moveToState(var8, var1, var2, var3, false);
                     var7 = var6;
                     if (var8.mLoaderManager != null) {
                        var7 = var6 | var8.mLoaderManager.hasRunningLoaders();
                     }
                  }

                  ++var5;
               }

               if (!var6) {
                  this.startPendingDeferredFragments();
               }

               if (this.mNeedMenuInvalidate && this.mActivity != null && this.mCurState == 5) {
                  this.mActivity.supportInvalidateOptionsMenu();
                  this.mNeedMenuInvalidate = false;
                  return;
               }
            }
         }

      }
   }

   void moveToState(int var1, boolean var2) {
      this.moveToState(var1, 0, 0, var2);
   }

   void moveToState(Fragment var1) {
      this.moveToState(var1, this.mCurState, 0, 0, false);
   }

   void moveToState(final Fragment var1, int var2, int var3, int var4, boolean var5) {
      int var6;
      label224: {
         if (var1.mAdded) {
            var6 = var2;
            if (!var1.mDetached) {
               break label224;
            }
         }

         var6 = var2;
         if (var2 > 1) {
            var6 = 1;
         }
      }

      int var7 = var6;
      if (var1.mRemoving) {
         var7 = var6;
         if (var6 > var1.mState) {
            var7 = var1.mState;
         }
      }

      var2 = var7;
      if (var1.mDeferStart) {
         var2 = var7;
         if (var1.mState < 4) {
            var2 = var7;
            if (var7 > 3) {
               var2 = 3;
            }
         }
      }

      ViewGroup var10;
      if (var1.mState < var2) {
         if (var1.mFromLayout && !var1.mInLayout) {
            return;
         }

         if (var1.mAnimatingAway != null) {
            var1.mAnimatingAway = null;
            this.moveToState(var1, var1.mStateAfterAnimating, 0, 0, true);
         }

         var6 = var2;
         int var8 = var2;
         var7 = var2;
         switch(var1.mState) {
         case 0:
            if (DEBUG) {
               Log.v("FragmentManager", "moveto CREATED: " + var1);
            }

            var7 = var2;
            if (var1.mSavedFragmentState != null) {
               var1.mSavedViewState = var1.mSavedFragmentState.getSparseParcelableArray("android:view_state");
               var1.mTarget = this.getFragment(var1.mSavedFragmentState, "android:target_state");
               if (var1.mTarget != null) {
                  var1.mTargetRequestCode = var1.mSavedFragmentState.getInt("android:target_req_state", 0);
               }

               var1.mUserVisibleHint = var1.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
               var7 = var2;
               if (!var1.mUserVisibleHint) {
                  var1.mDeferStart = true;
                  var7 = var2;
                  if (var2 > 3) {
                     var7 = 3;
                  }
               }
            }

            var1.mActivity = this.mActivity;
            var1.mParentFragment = this.mParent;
            FragmentManagerImpl var9;
            if (this.mParent != null) {
               var9 = this.mParent.mChildFragmentManager;
            } else {
               var9 = this.mActivity.mFragments;
            }

            var1.mFragmentManager = var9;
            var1.mCalled = false;
            var1.onAttach(this.mActivity);
            if (!var1.mCalled) {
               throw new SuperNotCalledException("Fragment " + var1 + " did not call through to super.onAttach()");
            }

            if (var1.mParentFragment == null) {
               this.mActivity.onAttachFragment(var1);
            }

            if (!var1.mRetaining) {
               var1.performCreate(var1.mSavedFragmentState);
            }

            var1.mRetaining = false;
            var6 = var7;
            if (var1.mFromLayout) {
               var1.mView = var1.performCreateView(var1.getLayoutInflater(var1.mSavedFragmentState), (ViewGroup)null, var1.mSavedFragmentState);
               if (var1.mView != null) {
                  var1.mInnerView = var1.mView;
                  var1.mView = NoSaveStateFrameLayout.wrap(var1.mView);
                  if (var1.mHidden) {
                     var1.mView.setVisibility(8);
                  }

                  var1.onViewCreated(var1.mView, var1.mSavedFragmentState);
                  var6 = var7;
               } else {
                  var1.mInnerView = null;
                  var6 = var7;
               }
            }
         case 1:
            var8 = var6;
            if (var6 > 1) {
               if (DEBUG) {
                  Log.v("FragmentManager", "moveto ACTIVITY_CREATED: " + var1);
               }

               if (!var1.mFromLayout) {
                  ViewGroup var11 = null;
                  if (var1.mContainerId != 0) {
                     var10 = (ViewGroup)this.mContainer.findViewById(var1.mContainerId);
                     var11 = var10;
                     if (var10 == null) {
                        var11 = var10;
                        if (!var1.mRestored) {
                           this.throwException(new IllegalArgumentException("No view found for id 0x" + Integer.toHexString(var1.mContainerId) + " (" + var1.getResources().getResourceName(var1.mContainerId) + ") for fragment " + var1));
                           var11 = var10;
                        }
                     }
                  }

                  var1.mContainer = var11;
                  var1.mView = var1.performCreateView(var1.getLayoutInflater(var1.mSavedFragmentState), var11, var1.mSavedFragmentState);
                  if (var1.mView != null) {
                     var1.mInnerView = var1.mView;
                     var1.mView = NoSaveStateFrameLayout.wrap(var1.mView);
                     if (var11 != null) {
                        Animation var12 = this.loadAnimation(var1, var3, true, var4);
                        if (var12 != null) {
                           var1.mView.startAnimation(var12);
                        }

                        var11.addView(var1.mView);
                     }

                     if (var1.mHidden) {
                        var1.mView.setVisibility(8);
                     }

                     var1.onViewCreated(var1.mView, var1.mSavedFragmentState);
                  } else {
                     var1.mInnerView = null;
                  }
               }

               var1.performActivityCreated(var1.mSavedFragmentState);
               if (var1.mView != null) {
                  var1.restoreViewState(var1.mSavedFragmentState);
               }

               var1.mSavedFragmentState = null;
               var8 = var6;
            }
         case 2:
         case 3:
            var7 = var8;
            if (var8 > 3) {
               if (DEBUG) {
                  Log.v("FragmentManager", "moveto STARTED: " + var1);
               }

               var1.performStart();
               var7 = var8;
            }
         case 4:
            var6 = var7;
            if (var7 > 4) {
               if (DEBUG) {
                  Log.v("FragmentManager", "moveto RESUMED: " + var1);
               }

               var1.mResumed = true;
               var1.performResume();
               var1.mSavedFragmentState = null;
               var1.mSavedViewState = null;
               var6 = var7;
            }
            break;
         default:
            var6 = var2;
         }
      } else {
         var6 = var2;
         if (var1.mState > var2) {
            switch(var1.mState) {
            case 5:
               if (var2 < 5) {
                  if (DEBUG) {
                     Log.v("FragmentManager", "movefrom RESUMED: " + var1);
                  }

                  var1.performPause();
                  var1.mResumed = false;
               }
            case 4:
               if (var2 < 4) {
                  if (DEBUG) {
                     Log.v("FragmentManager", "movefrom STARTED: " + var1);
                  }

                  var1.performStop();
               }
            case 3:
               if (var2 < 3) {
                  if (DEBUG) {
                     Log.v("FragmentManager", "movefrom STOPPED: " + var1);
                  }

                  var1.performReallyStop();
               }
            case 2:
               if (var2 < 2) {
                  if (DEBUG) {
                     Log.v("FragmentManager", "movefrom ACTIVITY_CREATED: " + var1);
                  }

                  if (var1.mView != null && !this.mActivity.isFinishing() && var1.mSavedViewState == null) {
                     this.saveFragmentViewState(var1);
                  }

                  var1.performDestroyView();
                  if (var1.mView != null && var1.mContainer != null) {
                     var10 = null;
                     Animation var13 = var10;
                     if (this.mCurState > 0) {
                        var13 = var10;
                        if (!this.mDestroyed) {
                           var13 = this.loadAnimation(var1, var3, false, var4);
                        }
                     }

                     if (var13 != null) {
                        var1.mAnimatingAway = var1.mView;
                        var1.mStateAfterAnimating = var2;
                        var13.setAnimationListener(new AnimationListener() {
                           public void onAnimationEnd(Animation var1x) {
                              if (var1.mAnimatingAway != null) {
                                 var1.mAnimatingAway = null;
                                 FragmentManagerImpl.this.moveToState(var1, var1.mStateAfterAnimating, 0, 0, false);
                              }

                           }

                           public void onAnimationRepeat(Animation var1x) {
                           }

                           public void onAnimationStart(Animation var1x) {
                           }
                        });
                        var1.mView.startAnimation(var13);
                     }

                     var1.mContainer.removeView(var1.mView);
                  }

                  var1.mContainer = null;
                  var1.mView = null;
                  var1.mInnerView = null;
               }
            case 1:
               var6 = var2;
               if (var2 < 1) {
                  if (this.mDestroyed && var1.mAnimatingAway != null) {
                     View var14 = var1.mAnimatingAway;
                     var1.mAnimatingAway = null;
                     var14.clearAnimation();
                  }

                  if (var1.mAnimatingAway != null) {
                     var1.mStateAfterAnimating = var2;
                     var6 = 1;
                  } else {
                     if (DEBUG) {
                        Log.v("FragmentManager", "movefrom CREATED: " + var1);
                     }

                     if (!var1.mRetaining) {
                        var1.performDestroy();
                     }

                     var1.mCalled = false;
                     var1.onDetach();
                     if (!var1.mCalled) {
                        throw new SuperNotCalledException("Fragment " + var1 + " did not call through to super.onDetach()");
                     }

                     var6 = var2;
                     if (!var5) {
                        if (!var1.mRetaining) {
                           this.makeInactive(var1);
                           var6 = var2;
                        } else {
                           var1.mActivity = null;
                           var1.mFragmentManager = null;
                           var6 = var2;
                        }
                     }
                  }
               }
               break;
            default:
               var6 = var2;
            }
         }
      }

      var1.mState = var6;
   }

   public void noteStateNotSaved() {
      this.mStateSaved = false;
   }

   public void performPendingDeferredStart(Fragment var1) {
      if (var1.mDeferStart) {
         if (!this.mExecutingActions) {
            var1.mDeferStart = false;
            this.moveToState(var1, this.mCurState, 0, 0, false);
            return;
         }

         this.mHavePendingDeferredStart = true;
      }

   }

   public void popBackStack() {
      this.enqueueAction(new Runnable() {
         public void run() {
            FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mActivity.mHandler, (String)null, -1, 0);
         }
      }, false);
   }

   public void popBackStack(final int var1, final int var2) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Bad id: " + var1);
      } else {
         this.enqueueAction(new Runnable() {
            public void run() {
               FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mActivity.mHandler, (String)null, var1, var2);
            }
         }, false);
      }
   }

   public void popBackStack(final String var1, final int var2) {
      this.enqueueAction(new Runnable() {
         public void run() {
            FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mActivity.mHandler, var1, -1, var2);
         }
      }, false);
   }

   public boolean popBackStackImmediate() {
      this.checkStateLoss();
      this.executePendingTransactions();
      return this.popBackStackState(this.mActivity.mHandler, (String)null, -1, 0);
   }

   public boolean popBackStackImmediate(int var1, int var2) {
      this.checkStateLoss();
      this.executePendingTransactions();
      if (var1 < 0) {
         throw new IllegalArgumentException("Bad id: " + var1);
      } else {
         return this.popBackStackState(this.mActivity.mHandler, (String)null, var1, var2);
      }
   }

   public boolean popBackStackImmediate(String var1, int var2) {
      this.checkStateLoss();
      this.executePendingTransactions();
      return this.popBackStackState(this.mActivity.mHandler, var1, -1, var2);
   }

   boolean popBackStackState(Handler var1, String var2, int var3, int var4) {
      if (this.mBackStack != null) {
         if (var2 == null && var3 < 0 && (var4 & 1) == 0) {
            var3 = this.mBackStack.size() - 1;
            if (var3 < 0) {
               return false;
            }

            ((BackStackRecord)this.mBackStack.remove(var3)).popFromBackStack(true);
            this.reportBackStackChanged();
         } else {
            int var5 = -1;
            if (var2 != null || var3 >= 0) {
               int var6;
               BackStackRecord var8;
               for(var6 = this.mBackStack.size() - 1; var6 >= 0; --var6) {
                  var8 = (BackStackRecord)this.mBackStack.get(var6);
                  if (var2 != null && var2.equals(var8.getName()) || var3 >= 0 && var3 == var8.mIndex) {
                     break;
                  }
               }

               if (var6 < 0) {
                  return false;
               }

               var5 = var6;
               if ((var4 & 1) != 0) {
                  var4 = var6 - 1;

                  while(true) {
                     var5 = var4;
                     if (var4 < 0) {
                        break;
                     }

                     var8 = (BackStackRecord)this.mBackStack.get(var4);
                     if (var2 == null || !var2.equals(var8.getName())) {
                        var5 = var4;
                        if (var3 < 0) {
                           break;
                        }

                        var5 = var4;
                        if (var3 != var8.mIndex) {
                           break;
                        }
                     }

                     --var4;
                  }
               }
            }

            if (var5 == this.mBackStack.size() - 1) {
               return false;
            }

            ArrayList var9 = new ArrayList();

            for(var3 = this.mBackStack.size() - 1; var3 > var5; --var3) {
               var9.add(this.mBackStack.remove(var3));
            }

            var4 = var9.size() - 1;

            for(var3 = 0; var3 <= var4; ++var3) {
               if (DEBUG) {
                  Log.v("FragmentManager", "Popping back stack state: " + var9.get(var3));
               }

               BackStackRecord var10 = (BackStackRecord)var9.get(var3);
               boolean var7;
               if (var3 == var4) {
                  var7 = true;
               } else {
                  var7 = false;
               }

               var10.popFromBackStack(var7);
            }

            this.reportBackStackChanged();
         }

         return true;
      } else {
         return false;
      }
   }

   public void putFragment(Bundle var1, String var2, Fragment var3) {
      if (var3.mIndex < 0) {
         this.throwException(new IllegalStateException("Fragment " + var3 + " is not currently in the FragmentManager"));
      }

      var1.putInt(var2, var3.mIndex);
   }

   public void removeFragment(Fragment var1, int var2, int var3) {
      if (DEBUG) {
         Log.v("FragmentManager", "remove: " + var1 + " nesting=" + var1.mBackStackNesting);
      }

      boolean var4;
      if (!var1.isInBackStack()) {
         var4 = true;
      } else {
         var4 = false;
      }

      if (!var1.mDetached || var4) {
         if (this.mAdded != null) {
            this.mAdded.remove(var1);
         }

         if (var1.mHasMenu && var1.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
         }

         var1.mAdded = false;
         var1.mRemoving = true;
         byte var5;
         if (var4) {
            var5 = 0;
         } else {
            var5 = 1;
         }

         this.moveToState(var1, var5, var2, var3, false);
      }

   }

   public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener var1) {
      if (this.mBackStackChangeListeners != null) {
         this.mBackStackChangeListeners.remove(var1);
      }

   }

   void reportBackStackChanged() {
      if (this.mBackStackChangeListeners != null) {
         for(int var1 = 0; var1 < this.mBackStackChangeListeners.size(); ++var1) {
            ((FragmentManager.OnBackStackChangedListener)this.mBackStackChangeListeners.get(var1)).onBackStackChanged();
         }
      }

   }

   void restoreAllState(Parcelable var1, ArrayList var2) {
      if (var1 != null) {
         FragmentManagerState var6 = (FragmentManagerState)var1;
         if (var6.mActive != null) {
            int var3;
            Fragment var4;
            if (var2 != null) {
               for(var3 = 0; var3 < var2.size(); ++var3) {
                  var4 = (Fragment)var2.get(var3);
                  if (DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: re-attaching retained " + var4);
                  }

                  FragmentState var5 = var6.mActive[var4.mIndex];
                  var5.mInstance = var4;
                  var4.mSavedViewState = null;
                  var4.mBackStackNesting = 0;
                  var4.mInLayout = false;
                  var4.mAdded = false;
                  var4.mTarget = null;
                  if (var5.mSavedFragmentState != null) {
                     var5.mSavedFragmentState.setClassLoader(this.mActivity.getClassLoader());
                     var4.mSavedViewState = var5.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                  }
               }
            }

            this.mActive = new ArrayList(var6.mActive.length);
            if (this.mAvailIndices != null) {
               this.mAvailIndices.clear();
            }

            for(var3 = 0; var3 < var6.mActive.length; ++var3) {
               FragmentState var9 = var6.mActive[var3];
               if (var9 != null) {
                  Fragment var10 = var9.instantiate(this.mActivity, this.mParent);
                  if (DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: active #" + var3 + ": " + var10);
                  }

                  this.mActive.add(var10);
                  var9.mInstance = null;
               } else {
                  this.mActive.add((Object)null);
                  if (this.mAvailIndices == null) {
                     this.mAvailIndices = new ArrayList();
                  }

                  if (DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: avail #" + var3);
                  }

                  this.mAvailIndices.add(var3);
               }
            }

            if (var2 != null) {
               for(var3 = 0; var3 < var2.size(); ++var3) {
                  var4 = (Fragment)var2.get(var3);
                  if (var4.mTargetIndex >= 0) {
                     if (var4.mTargetIndex < this.mActive.size()) {
                        var4.mTarget = (Fragment)this.mActive.get(var4.mTargetIndex);
                     } else {
                        Log.w("FragmentManager", "Re-attaching retained fragment " + var4 + " target no longer exists: " + var4.mTargetIndex);
                        var4.mTarget = null;
                     }
                  }
               }
            }

            if (var6.mAdded != null) {
               this.mAdded = new ArrayList(var6.mAdded.length);

               for(var3 = 0; var3 < var6.mAdded.length; ++var3) {
                  Fragment var7 = (Fragment)this.mActive.get(var6.mAdded[var3]);
                  if (var7 == null) {
                     this.throwException(new IllegalStateException("No instantiated fragment for index #" + var6.mAdded[var3]));
                  }

                  var7.mAdded = true;
                  if (DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: added #" + var3 + ": " + var7);
                  }

                  if (this.mAdded.contains(var7)) {
                     throw new IllegalStateException("Already added!");
                  }

                  this.mAdded.add(var7);
               }
            } else {
               this.mAdded = null;
            }

            if (var6.mBackStack == null) {
               this.mBackStack = null;
               return;
            }

            this.mBackStack = new ArrayList(var6.mBackStack.length);

            for(var3 = 0; var3 < var6.mBackStack.length; ++var3) {
               BackStackRecord var8 = var6.mBackStack[var3].instantiate(this);
               if (DEBUG) {
                  Log.v("FragmentManager", "restoreAllState: back stack #" + var3 + " (index " + var8.mIndex + "): " + var8);
                  var8.dump("  ", new PrintWriter(new LogWriter("FragmentManager")), false);
               }

               this.mBackStack.add(var8);
               if (var8.mIndex >= 0) {
                  this.setBackStackIndex(var8.mIndex, var8);
               }
            }
         }
      }

   }

   ArrayList retainNonConfig() {
      ArrayList var4 = null;
      ArrayList var3 = null;
      if (this.mActive != null) {
         int var1 = 0;

         while(true) {
            var4 = var3;
            if (var1 >= this.mActive.size()) {
               break;
            }

            Fragment var6 = (Fragment)this.mActive.get(var1);
            ArrayList var5 = var3;
            if (var6 != null) {
               var5 = var3;
               if (var6.mRetainInstance) {
                  var4 = var3;
                  if (var3 == null) {
                     var4 = new ArrayList();
                  }

                  var4.add(var6);
                  var6.mRetaining = true;
                  int var2;
                  if (var6.mTarget != null) {
                     var2 = var6.mTarget.mIndex;
                  } else {
                     var2 = -1;
                  }

                  var6.mTargetIndex = var2;
                  var5 = var4;
                  if (DEBUG) {
                     Log.v("FragmentManager", "retainNonConfig: keeping retained " + var6);
                     var5 = var4;
                  }
               }
            }

            ++var1;
            var3 = var5;
         }
      }

      return var4;
   }

   Parcelable saveAllState() {
      this.execPendingActions();
      if (HONEYCOMB) {
         this.mStateSaved = true;
      }

      if (this.mActive != null && this.mActive.size() > 0) {
         int var4 = this.mActive.size();
         FragmentState[] var8 = new FragmentState[var4];
         boolean var2 = false;

         int var1;
         FragmentState var6;
         for(var1 = 0; var1 < var4; ++var1) {
            Fragment var5 = (Fragment)this.mActive.get(var1);
            if (var5 != null) {
               if (var5.mIndex < 0) {
                  this.throwException(new IllegalStateException("Failure saving state: active " + var5 + " has cleared index: " + var5.mIndex));
               }

               boolean var3 = true;
               var6 = new FragmentState(var5);
               var8[var1] = var6;
               if (var5.mState > 0 && var6.mSavedFragmentState == null) {
                  var6.mSavedFragmentState = this.saveFragmentBasicState(var5);
                  if (var5.mTarget != null) {
                     if (var5.mTarget.mIndex < 0) {
                        this.throwException(new IllegalStateException("Failure saving state: " + var5 + " has target not in fragment manager: " + var5.mTarget));
                     }

                     if (var6.mSavedFragmentState == null) {
                        var6.mSavedFragmentState = new Bundle();
                     }

                     this.putFragment(var6.mSavedFragmentState, "android:target_state", var5.mTarget);
                     if (var5.mTargetRequestCode != 0) {
                        var6.mSavedFragmentState.putInt("android:target_req_state", var5.mTargetRequestCode);
                     }
                  }
               } else {
                  var6.mSavedFragmentState = var5.mSavedFragmentState;
               }

               var2 = var3;
               if (DEBUG) {
                  Log.v("FragmentManager", "Saved state of " + var5 + ": " + var6.mSavedFragmentState);
                  var2 = var3;
               }
            }
         }

         if (var2) {
            var6 = null;
            BackStackState[] var7 = null;
            int[] var10 = (int[])var6;
            int var9;
            if (this.mAdded != null) {
               var9 = this.mAdded.size();
               var10 = (int[])var6;
               if (var9 > 0) {
                  int[] var11 = new int[var9];
                  var1 = 0;

                  while(true) {
                     var10 = var11;
                     if (var1 >= var9) {
                        break;
                     }

                     var11[var1] = ((Fragment)this.mAdded.get(var1)).mIndex;
                     if (var11[var1] < 0) {
                        this.throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(var1) + " has cleared index: " + var11[var1]));
                     }

                     if (DEBUG) {
                        Log.v("FragmentManager", "saveAllState: adding fragment #" + var1 + ": " + this.mAdded.get(var1));
                     }

                     ++var1;
                  }
               }
            }

            BackStackState[] var12 = var7;
            if (this.mBackStack != null) {
               var9 = this.mBackStack.size();
               var12 = var7;
               if (var9 > 0) {
                  var7 = new BackStackState[var9];
                  var1 = 0;

                  while(true) {
                     var12 = var7;
                     if (var1 >= var9) {
                        break;
                     }

                     var7[var1] = new BackStackState(this, (BackStackRecord)this.mBackStack.get(var1));
                     if (DEBUG) {
                        Log.v("FragmentManager", "saveAllState: adding back stack #" + var1 + ": " + this.mBackStack.get(var1));
                     }

                     ++var1;
                  }
               }
            }

            FragmentManagerState var13 = new FragmentManagerState();
            var13.mActive = var8;
            var13.mAdded = var10;
            var13.mBackStack = var12;
            return var13;
         }

         if (DEBUG) {
            Log.v("FragmentManager", "saveAllState: no fragments!");
            return null;
         }
      }

      return null;
   }

   Bundle saveFragmentBasicState(Fragment var1) {
      Bundle var3 = null;
      if (this.mStateBundle == null) {
         this.mStateBundle = new Bundle();
      }

      var1.performSaveInstanceState(this.mStateBundle);
      if (!this.mStateBundle.isEmpty()) {
         var3 = this.mStateBundle;
         this.mStateBundle = null;
      }

      if (var1.mView != null) {
         this.saveFragmentViewState(var1);
      }

      Bundle var2 = var3;
      if (var1.mSavedViewState != null) {
         var2 = var3;
         if (var3 == null) {
            var2 = new Bundle();
         }

         var2.putSparseParcelableArray("android:view_state", var1.mSavedViewState);
      }

      var3 = var2;
      if (!var1.mUserVisibleHint) {
         var3 = var2;
         if (var2 == null) {
            var3 = new Bundle();
         }

         var3.putBoolean("android:user_visible_hint", var1.mUserVisibleHint);
      }

      return var3;
   }

   public Fragment.SavedState saveFragmentInstanceState(Fragment var1) {
      Object var3 = null;
      if (var1.mIndex < 0) {
         this.throwException(new IllegalStateException("Fragment " + var1 + " is not currently in the FragmentManager"));
      }

      Fragment.SavedState var2 = (Fragment.SavedState)var3;
      if (var1.mState > 0) {
         Bundle var4 = this.saveFragmentBasicState(var1);
         var2 = (Fragment.SavedState)var3;
         if (var4 != null) {
            var2 = new Fragment.SavedState(var4);
         }
      }

      return var2;
   }

   void saveFragmentViewState(Fragment var1) {
      if (var1.mInnerView != null) {
         if (this.mStateArray == null) {
            this.mStateArray = new SparseArray();
         } else {
            this.mStateArray.clear();
         }

         var1.mInnerView.saveHierarchyState(this.mStateArray);
         if (this.mStateArray.size() > 0) {
            var1.mSavedViewState = this.mStateArray;
            this.mStateArray = null;
            return;
         }
      }

   }

   public void setBackStackIndex(int var1, BackStackRecord var2) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label1119: {
         try {
            if (this.mBackStackIndices == null) {
               this.mBackStackIndices = new ArrayList();
            }
         } catch (Throwable var136) {
            var10000 = var136;
            var10001 = false;
            break label1119;
         }

         int var4;
         try {
            var4 = this.mBackStackIndices.size();
         } catch (Throwable var135) {
            var10000 = var135;
            var10001 = false;
            break label1119;
         }

         int var3 = var4;
         if (var1 < var4) {
            try {
               if (DEBUG) {
                  Log.v("FragmentManager", "Setting back stack index " + var1 + " to " + var2);
               }
            } catch (Throwable var132) {
               var10000 = var132;
               var10001 = false;
               break label1119;
            }

            try {
               this.mBackStackIndices.set(var1, var2);
            } catch (Throwable var131) {
               var10000 = var131;
               var10001 = false;
               break label1119;
            }
         } else {
            while(true) {
               if (var3 >= var1) {
                  try {
                     if (DEBUG) {
                        Log.v("FragmentManager", "Adding back stack index " + var1 + " with " + var2);
                     }
                  } catch (Throwable var133) {
                     var10000 = var133;
                     var10001 = false;
                     break label1119;
                  }

                  try {
                     this.mBackStackIndices.add(var2);
                     break;
                  } catch (Throwable var128) {
                     var10000 = var128;
                     var10001 = false;
                     break label1119;
                  }
               }

               try {
                  this.mBackStackIndices.add((Object)null);
                  if (this.mAvailBackStackIndices == null) {
                     this.mAvailBackStackIndices = new ArrayList();
                  }
               } catch (Throwable var130) {
                  var10000 = var130;
                  var10001 = false;
                  break label1119;
               }

               try {
                  if (DEBUG) {
                     Log.v("FragmentManager", "Adding available back stack index " + var3);
                  }
               } catch (Throwable var134) {
                  var10000 = var134;
                  var10001 = false;
                  break label1119;
               }

               try {
                  this.mAvailBackStackIndices.add(var3);
               } catch (Throwable var129) {
                  var10000 = var129;
                  var10001 = false;
                  break label1119;
               }

               ++var3;
            }
         }

         label1074:
         try {
            return;
         } catch (Throwable var127) {
            var10000 = var127;
            var10001 = false;
            break label1074;
         }
      }

      while(true) {
         Throwable var137 = var10000;

         try {
            throw var137;
         } catch (Throwable var126) {
            var10000 = var126;
            var10001 = false;
            continue;
         }
      }
   }

   public void showFragment(Fragment var1, int var2, int var3) {
      if (DEBUG) {
         Log.v("FragmentManager", "show: " + var1);
      }

      if (var1.mHidden) {
         var1.mHidden = false;
         if (var1.mView != null) {
            Animation var4 = this.loadAnimation(var1, var2, true, var3);
            if (var4 != null) {
               var1.mView.startAnimation(var4);
            }

            var1.mView.setVisibility(0);
         }

         if (var1.mAdded && var1.mHasMenu && var1.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
         }

         var1.onHiddenChanged(false);
      }

   }

   void startPendingDeferredFragments() {
      if (this.mActive != null) {
         for(int var1 = 0; var1 < this.mActive.size(); ++var1) {
            Fragment var2 = (Fragment)this.mActive.get(var1);
            if (var2 != null) {
               this.performPendingDeferredStart(var2);
            }
         }
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(128);
      var1.append("FragmentManager{");
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      var1.append(" in ");
      if (this.mParent != null) {
         DebugUtils.buildShortClassTag(this.mParent, var1);
      } else {
         DebugUtils.buildShortClassTag(this.mActivity, var1);
      }

      var1.append("}}");
      return var1.toString();
   }
}
