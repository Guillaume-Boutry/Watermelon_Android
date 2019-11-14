package org.boutry.watermelon.ui.payins;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.boutry.watermelon.R;
import org.boutry.watermelon.data.model.PayIn;
import org.boutry.watermelon.databinding.FragmentPayinBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PayinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayinFragment extends Fragment {

    private PayIn payIn;

    public PayinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PayinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PayinFragment newInstance() {
        PayinFragment fragment = new PayinFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentPayinBinding binding = FragmentPayinBinding.inflate(inflater, container, false);
        binding.setPayin(this.payIn);
        return binding.getRoot();
    }

    public void setPayIn(PayIn payIn) {
        this.payIn = payIn;
    }
}
