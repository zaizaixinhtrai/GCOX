package com.gcox.fansmeet.location;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.gcox.fansmeet.util.StringUtil;
import timber.log.Timber;

import java.util.List;
import java.util.Locale;

/**
 * Created by User on 9/30/2015.
 */
public class GetAddress {

    public static String getAddress(Activity activity, double latitude, double longitude) {

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(activity, Locale.getDefault());

        try {

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }

        if (addresses == null || addresses.size() == 0) {
            return "";
        }

//        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String address2 = addresses.get(0).getAddressLine(1);
//        String address3 = addresses.get(0).getAddressLine(2);
        String city = addresses.get(0).getAddressLine(3);
        String country = addresses.get(0).getCountryName();//getCountryName();

//        Timber.e("address -> %s",addresses);
        String returnAddress = "";

//        if (!StringUtil.isNullOrEmptyString(address)) {
//            returnAddress = returnAddress + address + ", ";
//        }
//
//        if (!StringUtil.isNullOrEmptyString(address2)) {
//            returnAddress = returnAddress + address2 + ", ";
//        }
//
//        if (!StringUtil.isNullOrEmptyString(address3)) {
//            returnAddress = returnAddress + address3 + ", ";
//        }

        if (!StringUtil.isNullOrEmptyString(city)) {
            returnAddress = returnAddress + city + ", ";
        }

        if (!StringUtil.isNullOrEmptyString(country)) {
            returnAddress = returnAddress + country;
        }

        return returnAddress;
    }

    public static String getCountryCode(Context context, double latitude, double longitude){
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }

        if (addresses == null || addresses.size() == 0) {
            return "";
        }

        String countryCode = addresses.get(0).getCountryCode();
        if (!TextUtils.isEmpty(countryCode)){
            return countryCode;
        }
        return "";
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toUpperCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toUpperCase(Locale.US);
                }
            }
        }
        catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }
}
