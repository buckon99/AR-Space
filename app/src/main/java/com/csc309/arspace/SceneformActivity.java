package com.csc309.arspace;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.*;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class  SceneformActivity extends AppCompatActivity {
    private static final String TAG = SceneformActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable cubeRenderable;
    private ViewRenderable ControlRenderable;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_ux);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        float width = (float)getIntent().getDoubleExtra("width", 0) * 0.0254f;
        float height = (float)getIntent().getDoubleExtra("height", 0) * 0.0254f;
        float length = (float)getIntent().getDoubleExtra("length", 0) * 0.0254f;

        CompletableFuture<ViewRenderable> ControlStage = ViewRenderable.builder().setView(this, R.layout.activity_sceneform).build();

        CompletableFuture.allOf(ControlStage).handle(
                (notUsed, throwable) -> {

                    if (throwable != null) {
                        Utility.displayError(this, "Unable to load renderable", throwable);
                        return null;
                    }

                    try {
                        ControlRenderable = ControlStage.get();
                    }
                    catch (InterruptedException | ExecutionException ex) {
                        Utility.displayError(this, "Unable to load renderable", ex);
                    }

                    return null;
                });

        MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.RED))
                .thenAccept(
                        material -> {
                            cubeRenderable = ShapeFactory.makeCube(new Vector3(width, height, length), new Vector3(0, height/2, 0), material);
                        });

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (cubeRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(cubeRenderable);
                    Node base = new Node();
                    base.setParent(andy);
                    base.setRenderable(ControlRenderable);
                    View ControlsView = ControlRenderable.getView();
                    Button controlButton = ControlsView.findViewById(R.id.item_button);
                    controlButton.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {
                            andy.getTranslationController();
                        }
                    });
                });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        if (!Utility.hasCameraPermission(this)) {
            if (!Utility.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                Utility.launchPermissionSettings(this);
            } else {
                Toast.makeText(
                        this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                        .show();
            }
            finish();
        }
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}
