package com.aj.roundrectoverlay;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookInitPackageResources, IXposedHookZygoteInit, IXposedHookLoadPackage {
    Boolean authentic = false;
    Boolean material1 = false;
    String system = "android";
    String ui = "com.android.systemui";
    String resources;
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resParam) throws Throwable {
        XModuleResources res = XModuleResources.createInstance(resources, null);
        if (resParam.packageName.equals(system)) {
            XResources.setSystemWideReplacement(system, "bool", "config_useRoundIcon", false);
            XResources.setSystemWideReplacement(system, "dimen", "config_bottomDialogCornerRadius",
                    res.fwd(R.dimen.bottomDialogCornerRadius));
            XResources.setSystemWideReplacement(system, "dimen", "config_dialogCornerRadius",
                    res.fwd(R.dimen.dialogCornerRadius));
            XResources.setSystemWideReplacement(system, "string", "config_icon_mask",
                    res.fwd(R.string.config_icon_mask));

            if (material1) {
                // TODO: change text size
                XResources.setSystemWideReplacement(system, "dimen", "notification_headerless_min_height",
                        res.fwd(R.dimen.notification_headerless_min_height));
                XResources.setSystemWideReplacement(system, "dimen", "notification_header_height",
                        res.fwd(R.dimen.notification_headerless_min_height));

                // alternate_expand_target
                XResources.setSystemWideReplacement(system, "dimen", "notification_content_margin_start",
                        res.fwd(R.dimen.notification_headerless_min_height));

                // TODO: right icon, conversation icon
                XResources.setSystemWideReplacement(system, "dimen", "notification_icon_circle_start",
                        res.fwd(R.dimen.notification_icon_circle_start));
                XResources.setSystemWideReplacement(system, "dimen", "notification_icon_circle_padding",
                        res.fwd(R.dimen.notification_icon_circle_padding));
                XResources.setSystemWideReplacement(system, "dimen", "notification_icon_circle_size",
                        res.fwd(R.dimen.notification_icon_circle_size));

                if (!authentic)
                    XResources.setSystemWideReplacement(system, "drawable", "notification_icon_circle",
                        res.fwd(R.drawable.notification_icon_circle_m));

                // TODO: action icons, dividers, info
                XResources.setSystemWideReplacement(system, "dimen", "notification_actions_padding_start",
                        res.fwd(R.dimen.notification_actions_padding_start));
            } else {
                if (!authentic) {
                    XResources.setSystemWideReplacement(system, "drawable", "conversation_badge_background",
                            res.fwd(R.drawable.conversation_badge_background));
                    XResources.setSystemWideReplacement(system, "drawable", "conversation_badge_ring",
                            res.fwd(R.drawable.conversation_badge_ring));
                }

                XResources.setSystemWideReplacement(system, "drawable", "notification_icon_circle",
                        res.fwd(R.drawable.notification_icon_circle));
            }
        } else if (resParam.packageName.equals(ui)) {
            resParam.res.setReplacement(ui, "dimen", "global_actions_corner_radius",
                    res.fwd(R.dimen.bottomDialogCornerRadius));

            // Brightness slider
            resParam.res.setReplacement(ui, "dimen", "rounded_slider_corner_radius",
                    res.fwd(R.dimen.bottomDialogCornerRadius));
            resParam.res.setReplacement(ui, "dimen", "rounded_slider_track_inset",
                    res.fwd(R.dimen.dialogCornerRadius));
            resParam.res.setReplacement(ui, "dimen", "rounded_slider_background_rounded_corner",
                    res.fwd(R.dimen.rounded_slider_background_rounded_corner));

            // QQS and Media controls
            resParam.res.setReplacement(ui, "dimen", "qs_corner_radius",
                    res.fwd(R.dimen.qs_corner_radius));
            resParam.res.setReplacement(ui, "dimen", "qs_media_album_radius",
                    res.fwd(R.dimen.bottomDialogCornerRadius));

            // Notifications
            resParam.res.setReplacement(ui, "dimen", "notification_corner_radius",
                    res.fwd(R.dimen.bottomDialogCornerRadius));
            resParam.res.setReplacement(ui, "dimen", "notification_corner_radius_small",
                    res.fwd(R.dimen.dialogCornerRadius));
            resParam.res.setReplacement(ui, "dimen", "notification_scrim_corner_radius",
                    new XResources.DimensionReplacement((32.0f/28.0f)*4.0f, TypedValue.COMPLEX_UNIT_DIP));
            if (material1)
                resParam.res.setReplacement(ui, "dimen", "notification_children_container_margin_top",
                        res.fwd(R.dimen.notification_children_container_margin_top));
            resParam.res.setReplacement(ui, "drawable", "notif_footer_btn_background",
                    res.fwd(R.drawable.notif_footer_btn_background));

            // Volume slider
            resParam.res.setReplacement(ui, "dimen", "volume_dialog_slider_corner_radius",
                    res.fwd(R.dimen.bottomDialogCornerRadius));
            resParam.res.setReplacement(ui, "dimen", "volume_dialog_panel_width_half",
                    res.fwd(R.dimen.bottomDialogCornerRadius));
            resParam.res.setReplacement(ui, "dimen", "volume_ringer_drawer_item_size_half",
                    res.fwd(R.dimen.bottomDialogCornerRadius));

            // Keyguard
            resParam.res.setReplacement(ui, "dimen", "keyguard_affordance_fixed_radius",
                    res.fwd(R.dimen.bottomDialogCornerRadius));

        } else if ((resParam.packageName.equals("com.android.launcher3")) ||
                (resParam.packageName.equals("com.google.android.apps.nexuslauncher"))) {
            resParam.res.setReplacement(resParam.packageName, "dimen", "default_dialog_corner_radius",
                    res.fwd(R.dimen.bottomDialogCornerRadius));

            resParam.res.setReplacement(resParam.packageName, "dimen", "task_corner_radius_override",
                    res.fwd(R.dimen.dialogCornerRadius));
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        resources = startupParam.modulePath;
    }

    String setLib = "com.android.settingslib";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals(setLib) || loadPackageParam.packageName.equals(ui)) {
            /*findAndHookMethod(setLib + ".notification.ConversationIconFactory.ConversationIconDrawable",
                    loadPackageParam.classLoader,
                    "draw",
                    Canvas.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            setObjectField(param.thisObject, "CENTER_RADIUS", 5.0f);
                        /*Drawable badgeIcon = (Drawable) getObjectField(param.thisObject, "mBadgeIcon");
                        int pos = (int) getIntField(param.thisObject, "badgeCenter");
                        Paint paint = (Paint) getObjectField(param.thisObject, "mPaddingPaint");
                        //int rad = (int) getIntField(param.thisObject, "radius");
                        if (badgeIcon != null) {
                            Canvas canvas = (Canvas) param.args[0];
                            canvas.drawRoundRect(0, 0, pos*2, pos*2, 2.0f, 2.0f, paint);
                        }*
                        }

                        //@Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            Drawable badgeIcon = (Drawable) getObjectField(param.thisObject, "mBadgeIcon");
                            int pos = (int) getIntField(param.thisObject, "badgeCenter");
                            Paint paint = (Paint) getObjectField(param.thisObject, "mPaddingPaint");
                            //int rad = (int) getIntField(param.thisObject, "radius");
                            if (badgeIcon != null) {
                                Canvas canvas = (Canvas) param.args[0];
                                canvas.drawRoundRect(0, 0, pos*2, pos*2, 2.0f, 2.0f, paint);
                            }
                        }
                    });*/
        } else if (loadPackageParam.packageName.equals(system)) {
            findAndHookMethod("com.android.internal.widget.ConversationLayout",
                    loadPackageParam.classLoader,
                    "onFinishInflate",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Thread.dumpStack();
                        }
                    });
        }
    }
}
