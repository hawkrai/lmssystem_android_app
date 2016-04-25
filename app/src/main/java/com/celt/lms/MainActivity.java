package com.celt.lms;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.celt.lms.adapter.*;
import com.celt.lms.dto.GroupDTO;
import com.celt.lms.dto.ParsingJsonLms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements onEventListener {

    private static final int LAYOUT = R.layout.activity_main;
    private static ArrayList<TabsPagerFragmentAdapter> adapterList;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private List<GroupDTO> groupDTOList;
    private TabLayout tabLayout;
    private Spinner spinner;
    private Spinner spinner2;

    static String downloadJson(String param) {
        try {
            URL url = new URL(param);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                return stringBuilder.toString();
            } catch (Exception e) {
                return null;
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    static void setFragment(FragmentSecondTab fragment) {
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

        if (savedInstanceState == null) {
            viewPager.setAdapter(adapterList.get(0));
            tabLayout.setupWithViewPager(viewPager);
            new getGroupsAsyncTask().execute("https://collapsed.space/ServicesCoreService.svcGetGroups2025.json");
        }
    }

    private SparseArrayCompat<AbsFragment> getTabs() {
        SparseArrayCompat<AbsFragment> tabs = new SparseArrayCompat<AbsFragment>();
        tabs.put(0, new AbstractFragment(this, "News", R.layout.fragment, new NewsListAdapter(), "https://collapsed.space/ServicesNewsNewsService.svcGetNews2025.json"));
        tabs.put(1, new AbstractFragment(this, "Lectures", R.layout.fragment, new LecturesListAdapter(), "https://collapsed.space/ServicesLecturesLecturesService.svcGetLectures2025.json"));
        tabs.put(2, new AbstractFragment(this, "Labs", R.layout.fragment, new LabsListAdapter(), "https://collapsed.space/ServicesLabsLabsService.svcGetLabs2025.json"));
        return tabs;
    }

    private SparseArrayCompat<AbsFragment> getTabs2() {
        SparseArrayCompat<AbsFragment> tabs = new SparseArrayCompat<AbsFragment>();
        tabs.put(0, new FragmentSecondTab(this, 0, "Labs", R.layout.fragment, new LabsScheduleListAdapter(), "https://collapsed.space/ServicesCoreService.svcGetGroups2025.json"));
        tabs.put(1, new FragmentSecondTab(this, 1, "LecturesVisiting", R.layout.fragment, new LecturesVisitingListAdapter(), "https://collapsed.space/ServicesCoreService.svcGetGroups2025.json"));
        return tabs;
    }

    private void setVisibilitySpinners(int visibility) {
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
                    ((FragmentSecondTab) adapterList.get(1).getTabs().get(1)).setAdapter(data.get(spinner.getSelectedItemPosition()).getLecturesMarkVisiting());
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
        } else
            setVisibilitySpinners(View.GONE);

        groupDTOList = (List<GroupDTO>) getLastCustomNonConfigurationInstance();
        setDataGroups(groupDTOList);
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
                if (tab.getText().equals("LecturesVisiting"))
                    spinner2.setVisibility(View.GONE);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getText().equals("LecturesVisiting"))
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

    private void setDataGroups(List<GroupDTO> data) {
        if (data != null) {
            FragmentSecondTab f = (FragmentSecondTab) adapterList.get(1).getTabs().get(0);
            FragmentSecondTab f2 = (FragmentSecondTab) adapterList.get(1).getTabs().get(1);

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
            }

            f.setAdapter((data.get(spinner.getSelectedItemPosition()).getSubGroup(spinner2.getSelectedItemPosition())));
            f2.setAdapter((data.get(spinner.getSelectedItemPosition()).getLecturesMarkVisiting()));

            f.setRefreshing(false);
            f2.setRefreshing(false);

            Toast.makeText(getApplicationContext(), "23", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    private class getGroupsAsyncTask extends AsyncTask<String, Void, List<GroupDTO>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((FragmentSecondTab) adapterList.get(1).getTabs().get(0)).setRefreshing(true);
            ((FragmentSecondTab) adapterList.get(1).getTabs().get(0)).setRefreshing(true);
        }

        @Override
        protected List doInBackground(String... params) {
            return ParsingJsonLms.getParseGroup(downloadJson(params[0]));
        }

        @Override
        protected void onPostExecute(List<GroupDTO> data) {
            setDataGroups(data);
        }
    }
}
