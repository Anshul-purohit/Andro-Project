package android.support.v4.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.TextView;

public class PagerTitleStrip
  extends ViewGroup
  implements ViewPager.Decor
{
  private static final int[] ATTRS = { 16842804, 16842901, 16842904, 16842927 };
  private static final PagerTitleStripImpl IMPL;
  private static final float SIDE_ALPHA = 0.6F;
  private static final String TAG = "PagerTitleStrip";
  private static final int[] TEXT_ATTRS = { 16843660 };
  private static final int TEXT_SPACING = 16;
  TextView mCurrText;
  private int mGravity;
  private int mLastKnownCurrentPage = -1;
  private float mLastKnownPositionOffset = -1.0F;
  TextView mNextText;
  private int mNonPrimaryAlpha;
  private final PageListener mPageListener = new PageListener(null);
  ViewPager mPager;
  TextView mPrevText;
  private int mScaledTextSpacing;
  int mTextColor;
  private boolean mUpdatingPositions;
  private boolean mUpdatingText;
  
  static
  {
    if (Build.VERSION.SDK_INT >= 14) {}
    for (IMPL = new PagerTitleStripImplIcs();; IMPL = new PagerTitleStripImplBase()) {
      return;
    }
  }
  
  public PagerTitleStrip(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public PagerTitleStrip(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    TextView localTextView = new TextView(paramContext);
    mPrevText = localTextView;
    addView(localTextView);
    localTextView = new TextView(paramContext);
    mCurrText = localTextView;
    addView(localTextView);
    localTextView = new TextView(paramContext);
    mNextText = localTextView;
    addView(localTextView);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, ATTRS);
    int i = paramAttributeSet.getResourceId(0, 0);
    if (i != 0)
    {
      mPrevText.setTextAppearance(paramContext, i);
      mCurrText.setTextAppearance(paramContext, i);
      mNextText.setTextAppearance(paramContext, i);
    }
    int j = paramAttributeSet.getDimensionPixelSize(1, 0);
    if (j != 0) {
      setTextSize(0, j);
    }
    if (paramAttributeSet.hasValue(2))
    {
      j = paramAttributeSet.getColor(2, 0);
      mPrevText.setTextColor(j);
      mCurrText.setTextColor(j);
      mNextText.setTextColor(j);
    }
    mGravity = paramAttributeSet.getInteger(3, 80);
    paramAttributeSet.recycle();
    mTextColor = mCurrText.getTextColors().getDefaultColor();
    setNonPrimaryAlpha(0.6F);
    mPrevText.setEllipsize(TextUtils.TruncateAt.END);
    mCurrText.setEllipsize(TextUtils.TruncateAt.END);
    mNextText.setEllipsize(TextUtils.TruncateAt.END);
    boolean bool = false;
    if (i != 0)
    {
      paramAttributeSet = paramContext.obtainStyledAttributes(i, TEXT_ATTRS);
      bool = paramAttributeSet.getBoolean(0, false);
      paramAttributeSet.recycle();
    }
    if (bool)
    {
      setSingleLineAllCaps(mPrevText);
      setSingleLineAllCaps(mCurrText);
      setSingleLineAllCaps(mNextText);
    }
    for (;;)
    {
      mScaledTextSpacing = ((int)(16.0F * getResourcesgetDisplayMetricsdensity));
      return;
      mPrevText.setSingleLine();
      mCurrText.setSingleLine();
      mNextText.setSingleLine();
    }
  }
  
  private static void setSingleLineAllCaps(TextView paramTextView)
  {
    IMPL.setSingleLineAllCaps(paramTextView);
  }
  
  int getMinHeight()
  {
    int i = 0;
    Drawable localDrawable = getBackground();
    if (localDrawable != null) {
      i = localDrawable.getIntrinsicHeight();
    }
    return i;
  }
  
  public int getTextSpacing()
  {
    return mScaledTextSpacing;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    Object localObject = getParent();
    if (!(localObject instanceof ViewPager)) {
      throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
    }
    ViewPager localViewPager = (ViewPager)localObject;
    localObject = localViewPager.getAdapter();
    localViewPager.setInternalPageChangeListener(mPageListener);
    localViewPager.setOnAdapterChangeListener(mPageListener);
    mPager = localViewPager;
    updateAdapter(null, (PagerAdapter)localObject);
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (mPager != null)
    {
      updateAdapter(mPager.getAdapter(), null);
      mPager.setInternalPageChangeListener(null);
      mPager.setOnAdapterChangeListener(null);
      mPager = null;
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    float f = 0.0F;
    if (mPager != null)
    {
      if (mLastKnownPositionOffset >= 0.0F) {
        f = mLastKnownPositionOffset;
      }
      updateTextPositions(mPager.getCurrentItem(), f, true);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    if (i != 1073741824) {
      throw new IllegalStateException("Must measure with an exact width");
    }
    int k = getMinHeight();
    int m = getPaddingTop() + getPaddingBottom();
    int n = View.MeasureSpec.makeMeasureSpec((int)(paramInt1 * 0.8F), Integer.MIN_VALUE);
    i = View.MeasureSpec.makeMeasureSpec(paramInt2 - m, Integer.MIN_VALUE);
    mPrevText.measure(n, i);
    mCurrText.measure(n, i);
    mNextText.measure(n, i);
    if (j == 1073741824) {
      setMeasuredDimension(paramInt1, paramInt2);
    }
    for (;;)
    {
      return;
      setMeasuredDimension(paramInt1, Math.max(k, mCurrText.getMeasuredHeight() + m));
    }
  }
  
  public void requestLayout()
  {
    if (!mUpdatingText) {
      super.requestLayout();
    }
  }
  
  public void setGravity(int paramInt)
  {
    mGravity = paramInt;
    requestLayout();
  }
  
  public void setNonPrimaryAlpha(float paramFloat)
  {
    mNonPrimaryAlpha = ((int)(255.0F * paramFloat) & 0xFF);
    int i = mNonPrimaryAlpha << 24 | mTextColor & 0xFFFFFF;
    mPrevText.setTextColor(i);
    mNextText.setTextColor(i);
  }
  
  public void setTextColor(int paramInt)
  {
    mTextColor = paramInt;
    mCurrText.setTextColor(paramInt);
    paramInt = mNonPrimaryAlpha << 24 | mTextColor & 0xFFFFFF;
    mPrevText.setTextColor(paramInt);
    mNextText.setTextColor(paramInt);
  }
  
  public void setTextSize(int paramInt, float paramFloat)
  {
    mPrevText.setTextSize(paramInt, paramFloat);
    mCurrText.setTextSize(paramInt, paramFloat);
    mNextText.setTextSize(paramInt, paramFloat);
  }
  
  public void setTextSpacing(int paramInt)
  {
    mScaledTextSpacing = paramInt;
    requestLayout();
  }
  
  void updateAdapter(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2)
  {
    if (paramPagerAdapter1 != null) {
      paramPagerAdapter1.unregisterDataSetObserver(mPageListener);
    }
    if (paramPagerAdapter2 != null) {
      paramPagerAdapter2.registerDataSetObserver(mPageListener);
    }
    if (mPager != null)
    {
      mLastKnownCurrentPage = -1;
      mLastKnownPositionOffset = -1.0F;
      updateText(mPager.getCurrentItem(), paramPagerAdapter2);
      requestLayout();
    }
  }
  
  void updateText(int paramInt, PagerAdapter paramPagerAdapter)
  {
    int i;
    TextView localTextView;
    if (paramPagerAdapter != null)
    {
      i = paramPagerAdapter.getCount();
      mUpdatingText = true;
      localTextView = null;
      localObject = localTextView;
      if (paramInt >= 1)
      {
        localObject = localTextView;
        if (paramPagerAdapter != null) {
          localObject = paramPagerAdapter.getPageTitle(paramInt - 1);
        }
      }
      mPrevText.setText((CharSequence)localObject);
      localTextView = mCurrText;
      if ((paramPagerAdapter == null) || (paramInt >= i)) {
        break label255;
      }
    }
    label255:
    for (Object localObject = paramPagerAdapter.getPageTitle(paramInt);; localObject = null)
    {
      localTextView.setText((CharSequence)localObject);
      localTextView = null;
      localObject = localTextView;
      if (paramInt + 1 < i)
      {
        localObject = localTextView;
        if (paramPagerAdapter != null) {
          localObject = paramPagerAdapter.getPageTitle(paramInt + 1);
        }
      }
      mNextText.setText((CharSequence)localObject);
      int j = getWidth();
      int k = getPaddingLeft();
      int m = getPaddingRight();
      int n = getHeight();
      i = getPaddingTop();
      int i1 = getPaddingBottom();
      k = View.MeasureSpec.makeMeasureSpec((int)((j - k - m) * 0.8F), Integer.MIN_VALUE);
      i = View.MeasureSpec.makeMeasureSpec(n - i - i1, Integer.MIN_VALUE);
      mPrevText.measure(k, i);
      mCurrText.measure(k, i);
      mNextText.measure(k, i);
      mLastKnownCurrentPage = paramInt;
      if (!mUpdatingPositions) {
        updateTextPositions(paramInt, mLastKnownPositionOffset, false);
      }
      mUpdatingText = false;
      return;
      i = 0;
      break;
    }
  }
  
  void updateTextPositions(int paramInt, float paramFloat, boolean paramBoolean)
  {
    int i;
    int j;
    int k;
    int m;
    int n;
    int i1;
    int i2;
    int i3;
    int i4;
    int i5;
    int i6;
    int i7;
    int i8;
    if (paramInt != mLastKnownCurrentPage)
    {
      updateText(paramInt, mPager.getAdapter());
      mUpdatingPositions = true;
      i = mPrevText.getMeasuredWidth();
      j = mCurrText.getMeasuredWidth();
      k = mNextText.getMeasuredWidth();
      m = j / 2;
      n = getWidth();
      i1 = getHeight();
      i2 = getPaddingLeft();
      i3 = getPaddingRight();
      paramInt = getPaddingTop();
      i4 = getPaddingBottom();
      i5 = i3 + m;
      float f1 = paramFloat + 0.5F;
      float f2 = f1;
      if (f1 > 1.0F) {
        f2 = f1 - 1.0F;
      }
      m = n - i5 - (int)((n - (i2 + m) - i5) * f2) - j / 2;
      j = m + j;
      i6 = mPrevText.getBaseline();
      i7 = mCurrText.getBaseline();
      i5 = mNextText.getBaseline();
      i8 = Math.max(Math.max(i6, i7), i5);
      i6 = i8 - i6;
      i7 = i8 - i7;
      i5 = i8 - i5;
      int i9 = mPrevText.getMeasuredHeight();
      i8 = mCurrText.getMeasuredHeight();
      int i10 = mNextText.getMeasuredHeight();
      i8 = Math.max(Math.max(i6 + i9, i7 + i8), i5 + i10);
      switch (mGravity & 0x70)
      {
      default: 
        i1 = paramInt + i6;
        i4 = paramInt + i7;
        paramInt += i5;
      }
    }
    for (;;)
    {
      mCurrText.layout(m, i4, j, mCurrText.getMeasuredHeight() + i4);
      i4 = Math.min(i2, m - mScaledTextSpacing - i);
      mPrevText.layout(i4, i1, i4 + i, mPrevText.getMeasuredHeight() + i1);
      i4 = Math.max(n - i3 - k, mScaledTextSpacing + j);
      mNextText.layout(i4, paramInt, i4 + k, mNextText.getMeasuredHeight() + paramInt);
      mLastKnownPositionOffset = paramFloat;
      mUpdatingPositions = false;
      for (;;)
      {
        return;
        if ((paramBoolean) || (paramFloat != mLastKnownPositionOffset)) {
          break;
        }
      }
      paramInt = (i1 - paramInt - i4 - i8) / 2;
      i1 = paramInt + i6;
      i4 = paramInt + i7;
      paramInt += i5;
      continue;
      paramInt = i1 - i4 - i8;
      i1 = paramInt + i6;
      i4 = paramInt + i7;
      paramInt += i5;
    }
  }
  
  private class PageListener
    extends DataSetObserver
    implements ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener
  {
    private int mScrollState;
    
    private PageListener() {}
    
    public void onAdapterChanged(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2)
    {
      updateAdapter(paramPagerAdapter1, paramPagerAdapter2);
    }
    
    public void onChanged()
    {
      updateText(mPager.getCurrentItem(), mPager.getAdapter());
    }
    
    public void onPageScrollStateChanged(int paramInt)
    {
      mScrollState = paramInt;
    }
    
    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
      paramInt2 = paramInt1;
      if (paramFloat > 0.5F) {
        paramInt2 = paramInt1 + 1;
      }
      updateTextPositions(paramInt2, paramFloat, false);
    }
    
    public void onPageSelected(int paramInt)
    {
      if (mScrollState == 0) {
        updateText(mPager.getCurrentItem(), mPager.getAdapter());
      }
    }
  }
  
  static abstract interface PagerTitleStripImpl
  {
    public abstract void setSingleLineAllCaps(TextView paramTextView);
  }
  
  static class PagerTitleStripImplBase
    implements PagerTitleStrip.PagerTitleStripImpl
  {
    public void setSingleLineAllCaps(TextView paramTextView)
    {
      paramTextView.setSingleLine();
    }
  }
  
  static class PagerTitleStripImplIcs
    implements PagerTitleStrip.PagerTitleStripImpl
  {
    public void setSingleLineAllCaps(TextView paramTextView)
    {
      PagerTitleStripIcs.setSingleLineAllCaps(paramTextView);
    }
  }
}

/* Location:
 * Qualified Name:     android.support.v4.view.PagerTitleStrip
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */