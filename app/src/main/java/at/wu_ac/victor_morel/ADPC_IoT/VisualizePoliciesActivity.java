package at.wu_ac.victor_morel.ADPC_IoT;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewTreeObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotRule;
import at.wu_ac.victor_morel.ADPC_IoT.Model.PilotPolicyViewModel;
import at.wu_ac.victor_morel.ADPC_IoT.Model.PilotRuleListAdapter;

public class VisualizePoliciesActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_policies);
        final View rootView = findViewById(android.R.id.content); //retrieve the view
        ViewTreeObserver observer = rootView.getViewTreeObserver(); //add an observer


        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() { //methods to set when the view is properly created
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                View policyView = mSectionsPagerAdapter.getFragment(0).getView();
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        //tabs are not fragments! they're literally the little things on top
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab3"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab4"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab5"));

        // Setting a listener for clicks.
        mViewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        mViewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visualize_policies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private PilotPolicyViewModel policyViewModel;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public static final int UPDATE_POLICY_ACTIVITY_REQUEST_CODE = 1;

        public static final String EXTRA_DATA_UPDATE_POLICY = "extra_policy_to_be_updated";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_visualize_policies, container, false);
            RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);


            final PilotRuleListAdapter adapter = new PilotRuleListAdapter(getActivity());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            policyViewModel = ViewModelProviders.of(getActivity()).get(PilotPolicyViewModel.class);
            policyViewModel.getAllRulesFromPolicy().observe(getActivity(), new Observer<List<PilotRule>>() {
                @Override
                public void onChanged(@Nullable final List<PilotRule> pilotRules) {
                    adapter.setRules(pilotRules);
                }
            });

            adapter.setOnItemClickListener(new PilotRuleListAdapter.ClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    PilotRule rule = adapter.getRuleAtPosition(position);
                    launchUpdatePolicyActivity(rule);
                }
            });


//            final PilotPolicyListAdapter adapter = new PilotPolicyListAdapter(getActivity());
//            recyclerView.setAdapter(adapter);
//            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//            policyViewModel = ViewModelProviders.of(getActivity()).get(PilotPolicyViewModel.class);
//            policyViewModel.getAllPolicies().observe(getActivity(), new Observer<List<PilotPolicy>>() {
//                @Override
//                public void onChanged(@Nullable final List<PilotPolicy> policies) {
//                    // Update the cached copy of the words in the adapter.
//                    adapter.setPolicies(policies); //ONLY ONY POLICY SHOULD BE SET
//                }
//            });
//            adapter.setOnItemClickListener(new PilotPolicyListAdapter.ClickListener() {
//
//                @Override
//                public void onItemClick(View v, int position) {
//                    PilotPolicy policy = adapter.getPolicyAtPosition(position);
//                    launchUpdatePolicyActivity(policy);
//                }
//            });


            return rootView;
        }

        private void launchUpdatePolicyActivity(PilotRule rule) {
            Intent intent = new Intent(getActivity(), AddRuleActivity.class);
            intent.putExtra(EXTRA_DATA_UPDATE_POLICY, rule);
            startActivityForResult(intent, UPDATE_POLICY_ACTIVITY_REQUEST_CODE);
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Map<Integer, Fragment> pageReferenceMap = new HashMap<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment myFragment = PlaceholderFragment.newInstance(position + 1);
            pageReferenceMap.put(position, myFragment);
            return myFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 100;
        }

        public Fragment getFragment(int index) {
            return pageReferenceMap.get(index);
        }
    }
}
