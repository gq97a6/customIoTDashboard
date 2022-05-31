package com.alteratom.dashboard

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.alteratom.R
import com.alteratom.databinding.DialogConfirmBinding

object DialogBuilder {
    fun Context.buildConfirm(
        message: String,
        label: String,
        onConfirm: () -> Unit,
        onDeny: () -> Unit = {}
    ) {
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.dialog_confirm)
        val binding = DialogConfirmBinding.bind(dialog.findViewById(R.id.root))

        binding.dcConfirm.setOnClickListener {
            onConfirm()
            dialog.dismiss()
        }

        binding.dcDeny.setOnClickListener {
            onDeny()
            dialog.dismiss()
        }

        binding.dcConfirm.text = label
        binding.dcText.text = message

        dialog.dialogSetup()
        G.theme.apply(binding.root)
        dialog.show()
    }

    fun Dialog.dialogSetup() {
        val a = this.window?.attributes
        a?.dimAmount = 0.9f
        this.window?.attributes = a
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        this.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }
}

@Composable
fun Context.dialogConfirmCompose() {
    Dialog(onDismissRequest = { }) {
        ComposeView(this).apply {
            setContent {
                Text("TEST", fontSize = 40.sp, color = Theme.colors.a)
            }
        }
    }
}