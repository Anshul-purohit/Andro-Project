package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class FragmentActivity
  extends Activity
{
  static final String FRAGMENTS_TAG = "android:support:fragments";
  private static final int HONEYCOMB = 11;
  static final int MSG_REALLY_STOPPED = 1;
  static final int MSG_RESUME_PENDING = 2;
  private static final String TAG = "FragmentActivity";
  HashMap<String, LoaderManagerImpl> mAllLoaderManagers;
  boolean mCheckedForLoaderManager;
  final FragmentContainer mContainer = new FragmentContainer()
  {
    public View findViewById(int paramAnonymousInt)
    {
      return FragmentActivity.this.findViewById(paramAnonymousInt);
    }
  };
  boolean mCreated;
  final FragmentManagerImpl mFragments = new FragmentManagerImpl();
  final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      switch (what)
      {
      default: 
        super.handleMessage(paramAnonymousMessage);
      }
      for (;;)
      {
        return;
        if (mStopped)
        {
          doReallyStop(false);
          continue;
          onResumeFragments();
          mFragments.execPendingActions();
        }
      }
    }
  };
  LoaderManagerImpl mLoaderManager;
  boolean mLoadersStarted;
  boolean mOptionsMenuInvalidated;
  boolean mReallyStopped;
  boolean mResumed;
  boolean mRetaining;
  boolean mStopped;
  
  private void dumpViewHierarchy(String paramString, PrintWriter paramPrintWriter, View paramView)
  {
    paramPrintWriter.print(paramString);
    if (paramView == null) {
      paramPrintWriter.println("null");
    }
    for (;;)
    {
      return;
      paramPrintWriter.println(viewToString(paramView));
      if ((paramView instanceof ViewGroup))
      {
        paramView = (ViewGroup)paramView;
        int i = paramView.getChildCount();
        if (i > 0)
        {
          paramString = paramString + "  ";
          for (int j = 0; j < i; j++) {
            dumpViewHierarchy(paramString, paramPrintWriter, paramView.getChildAt(j));
          }
        }
      }
    }
  }
  
  private static String viewToString(View paramView)
  {
    char c1 = 'F';
    int i = 46;
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append(paramView.getClass().getName());
    localStringBuilder.append('{');
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(paramView)));
    localStringBuilder.append(' ');
    char c2;
    char c3;
    label114:
    label136:
    label158:
    label180:
    label202:
    label224:
    label246:
    label270:
    label290:
    Object localObject;
    switch (paramView.getVisibility())
    {
    default: 
      localStringBuilder.append('.');
      if (paramView.isFocusable())
      {
        c2 = 'F';
        c3 = c2;
        localStringBuilder.append(c3);
        if (!paramView.isEnabled()) {
          break label573;
        }
        c2 = 'E';
        c3 = c2;
        localStringBuilder.append(c3);
        if (!paramView.willNotDraw()) {
          break label584;
        }
        c2 = '.';
        c3 = c2;
        localStringBuilder.append(c3);
        if (!paramView.isHorizontalScrollBarEnabled()) {
          break label595;
        }
        c2 = 'H';
        c3 = c2;
        localStringBuilder.append(c3);
        if (!paramView.isVerticalScrollBarEnabled()) {
          break label606;
        }
        c2 = 'V';
        c3 = c2;
        localStringBuilder.append(c3);
        if (!paramView.isClickable()) {
          break label617;
        }
        c2 = 'C';
        c3 = c2;
        localStringBuilder.append(c3);
        if (!paramView.isLongClickable()) {
          break label628;
        }
        c2 = 'L';
        c3 = c2;
        localStringBuilder.append(c3);
        localStringBuilder.append(' ');
        if (!paramView.isFocused()) {
          break label639;
        }
        c3 = c1;
        localStringBuilder.append(c3);
        if (!paramView.isSelected()) {
          break label648;
        }
        c1 = 'S';
        c3 = c1;
        localStringBuilder.append(c3);
        c3 = i;
        if (paramView.isPressed())
        {
          i = 80;
          c3 = i;
        }
        localStringBuilder.append(c3);
        localStringBuilder.append(' ');
        localStringBuilder.append(paramView.getLeft());
        localStringBuilder.append(',');
        localStringBuilder.append(paramView.getTop());
        localStringBuilder.append('-');
        localStringBuilder.append(paramView.getRight());
        localStringBuilder.append(',');
        localStringBuilder.append(paramView.getBottom());
        i = paramView.getId();
        if (i != -1)
        {
          localStringBuilder.append(" #");
          localStringBuilder.append(Integer.toHexString(i));
          localObject = paramView.getResources();
          if ((i != 0) && (localObject != null)) {
            switch (0xFF000000 & i)
            {
            }
          }
        }
      }
      break;
    }
    for (;;)
    {
      try
      {
        paramView = ((Resources)localObject).getResourcePackageName(i);
        String str = ((Resources)localObject).getResourceTypeName(i);
        localObject = ((Resources)localObject).getResourceEntryName(i);
        localStringBuilder.append(" ");
        localStringBuilder.append(paramView);
        localStringBuilder.append(":");
        localStringBuilder.append(str);
        localStringBuilder.append("/");
        localStringBuilder.append((String)localObject);
      }
      catch (Resources.NotFoundException paramView)
      {
        label573:
        label584:
        label595:
        label606:
        label617:
        label628:
        label639:
        label648:
        continue;
      }
      localStringBuilder.append("}");
      return localStringBuilder.toString();
      localStringBuilder.append('V');
      break;
      localStringBuilder.append('I');
      break;
      localStringBuilder.append('G');
      break;
      c2 = '.';
      c3 = c2;
      break label114;
      c2 = '.';
      c3 = c2;
      break label136;
      c2 = 'D';
      c3 = c2;
      break label158;
      c2 = '.';
      c3 = c2;
      break label180;
      c2 = '.';
      c3 = c2;
      break label202;
      c2 = '.';
      c3 = c2;
      break label224;
      c2 = '.';
      c3 = c2;
      break label246;
      c1 = '.';
      c3 = c1;
      break label270;
      c1 = '.';
      c3 = c1;
      break label290;
      paramView = "app";
      continue;
      paramView = "android";
    }
  }
  
  void doReallyStop(boolean paramBoolean)
  {
    if (!mReallyStopped)
    {
      mReallyStopped = true;
      mRetaining = paramBoolean;
      mHandler.removeMessages(1);
      onReallyStop();
    }
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    if (Build.VERSION.SDK_INT >= 11) {}
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("Local FragmentActivity ");
    paramPrintWriter.print(Integer.toHexString(System.identityHashCode(this)));
    paramPrintWriter.println(" State:");
    String str = paramString + "  ";
    paramPrintWriter.print(str);
    paramPrintWriter.print("mCreated=");
    paramPrintWriter.print(mCreated);
    paramPrintWriter.print("mResumed=");
    paramPrintWriter.print(mResumed);
    paramPrintWriter.print(" mStopped=");
    paramPrintWriter.print(mStopped);
    paramPrintWriter.print(" mReallyStopped=");
    paramPrintWriter.println(mReallyStopped);
    paramPrintWriter.print(str);
    paramPrintWriter.print("mLoadersStarted=");
    paramPrintWriter.println(mLoadersStarted);
    if (mLoaderManager != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("Loader Manager ");
      paramPrintWriter.print(Integer.toHexString(System.identityHashCode(mLoaderManager)));
      paramPrintWriter.println(":");
      mLoaderManager.dump(paramString + "  ", paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    mFragments.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("View Hierarchy:");
    dumpViewHierarchy(paramString + "  ", paramPrintWriter, getWindow().getDecorView());
  }
  
  public Object getLastCustomNonConfigurationInstance()
  {
    Object localObject = (NonConfigurationInstances)getLastNonConfigurationInstance();
    if (localObject != null) {}
    for (localObject = custom;; localObject = null) {
      return localObject;
    }
  }
  
  LoaderManagerImpl getLoaderManager(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (mAllLoaderManagers == null) {
      mAllLoaderManagers = new HashMap();
    }
    LoaderManagerImpl localLoaderManagerImpl = (LoaderManagerImpl)mAllLoaderManagers.get(paramString);
    if (localLoaderManagerImpl == null) {
      if (paramBoolean2)
      {
        localLoaderManagerImpl = new LoaderManagerImpl(paramString, this, paramBoolean1);
        mAllLoaderManagers.put(paramString, localLoaderManagerImpl);
      }
    }
    for (;;)
    {
      return localLoaderManagerImpl;
      localLoaderManagerImpl.updateActivity(this);
    }
  }
  
  public FragmentManager getSupportFragmentManager()
  {
    return mFragments;
  }
  
  public LoaderManager getSupportLoaderManager()
  {
    if (mLoaderManager != null) {}
    for (LoaderManagerImpl localLoaderManagerImpl = mLoaderManager;; localLoaderManagerImpl = mLoaderManager)
    {
      return localLoaderManagerImpl;
      mCheckedForLoaderManager = true;
      mLoaderManager = getLoaderManager(null, mLoadersStarted, true);
    }
  }
  
  void invalidateSupportFragment(String paramString)
  {
    if (mAllLoaderManagers != null)
    {
      LoaderManagerImpl localLoaderManagerImpl = (LoaderManagerImpl)mAllLoaderManagers.get(paramString);
      if ((localLoaderManagerImpl != null) && (!mRetaining))
      {
        localLoaderManagerImpl.doDestroy();
        mAllLoaderManagers.remove(paramString);
      }
    }
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    mFragments.noteStateNotSaved();
    int i = paramInt1 >> 16;
    if (i != 0)
    {
      i--;
      if ((mFragments.mActive == null) || (i < 0) || (i >= mFragments.mActive.size())) {
        Log.w("FragmentActivity", "Activity result fragment index out of range: 0x" + Integer.toHexString(paramInt1));
      }
    }
    for (;;)
    {
      return;
      Fragment localFragment = (Fragment)mFragments.mActive.get(i);
      if (localFragment == null)
      {
        Log.w("FragmentActivity", "Activity result no fragment exists for index: 0x" + Integer.toHexString(paramInt1));
      }
      else
      {
        localFragment.onActivityResult(0xFFFF & paramInt1, paramInt2, paramIntent);
        continue;
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
      }
    }
  }
  
  public void onAttachFragment(Fragment paramFragment) {}
  
  public void onBackPressed()
  {
    if (!mFragments.popBackStackImmediate()) {
      finish();
    }
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    mFragments.dispatchConfigurationChanged(paramConfiguration);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    Object localObject = null;
    mFragments.attachActivity(this, mContainer, null);
    if (getLayoutInflater().getFactory() == null) {
      getLayoutInflater().setFactory(this);
    }
    super.onCreate(paramBundle);
    NonConfigurationInstances localNonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
    if (localNonConfigurationInstances != null) {
      mAllLoaderManagers = loaders;
    }
    if (paramBundle != null)
    {
      Parcelable localParcelable = paramBundle.getParcelable("android:support:fragments");
      FragmentManagerImpl localFragmentManagerImpl = mFragments;
      paramBundle = (Bundle)localObject;
      if (localNonConfigurationInstances != null) {
        paramBundle = fragments;
      }
      localFragmentManagerImpl.restoreAllState(localParcelable, paramBundle);
    }
    mFragments.dispatchCreate();
  }
  
  public boolean onCreatePanelMenu(int paramInt, Menu paramMenu)
  {
    boolean bool;
    if (paramInt == 0)
    {
      bool = super.onCreatePanelMenu(paramInt, paramMenu) | mFragments.dispatchCreateOptionsMenu(paramMenu, getMenuInflater());
      if (Build.VERSION.SDK_INT < 11) {}
    }
    for (;;)
    {
      return bool;
      bool = true;
      continue;
      bool = super.onCreatePanelMenu(paramInt, paramMenu);
    }
  }
  
  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    Object localObject = null;
    if (!"fragment".equals(paramString)) {}
    for (paramString = super.onCreateView(paramString, paramContext, paramAttributeSet);; paramString = mView)
    {
      return paramString;
      paramString = paramAttributeSet.getAttributeValue(null, "class");
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, FragmentTag.Fragment);
      String str1 = paramString;
      if (paramString == null) {
        str1 = paramContext.getString(0);
      }
      int i = paramContext.getResourceId(1, -1);
      String str2 = paramContext.getString(2);
      paramContext.recycle();
      if (0 != 0) {
        throw new NullPointerException();
      }
      if ((-1 == 0) && (i == -1) && (str2 == null)) {
        throw new IllegalArgumentException(paramAttributeSet.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + str1);
      }
      paramContext = (Context)localObject;
      if (i != -1) {
        paramContext = mFragments.findFragmentById(i);
      }
      paramString = paramContext;
      if (paramContext == null)
      {
        paramString = paramContext;
        if (str2 != null) {
          paramString = mFragments.findFragmentByTag(str2);
        }
      }
      paramContext = paramString;
      if (paramString == null)
      {
        paramContext = paramString;
        if (-1 != 0) {
          paramContext = mFragments.findFragmentById(0);
        }
      }
      if (FragmentManagerImpl.DEBUG) {
        Log.v("FragmentActivity", "onCreateView: id=0x" + Integer.toHexString(i) + " fname=" + str1 + " existing=" + paramContext);
      }
      int j;
      if (paramContext == null)
      {
        paramContext = Fragment.instantiate(this, str1);
        mFromLayout = true;
        if (i != 0)
        {
          j = i;
          mFragmentId = j;
          mContainerId = 0;
          mTag = str2;
          mInLayout = true;
          mFragmentManager = mFragments;
          paramContext.onInflate(this, paramAttributeSet, mSavedFragmentState);
          mFragments.addFragment(paramContext, true);
        }
      }
      for (;;)
      {
        if (mView != null) {
          break label499;
        }
        throw new IllegalStateException("Fragment " + str1 + " did not create a view.");
        j = 0;
        break;
        if (mInLayout) {
          throw new IllegalArgumentException(paramAttributeSet.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(i) + ", tag " + str2 + ", or parent id 0x" + Integer.toHexString(0) + " with another fragment for " + str1);
        }
        mInLayout = true;
        if (!mRetaining) {
          paramContext.onInflate(this, paramAttributeSet, mSavedFragmentState);
        }
        mFragments.moveToState(paramContext);
      }
      label499:
      if (i != 0) {
        mView.setId(i);
      }
      if (mView.getTag() == null) {
        mView.setTag(str2);
      }
    }
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    doReallyStop(false);
    mFragments.dispatchDestroy();
    if (mLoaderManager != null) {
      mLoaderManager.doDestroy();
    }
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((Build.VERSION.SDK_INT < 5) && (paramInt == 4) && (paramKeyEvent.getRepeatCount() == 0)) {
      onBackPressed();
    }
    for (boolean bool = true;; bool = super.onKeyDown(paramInt, paramKeyEvent)) {
      return bool;
    }
  }
  
  public void onLowMemory()
  {
    super.onLowMemory();
    mFragments.dispatchLowMemory();
  }
  
  public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem)
  {
    boolean bool;
    if (super.onMenuItemSelected(paramInt, paramMenuItem)) {
      bool = true;
    }
    for (;;)
    {
      return bool;
      switch (paramInt)
      {
      default: 
        bool = false;
        break;
      case 0: 
        bool = mFragments.dispatchOptionsItemSelected(paramMenuItem);
        break;
      case 6: 
        bool = mFragments.dispatchContextItemSelected(paramMenuItem);
      }
    }
  }
  
  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    mFragments.noteStateNotSaved();
  }
  
  public void onPanelClosed(int paramInt, Menu paramMenu)
  {
    switch (paramInt)
    {
    }
    for (;;)
    {
      super.onPanelClosed(paramInt, paramMenu);
      return;
      mFragments.dispatchOptionsMenuClosed(paramMenu);
    }
  }
  
  protected void onPause()
  {
    super.onPause();
    mResumed = false;
    if (mHandler.hasMessages(2))
    {
      mHandler.removeMessages(2);
      onResumeFragments();
    }
    mFragments.dispatchPause();
  }
  
  protected void onPostResume()
  {
    super.onPostResume();
    mHandler.removeMessages(2);
    onResumeFragments();
    mFragments.execPendingActions();
  }
  
  public boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu)
  {
    boolean bool1 = false;
    if ((paramInt == 0) && (paramMenu != null))
    {
      if (mOptionsMenuInvalidated)
      {
        mOptionsMenuInvalidated = false;
        paramMenu.clear();
        onCreatePanelMenu(paramInt, paramMenu);
      }
      bool2 = bool1;
      if ((super.onPreparePanel(paramInt, paramView, paramMenu) | mFragments.dispatchPrepareOptionsMenu(paramMenu)))
      {
        bool2 = bool1;
        if (!paramMenu.hasVisibleItems()) {}
      }
    }
    for (boolean bool2 = true;; bool2 = super.onPreparePanel(paramInt, paramView, paramMenu)) {
      return bool2;
    }
  }
  
  void onReallyStop()
  {
    if (mLoadersStarted)
    {
      mLoadersStarted = false;
      if (mLoaderManager != null)
      {
        if (mRetaining) {
          break label41;
        }
        mLoaderManager.doStop();
      }
    }
    for (;;)
    {
      mFragments.dispatchReallyStop();
      return;
      label41:
      mLoaderManager.doRetain();
    }
  }
  
  protected void onResume()
  {
    super.onResume();
    mHandler.sendEmptyMessage(2);
    mResumed = true;
    mFragments.execPendingActions();
  }
  
  protected void onResumeFragments()
  {
    mFragments.dispatchResume();
  }
  
  public Object onRetainCustomNonConfigurationInstance()
  {
    return null;
  }
  
  public final Object onRetainNonConfigurationInstance()
  {
    if (mStopped) {
      doReallyStop(true);
    }
    Object localObject1 = onRetainCustomNonConfigurationInstance();
    ArrayList localArrayList = mFragments.retainNonConfig();
    int i = 0;
    int j = 0;
    int k = i;
    Object localObject2;
    if (mAllLoaderManagers != null)
    {
      LoaderManagerImpl[] arrayOfLoaderManagerImpl = new LoaderManagerImpl[mAllLoaderManagers.size()];
      mAllLoaderManagers.values().toArray(arrayOfLoaderManagerImpl);
      k = i;
      if (arrayOfLoaderManagerImpl != null)
      {
        i = 0;
        k = j;
        if (i < arrayOfLoaderManagerImpl.length)
        {
          localObject2 = arrayOfLoaderManagerImpl[i];
          if (mRetaining) {
            j = 1;
          }
          for (;;)
          {
            i++;
            break;
            ((LoaderManagerImpl)localObject2).doDestroy();
            mAllLoaderManagers.remove(mWho);
          }
        }
      }
    }
    if ((localArrayList == null) && (k == 0) && (localObject1 == null)) {
      localObject2 = null;
    }
    for (;;)
    {
      return localObject2;
      localObject2 = new NonConfigurationInstances();
      activity = null;
      custom = localObject1;
      children = null;
      fragments = localArrayList;
      loaders = mAllLoaderManagers;
    }
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    Parcelable localParcelable = mFragments.saveAllState();
    if (localParcelable != null) {
      paramBundle.putParcelable("android:support:fragments", localParcelable);
    }
  }
  
  protected void onStart()
  {
    super.onStart();
    mStopped = false;
    mReallyStopped = false;
    mHandler.removeMessages(1);
    if (!mCreated)
    {
      mCreated = true;
      mFragments.dispatchActivityCreated();
    }
    mFragments.noteStateNotSaved();
    mFragments.execPendingActions();
    if (!mLoadersStarted)
    {
      mLoadersStarted = true;
      if (mLoaderManager == null) {
        break label156;
      }
      mLoaderManager.doStart();
    }
    for (;;)
    {
      mCheckedForLoaderManager = true;
      mFragments.dispatchStart();
      if (mAllLoaderManagers == null) {
        break;
      }
      LoaderManagerImpl[] arrayOfLoaderManagerImpl = new LoaderManagerImpl[mAllLoaderManagers.size()];
      mAllLoaderManagers.values().toArray(arrayOfLoaderManagerImpl);
      if (arrayOfLoaderManagerImpl == null) {
        break;
      }
      for (int i = 0; i < arrayOfLoaderManagerImpl.length; i++)
      {
        LoaderManagerImpl localLoaderManagerImpl = arrayOfLoaderManagerImpl[i];
        localLoaderManagerImpl.finishRetain();
        localLoaderManagerImpl.doReportStart();
      }
      label156:
      if (!mCheckedForLoaderManager)
      {
        mLoaderManager = getLoaderManager(null, mLoadersStarted, false);
        if ((mLoaderManager != null) && (!mLoaderManager.mStarted)) {
          mLoaderManager.doStart();
        }
      }
    }
  }
  
  protected void onStop()
  {
    super.onStop();
    mStopped = true;
    mHandler.sendEmptyMessage(1);
    mFragments.dispatchStop();
  }
  
  public void startActivityForResult(Intent paramIntent, int paramInt)
  {
    if ((paramInt != -1) && ((0xFFFF0000 & paramInt) != 0)) {
      throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
    }
    super.startActivityForResult(paramIntent, paramInt);
  }
  
  public void startActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt)
  {
    if (paramInt == -1) {
      super.startActivityForResult(paramIntent, -1);
    }
    for (;;)
    {
      return;
      if ((0xFFFF0000 & paramInt) != 0) {
        throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
      }
      super.startActivityForResult(paramIntent, (mIndex + 1 << 16) + (0xFFFF & paramInt));
    }
  }
  
  public void supportInvalidateOptionsMenu()
  {
    if (Build.VERSION.SDK_INT >= 11) {
      ActivityCompatHoneycomb.invalidateOptionsMenu(this);
    }
    for (;;)
    {
      return;
      mOptionsMenuInvalidated = true;
    }
  }
  
  static class FragmentTag
  {
    public static final int[] Fragment = { 16842755, 16842960, 16842961 };
    public static final int Fragment_id = 1;
    public static final int Fragment_name = 0;
    public static final int Fragment_tag = 2;
  }
  
  static final class NonConfigurationInstances
  {
    Object activity;
    HashMap<String, Object> children;
    Object custom;
    ArrayList<Fragment> fragments;
    HashMap<String, LoaderManagerImpl> loaders;
  }
}

/* Location:
 * Qualified Name:     android.support.v4.app.FragmentActivity
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */