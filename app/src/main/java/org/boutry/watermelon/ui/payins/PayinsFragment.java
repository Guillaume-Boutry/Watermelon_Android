package org.boutry.watermelon.ui.payins;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.boutry.watermelon.Constants;
import org.boutry.watermelon.R;
import org.boutry.watermelon.data.LoginDataSource;
import org.boutry.watermelon.data.LoginRepository;
import org.boutry.watermelon.data.Result;
import org.boutry.watermelon.data.model.NotLoggedInException;
import org.boutry.watermelon.data.model.PayIn;
import org.boutry.watermelon.rest.LoggedInRequest;
import org.boutry.watermelon.rest.Request;
import org.boutry.watermelon.rest.RestAsync;
import org.boutry.watermelon.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class PayinsFragment extends Fragment {

    private Handler handler;
    private LinearLayout payinsFragment;
    private PayinsViewModel payinsViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_payins, container, false);
        PayinsViewModel payinsViewModel = ViewModelProviders.of(this).get(PayinsViewModel.class);
        payinsViewModel.getPayins().observe(this, payins -> {
            // update UI
            removeFragments();
            createFragments(payins);
        });

        return root;
    }

    private void createFragments(List<PayIn> payins) {
        for(PayIn payIn : payins) {
            PayinFragment frag = PayinFragment.newInstance();
            frag.setPayIn(payIn);
            getChildFragmentManager().beginTransaction().add(payinsFragment.getId(), frag).commit();
        }
    }

    private void removeFragments() {
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            getChildFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        payinsFragment = view.findViewById(R.id.payinsLayout);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(payinsViewModel != null) {
            removeFragments();
            createFragments(payinsViewModel.getPayins().getValue());
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


}