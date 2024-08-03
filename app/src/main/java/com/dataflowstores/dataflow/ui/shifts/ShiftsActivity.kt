package com.dataflowstores.dataflow.ui.shifts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.dataflowstores.dataflow.R
import com.dataflowstores.dataflow.ui.BaseActivity

class ShiftsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shifts)
        if (savedInstanceState == null) {
            val navController = findNavController(R.id.nav_host_fragment)
            supportActionBar?.hide()

            // Set navigation listener for handling navigation events
            navController.addOnDestinationChangedListener { _, destination, _ ->
                // Here, you can perform any additional logic based on the destination,
                // such as updating UI elements, handling back button visibility, etc.
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment)
        // Navigate up or call super method based on NavController
        if (!navController.navigateUp()) {
            super.onBackPressed()
        }
    }
}