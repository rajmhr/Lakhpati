package com.lakhpati.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.lakhpati.fragments.AllUserTicketsFragment;
import com.lakhpati.fragments.BuyTicketFragment;
import com.lakhpati.fragments.MyTicketsFragment;
import com.lakhpati.models.LotteryGroupCampaignDetailModel;

public class LotteryDefinitionTabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    FragmentActivity context;

    private Fragment[] childFragments;

    public LotteryDefinitionTabAdapter(FragmentManager fm, int NoofTabs, FragmentActivity context) {
        super(fm,FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mNumOfTabs = NoofTabs;
        this.context = (FragmentActivity)context;
        childFragments = new Fragment[] {
                new BuyTicketFragment(),
                new MyTicketsFragment(),
                new AllUserTicketsFragment()
        };
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

        if (position <= getCount()) {
            FragmentManager manager = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

}
