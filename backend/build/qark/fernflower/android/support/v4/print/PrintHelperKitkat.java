package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.BitmapFactory.Options;
import android.graphics.pdf.PdfDocument.Page;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.CancellationSignal.OnCancelListener;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.PrintAttributes.Builder;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.print.PrintDocumentAdapter.WriteResultCallback;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class PrintHelperKitkat {
   public static final int COLOR_MODE_COLOR = 2;
   public static final int COLOR_MODE_MONOCHROME = 1;
   private static final String LOG_TAG = "PrintHelperKitkat";
   private static final int MAX_PRINT_SIZE = 3500;
   public static final int ORIENTATION_LANDSCAPE = 1;
   public static final int ORIENTATION_PORTRAIT = 2;
   public static final int SCALE_MODE_FILL = 2;
   public static final int SCALE_MODE_FIT = 1;
   int mColorMode = 2;
   final Context mContext;
   Options mDecodeOptions = null;
   private final Object mLock = new Object();
   int mOrientation = 1;
   int mScaleMode = 2;

   PrintHelperKitkat(Context var1) {
      this.mContext = var1;
   }

   private Matrix getMatrix(int var1, int var2, RectF var3, int var4) {
      Matrix var6 = new Matrix();
      float var5 = var3.width() / (float)var1;
      if (var4 == 2) {
         var5 = Math.max(var5, var3.height() / (float)var2);
      } else {
         var5 = Math.min(var5, var3.height() / (float)var2);
      }

      var6.postScale(var5, var5);
      var6.postTranslate((var3.width() - (float)var1 * var5) / 2.0F, (var3.height() - (float)var2 * var5) / 2.0F);
      return var6;
   }

   private Bitmap loadBitmap(Uri var1, Options var2) throws FileNotFoundException {
      if (var1 != null && this.mContext != null) {
         InputStream var3 = null;

         InputStream var16;
         Bitmap var18;
         label130: {
            Throwable var10000;
            label131: {
               boolean var10001;
               try {
                  var16 = this.mContext.getContentResolver().openInputStream(var1);
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label131;
               }

               var3 = var16;

               label118:
               try {
                  var18 = BitmapFactory.decodeStream(var16, (Rect)null, var2);
                  break label130;
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label118;
               }
            }

            Throwable var17 = var10000;
            if (var3 != null) {
               try {
                  var3.close();
               } catch (IOException var12) {
                  Log.w("PrintHelperKitkat", "close fail ", var12);
               }
            }

            throw var17;
         }

         if (var16 != null) {
            try {
               var16.close();
            } catch (IOException var13) {
               Log.w("PrintHelperKitkat", "close fail ", var13);
               return var18;
            }
         }

         return var18;
      } else {
         throw new IllegalArgumentException("bad argument to loadBitmap");
      }
   }

   private Bitmap loadConstrainedBitmap(Uri param1, int param2) throws FileNotFoundException {
      // $FF: Couldn't be decompiled
   }

   public int getColorMode() {
      return this.mColorMode;
   }

   public int getOrientation() {
      return this.mOrientation;
   }

   public int getScaleMode() {
      return this.mScaleMode;
   }

   public void printBitmap(final String var1, final Bitmap var2) {
      if (var2 != null) {
         final int var3 = this.mScaleMode;
         PrintManager var5 = (PrintManager)this.mContext.getSystemService("print");
         MediaSize var4 = MediaSize.UNKNOWN_PORTRAIT;
         if (var2.getWidth() > var2.getHeight()) {
            var4 = MediaSize.UNKNOWN_LANDSCAPE;
         }

         PrintAttributes var6 = (new Builder()).setMediaSize(var4).setColorMode(this.mColorMode).build();
         var5.print(var1, new PrintDocumentAdapter() {
            private PrintAttributes mAttributes;

            public void onLayout(PrintAttributes var1x, PrintAttributes var2x, CancellationSignal var3x, LayoutResultCallback var4, Bundle var5) {
               boolean var6 = true;
               this.mAttributes = var2x;
               PrintDocumentInfo var7 = (new android.print.PrintDocumentInfo.Builder(var1)).setContentType(1).setPageCount(1).build();
               if (var2x.equals(var1x)) {
                  var6 = false;
               }

               var4.onLayoutFinished(var7, var6);
            }

            public void onWrite(PageRange[] var1x, ParcelFileDescriptor var2x, CancellationSignal var3x, WriteResultCallback var4) {
               PrintedPdfDocument var13 = new PrintedPdfDocument(PrintHelperKitkat.this.mContext, this.mAttributes);

               try {
                  Page var14 = var13.startPage(1);
                  RectF var5 = new RectF(var14.getInfo().getContentRect());
                  Matrix var15 = PrintHelperKitkat.this.getMatrix(var2.getWidth(), var2.getHeight(), var5, var3);
                  var14.getCanvas().drawBitmap(var2, var15, (Paint)null);
                  var13.finishPage(var14);

                  try {
                     var13.writeTo(new FileOutputStream(var2x.getFileDescriptor()));
                     var4.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                  } catch (IOException var11) {
                     Log.e("PrintHelperKitkat", "Error writing printed content", var11);
                     var4.onWriteFailed((CharSequence)null);
                  }
               } finally {
                  if (var13 != null) {
                     var13.close();
                  }

                  if (var2x != null) {
                     try {
                        var2x.close();
                     } catch (IOException var10) {
                     }
                  }

               }

            }
         }, var6);
      }
   }

   public void printBitmap(final String var1, final Uri var2) throws FileNotFoundException {
      PrintDocumentAdapter var5 = new PrintDocumentAdapter(this.mScaleMode) {
         AsyncTask loadBitmap;
         private PrintAttributes mAttributes;
         Bitmap mBitmap;
         // $FF: synthetic field
         final int val$fittingMode;

         {
            this.val$fittingMode = var4;
            this.mBitmap = null;
         }

         private void cancelLoad() {
            Object var1x = PrintHelperKitkat.this.mLock;
            synchronized(var1x){}

            Throwable var10000;
            boolean var10001;
            label122: {
               try {
                  if (PrintHelperKitkat.this.mDecodeOptions != null) {
                     PrintHelperKitkat.this.mDecodeOptions.requestCancelDecode();
                     PrintHelperKitkat.this.mDecodeOptions = null;
                  }
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label122;
               }

               label119:
               try {
                  return;
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label119;
               }
            }

            while(true) {
               Throwable var2x = var10000;

               try {
                  throw var2x;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  continue;
               }
            }
         }

         public void onFinish() {
            super.onFinish();
            this.cancelLoad();
            this.loadBitmap.cancel(true);
         }

         public void onLayout(final PrintAttributes var1x, final PrintAttributes var2x, final CancellationSignal var3, final LayoutResultCallback var4, Bundle var5) {
            boolean var6 = true;
            if (var3.isCanceled()) {
               var4.onLayoutCancelled();
               this.mAttributes = var2x;
            } else if (this.mBitmap != null) {
               PrintDocumentInfo var7 = (new android.print.PrintDocumentInfo.Builder(var1)).setContentType(1).setPageCount(1).build();
               if (var2x.equals(var1x)) {
                  var6 = false;
               }

               var4.onLayoutFinished(var7, var6);
            } else {
               this.loadBitmap = new AsyncTask() {
                  protected Bitmap doInBackground(Uri... var1xx) {
                     try {
                        Bitmap var3x = PrintHelperKitkat.this.loadConstrainedBitmap(var2, 3500);
                        return var3x;
                     } catch (FileNotFoundException var2xx) {
                        return null;
                     }
                  }

                  protected void onCancelled(Bitmap var1xx) {
                     var4.onLayoutCancelled();
                  }

                  protected void onPostExecute(Bitmap var1xx) {
                     boolean var2xx = true;
                     super.onPostExecute(var1xx);
                     mBitmap = var1xx;
                     if (var1xx != null) {
                        PrintDocumentInfo var3x = (new android.print.PrintDocumentInfo.Builder(var1)).setContentType(1).setPageCount(1).build();
                        if (var2x.equals(var1x)) {
                           var2xx = false;
                        }

                        var4.onLayoutFinished(var3x, var2xx);
                     } else {
                        var4.onLayoutFailed((CharSequence)null);
                     }
                  }

                  protected void onPreExecute() {
                     var3.setOnCancelListener(new OnCancelListener() {
                        public void onCancel() {
                           cancelLoad();
                           cancel(false);
                        }
                     });
                  }
               };
               this.loadBitmap.execute(new Uri[0]);
               this.mAttributes = var2x;
            }
         }

         public void onWrite(PageRange[] var1x, ParcelFileDescriptor var2x, CancellationSignal var3, WriteResultCallback var4) {
            PrintedPdfDocument var13 = new PrintedPdfDocument(PrintHelperKitkat.this.mContext, this.mAttributes);

            try {
               Page var14 = var13.startPage(1);
               RectF var5 = new RectF(var14.getInfo().getContentRect());
               Matrix var15 = PrintHelperKitkat.this.getMatrix(this.mBitmap.getWidth(), this.mBitmap.getHeight(), var5, this.val$fittingMode);
               var14.getCanvas().drawBitmap(this.mBitmap, var15, (Paint)null);
               var13.finishPage(var14);

               try {
                  var13.writeTo(new FileOutputStream(var2x.getFileDescriptor()));
                  var4.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
               } catch (IOException var11) {
                  Log.e("PrintHelperKitkat", "Error writing printed content", var11);
                  var4.onWriteFailed((CharSequence)null);
               }
            } finally {
               if (var13 != null) {
                  var13.close();
               }

               if (var2x != null) {
                  try {
                     var2x.close();
                  } catch (IOException var10) {
                  }
               }

            }

         }
      };
      PrintManager var3 = (PrintManager)this.mContext.getSystemService("print");
      Builder var4 = new Builder();
      var4.setColorMode(this.mColorMode);
      if (this.mOrientation == 1) {
         var4.setMediaSize(MediaSize.UNKNOWN_LANDSCAPE);
      } else if (this.mOrientation == 2) {
         var4.setMediaSize(MediaSize.UNKNOWN_PORTRAIT);
      }

      var3.print(var1, var5, var4.build());
   }

   public void setColorMode(int var1) {
      this.mColorMode = var1;
   }

   public void setOrientation(int var1) {
      this.mOrientation = var1;
   }

   public void setScaleMode(int var1) {
      this.mScaleMode = var1;
   }
}
