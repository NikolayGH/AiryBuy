package com.prog_edu.airybuy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.content.Context;

import androidx.annotation.Nullable;
import android.app.Fragment;
import androidx.core.app.ActivityCompat;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import java.io.IOException;

public class QrScannerFragment extends Fragment {

    private Listener listener;
    private String code;

    private SurfaceView surfaceView;


    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private EditText barcodeText;
    private String barcodeData;

    public static QrScannerFragment newInstance() {
        Bundle args = new Bundle();
        QrScannerFragment fragment = new QrScannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Listener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_scanner, container, false);

        surfaceView =  view.findViewById(R.id.surface_view);
        barcodeText =  view.findViewById(R.id.enter_code_edit_text);



        Button addProductToListButton = view.findViewById(R.id.add_product_to_list_button);
        addProductToListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addProductToList(code);
            }
        });
        //initialiseDetectorsAndSources();
        barcodeDetector = new BarcodeDetector.Builder(getActivity().getApplicationContext())
                .setBarcodeFormats(Barcode.EAN_13 | Barcode.EAN_8 | Barcode.UPC_A | Barcode.UPC_E | Barcode.CODE_39 | Barcode.CODE_128 | Barcode.ITF | Barcode.CODABAR)
                .build();

        if(!barcodeDetector.isOperational()){
            Toast.makeText(getActivity(), "barcodeDetector.isNOTOperational", Toast.LENGTH_SHORT).show();
        }

        cameraSource = new CameraSource.Builder(getActivity(), barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try{
                        cameraSource.start(holder);
                        Toast.makeText(getActivity(), "cameraSource.start(holder)", Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else{
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    Toast.makeText(getActivity(), "!cameraSource.start(holder)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }

        });


        return view;
    }

    public interface Listener{
        void addProductToList(String code);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

//    private void initialiseDetectorsAndSources() {
//
//
//
//        barcodeDetector = new BarcodeDetector.Builder(getActivity())
//                .setBarcodeFormats(Barcode.ALL_FORMATS)
//                .build();
//
//        cameraSource = new CameraSource.Builder(getActivity(), barcodeDetector)
//                .setRequestedPreviewSize(1920, 1080)
//                .setAutoFocusEnabled(true) //you should add this feature
//                .build();
//
//        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                try {
//                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                        cameraSource.start(surfaceView.getHolder());
//                    } else {
//                        ActivityCompat.requestPermissions(getActivity(), new
//                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                cameraSource.stop();
//            }
//        });
//
//
//        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
//            @Override
//            public void release() {
//                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void receiveDetections(Detector.Detections<Barcode> detections) {
//                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
//                if (barcodes.size() != 0) {
//
//
//                    barcodeText.post(new Runnable() {
//
//                        @Override
//                        public void run() {
//
//                            if (barcodes.valueAt(0).email != null) {
//                                barcodeText.removeCallbacks(null);
//                                barcodeData = barcodes.valueAt(0).email.address;
//                                barcodeText.setText(barcodeData);
//                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
//                            } else {
//
//                                barcodeData = barcodes.valueAt(0).displayValue;
//                                barcodeText.setText(barcodeData);
//                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
//
//                            }
//                        }
//                    });
//                }
//            }
//        });
//    }


    @Override
    public void onResume() {
        super.onResume();
        //initialiseDetectorsAndSources();





        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

//            @Override
//            public void receiveDetections(Detector.Detections<Barcode> detections) {
//                Toast.makeText(getActivity(), "receiveDetections", Toast.LENGTH_SHORT).show();
//
//                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
//                if(barcodes.size()!=0){
//
//                    barcodeText.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//                            vibrator.vibrate(1000);
//                            barcodeText.setText(barcodes.valueAt(0).displayValue);
//                        }
//                    });
//                }
//            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeText.setText(barcodeData);

//                    barcodeText.post(new Runnable() {
//
//                        @Override
//                        public void run() {
//
//                            if (barcodes.valueAt(0).email != null) {
//                                barcodeText.removeCallbacks(null);
//                                barcodeData = barcodes.valueAt(0).email.address;
//                                barcodeText.setText(barcodeData);
//                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
//                            } else {
//
//                                barcodeData = barcodes.valueAt(0).displayValue;
//                                barcodeText.setText(barcodeData);
//                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
//
//                            }
//                        }
//                    });
                }
            }
        });
    }




    @Override
    public void onPause() {
        super.onPause();
        //cameraSource.release();
    }


//    class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
//        @Override
//        public Tracker<Barcode> create(Barcode barcode) {
//            return new MyBarcodeTracker();
//        }
//    }

//    class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
//
//        public Tracker<Barcode> create(Barcode barcode) {
//            BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay);
//            return new GraphicTracker<>(mGraphicOverlay, graphic);
//        }
//    }
//
//    class BarcodeGraphic extends TrackedGraphic<Barcode> {
//
//        public void draw(Canvas canvas) {
//            canvas.drawRect(rect, mRectPaint);
//            canvas.drawText(barcode.rawValue, rect.left, rect.bottom, mTextPaint);
//        }
//    }

    class MyBarcodeTracker extends Tracker<Barcode> {
        @Override
        public void onUpdate(Detector.Detections<Barcode> detectionResults, Barcode barcode) {
            Toast.makeText(getActivity(), "receiveDetections", Toast.LENGTH_SHORT).show();
        }
    }


}
