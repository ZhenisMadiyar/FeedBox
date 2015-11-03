package kz.smf.feedbook.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import kz.smf.feedbook.tabs.scanner_result.ScanQRCode;

/**
 * Created by madiyarzhenis on 31.08.15.
 */
public class Scanner extends Fragment {
    Button scan;
    RelativeLayout layoutScan;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(kz.smf.feedbook.R.layout.qr_fragment, container, false);

        scan = (Button) view.findViewById(kz.smf.feedbook.R.id.buttonScan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScanQRCode.class));
            }
        });
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}