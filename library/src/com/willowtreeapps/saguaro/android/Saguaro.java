/*
 * Copyright (C) 2014 WillowTree Apps Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.willowtreeapps.saguaro.android;

import com.willowtreeapps.saguaro.android.model.License;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Saguaro {

    private static final String TAG = "Saguaro";
    private static final String WTA_ATTRIBUTION_URL
            = "http://www.willowtreeapps.com/?utm_source=%s&utm_medium=%s&utm_campaign=attribution";
    private static final String UTM_CAMPAIGN = "android";

    public static Intent getAttributionIntent(Context context) {
        Intent attributionIntent = new Intent(Intent.ACTION_VIEW);
        attributionIntent
                .setData(Uri.parse(String.format(WTA_ATTRIBUTION_URL, context.getPackageName(), UTM_CAMPAIGN)));
        return attributionIntent;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String getOsVersion() {
        return "Android " + Build.VERSION.RELEASE + " API Level " + Build.VERSION.SDK_INT;
    }

    public static String getFullVersionString(Context context) {
        return getVersionInfo(context, R.string.saguaro__full_version_text_dynamic);
    }

    public static String getMinVersionString(Context context) {
        return getVersionInfo(context, R.string.default_version_text_dynamic);
    }

    public static AlertDialog showOpenSourceDialog(final Context context) {
        Resources resources = context.getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.saguaro__acknowledgments);

        builder.setMessage(getOpenSourceText(context, resources));
        builder.setNegativeButton(resources.getString(R.string.saguaro__close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.ic_menu_info_details);
        AlertDialog dialog = builder.create();

        dialog.show();

        TextView message = (TextView) dialog.findViewById(android.R.id.message);
        message.setMovementMethod(LinkMovementMethod.getInstance());

        return dialog;
    }

    public static String getOpenSourceText(final Context context, final Resources resources) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(String.format(resources.getString(R.string.saguaro__licenses_header), getApplicationName(context)));
        sb.append("\n\n");

        // Iterate over "Other" projects
        String otherAcknowldgments = resources.getString(R.string.prepend_acknowledgments_text);
        if (!TextUtils.isEmpty(otherAcknowldgments)) {
            sb.append(otherAcknowldgments);
            sb.append("\n\n");
        }

        List<License> licenses = getOpenSourceLicenses(context, resources);

        for (final License license : licenses) {
            for (String project : license.projects) {
                sb.append("\u2022 ").append(project).append("\n");
            }
            sb.append("\n");
            String subHeader = resources.getString(R.string.saguaro__license_subheader, license.name);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    showLicenseDialog(context, license.textResId);
                }
            };

            int start = sb.length();
            int nameStart = subHeader.indexOf(license.name);
            sb.append(subHeader);
            sb.setSpan(clickableSpan, start + nameStart, start + nameStart + license.name.length(), 0);
            sb.append("\n\n");
        }
        return sb.toString();
    }

    public static List<License> getOpenSourceLicenses(final Context context, final Resources resources) {
        List<License> licenses = new ArrayList<License>();

        String[] baseLicenses = resources.getStringArray(R.array.saguaro__base_licenses);
        String[] userLicenses = resources.getStringArray(R.array.saguaro_licenses);

        List<String> licensesAsStrings = new ArrayList<String>();
        licensesAsStrings.addAll(Arrays.asList(baseLicenses));
        licensesAsStrings.addAll(Arrays.asList(userLicenses));

        for (String licenseString : licensesAsStrings) {
            int licenseNameId = resources.getIdentifier(licenseString + "_name", "string", context.getPackageName());
            final int licenseTextId = resources.getIdentifier(licenseString, "raw", context.getPackageName());
            int licenseProjectsId = resources
                    .getIdentifier(licenseString + "_projects", "array", context.getPackageName());

            // Skip if no licenses defined
            if (licenseProjectsId == 0) {
                continue;
            }

            String[] projects = resources.getStringArray(licenseProjectsId);

            if (projects.length > 0) {
                License license = new License();
                license.name = resources.getString(licenseNameId);
                license.textResId = licenseTextId;
                license.projects = projects;
                licenses.add(license);
            }
        }

        return licenses;
    }

    private static AlertDialog showLicenseDialog(Context context, int licenseTextId) {
        String licenseText = "";
        try {
            licenseText = readRawToString(context.getResources(), licenseTextId);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.saguaro__acknowledgments)
                .setMessage(licenseText)
                .setNegativeButton(R.string.saguaro__close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_menu_info_details);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static String readRawToString(Resources resources, int rawId) throws IOException {
        InputStream in = null;
        try {
            in = resources.openRawResource(rawId);
            return readToString(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private static String readToString(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[1000];

        StringBuilder x = new StringBuilder();

        int numRead;
        while ((numRead = inputStream.read(bytes)) >= 0) {
            x.append(new String(bytes, 0, numRead));
        }

        return x.toString();
    }

    public static SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);
        link.setSpan(new ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

    public static void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    public static Intent getSendFeedbackIntent(Context context) {
        Intent sendFeedbackIntent = new Intent(Intent.ACTION_SENDTO);

        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append("mailto:");
        uriBuilder.append(context.getString(R.string.send_feedback_email));

        uriBuilder.append("?subject=");
        String subject = context.getString(R.string.send_feedback_optional_subject);
        if (TextUtils.isEmpty(subject)) {
            uriBuilder.append(Uri.encode(
                    String.format(context.getString(R.string.saguaro__feedback_subject), getApplicationName(context))));
        } else {
            uriBuilder.append(Uri.encode(subject));
        }

        uriBuilder.append("&body=");
        String body = context.getString(R.string.send_feedback_optional_body);
        if (TextUtils.isEmpty(body)) {
            uriBuilder.append(Uri.encode("\n\n"));
            uriBuilder.append(Uri.encode(getFullVersionString(context)));
            uriBuilder.append(Uri.encode("\n"));
            uriBuilder.append(Uri.encode(getDeviceName()));
            uriBuilder.append(Uri.encode("\n"));
            uriBuilder.append(Uri.encode(getOsVersion()));
        } else {
            uriBuilder.append(Uri.encode(body));
        }

        sendFeedbackIntent.setData(Uri.parse(uriBuilder.toString()));

        return sendFeedbackIntent;
    }

    private static String getVersionInfo(Context context, int stringResId) {
        String versionString = "";
        int versionCode = 0;
        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        try {
            versionString = pm.getPackageInfo(packageName, 0).versionName;
            versionCode = pm.getPackageInfo(packageName, 0).versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            //Failed
        }
        return String.format(context.getResources().getString(stringResId), versionString,
                versionCode);
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private static String getApplicationName(Context context) {
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(context.getPackageName(), 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai)
                : context.getString(R.string.saguaro__this_application));
    }

    private static class ClickableString extends ClickableSpan {

        private View.OnClickListener mListener;

        public ClickableString(View.OnClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}
