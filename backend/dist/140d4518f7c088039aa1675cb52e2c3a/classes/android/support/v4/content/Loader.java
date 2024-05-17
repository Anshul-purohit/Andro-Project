package android.support.v4.content;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.v4.util.DebugUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class Loader<D>
{
  boolean mAbandoned = false;
  boolean mContentChanged = false;
  Context mContext;
  int mId;
  OnLoadCompleteListener<D> mListener;
  boolean mReset = true;
  boolean mStarted = false;
  
  public Loader(Context paramContext)
  {
    mContext = paramContext.getApplicationContext();
  }
  
  public void abandon()
  {
    mAbandoned = true;
    onAbandon();
  }
  
  public String dataToString(D paramD)
  {
    StringBuilder localStringBuilder = new StringBuilder(64);
    DebugUtils.buildShortClassTag(paramD, localStringBuilder);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void deliverResult(D paramD)
  {
    if (mListener != null) {
      mListener.onLoadComplete(this, paramD);
    }
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mId=");
    paramPrintWriter.print(mId);
    paramPrintWriter.print(" mListener=");
    paramPrintWriter.println(mListener);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mStarted=");
    paramPrintWriter.print(mStarted);
    paramPrintWriter.print(" mContentChanged=");
    paramPrintWriter.print(mContentChanged);
    paramPrintWriter.print(" mAbandoned=");
    paramPrintWriter.print(mAbandoned);
    paramPrintWriter.print(" mReset=");
    paramPrintWriter.println(mReset);
  }
  
  public void forceLoad()
  {
    onForceLoad();
  }
  
  public Context getContext()
  {
    return mContext;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public boolean isAbandoned()
  {
    return mAbandoned;
  }
  
  public boolean isReset()
  {
    return mReset;
  }
  
  public boolean isStarted()
  {
    return mStarted;
  }
  
  protected void onAbandon() {}
  
  public void onContentChanged()
  {
    if (mStarted) {
      forceLoad();
    }
    for (;;)
    {
      return;
      mContentChanged = true;
    }
  }
  
  protected void onForceLoad() {}
  
  protected void onReset() {}
  
  protected void onStartLoading() {}
  
  protected void onStopLoading() {}
  
  public void registerListener(int paramInt, OnLoadCompleteListener<D> paramOnLoadCompleteListener)
  {
    if (mListener != null) {
      throw new IllegalStateException("There is already a listener registered");
    }
    mListener = paramOnLoadCompleteListener;
    mId = paramInt;
  }
  
  public void reset()
  {
    onReset();
    mReset = true;
    mStarted = false;
    mAbandoned = false;
    mContentChanged = false;
  }
  
  public final void startLoading()
  {
    mStarted = true;
    mReset = false;
    mAbandoned = false;
    onStartLoading();
  }
  
  public void stopLoading()
  {
    mStarted = false;
    onStopLoading();
  }
  
  public boolean takeContentChanged()
  {
    boolean bool = mContentChanged;
    mContentChanged = false;
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(64);
    DebugUtils.buildShortClassTag(this, localStringBuilder);
    localStringBuilder.append(" id=");
    localStringBuilder.append(mId);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void unregisterListener(OnLoadCompleteListener<D> paramOnLoadCompleteListener)
  {
    if (mListener == null) {
      throw new IllegalStateException("No listener register");
    }
    if (mListener != paramOnLoadCompleteListener) {
      throw new IllegalArgumentException("Attempting to unregister the wrong listener");
    }
    mListener = null;
  }
  
  public final class ForceLoadContentObserver
    extends ContentObserver
  {
    public ForceLoadContentObserver()
    {
      super();
    }
    
    public boolean deliverSelfNotifications()
    {
      return true;
    }
    
    public void onChange(boolean paramBoolean)
    {
      onContentChanged();
    }
  }
  
  public static abstract interface OnLoadCompleteListener<D>
  {
    public abstract void onLoadComplete(Loader<D> paramLoader, D paramD);
  }
}

/* Location:
 * Qualified Name:     android.support.v4.content.Loader
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */