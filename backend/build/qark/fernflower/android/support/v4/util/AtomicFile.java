package android.support.v4.util;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile {
   private final File mBackupName;
   private final File mBaseName;

   public AtomicFile(File var1) {
      this.mBaseName = var1;
      this.mBackupName = new File(var1.getPath() + ".bak");
   }

   static boolean sync(FileOutputStream var0) {
      if (var0 != null) {
         try {
            var0.getFD().sync();
         } catch (IOException var1) {
            return false;
         }
      }

      return true;
   }

   public void delete() {
      this.mBaseName.delete();
      this.mBackupName.delete();
   }

   public void failWrite(FileOutputStream var1) {
      if (var1 != null) {
         sync(var1);

         try {
            var1.close();
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
         } catch (IOException var2) {
            Log.w("AtomicFile", "failWrite: Got exception:", var2);
            return;
         }
      }

   }

   public void finishWrite(FileOutputStream var1) {
      if (var1 != null) {
         sync(var1);

         try {
            var1.close();
            this.mBackupName.delete();
         } catch (IOException var2) {
            Log.w("AtomicFile", "finishWrite: Got exception:", var2);
            return;
         }
      }

   }

   public File getBaseFile() {
      return this.mBaseName;
   }

   public FileInputStream openRead() throws FileNotFoundException {
      if (this.mBackupName.exists()) {
         this.mBaseName.delete();
         this.mBackupName.renameTo(this.mBaseName);
      }

      return new FileInputStream(this.mBaseName);
   }

   public byte[] readFully() throws IOException {
      FileInputStream var6 = this.openRead();
      int var1 = 0;

      Throwable var10000;
      label210: {
         byte[] var4;
         boolean var10001;
         try {
            var4 = new byte[var6.available()];
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            break label210;
         }

         while(true) {
            int var2;
            try {
               var2 = var6.read(var4, var1, var4.length - var1);
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break;
            }

            if (var2 <= 0) {
               var6.close();
               return var4;
            }

            var2 += var1;

            int var3;
            try {
               var3 = var6.available();
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break;
            }

            var1 = var2;

            byte[] var5;
            try {
               if (var3 <= var4.length - var2) {
                  continue;
               }

               var5 = new byte[var2 + var3];
               System.arraycopy(var4, 0, var5, 0, var2);
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break;
            }

            var4 = var5;
            var1 = var2;
         }
      }

      Throwable var27 = var10000;
      var6.close();
      throw var27;
   }

   public FileOutputStream startWrite() throws IOException {
      if (this.mBaseName.exists()) {
         if (!this.mBackupName.exists()) {
            if (!this.mBaseName.renameTo(this.mBackupName)) {
               Log.w("AtomicFile", "Couldn't rename file " + this.mBaseName + " to backup file " + this.mBackupName);
            }
         } else {
            this.mBaseName.delete();
         }
      }

      FileOutputStream var1;
      try {
         var1 = new FileOutputStream(this.mBaseName);
         return var1;
      } catch (FileNotFoundException var3) {
         if (!this.mBaseName.getParentFile().mkdir()) {
            throw new IOException("Couldn't create directory " + this.mBaseName);
         } else {
            try {
               var1 = new FileOutputStream(this.mBaseName);
               return var1;
            } catch (FileNotFoundException var2) {
               throw new IOException("Couldn't create " + this.mBaseName);
            }
         }
      }
   }
}
