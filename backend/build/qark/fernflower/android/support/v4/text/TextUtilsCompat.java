package android.support.v4.text;

import java.util.Locale;

public class TextUtilsCompat {
   private static String ARAB_SCRIPT_SUBTAG = "Arab";
   private static String HEBR_SCRIPT_SUBTAG = "Hebr";
   public static final Locale ROOT = new Locale("", "");

   private static int getLayoutDirectionFromFirstChar(Locale var0) {
      switch(Character.getDirectionality(var0.getDisplayName(var0).charAt(0))) {
      case 1:
      case 2:
         return 1;
      default:
         return 0;
      }
   }

   public static int getLayoutDirectionFromLocale(Locale var0) {
      if (var0 != null && !var0.equals(ROOT)) {
         String var1 = ICUCompat.getScript(ICUCompat.addLikelySubtags(var0.toString()));
         if (var1 == null) {
            return getLayoutDirectionFromFirstChar(var0);
         }

         if (var1.equalsIgnoreCase(ARAB_SCRIPT_SUBTAG) || var1.equalsIgnoreCase(HEBR_SCRIPT_SUBTAG)) {
            return 1;
         }
      }

      return 0;
   }

   public static String htmlEncode(String var0) {
      StringBuilder var3 = new StringBuilder();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var1 = var0.charAt(var2);
         switch(var1) {
         case '"':
            var3.append("&quot;");
            break;
         case '&':
            var3.append("&amp;");
            break;
         case '\'':
            var3.append("&#39;");
            break;
         case '<':
            var3.append("&lt;");
            break;
         case '>':
            var3.append("&gt;");
            break;
         default:
            var3.append(var1);
         }
      }

      return var3.toString();
   }
}
