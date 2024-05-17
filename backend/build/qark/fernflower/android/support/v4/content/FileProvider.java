package android.support.v4.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider {
   private static final String ATTR_NAME = "name";
   private static final String ATTR_PATH = "path";
   private static final String[] COLUMNS = new String[]{"_display_name", "_size"};
   private static final File DEVICE_ROOT = new File("/");
   private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
   private static final String TAG_CACHE_PATH = "cache-path";
   private static final String TAG_EXTERNAL = "external-path";
   private static final String TAG_FILES_PATH = "files-path";
   private static final String TAG_ROOT_PATH = "root-path";
   private static HashMap sCache = new HashMap();
   private FileProvider.PathStrategy mStrategy;

   private static File buildPath(File var0, String... var1) {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         String var4 = var1[var2];
         if (var4 != null) {
            var0 = new File(var0, var4);
         }
      }

      return var0;
   }

   private static Object[] copyOf(Object[] var0, int var1) {
      Object[] var2 = new Object[var1];
      System.arraycopy(var0, 0, var2, 0, var1);
      return var2;
   }

   private static String[] copyOf(String[] var0, int var1) {
      String[] var2 = new String[var1];
      System.arraycopy(var0, 0, var2, 0, var1);
      return var2;
   }

   private static FileProvider.PathStrategy getPathStrategy(Context param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static Uri getUriForFile(Context var0, String var1, File var2) {
      return getPathStrategy(var0, var1).getUriForFile(var2);
   }

   private static int modeToMode(String var0) {
      if ("r".equals(var0)) {
         return 268435456;
      } else if (!"w".equals(var0) && !"wt".equals(var0)) {
         if ("wa".equals(var0)) {
            return 704643072;
         } else if ("rw".equals(var0)) {
            return 939524096;
         } else if ("rwt".equals(var0)) {
            return 1006632960;
         } else {
            throw new IllegalArgumentException("Invalid mode: " + var0);
         }
      } else {
         return 738197504;
      }
   }

   private static FileProvider.PathStrategy parsePathStrategy(Context var0, String var1) throws IOException, XmlPullParserException {
      FileProvider.SimplePathStrategy var3 = new FileProvider.SimplePathStrategy(var1);
      XmlResourceParser var4 = var0.getPackageManager().resolveContentProvider(var1, 128).loadXmlMetaData(var0.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
      if (var4 == null) {
         throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
      } else {
         while(true) {
            int var2 = var4.next();
            if (var2 == 1) {
               return var3;
            }

            if (var2 == 2) {
               String var6 = var4.getName();
               String var5 = var4.getAttributeValue((String)null, "name");
               String var7 = var4.getAttributeValue((String)null, "path");
               File var8 = null;
               if ("root-path".equals(var6)) {
                  var8 = buildPath(DEVICE_ROOT, var7);
               } else if ("files-path".equals(var6)) {
                  var8 = buildPath(var0.getFilesDir(), var7);
               } else if ("cache-path".equals(var6)) {
                  var8 = buildPath(var0.getCacheDir(), var7);
               } else if ("external-path".equals(var6)) {
                  var8 = buildPath(Environment.getExternalStorageDirectory(), var7);
               }

               if (var8 != null) {
                  var3.addRoot(var5, var8);
               }
            }
         }
      }
   }

   public void attachInfo(Context var1, ProviderInfo var2) {
      super.attachInfo(var1, var2);
      if (var2.exported) {
         throw new SecurityException("Provider must not be exported");
      } else if (!var2.grantUriPermissions) {
         throw new SecurityException("Provider must grant uri permissions");
      } else {
         this.mStrategy = getPathStrategy(var1, var2.authority);
      }
   }

   public int delete(Uri var1, String var2, String[] var3) {
      return this.mStrategy.getFileForUri(var1).delete() ? 1 : 0;
   }

   public String getType(Uri var1) {
      File var3 = this.mStrategy.getFileForUri(var1);
      int var2 = var3.getName().lastIndexOf(46);
      if (var2 >= 0) {
         String var4 = var3.getName().substring(var2 + 1);
         var4 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var4);
         if (var4 != null) {
            return var4;
         }
      }

      return "application/octet-stream";
   }

   public Uri insert(Uri var1, ContentValues var2) {
      throw new UnsupportedOperationException("No external inserts");
   }

   public boolean onCreate() {
      return true;
   }

   public ParcelFileDescriptor openFile(Uri var1, String var2) throws FileNotFoundException {
      return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(var1), modeToMode(var2));
   }

   public Cursor query(Uri var1, String[] var2, String var3, String[] var4, String var5) {
      File var13 = this.mStrategy.getFileForUri(var1);
      String[] var10 = var2;
      if (var2 == null) {
         var10 = COLUMNS;
      }

      var4 = new String[var10.length];
      Object[] var11 = new Object[var10.length];
      int var9 = var10.length;
      int var7 = 0;

      int var6;
      for(var6 = 0; var7 < var9; ++var7) {
         var5 = var10[var7];
         int var8;
         if ("_display_name".equals(var5)) {
            var4[var6] = "_display_name";
            var8 = var6 + 1;
            var11[var6] = var13.getName();
            var6 = var8;
         } else if ("_size".equals(var5)) {
            var4[var6] = "_size";
            var8 = var6 + 1;
            var11[var6] = var13.length();
            var6 = var8;
         }
      }

      var10 = copyOf(var4, var6);
      var11 = copyOf(var11, var6);
      MatrixCursor var12 = new MatrixCursor(var10, 1);
      var12.addRow(var11);
      return var12;
   }

   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      throw new UnsupportedOperationException("No external updates");
   }

   interface PathStrategy {
      File getFileForUri(Uri var1);

      Uri getUriForFile(File var1);
   }

   static class SimplePathStrategy implements FileProvider.PathStrategy {
      private final String mAuthority;
      private final HashMap mRoots = new HashMap();

      public SimplePathStrategy(String var1) {
         this.mAuthority = var1;
      }

      public void addRoot(String var1, File var2) {
         if (TextUtils.isEmpty(var1)) {
            throw new IllegalArgumentException("Name must not be empty");
         } else {
            File var3;
            try {
               var3 = var2.getCanonicalFile();
            } catch (IOException var4) {
               throw new IllegalArgumentException("Failed to resolve canonical path for " + var2, var4);
            }

            this.mRoots.put(var1, var3);
         }
      }

      public File getFileForUri(Uri var1) {
         String var4 = var1.getEncodedPath();
         int var2 = var4.indexOf(47, 1);
         String var3 = Uri.decode(var4.substring(1, var2));
         var4 = Uri.decode(var4.substring(var2 + 1));
         File var7 = (File)this.mRoots.get(var3);
         if (var7 == null) {
            throw new IllegalArgumentException("Unable to find configured root for " + var1);
         } else {
            File var6 = new File(var7, var4);

            File var8;
            try {
               var8 = var6.getCanonicalFile();
            } catch (IOException var5) {
               throw new IllegalArgumentException("Failed to resolve canonical path for " + var6);
            }

            if (!var8.getPath().startsWith(var7.getPath())) {
               throw new SecurityException("Resolved path jumped beyond configured root");
            } else {
               return var8;
            }
         }
      }

      public Uri getUriForFile(File var1) {
         String var3;
         try {
            var3 = var1.getCanonicalPath();
         } catch (IOException var6) {
            throw new IllegalArgumentException("Failed to resolve canonical path for " + var1);
         }

         Entry var7 = null;
         Iterator var4 = this.mRoots.entrySet().iterator();

         while(true) {
            Entry var2;
            String var5;
            do {
               do {
                  if (!var4.hasNext()) {
                     if (var7 == null) {
                        throw new IllegalArgumentException("Failed to find configured root that contains " + var3);
                     }

                     String var9 = ((File)var7.getValue()).getPath();
                     if (var9.endsWith("/")) {
                        var9 = var3.substring(var9.length());
                     } else {
                        var9 = var3.substring(var9.length() + 1);
                     }

                     String var8 = Uri.encode((String)var7.getKey()) + '/' + Uri.encode(var9, "/");
                     return (new Builder()).scheme("content").authority(this.mAuthority).encodedPath(var8).build();
                  }

                  var2 = (Entry)var4.next();
                  var5 = ((File)var2.getValue()).getPath();
               } while(!var3.startsWith(var5));
            } while(var7 != null && var5.length() <= ((File)var7.getValue()).getPath().length());

            var7 = var2;
         }
      }
   }
}
