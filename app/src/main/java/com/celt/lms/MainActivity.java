package com.celt.lms;

import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.*;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.celt.lms.adapter.*;
import com.celt.lms.api.ApiFactory;
import com.celt.lms.api.ApiLms;
import com.celt.lms.dto.GroupDTO;
import com.celt.lms.dto.LecturesDTO;
import com.celt.lms.dto.ParsingJsonLms;
import com.celt.lms.dto.Student;
import com.celt.lms.fragments.AbsFragment;
import com.celt.lms.fragments.FragmentFirstTab;
import com.celt.lms.fragments.FragmentSecondTab;
import com.celt.lms.fragments.dialogs.SaveNewsDialogFragment;
import com.google.gson.JsonElement;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.Snackbar.make;

public class MainActivity extends AppCompatActivity implements onEventListener {

    private static final int LAYOUT = R.layout.activity_main;
    private static TabsPagerFragmentAdapter tabsPagerAdapter;
    private static boolean isRefresh;
    private static int subjectId;

    private ApiLms api;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Spinner spinner;
    private Spinner spinner2;
    private FloatingActionButton fab;

    private Object[] objects;
    private ActionBarDrawerToggle toggle;

    public static boolean isCheckRefresh() {
        return isRefresh;
    }

    public static int getSubjectId() {
        return subjectId;
    }

    public static void setFragment(FragmentSecondTab fragment) {
        tabsPagerAdapter.getTabs().append(fragment.getKey(), fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

//        subjectId = 2014;
        subjectId = 2025;

        initToolbar();
        initNavigationView();
        initFab();

        tabsPagerAdapter = new TabsPagerFragmentAdapter(this, getSupportFragmentManager(), getTabs());

        api = ApiFactory.getService();

        if (savedInstanceState == null) {
            setVisibilitySpinners(View.GONE);
            if (isNetworkConnected()) {
                new getGroupsAsyncTask().execute();
                isRefresh = true;
            }
        }

        viewPager.setAdapter(tabsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        setOnTabSelectedListener();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        objects = (Object[]) getLastCustomNonConfigurationInstance();
        if (tabLayout.getSelectedTabPosition() == 0)
            setVisibilitySpinners(View.GONE);
        if (objects != null)
            setDataGroups((List<GroupDTO>) objects[0], (List<LecturesDTO>) objects[1], savedInstanceState.getInt("spinner", 0), savedInstanceState.getInt("spinner2", 0));
    }

    private SparseArrayCompat<AbsFragment> getTabs() {
        SparseArrayCompat<AbsFragment> tabs = new SparseArrayCompat<AbsFragment>();
        tabs.put(0, new FragmentFirstTab(this, getString(R.string.news), R.layout.fragment, new NewsListAdapter(this)));
        tabs.put(1, new FragmentSecondTab(this, 1, getString(R.string.education), R.layout.fragment, new LecturesListAdapter()));
        tabs.put(2, new FragmentSecondTab(this, 2, getString(R.string.visiting), R.layout.fragment, new VisitingListAdapter()));
        tabs.put(3, new FragmentSecondTab(this, 3, getString(R.string.marks), R.layout.fragment, new LabMarksListAdapter()));
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
        setSupportActionBar(toolbar);

        spinner = (Spinner) findViewById(R.id.spinner_nav);
        spinner2 = (Spinner) findViewById(R.id.spinner_nav2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<GroupDTO> data = (List<GroupDTO>) objects[0];
                if (data != null) {

                    Object[] object = new Object[2];
                    object[0] = objects[1];
                    object[1] = data.get(spinner.getSelectedItemPosition()).getSubGroup(spinner2.getSelectedItemPosition());
                    ((FragmentSecondTab) tabsPagerAdapter.getTabs().get(1)).setAdapter(object);
                    ((FragmentSecondTab) tabsPagerAdapter.getTabs().get(2)).setAdapter(data.get(spinner.getSelectedItemPosition()));
                    ((FragmentSecondTab) tabsPagerAdapter.getTabs().get(3)).setAdapter(data.get(spinner.getSelectedItemPosition()).getSubGroup(spinner2.getSelectedItemPosition()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<GroupDTO> data = (List<GroupDTO>) objects[0];
                Object[] object = new Object[2];
                object[0] = objects[1];
                object[1] = data.get(spinner.getSelectedItemPosition()).getSubGroup(spinner2.getSelectedItemPosition());
                ((FragmentSecondTab) tabsPagerAdapter.getTabs().get(1)).setAdapter(object);
                ((FragmentSecondTab) tabsPagerAdapter.getTabs().get(3)).setAdapter(data.get(spinner.getSelectedItemPosition()).getSubGroup(spinner2.getSelectedItemPosition()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initFab() {
        fab = (FloatingActionButton) findViewById(R.id.fabButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        Bundle args = new Bundle();
                        args.putInt("subjectId", getSubjectId());
                        args.putInt("idNews", 0);
                        args.putBoolean("is", false);
                        args.putString("title", "");
                        args.putString("text", "");

                        SaveNewsDialogFragment df = (new SaveNewsDialogFragment());
                        df.setArguments(args);
                        df.show(getSupportFragmentManager(), "dialog");
                        break;
                    case 3:
                        ((FragmentSecondTab) tabsPagerAdapter.getTabs().get(3)).changeView();
                        if (((FragmentSecondTab) tabsPagerAdapter.getTabs().get(3)).isTypeList()) {
                            fab.hide();
                            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                            params.setBehavior(null);
                            fab.setLayoutParams(params);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            toggle.syncState();
                            animateHamburgerIcon(0, 1);

                            final ImageButton saveButton = (ImageButton) findViewById(R.id.saveButton);
                            saveButton.setVisibility(View.VISIBLE);
                            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hideKeyboard(MainActivity.this);
                                    disableEditStatus(saveButton);
                                }
                            });
                            saveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hideKeyboard(MainActivity.this);
                                    new CallTask().execute(createJsonObjectStudents(((FragmentSecondTab) tabsPagerAdapter.getTabs().get(3)).getStudentsAdapter()));
                                }

                                class CallTask extends AsyncTask<JSONObject, Void, String> {

                                    @Override
                                    protected String doInBackground(JSONObject... params) {

                                        ApiLms api = ApiFactory.getService();
                                        try {
                                            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params[0].toString());
                                            Call<JsonElement> call = api.saveStudentLabsMark(body);
                                            JsonElement json = call.execute().body();
                                            if (json != null)
                                                return (new JSONObject(json.toString()).optString("Message"));
                                            else
                                                return null;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        return "";
                                    }

                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result == null)
                                            result = getString(R.string.network_error);
                                        else {
                                            disableEditStatus(saveButton);
                                        }
                                        make(viewPager, result, Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        break;
                }
            }
        });
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void disableEditStatus(ImageButton saveButton) {
        ((FragmentSecondTab) tabsPagerAdapter.getTabs().get(3)).changeView();
        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.syncState();
        animateHamburgerIcon(1, 0);
        saveButton.setVisibility(View.GONE);
        fab.show();
        CoordinatorLayout.LayoutParams la = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        la.setBehavior(new ScrollingFABBehavior());
        fab.setLayoutParams(la);
    }

    private JSONObject createJsonObjectStudents(List<Student> studentsList) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray students = new JSONArray();
            for (Student item : studentsList) {
                students.put(item.getJSONObject());
            }
            jsonObject.put("students", students);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void animateHamburgerIcon(int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator anim = ValueAnimator.ofFloat(start, end);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float slideOffset = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        slideOffset = (Float) valueAnimator.getAnimatedValue();
                    }
                    toggle.onDrawerSlide(drawerLayout, slideOffset);
                }
            });
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(500);
            anim.start();
        }
    }

    private void animateFab(final int position) {
        final int[] iconIntArray = {R.mipmap.ic_plus, R.mipmap.ic_pencil_dark};

        fab.clearAnimation();
        // Scale down animation
        ScaleAnimation shrink = new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);     // animation duration in milliseconds
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setImageDrawable(getResources().getDrawable(iconIntArray[position]));

                // Scale up animation
                ScaleAnimation expand = new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);     // animation duration in milliseconds
                expand.setInterpolator(new AccelerateInterpolator());
                fab.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fab.startAnimation(shrink);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getApplicationContext(), "s", Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("spinner", spinner.getSelectedItemPosition());
        outState.putInt("spinner2", spinner2.getSelectedItemPosition());
    }

    private void setDataGroups(List<GroupDTO> groupDTOList, List<LecturesDTO> lecturesDTOList, int spinnerSelect, int spinnerSelect2) {
        FragmentSecondTab f1 = (FragmentSecondTab) tabsPagerAdapter.getTabs().get(1);
        FragmentSecondTab f2 = (FragmentSecondTab) tabsPagerAdapter.getTabs().get(2);
        FragmentSecondTab f3 = (FragmentSecondTab) tabsPagerAdapter.getTabs().get(3);

        if (spinner.getAdapter() == null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item);
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item2);
            for (GroupDTO item : groupDTOList) {
                arrayAdapter.add(item.getGroupName());
            }
            spinner.setAdapter(arrayAdapter);
            spinner.setSelection(spinnerSelect);

            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item);
            arrayAdapter2.setDropDownViewResource(R.layout.spinner_item2);
            arrayAdapter2.add("Подгруппа 1");
            arrayAdapter2.add("Подгруппа 2");
            spinner2.setAdapter(arrayAdapter2);
            spinner2.setSelection(spinnerSelect2);
        }

        Object[] objects = new Object[2];
        objects[0] = lecturesDTOList;
        objects[1] = groupDTOList.get(spinner.getSelectedItemPosition()).getSubGroup(spinner2.getSelectedItemPosition());
        f1.setAdapter(objects);
        f2.setAdapter(groupDTOList.get(spinner.getSelectedItemPosition()));
        f3.setAdapter((groupDTOList.get(spinner.getSelectedItemPosition()).getSubGroup(spinner2.getSelectedItemPosition())));
//
//        f1.setRefreshing(false);
//        f2.setRefreshing(false);
//        f3.setRefreshing(false);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return objects;
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getOrder()) {
                    case 0:
                        break;
                    case 1:
                        break;
                }
//                tabLayout.setupWithViewPager(viewPager);
//                setOnTabSelectedListener();
                return true;
            }
        });
    }

    private void setOnTabSelectedListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hideKeyboard(MainActivity.this);
                viewPager.setCurrentItem(tab.getPosition());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    if ((findViewById(R.id.appBarLayout)).getTop() == 0) {
                        toolbar.setLayoutTransition(new LayoutTransition());
                    } else {
                        toolbar.setLayoutTransition(null);
                    }
                }
                if (tab.getText() == getString(R.string.news)) {
                    setVisibilitySpinners(View.GONE);
                    CoordinatorLayout.LayoutParams la = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                    la.setBehavior(new ScrollingFABBehavior());
                    fab.setLayoutParams(la);
                    if (fab.getVisibility() == View.GONE) {
                        fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_plus));
                        fab.show();
                    } else
                        animateFab(0);
                }

                if (tab.getText() == getString(R.string.marks)) {
                    if (((FragmentSecondTab) tabsPagerAdapter.getTabs().get(3)).isTypeList()) {
                        fab.hide();
                        CoordinatorLayout.LayoutParams la = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                        la.setBehavior(null);
                        fab.setLayoutParams(la);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toggle.syncState();
                        animateHamburgerIcon(0, 1);
                        final ImageButton saveButton = (ImageButton) findViewById(R.id.saveButton);
                        saveButton.setVisibility(View.VISIBLE);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                disableEditStatus(saveButton);
                            }
                        });
                    } else {
                        CoordinatorLayout.LayoutParams la = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                        la.setBehavior(new ScrollingFABBehavior());
                        fab.setLayoutParams(la);
                        if (fab.getVisibility() == View.GONE) {
                            fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pencil_dark));
                            fab.show();
                        } else
                            animateFab(1);
                    }
                }

                if (tab.getText() == getString(R.string.education) || tab.getText() == getString(R.string.visiting)) {
                    fab.hide();
                }

                if (spinner.getAdapter() != null && (tab.getText() == getString(R.string.education) || tab.getText() == getString(R.string.marks))) {
                    setVisibilitySpinners(View.VISIBLE);
                }

                if (spinner.getAdapter() != null && tab.getText() == getString(R.string.visiting)) {
                    spinner.setVisibility(View.VISIBLE);
                    spinner2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getText() == getString(R.string.marks)) {
                    final ImageButton saveButton = (ImageButton) findViewById(R.id.saveButton);
                    saveButton.setVisibility(View.GONE);
                    toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.open, R.string.close);
                    toggle.syncState();
                    animateHamburgerIcon(1, 0);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void updateGroupList() {
        tabsPagerAdapter.getTabs().get(1).setRefreshing(true);
        tabsPagerAdapter.getTabs().get(2).setRefreshing(true);
        tabsPagerAdapter.getTabs().get(3).setRefreshing(true);
        new getGroupsAsyncTask().execute();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private class getGroupsAsyncTask extends AsyncTask<Void, Void, ArrayList<JsonElement>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isNetworkConnected()) {
                this.cancel(true);
                Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                isRefresh = false;
            }
        }

        @Override
        protected ArrayList<JsonElement> doInBackground(Void... params) {
            try {
                if (!isCancelled()) {
                    ArrayList<JsonElement> jsonList = new ArrayList<JsonElement>();
                    Call<JsonElement> call2 = api.getLectures(getSubjectId());
                    JsonElement json = call2.execute().body();
                    jsonList.add(json);

                    Call<JsonElement> call = api.getGroups(getSubjectId());
                    json = call.execute().body();
                    jsonList.add(json);
                    return jsonList;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<JsonElement> data) {
            isRefresh = false;

            if (data != null && data.get(0) != null && data.get(1) != null) {
                objects = new Object[2];
                objects[0] = ParsingJsonLms.getParseGroup(data.get(1).toString());
                objects[1] = ParsingJsonLms.getParseLectures(data.get(0).toString());
                setDataGroups((List<GroupDTO>) objects[0], (List<LecturesDTO>) objects[1], 0, 0);
                Toast.makeText(getApplicationContext(), "Succeeded!", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
        }
    }
}
