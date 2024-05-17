/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Bitmap
 *  android.graphics.BitmapShader
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.graphics.drawable.Animatable
 *  android.graphics.drawable.AnimationDrawable
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.ClipDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.graphics.drawable.LayerDrawable
 *  android.graphics.drawable.ShapeDrawable
 *  android.graphics.drawable.shapes.RoundRectShape
 *  android.graphics.drawable.shapes.Shape
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.SystemClock
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$BaseSavedState
 *  android.view.animation.AlphaAnimation
 *  android.view.animation.AnimationUtils
 *  android.view.animation.Interpolator
 *  android.view.animation.LinearInterpolator
 *  android.view.animation.Transformation
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.Thread
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class ProgressBarICS
extends View {
    private static final int ANIMATION_RESOLUTION = 200;
    private static final int MAX_LEVEL = 10000;
    private static final int[] android_R_styleable_ProgressBar = new int[]{16843062, 16843063, 16843064, 16843065, 16843066, 16843067, 16843068, 16843069, 16843070, 16843071, 16843039, 16843072, 16843040, 16843073};
    private AlphaAnimation mAnimation;
    private int mBehavior;
    private Drawable mCurrentDrawable;
    private int mDuration;
    private boolean mInDrawing;
    private boolean mIndeterminate;
    private Drawable mIndeterminateDrawable;
    private Interpolator mInterpolator;
    private long mLastDrawTime;
    private int mMax;
    int mMaxHeight;
    int mMaxWidth;
    int mMinHeight;
    int mMinWidth;
    private boolean mNoInvalidate;
    private boolean mOnlyIndeterminate;
    private int mProgress;
    private Drawable mProgressDrawable;
    private RefreshProgressRunnable mRefreshProgressRunnable;
    Bitmap mSampleTile;
    private int mSecondaryProgress;
    private boolean mShouldStartAnimationDrawable;
    private Transformation mTransformation;
    private long mUiThreadId;

    public ProgressBarICS(Context context, AttributeSet attributeSet, int n, int n2) {
        boolean bl = false;
        super(context, attributeSet, n);
        this.mUiThreadId = Thread.currentThread().getId();
        this.initProgressBar();
        attributeSet = context.obtainStyledAttributes(attributeSet, android_R_styleable_ProgressBar, n, n2);
        this.mNoInvalidate = true;
        this.setMax(attributeSet.getInt(0, this.mMax));
        this.setProgress(attributeSet.getInt(1, this.mProgress));
        this.setSecondaryProgress(attributeSet.getInt(2, this.mSecondaryProgress));
        boolean bl2 = attributeSet.getBoolean(3, this.mIndeterminate);
        this.mOnlyIndeterminate = attributeSet.getBoolean(4, this.mOnlyIndeterminate);
        Drawable drawable2 = attributeSet.getDrawable(5);
        if (drawable2 != null) {
            this.setIndeterminateDrawable(this.tileifyIndeterminate(drawable2));
        }
        if ((drawable2 = attributeSet.getDrawable(6)) != null) {
            this.setProgressDrawable(this.tileify(drawable2, false));
        }
        this.mDuration = attributeSet.getInt(7, this.mDuration);
        this.mBehavior = attributeSet.getInt(8, this.mBehavior);
        this.mMinWidth = attributeSet.getDimensionPixelSize(9, this.mMinWidth);
        this.mMaxWidth = attributeSet.getDimensionPixelSize(10, this.mMaxWidth);
        this.mMinHeight = attributeSet.getDimensionPixelSize(11, this.mMinHeight);
        this.mMaxHeight = attributeSet.getDimensionPixelSize(12, this.mMaxHeight);
        n = attributeSet.getResourceId(13, 17432587);
        if (n > 0) {
            this.setInterpolator(context, n);
        }
        attributeSet.recycle();
        this.mNoInvalidate = false;
        if (this.mOnlyIndeterminate || bl2) {
            bl = true;
        }
        this.setIndeterminate(bl);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void doRefreshProgress(int n, int n2, boolean bl, boolean bl2) {
        synchronized (this) {
            float f = this.mMax > 0 ? (float)n2 / (float)this.mMax : 0.0f;
            Drawable drawable2 = this.mCurrentDrawable;
            if (drawable2 == null) {
                this.invalidate();
            } else {
                Drawable drawable3 = null;
                if (drawable2 instanceof LayerDrawable) {
                    drawable3 = ((LayerDrawable)drawable2).findDrawableByLayerId(n);
                }
                n = (int)(10000.0f * f);
                if (drawable3 == null) {
                    drawable3 = drawable2;
                }
                drawable3.setLevel(n);
            }
            return;
        }
    }

    private void initProgressBar() {
        this.mMax = 100;
        this.mProgress = 0;
        this.mSecondaryProgress = 0;
        this.mIndeterminate = false;
        this.mOnlyIndeterminate = false;
        this.mDuration = 4000;
        this.mBehavior = 1;
        this.mMinWidth = 24;
        this.mMaxWidth = 48;
        this.mMinHeight = 24;
        this.mMaxHeight = 48;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void refreshProgress(int n, int n2, boolean bl) {
        synchronized (this) {
            if (this.mUiThreadId == Thread.currentThread().getId()) {
                this.doRefreshProgress(n, n2, bl, true);
            } else {
                RefreshProgressRunnable refreshProgressRunnable;
                if (this.mRefreshProgressRunnable != null) {
                    refreshProgressRunnable = this.mRefreshProgressRunnable;
                    this.mRefreshProgressRunnable = null;
                    refreshProgressRunnable.setup(n, n2, bl);
                } else {
                    refreshProgressRunnable = new RefreshProgressRunnable(n, n2, bl);
                }
                this.post((Runnable)refreshProgressRunnable);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private Drawable tileify(Drawable layerDrawable, boolean bl) {
        LayerDrawable layerDrawable2;
        LayerDrawable layerDrawable3;
        int n;
        int n2;
        if (layerDrawable instanceof LayerDrawable) {
            layerDrawable3 = layerDrawable;
            n2 = layerDrawable3.getNumberOfLayers();
            layerDrawable = new Drawable[n2];
        } else {
            if (!(layerDrawable instanceof BitmapDrawable)) {
                return layerDrawable;
            }
            layerDrawable = ((BitmapDrawable)layerDrawable).getBitmap();
            if (this.mSampleTile == null) {
                this.mSampleTile = layerDrawable;
            }
            ShapeDrawable shapeDrawable = new ShapeDrawable(this.getDrawableShape());
            layerDrawable = new BitmapShader((Bitmap)layerDrawable, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            shapeDrawable.getPaint().setShader((Shader)layerDrawable);
            layerDrawable = shapeDrawable;
            if (!bl) return layerDrawable;
            return new ClipDrawable((Drawable)shapeDrawable, 3, 1);
        }
        for (n = 0; n < n2; ++n) {
            int n3 = layerDrawable3.getId(n);
            layerDrawable2 = layerDrawable3.getDrawable(n);
            bl = n3 == 16908301 || n3 == 16908303;
            layerDrawable[n] = this.tileify((Drawable)layerDrawable2, bl);
        }
        layerDrawable2 = new LayerDrawable((Drawable[])layerDrawable);
        n = 0;
        do {
            layerDrawable = layerDrawable2;
            if (n >= n2) return layerDrawable;
            layerDrawable2.setId(n, layerDrawable3.getId(n));
            ++n;
        } while (true);
    }

    private Drawable tileifyIndeterminate(Drawable drawable2) {
        Drawable drawable3 = drawable2;
        if (drawable2 instanceof AnimationDrawable) {
            drawable2 = (AnimationDrawable)drawable2;
            int n = drawable2.getNumberOfFrames();
            drawable3 = new AnimationDrawable();
            drawable3.setOneShot(drawable2.isOneShot());
            for (int i = 0; i < n; ++i) {
                Drawable drawable4 = this.tileify(drawable2.getFrame(i), true);
                drawable4.setLevel(10000);
                drawable3.addFrame(drawable4, drawable2.getDuration(i));
            }
            drawable3.setLevel(10000);
        }
        return drawable3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateDrawableBounds(int n, int n2) {
        int n3 = n - this.getPaddingRight() - this.getPaddingLeft();
        int n4 = n2 - this.getPaddingBottom() - this.getPaddingTop();
        int n5 = 0;
        int n6 = 0;
        int n7 = n4;
        int n8 = n3;
        if (this.mIndeterminateDrawable != null) {
            n7 = n4;
            int n9 = n6;
            n8 = n3;
            int n10 = n5;
            if (this.mOnlyIndeterminate) {
                n7 = n4;
                n9 = n6;
                n8 = n3;
                n10 = n5;
                if (!(this.mIndeterminateDrawable instanceof AnimationDrawable)) {
                    n7 = this.mIndeterminateDrawable.getIntrinsicWidth();
                    n8 = this.mIndeterminateDrawable.getIntrinsicHeight();
                    float f = (float)n7 / (float)n8;
                    float f2 = (float)n / (float)n2;
                    n7 = n4;
                    n9 = n6;
                    n8 = n3;
                    n10 = n5;
                    if (f != f2) {
                        if (f2 > f) {
                            n2 = (int)((float)n2 * f);
                            n9 = (n - n2) / 2;
                            n8 = n9 + n2;
                            n10 = n5;
                            n7 = n4;
                        } else {
                            n = (int)((float)n * (1.0f / f));
                            n10 = (n2 - n) / 2;
                            n7 = n10 + n;
                            n9 = n6;
                            n8 = n3;
                        }
                    }
                }
            }
            this.mIndeterminateDrawable.setBounds(n9, n10, n8, n7);
        }
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.setBounds(0, 0, n8, n7);
        }
    }

    private void updateDrawableState() {
        int[] arrn = this.getDrawableState();
        if (this.mProgressDrawable != null && this.mProgressDrawable.isStateful()) {
            this.mProgressDrawable.setState(arrn);
        }
        if (this.mIndeterminateDrawable != null && this.mIndeterminateDrawable.isStateful()) {
            this.mIndeterminateDrawable.setState(arrn);
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.updateDrawableState();
    }

    Shape getDrawableShape() {
        return new RoundRectShape(new float[]{5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f}, null, null);
    }

    public Drawable getIndeterminateDrawable() {
        return this.mIndeterminateDrawable;
    }

    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }

    public int getMax() {
        synchronized (this) {
            int n = this.mMax;
            return n;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getProgress() {
        synchronized (this) {
            block6 : {
                boolean bl = this.mIndeterminate;
                if (!bl) break block6;
                return 0;
            }
            int n = this.mProgress;
            return n;
        }
    }

    public Drawable getProgressDrawable() {
        return this.mProgressDrawable;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getSecondaryProgress() {
        synchronized (this) {
            block6 : {
                boolean bl = this.mIndeterminate;
                if (!bl) break block6;
                return 0;
            }
            int n = this.mSecondaryProgress;
            return n;
        }
    }

    public final void incrementProgressBy(int n) {
        synchronized (this) {
            this.setProgress(this.mProgress + n);
            return;
        }
    }

    public final void incrementSecondaryProgressBy(int n) {
        synchronized (this) {
            this.setSecondaryProgress(this.mSecondaryProgress + n);
            return;
        }
    }

    public void invalidateDrawable(Drawable drawable2) {
        block3 : {
            block2 : {
                if (this.mInDrawing) break block2;
                if (!this.verifyDrawable(drawable2)) break block3;
                drawable2 = drawable2.getBounds();
                int n = this.getScrollX() + this.getPaddingLeft();
                int n2 = this.getScrollY() + this.getPaddingTop();
                this.invalidate(drawable2.left + n, drawable2.top + n2, drawable2.right + n, drawable2.bottom + n2);
            }
            return;
        }
        super.invalidateDrawable(drawable2);
    }

    public boolean isIndeterminate() {
        synchronized (this) {
            boolean bl = this.mIndeterminate;
            return bl;
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mIndeterminate) {
            this.startAnimation();
        }
    }

    protected void onDetachedFromWindow() {
        if (this.mIndeterminate) {
            this.stopAnimation();
        }
        if (this.mRefreshProgressRunnable != null) {
            this.removeCallbacks((Runnable)this.mRefreshProgressRunnable);
        }
        super.onDetachedFromWindow();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onDraw(Canvas canvas) {
        synchronized (this) {
            super.onDraw(canvas);
            Drawable drawable2 = this.mCurrentDrawable;
            if (drawable2 != null) {
                block9 : {
                    canvas.save();
                    canvas.translate((float)this.getPaddingLeft(), (float)this.getPaddingTop());
                    long l = this.getDrawingTime();
                    if (this.mAnimation != null) {
                        this.mAnimation.getTransformation(l, this.mTransformation);
                        float f = this.mTransformation.getAlpha();
                        this.mInDrawing = true;
                        drawable2.setLevel((int)(10000.0f * f));
                        if (SystemClock.uptimeMillis() - this.mLastDrawTime < 200L) break block9;
                        this.mLastDrawTime = SystemClock.uptimeMillis();
                        this.postInvalidateDelayed(200L);
                    }
                }
                drawable2.draw(canvas);
                canvas.restore();
                if (this.mShouldStartAnimationDrawable && drawable2 instanceof Animatable) {
                    ((Animatable)drawable2).start();
                    this.mShouldStartAnimationDrawable = false;
                }
            }
            return;
            finally {
                this.mInDrawing = false;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onMeasure(int n, int n2) {
        synchronized (this) {
            Drawable drawable2 = this.mCurrentDrawable;
            int n3 = 0;
            int n4 = 0;
            if (drawable2 != null) {
                n3 = Math.max((int)this.mMinWidth, (int)Math.min((int)this.mMaxWidth, (int)drawable2.getIntrinsicWidth()));
                n4 = Math.max((int)this.mMinHeight, (int)Math.min((int)this.mMaxHeight, (int)drawable2.getIntrinsicHeight()));
            }
            this.updateDrawableState();
            int n5 = this.getPaddingLeft();
            int n6 = this.getPaddingRight();
            int n7 = this.getPaddingTop();
            int n8 = this.getPaddingBottom();
            this.setMeasuredDimension(ProgressBarICS.resolveSize((int)(n3 + (n5 + n6)), (int)n), ProgressBarICS.resolveSize((int)(n4 + (n7 + n8)), (int)n2));
            return;
        }
    }

    public void onRestoreInstanceState(Parcelable object) {
        object = (SavedState)((Object)object);
        super.onRestoreInstanceState(object.getSuperState());
        this.setProgress(object.progress);
        this.setSecondaryProgress(object.secondaryProgress);
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.progress = this.mProgress;
        savedState.secondaryProgress = this.mSecondaryProgress;
        return savedState;
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        this.updateDrawableBounds(n, n2);
    }

    protected void onVisibilityChanged(View view, int n) {
        block3 : {
            block2 : {
                super.onVisibilityChanged(view, n);
                if (!this.mIndeterminate) break block2;
                if (n != 8 && n != 4) break block3;
                this.stopAnimation();
            }
            return;
        }
        this.startAnimation();
    }

    public void postInvalidate() {
        if (!this.mNoInvalidate) {
            super.postInvalidate();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void setIndeterminate(boolean bl) {
        synchronized (this) {
            block9 : {
                block8 : {
                    if (this.mOnlyIndeterminate && this.mIndeterminate || bl == this.mIndeterminate) break block8;
                    this.mIndeterminate = bl;
                    if (!bl) break block9;
                    this.mCurrentDrawable = this.mIndeterminateDrawable;
                    this.startAnimation();
                }
                do {
                    return;
                    break;
                } while (true);
            }
            this.mCurrentDrawable = this.mProgressDrawable;
            this.stopAnimation();
            return;
        }
    }

    public void setIndeterminateDrawable(Drawable drawable2) {
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this);
        }
        this.mIndeterminateDrawable = drawable2;
        if (this.mIndeterminate) {
            this.mCurrentDrawable = drawable2;
            this.postInvalidate();
        }
    }

    public void setInterpolator(Context context, int n) {
        this.setInterpolator(AnimationUtils.loadInterpolator((Context)context, (int)n));
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public void setMax(int n) {
        synchronized (this) {
            int n2 = n;
            if (n < 0) {
                n2 = 0;
            }
            if (n2 != this.mMax) {
                this.mMax = n2;
                this.postInvalidate();
                if (this.mProgress > n2) {
                    this.mProgress = n2;
                }
                this.refreshProgress(16908301, this.mProgress, false);
            }
            return;
        }
    }

    public void setProgress(int n) {
        synchronized (this) {
            this.setProgress(n, false);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void setProgress(int n, boolean bl) {
        synchronized (this) {
            boolean bl2 = this.mIndeterminate;
            if (!bl2) {
                int n2 = n;
                if (n < 0) {
                    n2 = 0;
                }
                n = n2;
                if (n2 > this.mMax) {
                    n = this.mMax;
                }
                if (n != this.mProgress) {
                    this.mProgress = n;
                    this.refreshProgress(16908301, this.mProgress, bl);
                }
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setProgressDrawable(Drawable drawable2) {
        boolean bl;
        if (this.mProgressDrawable != null && drawable2 != this.mProgressDrawable) {
            this.mProgressDrawable.setCallback(null);
            bl = true;
        } else {
            bl = false;
        }
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this);
            int n = drawable2.getMinimumHeight();
            if (this.mMaxHeight < n) {
                this.mMaxHeight = n;
                this.requestLayout();
            }
        }
        this.mProgressDrawable = drawable2;
        if (!this.mIndeterminate) {
            this.mCurrentDrawable = drawable2;
            this.postInvalidate();
        }
        if (bl) {
            this.updateDrawableBounds(this.getWidth(), this.getHeight());
            this.updateDrawableState();
            this.doRefreshProgress(16908301, this.mProgress, false, false);
            this.doRefreshProgress(16908303, this.mSecondaryProgress, false, false);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setSecondaryProgress(int n) {
        synchronized (this) {
            boolean bl = this.mIndeterminate;
            if (!bl) {
                int n2 = n;
                if (n < 0) {
                    n2 = 0;
                }
                n = n2;
                if (n2 > this.mMax) {
                    n = this.mMax;
                }
                if (n != this.mSecondaryProgress) {
                    this.mSecondaryProgress = n;
                    this.refreshProgress(16908303, this.mSecondaryProgress, false);
                }
            }
            return;
        }
    }

    public void setVisibility(int n) {
        block3 : {
            block2 : {
                if (this.getVisibility() == n) break block2;
                super.setVisibility(n);
                if (!this.mIndeterminate) break block2;
                if (n != 8 && n != 4) break block3;
                this.stopAnimation();
            }
            return;
        }
        this.startAnimation();
    }

    /*
     * Enabled aggressive block sorting
     */
    void startAnimation() {
        if (this.getVisibility() != 0) {
            return;
        }
        if (this.mIndeterminateDrawable instanceof Animatable) {
            this.mShouldStartAnimationDrawable = true;
            this.mAnimation = null;
        } else {
            if (this.mInterpolator == null) {
                this.mInterpolator = new LinearInterpolator();
            }
            this.mTransformation = new Transformation();
            this.mAnimation = new AlphaAnimation(0.0f, 1.0f);
            this.mAnimation.setRepeatMode(this.mBehavior);
            this.mAnimation.setRepeatCount(-1);
            this.mAnimation.setDuration((long)this.mDuration);
            this.mAnimation.setInterpolator(this.mInterpolator);
            this.mAnimation.setStartTime(-1L);
        }
        this.postInvalidate();
    }

    void stopAnimation() {
        this.mAnimation = null;
        this.mTransformation = null;
        if (this.mIndeterminateDrawable instanceof Animatable) {
            ((Animatable)this.mIndeterminateDrawable).stop();
            this.mShouldStartAnimationDrawable = false;
        }
        this.postInvalidate();
    }

    protected boolean verifyDrawable(Drawable drawable2) {
        if (drawable2 == this.mProgressDrawable || drawable2 == this.mIndeterminateDrawable || super.verifyDrawable(drawable2)) {
            return true;
        }
        return false;
    }

    private class RefreshProgressRunnable
    implements Runnable {
        private boolean mFromUser;
        private int mId;
        private int mProgress;

        RefreshProgressRunnable(int n, int n2, boolean bl) {
            this.mId = n;
            this.mProgress = n2;
            this.mFromUser = bl;
        }

        public void run() {
            ProgressBarICS.this.doRefreshProgress(this.mId, this.mProgress, this.mFromUser, true);
            ProgressBarICS.this.mRefreshProgressRunnable = this;
        }

        public void setup(int n, int n2, boolean bl) {
            this.mId = n;
            this.mProgress = n2;
            this.mFromUser = bl;
        }
    }

    static class SavedState
    extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        int progress;
        int secondaryProgress;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.progress = parcel.readInt();
            this.secondaryProgress = parcel.readInt();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.progress);
            parcel.writeInt(this.secondaryProgress);
        }

    }

}

