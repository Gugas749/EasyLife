package com.alexandreconrado.easylife.fragments.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.databinding.FragmentRegisterDialogAccountBinding;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogNotifyFragment;
import com.alexandreconrado.easylife.scripts.CustomAlertDialogFragment;
import com.alexandreconrado.easylife.scripts.mailsending.SendMailTask;

import java.util.Random;

public class RegisterDialogAccountFragment extends Fragment {

    private FragmentRegisterDialogAccountBinding binding;
    private final RegisterFragment parent;
    private String codeEmail;

    public RegisterDialogAccountFragment(RegisterFragment parent) {
        this.parent = parent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterDialogAccountBinding.inflate(inflater);

        setupContinueButton();

        return binding.getRoot();
    }

    private void setupContinueButton(){
        binding.butContinueRegisterAccountDialogFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.butContinueRegisterAccountDialogFrag.setEnabled(false);
                if (binding.editTextEmailCodeRegisterAccountDialogFrag.getVisibility() == View.GONE) {
                    String email = binding.editTextEmailRegisterAccountDialogFrag.getText().toString().trim();
                    if (isValidEmail(email)) {
                        sendEmail(email);
                        PopUpNotifyEmailSent();
                    } else {
                        Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(codeEmail.equals(binding.editTextEmailCodeRegisterAccountDialogFrag.getText().toString())){
                        //TODO: Registrar a conta online
                        String email = binding.editTextEmailRegisterAccountDialogFrag.getText().toString().trim();
                        parent.changeDialogFragments(email);
                    }else{
                        binding.editTextEmailCodeRegisterAccountDialogFrag.setError(getString(R.string.register_dialog_account_email_code_wrong));
                    }
                }
                binding.butContinueRegisterAccountDialogFrag.setEnabled(true);
            }
        });
    }
    private boolean isValidEmail(CharSequence target) {
        return (target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private void sendEmail(String email) {
        String fromEmail = "hmtdiverification@yahoo.com";
        String fromPassword = "pugbvahsdlwyiuxt";
        String toEmail = email;
        String emailSubject = "Easy Life Account Creation";
        int min = 100000; // The minimum value of the range (inclusive)
        int max = 999999; // The maximum value of the range (inclusive)

        Random random = new Random();
        int randomNumber = random.nextInt((max - min) + 1) + min;
        String emailBody = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Email Verification</title><style>@import url('https://fonts.googleapis.com/css2?family=Dosis:wght@400&family=Arapey:wght@400&display=swap');body{font-family:'Arial',sans-serif;margin:0;padding:0;background-color:#f4f4f4;}.container{max-width:600px;margin:20px auto;background-color:#6A6ECF;padding:20px;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);position:relative;color:#ffffff;}.logo{max-width:60px;height:auto;border-radius:50%;margin-right:10px;}.content{text-align:left;}.verification-code{font-size:24px;font-weight:bold;color:#F493FB;text-align:center;}.footer{margin-top:20px;text-align:center;color:#8D93E3;}</style></head><body><div class=\"container\"><div><img class=\"logo\" src=\"https://media.discordapp.net/attachments/1117167988659998791/1191714890432380968/snapLogoWhite.png?ex=65a671fa&is=6593fcfa&hm=29e4cfabf6f0eb62607799a2cacc4c0f98cc98025b4c524a66cd3a8750baeca1&=&format=webp&quality=lossless\" alt=\"Company Logo\"><h2>Email Verification</h2></div><div class=\"content\"><p>Hello!! User,</p><p>Thanks for creating an account!! Please use the following verification code to enter the Application:</p><p class=\"verification-code\">" + randomNumber + "</p></div><div class=\"footer\"><p>If you did not sign up for this service, please ignore this email.</p></div></div></body></html>\n";

        SendMailTask sendMailTask = new SendMailTask(fromEmail, fromPassword, toEmail, emailSubject, emailBody);
        sendMailTask.execute();

        binding.editTextEmailCodeRegisterAccountDialogFrag.setVisibility(View.VISIBLE);
        codeEmail = Integer.toString(randomNumber);
    }
    private void PopUpNotifyEmailSent(){
        CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
        AlertDialogNotifyFragment fragment = new AlertDialogNotifyFragment(getString(R.string.alertDialog_Notify_EmailSend_Title), getString(R.string.alertDialog_Notify_EmailSend_Description), customAlertDialogFragment);
        customAlertDialogFragment.setCustomFragment(fragment);
        customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
    }
}