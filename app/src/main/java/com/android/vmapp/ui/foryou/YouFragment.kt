package com.android.vmapp.ui.foryou

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.vmapp.ui.about.AboutActivity
import com.android.vmapp.ui.billing.BillingActivity
import com.android.vmapp.ui.points.FreeVipActivity
import com.android.vmapp.ui.settings.SettingsActivity
import com.clone.android.dual.space.R
import com.phantom.killer.disabler.DisablerLauncher

class YouFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_you, container, false)

        view.findViewById<View>(R.id.account_header)?.visibility = View.GONE

        view.findViewById<View>(R.id.subscription_card)?.setOnClickListener {
            startActivity(Intent(requireContext(), BillingActivity::class.java))
        }

        view.findViewById<View>(R.id.free_vip)?.setOnClickListener {
            startActivity(Intent(requireContext(), FreeVipActivity::class.java))
        }

        view.findViewById<View>(R.id.settings)?.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        view.findViewById<View>(R.id.adb_activate)?.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                DisablerLauncher.launch(requireContext())
            }
        }

        view.findViewById<View>(R.id.feedback)?.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:virtualmaster.vip@gmail.com"))
                startActivity(intent)
            } catch (_: Throwable) {
            }
        }

        view.findViewById<View>(R.id.about)?.setOnClickListener {
            startActivity(Intent(requireContext(), AboutActivity::class.java))
        }

        return view
    }
}
