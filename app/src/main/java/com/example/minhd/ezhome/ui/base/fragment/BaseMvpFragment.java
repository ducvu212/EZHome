package com.t3h.basemvp.ui.base.fragment;

import com.t3h.basemvp.ui.base.IBasePresenter;

/**
 * Created by ducnd on 5/21/17.
 */

public abstract class BaseMvpFragment<P extends IBasePresenter> extends BaseFragment {

    protected P mPresenter;

    @Override
    public void onDestroyView() {
        if ( mPresenter != null ) {
            mPresenter.onDestroy();
        }
        super.onDestroyView();
    }
}
