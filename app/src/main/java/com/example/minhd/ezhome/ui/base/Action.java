package com.t3h.basemvp.ui.base;

/**
 * Created by ducnd on 5/21/17.
 */

public interface Action<E> {
    void call(E e);
}
