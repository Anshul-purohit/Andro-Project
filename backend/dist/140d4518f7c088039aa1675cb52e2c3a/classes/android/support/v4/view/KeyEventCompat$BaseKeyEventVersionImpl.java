package android.support.v4.view;

class KeyEventCompat$BaseKeyEventVersionImpl
  implements KeyEventCompat.KeyEventVersionImpl
{
  private static final int META_ALL_MASK = 247;
  private static final int META_MODIFIER_MASK = 247;
  
  private static int metaStateFilterDirectionalModifiers(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int i = 1;
    int j;
    if ((paramInt2 & paramInt3) != 0)
    {
      j = 1;
      paramInt5 = paramInt4 | paramInt5;
      if ((paramInt2 & paramInt5) == 0) {
        break label53;
      }
      paramInt4 = i;
    }
    for (;;)
    {
      if (j != 0)
      {
        if (paramInt4 != 0)
        {
          throw new IllegalArgumentException("bad arguments");
          j = 0;
          break;
          label53:
          paramInt4 = 0;
          continue;
        }
        paramInt2 = paramInt1 & (paramInt5 ^ 0xFFFFFFFF);
      }
    }
    for (;;)
    {
      return paramInt2;
      paramInt2 = paramInt1;
      if (paramInt4 != 0) {
        paramInt2 = paramInt1 & (paramInt3 ^ 0xFFFFFFFF);
      }
    }
  }
  
  public boolean metaStateHasModifiers(int paramInt1, int paramInt2)
  {
    boolean bool = true;
    if (metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(normalizeMetaState(paramInt1) & 0xF7, paramInt2, 1, 64, 128), paramInt2, 2, 16, 32) == paramInt2) {}
    for (;;)
    {
      return bool;
      bool = false;
    }
  }
  
  public boolean metaStateHasNoModifiers(int paramInt)
  {
    if ((normalizeMetaState(paramInt) & 0xF7) == 0) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public int normalizeMetaState(int paramInt)
  {
    int i = paramInt;
    if ((paramInt & 0xC0) != 0) {
      i = paramInt | 0x1;
    }
    paramInt = i;
    if ((i & 0x30) != 0) {
      paramInt = i | 0x2;
    }
    return paramInt & 0xF7;
  }
}

/* Location:
 * Qualified Name:     android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */