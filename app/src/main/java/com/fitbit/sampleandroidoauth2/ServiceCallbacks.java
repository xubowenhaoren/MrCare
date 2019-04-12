package com.fitbit.sampleandroidoauth2;

import com.fitbit.sampleandroidoauth2.fragments.HeartRateFragment;
import com.fitbit.sampleandroidoauth2.fragments.InfoFragment;

/**
 * Created by Bowen Xu on 12/29/2017.
 */

public interface ServiceCallbacks {
     // Deprecated method to pass HeartRateFragment to BackgroundService when UserDataActivity stops.
     void update();
     void hello();
}
