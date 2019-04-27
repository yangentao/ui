package dev.entao.util

import java.io.*
import java.nio.charset.Charset

/**
 * Created by entaoyang@163.com on 16/7/20.
 */
object StreamUtil {
    /**
     * 会close inputstream
     *
     * @param inStream
     * @return
     */
    @Throws(IOException::class)
    fun readBytes(inStream: InputStream): ByteArray? {
        val os = ByteArrayOutputStream(4096)
        StreamUtil.copyStream(inStream, os)
        return os.toByteArray()
    }

    /**
     * 会关闭输入流
     *
     * @param inStream
     * @param encoding
     * @return
     */
    @Throws(IOException::class)
    fun readString(inStream: InputStream, encoding: String): String? {
        val data = readBytes(inStream)
        return if (data == null) null else String(data, Charset.forName(encoding))
    }

    /**
     * @throws IOException
     */
    @Throws(IOException::class)
    fun copyStream(`is`: InputStream, os: OutputStream) {
        copyStream(`is`, true, os, true)
    }

    @Throws(IOException::class)
    fun copyStream(`is`: InputStream, os: OutputStream, close: Boolean) {
        copyStream(`is`, close, os, close)
    }

    @Throws(IOException::class)
    fun copyStream(inStream: InputStream, closeIn: Boolean, os: OutputStream, closeOut: Boolean) {
        try {
            val buffer = ByteArray(4096)
            do {
                val n = inStream.read(buffer)
                if (n != -1) {
                    os.write(buffer, 0, n)
                } else {
                    break
                }
            } while (true)

            os.flush()
        } finally {
            if (closeIn) {
                close(inStream)
            }
            if (closeOut) {
                close(os)
            }
        }
    }

    fun close(c: Closeable?) {
        if (c != null) {
            try {
                c.close()
            } catch (ex: Exception) {
                //				ex.printStackTrace();
            }

        }
    }

}
