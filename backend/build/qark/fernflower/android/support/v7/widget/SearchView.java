package android.support.v7.widget;

import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.Build.VERSION;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$dimen;
import android.support.v7.appcompat.R$id;
import android.support.v7.appcompat.R$layout;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.view.CollapsibleActionView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.KeyEvent.DispatcherState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView.OnEditorActionListener;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

public class SearchView extends LinearLayout implements CollapsibleActionView {
   private static final boolean DBG = false;
   static final SearchView.AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new SearchView.AutoCompleteTextViewReflector();
   private static final String IME_OPTION_NO_MICROPHONE = "nm";
   private static final String LOG_TAG = "SearchView";
   private Bundle mAppSearchData;
   private boolean mClearingFocus;
   private ImageView mCloseButton;
   private int mCollapsedImeOptions;
   private View mDropDownAnchor;
   private boolean mExpandedInActionView;
   private boolean mIconified;
   private boolean mIconifiedByDefault;
   private int mMaxWidth;
   private CharSequence mOldQueryText;
   private final OnClickListener mOnClickListener;
   private SearchView.OnCloseListener mOnCloseListener;
   private final OnEditorActionListener mOnEditorActionListener;
   private final OnItemClickListener mOnItemClickListener;
   private final OnItemSelectedListener mOnItemSelectedListener;
   private SearchView.OnQueryTextListener mOnQueryChangeListener;
   private OnFocusChangeListener mOnQueryTextFocusChangeListener;
   private OnClickListener mOnSearchClickListener;
   private SearchView.OnSuggestionListener mOnSuggestionListener;
   private final WeakHashMap mOutsideDrawablesCache;
   private CharSequence mQueryHint;
   private boolean mQueryRefinement;
   private SearchView.SearchAutoComplete mQueryTextView;
   private Runnable mReleaseCursorRunnable;
   private View mSearchButton;
   private View mSearchEditFrame;
   private ImageView mSearchHintIcon;
   private View mSearchPlate;
   private SearchableInfo mSearchable;
   private Runnable mShowImeRunnable;
   private View mSubmitArea;
   private View mSubmitButton;
   private boolean mSubmitButtonEnabled;
   private CursorAdapter mSuggestionsAdapter;
   OnKeyListener mTextKeyListener;
   private TextWatcher mTextWatcher;
   private Runnable mUpdateDrawableStateRunnable;
   private CharSequence mUserQuery;
   private final Intent mVoiceAppSearchIntent;
   private View mVoiceButton;
   private boolean mVoiceButtonEnabled;
   private final Intent mVoiceWebSearchIntent;

   public SearchView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SearchView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mShowImeRunnable = new Runnable() {
         public void run() {
            InputMethodManager var1 = (InputMethodManager)SearchView.this.getContext().getSystemService("input_method");
            if (var1 != null) {
               SearchView.HIDDEN_METHOD_INVOKER.showSoftInputUnchecked(var1, SearchView.this, 0);
            }

         }
      };
      this.mUpdateDrawableStateRunnable = new Runnable() {
         public void run() {
            SearchView.this.updateFocusedState();
         }
      };
      this.mReleaseCursorRunnable = new Runnable() {
         public void run() {
            if (SearchView.this.mSuggestionsAdapter != null && SearchView.this.mSuggestionsAdapter instanceof SuggestionsAdapter) {
               SearchView.this.mSuggestionsAdapter.changeCursor((Cursor)null);
            }

         }
      };
      this.mOutsideDrawablesCache = new WeakHashMap();
      this.mOnClickListener = new OnClickListener() {
         public void onClick(View var1) {
            if (var1 == SearchView.this.mSearchButton) {
               SearchView.this.onSearchClicked();
            } else {
               if (var1 == SearchView.this.mCloseButton) {
                  SearchView.this.onCloseClicked();
                  return;
               }

               if (var1 == SearchView.this.mSubmitButton) {
                  SearchView.this.onSubmitQuery();
                  return;
               }

               if (var1 == SearchView.this.mVoiceButton) {
                  SearchView.this.onVoiceClicked();
                  return;
               }

               if (var1 == SearchView.this.mQueryTextView) {
                  SearchView.this.forceSuggestionQuery();
                  return;
               }
            }

         }
      };
      this.mTextKeyListener = new OnKeyListener() {
         public boolean onKey(View var1, int var2, KeyEvent var3) {
            if (SearchView.this.mSearchable != null) {
               if (SearchView.this.mQueryTextView.isPopupShowing() && SearchView.this.mQueryTextView.getListSelection() != -1) {
                  return SearchView.this.onSuggestionsKey(var1, var2, var3);
               }

               if (!SearchView.this.mQueryTextView.isEmpty() && KeyEventCompat.hasNoModifiers(var3) && var3.getAction() == 1 && var2 == 66) {
                  var1.cancelLongPress();
                  SearchView.this.launchQuerySearch(0, (String)null, SearchView.this.mQueryTextView.getText().toString());
                  return true;
               }
            }

            return false;
         }
      };
      this.mOnEditorActionListener = new OnEditorActionListener() {
         public boolean onEditorAction(TextView var1, int var2, KeyEvent var3) {
            SearchView.this.onSubmitQuery();
            return true;
         }
      };
      this.mOnItemClickListener = new OnItemClickListener() {
         public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
            SearchView.this.onItemClicked(var3, 0, (String)null);
         }
      };
      this.mOnItemSelectedListener = new OnItemSelectedListener() {
         public void onItemSelected(AdapterView var1, View var2, int var3, long var4) {
            SearchView.this.onItemSelected(var3);
         }

         public void onNothingSelected(AdapterView var1) {
         }
      };
      this.mTextWatcher = new TextWatcher() {
         public void afterTextChanged(Editable var1) {
         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            SearchView.this.onTextChanged(var1);
         }
      };
      ((LayoutInflater)var1.getSystemService("layout_inflater")).inflate(R$layout.abc_search_view, this, true);
      this.mSearchButton = this.findViewById(R$id.search_button);
      this.mQueryTextView = (SearchView.SearchAutoComplete)this.findViewById(R$id.search_src_text);
      this.mQueryTextView.setSearchView(this);
      this.mSearchEditFrame = this.findViewById(R$id.search_edit_frame);
      this.mSearchPlate = this.findViewById(R$id.search_plate);
      this.mSubmitArea = this.findViewById(R$id.submit_area);
      this.mSubmitButton = this.findViewById(R$id.search_go_btn);
      this.mCloseButton = (ImageView)this.findViewById(R$id.search_close_btn);
      this.mVoiceButton = this.findViewById(R$id.search_voice_btn);
      this.mSearchHintIcon = (ImageView)this.findViewById(R$id.search_mag_icon);
      this.mSearchButton.setOnClickListener(this.mOnClickListener);
      this.mCloseButton.setOnClickListener(this.mOnClickListener);
      this.mSubmitButton.setOnClickListener(this.mOnClickListener);
      this.mVoiceButton.setOnClickListener(this.mOnClickListener);
      this.mQueryTextView.setOnClickListener(this.mOnClickListener);
      this.mQueryTextView.addTextChangedListener(this.mTextWatcher);
      this.mQueryTextView.setOnEditorActionListener(this.mOnEditorActionListener);
      this.mQueryTextView.setOnItemClickListener(this.mOnItemClickListener);
      this.mQueryTextView.setOnItemSelectedListener(this.mOnItemSelectedListener);
      this.mQueryTextView.setOnKeyListener(this.mTextKeyListener);
      this.mQueryTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
         public void onFocusChange(View var1, boolean var2) {
            if (SearchView.this.mOnQueryTextFocusChangeListener != null) {
               SearchView.this.mOnQueryTextFocusChangeListener.onFocusChange(SearchView.this, var2);
            }

         }
      });
      TypedArray var5 = var1.obtainStyledAttributes(var2, R$styleable.SearchView, 0, 0);
      this.setIconifiedByDefault(var5.getBoolean(3, true));
      int var3 = var5.getDimensionPixelSize(0, -1);
      if (var3 != -1) {
         this.setMaxWidth(var3);
      }

      CharSequence var6 = var5.getText(4);
      if (!TextUtils.isEmpty(var6)) {
         this.setQueryHint(var6);
      }

      var3 = var5.getInt(2, -1);
      if (var3 != -1) {
         this.setImeOptions(var3);
      }

      var3 = var5.getInt(1, -1);
      if (var3 != -1) {
         this.setInputType(var3);
      }

      var5.recycle();
      TypedArray var7 = var1.obtainStyledAttributes(var2, R$styleable.View, 0, 0);
      boolean var4 = var7.getBoolean(0, true);
      var7.recycle();
      this.setFocusable(var4);
      this.mVoiceWebSearchIntent = new Intent("android.speech.action.WEB_SEARCH");
      this.mVoiceWebSearchIntent.addFlags(268435456);
      this.mVoiceWebSearchIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
      this.mVoiceAppSearchIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
      this.mVoiceAppSearchIntent.addFlags(268435456);
      this.mDropDownAnchor = this.findViewById(this.mQueryTextView.getDropDownAnchor());
      if (this.mDropDownAnchor != null) {
         if (VERSION.SDK_INT >= 11) {
            this.addOnLayoutChangeListenerToDropDownAnchorSDK11();
         } else {
            this.addOnLayoutChangeListenerToDropDownAnchorBase();
         }
      }

      this.updateViewsVisibility(this.mIconifiedByDefault);
      this.updateQueryHint();
   }

   private void addOnLayoutChangeListenerToDropDownAnchorBase() {
      this.mDropDownAnchor.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
         public void onGlobalLayout() {
            SearchView.this.adjustDropDownSizeAndPosition();
         }
      });
   }

   private void addOnLayoutChangeListenerToDropDownAnchorSDK11() {
      this.mDropDownAnchor.addOnLayoutChangeListener(new OnLayoutChangeListener() {
         public void onLayoutChange(View var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
            SearchView.this.adjustDropDownSizeAndPosition();
         }
      });
   }

   private void adjustDropDownSizeAndPosition() {
      if (this.mDropDownAnchor.getWidth() > 1) {
         Resources var6 = this.getContext().getResources();
         int var2 = this.mSearchPlate.getPaddingLeft();
         Rect var7 = new Rect();
         int var1;
         if (this.mIconifiedByDefault) {
            var1 = var6.getDimensionPixelSize(R$dimen.abc_dropdownitem_icon_width) + var6.getDimensionPixelSize(R$dimen.abc_dropdownitem_text_padding_left);
         } else {
            var1 = 0;
         }

         this.mQueryTextView.getDropDownBackground().getPadding(var7);
         int var3 = var7.left;
         this.mQueryTextView.setDropDownHorizontalOffset(var2 - (var3 + var1));
         var3 = this.mDropDownAnchor.getWidth();
         int var4 = var7.left;
         int var5 = var7.right;
         this.mQueryTextView.setDropDownWidth(var3 + var4 + var5 + var1 - var2);
      }

   }

   private Intent createIntent(String var1, Uri var2, String var3, String var4, int var5, String var6) {
      Intent var7 = new Intent(var1);
      var7.addFlags(268435456);
      if (var2 != null) {
         var7.setData(var2);
      }

      var7.putExtra("user_query", this.mUserQuery);
      if (var4 != null) {
         var7.putExtra("query", var4);
      }

      if (var3 != null) {
         var7.putExtra("intent_extra_data_key", var3);
      }

      if (this.mAppSearchData != null) {
         var7.putExtra("app_data", this.mAppSearchData);
      }

      if (var5 != 0) {
         var7.putExtra("action_key", var5);
         var7.putExtra("action_msg", var6);
      }

      var7.setComponent(this.mSearchable.getSearchActivity());
      return var7;
   }

   private Intent createIntentFromSuggestion(Cursor var1, int var2, String var3) {
      RuntimeException var10000;
      label83: {
         String var5;
         boolean var10001;
         try {
            var5 = SuggestionsAdapter.getColumnString(var1, "suggest_intent_action");
         } catch (RuntimeException var16) {
            var10000 = var16;
            var10001 = false;
            break label83;
         }

         String var4 = var5;
         if (var5 == null) {
            try {
               var4 = this.mSearchable.getSuggestIntentAction();
            } catch (RuntimeException var15) {
               var10000 = var15;
               var10001 = false;
               break label83;
            }
         }

         var5 = var4;
         if (var4 == null) {
            var5 = "android.intent.action.SEARCH";
         }

         String var6;
         try {
            var6 = SuggestionsAdapter.getColumnString(var1, "suggest_intent_data");
         } catch (RuntimeException var14) {
            var10000 = var14;
            var10001 = false;
            break label83;
         }

         var4 = var6;
         if (var6 == null) {
            try {
               var4 = this.mSearchable.getSuggestIntentData();
            } catch (RuntimeException var13) {
               var10000 = var13;
               var10001 = false;
               break label83;
            }
         }

         var6 = var4;
         if (var4 != null) {
            String var7;
            try {
               var7 = SuggestionsAdapter.getColumnString(var1, "suggest_intent_data_id");
            } catch (RuntimeException var12) {
               var10000 = var12;
               var10001 = false;
               break label83;
            }

            var6 = var4;
            if (var7 != null) {
               try {
                  var6 = var4 + "/" + Uri.encode(var7);
               } catch (RuntimeException var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label83;
               }
            }
         }

         Uri var18;
         if (var6 == null) {
            var18 = null;
         } else {
            try {
               var18 = Uri.parse(var6);
            } catch (RuntimeException var10) {
               var10000 = var10;
               var10001 = false;
               break label83;
            }
         }

         try {
            var6 = SuggestionsAdapter.getColumnString(var1, "suggest_intent_query");
            return this.createIntent(var5, var18, SuggestionsAdapter.getColumnString(var1, "suggest_intent_extra_data"), var6, var2, var3);
         } catch (RuntimeException var9) {
            var10000 = var9;
            var10001 = false;
         }
      }

      RuntimeException var17 = var10000;

      try {
         var2 = var1.getPosition();
      } catch (RuntimeException var8) {
         var2 = -1;
      }

      Log.w("SearchView", "Search suggestions cursor at row " + var2 + " returned exception.", var17);
      return null;
   }

   private Intent createVoiceAppSearchIntent(Intent var1, SearchableInfo var2) {
      ComponentName var9 = var2.getSearchActivity();
      Intent var4 = new Intent("android.intent.action.SEARCH");
      var4.setComponent(var9);
      PendingIntent var6 = PendingIntent.getActivity(this.getContext(), 0, var4, 1073741824);
      Bundle var7 = new Bundle();
      if (this.mAppSearchData != null) {
         var7.putParcelable("app_data", this.mAppSearchData);
      }

      Intent var8 = new Intent(var1);
      String var11 = "free_form";
      String var12 = null;
      String var5 = null;
      int var3 = 1;
      Resources var10 = this.getResources();
      if (var2.getVoiceLanguageModeId() != 0) {
         var11 = var10.getString(var2.getVoiceLanguageModeId());
      }

      if (var2.getVoicePromptTextId() != 0) {
         var12 = var10.getString(var2.getVoicePromptTextId());
      }

      if (var2.getVoiceLanguageId() != 0) {
         var5 = var10.getString(var2.getVoiceLanguageId());
      }

      if (var2.getVoiceMaxResults() != 0) {
         var3 = var2.getVoiceMaxResults();
      }

      var8.putExtra("android.speech.extra.LANGUAGE_MODEL", var11);
      var8.putExtra("android.speech.extra.PROMPT", var12);
      var8.putExtra("android.speech.extra.LANGUAGE", var5);
      var8.putExtra("android.speech.extra.MAX_RESULTS", var3);
      if (var9 == null) {
         var11 = null;
      } else {
         var11 = var9.flattenToShortString();
      }

      var8.putExtra("calling_package", var11);
      var8.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", var6);
      var8.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", var7);
      return var8;
   }

   private Intent createVoiceWebSearchIntent(Intent var1, SearchableInfo var2) {
      Intent var3 = new Intent(var1);
      ComponentName var4 = var2.getSearchActivity();
      String var5;
      if (var4 == null) {
         var5 = null;
      } else {
         var5 = var4.flattenToShortString();
      }

      var3.putExtra("calling_package", var5);
      return var3;
   }

   private void dismissSuggestions() {
      this.mQueryTextView.dismissDropDown();
   }

   private void forceSuggestionQuery() {
      HIDDEN_METHOD_INVOKER.doBeforeTextChanged(this.mQueryTextView);
      HIDDEN_METHOD_INVOKER.doAfterTextChanged(this.mQueryTextView);
   }

   private CharSequence getDecoratedHint(CharSequence var1) {
      if (!this.mIconifiedByDefault) {
         return var1;
      } else {
         SpannableStringBuilder var3 = new SpannableStringBuilder("   ");
         var3.append(var1);
         Drawable var4 = this.getContext().getResources().getDrawable(this.getSearchIconId());
         int var2 = (int)((double)this.mQueryTextView.getTextSize() * 1.25D);
         var4.setBounds(0, 0, var2, var2);
         var3.setSpan(new ImageSpan(var4), 1, 2, 33);
         return var3;
      }
   }

   private int getPreferredWidth() {
      return this.getContext().getResources().getDimensionPixelSize(R$dimen.abc_search_view_preferred_width);
   }

   private int getSearchIconId() {
      TypedValue var1 = new TypedValue();
      this.getContext().getTheme().resolveAttribute(R$attr.searchViewSearchIcon, var1, true);
      return var1.resourceId;
   }

   private boolean hasVoiceSearch() {
      boolean var2 = false;
      boolean var1 = var2;
      if (this.mSearchable != null) {
         var1 = var2;
         if (this.mSearchable.getVoiceSearchEnabled()) {
            Intent var3 = null;
            if (this.mSearchable.getVoiceSearchLaunchWebSearch()) {
               var3 = this.mVoiceWebSearchIntent;
            } else if (this.mSearchable.getVoiceSearchLaunchRecognizer()) {
               var3 = this.mVoiceAppSearchIntent;
            }

            var1 = var2;
            if (var3 != null) {
               var1 = var2;
               if (this.getContext().getPackageManager().resolveActivity(var3, 65536) != null) {
                  var1 = true;
               }
            }
         }
      }

      return var1;
   }

   static boolean isLandscapeMode(Context var0) {
      return var0.getResources().getConfiguration().orientation == 2;
   }

   private boolean isSubmitAreaEnabled() {
      return (this.mSubmitButtonEnabled || this.mVoiceButtonEnabled) && !this.isIconified();
   }

   private void launchIntent(Intent var1) {
      if (var1 != null) {
         try {
            this.getContext().startActivity(var1);
         } catch (RuntimeException var3) {
            Log.e("SearchView", "Failed launch activity: " + var1, var3);
         }
      }
   }

   private void launchQuerySearch(int var1, String var2, String var3) {
      Intent var4 = this.createIntent("android.intent.action.SEARCH", (Uri)null, (String)null, var3, var1, var2);
      this.getContext().startActivity(var4);
   }

   private boolean launchSuggestion(int var1, int var2, String var3) {
      Cursor var4 = this.mSuggestionsAdapter.getCursor();
      if (var4 != null && var4.moveToPosition(var1)) {
         this.launchIntent(this.createIntentFromSuggestion(var4, var2, var3));
         return true;
      } else {
         return false;
      }
   }

   private void onCloseClicked() {
      if (!TextUtils.isEmpty(this.mQueryTextView.getText())) {
         this.mQueryTextView.setText("");
         this.mQueryTextView.requestFocus();
         this.setImeVisibility(true);
      } else {
         if (this.mIconifiedByDefault && (this.mOnCloseListener == null || !this.mOnCloseListener.onClose())) {
            this.clearFocus();
            this.updateViewsVisibility(true);
         }

      }
   }

   private boolean onItemClicked(int var1, int var2, String var3) {
      boolean var4 = false;
      if (this.mOnSuggestionListener == null || !this.mOnSuggestionListener.onSuggestionClick(var1)) {
         this.launchSuggestion(var1, 0, (String)null);
         this.setImeVisibility(false);
         this.dismissSuggestions();
         var4 = true;
      }

      return var4;
   }

   private boolean onItemSelected(int var1) {
      if (this.mOnSuggestionListener != null && this.mOnSuggestionListener.onSuggestionSelect(var1)) {
         return false;
      } else {
         this.rewriteQueryFromSuggestion(var1);
         return true;
      }
   }

   private void onSearchClicked() {
      this.updateViewsVisibility(false);
      this.mQueryTextView.requestFocus();
      this.setImeVisibility(true);
      if (this.mOnSearchClickListener != null) {
         this.mOnSearchClickListener.onClick(this);
      }

   }

   private void onSubmitQuery() {
      Editable var1 = this.mQueryTextView.getText();
      if (var1 != null && TextUtils.getTrimmedLength(var1) > 0 && (this.mOnQueryChangeListener == null || !this.mOnQueryChangeListener.onQueryTextSubmit(var1.toString()))) {
         if (this.mSearchable != null) {
            this.launchQuerySearch(0, (String)null, var1.toString());
            this.setImeVisibility(false);
         }

         this.dismissSuggestions();
      }

   }

   private boolean onSuggestionsKey(View var1, int var2, KeyEvent var3) {
      if (this.mSearchable != null && this.mSuggestionsAdapter != null && var3.getAction() == 0 && KeyEventCompat.hasNoModifiers(var3)) {
         if (var2 != 66 && var2 != 84 && var2 != 61) {
            if (var2 != 21 && var2 != 22) {
               if (var2 == 19 && this.mQueryTextView.getListSelection() == 0) {
                  return false;
               } else {
                  return false;
               }
            } else {
               if (var2 == 21) {
                  var2 = 0;
               } else {
                  var2 = this.mQueryTextView.length();
               }

               this.mQueryTextView.setSelection(var2);
               this.mQueryTextView.setListSelection(0);
               this.mQueryTextView.clearListSelection();
               HIDDEN_METHOD_INVOKER.ensureImeVisible(this.mQueryTextView, true);
               return true;
            }
         } else {
            return this.onItemClicked(this.mQueryTextView.getListSelection(), 0, (String)null);
         }
      } else {
         return false;
      }
   }

   private void onTextChanged(CharSequence var1) {
      boolean var3 = true;
      Editable var4 = this.mQueryTextView.getText();
      this.mUserQuery = var4;
      boolean var2;
      if (!TextUtils.isEmpty(var4)) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.updateSubmitButton(var2);
      if (!var2) {
         var2 = var3;
      } else {
         var2 = false;
      }

      this.updateVoiceButton(var2);
      this.updateCloseButton();
      this.updateSubmitArea();
      if (this.mOnQueryChangeListener != null && !TextUtils.equals(var1, this.mOldQueryText)) {
         this.mOnQueryChangeListener.onQueryTextChange(var1.toString());
      }

      this.mOldQueryText = var1.toString();
   }

   private void onVoiceClicked() {
      if (this.mSearchable != null) {
         SearchableInfo var1 = this.mSearchable;

         try {
            Intent var3;
            if (var1.getVoiceSearchLaunchWebSearch()) {
               var3 = this.createVoiceWebSearchIntent(this.mVoiceWebSearchIntent, var1);
               this.getContext().startActivity(var3);
               return;
            }

            if (var1.getVoiceSearchLaunchRecognizer()) {
               var3 = this.createVoiceAppSearchIntent(this.mVoiceAppSearchIntent, var1);
               this.getContext().startActivity(var3);
               return;
            }
         } catch (ActivityNotFoundException var2) {
            Log.w("SearchView", "Could not find voice search activity");
            return;
         }
      }

   }

   private void postUpdateFocusedState() {
      this.post(this.mUpdateDrawableStateRunnable);
   }

   private void rewriteQueryFromSuggestion(int var1) {
      Editable var2 = this.mQueryTextView.getText();
      Cursor var3 = this.mSuggestionsAdapter.getCursor();
      if (var3 != null) {
         if (var3.moveToPosition(var1)) {
            CharSequence var4 = this.mSuggestionsAdapter.convertToString(var3);
            if (var4 != null) {
               this.setQuery(var4);
            } else {
               this.setQuery(var2);
            }
         } else {
            this.setQuery(var2);
         }
      }
   }

   private void setImeVisibility(boolean var1) {
      if (var1) {
         this.post(this.mShowImeRunnable);
      } else {
         this.removeCallbacks(this.mShowImeRunnable);
         InputMethodManager var2 = (InputMethodManager)this.getContext().getSystemService("input_method");
         if (var2 != null) {
            var2.hideSoftInputFromWindow(this.getWindowToken(), 0);
            return;
         }
      }

   }

   private void setQuery(CharSequence var1) {
      this.mQueryTextView.setText(var1);
      SearchView.SearchAutoComplete var3 = this.mQueryTextView;
      int var2;
      if (TextUtils.isEmpty(var1)) {
         var2 = 0;
      } else {
         var2 = var1.length();
      }

      var3.setSelection(var2);
   }

   private void updateCloseButton() {
      boolean var4 = true;
      byte var3 = 0;
      boolean var1;
      if (!TextUtils.isEmpty(this.mQueryTextView.getText())) {
         var1 = true;
      } else {
         var1 = false;
      }

      boolean var2 = var4;
      if (!var1) {
         if (this.mIconifiedByDefault && !this.mExpandedInActionView) {
            var2 = var4;
         } else {
            var2 = false;
         }
      }

      ImageView var5 = this.mCloseButton;
      byte var7;
      if (var2) {
         var7 = var3;
      } else {
         var7 = 8;
      }

      var5.setVisibility(var7);
      Drawable var6 = this.mCloseButton.getDrawable();
      int[] var8;
      if (var1) {
         var8 = ENABLED_STATE_SET;
      } else {
         var8 = EMPTY_STATE_SET;
      }

      var6.setState(var8);
   }

   private void updateFocusedState() {
      boolean var1 = this.mQueryTextView.hasFocus();
      Drawable var3 = this.mSearchPlate.getBackground();
      int[] var2;
      if (var1) {
         var2 = FOCUSED_STATE_SET;
      } else {
         var2 = EMPTY_STATE_SET;
      }

      var3.setState(var2);
      var3 = this.mSubmitArea.getBackground();
      if (var1) {
         var2 = FOCUSED_STATE_SET;
      } else {
         var2 = EMPTY_STATE_SET;
      }

      var3.setState(var2);
      this.invalidate();
   }

   private void updateQueryHint() {
      if (this.mQueryHint != null) {
         this.mQueryTextView.setHint(this.getDecoratedHint(this.mQueryHint));
      } else {
         if (this.mSearchable == null) {
            this.mQueryTextView.setHint(this.getDecoratedHint(""));
            return;
         }

         String var2 = null;
         int var1 = this.mSearchable.getHintId();
         if (var1 != 0) {
            var2 = this.getContext().getString(var1);
         }

         if (var2 != null) {
            this.mQueryTextView.setHint(this.getDecoratedHint(var2));
            return;
         }
      }

   }

   private void updateSearchAutoComplete() {
      byte var2 = 1;
      this.mQueryTextView.setThreshold(this.mSearchable.getSuggestThreshold());
      this.mQueryTextView.setImeOptions(this.mSearchable.getImeOptions());
      int var3 = this.mSearchable.getInputType();
      int var1 = var3;
      if ((var3 & 15) == 1) {
         var3 &= -65537;
         var1 = var3;
         if (this.mSearchable.getSuggestAuthority() != null) {
            var1 = var3 | 65536 | 524288;
         }
      }

      this.mQueryTextView.setInputType(var1);
      if (this.mSuggestionsAdapter != null) {
         this.mSuggestionsAdapter.changeCursor((Cursor)null);
      }

      if (this.mSearchable.getSuggestAuthority() != null) {
         this.mSuggestionsAdapter = new SuggestionsAdapter(this.getContext(), this, this.mSearchable, this.mOutsideDrawablesCache);
         this.mQueryTextView.setAdapter(this.mSuggestionsAdapter);
         SuggestionsAdapter var4 = (SuggestionsAdapter)this.mSuggestionsAdapter;
         byte var5 = var2;
         if (this.mQueryRefinement) {
            var5 = 2;
         }

         var4.setQueryRefinement(var5);
      }

   }

   private void updateSubmitArea() {
      byte var2 = 8;
      byte var1 = var2;
      if (this.isSubmitAreaEnabled()) {
         label12: {
            if (this.mSubmitButton.getVisibility() != 0) {
               var1 = var2;
               if (this.mVoiceButton.getVisibility() != 0) {
                  break label12;
               }
            }

            var1 = 0;
         }
      }

      this.mSubmitArea.setVisibility(var1);
   }

   private void updateSubmitButton(boolean var1) {
      byte var3 = 8;
      byte var2 = var3;
      if (this.mSubmitButtonEnabled) {
         var2 = var3;
         if (this.isSubmitAreaEnabled()) {
            var2 = var3;
            if (this.hasFocus()) {
               label14: {
                  if (!var1) {
                     var2 = var3;
                     if (this.mVoiceButtonEnabled) {
                        break label14;
                     }
                  }

                  var2 = 0;
               }
            }
         }
      }

      this.mSubmitButton.setVisibility(var2);
   }

   private void updateViewsVisibility(boolean var1) {
      boolean var5 = true;
      byte var3 = 8;
      this.mIconified = var1;
      byte var2;
      if (var1) {
         var2 = 0;
      } else {
         var2 = 8;
      }

      boolean var4;
      if (!TextUtils.isEmpty(this.mQueryTextView.getText())) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.mSearchButton.setVisibility(var2);
      this.updateSubmitButton(var4);
      View var6 = this.mSearchEditFrame;
      if (var1) {
         var2 = 8;
      } else {
         var2 = 0;
      }

      var6.setVisibility(var2);
      ImageView var7 = this.mSearchHintIcon;
      if (this.mIconifiedByDefault) {
         var2 = var3;
      } else {
         var2 = 0;
      }

      var7.setVisibility(var2);
      this.updateCloseButton();
      if (!var4) {
         var1 = var5;
      } else {
         var1 = false;
      }

      this.updateVoiceButton(var1);
      this.updateSubmitArea();
   }

   private void updateVoiceButton(boolean var1) {
      byte var3 = 8;
      byte var2 = var3;
      if (this.mVoiceButtonEnabled) {
         var2 = var3;
         if (!this.isIconified()) {
            var2 = var3;
            if (var1) {
               var2 = 0;
               this.mSubmitButton.setVisibility(8);
            }
         }
      }

      this.mVoiceButton.setVisibility(var2);
   }

   public void clearFocus() {
      this.mClearingFocus = true;
      this.setImeVisibility(false);
      super.clearFocus();
      this.mQueryTextView.clearFocus();
      this.mClearingFocus = false;
   }

   public int getImeOptions() {
      return this.mQueryTextView.getImeOptions();
   }

   public int getInputType() {
      return this.mQueryTextView.getInputType();
   }

   public int getMaxWidth() {
      return this.mMaxWidth;
   }

   public CharSequence getQuery() {
      return this.mQueryTextView.getText();
   }

   public CharSequence getQueryHint() {
      CharSequence var2;
      if (this.mQueryHint != null) {
         var2 = this.mQueryHint;
      } else {
         if (this.mSearchable == null) {
            return null;
         }

         var2 = null;
         int var1 = this.mSearchable.getHintId();
         if (var1 != 0) {
            return this.getContext().getString(var1);
         }
      }

      return var2;
   }

   public CursorAdapter getSuggestionsAdapter() {
      return this.mSuggestionsAdapter;
   }

   public boolean isIconfiedByDefault() {
      return this.mIconifiedByDefault;
   }

   public boolean isIconified() {
      return this.mIconified;
   }

   public boolean isQueryRefinementEnabled() {
      return this.mQueryRefinement;
   }

   public boolean isSubmitButtonEnabled() {
      return this.mSubmitButtonEnabled;
   }

   public void onActionViewCollapsed() {
      this.clearFocus();
      this.updateViewsVisibility(true);
      this.mQueryTextView.setImeOptions(this.mCollapsedImeOptions);
      this.mExpandedInActionView = false;
   }

   public void onActionViewExpanded() {
      if (!this.mExpandedInActionView) {
         this.mExpandedInActionView = true;
         this.mCollapsedImeOptions = this.mQueryTextView.getImeOptions();
         this.mQueryTextView.setImeOptions(this.mCollapsedImeOptions | 33554432);
         this.mQueryTextView.setText("");
         this.setIconified(false);
      }
   }

   protected void onDetachedFromWindow() {
      this.removeCallbacks(this.mUpdateDrawableStateRunnable);
      this.post(this.mReleaseCursorRunnable);
      super.onDetachedFromWindow();
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      return this.mSearchable == null ? false : super.onKeyDown(var1, var2);
   }

   protected void onMeasure(int var1, int var2) {
      if (this.isIconified()) {
         super.onMeasure(var1, var2);
      } else {
         int var4 = MeasureSpec.getMode(var1);
         int var3 = MeasureSpec.getSize(var1);
         switch(var4) {
         case Integer.MIN_VALUE:
            if (this.mMaxWidth > 0) {
               var1 = Math.min(this.mMaxWidth, var3);
            } else {
               var1 = Math.min(this.getPreferredWidth(), var3);
            }
            break;
         case 0:
            if (this.mMaxWidth > 0) {
               var1 = this.mMaxWidth;
            } else {
               var1 = this.getPreferredWidth();
            }
            break;
         case 1073741824:
            var1 = var3;
            if (this.mMaxWidth > 0) {
               var1 = Math.min(this.mMaxWidth, var3);
            }
            break;
         default:
            var1 = var3;
         }

         super.onMeasure(MeasureSpec.makeMeasureSpec(var1, 1073741824), var2);
      }
   }

   void onQueryRefine(CharSequence var1) {
      this.setQuery(var1);
   }

   void onTextFocusChanged() {
      this.updateViewsVisibility(this.isIconified());
      this.postUpdateFocusedState();
      if (this.mQueryTextView.hasFocus()) {
         this.forceSuggestionQuery();
      }

   }

   public void onWindowFocusChanged(boolean var1) {
      super.onWindowFocusChanged(var1);
      this.postUpdateFocusedState();
   }

   public boolean requestFocus(int var1, Rect var2) {
      boolean var3;
      if (this.mClearingFocus) {
         var3 = false;
      } else {
         if (!this.isFocusable()) {
            return false;
         }

         if (this.isIconified()) {
            return super.requestFocus(var1, var2);
         }

         boolean var4 = this.mQueryTextView.requestFocus(var1, var2);
         var3 = var4;
         if (var4) {
            this.updateViewsVisibility(false);
            return var4;
         }
      }

      return var3;
   }

   public void setAppSearchData(Bundle var1) {
      this.mAppSearchData = var1;
   }

   public void setIconified(boolean var1) {
      if (var1) {
         this.onCloseClicked();
      } else {
         this.onSearchClicked();
      }
   }

   public void setIconifiedByDefault(boolean var1) {
      if (this.mIconifiedByDefault != var1) {
         this.mIconifiedByDefault = var1;
         this.updateViewsVisibility(var1);
         this.updateQueryHint();
      }
   }

   public void setImeOptions(int var1) {
      this.mQueryTextView.setImeOptions(var1);
   }

   public void setInputType(int var1) {
      this.mQueryTextView.setInputType(var1);
   }

   public void setMaxWidth(int var1) {
      this.mMaxWidth = var1;
      this.requestLayout();
   }

   public void setOnCloseListener(SearchView.OnCloseListener var1) {
      this.mOnCloseListener = var1;
   }

   public void setOnQueryTextFocusChangeListener(OnFocusChangeListener var1) {
      this.mOnQueryTextFocusChangeListener = var1;
   }

   public void setOnQueryTextListener(SearchView.OnQueryTextListener var1) {
      this.mOnQueryChangeListener = var1;
   }

   public void setOnSearchClickListener(OnClickListener var1) {
      this.mOnSearchClickListener = var1;
   }

   public void setOnSuggestionListener(SearchView.OnSuggestionListener var1) {
      this.mOnSuggestionListener = var1;
   }

   public void setQuery(CharSequence var1, boolean var2) {
      this.mQueryTextView.setText(var1);
      if (var1 != null) {
         this.mQueryTextView.setSelection(this.mQueryTextView.length());
         this.mUserQuery = var1;
      }

      if (var2 && !TextUtils.isEmpty(var1)) {
         this.onSubmitQuery();
      }

   }

   public void setQueryHint(CharSequence var1) {
      this.mQueryHint = var1;
      this.updateQueryHint();
   }

   public void setQueryRefinementEnabled(boolean var1) {
      this.mQueryRefinement = var1;
      if (this.mSuggestionsAdapter instanceof SuggestionsAdapter) {
         SuggestionsAdapter var3 = (SuggestionsAdapter)this.mSuggestionsAdapter;
         byte var2;
         if (var1) {
            var2 = 2;
         } else {
            var2 = 1;
         }

         var3.setQueryRefinement(var2);
      }

   }

   public void setSearchableInfo(SearchableInfo var1) {
      this.mSearchable = var1;
      if (this.mSearchable != null) {
         this.updateSearchAutoComplete();
         this.updateQueryHint();
      }

      this.mVoiceButtonEnabled = this.hasVoiceSearch();
      if (this.mVoiceButtonEnabled) {
         this.mQueryTextView.setPrivateImeOptions("nm");
      }

      this.updateViewsVisibility(this.isIconified());
   }

   public void setSubmitButtonEnabled(boolean var1) {
      this.mSubmitButtonEnabled = var1;
      this.updateViewsVisibility(this.isIconified());
   }

   public void setSuggestionsAdapter(CursorAdapter var1) {
      this.mSuggestionsAdapter = var1;
      this.mQueryTextView.setAdapter(this.mSuggestionsAdapter);
   }

   private static class AutoCompleteTextViewReflector {
      private Method doAfterTextChanged;
      private Method doBeforeTextChanged;
      private Method ensureImeVisible;
      private Method showSoftInputUnchecked;

      AutoCompleteTextViewReflector() {
         try {
            this.doBeforeTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged");
            this.doBeforeTextChanged.setAccessible(true);
         } catch (NoSuchMethodException var5) {
         }

         try {
            this.doAfterTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged");
            this.doAfterTextChanged.setAccessible(true);
         } catch (NoSuchMethodException var4) {
         }

         try {
            this.ensureImeVisible = AutoCompleteTextView.class.getMethod("ensureImeVisible", Boolean.TYPE);
            this.ensureImeVisible.setAccessible(true);
         } catch (NoSuchMethodException var3) {
         }

         try {
            this.showSoftInputUnchecked = InputMethodManager.class.getMethod("showSoftInputUnchecked", Integer.TYPE, ResultReceiver.class);
            this.showSoftInputUnchecked.setAccessible(true);
         } catch (NoSuchMethodException var2) {
         }
      }

      void doAfterTextChanged(AutoCompleteTextView var1) {
         if (this.doAfterTextChanged != null) {
            try {
               this.doAfterTextChanged.invoke(var1);
            } catch (Exception var2) {
               return;
            }
         }

      }

      void doBeforeTextChanged(AutoCompleteTextView var1) {
         if (this.doBeforeTextChanged != null) {
            try {
               this.doBeforeTextChanged.invoke(var1);
            } catch (Exception var2) {
               return;
            }
         }

      }

      void ensureImeVisible(AutoCompleteTextView var1, boolean var2) {
         if (this.ensureImeVisible != null) {
            try {
               this.ensureImeVisible.invoke(var1, var2);
            } catch (Exception var3) {
               return;
            }
         }

      }

      void showSoftInputUnchecked(InputMethodManager var1, View var2, int var3) {
         if (this.showSoftInputUnchecked != null) {
            try {
               this.showSoftInputUnchecked.invoke(var1, var3, null);
               return;
            } catch (Exception var5) {
            }
         }

         var1.showSoftInput(var2, var3);
      }
   }

   public interface OnCloseListener {
      boolean onClose();
   }

   public interface OnQueryTextListener {
      boolean onQueryTextChange(String var1);

      boolean onQueryTextSubmit(String var1);
   }

   public interface OnSuggestionListener {
      boolean onSuggestionClick(int var1);

      boolean onSuggestionSelect(int var1);
   }

   public static class SearchAutoComplete extends AutoCompleteTextView {
      private SearchView mSearchView;
      private int mThreshold = this.getThreshold();

      public SearchAutoComplete(Context var1) {
         super(var1);
      }

      public SearchAutoComplete(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public SearchAutoComplete(Context var1, AttributeSet var2, int var3) {
         super(var1, var2, var3);
      }

      private boolean isEmpty() {
         return TextUtils.getTrimmedLength(this.getText()) == 0;
      }

      public boolean enoughToFilter() {
         return this.mThreshold <= 0 || super.enoughToFilter();
      }

      protected void onFocusChanged(boolean var1, int var2, Rect var3) {
         super.onFocusChanged(var1, var2, var3);
         this.mSearchView.onTextFocusChanged();
      }

      public boolean onKeyPreIme(int var1, KeyEvent var2) {
         if (var1 == 4) {
            DispatcherState var3;
            if (var2.getAction() == 0 && var2.getRepeatCount() == 0) {
               var3 = this.getKeyDispatcherState();
               if (var3 != null) {
                  var3.startTracking(var2, this);
               }

               return true;
            }

            if (var2.getAction() == 1) {
               var3 = this.getKeyDispatcherState();
               if (var3 != null) {
                  var3.handleUpEvent(var2);
               }

               if (var2.isTracking() && !var2.isCanceled()) {
                  this.mSearchView.clearFocus();
                  this.mSearchView.setImeVisibility(false);
                  return true;
               }
            }
         }

         return super.onKeyPreIme(var1, var2);
      }

      public void onWindowFocusChanged(boolean var1) {
         super.onWindowFocusChanged(var1);
         if (var1 && this.mSearchView.hasFocus() && this.getVisibility() == 0) {
            ((InputMethodManager)this.getContext().getSystemService("input_method")).showSoftInput(this, 0);
            if (SearchView.isLandscapeMode(this.getContext())) {
               SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true);
            }
         }

      }

      public void performCompletion() {
      }

      protected void replaceText(CharSequence var1) {
      }

      void setSearchView(SearchView var1) {
         this.mSearchView = var1;
      }

      public void setThreshold(int var1) {
         super.setThreshold(var1);
         this.mThreshold = var1;
      }
   }
}
