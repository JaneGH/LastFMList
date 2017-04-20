package com.itclimb.lastfmlist.base;

import android.support.v4.app.Fragment;

import com.itclimb.lastfmlist.injection.IHasComponent;

public abstract class BaseFragment extends Fragment {
    @SuppressWarnings("unchecked")
    protected <T> T getComponent(Class<T> componentType) {
        return componentType.cast(((IHasComponent<T>)getActivity()).getComponent());
    }

}
