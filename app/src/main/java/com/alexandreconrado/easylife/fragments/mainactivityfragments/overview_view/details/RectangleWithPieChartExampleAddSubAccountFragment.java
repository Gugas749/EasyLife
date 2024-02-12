package com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.databinding.FragmentRectangleWithPieChartExampleAddSubAccountBinding;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;
import com.alexandreconrado.easylife.scripts.CustomAlertDialogFragment;

public class RectangleWithPieChartExampleAddSubAccountFragment extends Fragment implements CustomAlertDialogFragment.CancelButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogQuestionFrag {

    private FragmentRectangleWithPieChartExampleAddSubAccountBinding binding;
    private RectangleWithPieChartExampleAddSubAccountFragment THIS;
    private ConfirmAlertDialogFragRectangleWithPieChartExampleAddSubAccount listenner;
    private int index = 0;
    public interface ConfirmAlertDialogFragRectangleWithPieChartExampleAddSubAccount{
        void onConfirmAlertDialogFragRectangleWithPieChartExampleAddSubAccount(int index);
    }
    public void setConfirmAlertDialogFragRectangleWithPieChartExampleAddSubAccountListenner(ConfirmAlertDialogFragRectangleWithPieChartExampleAddSubAccount listenner){
        this.listenner = listenner;
    }

    public RectangleWithPieChartExampleAddSubAccountFragment() {

    }
    public RectangleWithPieChartExampleAddSubAccountFragment(int index) {
        this.index = index;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRectangleWithPieChartExampleAddSubAccountBinding.inflate(inflater);

        THIS = this;
        binding.imageViewAddSubAccountFragRectangleWithPieChartExampleAddSubAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                customAlertDialogFragment.setCancelListenner(THIS);
                customAlertDialogFragment.setConfirmListenner(THIS);
                AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsForm_AlertDialog_Question_AddSubAccount_Title), getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsForm_AlertDialog_Question_AddSubAccount_Text), customAlertDialogFragment, customAlertDialogFragment, "2");
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.setTag("FragACOverviewViewSpendingAccountDetailsForm");
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onConfirmButtonClicked(String Tag) {
        if (Tag.equals("FragACOverviewViewSpendingAccountDetailsForm")){
            listenner.onConfirmAlertDialogFragRectangleWithPieChartExampleAddSubAccount(index);
        }
    }

    @Override
    public void onCancelButtonClicked(String Tag) {

    }
}