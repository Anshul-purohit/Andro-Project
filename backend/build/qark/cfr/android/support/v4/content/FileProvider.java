/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ContentProvider
 *  android.content.ContentValues
 *  android.content.Context
 *  android.content.pm.PackageManager
 *  android.content.pm.ProviderInfo
 *  android.content.res.XmlResourceParser
 *  android.database.Cursor
 *  android.database.MatrixCursor
 *  android.net.Uri
 *  android.net.Uri$Builder
 *  android.os.Environment
 *  android.os.ParcelFileDescriptor
 *  android.text.TextUtils
 *  android.webkit.MimeTypeMap
 *  java.io.File
 *  java.io.FileNotFoundException
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.IllegalArgumentException
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.SecurityException
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.lang.UnsupportedOperationException
 *  java.util.HashMap
 *  java.util.Set
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.v4.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider
extends ContentProvider {
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String[] COLUMNS = new String[]{"_display_name", "_size"};
    private static final File DEVICE_ROOT = new File("/");
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    private static HashMap<String, PathStrategy> sCache = new HashMap();
    private PathStrategy mStrategy;

    private static /* varargs */ File buildPath(File file, String ... arrstring) {
        for (String string2 : arrstring) {
            if (string2 == null) continue;
            file = new File(file, string2);
        }
        return file;
    }

    private static Object[] copyOf(Object[] arrobject, int n) {
        Object[] arrobject2 = new Object[n];
        System.arraycopy((Object)arrobject, (int)0, (Object)arrobject2, (int)0, (int)n);
        return arrobject2;
    }

    private static String[] copyOf(String[] arrstring, int n) {
        String[] arrstring2 = new String[n];
        System.arraycopy((Object)arrstring, (int)0, (Object)arrstring2, (int)0, (int)n);
        return arrstring2;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private static PathStrategy getPathStrategy(Context context, String string2) {
        PathStrategy pathStrategy;
        HashMap<String, PathStrategy> hashMap = sCache;
        // MONITORENTER : hashMap
        PathStrategy pathStrategy2 = pathStrategy = (PathStrategy)sCache.get((Object)string2);
        if (pathStrategy == null) {
            pathStrategy2 = FileProvider.parsePathStrategy(context, string2);
            sCache.put((Object)string2, (Object)pathStrategy2);
        }
        // MONITOREXIT : hashMap
        return pathStrategy2;
        catch (IOException iOException) {
            throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", (Throwable)iOException);
        }
        catch (XmlPullParserException xmlPullParserException) {
            throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", (Throwable)xmlPullParserException);
        }
    }

    public static Uri getUriForFile(Context context, String string2, File file) {
        return FileProvider.getPathStrategy(context, string2).getUriForFile(file);
    }

    private static int modeToMode(String string2) {
        if ("r".equals((Object)string2)) {
            return 268435456;
        }
        if ("w".equals((Object)string2) || "wt".equals((Object)string2)) {
            return 738197504;
        }
        if ("wa".equals((Object)string2)) {
            return 704643072;
        }
        if ("rw".equals((Object)string2)) {
            return 939524096;
        }
        if ("rwt".equals((Object)string2)) {
            return 1006632960;
        }
        throw new IllegalArgumentException("Invalid mode: " + string2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static PathStrategy parsePathStrategy(Context context, String string2) throws IOException, XmlPullParserException {
        SimplePathStrategy simplePathStrategy = new SimplePathStrategy(string2);
        XmlResourceParser xmlResourceParser = context.getPackageManager().resolveContentProvider(string2, 128).loadXmlMetaData(context.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
        if (xmlResourceParser == null) {
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
        }
        int n;
        while ((n = xmlResourceParser.next()) != 1) {
            if (n != 2) continue;
            String string3 = xmlResourceParser.getName();
            String string4 = xmlResourceParser.getAttributeValue(null, "name");
            String string5 = xmlResourceParser.getAttributeValue(null, "path");
            string2 = null;
            if ("root-path".equals((Object)string3)) {
                string2 = FileProvider.buildPath(DEVICE_ROOT, string5);
            } else if ("files-path".equals((Object)string3)) {
                string2 = FileProvider.buildPath(context.getFilesDir(), string5);
            } else if ("cache-path".equals((Object)string3)) {
                string2 = FileProvider.buildPath(context.getCacheDir(), string5);
            } else if ("external-path".equals((Object)string3)) {
                string2 = FileProvider.buildPath(Environment.getExternalStorageDirectory(), string5);
            }
            if (string2 == null) continue;
            simplePathStrategy.addRoot(string4, (File)string2);
        }
        return simplePathStrategy;
    }

    public void attachInfo(Context context, ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (providerInfo.exported) {
            throw new SecurityException("Provider must not be exported");
        }
        if (!providerInfo.grantUriPermissions) {
            throw new SecurityException("Provider must grant uri permissions");
        }
        this.mStrategy = FileProvider.getPathStrategy(context, providerInfo.authority);
    }

    public int delete(Uri uri, String string2, String[] arrstring) {
        if (this.mStrategy.getFileForUri(uri).delete()) {
            return 1;
        }
        return 0;
    }

    public String getType(Uri object) {
        int n = (object = this.mStrategy.getFileForUri((Uri)object)).getName().lastIndexOf(46);
        if (n >= 0) {
            object = object.getName().substring(n + 1);
            object = MimeTypeMap.getSingleton().getMimeTypeFromExtension((String)object);
            if (object != null) {
                return object;
            }
        }
        return "application/octet-stream";
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("No external inserts");
    }

    public boolean onCreate() {
        return true;
    }

    public ParcelFileDescriptor openFile(Uri uri, String string2) throws FileNotFoundException {
        return ParcelFileDescriptor.open((File)this.mStrategy.getFileForUri(uri), (int)FileProvider.modeToMode(string2));
    }

    /*
     * Enabled aggressive block sorting
     */
    public Cursor query(Uri matrixCursor, String[] arrstring, String string2, String[] arrstring2, String string3) {
        string2 = this.mStrategy.getFileForUri((Uri)matrixCursor);
        matrixCursor = arrstring;
        if (arrstring == null) {
            matrixCursor = COLUMNS;
        }
        arrstring2 = new String[matrixCursor.length];
        arrstring = new Object[matrixCursor.length];
        int n = matrixCursor.length;
        int n2 = 0;
        int n3 = 0;
        do {
            int n4;
            if (n2 >= n) {
                matrixCursor = FileProvider.copyOf(arrstring2, n3);
                arrstring = FileProvider.copyOf((Object[])arrstring, n3);
                matrixCursor = new MatrixCursor((String[])matrixCursor, 1);
                matrixCursor.addRow((Object[])arrstring);
                return matrixCursor;
            }
            string3 = matrixCursor[n2];
            if ("_display_name".equals((Object)string3)) {
                arrstring2[n3] = "_display_name";
                n4 = n3 + 1;
                arrstring[n3] = string2.getName();
                n3 = n4;
            } else if ("_size".equals((Object)string3)) {
                arrstring2[n3] = "_size";
                n4 = n3 + 1;
                arrstring[n3] = Long.valueOf((long)string2.length());
                n3 = n4;
            }
            ++n2;
        } while (true);
    }

    public int update(Uri uri, ContentValues contentValues, String string2, String[] arrstring) {
        throw new UnsupportedOperationException("No external updates");
    }

    static interface PathStrategy {
        public File getFileForUri(Uri var1);

        public Uri getUriForFile(File var1);
    }

    static class SimplePathStrategy
    implements PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap();

        public SimplePathStrategy(String string2) {
            this.mAuthority = string2;
        }

        public void addRoot(String string2, File file) {
            File file2;
            if (TextUtils.isEmpty((CharSequence)string2)) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            try {
                file2 = file.getCanonicalFile();
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + (Object)file, (Throwable)iOException);
            }
            this.mRoots.put((Object)string2, (Object)file2);
        }

        @Override
        public File getFileForUri(Uri uri) {
            String string2;
            string2 = uri.getEncodedPath();
            int n = string2.indexOf(47, 1);
            String string3 = Uri.decode((String)string2.substring(1, n));
            string2 = Uri.decode((String)string2.substring(n + 1));
            if ((string3 = (File)this.mRoots.get((Object)string3)) == null) {
                throw new IllegalArgumentException("Unable to find configured root for " + (Object)uri);
            }
            uri = new File((File)string3, string2);
            try {
                string2 = uri.getCanonicalFile();
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + (Object)uri);
            }
            if (!string2.getPath().startsWith(string3.getPath())) {
                throw new SecurityException("Resolved path jumped beyond configured root");
            }
            return string2;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public Uri getUriForFile(File object) {
            Object object22;
            String string2;
            try {
                string2 = object.getCanonicalPath();
                object = null;
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + object);
            }
            for (Object object22 : this.mRoots.entrySet()) {
                String string3 = ((File)object22.getValue()).getPath();
                if (!string2.startsWith(string3) || object != null && string3.length() <= ((File)object.getValue()).getPath().length()) continue;
                object = object22;
            }
            if (object == null) {
                throw new IllegalArgumentException("Failed to find configured root that contains " + string2);
            }
            object22 = ((File)object.getValue()).getPath();
            object22 = object22.endsWith("/") ? string2.substring(object22.length()) : string2.substring(object22.length() + 1);
            object = Uri.encode((String)((String)object.getKey())) + '/' + Uri.encode((String)object22, (String)"/");
            return new Uri.Builder().scheme("content").authority(this.mAuthority).encodedPath((String)object).build();
        }
    }

}

