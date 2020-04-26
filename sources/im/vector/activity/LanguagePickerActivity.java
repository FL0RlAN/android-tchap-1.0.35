package im.vector.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter.FilterListener;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.appcompat.widget.SearchView.SearchAutoComplete;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.gouv.tchap.a.R;
import im.vector.VectorApp;
import im.vector.adapters.LanguagesAdapter;
import im.vector.adapters.LanguagesAdapter.OnSelectLocaleListener;
import im.vector.settings.VectorLocale;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.Picker;
import im.vector.ui.themes.ThemeUtils;
import java.util.Locale;

public class LanguagePickerActivity extends VectorAppCompatActivity implements OnSelectLocaleListener, OnQueryTextListener {
    private LanguagesAdapter mAdapter;
    /* access modifiers changed from: private */
    public View mLanguagesEmptyView;
    private SearchView mSearchView;

    public int getLayoutRes() {
        return R.layout.activity_langagues_picker;
    }

    public int getMenuRes() {
        return R.menu.menu_languages_picker;
    }

    public int getTitleRes() {
        return R.string.settings_select_language;
    }

    public boolean onQueryTextSubmit(String str) {
        return true;
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, LanguagePickerActivity.class);
    }

    public ActivityOtherThemes getOtherThemes() {
        return Picker.INSTANCE;
    }

    public void initUiAndData() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        initViews();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem findItem = menu.findItem(R.id.action_search);
        if (findItem != null) {
            SearchManager searchManager = (SearchManager) getSystemService("search");
            this.mSearchView = (SearchView) MenuItemCompat.getActionView(findItem);
            this.mSearchView.setMaxWidth(Integer.MAX_VALUE);
            this.mSearchView.setSubmitButtonEnabled(false);
            this.mSearchView.setQueryHint(getString(R.string.search_hint));
            this.mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            this.mSearchView.setOnQueryTextListener(this);
            ((SearchAutoComplete) this.mSearchView.findViewById(R.id.search_src_text)).setHintTextColor(ThemeUtils.INSTANCE.getColor(this, R.attr.vctr_default_text_hint_color));
        }
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        SearchView searchView = this.mSearchView;
        if (searchView != null) {
            searchView.setOnQueryTextListener(null);
        }
    }

    private void initViews() {
        this.mLanguagesEmptyView = findViewById(R.id.languages_empty_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.languages_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(1);
        recyclerView.setLayoutManager(linearLayoutManager);
        this.mAdapter = new LanguagesAdapter(VectorLocale.INSTANCE.getSupportedLocales(), this);
        recyclerView.setAdapter(this.mAdapter);
    }

    private void filterLocales(String str) {
        this.mAdapter.getFilter().filter(str, new FilterListener() {
            public void onFilterComplete(int i) {
                LanguagePickerActivity.this.mLanguagesEmptyView.setVisibility(i > 0 ? 8 : 0);
            }
        });
    }

    public void onSelectLocale(Locale locale) {
        VectorApp.updateApplicationLocale(locale);
        setResult(-1);
        finish();
    }

    public boolean onQueryTextChange(String str) {
        filterLocales(str);
        return true;
    }
}
