package dev.entao.utilapp

import android.content.Context
import android.os.Bundle
import android.widget.RelativeLayout
import dev.entao.kan.base.BasePage
import dev.entao.kan.creator.textView
import dev.entao.kan.ext.CenterInParent
import dev.entao.kan.ext.RParam
import dev.entao.kan.ext.wrap

class APage : BasePage() {

    override fun onCreatePage(context: Context, pageView: RelativeLayout, savedInstanceState: Bundle?) {
        super.onCreatePage(context, pageView, savedInstanceState)
        pageView.textView(RParam.CenterInParent.wrap()) {
            text = "APage "
        }
    }
}


class BPage : BasePage() {

    override fun onCreatePage(context: Context, pageView: RelativeLayout, savedInstanceState: Bundle?) {
        super.onCreatePage(context, pageView, savedInstanceState)
        pageView.textView(RParam.CenterInParent.wrap()) {
            text = "BB "
        }
    }
}