package android.support.v4.app;

import android.os.Bundle;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public abstract class FragmentManager {
   public static final int POP_BACK_STACK_INCLUSIVE = 1;

   public static void enableDebugLogging(boolean var0) {
      FragmentManagerImpl.DEBUG = var0;
   }

   public abstract void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener var1);

   public abstract FragmentTransaction beginTransaction();

   public abstract void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4);

   public abstract boolean executePendingTransactions();

   public abstract Fragment findFragmentById(int var1);

   public abstract Fragment findFragmentByTag(String var1);

   public abstract FragmentManager.BackStackEntry getBackStackEntryAt(int var1);

   public abstract int getBackStackEntryCount();

   public abstract Fragment getFragment(Bundle var1, String var2);

   public abstract List getFragments();

   public abstract boolean isDestroyed();

   @Deprecated
   public FragmentTransaction openTransaction() {
      return this.beginTransaction();
   }

   public abstract void popBackStack();

   public abstract void popBackStack(int var1, int var2);

   public abstract void popBackStack(String var1, int var2);

   public abstract boolean popBackStackImmediate();

   public abstract boolean popBackStackImmediate(int var1, int var2);

   public abstract boolean popBackStackImmediate(String var1, int var2);

   public abstract void putFragment(Bundle var1, String var2, Fragment var3);

   public abstract void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener var1);

   public abstract Fragment.SavedState saveFragmentInstanceState(Fragment var1);

   public interface BackStackEntry {
      CharSequence getBreadCrumbShortTitle();

      int getBreadCrumbShortTitleRes();

      CharSequence getBreadCrumbTitle();

      int getBreadCrumbTitleRes();

      int getId();

      String getName();
   }

   public interface OnBackStackChangedListener {
      void onBackStackChanged();
   }
}
