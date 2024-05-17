package android.support.v4.util;

public class SparseArrayCompat<E>
{
  private static final Object DELETED = new Object();
  private boolean mGarbage = false;
  private int[] mKeys;
  private int mSize;
  private Object[] mValues;
  
  public SparseArrayCompat()
  {
    this(10);
  }
  
  public SparseArrayCompat(int paramInt)
  {
    paramInt = idealIntArraySize(paramInt);
    mKeys = new int[paramInt];
    mValues = new Object[paramInt];
    mSize = 0;
  }
  
  private static int binarySearch(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt1 + paramInt2;
    int j = paramInt1 - 1;
    while (i - j > 1)
    {
      int k = (i + j) / 2;
      if (paramArrayOfInt[k] < paramInt3) {
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
      if (paramArrayOfInt[i] != paramInt3) {
        paramInt1 = i ^ 0xFFFFFFFF;
      }
    }
  }
  
  private void gc()
  {
    int i = mSize;
    int j = 0;
    int[] arrayOfInt = mKeys;
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
          arrayOfInt[j] = arrayOfInt[k];
          arrayOfObject[j] = localObject;
        }
        m = j + 1;
      }
      k++;
      j = m;
    }
    mGarbage = false;
    mSize = j;
  }
  
  static int idealByteArraySize(int paramInt)
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
  
  static int idealIntArraySize(int paramInt)
  {
    return idealByteArraySize(paramInt * 4) / 4;
  }
  
  public void append(int paramInt, E paramE)
  {
    if ((mSize != 0) && (paramInt <= mKeys[(mSize - 1)])) {
      put(paramInt, paramE);
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
        int j = idealIntArraySize(i + 1);
        int[] arrayOfInt = new int[j];
        Object[] arrayOfObject = new Object[j];
        System.arraycopy(mKeys, 0, arrayOfInt, 0, mKeys.length);
        System.arraycopy(mValues, 0, arrayOfObject, 0, mValues.length);
        mKeys = arrayOfInt;
        mValues = arrayOfObject;
      }
      mKeys[i] = paramInt;
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
  
  public void delete(int paramInt)
  {
    paramInt = binarySearch(mKeys, 0, mSize, paramInt);
    if ((paramInt >= 0) && (mValues[paramInt] != DELETED))
    {
      mValues[paramInt] = DELETED;
      mGarbage = true;
    }
  }
  
  public E get(int paramInt)
  {
    return (E)get(paramInt, null);
  }
  
  public E get(int paramInt, E paramE)
  {
    paramInt = binarySearch(mKeys, 0, mSize, paramInt);
    Object localObject = paramE;
    if (paramInt >= 0) {
      if (mValues[paramInt] != DELETED) {
        break label36;
      }
    }
    label36:
    for (localObject = paramE;; localObject = mValues[paramInt]) {
      return (E)localObject;
    }
  }
  
  public int indexOfKey(int paramInt)
  {
    if (mGarbage) {
      gc();
    }
    return binarySearch(mKeys, 0, mSize, paramInt);
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
  
  public int keyAt(int paramInt)
  {
    if (mGarbage) {
      gc();
    }
    return mKeys[paramInt];
  }
  
  public void put(int paramInt, E paramE)
  {
    int i = binarySearch(mKeys, 0, mSize, paramInt);
    if (i >= 0) {
      mValues[i] = paramE;
    }
    for (;;)
    {
      return;
      int j = i ^ 0xFFFFFFFF;
      if ((j < mSize) && (mValues[j] == DELETED))
      {
        mKeys[j] = paramInt;
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
            i = binarySearch(mKeys, 0, mSize, paramInt) ^ 0xFFFFFFFF;
          }
        }
        if (mSize >= mKeys.length)
        {
          j = idealIntArraySize(mSize + 1);
          int[] arrayOfInt = new int[j];
          Object[] arrayOfObject = new Object[j];
          System.arraycopy(mKeys, 0, arrayOfInt, 0, mKeys.length);
          System.arraycopy(mValues, 0, arrayOfObject, 0, mValues.length);
          mKeys = arrayOfInt;
          mValues = arrayOfObject;
        }
        if (mSize - i != 0)
        {
          System.arraycopy(mKeys, i, mKeys, i + 1, mSize - i);
          System.arraycopy(mValues, i, mValues, i + 1, mSize - i);
        }
        mKeys[i] = paramInt;
        mValues[i] = paramE;
        mSize += 1;
      }
    }
  }
  
  public void remove(int paramInt)
  {
    delete(paramInt);
  }
  
  public void removeAt(int paramInt)
  {
    if (mValues[paramInt] != DELETED)
    {
      mValues[paramInt] = DELETED;
      mGarbage = true;
    }
  }
  
  public void removeAtRange(int paramInt1, int paramInt2)
  {
    paramInt2 = Math.min(mSize, paramInt1 + paramInt2);
    while (paramInt1 < paramInt2)
    {
      removeAt(paramInt1);
      paramInt1++;
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
 * Qualified Name:     android.support.v4.util.SparseArrayCompat
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */