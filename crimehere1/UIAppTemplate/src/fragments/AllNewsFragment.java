package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.csform.android.uiapptemplate.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllNewsFragment extends Fragment {



   



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout linearLayout;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllNews.
     */
    // TODO: Rename and change types and number of parameters
    public static AllNewsFragment newInstance(String param1, String param2) {
        AllNewsFragment fragment = new AllNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
     Context context;
    public AllNewsFragment() {
        // Required empty public constructor
    }
     public AllNewsFragment(Context context)
     {
         this.context=context;
     }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_all_news, container, false);
         linearLayout=(LinearLayout)view.findViewById(R.id.linearLayout2);
      

        return view;
    }
  

   

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
       // nMgr.cancelAll();
    }



   

}
