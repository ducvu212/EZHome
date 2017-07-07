package com.ezhometeam.common.ModulDirection;

import java.util.List;

/**
 * Created by Vu Minh Quang on 06-Jul-17.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}