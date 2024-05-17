package android.support.v4.util;

import java.io.PrintWriter;

public class TimeUtils
{
  public static final int HUNDRED_DAY_FIELD_LEN = 19;
  private static final int SECONDS_PER_DAY = 86400;
  private static final int SECONDS_PER_HOUR = 3600;
  private static final int SECONDS_PER_MINUTE = 60;
  private static char[] sFormatStr = new char[24];
  private static final Object sFormatSync = new Object();
  
  private static int accumField(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    if ((paramInt1 > 99) || ((paramBoolean) && (paramInt3 >= 3))) {
      paramInt1 = paramInt2 + 3;
    }
    for (;;)
    {
      return paramInt1;
      if ((paramInt1 > 9) || ((paramBoolean) && (paramInt3 >= 2))) {
        paramInt1 = paramInt2 + 2;
      } else if ((paramBoolean) || (paramInt1 > 0)) {
        paramInt1 = paramInt2 + 1;
      } else {
        paramInt1 = 0;
      }
    }
  }
  
  public static void formatDuration(long paramLong1, long paramLong2, PrintWriter paramPrintWriter)
  {
    if (paramLong1 == 0L) {
      paramPrintWriter.print("--");
    }
    for (;;)
    {
      return;
      formatDuration(paramLong1 - paramLong2, paramPrintWriter, 0);
    }
  }
  
  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter)
  {
    formatDuration(paramLong, paramPrintWriter, 0);
  }
  
  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter, int paramInt)
  {
    synchronized (sFormatSync)
    {
      paramInt = formatDurationLocked(paramLong, paramInt);
      String str = new java/lang/String;
      str.<init>(sFormatStr, 0, paramInt);
      paramPrintWriter.print(str);
      return;
    }
  }
  
  public static void formatDuration(long paramLong, StringBuilder paramStringBuilder)
  {
    synchronized (sFormatSync)
    {
      int i = formatDurationLocked(paramLong, 0);
      paramStringBuilder.append(sFormatStr, 0, i);
      return;
    }
  }
  
  private static int formatDurationLocked(long paramLong, int paramInt)
  {
    if (sFormatStr.length < paramInt) {
      sFormatStr = new char[paramInt];
    }
    char[] arrayOfChar = sFormatStr;
    if (paramLong == 0L)
    {
      while (paramInt - 1 < 0) {
        arrayOfChar[0] = ((char)32);
      }
      arrayOfChar[0] = ((char)48);
      paramInt = 1;
      return paramInt;
    }
    int j;
    int m;
    int n;
    int i1;
    int i2;
    int i3;
    int i4;
    boolean bool;
    label203:
    label225:
    label247:
    int i5;
    if (paramLong > 0L)
    {
      i = 43;
      j = (int)(paramLong % 1000L);
      k = (int)Math.floor(paramLong / 1000L);
      m = 0;
      n = 0;
      i1 = 0;
      i2 = k;
      if (k > 86400)
      {
        m = k / 86400;
        i2 = k - 86400 * m;
      }
      k = i2;
      if (i2 > 3600)
      {
        n = i2 / 3600;
        k = i2 - n * 3600;
      }
      i2 = k;
      if (k > 60)
      {
        i1 = k / 60;
        i2 = k - i1 * 60;
      }
      i3 = 0;
      i4 = 0;
      if (paramInt == 0) {
        break label349;
      }
      k = accumField(m, 1, false, 0);
      if (k <= 0) {
        break label325;
      }
      bool = true;
      k += accumField(n, 1, bool, 2);
      if (k <= 0) {
        break label331;
      }
      bool = true;
      k += accumField(i1, 1, bool, 2);
      if (k <= 0) {
        break label337;
      }
      bool = true;
      i5 = k + accumField(i2, 1, bool, 2);
      if (i5 <= 0) {
        break label343;
      }
    }
    label325:
    label331:
    label337:
    label343:
    for (int k = 3;; k = 0)
    {
      i5 += accumField(j, 2, true, k) + 1;
      k = i4;
      for (;;)
      {
        i3 = k;
        if (i5 >= paramInt) {
          break;
        }
        arrayOfChar[k] = ((char)32);
        k++;
        i5++;
      }
      i = 45;
      paramLong = -paramLong;
      break;
      bool = false;
      break label203;
      bool = false;
      break label225;
      bool = false;
      break label247;
    }
    label349:
    arrayOfChar[i3] = ((char)i);
    int i = i3 + 1;
    if (paramInt != 0)
    {
      paramInt = 1;
      label368:
      m = printField(arrayOfChar, m, 'd', i, false, 0);
      if (m == i) {
        break label524;
      }
      bool = true;
      label392:
      if (paramInt == 0) {
        break label530;
      }
      k = 2;
      label399:
      n = printField(arrayOfChar, n, 'h', m, bool, k);
      if (n == i) {
        break label536;
      }
      bool = true;
      label425:
      if (paramInt == 0) {
        break label542;
      }
      k = 2;
      label432:
      n = printField(arrayOfChar, i1, 'm', n, bool, k);
      if (n == i) {
        break label548;
      }
      bool = true;
      label458:
      if (paramInt == 0) {
        break label554;
      }
      k = 2;
      label465:
      k = printField(arrayOfChar, i2, 's', n, bool, k);
      if ((paramInt == 0) || (k == i)) {
        break label560;
      }
    }
    label524:
    label530:
    label536:
    label542:
    label548:
    label554:
    label560:
    for (paramInt = 3;; paramInt = 0)
    {
      paramInt = printField(arrayOfChar, j, 'm', k, true, paramInt);
      arrayOfChar[paramInt] = ((char)115);
      paramInt++;
      break;
      paramInt = 0;
      break label368;
      bool = false;
      break label392;
      k = 0;
      break label399;
      bool = false;
      break label425;
      k = 0;
      break label432;
      bool = false;
      break label458;
      k = 0;
      break label465;
    }
  }
  
  private static int printField(char[] paramArrayOfChar, int paramInt1, char paramChar, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    int i;
    if (!paramBoolean)
    {
      i = paramInt2;
      if (paramInt1 <= 0) {}
    }
    else
    {
      int j;
      if ((!paramBoolean) || (paramInt3 < 3))
      {
        i = paramInt1;
        j = paramInt2;
        if (paramInt1 <= 99) {}
      }
      else
      {
        i = paramInt1 / 100;
        paramArrayOfChar[paramInt2] = ((char)(char)(i + 48));
        j = paramInt2 + 1;
        i = paramInt1 - i * 100;
      }
      if (((!paramBoolean) || (paramInt3 < 2)) && (i <= 9))
      {
        paramInt3 = i;
        paramInt1 = j;
        if (paramInt2 == j) {}
      }
      else
      {
        paramInt2 = i / 10;
        paramArrayOfChar[j] = ((char)(char)(paramInt2 + 48));
        paramInt1 = j + 1;
        paramInt3 = i - paramInt2 * 10;
      }
      paramArrayOfChar[paramInt1] = ((char)(char)(paramInt3 + 48));
      paramInt1++;
      paramArrayOfChar[paramInt1] = ((char)paramChar);
      i = paramInt1 + 1;
    }
    return i;
  }
}

/* Location:
 * Qualified Name:     android.support.v4.util.TimeUtils
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */