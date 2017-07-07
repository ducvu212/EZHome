package com.ezhometeam.gallery.utils;

import java.io.Serializable;

/**
 * Created by n on 06/07/2017.
 */

    public class PairInt implements Serializable {
        private final int mFirst;
        private final int mSecond;

        public PairInt(int first, int second) {
            mFirst = first;
            mSecond = second;
        }

        public int getFirst() {
            return mFirst;
        }

        public int getSecond() {
            return mSecond;
        }

    }
