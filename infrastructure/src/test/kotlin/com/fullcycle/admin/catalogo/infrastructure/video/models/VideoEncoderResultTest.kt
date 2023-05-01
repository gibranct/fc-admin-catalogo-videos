package com.fullcycle.admin.catalogo.infrastructure.video.models

import com.fullcycle.admin.catalogo.JacksonTest
import com.fullcycle.admin.catalogo.domain.utils.IdUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester

@JacksonTest
class VideoEncoderResultTest {

    @Autowired
    lateinit var json: JacksonTester<VideoEncoderResult>

    @Test
    fun `test unmarshall success result`() {
        // given
        val expectedId = IdUtils.uuid()
        val expectedOutputBucketPath = "codeeducationtest"
        val expectedStatus = "COMPLETED"
        val expectedEncoderVideoFolder = "anyfolder"
        val expectedResourceId = IdUtils.uuid()
        val expectedFilePath = "any.mp4"
        val expectedMetadata = VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath)

        val jsonString = """
            {
                  "status": "$expectedStatus",
                  "id": "$expectedId",
                  "output_bucket_path": "$expectedOutputBucketPath",
                  "video": {
                    "encoded_video_folder": "$expectedEncoderVideoFolder",
                    "resource_id": "$expectedResourceId",
                    "file_path": "$expectedFilePath"
                  }
            }
        """.trimIndent()

        // when
        val actualResult = json.parse(jsonString)

        // then
        Assertions.assertThat(actualResult)
            .isInstanceOf(VideoEncoderCompleted::class.java)
            .hasFieldOrPropertyWithValue("id", expectedId)
            .hasFieldOrPropertyWithValue("outputBucketPath", expectedOutputBucketPath)
            .hasFieldOrPropertyWithValue("video", expectedMetadata)
    }

    @Test
    fun `test marshall success result`() {
        // given
        val expectedId = IdUtils.uuid()
        val expectedOutputBucketPath = "codeeducationtest"
        val expectedStatus = "COMPLETED"
        val expectedEncoderVideoFolder = "anyfolder"
        val expectedResourceId = IdUtils.uuid()
        val expectedFilePath = "any.mp4"
        val expectedMetadata = VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath)
        val aResult = VideoEncoderCompleted(expectedId, expectedOutputBucketPath, expectedMetadata)

        // when
        val actualResult = json.write(aResult)

        // then
        Assertions.assertThat(actualResult)
            .hasJsonPathValue("$.id", expectedId)
            .hasJsonPathValue("$.output_bucket_path", expectedOutputBucketPath)
            .hasJsonPathValue("$.status", expectedStatus)
            .hasJsonPathValue("$.video.encoded_video_folder", expectedEncoderVideoFolder)
            .hasJsonPathValue("$.video.resource_id", expectedResourceId)
            .hasJsonPathValue("$.video.encoded_video_folder", expectedFilePath)
    }

    @Test
    fun `test unmarshall success error result`() {
        // given
        val expectedMessage = "Resource not found"
        val expectedStatus = "ERROR"
        val expectedResourceId = IdUtils.uuid()
        val expectedFilePath = "any.mp4"
        val expectedVideoMessage = VideoMessage(expectedResourceId, expectedFilePath)

        val jsonString = """
            {
              "status": "$expectedStatus",
              "error": "$expectedMessage",
              "message": {
                "resource_id": "$expectedResourceId",
                "file_path": "$expectedFilePath"
              }
            }
        """.trimIndent()

        // when
        val actualResult = json.parse(jsonString)

        // then
        Assertions.assertThat(actualResult)
            .isInstanceOf(VideoEncoderError::class.java)
            .hasFieldOrPropertyWithValue("error", expectedMessage)
            .hasFieldOrPropertyWithValue("message", expectedVideoMessage)
    }

    @Test
    fun `test marshall success error result`() {
        // given
        val expectedMessage = "Resource not found"
        val expectedStatus = "ERROR"
        val expectedResourceId = IdUtils.uuid()
        val expectedFilePath = "any.mp4"
        val expectedVideoMessage = VideoMessage(expectedResourceId, expectedFilePath)
        val aResult = VideoEncoderError(expectedVideoMessage, expectedMessage)

        // when
        val actualResult = json.write(aResult)

        // then
        Assertions.assertThat(actualResult)
            .hasJsonPathValue("$.status", expectedStatus)
            .hasJsonPathValue("$.error", expectedMessage)
            .hasJsonPathValue("$.message.resource_id", expectedResourceId)
            .hasJsonPathValue("$.message.file_path", expectedFilePath);
    }
}