package android.support.v4.util;

public class DebugUtils
{
  public static void buildShortClassTag(Object paramObject, StringBuilder paramStringBuilder)
  {
    if (paramObject == null) {
      paramStringBuilder.append("null");
    }
    for (;;)
    {
      return;
      String str1 = paramObject.getClass().getSimpleName();
      String str2;
      if (str1 != null)
      {
        str2 = str1;
        if (str1.length() > 0) {}
      }
      else
      {
        str1 = paramObject.getClass().getName();
        int i = str1.lastIndexOf('.');
        str2 = str1;
        if (i > 0) {
          str2 = str1.substring(i + 1);
        }
      }
      paramStringBuilder.append(str2);
      paramStringBuilder.append('{');
      paramStringBuilder.append(Integer.toHexString(System.identityHashCode(paramObject)));
    }
  }
}

/* Location:
 * Qualified Name:     android.support.v4.util.DebugUtils
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */