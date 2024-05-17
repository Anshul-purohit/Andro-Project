package de.ecspride;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.PrintStream;

public class ImplicitFlow1
  extends Activity
{
  private String copyIMEI(String paramString)
  {
    paramString = paramString.toCharArray();
    char[] arrayOfChar = new char[paramString.length];
    for (int i = 0;; i++)
    {
      if (i >= paramString.length) {
        return new String(arrayOfChar);
      }
      int j = paramString[i];
      arrayOfChar[i] = ((char)(char)new Integer[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30), Integer.valueOf(31), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), Integer.valueOf(40), Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43), Integer.valueOf(44), Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(49), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53), Integer.valueOf(54), Integer.valueOf(55), Integer.valueOf(56), Integer.valueOf(57) }[j].intValue());
    }
  }
  
  private String obfuscateIMEI(String paramString)
  {
    String str = "";
    char[] arrayOfChar = paramString.toCharArray();
    int i = arrayOfChar.length;
    int j = 0;
    paramString = str;
    if (j >= i) {
      return paramString;
    }
    char c = arrayOfChar[j];
    switch (c)
    {
    default: 
      System.err.println("Problem in obfuscateIMEI for character: " + c);
    }
    for (;;)
    {
      j++;
      break;
      paramString = paramString + 'a';
      continue;
      paramString = paramString + 'b';
      continue;
      paramString = paramString + 'c';
      continue;
      paramString = paramString + 'd';
      continue;
      paramString = paramString + 'e';
      continue;
      paramString = paramString + 'f';
      continue;
      paramString = paramString + 'g';
      continue;
      paramString = paramString + 'h';
      continue;
      paramString = paramString + 'i';
      continue;
      paramString = paramString + 'j';
    }
  }
  
  private void writeToLog(String paramString)
  {
    Log.i("INFO", paramString);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903040);
    paramBundle = ((TelephonyManager)getSystemService("phone")).getDeviceId();
    writeToLog(obfuscateIMEI(paramBundle));
    writeToLog(copyIMEI(paramBundle));
  }
}

/* Location:
 * Qualified Name:     de.ecspride.ImplicitFlow1
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */