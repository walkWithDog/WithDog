package com.teamsparta.withdog.infra.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.*

@Service
class S3Service(
    private val amazonS3Client: AmazonS3
) {

    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    var dir: String = "image/"

    @Throws(IOException::class)
    fun upload(file: MultipartFile): String {
        val extension = file.originalFilename?.let { validateFileExtension(it) }
        val fileName = UUID.randomUUID().toString() + "-" + file.originalFilename

        val objMeta = ObjectMetadata()

        val bytes = IOUtils.toByteArray(file.inputStream)

        objMeta.contentType = dir + extension
        objMeta.contentLength = bytes.size.toLong()

        val byteArrayIs = ByteArrayInputStream(bytes)

        amazonS3Client.putObject(
            PutObjectRequest(bucket, dir + fileName, byteArrayIs, objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead))

        return amazonS3Client.getUrl(bucket, dir + fileName).toString()
    }

    fun delete(fileName: String) {
        amazonS3Client.deleteObject(bucket, fileName)
    }

    private fun validateFileExtension(fileName: String): String {
        val extensionIndex = fileName.lastIndexOf('.')

        val extension = fileName.substring(extensionIndex + 1).toLowerCase()
        val allowedExtensionList = arrayOf("jpg", "jpeg", "png", "gif")

        if (!allowedExtensionList.contains(extension)) {
            throw IllegalArgumentException("Invalid extension: $extension")
        }
        return fileName.substring(extensionIndex + 1)
    }

}