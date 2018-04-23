package com.csform.android.uiapptemplate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by Ten-Thinlay on 4/21/15.
 */
public class MyFragment extends Fragment {
    private TextView t;
    public static MyFragment getInstance(int position){
        MyFragment myFragment=new MyFragment();
        Bundle args=new Bundle();
        args.putInt("position",position);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.my_fragment,container,false);
        t=(TextView)layout.findViewById(R.id.fragmentTextView);
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            t.setText("this is position:"+bundle.getInt("position"));
        }
      
        return layout;
    }
}

