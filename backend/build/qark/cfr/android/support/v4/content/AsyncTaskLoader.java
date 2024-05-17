/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.os.SystemClock
 *  java.io.FileDescriptor
 *  java.io.PrintWriter
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.Void
 *  java.util.concurrent.CountDownLatch
 *  java.util.concurrent.Executor
 */
package android.support.v4.content;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.Loader;
import android.support.v4.content.ModernAsyncTask;
import android.support.v4.util.TimeUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public abstract class AsyncTaskLoader<D>
extends Loader<D> {
    static final boolean DEBUG = false;
    static final String TAG = "AsyncTaskLoader";
    volatile AsyncTaskLoader<D> mCancellingTask;
    Handler mHandler;
    long mLastLoadCompleteTime = -10000L;
    volatile AsyncTaskLoader<D> mTask;
    long mUpdateThrottle;

    public AsyncTaskLoader(Context context) {
        super(context);
    }

    public boolean cancelLoad() {
        block7 : {
            block6 : {
                if (this.mTask == null) break block6;
                if (this.mCancellingTask == null) break block7;
                if (this.mTask.waiting) {
                    this.mTask.waiting = false;
                    this.mHandler.removeCallbacks(this.mTask);
                }
                this.mTask = null;
            }
            return false;
        }
        if (this.mTask.waiting) {
            this.mTask.waiting = false;
            this.mHandler.removeCallbacks(this.mTask);
            this.mTask = null;
            return false;
        }
        boolean bl = this.mTask.cancel(false);
        if (bl) {
            this.mCancellingTask = this.mTask;
        }
        this.mTask = null;
        return bl;
    }

    void dispatchOnCancelled(AsyncTaskLoader<D> asyncTaskLoader, D d) {
        this.onCanceled(d);
        if (this.mCancellingTask == asyncTaskLoader) {
            this.rollbackContentChanged();
            this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
            this.mCancellingTask = null;
            this.executePendingTask();
        }
    }

    void dispatchOnLoadComplete(AsyncTaskLoader<D> asyncTaskLoader, D d) {
        if (this.mTask != asyncTaskLoader) {
            this.dispatchOnCancelled(asyncTaskLoader, d);
            return;
        }
        if (this.isAbandoned()) {
            this.onCanceled(d);
            return;
        }
        this.commitContentChanged();
        this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
        this.mTask = null;
        this.deliverResult(d);
    }

    @Override
    public void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
        super.dump(string2, fileDescriptor, printWriter, arrstring);
        if (this.mTask != null) {
            printWriter.print(string2);
            printWriter.print("mTask=");
            printWriter.print(this.mTask);
            printWriter.print(" waiting=");
            printWriter.println(this.mTask.waiting);
        }
        if (this.mCancellingTask != null) {
            printWriter.print(string2);
            printWriter.print("mCancellingTask=");
            printWriter.print(this.mCancellingTask);
            printWriter.print(" waiting=");
            printWriter.println(this.mCancellingTask.waiting);
        }
        if (this.mUpdateThrottle != 0L) {
            printWriter.print(string2);
            printWriter.print("mUpdateThrottle=");
            TimeUtils.formatDuration(this.mUpdateThrottle, printWriter);
            printWriter.print(" mLastLoadCompleteTime=");
            TimeUtils.formatDuration(this.mLastLoadCompleteTime, SystemClock.uptimeMillis(), printWriter);
            printWriter.println();
        }
    }

    void executePendingTask() {
        block5 : {
            block4 : {
                if (this.mCancellingTask != null || this.mTask == null) break block4;
                if (this.mTask.waiting) {
                    this.mTask.waiting = false;
                    this.mHandler.removeCallbacks(this.mTask);
                }
                if (this.mUpdateThrottle <= 0L || SystemClock.uptimeMillis() >= this.mLastLoadCompleteTime + this.mUpdateThrottle) break block5;
                this.mTask.waiting = true;
                this.mHandler.postAtTime(this.mTask, this.mLastLoadCompleteTime + this.mUpdateThrottle);
            }
            return;
        }
        this.mTask.executeOnExecutor(ModernAsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    public abstract D loadInBackground();

    public void onCanceled(D d) {
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        this.cancelLoad();
        this.mTask = new LoadTask();
        this.executePendingTask();
    }

    protected D onLoadInBackground() {
        return this.loadInBackground();
    }

    public void setUpdateThrottle(long l) {
        this.mUpdateThrottle = l;
        if (l != 0L) {
            this.mHandler = new Handler();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void waitForLoader() {
        AsyncTaskLoader<D> asyncTaskLoader = this.mTask;
        if (asyncTaskLoader == null) return;
        try {
            asyncTaskLoader.done.await();
            return;
        }
        catch (InterruptedException interruptedException) {
            return;
        }
    }

    final class LoadTask
    extends ModernAsyncTask<Void, Void, D>
    implements Runnable {
        private CountDownLatch done;
        D result;
        boolean waiting;

        LoadTask() {
            this.done = new CountDownLatch(1);
        }

        protected /* varargs */ D doInBackground(Void ... arrvoid) {
            this.result = AsyncTaskLoader.this.onLoadInBackground();
            return this.result;
        }

        @Override
        protected void onCancelled() {
            try {
                AsyncTaskLoader.this.dispatchOnCancelled(this, this.result);
                return;
            }
            finally {
                this.done.countDown();
            }
        }

        @Override
        protected void onPostExecute(D d) {
            try {
                AsyncTaskLoader.this.dispatchOnLoadComplete(this, d);
                return;
            }
            finally {
                this.done.countDown();
            }
        }

        public void run() {
            this.waiting = false;
            AsyncTaskLoader.this.executePendingTask();
        }
    }

}

