package com.appbusters.robinkamboj.senseitall.ui.dashboard_activity.profile_fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.appbusters.robinkamboj.senseitall.R

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment(), ProfileInterface {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the parentView for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


}
