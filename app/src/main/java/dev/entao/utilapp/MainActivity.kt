package dev.entao.utilapp

import android.os.Bundle
import dev.entao.ui.base.ContainerActivity

class MainActivity : ContainerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        push(HelloPage())
    }

}


