package com.appbusters.robinkamboj.senseitall.view.dashboard_activity.tools_fragment

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.location.LocationManager
import android.net.*
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
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

    private var everydayToolsList: MutableList<ToolsItem> = ArrayList()
    private var imageToolsList: MutableList<ToolsItem> = ArrayList()
    lateinit var list: MutableList<TinyInfo>
    private lateinit var quickAdapter: QuickSettingsAdapter
    private lateinit var imageToolsAdapter: ImageToolsAdapter
    private lateinit var everydayToolsAdapter: ImageToolsAdapter
    lateinit var lv : View

    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var connectivityManager: ConnectivityManager? = null
    private var locationReceiver: BroadcastReceiver? = null
    private var wifiAndHotspotReceiver: BroadcastReceiver? = null
    private var bluetoothReceiver: BroadcastReceiver? = null
    private var airplaneReceiver: BroadcastReceiver? = null
    private var autorotateObserver: ContentObserver? = null

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
    }

    override fun registerReceivers() {
        registerWifiStateReceiver()
        registerBluetoothStateReceiver()
        registerAutorotateStateReceiver()
        registerAirplaneModeStateReceiver()
        registerLocationAccessStateReceiver()
    }

    override fun registerLocationAccessStateReceiver() {
        if(locationReceiver == null) {
            locationReceiver = object: BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    try {
                        if(context != null && intent != null && intent.action != null) {
                            val action = intent.action
                            if (LocationManager.MODE_CHANGED_ACTION == action) {
                                val locationManager
                                        = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                                checkStateAndShow(
                                        LOCATION_QUICK,
                                        locationManager.isLocationEnabled
                                )
                            }
                        }
                    }
                    catch (e: Exception) {

                    }
                }
            }
        }

        if(activity != null) {
            val locationFilter = IntentFilter()
            locationFilter.addAction(LocationManager.MODE_CHANGED_ACTION)
            activity!!.registerReceiver(locationReceiver, locationFilter)
        }
    }

    override fun registerWifiStateReceiver() {
        if(wifiAndHotspotReceiver == null) {
            wifiAndHotspotReceiver = object: BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    try {
                        if(context != null && intent != null && intent.action != null) {
                            val action = intent.action
                            if(action == WifiManager.NETWORK_STATE_CHANGED_ACTION) {
                                val networkInfo: NetworkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)
                                checkStateAndShow(
                                        WIFI_QUICK,
                                        networkInfo.detailedState == NetworkInfo.DetailedState.IDLE ||
                                                networkInfo.detailedState == NetworkInfo.DetailedState.SCANNING ||
                                                networkInfo.detailedState == NetworkInfo.DetailedState.CONNECTING ||
                                                networkInfo.detailedState == NetworkInfo.DetailedState.CONNECTED ||
                                                networkInfo.detailedState == NetworkInfo.DetailedState.AUTHENTICATING ||
                                                networkInfo.detailedState == NetworkInfo.DetailedState.OBTAINING_IPADDR ||
                                                networkInfo.detailedState == NetworkInfo.DetailedState.SUSPENDED
                                )
                            }
                            if(action == getString(R.string.wifi_ap_state_change_action)) {
                                val state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0)

                                checkStateAndShow(
                                        HOTSPOT_QUICK,
                                        WifiManager.WIFI_STATE_ENABLED == state % 10
                                )
                            }
                        }
                    }
                    catch (e: Exception) {

                    }
                }
            }
        }

        if(activity != null) {
            val wifiFilter = IntentFilter()
            wifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            wifiFilter.addAction(getString(R.string.wifi_ap_state_change_action))
            activity?.registerReceiver(wifiAndHotspotReceiver, wifiFilter)
        }
    }

    override fun registerBluetoothStateReceiver() {
        if(bluetoothReceiver == null) {
            bluetoothReceiver = object: BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    try {
                        if(context != null && intent != null && intent.action != null) {
                            val action = intent.action
                            if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
                                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)

                                checkStateAndShow(
                                        BLUETOOTH_QUICK,
                                        state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_CONNECTING
                                                || state == BluetoothAdapter.STATE_TURNING_ON
                                )
                            }
                        }
                    }
                    catch (e: Exception) {

                    }
                }
            }
        }

        if(activity != null) {
            val bluetoothFilter = IntentFilter()
            bluetoothFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            activity?.registerReceiver(bluetoothReceiver, bluetoothFilter)
        }
    }

    override fun registerAirplaneModeStateReceiver() {
        if(airplaneReceiver == null) {
            airplaneReceiver = object: BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    try {
                        if(context != null && intent != null && intent.action != null) {
                            val action = intent.action
                            if (Intent.ACTION_AIRPLANE_MODE_CHANGED == action) {
                                checkStateAndShow(
                                        AIRPLANE_QUICK,
                                        Settings.Global.getInt(context.contentResolver,
                                                Settings.Global.AIRPLANE_MODE_ON, 0) != 0
                                )
                            }
                        }
                    }
                    catch (e: Exception) {

                    }
                }
            }
        }

        if(activity != null) {
            val airplaneFilter = IntentFilter()
            airplaneFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            activity?.registerReceiver(airplaneReceiver, airplaneFilter)
        }
    }

    override fun registerAutorotateStateReceiver() {
        if(autorotateObserver == null) {
            autorotateObserver = object: ContentObserver(Handler()) {
                override fun onChange(selfChange: Boolean) {
                    checkStateAndShow(
                            AUTOROTATE_QUICK,
                            android.provider.Settings.System.getInt(activity?.contentResolver,
                                    Settings.System.ACCELEROMETER_ROTATION, 0) == 1
                    )
                }
            }
        }

        if(autorotateObserver != null) {
            activity?.contentResolver?.registerContentObserver(
                    Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION),
                    true,
                    autorotateObserver!!
            )
        }
    }

    fun checkStateAndShow(item: String, isPositive: Boolean) {
        if(isPositive) {
            if(!quickAdapter.getItemState(item)) {
                quickAdapter.updateItemState(item, true)
                showCoordinatorPositive("$item Enabled")
            }
        }
        else {
            if(quickAdapter.getItemState(item)) {
                quickAdapter.updateItemState(item, false)
                showCoordinatorPositive("$item Disabled")
            }
        }
    }

    override fun setImageToolsAdapter() {
        val list : List<String> = AppConstants.imageTools
        list.forEach {
            imageToolsList.add(ToolsItem(it, AppConstants.imageUrlMap[it]))
        }
        imageToolsAdapter = ImageToolsAdapter(imageToolsList, activity)
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
            everydayToolsList.add(ToolsItem(it, AppConstants.imageUrlMap[it]))
        }
        everydayToolsAdapter = ImageToolsAdapter(everydayToolsList, activity)
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
                try {

                }
                catch(e: Exception) {
                    val wifiManager =
                            activity?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    if(wifiManager.isWifiEnabled) quickAdapter.updateItemState(info, true)
                }

            }
            BLUETOOTH_QUICK -> {
                try {
                    if(BluetoothAdapter.getDefaultAdapter().isEnabled) quickAdapter.updateItemState(info, true)
                }
                catch(e: Exception) {

                }
            }
            BRIGHTNESS_QUICK -> {

            }
            VOLUME_QUICK -> {

            }
            HOTSPOT_QUICK -> {
                try {
                    val wifiManager =
                            activity?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val method = wifiManager.javaClass.getDeclaredMethod("getWifiApState")
                    method.isAccessible = true
                    val actualState = method.invoke(wifiManager, null) as Int
                    if(actualState%10 == WifiManager.WIFI_STATE_ENABLED) {
                        quickAdapter.updateItemState(info, true)
                    }
                    else {
                        quickAdapter.updateItemState(info, false)
                    }
                }
                catch (e: java.lang.Exception) {

                }
            }
            FLASHLIGHT_QUICK -> {

            }
            LOCATION_QUICK -> {
                try {
                    val locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    if(locationManager.isLocationEnabled) quickAdapter.updateItemState(LOCATION_QUICK, true)
                    else quickAdapter.updateItemState(LOCATION_QUICK, false)
                }
                catch(e: Exception) {

                }
            }
            AIRPLANE_QUICK -> {
                try {
                    if(Settings.Global.getInt(context?.contentResolver,
                                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0) {
                        quickAdapter.updateItemState(info, true)
                    }
                    else {
                        quickAdapter.updateItemState(info, false)
                    }
                }
                catch(e: Exception) {

                }
            }
            AUTOROTATE_QUICK -> {
                try {
                    if(android.provider.Settings.System.getInt(activity?.contentResolver,
                                    Settings.System.ACCELEROMETER_ROTATION, 0) == 1){
                        quickAdapter.updateItemState(info, true)
                    }
                    else quickAdapter.updateItemState(info, false)
                }
                catch(e: Exception) {

                }
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
                flipHotspotSetting(!info.isOn)
            }
            FLASHLIGHT_QUICK -> {

            }
            LOCATION_QUICK -> {
                flipLocationAccessSetting(!info.isOn)
            }
            AIRPLANE_QUICK -> {
                flipAirplaneModeSetting(!info.isOn)
            }
            AUTOROTATE_QUICK -> {
                flipAutorotateSetting(!info.isOn)
            }
        }
    }

    override fun flipBluetoothSetting(turnOn: Boolean) {

        if(BluetoothAdapter.getDefaultAdapter().isEnabled && turnOn) {
            checkStateAndShow(BLUETOOTH_QUICK, turnOn)
            quickAdapter.updateItemState(BLUETOOTH_QUICK, true)
            return
        }

        if(!BluetoothAdapter.getDefaultAdapter().isEnabled && !turnOn) {
            checkStateAndShow(BLUETOOTH_QUICK, turnOn)
            quickAdapter.updateItemState(BLUETOOTH_QUICK, false)
            return
        }

        if(!BluetoothAdapter.getDefaultAdapter().isEnabled && turnOn) {
            BluetoothAdapter.getDefaultAdapter().enable()
            checkStateAndShow(BLUETOOTH_QUICK, turnOn)
            quickAdapter.updateItemState(BLUETOOTH_QUICK, true)
            return
        }

        if(BluetoothAdapter.getDefaultAdapter().isEnabled && !turnOn) {
            BluetoothAdapter.getDefaultAdapter().disable()
            checkStateAndShow(BLUETOOTH_QUICK, turnOn)
            quickAdapter.updateItemState(BLUETOOTH_QUICK, false)
            return
        }
    }

    override fun flipWifiSetting(turnOn: Boolean) {
        val wifiManager =
                activity?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager

        //request is to turn on and it is already enabled
        if(wifiManager.isWifiEnabled && turnOn) {
            checkStateAndShow(WIFI_QUICK, turnOn)
            quickAdapter.updateItemState(WIFI_QUICK, true)
            return
        }

        //request is to turn off and it is already disabled
        if(!wifiManager.isWifiEnabled && !turnOn) {
            checkStateAndShow(WIFI_QUICK, turnOn)
            quickAdapter.updateItemState(WIFI_QUICK, false)
            return
        }

        //it is turned on but request is to turn off
        if(wifiManager.isWifiEnabled && !turnOn) {
            wifiManager.isWifiEnabled = false
            checkStateAndShow(WIFI_QUICK, turnOn)
            quickAdapter.updateItemState(WIFI_QUICK, false)
            return
        }

        if(!wifiManager.isWifiEnabled && turnOn) {
            wifiManager.isWifiEnabled = true
            checkStateAndShow(WIFI_QUICK, turnOn)
            quickAdapter.updateItemState(WIFI_QUICK, true)
            return
        }
    }

    override fun flipHotspotSetting(turnOn: Boolean) {
        showCoordinatorNegative("this setting cannot be changed from here")
    }

    override fun flipAutorotateSetting(turnOn: Boolean) {
        try {
            Settings.System.putInt(
                    context!!.contentResolver,
                    Settings.System.ACCELEROMETER_ROTATION,
                    if (turnOn) 1 else 0
            )
            checkStateAndShow(AUTOROTATE_QUICK, turnOn)
            quickAdapter.updateItemState(AUTOROTATE_QUICK, turnOn)
        }
        catch (e: Exception) {
            showCoordinatorSettings("allow \"modify system settings\" for this app under \"advanced\" section")
        }
    }

    override fun flipAirplaneModeSetting(turnOn: Boolean) {
        showCoordinatorNegative("this setting cannot be changed from here")
    }

    override fun flipLocationAccessSetting(turnOn: Boolean) {
        showCoordinatorNegative("this setting cannot be changed from here")
    }

    private fun showCoordinatorNegative(coordinatorText: String) {
        val s = Snackbar.make(lv.coordinator_tools, coordinatorText, Snackbar.LENGTH_SHORT)
        val v = s.view
        v.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.red_shade_three_less_vibrant))
        val t = v.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        t.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
        t.textAlignment = View.TEXT_ALIGNMENT_CENTER
        s.show()
    }

    private fun showCoordinatorPositive(coordinatorText: String) {
        val s = Snackbar.make(lv.coordinator_tools, coordinatorText, Snackbar.LENGTH_SHORT)
        val v = s.view
        v.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.primary_new))
        val t = v.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        t.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
        t.textAlignment = View.TEXT_ALIGNMENT_CENTER
        s.show()
    }

    private fun showCoordinatorSettings(coordinatorText: String) {
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

    override fun onStart() {
        super.onStart()
        registerReceivers()
    }

    override fun onStop() {
        unregisterReceivers()
        super.onStop()
    }

    override fun unregisterReceivers() {
        try { activity?.unregisterReceiver(locationReceiver)} catch (e: java.lang.Exception) {}
        try { activity?.unregisterReceiver(wifiAndHotspotReceiver)} catch (e: java.lang.Exception) {}
        try { activity?.unregisterReceiver(bluetoothReceiver)} catch (e: java.lang.Exception) {}
        try { activity?.unregisterReceiver(airplaneReceiver)} catch (e: java.lang.Exception) {}
        try {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        }
        catch (e: java.lang.Exception) {}

        if(autorotateObserver != null)
            try {
                activity?.contentResolver?.unregisterContentObserver(autorotateObserver!!)
            }
            catch (e: java.lang.Exception) {}
    }
}
