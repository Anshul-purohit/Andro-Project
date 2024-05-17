package android.support.v4.util;

public class LongSparseArray<E>
  implements Cloneable
{
  private static final Object DELETED = new Object();
  private boolean mGarbage = false;
  private long[] mKeys;
  private int mSize;
  private Object[] mValues;
  
  public LongSparseArray()
  {
    this(10);
  }
  
  public LongSparseArray(int paramInt)
  {
    paramInt = idealLongArraySize(paramInt);
    mKeys = new long[paramInt];
    mValues = new Object[paramInt];
    mSize = 0;
  }
  
  private static int binarySearch(long[] paramArrayOfLong, int paramInt1, int paramInt2, long paramLong)
  {
    int i = paramInt1 + paramInt2;
    int j = paramInt1 - 1;
    while (i - j > 1)
    {
      int k = (i + j) / 2;
      if (paramArrayOfLong[k] < paramLong) {
        j = k;
      } else {
        i = k;
      }
    }
    if (i == paramInt1 + paramInt2) {
      paramInt1 = paramInt1 + paramInt2 ^ 0xFFFFFFFF;
    }
    for (;;)
    {
      return paramInt1;
      paramInt1 = i;
      if (paramArrayOfLong[i] != paramLong) {
        paramInt1 = i ^ 0xFFFFFFFF;
      }
    }
  }
  
  private void gc()
  {
    int i = mSize;
    int j = 0;
    long[] arrayOfLong = mKeys;
    Object[] arrayOfObject = mValues;
    int k = 0;
    while (k < i)
    {
      Object localObject = arrayOfObject[k];
      int m = j;
      if (localObject != DELETED)
      {
        if (k != j)
        {
          arrayOfLong[j] = arrayOfLong[k];
          arrayOfObject[j] = localObject;
          arrayOfObject[k] = null;
        }
        m = j + 1;
      }
      k++;
      j = m;
    }
    mGarbage = false;
    mSize = j;
  }
  
  public static int idealByteArraySize(int paramInt)
  {
    for (int i = 4;; i++)
    {
      int j = paramInt;
      if (i < 32)
      {
        if (paramInt <= (1 << i) - 12) {
          j = (1 << i) - 12;
        }
      }
      else {
        return j;
      }
    }
  }
  
  public static int idealLongArraySize(int paramInt)
  {
    return idealByteArraySize(paramInt * 8) / 8;
  }
  
  public void append(long paramLong, E paramE)
  {
    if ((mSize != 0) && (paramLong <= mKeys[(mSize - 1)])) {
      put(paramLong, paramE);
    }
    for (;;)
    {
      return;
      if ((mGarbage) && (mSize >= mKeys.length)) {
        gc();
      }
      int i = mSize;
      if (i >= mKeys.length)
      {
        int j = idealLongArraySize(i + 1);
        long[] arrayOfLong = new long[j];
        Object[] arrayOfObject = new Object[j];
        System.arraycopy(mKeys, 0, arrayOfLong, 0, mKeys.length);
        System.arraycopy(mValues, 0, arrayOfObject, 0, mValues.length);
        mKeys = arrayOfLong;
        mValues = arrayOfObject;
      }
      mKeys[i] = paramLong;
      mValues[i] = paramE;
      mSize = (i + 1);
    }
  }
  
  public void clear()
  {
    int i = mSize;
    Object[] arrayOfObject = mValues;
    for (int j = 0; j < i; j++) {
      arrayOfObject[j] = null;
    }
    mSize = 0;
    mGarbage = false;
  }
  
  public LongSparseArray<E> clone()
  {
    Object localObject = null;
    try
    {
      LongSparseArray localLongSparseArray = (LongSparseArray)super.clone();
      localObject = localLongSparseArray;
      mKeys = ((long[])mKeys.clone());
      localObject = localLongSparseArray;
      mValues = ((Object[])mValues.clone());
      localObject = localLongSparseArray;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
      for (;;) {}
    }
    return (LongSparseArray<E>)localObject;
  }
  
  public void delete(long paramLong)
  {
    int i = binarySearch(mKeys, 0, mSize, paramLong);
    if ((i >= 0) && (mValues[i] != DELETED))
    {
      mValues[i] = DELETED;
      mGarbage = true;
    }
  }
  
  public E get(long paramLong)
  {
    return (E)get(paramLong, null);
  }
  
  public E get(long paramLong, E paramE)
  {
    int i = binarySearch(mKeys, 0, mSize, paramLong);
    Object localObject = paramE;
    if (i >= 0) {
      if (mValues[i] != DELETED) {
        break label42;
      }
    }
    label42:
    for (localObject = paramE;; localObject = mValues[i]) {
      return (E)localObject;
    }
  }
  
  public int indexOfKey(long paramLong)
  {
    if (mGarbage) {
      gc();
    }
    return binarySearch(mKeys, 0, mSize, paramLong);
  }
  
  public int indexOfValue(E paramE)
  {
    if (mGarbage) {
      gc();
    }
    int i = 0;
    if (i < mSize) {
      if (mValues[i] != paramE) {}
    }
    for (;;)
    {
      return i;
      i++;
      break;
      i = -1;
    }
  }
  
  public long keyAt(int paramInt)
  {
    if (mGarbage) {
      gc();
    }
    return mKeys[paramInt];
  }
  
  public void put(long paramLong, E paramE)
  {
    int i = binarySearch(mKeys, 0, mSize, paramLong);
    if (i >= 0) {
      mValues[i] = paramE;
    }
    for (;;)
    {
      return;
      int j = i ^ 0xFFFFFFFF;
      if ((j < mSize) && (mValues[j] == DELETED))
      {
        mKeys[j] = paramLong;
        mValues[j] = paramE;
      }
      else
      {
        i = j;
        if (mGarbage)
        {
          i = j;
          if (mSize >= mKeys.length)
          {
            gc();
            i = binarySearch(mKeys, 0, mSize, paramLong) ^ 0xFFFFFFFF;
          }
        }
        if (mSize >= mKeys.length)
        {
          j = idealLongArraySize(mSize + 1);
          long[] arrayOfLong = new long[j];
          Object[] arrayOfObject = new Object[j];
          System.arraycopy(mKeys, 0, arrayOfLong, 0, mKeys.length);
          System.arraycopy(mValues, 0, arrayOfObject, 0, mValues.length);
          mKeys = arrayOfLong;
          mValues = arrayOfObject;
        }
        if (mSize - i != 0)
        {
          System.arraycopy(mKeys, i, mKeys, i + 1, mSize - i);
          System.arraycopy(mValues, i, mValues, i + 1, mSize - i);
        }
        mKeys[i] = paramLong;
        mValues[i] = paramE;
        mSize += 1;
      }
    }
  }
  
  public void remove(long paramLong)
  {
    delete(paramLong);
  }
  
  public void removeAt(int paramInt)
  {
    if (mValues[paramInt] != DELETED)
    {
      mValues[paramInt] = DELETED;
      mGarbage = true;
    }
  }
  
  public void setValueAt(int paramInt, E paramE)
  {
    if (mGarbage) {
      gc();
    }
    mValues[paramInt] = paramE;
  }
  
  public int size()
  {
    if (mGarbage) {
      gc();
    }
    return mSize;
  }
  
  public E valueAt(int paramInt)
  {
    if (mGarbage) {
      gc();
    }
    return (E)mValues[paramInt];
  }
}

/* Location:
 * Qualified Name:     android.support.v4.util.LongSparseArray
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */