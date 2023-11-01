package com.aj.roundrectoverlay;

import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.util.TypedValue;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;

public class Hook implements IXposedHookInitPackageResources, IXposedHookZygoteInit {
    String system = "android";
    String ui = "com.android.systemui";
    String resources;
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resParam) throws Throwable {
        if (resParam.packageName.equals(system)) {
            XResources.setSystemWideReplacement(system, "bool", "config_useRoundIcon", false);
            XResources.setSystemWideReplacement(system, "dimen", "config_bottomDialogCornerRadius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            XResources.setSystemWideReplacement(system, "dimen", "config_dialogCornerRadius",
                    new XResources.DimensionReplacement(2.0f, TypedValue.COMPLEX_UNIT_DIP));
            XResources.setSystemWideReplacement(system, "string", "config_icon_mask",
                    "M50,0L88,0 C94.4,0 100,5.4 100 12 L100,88 C100,94.6 94.6 100 88 100 L12,100 C5.4,100 0,94.6 0,88 L0 12 C0 5.4 5.4 0 12 0 L50,0 Z");

            XModuleResources res = XModuleResources.createInstance(resources, null);
            XResources.setSystemWideReplacement(system, "drawable", "notification_icon_circle",
                    res.fwd(R.drawable.notification_icon_circle));
            XResources.setSystemWideReplacement(system, "drawable", "conversation_badge_background",
                    res.fwd(R.drawable.conversation_badge_background));
            XResources.setSystemWideReplacement(system, "drawable", "conversation_badge_ring",
                    res.fwd(R.drawable.conversation_badge_ring));

        } else if (resParam.packageName.equals(ui)) {
            resParam.res.setReplacement(ui, "dimen", "global_actions_corner_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "qs_corner_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "notification_corner_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "notification_corner_radius_small",
                    new XResources.DimensionReplacement(2.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "notification_scrim_corner_radius",
                    new XResources.DimensionReplacement(4.57f, TypedValue.COMPLEX_UNIT_DIP));
            /*resParam.res.setReplacement(ui, "dimen", "qs_media_album_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));*/
            //<dimen name="qs_media_album_radius">14dp</dimen>
        }

    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        resources = startupParam.modulePath;
    }
}
