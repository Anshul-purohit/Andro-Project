// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.print;

import java.io.FileNotFoundException;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.Build$VERSION;
import android.content.Context;

public final class PrintHelper
{
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    PrintHelperVersionImpl mImpl;
    
    public PrintHelper(final Context context) {
        if (systemSupportsPrint()) {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperKitkatImpl(context);
            return;
        }
        this.mImpl = (PrintHelperVersionImpl)new PrintHelperStubImpl();
    }
    
    public static boolean systemSupportsPrint() {
        return Build$VERSION.SDK_INT >= 19;
    }
    
    public int getColorMode() {
        return this.mImpl.getColorMode();
    }
    
    public int getOrientation() {
        return this.mImpl.getOrientation();
    }
    
    public int getScaleMode() {
        return this.mImpl.getScaleMode();
    }
    
    public void printBitmap(final String s, final Bitmap bitmap) {
        this.mImpl.printBitmap(s, bitmap);
    }
    
    public void printBitmap(final String s, final Uri uri) throws FileNotFoundException {
        this.mImpl.printBitmap(s, uri);
    }
    
    public void setColorMode(final int colorMode) {
        this.mImpl.setColorMode(colorMode);
    }
    
    public void setOrientation(final int orientation) {
        this.mImpl.setOrientation(orientation);
    }
    
    public void setScaleMode(final int scaleMode) {
        this.mImpl.setScaleMode(scaleMode);
    }
    
    private static final class PrintHelperKitkatImpl implements PrintHelperVersionImpl
    {
        private final PrintHelperKitkat mPrintHelper;
        
        PrintHelperKitkatImpl(final Context context) {
            this.mPrintHelper = new PrintHelperKitkat(context);
        }
        
        @Override
        public int getColorMode() {
            return this.mPrintHelper.getColorMode();
        }
        
        @Override
        public int getOrientation() {
            return this.mPrintHelper.getOrientation();
        }
        
        @Override
        public int getScaleMode() {
            return this.mPrintHelper.getScaleMode();
        }
        
        @Override
        public void printBitmap(final String s, final Bitmap bitmap) {
            this.mPrintHelper.printBitmap(s, bitmap);
        }
        
        @Override
        public void printBitmap(final String s, final Uri uri) throws FileNotFoundException {
            this.mPrintHelper.printBitmap(s, uri);
        }
        
        @Override
        public void setColorMode(final int colorMode) {
            this.mPrintHelper.setColorMode(colorMode);
        }
        
        @Override
        public void setOrientation(final int orientation) {
            this.mPrintHelper.setOrientation(orientation);
        }
        
        @Override
        public void setScaleMode(final int scaleMode) {
            this.mPrintHelper.setScaleMode(scaleMode);
        }
    }
    
    private static final class PrintHelperStubImpl implements PrintHelperVersionImpl
    {
        int mColorMode;
        int mOrientation;
        int mScaleMode;
        
        private PrintHelperStubImpl() {
            this.mScaleMode = 2;
            this.mColorMode = 2;
            this.mOrientation = 1;
        }
        
        @Override
        public int getColorMode() {
            return this.mColorMode;
        }
        
        @Override
        public int getOrientation() {
            return this.mOrientation;
        }
        
        @Override
        public int getScaleMode() {
            return this.mScaleMode;
        }
        
        @Override
        public void printBitmap(final String s, final Bitmap bitmap) {
        }
        
        @Override
        public void printBitmap(final String s, final Uri uri) {
        }
        
        @Override
        public void setColorMode(final int mColorMode) {
            this.mColorMode = mColorMode;
        }
        
        @Override
        public void setOrientation(final int mOrientation) {
            this.mOrientation = mOrientation;
        }
        
        @Override
        public void setScaleMode(final int mScaleMode) {
            this.mScaleMode = mScaleMode;
        }
    }
    
    interface PrintHelperVersionImpl
    {
        int getColorMode();
        
        int getOrientation();
        
        int getScaleMode();
        
        void printBitmap(final String p0, final Bitmap p1);
        
        void printBitmap(final String p0, final Uri p1) throws FileNotFoundException;
        
        void setColorMode(final int p0);
        
        void setOrientation(final int p0);
        
        void setScaleMode(final int p0);
    }
}
