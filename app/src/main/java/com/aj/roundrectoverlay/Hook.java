package com.aj.roundrectoverlay;

import android.content.res.XResources;
import android.util.TypedValue;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;

public class Hook implements IXposedHookInitPackageResources {
    String system = "android";
    String ui = "com.android.systemui";
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resParam) throws Throwable {
        if (resParam.packageName.equals(system)) {
            resParam.res.setReplacement(system, "bool", "config_useRoundIcon", false);
            resParam.res.setReplacement(system, "dimen", "config_bottomDialogCornerRadius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(system, "dimen", "config_dialogCornerRadius",
                    new XResources.DimensionReplacement(2.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(system, "string", "config_icon_mask",
                    "M50,0L88,0 C94.4,0 100,5.4 100 12 L100,88 C100,94.6 94.6 100 88 100 L12,100 C5.4,100 0,94.6 0,88 L0 12 C0 5.4 5.4 0 12 0 L50,0 Z");

        } else if (resParam.packageName.equals(ui)) {
            resParam.res.setReplacement(ui, "dimen", "global_actions_corner_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "qs_footer_action_corner_radius",//qs_corner_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "notification_corner_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "notification_corner_radius_small",
                    new XResources.DimensionReplacement(2.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "notification_scrim_corner_radius",
                    new XResources.DimensionReplacement(8.0f, TypedValue.COMPLEX_UNIT_DIP));
            //<dimen name="qs_media_album_radius">14dp</dimen>
        }

    }
}
