@file:Suppress("MemberVisibilityCanBePrivate", "ObjectLiteralToLambda")

package dev.entao.kan.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import dev.entao.kan.appbase.App
import dev.entao.kan.appbase.ex.Bmp
import dev.entao.kan.appbase.ex.saveJpg
import dev.entao.kan.appbase.ex.savePng
import dev.entao.kan.base.ex.lowerCased
import dev.entao.kan.dialogs.HorProgressDlg
import dev.entao.kan.dialogs.SpinProgressDlg
import dev.entao.kan.dialogs.dialogX
import dev.entao.kan.log.Yog
import dev.entao.kan.util.*
import dev.entao.kan.widget.RelativeLayoutX
import dev.entao.kan.widget.TabBar
import java.io.File
import kotlin.collections.set

/**
 * Created by entaoyang@163.com on 16/3/12.
 */


/**
 * 不要调用getActivity().finish(). 要调用finish(), finish处理了动画
 * fragment基类 公用方法在此处理
 */
open class BasePage : Fragment(), MsgListener {

    val uniqueName: String = "fragment${identity++}"

    lateinit var pageRootView: RelativeLayoutX
        private set

    @SuppressLint("UseSparseArrays")
    private val resultListeners = HashMap<Int, ActivityResultListener>(8)
    lateinit var spinProgressDlg: SpinProgressDlg
    lateinit var horProgressDlg: HorProgressDlg


    val watchMap = HashMap<Uri, ContentObserver>()


    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        pageRootView = RelativeLayoutX(act)
        onCreatePage(act, pageRootView, savedInstanceState)
        return pageRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onPageCreated()
    }

    open fun onCreatePage(context: Context, pageView: RelativeLayout, savedInstanceState: Bundle?) {

    }

    open fun onPageCreated() {

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Perm.onPermResult(requestCode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        spinProgressDlg = SpinProgressDlg(act)
        horProgressDlg = HorProgressDlg(act)
        MsgCenter.listenAll(this)
    }


    fun statusBarColor(color: Int) {
        val w = activity?.window ?: return
        if (Build.VERSION.SDK_INT >= 21) {
            w.statusBarColor = color
        }
    }


    override fun onResume() {
        super.onResume()
        if (!isHidden) {
            onShow()
        }
    }

    override fun onPause() {
        if (!isHidden) {
            onHide()
        }
        Yog.flush()
        super.onPause()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (isResumed) {
            if (hidden) onHide() else onShow()
        }
    }

    open fun onShow() {

    }

    open fun onHide() {

    }

    /**
     * 可见, 并且没锁屏

     * @return
     */
    val isVisiableToUser: Boolean
        get() = this.isResumed && isVisible

    fun takeViedo(sizeM: Int, block: (Uri) -> Unit) {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, sizeM * 1024 * 1024)
        val onResult = object : ActivityResultListener {
            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Boolean {
                if (resultCode == Activity.RESULT_OK) {
                    if (data.data != null) {
                        block.invoke(data.data!!)
                    }
                }
                return true
            }

        }
        startActivityForResult(TAKE_VIDEO, intent, onResult)
    }

    fun pickVideo(block: (Uri) -> Unit) {
        val i = Intent(Intent.ACTION_PICK)
        i.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*")
        val onResult = object : ActivityResultListener {
            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Boolean {
                if (resultCode == Activity.RESULT_OK) {
                    if (data.data != null) {
                        block.invoke(data.data!!)
                    }
                }
                return true
            }
        }
        startActivityForResult(PICK_PHOTO, i, onResult)
    }

    fun selectImage(width: Int, block: (Uri) -> Unit) {
        this.dialogX.showListItem(listOf("拍照", "相册"), null) {
            if (it == "拍照") {
                takePhotoJpg(width) { fff ->
                    block(Uri.fromFile(fff))
                }
            } else {
                pickPhoto(width) { uu ->
                    block(uu)
                }
            }
        }
    }

    fun pickJpg(width: Int, block: (File) -> Unit) {
        val i = Intent(Intent.ACTION_PICK)
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        val onResult = object : ActivityResultListener {
            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Boolean {
                if (resultCode == Activity.RESULT_OK) {
                    if (data.data != null) {
                        val outputFile = App.files.ex.temp("" + System.currentTimeMillis() + ".jpg")
                        val bmp = Bmp.uri(data.data, width, Bitmap.Config.ARGB_8888)
                        if (bmp != null) {
                            bmp.saveJpg(outputFile)
                            block.invoke(outputFile)
                        }
                    }
                }
                return true
            }
        }
        startActivityForResult(PICK_PHOTO, i, onResult)
    }

    fun pickPhoto(width: Int, block: (Uri) -> Unit) {
        val i = Intent(Intent.ACTION_PICK)
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        val onResult = object : ActivityResultListener {
            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Boolean {
                if (resultCode == Activity.RESULT_OK) {
                    if (data.data != null) {
                        val f = App.files.ex.tempFile("PNG")
                        val bmp = Bmp.uri(data.data, width, Bitmap.Config.ARGB_8888)
                        if (bmp != null) {
                            bmp.savePng(f)
                            if (f.exists()) {
                                block.invoke(Uri.fromFile(f))
                            }
                        }

                    }
                }
                return true
            }
        }
        startActivityForResult(PICK_PHOTO, i, onResult)
    }

    fun takePhotoPng(width: Int, block: (File) -> Unit) {
        takePhoto(width, true, block)
    }

    fun takePhotoJpg(width: Int, block: (File) -> Unit) {
        takePhoto(width, false, block)
    }

    fun takePhoto(width: Int, png: Boolean, block: (File) -> Unit) {
        val fmt = if (png) "PNG" else "JPEG"
        val outputFile = App.files.ex.temp("" + System.currentTimeMillis() + "." + fmt)
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0)
        val outUri = UriFromSdFile(outputFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri)
        intent.putExtra("outputFormat", fmt)
        val onResult = object : ActivityResultListener {
            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Boolean {
                if (resultCode == Activity.RESULT_OK && outputFile.exists()) {
                    val f = App.files.ex.tempFile(fmt.lowerCased)
                    val bmp = Bmp.file(outputFile, width, Bitmap.Config.ARGB_8888)
                    if (bmp != null) {
                        if (png) {
                            bmp.savePng(f)
                        } else {
                            bmp.saveJpg(f)
                        }
                        if (f.exists()) {
                            block(f)
                        }
                    }
                }
                return true
            }
        }
        startActivityForResult(TAKE_PHOTO, intent, onResult)
    }


    fun cropPhoto(uri: Uri, outX: Int, outY: Int, result: (Bitmap?) -> Unit) {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outX)
        intent.putExtra("outputY", outY)
        intent.putExtra("return-data", true)
        // intent.putExtra("output",CAMERA_EXTRA_OUTPUT_FILE);
        val onResult = object : ActivityResultListener {
            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Boolean {
                if (resultCode == Activity.RESULT_OK) {
                    val extras = data.extras
                    var photo: Bitmap? = null
                    if (extras != null) {
                        photo = extras.getParcelable("data")
                    }
                    result.invoke(photo)
                } else {
                    result.invoke(null)
                }
                return true
            }
        }
        startActivityForResult(CROP_PHOTO, intent, onResult)
    }

    fun startActivityForResult(requestCode: Int, intent: Intent, onResult: ActivityResultListener) {
        resultListeners[requestCode] = onResult
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        resultListeners.remove(requestCode)?.onActivityResult(requestCode, resultCode, data!!)
        super.onActivityResult(requestCode, resultCode, data)
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    fun finish() {
        val a = activity ?: return
        if (a is StackActivity) {
            a.pop()
        } else {
            a.finish()
        }
    }

    fun findTabBar(): TabBar? {
        for (f in this.fragMgr.fragments) {
            if (f is TabBarPage) {
                return f.tabBar
            }
        }
        return null
    }


    open fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return false
    }


    fun unWatch(uri: Uri) {
        val ob = watchMap[uri]
        if (ob != null) {
            act.contentResolver.unregisterContentObserver(ob)
        }
    }

    fun watch(uri: Uri, block: (Uri) -> Unit = {}) {
        if (watchMap.containsKey(uri)) {
            return
        }
        val ob = object : ContentObserver(Handler(Looper.getMainLooper())) {

            override fun onChange(selfChange: Boolean, uri: Uri) {
                mergeAction("watchUri:$uri") {
                    block(uri)
                    onUriChanged(uri)
                }
            }
        }
        watchMap[uri] = ob
        act.contentResolver.registerContentObserver(uri, true, ob)
    }

    open fun onUriChanged(uri: Uri) {

    }


    override fun onDestroy() {
        for (ob in watchMap.values) {
            act.contentResolver.unregisterContentObserver(ob)
        }
        watchMap.clear()
        MsgCenter.remove(this)
        super.onDestroy()
    }

    override fun onMsg(msg: Msg) {
    }


    companion object {
        private const val TAKE_PHOTO = 988
        private const val TAKE_VIDEO = 989
        private const val PICK_PHOTO = 977
        private const val CROP_PHOTO = 966

        private var identity: Int = 1
    }

}
