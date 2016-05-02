package com.celt.lms;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.celt.lms.adapter.*;
import com.celt.lms.api.ApiFactory;
import com.celt.lms.api.ApiLms;
import com.celt.lms.dto.GroupDTO;
import com.celt.lms.dto.ParsingJsonLms;
import com.celt.lms.fragments.AbsFragment;
import com.celt.lms.fragments.FragmentFirstTab;
import com.celt.lms.fragments.FragmentSecondTab;
import com.google.gson.JsonElement;
import retrofit2.Call;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements onEventListener {

    private static final int LAYOUT = R.layout.activity_main;
    private static ArrayList<TabsPagerFragmentAdapter> adapterList;
    private ApiLms api;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private List<GroupDTO> groupDTOList;
    private TabLayout tabLayout;
    private Spinner spinner;
    private Spinner spinner2;


    public static void setFragment(FragmentSecondTab fragment) {
        adapterList.get(1).getTabs().append(fragment.getKey(), fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        initToolbar();
        initNavigationView();
        setVisibilitySpinners(View.GONE);

        adapterList = new ArrayList<TabsPagerFragmentAdapter>();
        adapterList.add(new TabsPagerFragmentAdapter(this, getSupportFragmentManager(), getTabs()));
        adapterList.add(new TabsPagerFragmentAdapter(this, getSupportFragmentManager(), getTabs2()));

        api = ApiFactory.getService();

        if (savedInstanceState == null) {
            viewPager.setAdapter(adapterList.get(0));
            tabLayout.setupWithViewPager(viewPager);
            new getGroupsAsyncTask().execute("https://collapsed.space/ServicesCoreService.svcGetGroups2025.json");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setAdapter(adapterList.get(savedInstanceState.getInt("count")));
        tabLayout.setupWithViewPager(viewPager);
        setOnTabSelectedListener();
        if (savedInstanceState.getInt("count") == 1) {
            setVisibilitySpinners(View.VISIBLE);
            if (tabLayout.getSelectedTabPosition() == 1)
                spinner2.setVisibility(View.GONE);
        } else {
            setVisibilitySpinners(View.GONE);
        }

        groupDTOList = (List<GroupDTO>) getLastCustomNonConfigurationInstance();
        setDataGroups(groupDTOList);
    }

    private SparseArrayCompat<AbsFragment> getTabs() {
        SparseArrayCompat<AbsFragment> tabs = new SparseArrayCompat<AbsFragment>();
        tabs.put(0, new FragmentFirstTab(this, "News", R.layout.fragment, new NewsListAdapter(), "https://collapsed.space/ServicesNewsNewsService.svcGetNews2025.json"));
        tabs.put(1, new FragmentFirstTab(this, "Lectures", R.layout.fragment, new LecturesListAdapter(), "https://collapsed.space/ServicesLecturesLecturesService.svcGetLectures2025.json"));
        tabs.put(2, new FragmentFirstTab(this, "Labs", R.layout.fragment, new LabsListAdapter(), "https://collapsed.space/ServicesLabsLabsService.svcGetLabs2025.json"));
        return tabs;
    }

    private SparseArrayCompat<AbsFragment> getTabs2() {
        SparseArrayCompat<AbsFragment> tabs = new SparseArrayCompat<AbsFragment>();
        tabs.put(0, new FragmentSecondTab(this, 0, "Labs", R.layout.fragment, new LabsScheduleListAdapter(), "https://collapsed.space/ServicesCoreService.svcGetGroups2025.json"));
        tabs.put(1, new FragmentSecondTab(this, 1, "Visiting", R.layout.fragment, new VisitingListAdapter(), "https://collapsed.space/ServicesCoreService.svcGetGroups2025.json"));
        tabs.put(2, new FragmentSecondTab(this, 2, "LabMarks", R.layout.fragment, new LabMarksListAdapter(), "https://collapsed.space/ServicesCoreService.svcGetGroups2025.json"));
        return tabs;
    }

    private void setVisibilitySpinners(int visibility) {
        if (visibility == View.VISIBLE && (spinner.getCount() == 0 || spinner2.getCount() == 0))
            return;
        spinner.setVisibility(visibility);
        spinner2.setVisibility(visibility);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        spinner = (Spinner) findViewById(R.id.spinner_nav);
        spinner2 = (Spinner) findViewById(R.id.spinner_nav2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<GroupDTO> data = groupDTOList;
                if (data != null) {
                    ((FragmentSecondTab) adapterList.get(1).getTabs().get(0)).setAdapter(data.get(spinner.getSelectedItemPosition()).getSubGroup(spinner2.getSelectedItemPosition()));
                    ((FragmentSecondTab) adapterList.get(1).getTabs().get(1)).setAdapter(data.get(spinner.getSelectedItemPosition()));
                    ((FragmentSecondTab) adapterList.get(1).getTabs().get(2)).setAdapter(data.get(spinner.getSelectedItemPosition()).getSubGroup(spinner2.getSelectedItemPosition()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<GroupDTO> data = groupDTOList;
                ((FragmentSecondTab) adapterList.get(1).getTabs().get(0)).setAdapter(data.get(spinner.getSelectedItemPosition()).getSubGroup(spinner2.getSelectedItemPosition()));
                ((FragmentSecondTab) adapterList.get(1).getTabs().get(2)).setAdapter(data.get(spinner.getSelectedItemPosition()).getSubGroup(spinner2.getSelectedItemPosition()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapterList.get(0).equals(viewPager.getAdapter()))
            outState.putInt("count", 0);
        else
            outState.putInt("count", 1);
    }

    private void setDataGroups(List<GroupDTO> data) {
        FragmentSecondTab f = (FragmentSecondTab) adapterList.get(1).getTabs().get(0);
        FragmentSecondTab f2 = (FragmentSecondTab) adapterList.get(1).getTabs().get(1);
        FragmentSecondTab f3 = (FragmentSecondTab) adapterList.get(1).getTabs().get(2);

        if (data != null && data.size() != 0) {

            groupDTOList = data;

            if (spinner.getAdapter() == null) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item);
                arrayAdapter.setDropDownViewResource(R.layout.spinner_item2);
                for (GroupDTO item : data) {
                    arrayAdapter.add(item.getGroupName());
                }
                spinner.setAdapter(arrayAdapter);

                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item);
                arrayAdapter2.setDropDownViewResource(R.layout.spinner_item2);
                arrayAdapter2.add("Подгруппа 1");
                arrayAdapter2.add("Подгруппа 2");
                spinner2.setAdapter(arrayAdapter2);

                if (viewPager.getAdapter() == adapterList.get(1)) {
                    spinner.setVisibility(View.VISIBLE);
                    if (tabLayout.getSelectedTabPosition() != 1)
                        spinner2.setVisibility(View.VISIBLE);
                }
            }

            f.setAdapter((data.get(spinner.getSelectedItemPosition()).getSubGroup(spinner2.getSelectedItemPosition())));
            f2.setAdapter(data.get(spinner.getSelectedItemPosition()));
            f3.setAdapter((data.get(spinner.getSelectedItemPosition()).getSubGroup(spinner2.getSelectedItemPosition())));

            Toast.makeText(getApplicationContext(), "Succeeded!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
        }
        f.setRefreshing(false);
        f2.setRefreshing(false);
        f3.setRefreshing(false);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return groupDTOList;
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getOrder()) {
                    case 0:
                        viewPager.setAdapter(adapterList.get(0));
                        setVisibilitySpinners(View.GONE);
                        break;
                    case 1:
                        viewPager.setAdapter(adapterList.get(1));
                        setVisibilitySpinners(View.VISIBLE);
                        break;
                }
                tabLayout.setupWithViewPager(viewPager);
                setOnTabSelectedListener();
                return true;
            }
        });
    }

    private void setOnTabSelectedListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText() == "Visiting")
                    spinner2.setVisibility(View.GONE);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getText() == "Visiting" && spinner2.getCount() != 0)
                    spinner2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void updateGroupList(Object s) {
        new getGroupsAsyncTask().execute(String.valueOf(s));
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private class getGroupsAsyncTask extends AsyncTask<String, Void, List<GroupDTO>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkConnected()) {
                this.cancel(true);
                Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                return;
            }
            ((FragmentSecondTab) adapterList.get(1).getTabs().get(0)).setRefreshing(true);
            ((FragmentSecondTab) adapterList.get(1).getTabs().get(1)).setRefreshing(true);
            ((FragmentSecondTab) adapterList.get(1).getTabs().get(2)).setRefreshing(true);
        }

        @Override
        protected ArrayList<GroupDTO> doInBackground(String... params) {
            try {
                if (!isCancelled()) {
                    Call<JsonElement> call = api.getGroups(2025);
                    JsonElement json = call.execute().body();
                    if (json != null)
                        return ParsingJsonLms.getParseGroup(json.toString());
                }
            } catch (IOException ignored) {
            }
            return new ArrayList<GroupDTO>();
        }

        @Override
        protected void onPostExecute(List<GroupDTO> data) {
            setDataGroups(data);
        }
    }
}
