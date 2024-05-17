package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

final class FragmentManagerImpl
  extends FragmentManager
{
  static final Interpolator ACCELERATE_CUBIC = new AccelerateInterpolator(1.5F);
  static final Interpolator ACCELERATE_QUINT;
  static final int ANIM_DUR = 220;
  public static final int ANIM_STYLE_CLOSE_ENTER = 3;
  public static final int ANIM_STYLE_CLOSE_EXIT = 4;
  public static final int ANIM_STYLE_FADE_ENTER = 5;
  public static final int ANIM_STYLE_FADE_EXIT = 6;
  public static final int ANIM_STYLE_OPEN_ENTER = 1;
  public static final int ANIM_STYLE_OPEN_EXIT = 2;
  static boolean DEBUG = false;
  static final Interpolator DECELERATE_CUBIC;
  static final Interpolator DECELERATE_QUINT;
  static final boolean HONEYCOMB;
  static final String TAG = "FragmentManager";
  static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
  static final String TARGET_STATE_TAG = "android:target_state";
  static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
  static final String VIEW_STATE_TAG = "android:view_state";
  ArrayList<Fragment> mActive;
  FragmentActivity mActivity;
  ArrayList<Fragment> mAdded;
  ArrayList<Integer> mAvailBackStackIndices;
  ArrayList<Integer> mAvailIndices;
  ArrayList<BackStackRecord> mBackStack;
  ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
  ArrayList<BackStackRecord> mBackStackIndices;
  ArrayList<Fragment> mCreatedMenus;
  int mCurState = 0;
  boolean mDestroyed;
  Runnable mExecCommit = new Runnable()
  {
    public void run()
    {
      execPendingActions();
    }
  };
  boolean mExecutingActions;
  boolean mHavePendingDeferredStart;
  boolean mNeedMenuInvalidate;
  String mNoTransactionsBecause;
  ArrayList<Runnable> mPendingActions;
  SparseArray<Parcelable> mStateArray = null;
  Bundle mStateBundle = null;
  boolean mStateSaved;
  Runnable[] mTmpActions;
  
  static
  {
    boolean bool = false;
    DEBUG = false;
    if (Build.VERSION.SDK_INT >= 11) {
      bool = true;
    }
    HONEYCOMB = bool;
    DECELERATE_QUINT = new DecelerateInterpolator(2.5F);
    DECELERATE_CUBIC = new DecelerateInterpolator(1.5F);
    ACCELERATE_QUINT = new AccelerateInterpolator(2.5F);
  }
  
  private void checkStateLoss()
  {
    if (mStateSaved) {
      throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
    }
    if (mNoTransactionsBecause != null) {
      throw new IllegalStateException("Can not perform this action inside of " + mNoTransactionsBecause);
    }
  }
  
  static Animation makeFadeAnimation(Context paramContext, float paramFloat1, float paramFloat2)
  {
    paramContext = new AlphaAnimation(paramFloat1, paramFloat2);
    paramContext.setInterpolator(DECELERATE_CUBIC);
    paramContext.setDuration(220L);
    return paramContext;
  }
  
  static Animation makeOpenCloseAnimation(Context paramContext, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    paramContext = new AnimationSet(false);
    Object localObject = new ScaleAnimation(paramFloat1, paramFloat2, paramFloat1, paramFloat2, 1, 0.5F, 1, 0.5F);
    ((ScaleAnimation)localObject).setInterpolator(DECELERATE_QUINT);
    ((ScaleAnimation)localObject).setDuration(220L);
    paramContext.addAnimation((Animation)localObject);
    localObject = new AlphaAnimation(paramFloat3, paramFloat4);
    ((AlphaAnimation)localObject).setInterpolator(DECELERATE_CUBIC);
    ((AlphaAnimation)localObject).setDuration(220L);
    paramContext.addAnimation((Animation)localObject);
    return paramContext;
  }
  
  public static int reverseTransit(int paramInt)
  {
    int i = 0;
    switch (paramInt)
    {
    default: 
      paramInt = i;
    }
    for (;;)
    {
      return paramInt;
      paramInt = 8194;
      continue;
      paramInt = 4097;
      continue;
      paramInt = 4099;
    }
  }
  
  public static int transitToStyleIndex(int paramInt, boolean paramBoolean)
  {
    int i = -1;
    switch (paramInt)
    {
    default: 
      paramInt = i;
      return paramInt;
    case 4097: 
      if (paramBoolean) {}
      for (paramInt = 1;; paramInt = 2) {
        break;
      }
    case 8194: 
      if (paramBoolean) {}
      for (paramInt = 3;; paramInt = 4) {
        break;
      }
    }
    if (paramBoolean) {}
    for (paramInt = 5;; paramInt = 6) {
      break;
    }
  }
  
  void addBackStackState(BackStackRecord paramBackStackRecord)
  {
    if (mBackStack == null) {
      mBackStack = new ArrayList();
    }
    mBackStack.add(paramBackStackRecord);
    reportBackStackChanged();
  }
  
  public void addFragment(Fragment paramFragment, boolean paramBoolean)
  {
    if (mAdded == null) {
      mAdded = new ArrayList();
    }
    if (DEBUG) {
      Log.v("FragmentManager", "add: " + paramFragment);
    }
    makeActive(paramFragment);
    if (!mDetached)
    {
      mAdded.add(paramFragment);
      mAdded = true;
      mRemoving = false;
      if ((mHasMenu) && (mMenuVisible)) {
        mNeedMenuInvalidate = true;
      }
      if (paramBoolean) {
        moveToState(paramFragment);
      }
    }
  }
  
  public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener)
  {
    if (mBackStackChangeListeners == null) {
      mBackStackChangeListeners = new ArrayList();
    }
    mBackStackChangeListeners.add(paramOnBackStackChangedListener);
  }
  
  /* Error */
  public int allocBackStackIndex(BackStackRecord paramBackStackRecord)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 258	android/support/v4/app/FragmentManagerImpl:mAvailBackStackIndices	Ljava/util/ArrayList;
    //   6: ifnull +13 -> 19
    //   9: aload_0
    //   10: getfield 258	android/support/v4/app/FragmentManagerImpl:mAvailBackStackIndices	Ljava/util/ArrayList;
    //   13: invokevirtual 262	java/util/ArrayList:size	()I
    //   16: ifgt +88 -> 104
    //   19: aload_0
    //   20: getfield 264	android/support/v4/app/FragmentManagerImpl:mBackStackIndices	Ljava/util/ArrayList;
    //   23: ifnonnull +16 -> 39
    //   26: new 202	java/util/ArrayList
    //   29: astore_2
    //   30: aload_2
    //   31: invokespecial 203	java/util/ArrayList:<init>	()V
    //   34: aload_0
    //   35: aload_2
    //   36: putfield 264	android/support/v4/app/FragmentManagerImpl:mBackStackIndices	Ljava/util/ArrayList;
    //   39: aload_0
    //   40: getfield 264	android/support/v4/app/FragmentManagerImpl:mBackStackIndices	Ljava/util/ArrayList;
    //   43: invokevirtual 262	java/util/ArrayList:size	()I
    //   46: istore_3
    //   47: getstatic 91	android/support/v4/app/FragmentManagerImpl:DEBUG	Z
    //   50: ifeq +41 -> 91
    //   53: new 145	java/lang/StringBuilder
    //   56: astore_2
    //   57: aload_2
    //   58: invokespecial 146	java/lang/StringBuilder:<init>	()V
    //   61: ldc 42
    //   63: aload_2
    //   64: ldc_w 266
    //   67: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   70: iload_3
    //   71: invokevirtual 269	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   74: ldc_w 271
    //   77: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   80: aload_1
    //   81: invokevirtual 219	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   84: invokevirtual 156	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   87: invokestatic 225	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   90: pop
    //   91: aload_0
    //   92: getfield 264	android/support/v4/app/FragmentManagerImpl:mBackStackIndices	Ljava/util/ArrayList;
    //   95: aload_1
    //   96: invokevirtual 207	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   99: pop
    //   100: aload_0
    //   101: monitorexit
    //   102: iload_3
    //   103: ireturn
    //   104: aload_0
    //   105: getfield 258	android/support/v4/app/FragmentManagerImpl:mAvailBackStackIndices	Ljava/util/ArrayList;
    //   108: aload_0
    //   109: getfield 258	android/support/v4/app/FragmentManagerImpl:mAvailBackStackIndices	Ljava/util/ArrayList;
    //   112: invokevirtual 262	java/util/ArrayList:size	()I
    //   115: iconst_1
    //   116: isub
    //   117: invokevirtual 275	java/util/ArrayList:remove	(I)Ljava/lang/Object;
    //   120: checkcast 277	java/lang/Integer
    //   123: invokevirtual 280	java/lang/Integer:intValue	()I
    //   126: istore_3
    //   127: getstatic 91	android/support/v4/app/FragmentManagerImpl:DEBUG	Z
    //   130: ifeq +41 -> 171
    //   133: new 145	java/lang/StringBuilder
    //   136: astore_2
    //   137: aload_2
    //   138: invokespecial 146	java/lang/StringBuilder:<init>	()V
    //   141: ldc 42
    //   143: aload_2
    //   144: ldc_w 282
    //   147: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   150: iload_3
    //   151: invokevirtual 269	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   154: ldc_w 284
    //   157: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: aload_1
    //   161: invokevirtual 219	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   164: invokevirtual 156	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   167: invokestatic 225	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   170: pop
    //   171: aload_0
    //   172: getfield 264	android/support/v4/app/FragmentManagerImpl:mBackStackIndices	Ljava/util/ArrayList;
    //   175: iload_3
    //   176: aload_1
    //   177: invokevirtual 288	java/util/ArrayList:set	(ILjava/lang/Object;)Ljava/lang/Object;
    //   180: pop
    //   181: aload_0
    //   182: monitorexit
    //   183: goto -81 -> 102
    //   186: astore_1
    //   187: aload_0
    //   188: monitorexit
    //   189: aload_1
    //   190: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	191	0	this	FragmentManagerImpl
    //   0	191	1	paramBackStackRecord	BackStackRecord
    //   29	115	2	localObject	Object
    //   46	130	3	i	int
    // Exception table:
    //   from	to	target	type
    //   2	19	186	finally
    //   19	39	186	finally
    //   39	91	186	finally
    //   91	102	186	finally
    //   104	171	186	finally
    //   171	183	186	finally
    //   187	189	186	finally
  }
  
  public void attachActivity(FragmentActivity paramFragmentActivity)
  {
    if (mActivity != null) {
      throw new IllegalStateException();
    }
    mActivity = paramFragmentActivity;
  }
  
  public void attachFragment(Fragment paramFragment, int paramInt1, int paramInt2)
  {
    if (DEBUG) {
      Log.v("FragmentManager", "attach: " + paramFragment);
    }
    if (mDetached)
    {
      mDetached = false;
      if (!mAdded)
      {
        if (mAdded == null) {
          mAdded = new ArrayList();
        }
        mAdded.add(paramFragment);
        mAdded = true;
        if ((mHasMenu) && (mMenuVisible)) {
          mNeedMenuInvalidate = true;
        }
        moveToState(paramFragment, mCurState, paramInt1, paramInt2, false);
      }
    }
  }
  
  public FragmentTransaction beginTransaction()
  {
    return new BackStackRecord(this);
  }
  
  public void detachFragment(Fragment paramFragment, int paramInt1, int paramInt2)
  {
    if (DEBUG) {
      Log.v("FragmentManager", "detach: " + paramFragment);
    }
    if (!mDetached)
    {
      mDetached = true;
      if (mAdded)
      {
        if (mAdded != null) {
          mAdded.remove(paramFragment);
        }
        if ((mHasMenu) && (mMenuVisible)) {
          mNeedMenuInvalidate = true;
        }
        mAdded = false;
        moveToState(paramFragment, 1, paramInt1, paramInt2, false);
      }
    }
  }
  
  public void dispatchActivityCreated()
  {
    mStateSaved = false;
    moveToState(2, false);
  }
  
  public void dispatchConfigurationChanged(Configuration paramConfiguration)
  {
    if (mAdded != null) {
      for (int i = 0; i < mAdded.size(); i++)
      {
        Fragment localFragment = (Fragment)mAdded.get(i);
        if (localFragment != null) {
          localFragment.onConfigurationChanged(paramConfiguration);
        }
      }
    }
  }
  
  public boolean dispatchContextItemSelected(MenuItem paramMenuItem)
  {
    int i;
    if (mAdded != null)
    {
      i = 0;
      if (i < mAdded.size())
      {
        Fragment localFragment = (Fragment)mAdded.get(i);
        if ((localFragment == null) || (mHidden) || (!mUserVisibleHint) || (!localFragment.onContextItemSelected(paramMenuItem))) {}
      }
    }
    for (boolean bool = true;; bool = false)
    {
      return bool;
      i++;
      break;
    }
  }
  
  public void dispatchCreate()
  {
    mStateSaved = false;
    moveToState(1, false);
  }
  
  public boolean dispatchCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    Object localObject1 = null;
    Object localObject2 = null;
    int i;
    if (mAdded != null)
    {
      i = 0;
      for (;;)
      {
        localObject1 = localObject2;
        bool1 = bool2;
        if (i >= mAdded.size()) {
          break;
        }
        Fragment localFragment = (Fragment)mAdded.get(i);
        localObject1 = localObject2;
        bool1 = bool2;
        if (localFragment != null)
        {
          localObject1 = localObject2;
          bool1 = bool2;
          if (!mHidden)
          {
            localObject1 = localObject2;
            bool1 = bool2;
            if (mHasMenu)
            {
              localObject1 = localObject2;
              bool1 = bool2;
              if (mMenuVisible)
              {
                bool1 = true;
                localFragment.onCreateOptionsMenu(paramMenu, paramMenuInflater);
                localObject1 = localObject2;
                if (localObject2 == null) {
                  localObject1 = new ArrayList();
                }
                ((ArrayList)localObject1).add(localFragment);
              }
            }
          }
        }
        i++;
        localObject2 = localObject1;
        bool2 = bool1;
      }
    }
    if (mCreatedMenus != null) {
      for (i = 0; i < mCreatedMenus.size(); i++)
      {
        paramMenu = (Fragment)mCreatedMenus.get(i);
        if ((localObject1 == null) || (!((ArrayList)localObject1).contains(paramMenu))) {
          paramMenu.onDestroyOptionsMenu();
        }
      }
    }
    mCreatedMenus = ((ArrayList)localObject1);
    return bool1;
  }
  
  public void dispatchDestroy()
  {
    mDestroyed = true;
    execPendingActions();
    moveToState(0, false);
    mActivity = null;
  }
  
  public void dispatchLowMemory()
  {
    if (mAdded != null) {
      for (int i = 0; i < mAdded.size(); i++)
      {
        Fragment localFragment = (Fragment)mAdded.get(i);
        if (localFragment != null) {
          localFragment.onLowMemory();
        }
      }
    }
  }
  
  public boolean dispatchOptionsItemSelected(MenuItem paramMenuItem)
  {
    int i;
    if (mAdded != null)
    {
      i = 0;
      if (i < mAdded.size())
      {
        Fragment localFragment = (Fragment)mAdded.get(i);
        if ((localFragment == null) || (mHidden) || (!mHasMenu) || (!mMenuVisible) || (!localFragment.onOptionsItemSelected(paramMenuItem))) {}
      }
    }
    for (boolean bool = true;; bool = false)
    {
      return bool;
      i++;
      break;
    }
  }
  
  public void dispatchOptionsMenuClosed(Menu paramMenu)
  {
    if (mAdded != null) {
      for (int i = 0; i < mAdded.size(); i++)
      {
        Fragment localFragment = (Fragment)mAdded.get(i);
        if ((localFragment != null) && (!mHidden) && (mHasMenu) && (mMenuVisible)) {
          localFragment.onOptionsMenuClosed(paramMenu);
        }
      }
    }
  }
  
  public void dispatchPause()
  {
    moveToState(4, false);
  }
  
  public boolean dispatchPrepareOptionsMenu(Menu paramMenu)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (mAdded != null)
    {
      int i = 0;
      for (;;)
      {
        bool1 = bool2;
        if (i >= mAdded.size()) {
          break;
        }
        Fragment localFragment = (Fragment)mAdded.get(i);
        bool1 = bool2;
        if (localFragment != null)
        {
          bool1 = bool2;
          if (!mHidden)
          {
            bool1 = bool2;
            if (mHasMenu)
            {
              bool1 = bool2;
              if (mMenuVisible)
              {
                bool1 = true;
                localFragment.onPrepareOptionsMenu(paramMenu);
              }
            }
          }
        }
        i++;
        bool2 = bool1;
      }
    }
    return bool1;
  }
  
  public void dispatchReallyStop()
  {
    moveToState(2, false);
  }
  
  public void dispatchResume()
  {
    mStateSaved = false;
    moveToState(5, false);
  }
  
  public void dispatchStart()
  {
    mStateSaved = false;
    moveToState(4, false);
  }
  
  public void dispatchStop()
  {
    mStateSaved = true;
    moveToState(3, false);
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    String str = paramString + "    ";
    int i;
    int j;
    Object localObject;
    if (mActive != null)
    {
      i = mActive.size();
      if (i > 0)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("Active Fragments in ");
        paramPrintWriter.print(Integer.toHexString(System.identityHashCode(this)));
        paramPrintWriter.println(":");
        for (j = 0; j < i; j++)
        {
          localObject = (Fragment)mActive.get(j);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(j);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(localObject);
          if (localObject != null) {
            ((Fragment)localObject).dump(str, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
          }
        }
      }
    }
    if (mAdded != null)
    {
      i = mAdded.size();
      if (i > 0)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Added Fragments:");
        for (j = 0; j < i; j++)
        {
          localObject = (Fragment)mAdded.get(j);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(j);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(((Fragment)localObject).toString());
        }
      }
    }
    if (mCreatedMenus != null)
    {
      i = mCreatedMenus.size();
      if (i > 0)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Fragments Created Menus:");
        for (j = 0; j < i; j++)
        {
          localObject = (Fragment)mCreatedMenus.get(j);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(j);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(((Fragment)localObject).toString());
        }
      }
    }
    if (mBackStack != null)
    {
      i = mBackStack.size();
      if (i > 0)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Back Stack:");
        for (j = 0; j < i; j++)
        {
          localObject = (BackStackRecord)mBackStack.get(j);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(j);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(localObject.toString());
          ((BackStackRecord)localObject).dump(str, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        }
      }
    }
    try
    {
      if (mBackStackIndices != null)
      {
        i = mBackStackIndices.size();
        if (i > 0)
        {
          paramPrintWriter.print(paramString);
          paramPrintWriter.println("Back Stack Indices:");
          for (j = 0; j < i; j++)
          {
            paramFileDescriptor = (BackStackRecord)mBackStackIndices.get(j);
            paramPrintWriter.print(paramString);
            paramPrintWriter.print("  #");
            paramPrintWriter.print(j);
            paramPrintWriter.print(": ");
            paramPrintWriter.println(paramFileDescriptor);
          }
        }
      }
      if ((mAvailBackStackIndices != null) && (mAvailBackStackIndices.size() > 0))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mAvailBackStackIndices: ");
        paramPrintWriter.println(Arrays.toString(mAvailBackStackIndices.toArray()));
      }
      if (mPendingActions != null)
      {
        i = mPendingActions.size();
        if (i > 0)
        {
          paramPrintWriter.print(paramString);
          paramPrintWriter.println("Pending Actions:");
          for (j = 0; j < i; j++)
          {
            paramFileDescriptor = (Runnable)mPendingActions.get(j);
            paramPrintWriter.print(paramString);
            paramPrintWriter.print("  #");
            paramPrintWriter.print(j);
            paramPrintWriter.print(": ");
            paramPrintWriter.println(paramFileDescriptor);
          }
        }
      }
      paramPrintWriter.print(paramString);
    }
    finally {}
    paramPrintWriter.println("FragmentManager misc state:");
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("  mCurState=");
    paramPrintWriter.print(mCurState);
    paramPrintWriter.print(" mStateSaved=");
    paramPrintWriter.print(mStateSaved);
    paramPrintWriter.print(" mDestroyed=");
    paramPrintWriter.println(mDestroyed);
    if (mNeedMenuInvalidate)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  mNeedMenuInvalidate=");
      paramPrintWriter.println(mNeedMenuInvalidate);
    }
    if (mNoTransactionsBecause != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  mNoTransactionsBecause=");
      paramPrintWriter.println(mNoTransactionsBecause);
    }
    if ((mAvailIndices != null) && (mAvailIndices.size() > 0))
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  mAvailIndices: ");
      paramPrintWriter.println(Arrays.toString(mAvailIndices.toArray()));
    }
  }
  
  public void enqueueAction(Runnable paramRunnable, boolean paramBoolean)
  {
    if (!paramBoolean) {
      checkStateLoss();
    }
    try
    {
      if (mActivity == null)
      {
        paramRunnable = new java/lang/IllegalStateException;
        paramRunnable.<init>("Activity has been destroyed");
        throw paramRunnable;
      }
    }
    finally {}
    if (mPendingActions == null)
    {
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>();
      mPendingActions = localArrayList;
    }
    mPendingActions.add(paramRunnable);
    if (mPendingActions.size() == 1)
    {
      mActivity.mHandler.removeCallbacks(mExecCommit);
      mActivity.mHandler.post(mExecCommit);
    }
  }
  
  public boolean execPendingActions()
  {
    if (mExecutingActions) {
      throw new IllegalStateException("Recursive entry to executePendingTransactions");
    }
    if (Looper.myLooper() != mActivity.mHandler.getLooper()) {
      throw new IllegalStateException("Must be called from main thread of process");
    }
    boolean bool2;
    for (boolean bool1 = false;; bool1 = true) {
      try
      {
        if ((mPendingActions == null) || (mPendingActions.size() == 0))
        {
          if (!mHavePendingDeferredStart) {
            return bool1;
          }
          bool2 = false;
          i = 0;
          while (i < mActive.size())
          {
            Fragment localFragment = (Fragment)mActive.get(i);
            boolean bool3 = bool2;
            if (localFragment != null)
            {
              bool3 = bool2;
              if (mLoaderManager != null) {
                bool3 = bool2 | mLoaderManager.hasRunningLoaders();
              }
            }
            i++;
            bool2 = bool3;
          }
        }
        int j = mPendingActions.size();
        if ((mTmpActions == null) || (mTmpActions.length < j)) {
          mTmpActions = new Runnable[j];
        }
        mPendingActions.toArray(mTmpActions);
        mPendingActions.clear();
        mActivity.mHandler.removeCallbacks(mExecCommit);
        mExecutingActions = true;
        for (int i = 0; i < j; i++)
        {
          mTmpActions[i].run();
          mTmpActions[i] = null;
        }
        mExecutingActions = false;
      }
      finally {}
    }
    if (!bool2)
    {
      mHavePendingDeferredStart = false;
      startPendingDeferredFragments();
    }
    return bool1;
  }
  
  public boolean executePendingTransactions()
  {
    return execPendingActions();
  }
  
  public Fragment findFragmentById(int paramInt)
  {
    int i;
    Object localObject;
    if (mAdded != null)
    {
      i = mAdded.size() - 1;
      if (i >= 0)
      {
        localObject = (Fragment)mAdded.get(i);
        if ((localObject == null) || (mFragmentId != paramInt)) {}
      }
    }
    for (;;)
    {
      return (Fragment)localObject;
      i--;
      break;
      if (mActive != null) {
        for (i = mActive.size() - 1;; i--)
        {
          if (i < 0) {
            break label110;
          }
          Fragment localFragment = (Fragment)mActive.get(i);
          if (localFragment != null)
          {
            localObject = localFragment;
            if (mFragmentId == paramInt) {
              break;
            }
          }
        }
      }
      label110:
      localObject = null;
    }
  }
  
  public Fragment findFragmentByTag(String paramString)
  {
    int i;
    Object localObject;
    if ((mAdded != null) && (paramString != null))
    {
      i = mAdded.size() - 1;
      if (i >= 0)
      {
        localObject = (Fragment)mAdded.get(i);
        if ((localObject == null) || (!paramString.equals(mTag))) {}
      }
    }
    for (;;)
    {
      return (Fragment)localObject;
      i--;
      break;
      if ((mActive != null) && (paramString != null)) {
        for (i = mActive.size() - 1;; i--)
        {
          if (i < 0) {
            break label124;
          }
          Fragment localFragment = (Fragment)mActive.get(i);
          if (localFragment != null)
          {
            localObject = localFragment;
            if (paramString.equals(mTag)) {
              break;
            }
          }
        }
      }
      label124:
      localObject = null;
    }
  }
  
  public Fragment findFragmentByWho(String paramString)
  {
    int i;
    Fragment localFragment;
    if ((mActive != null) && (paramString != null))
    {
      i = mActive.size() - 1;
      if (i >= 0)
      {
        localFragment = (Fragment)mActive.get(i);
        if ((localFragment == null) || (!paramString.equals(mWho))) {}
      }
    }
    for (paramString = localFragment;; paramString = null)
    {
      return paramString;
      i--;
      break;
    }
  }
  
  public void freeBackStackIndex(int paramInt)
  {
    try
    {
      mBackStackIndices.set(paramInt, null);
      Object localObject1;
      if (mAvailBackStackIndices == null)
      {
        localObject1 = new java/util/ArrayList;
        ((ArrayList)localObject1).<init>();
        mAvailBackStackIndices = ((ArrayList)localObject1);
      }
      if (DEBUG)
      {
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        Log.v("FragmentManager", "Freeing back stack index " + paramInt);
      }
      mAvailBackStackIndices.add(Integer.valueOf(paramInt));
      return;
    }
    finally {}
  }
  
  public FragmentManager.BackStackEntry getBackStackEntryAt(int paramInt)
  {
    return (FragmentManager.BackStackEntry)mBackStack.get(paramInt);
  }
  
  public int getBackStackEntryCount()
  {
    if (mBackStack != null) {}
    for (int i = mBackStack.size();; i = 0) {
      return i;
    }
  }
  
  public Fragment getFragment(Bundle paramBundle, String paramString)
  {
    int i = paramBundle.getInt(paramString, -1);
    if (i == -1) {
      paramBundle = null;
    }
    Fragment localFragment;
    do
    {
      return paramBundle;
      if (i >= mActive.size()) {
        throw new IllegalStateException("Fragement no longer exists for key " + paramString + ": index " + i);
      }
      localFragment = (Fragment)mActive.get(i);
      paramBundle = localFragment;
    } while (localFragment != null);
    throw new IllegalStateException("Fragement no longer exists for key " + paramString + ": index " + i);
  }
  
  public void hideFragment(Fragment paramFragment, int paramInt1, int paramInt2)
  {
    if (DEBUG) {
      Log.v("FragmentManager", "hide: " + paramFragment);
    }
    if (!mHidden)
    {
      mHidden = true;
      if (mView != null)
      {
        Animation localAnimation = loadAnimation(paramFragment, paramInt1, true, paramInt2);
        if (localAnimation != null) {
          mView.startAnimation(localAnimation);
        }
        mView.setVisibility(8);
      }
      if ((mAdded) && (mHasMenu) && (mMenuVisible)) {
        mNeedMenuInvalidate = true;
      }
      paramFragment.onHiddenChanged(true);
    }
  }
  
  Animation loadAnimation(Fragment paramFragment, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    Animation localAnimation = paramFragment.onCreateAnimation(paramInt1, paramBoolean, mNextAnim);
    if (localAnimation != null) {
      paramFragment = localAnimation;
    }
    for (;;)
    {
      return paramFragment;
      if (mNextAnim != 0)
      {
        paramFragment = AnimationUtils.loadAnimation(mActivity, mNextAnim);
        if (paramFragment != null) {}
      }
      else if (paramInt1 == 0)
      {
        paramFragment = null;
      }
      else
      {
        paramInt1 = transitToStyleIndex(paramInt1, paramBoolean);
        if (paramInt1 < 0) {
          paramFragment = null;
        } else {
          switch (paramInt1)
          {
          default: 
            paramInt1 = paramInt2;
            if (paramInt2 == 0)
            {
              paramInt1 = paramInt2;
              if (mActivity.getWindow() != null) {
                paramInt1 = mActivity.getWindow().getAttributes().windowAnimations;
              }
            }
            if (paramInt1 == 0) {
              paramFragment = null;
            }
            break;
          case 1: 
            paramFragment = makeOpenCloseAnimation(mActivity, 1.125F, 1.0F, 0.0F, 1.0F);
            break;
          case 2: 
            paramFragment = makeOpenCloseAnimation(mActivity, 1.0F, 0.975F, 1.0F, 0.0F);
            break;
          case 3: 
            paramFragment = makeOpenCloseAnimation(mActivity, 0.975F, 1.0F, 0.0F, 1.0F);
            break;
          case 4: 
            paramFragment = makeOpenCloseAnimation(mActivity, 1.0F, 1.075F, 1.0F, 0.0F);
            break;
          case 5: 
            paramFragment = makeFadeAnimation(mActivity, 0.0F, 1.0F);
            break;
          case 6: 
            paramFragment = makeFadeAnimation(mActivity, 1.0F, 0.0F);
            continue;
            paramFragment = null;
          }
        }
      }
    }
  }
  
  void makeActive(Fragment paramFragment)
  {
    if (mIndex >= 0) {}
    label138:
    for (;;)
    {
      return;
      if ((mAvailIndices == null) || (mAvailIndices.size() <= 0))
      {
        if (mActive == null) {
          mActive = new ArrayList();
        }
        paramFragment.setIndex(mActive.size());
        mActive.add(paramFragment);
      }
      for (;;)
      {
        if (!DEBUG) {
          break label138;
        }
        Log.v("FragmentManager", "Allocated fragment index " + paramFragment);
        break;
        paramFragment.setIndex(((Integer)mAvailIndices.remove(mAvailIndices.size() - 1)).intValue());
        mActive.set(mIndex, paramFragment);
      }
    }
  }
  
  void makeInactive(Fragment paramFragment)
  {
    if (mIndex < 0) {}
    for (;;)
    {
      return;
      if (DEBUG) {
        Log.v("FragmentManager", "Freeing fragment index " + paramFragment);
      }
      mActive.set(mIndex, null);
      if (mAvailIndices == null) {
        mAvailIndices = new ArrayList();
      }
      mAvailIndices.add(Integer.valueOf(mIndex));
      mActivity.invalidateSupportFragmentIndex(mIndex);
      paramFragment.initState();
    }
  }
  
  void moveToState(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    if ((mActivity == null) && (paramInt1 != 0)) {
      throw new IllegalStateException("No activity");
    }
    if ((!paramBoolean) && (mCurState == paramInt1)) {}
    for (;;)
    {
      return;
      mCurState = paramInt1;
      if (mActive != null)
      {
        boolean bool1 = false;
        int i = 0;
        while (i < mActive.size())
        {
          Fragment localFragment = (Fragment)mActive.get(i);
          boolean bool2 = bool1;
          if (localFragment != null)
          {
            moveToState(localFragment, paramInt1, paramInt2, paramInt3, false);
            bool2 = bool1;
            if (mLoaderManager != null) {
              bool2 = bool1 | mLoaderManager.hasRunningLoaders();
            }
          }
          i++;
          bool1 = bool2;
        }
        if (!bool1) {
          startPendingDeferredFragments();
        }
        if ((mNeedMenuInvalidate) && (mActivity != null) && (mCurState == 5))
        {
          mActivity.supportInvalidateOptionsMenu();
          mNeedMenuInvalidate = false;
        }
      }
    }
  }
  
  void moveToState(int paramInt, boolean paramBoolean)
  {
    moveToState(paramInt, 0, 0, paramBoolean);
  }
  
  void moveToState(Fragment paramFragment)
  {
    moveToState(paramFragment, mCurState, 0, 0, false);
  }
  
  void moveToState(final Fragment paramFragment, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    int i = paramInt1;
    if (!mAdded)
    {
      i = paramInt1;
      if (paramInt1 > 1) {
        i = 1;
      }
    }
    int j = i;
    if (mRemoving)
    {
      j = i;
      if (i > mState) {
        j = mState;
      }
    }
    paramInt1 = j;
    if (mDeferStart)
    {
      paramInt1 = j;
      if (mState < 4)
      {
        paramInt1 = j;
        if (j > 3) {
          paramInt1 = 3;
        }
      }
    }
    int k;
    if (mState < paramInt1)
    {
      if ((mFromLayout) && (!mInLayout)) {
        return;
      }
      if (mAnimatingAway != null)
      {
        mAnimatingAway = null;
        moveToState(paramFragment, mStateAfterAnimating, 0, 0, true);
      }
      j = paramInt1;
      k = paramInt1;
      i = paramInt1;
      switch (mState)
      {
      default: 
        j = paramInt1;
      }
    }
    for (;;)
    {
      mState = j;
      break;
      if (DEBUG) {
        Log.v("FragmentManager", "moveto CREATED: " + paramFragment);
      }
      i = paramInt1;
      if (mSavedFragmentState != null)
      {
        mSavedViewState = mSavedFragmentState.getSparseParcelableArray("android:view_state");
        mTarget = getFragment(mSavedFragmentState, "android:target_state");
        if (mTarget != null) {
          mTargetRequestCode = mSavedFragmentState.getInt("android:target_req_state", 0);
        }
        mUserVisibleHint = mSavedFragmentState.getBoolean("android:user_visible_hint", true);
        i = paramInt1;
        if (!mUserVisibleHint)
        {
          mDeferStart = true;
          i = paramInt1;
          if (paramInt1 > 3) {
            i = 3;
          }
        }
      }
      mActivity = mActivity;
      mFragmentManager = mActivity.mFragments;
      mCalled = false;
      paramFragment.onAttach(mActivity);
      if (!mCalled) {
        throw new SuperNotCalledException("Fragment " + paramFragment + " did not call through to super.onAttach()");
      }
      mActivity.onAttachFragment(paramFragment);
      if (!mRetaining)
      {
        mCalled = false;
        paramFragment.onCreate(mSavedFragmentState);
        if (!mCalled) {
          throw new SuperNotCalledException("Fragment " + paramFragment + " did not call through to super.onCreate()");
        }
      }
      mRetaining = false;
      j = i;
      if (mFromLayout)
      {
        mView = paramFragment.onCreateView(paramFragment.getLayoutInflater(mSavedFragmentState), null, mSavedFragmentState);
        if (mView == null) {
          break label694;
        }
        mInnerView = mView;
        mView = NoSaveStateFrameLayout.wrap(mView);
        if (mHidden) {
          mView.setVisibility(8);
        }
        paramFragment.onViewCreated(mView, mSavedFragmentState);
      }
      Object localObject1;
      Object localObject2;
      for (j = i;; j = i)
      {
        k = j;
        if (j <= 1) {
          break label909;
        }
        if (DEBUG) {
          Log.v("FragmentManager", "moveto ACTIVITY_CREATED: " + paramFragment);
        }
        if (mFromLayout) {
          break label827;
        }
        localObject1 = null;
        if (mContainerId == 0) {
          break;
        }
        localObject2 = (ViewGroup)mActivity.findViewById(mContainerId);
        localObject1 = localObject2;
        if (localObject2 != null) {
          break;
        }
        localObject1 = localObject2;
        if (mRestored) {
          break;
        }
        throw new IllegalArgumentException("No view found for id 0x" + Integer.toHexString(mContainerId) + " for fragment " + paramFragment);
        label694:
        mInnerView = null;
      }
      mContainer = ((ViewGroup)localObject1);
      mView = paramFragment.onCreateView(paramFragment.getLayoutInflater(mSavedFragmentState), (ViewGroup)localObject1, mSavedFragmentState);
      if (mView != null)
      {
        mInnerView = mView;
        mView = NoSaveStateFrameLayout.wrap(mView);
        if (localObject1 != null)
        {
          localObject2 = loadAnimation(paramFragment, paramInt2, true, paramInt3);
          if (localObject2 != null) {
            mView.startAnimation((Animation)localObject2);
          }
          ((ViewGroup)localObject1).addView(mView);
        }
        if (mHidden) {
          mView.setVisibility(8);
        }
        paramFragment.onViewCreated(mView, mSavedFragmentState);
      }
      for (;;)
      {
        label827:
        mCalled = false;
        paramFragment.onActivityCreated(mSavedFragmentState);
        if (mCalled) {
          break;
        }
        throw new SuperNotCalledException("Fragment " + paramFragment + " did not call through to super.onActivityCreated()");
        mInnerView = null;
      }
      if (mView != null) {
        paramFragment.restoreViewState();
      }
      mSavedFragmentState = null;
      k = j;
      label909:
      i = k;
      if (k > 3)
      {
        if (DEBUG) {
          Log.v("FragmentManager", "moveto STARTED: " + paramFragment);
        }
        mCalled = false;
        paramFragment.performStart();
        i = k;
        if (!mCalled) {
          throw new SuperNotCalledException("Fragment " + paramFragment + " did not call through to super.onStart()");
        }
      }
      j = i;
      if (i > 4)
      {
        if (DEBUG) {
          Log.v("FragmentManager", "moveto RESUMED: " + paramFragment);
        }
        mCalled = false;
        mResumed = true;
        paramFragment.onResume();
        if (!mCalled) {
          throw new SuperNotCalledException("Fragment " + paramFragment + " did not call through to super.onResume()");
        }
        mSavedFragmentState = null;
        mSavedViewState = null;
        j = i;
        continue;
        j = paramInt1;
        if (mState > paramInt1) {
          switch (mState)
          {
          default: 
            j = paramInt1;
            break;
          case 1: 
          case 5: 
          case 4: 
          case 3: 
          case 2: 
            for (;;)
            {
              j = paramInt1;
              if (paramInt1 >= 1) {
                break;
              }
              if ((mDestroyed) && (mAnimatingAway != null))
              {
                localObject1 = mAnimatingAway;
                mAnimatingAway = null;
                ((View)localObject1).clearAnimation();
              }
              if (mAnimatingAway == null) {
                break label1686;
              }
              mStateAfterAnimating = paramInt1;
              j = 1;
              break;
              if (paramInt1 < 5)
              {
                if (DEBUG) {
                  Log.v("FragmentManager", "movefrom RESUMED: " + paramFragment);
                }
                mCalled = false;
                paramFragment.onPause();
                if (!mCalled) {
                  throw new SuperNotCalledException("Fragment " + paramFragment + " did not call through to super.onPause()");
                }
                mResumed = false;
              }
              if (paramInt1 < 4)
              {
                if (DEBUG) {
                  Log.v("FragmentManager", "movefrom STARTED: " + paramFragment);
                }
                mCalled = false;
                paramFragment.performStop();
                if (!mCalled) {
                  throw new SuperNotCalledException("Fragment " + paramFragment + " did not call through to super.onStop()");
                }
              }
              if (paramInt1 < 3)
              {
                if (DEBUG) {
                  Log.v("FragmentManager", "movefrom STOPPED: " + paramFragment);
                }
                paramFragment.performReallyStop();
              }
              if (paramInt1 < 2)
              {
                if (DEBUG) {
                  Log.v("FragmentManager", "movefrom ACTIVITY_CREATED: " + paramFragment);
                }
                if ((mView != null) && (!mActivity.isFinishing()) && (mSavedViewState == null)) {
                  saveFragmentViewState(paramFragment);
                }
                mCalled = false;
                paramFragment.performDestroyView();
                if (!mCalled) {
                  throw new SuperNotCalledException("Fragment " + paramFragment + " did not call through to super.onDestroyView()");
                }
                if ((mView != null) && (mContainer != null))
                {
                  localObject2 = null;
                  localObject1 = localObject2;
                  if (mCurState > 0)
                  {
                    localObject1 = localObject2;
                    if (!mDestroyed) {
                      localObject1 = loadAnimation(paramFragment, paramInt2, false, paramInt3);
                    }
                  }
                  if (localObject1 != null)
                  {
                    mAnimatingAway = mView;
                    mStateAfterAnimating = paramInt1;
                    ((Animation)localObject1).setAnimationListener(new Animation.AnimationListener()
                    {
                      public void onAnimationEnd(Animation paramAnonymousAnimation)
                      {
                        if (paramFragmentmAnimatingAway != null)
                        {
                          paramFragmentmAnimatingAway = null;
                          moveToState(paramFragment, paramFragmentmStateAfterAnimating, 0, 0, false);
                        }
                      }
                      
                      public void onAnimationRepeat(Animation paramAnonymousAnimation) {}
                      
                      public void onAnimationStart(Animation paramAnonymousAnimation) {}
                    });
                    mView.startAnimation((Animation)localObject1);
                  }
                  mContainer.removeView(mView);
                }
                mContainer = null;
                mView = null;
                mInnerView = null;
              }
            }
            label1686:
            if (DEBUG) {
              Log.v("FragmentManager", "movefrom CREATED: " + paramFragment);
            }
            if (!mRetaining)
            {
              mCalled = false;
              paramFragment.onDestroy();
              if (!mCalled) {
                throw new SuperNotCalledException("Fragment " + paramFragment + " did not call through to super.onDestroy()");
              }
            }
            mCalled = false;
            paramFragment.onDetach();
            if (!mCalled) {
              throw new SuperNotCalledException("Fragment " + paramFragment + " did not call through to super.onDetach()");
            }
            j = paramInt1;
            if (!paramBoolean) {
              if (!mRetaining)
              {
                makeInactive(paramFragment);
                j = paramInt1;
              }
              else
              {
                mActivity = null;
                mFragmentManager = null;
                j = paramInt1;
              }
            }
            break;
          }
        }
      }
    }
  }
  
  public void noteStateNotSaved()
  {
    mStateSaved = false;
  }
  
  public void performPendingDeferredStart(Fragment paramFragment)
  {
    if (mDeferStart)
    {
      if (!mExecutingActions) {
        break label20;
      }
      mHavePendingDeferredStart = true;
    }
    for (;;)
    {
      return;
      label20:
      mDeferStart = false;
      moveToState(paramFragment, mCurState, 0, 0, false);
    }
  }
  
  public void popBackStack()
  {
    enqueueAction(new Runnable()
    {
      public void run()
      {
        popBackStackState(mActivity.mHandler, null, -1, 0);
      }
    }, false);
  }
  
  public void popBackStack(final int paramInt1, final int paramInt2)
  {
    if (paramInt1 < 0) {
      throw new IllegalArgumentException("Bad id: " + paramInt1);
    }
    enqueueAction(new Runnable()
    {
      public void run()
      {
        popBackStackState(mActivity.mHandler, null, paramInt1, paramInt2);
      }
    }, false);
  }
  
  public void popBackStack(final String paramString, final int paramInt)
  {
    enqueueAction(new Runnable()
    {
      public void run()
      {
        popBackStackState(mActivity.mHandler, paramString, -1, paramInt);
      }
    }, false);
  }
  
  public boolean popBackStackImmediate()
  {
    checkStateLoss();
    executePendingTransactions();
    return popBackStackState(mActivity.mHandler, null, -1, 0);
  }
  
  public boolean popBackStackImmediate(int paramInt1, int paramInt2)
  {
    checkStateLoss();
    executePendingTransactions();
    if (paramInt1 < 0) {
      throw new IllegalArgumentException("Bad id: " + paramInt1);
    }
    return popBackStackState(mActivity.mHandler, null, paramInt1, paramInt2);
  }
  
  public boolean popBackStackImmediate(String paramString, int paramInt)
  {
    checkStateLoss();
    executePendingTransactions();
    return popBackStackState(mActivity.mHandler, paramString, -1, paramInt);
  }
  
  boolean popBackStackState(Handler paramHandler, String paramString, int paramInt1, int paramInt2)
  {
    boolean bool1 = false;
    boolean bool2;
    if (mBackStack == null) {
      bool2 = bool1;
    }
    do
    {
      return bool2;
      if ((paramString != null) || (paramInt1 >= 0) || ((paramInt2 & 0x1) != 0)) {
        break;
      }
      paramInt1 = mBackStack.size() - 1;
      bool2 = bool1;
    } while (paramInt1 < 0);
    ((BackStackRecord)mBackStack.remove(paramInt1)).popFromBackStack(true);
    reportBackStackChanged();
    for (;;)
    {
      bool2 = true;
      break;
      int i = -1;
      if ((paramString != null) || (paramInt1 >= 0)) {
        for (int j = mBackStack.size() - 1;; j--)
        {
          if (j >= 0)
          {
            paramHandler = (BackStackRecord)mBackStack.get(j);
            if ((paramString == null) || (!paramString.equals(paramHandler.getName()))) {
              break label219;
            }
          }
          label219:
          while ((paramInt1 >= 0) && (paramInt1 == mIndex))
          {
            bool2 = bool1;
            if (j < 0) {
              break;
            }
            i = j;
            if ((paramInt2 & 0x1) == 0) {
              break label237;
            }
            for (paramInt2 = j - 1;; paramInt2--)
            {
              i = paramInt2;
              if (paramInt2 < 0) {
                break;
              }
              paramHandler = (BackStackRecord)mBackStack.get(paramInt2);
              if ((paramString == null) || (!paramString.equals(paramHandler.getName())))
              {
                i = paramInt2;
                if (paramInt1 < 0) {
                  break;
                }
                i = paramInt2;
                if (paramInt1 != mIndex) {
                  break;
                }
              }
            }
          }
        }
      }
      label237:
      bool2 = bool1;
      if (i == mBackStack.size() - 1) {
        break;
      }
      paramHandler = new ArrayList();
      for (paramInt1 = mBackStack.size() - 1; paramInt1 > i; paramInt1--) {
        paramHandler.add(mBackStack.remove(paramInt1));
      }
      paramInt2 = paramHandler.size() - 1;
      paramInt1 = 0;
      if (paramInt1 <= paramInt2)
      {
        if (DEBUG) {
          Log.v("FragmentManager", "Popping back stack state: " + paramHandler.get(paramInt1));
        }
        paramString = (BackStackRecord)paramHandler.get(paramInt1);
        if (paramInt1 == paramInt2) {}
        for (bool2 = true;; bool2 = false)
        {
          paramString.popFromBackStack(bool2);
          paramInt1++;
          break;
        }
      }
      reportBackStackChanged();
    }
  }
  
  public void putFragment(Bundle paramBundle, String paramString, Fragment paramFragment)
  {
    if (mIndex < 0) {
      throw new IllegalStateException("Fragment " + paramFragment + " is not currently in the FragmentManager");
    }
    paramBundle.putInt(paramString, mIndex);
  }
  
  public void removeFragment(Fragment paramFragment, int paramInt1, int paramInt2)
  {
    if (DEBUG) {
      Log.v("FragmentManager", "remove: " + paramFragment + " nesting=" + mBackStackNesting);
    }
    if (!paramFragment.isInBackStack())
    {
      i = 1;
      if ((!mDetached) || (i != 0))
      {
        if (mAdded != null) {
          mAdded.remove(paramFragment);
        }
        if ((mHasMenu) && (mMenuVisible)) {
          mNeedMenuInvalidate = true;
        }
        mAdded = false;
        mRemoving = true;
        if (i == 0) {
          break label137;
        }
      }
    }
    label137:
    for (int i = 0;; i = 1)
    {
      moveToState(paramFragment, i, paramInt1, paramInt2, false);
      return;
      i = 0;
      break;
    }
  }
  
  public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener)
  {
    if (mBackStackChangeListeners != null) {
      mBackStackChangeListeners.remove(paramOnBackStackChangedListener);
    }
  }
  
  void reportBackStackChanged()
  {
    if (mBackStackChangeListeners != null) {
      for (int i = 0; i < mBackStackChangeListeners.size(); i++) {
        ((FragmentManager.OnBackStackChangedListener)mBackStackChangeListeners.get(i)).onBackStackChanged();
      }
    }
  }
  
  void restoreAllState(Parcelable paramParcelable, ArrayList<Fragment> paramArrayList)
  {
    if (paramParcelable == null) {}
    for (;;)
    {
      return;
      paramParcelable = (FragmentManagerState)paramParcelable;
      if (mActive != null)
      {
        Fragment localFragment;
        Object localObject;
        if (paramArrayList != null) {
          for (i = 0; i < paramArrayList.size(); i++)
          {
            localFragment = (Fragment)paramArrayList.get(i);
            if (DEBUG) {
              Log.v("FragmentManager", "restoreAllState: re-attaching retained " + localFragment);
            }
            localObject = mActive[mIndex];
            mInstance = localFragment;
            mSavedViewState = null;
            mBackStackNesting = 0;
            mInLayout = false;
            mAdded = false;
            mTarget = null;
            if (mSavedFragmentState != null)
            {
              mSavedFragmentState.setClassLoader(mActivity.getClassLoader());
              mSavedViewState = mSavedFragmentState.getSparseParcelableArray("android:view_state");
            }
          }
        }
        mActive = new ArrayList(mActive.length);
        if (mAvailIndices != null) {
          mAvailIndices.clear();
        }
        int i = 0;
        if (i < mActive.length)
        {
          localObject = mActive[i];
          if (localObject != null)
          {
            localFragment = ((FragmentState)localObject).instantiate(mActivity);
            if (DEBUG) {
              Log.v("FragmentManager", "restoreAllState: adding #" + i + ": " + localFragment);
            }
            mActive.add(localFragment);
            mInstance = null;
          }
          for (;;)
          {
            i++;
            break;
            if (DEBUG) {
              Log.v("FragmentManager", "restoreAllState: adding #" + i + ": (null)");
            }
            mActive.add(null);
            if (mAvailIndices == null) {
              mAvailIndices = new ArrayList();
            }
            if (DEBUG) {
              Log.v("FragmentManager", "restoreAllState: adding avail #" + i);
            }
            mAvailIndices.add(Integer.valueOf(i));
          }
        }
        if (paramArrayList != null)
        {
          i = 0;
          if (i < paramArrayList.size())
          {
            localObject = (Fragment)paramArrayList.get(i);
            if (mTargetIndex >= 0) {
              if (mTargetIndex >= mActive.size()) {
                break label482;
              }
            }
            for (mTarget = ((Fragment)mActive.get(mTargetIndex));; mTarget = null)
            {
              i++;
              break;
              label482:
              Log.w("FragmentManager", "Re-attaching retained fragment " + localObject + " target no longer exists: " + mTargetIndex);
            }
          }
        }
        if (mAdded != null)
        {
          mAdded = new ArrayList(mAdded.length);
          for (i = 0; i < mAdded.length; i++)
          {
            paramArrayList = (Fragment)mActive.get(mAdded[i]);
            if (paramArrayList == null) {
              throw new IllegalStateException("No instantiated fragment for index #" + mAdded[i]);
            }
            mAdded = true;
            if (DEBUG) {
              Log.v("FragmentManager", "restoreAllState: making added #" + i + ": " + paramArrayList);
            }
            mAdded.add(paramArrayList);
          }
        }
        mAdded = null;
        if (mBackStack != null)
        {
          mBackStack = new ArrayList(mBackStack.length);
          for (i = 0; i < mBackStack.length; i++)
          {
            paramArrayList = mBackStack[i].instantiate(this);
            if (DEBUG) {
              Log.v("FragmentManager", "restoreAllState: adding bse #" + i + " (index " + mIndex + "): " + paramArrayList);
            }
            mBackStack.add(paramArrayList);
            if (mIndex >= 0) {
              setBackStackIndex(mIndex, paramArrayList);
            }
          }
        }
        else
        {
          mBackStack = null;
        }
      }
    }
  }
  
  ArrayList<Fragment> retainNonConfig()
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if (mActive != null)
    {
      int i = 0;
      localObject1 = localObject2;
      if (i < mActive.size())
      {
        Fragment localFragment = (Fragment)mActive.get(i);
        Object localObject3 = localObject2;
        if (localFragment != null)
        {
          localObject3 = localObject2;
          if (mRetainInstance)
          {
            localObject1 = localObject2;
            if (localObject2 == null) {
              localObject1 = new ArrayList();
            }
            ((ArrayList)localObject1).add(localFragment);
            mRetaining = true;
            if (mTarget == null) {
              break label158;
            }
          }
        }
        label158:
        for (int j = mTarget.mIndex;; j = -1)
        {
          mTargetIndex = j;
          localObject3 = localObject1;
          if (DEBUG)
          {
            Log.v("FragmentManager", "retainNonConfig: keeping retained " + localFragment);
            localObject3 = localObject1;
          }
          i++;
          localObject2 = localObject3;
          break;
        }
      }
    }
    return (ArrayList<Fragment>)localObject1;
  }
  
  Parcelable saveAllState()
  {
    Object localObject1 = null;
    execPendingActions();
    if (HONEYCOMB) {
      mStateSaved = true;
    }
    Object localObject2 = localObject1;
    if (mActive != null)
    {
      if (mActive.size() > 0) {
        break label41;
      }
      localObject2 = localObject1;
    }
    for (;;)
    {
      return (Parcelable)localObject2;
      label41:
      int i = mActive.size();
      FragmentState[] arrayOfFragmentState = new FragmentState[i];
      int j = 0;
      int k = 0;
      Object localObject3;
      if (k < i)
      {
        localObject2 = (Fragment)mActive.get(k);
        int m;
        if (localObject2 != null)
        {
          if (mIndex < 0)
          {
            localObject2 = "Failure saving state: active " + localObject2 + " has cleared index: " + mIndex;
            Log.e("FragmentManager", (String)localObject2);
            dump("  ", null, new PrintWriter(new LogWriter("FragmentManager")), new String[0]);
            throw new IllegalStateException((String)localObject2);
          }
          m = 1;
          localObject3 = new FragmentState((Fragment)localObject2);
          arrayOfFragmentState[k] = localObject3;
          if ((mState <= 0) || (mSavedFragmentState != null)) {
            break label425;
          }
          mSavedFragmentState = saveFragmentBasicState((Fragment)localObject2);
          if (mTarget != null)
          {
            if (mTarget.mIndex < 0)
            {
              localObject2 = "Failure saving state: " + localObject2 + " has target not in fragment manager: " + mTarget;
              Log.e("FragmentManager", (String)localObject2);
              dump("  ", null, new PrintWriter(new LogWriter("FragmentManager")), new String[0]);
              throw new IllegalStateException((String)localObject2);
            }
            if (mSavedFragmentState == null) {
              mSavedFragmentState = new Bundle();
            }
            putFragment(mSavedFragmentState, "android:target_state", mTarget);
            if (mTargetRequestCode != 0) {
              mSavedFragmentState.putInt("android:target_req_state", mTargetRequestCode);
            }
          }
        }
        for (;;)
        {
          j = m;
          if (DEBUG)
          {
            Log.v("FragmentManager", "Saved state of " + localObject2 + ": " + mSavedFragmentState);
            j = m;
          }
          k++;
          break;
          label425:
          mSavedFragmentState = mSavedFragmentState;
        }
      }
      if (j == 0)
      {
        localObject2 = localObject1;
        if (DEBUG)
        {
          Log.v("FragmentManager", "saveAllState: no fragments!");
          localObject2 = localObject1;
        }
      }
      else
      {
        localObject1 = null;
        localObject3 = null;
        localObject2 = localObject1;
        if (mAdded != null)
        {
          j = mAdded.size();
          localObject2 = localObject1;
          if (j > 0)
          {
            localObject1 = new int[j];
            for (k = 0;; k++)
            {
              localObject2 = localObject1;
              if (k >= j) {
                break;
              }
              localObject1[k] = mAdded.get(k)).mIndex;
              if (localObject1[k] < 0)
              {
                localObject2 = "Failure saving state: active " + mAdded.get(k) + " has cleared index: " + localObject1[k];
                Log.e("FragmentManager", (String)localObject2);
                dump("  ", null, new PrintWriter(new LogWriter("FragmentManager")), new String[0]);
                throw new IllegalStateException((String)localObject2);
              }
              if (DEBUG) {
                Log.v("FragmentManager", "saveAllState: adding fragment #" + k + ": " + mAdded.get(k));
              }
            }
          }
        }
        localObject1 = localObject3;
        if (mBackStack != null)
        {
          j = mBackStack.size();
          localObject1 = localObject3;
          if (j > 0)
          {
            localObject3 = new BackStackState[j];
            for (k = 0;; k++)
            {
              localObject1 = localObject3;
              if (k >= j) {
                break;
              }
              localObject3[k] = new BackStackState(this, (BackStackRecord)mBackStack.get(k));
              if (DEBUG) {
                Log.v("FragmentManager", "saveAllState: adding back stack #" + k + ": " + mBackStack.get(k));
              }
            }
          }
        }
        localObject3 = new FragmentManagerState();
        mActive = arrayOfFragmentState;
        mAdded = ((int[])localObject2);
        mBackStack = ((BackStackState[])localObject1);
        localObject2 = localObject3;
      }
    }
  }
  
  Bundle saveFragmentBasicState(Fragment paramFragment)
  {
    Object localObject1 = null;
    if (mStateBundle == null) {
      mStateBundle = new Bundle();
    }
    paramFragment.onSaveInstanceState(mStateBundle);
    if (!mStateBundle.isEmpty())
    {
      localObject1 = mStateBundle;
      mStateBundle = null;
    }
    if (mView != null) {
      saveFragmentViewState(paramFragment);
    }
    Object localObject2 = localObject1;
    if (mSavedViewState != null)
    {
      localObject2 = localObject1;
      if (localObject1 == null) {
        localObject2 = new Bundle();
      }
      ((Bundle)localObject2).putSparseParcelableArray("android:view_state", mSavedViewState);
    }
    localObject1 = localObject2;
    if (!mUserVisibleHint)
    {
      localObject1 = localObject2;
      if (localObject2 == null) {
        localObject1 = new Bundle();
      }
      ((Bundle)localObject1).putBoolean("android:user_visible_hint", mUserVisibleHint);
    }
    return (Bundle)localObject1;
  }
  
  public Fragment.SavedState saveFragmentInstanceState(Fragment paramFragment)
  {
    Object localObject1 = null;
    if (mIndex < 0) {
      throw new IllegalStateException("Fragment " + paramFragment + " is not currently in the FragmentManager");
    }
    Object localObject2 = localObject1;
    if (mState > 0)
    {
      paramFragment = saveFragmentBasicState(paramFragment);
      localObject2 = localObject1;
      if (paramFragment != null) {
        localObject2 = new Fragment.SavedState(paramFragment);
      }
    }
    return (Fragment.SavedState)localObject2;
  }
  
  void saveFragmentViewState(Fragment paramFragment)
  {
    if (mInnerView == null) {
      return;
    }
    if (mStateArray == null) {
      mStateArray = new SparseArray();
    }
    for (;;)
    {
      mInnerView.saveHierarchyState(mStateArray);
      if (mStateArray.size() <= 0) {
        break;
      }
      mSavedViewState = mStateArray;
      mStateArray = null;
      break;
      mStateArray.clear();
    }
  }
  
  /* Error */
  public void setBackStackIndex(int paramInt, BackStackRecord paramBackStackRecord)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 264	android/support/v4/app/FragmentManagerImpl:mBackStackIndices	Ljava/util/ArrayList;
    //   6: ifnonnull +16 -> 22
    //   9: new 202	java/util/ArrayList
    //   12: astore_3
    //   13: aload_3
    //   14: invokespecial 203	java/util/ArrayList:<init>	()V
    //   17: aload_0
    //   18: aload_3
    //   19: putfield 264	android/support/v4/app/FragmentManagerImpl:mBackStackIndices	Ljava/util/ArrayList;
    //   22: aload_0
    //   23: getfield 264	android/support/v4/app/FragmentManagerImpl:mBackStackIndices	Ljava/util/ArrayList;
    //   26: invokevirtual 262	java/util/ArrayList:size	()I
    //   29: istore 4
    //   31: iload 4
    //   33: istore 5
    //   35: iload_1
    //   36: iload 4
    //   38: if_icmpge +60 -> 98
    //   41: getstatic 91	android/support/v4/app/FragmentManagerImpl:DEBUG	Z
    //   44: ifeq +41 -> 85
    //   47: new 145	java/lang/StringBuilder
    //   50: astore_3
    //   51: aload_3
    //   52: invokespecial 146	java/lang/StringBuilder:<init>	()V
    //   55: ldc 42
    //   57: aload_3
    //   58: ldc_w 266
    //   61: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: iload_1
    //   65: invokevirtual 269	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   68: ldc_w 271
    //   71: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: aload_2
    //   75: invokevirtual 219	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   78: invokevirtual 156	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   81: invokestatic 225	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   84: pop
    //   85: aload_0
    //   86: getfield 264	android/support/v4/app/FragmentManagerImpl:mBackStackIndices	Ljava/util/ArrayList;
    //   89: iload_1
    //   90: aload_2
    //   91: invokevirtual 288	java/util/ArrayList:set	(ILjava/lang/Object;)Ljava/lang/Object;
    //   94: pop
    //   95: aload_0
    //   96: monitorexit
    //   97: return
    //   98: iload 5
    //   100: iload_1
    //   101: if_icmpge +86 -> 187
    //   104: aload_0
    //   105: getfield 264	android/support/v4/app/FragmentManagerImpl:mBackStackIndices	Ljava/util/ArrayList;
    //   108: aconst_null
    //   109: invokevirtual 207	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   112: pop
    //   113: aload_0
    //   114: getfield 258	android/support/v4/app/FragmentManagerImpl:mAvailBackStackIndices	Ljava/util/ArrayList;
    //   117: ifnonnull +16 -> 133
    //   120: new 202	java/util/ArrayList
    //   123: astore_3
    //   124: aload_3
    //   125: invokespecial 203	java/util/ArrayList:<init>	()V
    //   128: aload_0
    //   129: aload_3
    //   130: putfield 258	android/support/v4/app/FragmentManagerImpl:mAvailBackStackIndices	Ljava/util/ArrayList;
    //   133: getstatic 91	android/support/v4/app/FragmentManagerImpl:DEBUG	Z
    //   136: ifeq +32 -> 168
    //   139: new 145	java/lang/StringBuilder
    //   142: astore_3
    //   143: aload_3
    //   144: invokespecial 146	java/lang/StringBuilder:<init>	()V
    //   147: ldc 42
    //   149: aload_3
    //   150: ldc_w 1082
    //   153: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   156: iload 5
    //   158: invokevirtual 269	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   161: invokevirtual 156	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   164: invokestatic 225	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   167: pop
    //   168: aload_0
    //   169: getfield 258	android/support/v4/app/FragmentManagerImpl:mAvailBackStackIndices	Ljava/util/ArrayList;
    //   172: iload 5
    //   174: invokestatic 557	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   177: invokevirtual 207	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   180: pop
    //   181: iinc 5 1
    //   184: goto -86 -> 98
    //   187: getstatic 91	android/support/v4/app/FragmentManagerImpl:DEBUG	Z
    //   190: ifeq +41 -> 231
    //   193: new 145	java/lang/StringBuilder
    //   196: astore_3
    //   197: aload_3
    //   198: invokespecial 146	java/lang/StringBuilder:<init>	()V
    //   201: ldc 42
    //   203: aload_3
    //   204: ldc_w 282
    //   207: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   210: iload_1
    //   211: invokevirtual 269	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   214: ldc_w 284
    //   217: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   220: aload_2
    //   221: invokevirtual 219	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   224: invokevirtual 156	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   227: invokestatic 225	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   230: pop
    //   231: aload_0
    //   232: getfield 264	android/support/v4/app/FragmentManagerImpl:mBackStackIndices	Ljava/util/ArrayList;
    //   235: aload_2
    //   236: invokevirtual 207	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   239: pop
    //   240: goto -145 -> 95
    //   243: astore_2
    //   244: aload_0
    //   245: monitorexit
    //   246: aload_2
    //   247: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	248	0	this	FragmentManagerImpl
    //   0	248	1	paramInt	int
    //   0	248	2	paramBackStackRecord	BackStackRecord
    //   12	192	3	localObject	Object
    //   29	10	4	i	int
    //   33	149	5	j	int
    // Exception table:
    //   from	to	target	type
    //   2	22	243	finally
    //   22	31	243	finally
    //   41	85	243	finally
    //   85	95	243	finally
    //   95	97	243	finally
    //   104	133	243	finally
    //   133	168	243	finally
    //   168	181	243	finally
    //   187	231	243	finally
    //   231	240	243	finally
    //   244	246	243	finally
  }
  
  public void showFragment(Fragment paramFragment, int paramInt1, int paramInt2)
  {
    if (DEBUG) {
      Log.v("FragmentManager", "show: " + paramFragment);
    }
    if (mHidden)
    {
      mHidden = false;
      if (mView != null)
      {
        Animation localAnimation = loadAnimation(paramFragment, paramInt1, true, paramInt2);
        if (localAnimation != null) {
          mView.startAnimation(localAnimation);
        }
        mView.setVisibility(0);
      }
      if ((mAdded) && (mHasMenu) && (mMenuVisible)) {
        mNeedMenuInvalidate = true;
      }
      paramFragment.onHiddenChanged(false);
    }
  }
  
  void startPendingDeferredFragments()
  {
    if (mActive == null) {}
    for (;;)
    {
      return;
      for (int i = 0; i < mActive.size(); i++)
      {
        Fragment localFragment = (Fragment)mActive.get(i);
        if (localFragment != null) {
          performPendingDeferredStart(localFragment);
        }
      }
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("FragmentManager{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" in ");
    DebugUtils.buildShortClassTag(mActivity, localStringBuilder);
    localStringBuilder.append("}}");
    return localStringBuilder.toString();
  }
}

/* Location:
 * Qualified Name:     android.support.v4.app.FragmentManagerImpl
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */