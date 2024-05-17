package android.support.v4.util;

import java.io.PrintWriter;

public class TimeUtils {
   public static final int HUNDRED_DAY_FIELD_LEN = 19;
   private static final int SECONDS_PER_DAY = 86400;
   private static final int SECONDS_PER_HOUR = 3600;
   private static final int SECONDS_PER_MINUTE = 60;
   private static char[] sFormatStr = new char[24];
   private static final Object sFormatSync = new Object();

   private static int accumField(int var0, int var1, boolean var2, int var3) {
      if (var0 <= 99 && (!var2 || var3 < 3)) {
         if (var0 > 9 || var2 && var3 >= 2) {
            return var1 + 2;
         } else {
            return !var2 && var0 <= 0 ? 0 : var1 + 1;
         }
      } else {
         return var1 + 3;
      }
   }

   public static void formatDuration(long var0, long var2, PrintWriter var4) {
      if (var0 == 0L) {
         var4.print("--");
      } else {
         formatDuration(var0 - var2, var4, 0);
      }
   }

   public static void formatDuration(long var0, PrintWriter var2) {
      formatDuration(var0, var2, 0);
   }

   public static void formatDuration(long param0, PrintWriter param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public static void formatDuration(long param0, StringBuilder param2) {
      // $FF: Couldn't be decompiled
   }

   private static int formatDurationLocked(long var0, int var2) {
      if (sFormatStr.length < var2) {
         sFormatStr = new char[var2];
      }

      char[] var14 = sFormatStr;
      if (var0 == 0L) {
         while(var2 - 1 < 0) {
            var14[0] = ' ';
         }

         var14[0] = '0';
         return 1;
      } else {
         byte var3;
         if (var0 > 0L) {
            var3 = 43;
         } else {
            var3 = 45;
            var0 = -var0;
         }

         int var12 = (int)(var0 % 1000L);
         int var5 = (int)Math.floor((double)(var0 / 1000L));
         int var7 = 0;
         int var8 = 0;
         int var9 = 0;
         int var4 = var5;
         if (var5 > 86400) {
            var7 = var5 / 86400;
            var4 = var5 - 86400 * var7;
         }

         var5 = var4;
         if (var4 > 3600) {
            var8 = var4 / 3600;
            var5 = var4 - var8 * 3600;
         }

         int var6 = var5;
         if (var5 > 60) {
            var9 = var5 / 60;
            var6 = var5 - var9 * 60;
         }

         int var10 = 0;
         byte var11 = 0;
         boolean var13;
         byte var17;
         if (var2 != 0) {
            var4 = accumField(var7, 1, false, 0);
            if (var4 > 0) {
               var13 = true;
            } else {
               var13 = false;
            }

            var4 += accumField(var8, 1, var13, 2);
            if (var4 > 0) {
               var13 = true;
            } else {
               var13 = false;
            }

            var4 += accumField(var9, 1, var13, 2);
            if (var4 > 0) {
               var13 = true;
            } else {
               var13 = false;
            }

            var5 = var4 + accumField(var6, 1, var13, 2);
            if (var5 > 0) {
               var17 = 3;
            } else {
               var17 = 0;
            }

            var5 += accumField(var12, 2, true, var17) + 1;
            var4 = var11;

            while(true) {
               var10 = var4;
               if (var5 >= var2) {
                  break;
               }

               var14[var4] = ' ';
               ++var4;
               ++var5;
            }
         }

         var14[var10] = (char)var3;
         var5 = var10 + 1;
         boolean var15;
         if (var2 != 0) {
            var15 = true;
         } else {
            var15 = false;
         }

         var7 = printField(var14, var7, 'd', var5, false, 0);
         if (var7 != var5) {
            var13 = true;
         } else {
            var13 = false;
         }

         if (var15) {
            var17 = 2;
         } else {
            var17 = 0;
         }

         var7 = printField(var14, var8, 'h', var7, var13, var17);
         if (var7 != var5) {
            var13 = true;
         } else {
            var13 = false;
         }

         if (var15) {
            var17 = 2;
         } else {
            var17 = 0;
         }

         var7 = printField(var14, var9, 'm', var7, var13, var17);
         if (var7 != var5) {
            var13 = true;
         } else {
            var13 = false;
         }

         if (var15) {
            var17 = 2;
         } else {
            var17 = 0;
         }

         var4 = printField(var14, var6, 's', var7, var13, var17);
         byte var16;
         if (var15 && var4 != var5) {
            var16 = 3;
         } else {
            var16 = 0;
         }

         var2 = printField(var14, var12, 'm', var4, true, var16);
         var14[var2] = 's';
         return var2 + 1;
      }
   }

   private static int printField(char[] var0, int var1, char var2, int var3, boolean var4, int var5) {
      int var6;
      if (!var4) {
         var6 = var3;
         if (var1 <= 0) {
            return var6;
         }
      }

      int var7;
      label40: {
         if (!var4 || var5 < 3) {
            var6 = var1;
            var7 = var3;
            if (var1 <= 99) {
               break label40;
            }
         }

         var6 = var1 / 100;
         var0[var3] = (char)(var6 + 48);
         var7 = var3 + 1;
         var6 = var1 - var6 * 100;
      }

      label41: {
         if ((!var4 || var5 < 2) && var6 <= 9) {
            var5 = var6;
            var1 = var7;
            if (var3 == var7) {
               break label41;
            }
         }

         var3 = var6 / 10;
         var0[var7] = (char)(var3 + 48);
         var1 = var7 + 1;
         var5 = var6 - var3 * 10;
      }

      var0[var1] = (char)(var5 + 48);
      ++var1;
      var0[var1] = var2;
      var6 = var1 + 1;
      return var6;
   }
}
