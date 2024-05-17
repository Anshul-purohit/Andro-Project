package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;

final class BackStackState
  implements Parcelable
{
  public static final Parcelable.Creator<BackStackState> CREATOR = new Parcelable.Creator()
  {
    public BackStackState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BackStackState(paramAnonymousParcel);
    }
    
    public BackStackState[] newArray(int paramAnonymousInt)
    {
      return new BackStackState[paramAnonymousInt];
    }
  };
  final int mBreadCrumbShortTitleRes;
  final CharSequence mBreadCrumbShortTitleText;
  final int mBreadCrumbTitleRes;
  final CharSequence mBreadCrumbTitleText;
  final int mIndex;
  final String mName;
  final int[] mOps;
  final int mTransition;
  final int mTransitionStyle;
  
  public BackStackState(Parcel paramParcel)
  {
    mOps = paramParcel.createIntArray();
    mTransition = paramParcel.readInt();
    mTransitionStyle = paramParcel.readInt();
    mName = paramParcel.readString();
    mIndex = paramParcel.readInt();
    mBreadCrumbTitleRes = paramParcel.readInt();
    mBreadCrumbTitleText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mBreadCrumbShortTitleRes = paramParcel.readInt();
    mBreadCrumbShortTitleText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
  }
  
  public BackStackState(FragmentManagerImpl paramFragmentManagerImpl, BackStackRecord paramBackStackRecord)
  {
    int i = 0;
    paramFragmentManagerImpl = mHead;
    int j;
    while (paramFragmentManagerImpl != null)
    {
      j = i;
      if (removed != null) {
        j = i + removed.size();
      }
      paramFragmentManagerImpl = next;
      i = j;
    }
    mOps = new int[mNumOp * 7 + i];
    if (!mAddToBackStack) {
      throw new IllegalStateException("Not on back stack");
    }
    paramFragmentManagerImpl = mHead;
    i = 0;
    if (paramFragmentManagerImpl != null)
    {
      int[] arrayOfInt = mOps;
      j = i + 1;
      arrayOfInt[i] = cmd;
      arrayOfInt = mOps;
      int k = j + 1;
      if (fragment != null) {}
      int m;
      for (i = fragment.mIndex;; i = -1)
      {
        arrayOfInt[j] = i;
        arrayOfInt = mOps;
        i = k + 1;
        arrayOfInt[k] = enterAnim;
        arrayOfInt = mOps;
        j = i + 1;
        arrayOfInt[i] = exitAnim;
        arrayOfInt = mOps;
        i = j + 1;
        arrayOfInt[j] = popEnterAnim;
        arrayOfInt = mOps;
        m = i + 1;
        arrayOfInt[i] = popExitAnim;
        if (removed == null) {
          break label303;
        }
        k = removed.size();
        mOps[m] = k;
        j = 0;
        for (i = m + 1; j < k; i++)
        {
          mOps[i] = removed.get(j)).mIndex;
          j++;
        }
      }
      for (;;)
      {
        paramFragmentManagerImpl = next;
        break;
        label303:
        arrayOfInt = mOps;
        i = m + 1;
        arrayOfInt[m] = 0;
      }
    }
    mTransition = mTransition;
    mTransitionStyle = mTransitionStyle;
    mName = mName;
    mIndex = mIndex;
    mBreadCrumbTitleRes = mBreadCrumbTitleRes;
    mBreadCrumbTitleText = mBreadCrumbTitleText;
    mBreadCrumbShortTitleRes = mBreadCrumbShortTitleRes;
    mBreadCrumbShortTitleText = mBreadCrumbShortTitleText;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public BackStackRecord instantiate(FragmentManagerImpl paramFragmentManagerImpl)
  {
    BackStackRecord localBackStackRecord = new BackStackRecord(paramFragmentManagerImpl);
    int i = 0;
    for (int j = 0; i < mOps.length; j++)
    {
      BackStackRecord.Op localOp = new BackStackRecord.Op();
      Object localObject = mOps;
      int k = i + 1;
      cmd = localObject[i];
      if (FragmentManagerImpl.DEBUG) {
        Log.v("FragmentManager", "Instantiate " + localBackStackRecord + " op #" + j + " base fragment #" + mOps[k]);
      }
      localObject = mOps;
      i = k + 1;
      k = localObject[k];
      if (k >= 0) {}
      for (fragment = ((Fragment)mActive.get(k));; fragment = null)
      {
        localObject = mOps;
        k = i + 1;
        enterAnim = localObject[i];
        localObject = mOps;
        int m = k + 1;
        exitAnim = localObject[k];
        localObject = mOps;
        i = m + 1;
        popEnterAnim = localObject[m];
        localObject = mOps;
        k = i + 1;
        popExitAnim = localObject[i];
        localObject = mOps;
        i = k + 1;
        int n = localObject[k];
        k = i;
        if (n <= 0) {
          break;
        }
        removed = new ArrayList(n);
        m = 0;
        for (;;)
        {
          k = i;
          if (m >= n) {
            break;
          }
          if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "Instantiate " + localBackStackRecord + " set remove fragment #" + mOps[i]);
          }
          localObject = (Fragment)mActive.get(mOps[i]);
          removed.add(localObject);
          m++;
          i++;
        }
      }
      i = k;
      localBackStackRecord.addOp(localOp);
    }
    mTransition = mTransition;
    mTransitionStyle = mTransitionStyle;
    mName = mName;
    mIndex = mIndex;
    mAddToBackStack = true;
    mBreadCrumbTitleRes = mBreadCrumbTitleRes;
    mBreadCrumbTitleText = mBreadCrumbTitleText;
    mBreadCrumbShortTitleRes = mBreadCrumbShortTitleRes;
    mBreadCrumbShortTitleText = mBreadCrumbShortTitleText;
    localBackStackRecord.bumpBackStackNesting(1);
    return localBackStackRecord;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeIntArray(mOps);
    paramParcel.writeInt(mTransition);
    paramParcel.writeInt(mTransitionStyle);
    paramParcel.writeString(mName);
    paramParcel.writeInt(mIndex);
    paramParcel.writeInt(mBreadCrumbTitleRes);
    TextUtils.writeToParcel(mBreadCrumbTitleText, paramParcel, 0);
    paramParcel.writeInt(mBreadCrumbShortTitleRes);
    TextUtils.writeToParcel(mBreadCrumbShortTitleText, paramParcel, 0);
  }
}

/* Location:
 * Qualified Name:     android.support.v4.app.BackStackState
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */