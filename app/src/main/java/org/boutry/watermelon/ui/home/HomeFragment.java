package org.boutry.watermelon.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.boutry.watermelon.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView balance = root.findViewById(R.id.text_balance);
        final String balanceLocale = getResources().getString(R.string.watermelon_balance);
        homeViewModel.getBalance().observe(this, (wallet -> {
            balance.setText(String.format("%s %s€", balanceLocale, wallet.getBalance()));
        }));
        return root;
    }

}