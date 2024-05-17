package android.support.v4.widget;

import android.content.Context;
import android.view.View;

class SearchViewCompat$SearchViewCompatHoneycombImpl
  extends SearchViewCompat.SearchViewCompatStubImpl
{
  public Object newOnQueryTextListener(final SearchViewCompat.OnQueryTextListenerCompat paramOnQueryTextListenerCompat)
  {
    SearchViewCompatHoneycomb.newOnQueryTextListener(new SearchViewCompatHoneycomb.OnQueryTextListenerCompatBridge()
    {
      public boolean onQueryTextChange(String paramAnonymousString)
      {
        return paramOnQueryTextListenerCompat.onQueryTextChange(paramAnonymousString);
      }
      
      public boolean onQueryTextSubmit(String paramAnonymousString)
      {
        return paramOnQueryTextListenerCompat.onQueryTextSubmit(paramAnonymousString);
      }
    });
  }
  
  public View newSearchView(Context paramContext)
  {
    return SearchViewCompatHoneycomb.newSearchView(paramContext);
  }
  
  public void setOnQueryTextListener(Object paramObject1, Object paramObject2)
  {
    SearchViewCompatHoneycomb.setOnQueryTextListener(paramObject1, paramObject2);
  }
}

/* Location:
 * Qualified Name:     android.support.v4.widget.SearchViewCompat.SearchViewCompatHoneycombImpl
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */