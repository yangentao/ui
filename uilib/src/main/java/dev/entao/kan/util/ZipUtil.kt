package dev.entao.kan.util


import dev.entao.kan.appbase.ex.Asset

import java.io.*
import java.nio.charset.Charset
import java.util.zip.*

object ZipUtil {
    /**
     * 只解压第一个文件
     *
     * @param zis
     * @param toDir
     * @throws IOException
     */
    @Throws(IOException::class)
    fun unzipOneToDir(zis: ZipInputStream, toDir: File) {
        var os: OutputStream? = null
        try {
            val e = zis.nextEntry
            if (e != null) {
                os = FileOutputStream(File(toDir, e.name))
                StreamUtil.copyStream(zis, false, os, true)
                zis.closeEntry()
            }

        } finally {
            close(os)
            close(zis)
        }
    }

    fun close(c: Closeable?) {
        try {
            c?.close()
        } catch (e: Exception) {
        }

    }

    @Throws(IOException::class)
    fun unzipOneToFile(zis: ZipInputStream, toFile: File) {
        var os: OutputStream? = null
        try {
            val e = zis.nextEntry
            if (e != null) {
                os = FileOutputStream(toFile)
                StreamUtil.copyStream(zis, false, os, true)
                zis.closeEntry()
            }

        } finally {
            close(os)
            close(zis)
        }
    }

    @Throws(IOException::class)
    fun unzipAssetOneToDir(assetFile: String, toDir: File) {
        val zis = Asset.streamZip(assetFile)
        unzipOneToDir(zis, toDir)
    }

    @Throws(IOException::class)
    fun unzipAssetOneToFile(assetFile: String, toFile: File) {
        val zis = Asset.streamZip(assetFile)
        unzipOneToFile(zis, toFile)
    }

    @Throws(IOException::class)
    fun zip(data: ByteArray, filename: String, zipFile: File) {
        val fos = FileOutputStream(zipFile)
        val zos = ZipOutputStream(fos)
        zos.putNextEntry(ZipEntry(filename))
        try {
            zos.write(data)
            zos.closeEntry()
            zos.flush()
            fos.flush()
        } finally {
            close(zos)
            close(fos)
        }
    }

    @Throws(IOException::class)
    fun zip(from: File, zipFile: File) {
        val fis = FileInputStream(from)
        val fos = FileOutputStream(zipFile)
        val zos = ZipOutputStream(fos)
        zos.putNextEntry(ZipEntry(from.name))
        try {
            StreamUtil.copyStream(fis, zos, false)
            zos.closeEntry()
            zos.flush()
            fos.flush()
        } finally {
            close(zos)
            close(fos)
            close(fis)
        }
    }

    @Throws(IOException::class)
    fun gzip(data: ByteArray, zipFile: File) {
        val fos = FileOutputStream(zipFile)
        val zos = GZIPOutputStream(fos)
        try {
            zos.write(data)
            zos.finish()
            zos.flush()
            fos.flush()
        } finally {
            close(zos)
            close(fos)
        }
    }

    @Throws(IOException::class)
    fun gzip(from: File, zipFile: File) {
        val fis = FileInputStream(from)
        val fos = FileOutputStream(zipFile)
        val zos = GZIPOutputStream(fos)
        try {
            StreamUtil.copyStream(fis, zos, false)
            zos.finish()
            zos.flush()
            fos.flush()
        } finally {
            close(zos)
            close(fos)
            close(fis)
        }
    }

    @Throws(Exception::class)
    fun readGzipUtf8(gzipFile: File): String? {
        val buffer = readGzip(gzipFile)
        return if (buffer == null) null else String(buffer, Charset.forName("UTF-8"))
    }

    @Throws(IOException::class)
    fun readGzip(gzipFile: File): ByteArray? {
        val fis = FileInputStream(gzipFile)
        val zis = GZIPInputStream(fis)
        return StreamUtil.readBytes(zis)
    }

    @Throws(IOException::class)
    fun unGzip(gzipFile: File, toFile: File) {
        val fis = FileInputStream(gzipFile)
        val zis = GZIPInputStream(fis)
        val fos = FileOutputStream(toFile)
        StreamUtil.copyStream(zis, fos, true)
    }

}
