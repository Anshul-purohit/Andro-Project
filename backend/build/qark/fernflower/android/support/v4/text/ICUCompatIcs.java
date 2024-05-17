package android.support.v4.text;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ICUCompatIcs {
   private static final String TAG = "ICUCompatIcs";
   private static Method sAddLikelySubtagsMethod;
   private static Method sGetScriptMethod;

   static {
      // $FF: Couldn't be decompiled
   }

   public static String addLikelySubtags(String var0) {
      try {
         if (sAddLikelySubtagsMethod != null) {
            String var1 = (String)sAddLikelySubtagsMethod.invoke((Object)null, var0);
            return var1;
         }
      } catch (IllegalAccessException var2) {
         Log.w("ICUCompatIcs", var2);
      } catch (InvocationTargetException var3) {
         Log.w("ICUCompatIcs", var3);
      }

      return var0;
   }

   public static String getScript(String var0) {
      try {
         if (sGetScriptMethod != null) {
            var0 = (String)sGetScriptMethod.invoke((Object)null, var0);
            return var0;
         }
      } catch (IllegalAccessException var1) {
         Log.w("ICUCompatIcs", var1);
      } catch (InvocationTargetException var2) {
         Log.w("ICUCompatIcs", var2);
      }

      return null;
   }
}
