package br.net.bmobile.ow.activity;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import br.net.bmobile.ow.R;
import br.net.bmobile.ow.fragment.ContactsFragment;
import br.net.bmobile.ow.fragment.NotificationsFragment;
import br.net.bmobile.ow.fragment.SettingsFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends RoboFragmentActivity {

	private PagerAdapter pagerAdapter;
	
	@InjectView(R.id.pager)
	private ViewPager viewPager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        initializePaging();
	}
	
	public void selectPage(int page) {
		
		viewPager.setCurrentItem(page, true);
	}
	
	private void initializePaging() {
		
		List<Fragment> fragments = new ArrayList<Fragment>();

		fragments.add(Fragment.instantiate(getApplicationContext(), 
				SettingsFragment.class.getName()));

		fragments.add(Fragment.instantiate(getApplicationContext(), 
				ContactsFragment.class.getName()));

		fragments.add(Fragment.instantiate(getApplicationContext(), 
				NotificationsFragment.class.getName()));
		
		pagerAdapter = new ApplicationPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
	}

	private class ApplicationPagerAdapter extends FragmentStatePagerAdapter {
       
		private List<Fragment> fragments;
		
		public ApplicationPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }	
}
