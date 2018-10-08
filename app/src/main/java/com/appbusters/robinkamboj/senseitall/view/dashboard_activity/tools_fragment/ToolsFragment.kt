package com.appbusters.robinkamboj.senseitall.view.dashboard_activity.tools_fragment


import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.appbusters.robinkamboj.senseitall.R
import com.appbusters.robinkamboj.senseitall.model.recycler.TinyInfo
import com.appbusters.robinkamboj.senseitall.model.recycler.ToolsItem
import com.appbusters.robinkamboj.senseitall.utils.AppConstants
import com.appbusters.robinkamboj.senseitall.utils.AppConstants.*
import com.appbusters.robinkamboj.senseitall.utils.StartSnapHelper
import com.appbusters.robinkamboj.senseitall.view.dashboard_activity.tools_fragment.adapter.image_tools.ImageToolsAdapter
import com.appbusters.robinkamboj.senseitall.view.dashboard_activity.tools_fragment.adapter.quick_settings.QuickSettingsAdapter
import com.appbusters.robinkamboj.senseitall.view.dashboard_activity.tools_fragment.adapter.quick_settings.QuickSettingsListener
import kotlinx.android.synthetic.main.fragment_tools.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class ToolsFragment : Fragment(), ToolsInterface {

    var everyday_tools_list: MutableList<ToolsItem> = ArrayList()
    var image_tools_list: MutableList<ToolsItem> = ArrayList()
    lateinit var list: MutableList<TinyInfo>
    lateinit var quickAdapter: QuickSettingsAdapter
    lateinit var imageToolsAdapter: ImageToolsAdapter
    lateinit var everydayToolsAdapter: ImageToolsAdapter
    lateinit var lv : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the lv for this fragment
        val v = inflater.inflate(R.layout.fragment_tools, container, false)
        setup(v)
        return v
    }

    override fun setup(v: View) {
        lv = v
        setImageToolsAdapter()
        setEverydayToolsAdapter()
        setQuickSettingsRecycler()
        checkQuickSettingsStatus()
        registerReceivers()
    }

    override fun registerReceivers() {
        registerWifiStateReceiver()
        registerBluetoothStateReceiver()
        registerAutorotateStateReceiver()
    }

    @Suppress("DEPRECATION")
    override fun registerWifiStateReceiver() {
        val wifiFilter = IntentFilter()
        wifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        activity!!.registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                try {
                    if(context != null && intent != null && intent.action != null) {
                        val action = intent.action
                        if(action == WifiManager.NETWORK_STATE_CHANGED_ACTION) {
                            val networkInfo: NetworkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)
                            if(networkInfo.state == NetworkInfo.State.DISCONNECTED ||
                                    networkInfo.state == NetworkInfo.State.DISCONNECTING) {
                                quickAdapter.updateItemState(WIFI_QUICK, false)
                            }
                            else if(networkInfo.state == NetworkInfo.State.CONNECTED ||
                                    networkInfo.state == NetworkInfo.State.CONNECTING) {
                                quickAdapter.updateItemState(WIFI_QUICK, true)
                            }

                        }
                    }
                }
                catch (e: Exception) {

                }
            }
        }, wifiFilter)
    }

    override fun registerBluetoothStateReceiver() {
        val wifiFilter = IntentFilter()
        wifiFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        activity!!.registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                try {
                    if(context != null && intent != null && intent.action != null) {
                        val action = intent.action
                        if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
                            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                            if (state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_CONNECTING
                            || state == BluetoothAdapter.STATE_TURNING_ON) {
                                quickAdapter.updateItemState(BLUETOOTH_QUICK, true)
                            }
                            if (state == BluetoothAdapter.STATE_OFF || state == BluetoothAdapter.STATE_DISCONNECTING
                                   || state == BluetoothAdapter.STATE_TURNING_OFF) {
                                quickAdapter.updateItemState(BLUETOOTH_QUICK, false)
                            }
                        }
                    }
                }
                catch (e: Exception) {

                }
            }
        }, wifiFilter)
    }

    override fun registerAutorotateStateReceiver() {
        val contentObserver = object: ContentObserver(Handler()) {
            override fun onChange(selfChange: Boolean) {
                //notifies false if my app is not changing autorotate
                if(android.provider.Settings.System.getInt(activity?.contentResolver,
                                Settings.System.ACCELEROMETER_ROTATION, 0) == 1){
                    quickAdapter.updateItemState(AUTOROTATE_QUICK, true)
                }
            }
        }
        activity?.contentResolver?.registerContentObserver(
                Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION),
                true,
                contentObserver
        )
    }

    override fun setImageToolsAdapter() {
        val list : List<String> = AppConstants.imageTools
        list.forEach {
            image_tools_list.add(ToolsItem(it, AppConstants.imageUrlMap[it]))
        }
        imageToolsAdapter = ImageToolsAdapter(image_tools_list, activity)
        lv.image_tools_rv.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        lv.image_tools_rv.adapter = imageToolsAdapter
        lv.image_tools_rv.onFlingListener = null
        StartSnapHelper().attachToRecyclerView(lv.image_tools_rv)
    }

    override fun setEverydayToolsAdapter() {
        val list : List<String> = AppConstants.everydayTools
        list.forEach {
            everyday_tools_list.add(ToolsItem(it, AppConstants.imageUrlMap[it]))
        }
        everydayToolsAdapter = ImageToolsAdapter(everyday_tools_list, activity)
        lv.everyday_tools_rv.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        lv.everyday_tools_rv.adapter = everydayToolsAdapter
        lv.everyday_tools_rv.onFlingListener = null
        StartSnapHelper().attachToRecyclerView(lv.everyday_tools_rv)
    }

    override fun setQuickSettingsRecycler() {
        list = AppConstants.quickSettings
        quickAdapter = QuickSettingsAdapter(list, activity, QuickSettingsListener {
            flipSetting(it)
        })
        lv.quick_settings_rv.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        lv.quick_settings_rv.adapter = quickAdapter
        lv.quick_settings_rv.onFlingListener = null
        StartSnapHelper().attachToRecyclerView(lv.quick_settings_rv)
    }

    override fun checkQuickSettingsStatus() {
        for(info in list) {
            checkEachQuickSetting(info.name)
        }
    }

    override fun checkEachQuickSetting(info: String) {
        when (info) {
            WIFI_QUICK -> {
                val wifiManager =
                        activity?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
                if(wifiManager.isWifiEnabled) quickAdapter.updateItemState(info, true)

            }
            BLUETOOTH_QUICK -> {
                if(BluetoothAdapter.getDefaultAdapter().isEnabled) quickAdapter.updateItemState(info, true)
            }
            BRIGHTNESS_QUICK -> {

            }
            VOLUME_QUICK -> {

            }
            HOTSPOT_QUICK -> {

            }
            FLASHLIGHT_QUICK -> {

            }
            LOCATION_QUICK -> {

            }
            AIRPLANE_QUICK -> {
//                if(Settings.System.getInt(context?.getContentResolver(),
//                                AIRPLANE_MODE_ON, 0) != 0) {
//                    quickAdapter.updateItemState(info, true)
//                }
            }
            AUTOROTATE_QUICK -> {
                if(android.provider.Settings.System.getInt(activity?.contentResolver,
                                Settings.System.ACCELEROMETER_ROTATION, 0) == 1){
                    quickAdapter.updateItemState(info, true)
                }
                else quickAdapter.updateItemState(info, false)
            }
        }
    }

    override fun flipSetting(info: TinyInfo) {
        when(info.name) {
            WIFI_QUICK -> {
                flipWifiSetting(!info.isOn)
            }
            BLUETOOTH_QUICK -> {
                flipBluetoothSetting(!info.isOn)
            }
            BRIGHTNESS_QUICK -> {

            }
            VOLUME_QUICK -> {

            }
            HOTSPOT_QUICK -> {

            }
            FLASHLIGHT_QUICK -> {

            }
            LOCATION_QUICK -> {

            }
            AIRPLANE_QUICK -> {

            }
            AUTOROTATE_QUICK -> {
                flipAutorotateSetting(!info.isOn)
            }
        }
    }

    override fun flipBluetoothSetting(turnOn: Boolean) {

        if(BluetoothAdapter.getDefaultAdapter().isEnabled && turnOn) {
            quickAdapter.updateItemState(BLUETOOTH_QUICK, true)
            return
        }

        if(!BluetoothAdapter.getDefaultAdapter().isEnabled && !turnOn) {
            quickAdapter.updateItemState(BLUETOOTH_QUICK, false)
            return
        }

        if(!BluetoothAdapter.getDefaultAdapter().isEnabled && turnOn) {
            BluetoothAdapter.getDefaultAdapter().enable()
            quickAdapter.updateItemState(BLUETOOTH_QUICK, true)
            return
        }

        if(BluetoothAdapter.getDefaultAdapter().isEnabled && !turnOn) {
            BluetoothAdapter.getDefaultAdapter().disable()
            quickAdapter.updateItemState(BLUETOOTH_QUICK, false)
            return
        }
    }

    override fun flipWifiSetting(turnOn: Boolean) {
        val wifiManager =
                activity?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager

        //request is to turn on and it is already enabled
        if(wifiManager.isWifiEnabled && turnOn) {
            quickAdapter.updateItemState(WIFI_QUICK, true)
            return
        }

        //request is to turn off and it is already disabled
        if(!wifiManager.isWifiEnabled && !turnOn) {
            quickAdapter.updateItemState(WIFI_QUICK, false)
            return
        }

        //it is turned on but request is to turn off
        if(wifiManager.isWifiEnabled && !turnOn) {
            wifiManager.isWifiEnabled = false
            quickAdapter.updateItemState(WIFI_QUICK, false)
            return
        }

        if(!wifiManager.isWifiEnabled && turnOn) {
            wifiManager.isWifiEnabled = true
            quickAdapter.updateItemState(WIFI_QUICK, true)
            return
        }
    }

    override fun flipAutorotateSetting(turnOn: Boolean) {
        try {
            Settings.System.putInt(
                    context?.contentResolver,
                    Settings.System.ACCELEROMETER_ROTATION,
                    if (turnOn) 1 else 0
            )
            quickAdapter.updateItemState(AUTOROTATE_QUICK, turnOn)
        }
        catch (e: Exception) {
            showCoordinatorSettings("allow \"modify system settings\" for this app to use this feature " +
                    "(under Advanced)")
        }
    }

    fun showCoordinatorNegative(coordinatorText: String) {
        val s = Snackbar.make(lv.coordinator_tools, coordinatorText, Snackbar.LENGTH_SHORT)
        val v = s.view
        v.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.red_shade_three_less_vibrant))
        val t = v.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        t.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
        t.textAlignment = View.TEXT_ALIGNMENT_CENTER
        s.show()
    }

    fun showCoordinatorSettings(coordinatorText: String) {
        val s = Snackbar.make(lv.coordinator_tools, coordinatorText, Snackbar.LENGTH_LONG)
        val v = s.view
        v.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorBlackShade))
        val t = v.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        t.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
        s.setAction("SETTINGS") {
            startActivityForResult(Intent(android.provider.Settings.ACTION_APPLICATION_SETTINGS), 0)
        }
        s.show()
    }

    fun showCoordinatorPositive(coordinatorText: String) {
        val s = Snackbar.make(lv.coordinator_tools, coordinatorText, Snackbar.LENGTH_SHORT)
        val v = s.view
        v.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.primary_new))
        val t = v.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        t.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
        t.textAlignment = View.TEXT_ALIGNMENT_CENTER
        s.show()
    }
}
