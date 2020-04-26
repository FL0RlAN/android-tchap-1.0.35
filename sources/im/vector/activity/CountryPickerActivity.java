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
import im.vector.adapters.CountryAdapter;
import im.vector.adapters.CountryAdapter.OnSelectCountryListener;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.Picker;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.CountryPhoneData;
import im.vector.util.PhoneNumberUtils;

public class CountryPickerActivity extends VectorAppCompatActivity implements OnSelectCountryListener, OnQueryTextListener {
    private static final String EXTRA_IN_WITH_INDICATOR = "EXTRA_IN_WITH_INDICATOR";
    private static final String EXTRA_OUT_CALLING_CODE = "EXTRA_OUT_CALLING_CODE";
    public static final String EXTRA_OUT_COUNTRY_CODE = "EXTRA_OUT_COUNTRY_CODE";
    public static final String EXTRA_OUT_COUNTRY_NAME = "EXTRA_OUT_COUNTRY_NAME";
    private CountryAdapter mCountryAdapter;
    /* access modifiers changed from: private */
    public View mCountryEmptyView;
    private RecyclerView mCountryRecyclerView;
    private SearchView mSearchView;
    private boolean mWithIndicator;

    public int getLayoutRes() {
        return R.layout.activity_country_picker;
    }

    public int getMenuRes() {
        return R.menu.menu_country_picker;
    }

    public int getTitleRes() {
        return R.string.settings_select_country;
    }

    public boolean onQueryTextSubmit(String str) {
        return true;
    }

    public static Intent getIntent(Context context, boolean z) {
        Intent intent = new Intent(context, CountryPickerActivity.class);
        intent.putExtra(EXTRA_IN_WITH_INDICATOR, z);
        return intent;
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
        this.mWithIndicator = getIntent().getBooleanExtra(EXTRA_IN_WITH_INDICATOR, false);
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
        this.mCountryEmptyView = findViewById(R.id.country_empty_view);
        this.mCountryRecyclerView = (RecyclerView) findViewById(R.id.country_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(1);
        this.mCountryRecyclerView.setLayoutManager(linearLayoutManager);
        this.mCountryAdapter = new CountryAdapter(PhoneNumberUtils.getCountriesWithIndicator(), this.mWithIndicator, this);
        this.mCountryRecyclerView.setAdapter(this.mCountryAdapter);
    }

    private void filterCountries(String str) {
        this.mCountryAdapter.getFilter().filter(str, new FilterListener() {
            public void onFilterComplete(int i) {
                CountryPickerActivity.this.mCountryEmptyView.setVisibility(i > 0 ? 8 : 0);
            }
        });
    }

    public void onSelectCountry(CountryPhoneData countryPhoneData) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_OUT_COUNTRY_NAME, countryPhoneData.getCountryName());
        intent.putExtra(EXTRA_OUT_COUNTRY_CODE, countryPhoneData.getCountryCode());
        intent.putExtra(EXTRA_OUT_CALLING_CODE, countryPhoneData.getCallingCode());
        setResult(-1, intent);
        finish();
    }

    public boolean onQueryTextChange(String str) {
        filterCountries(str);
        return true;
    }
}
