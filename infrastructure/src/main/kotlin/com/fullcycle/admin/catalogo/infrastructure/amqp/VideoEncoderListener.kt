package com.fullcycle.admin.catalogo.infrastructure.amqp

import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusCommand
import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase
import com.fullcycle.admin.catalogo.domain.video.MediaStatus
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoEncoderCompleted
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoEncoderError
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoEncoderResult
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component


@Component
class VideoEncoderListener(
    private val updateMediaStatusUseCase: UpdateMediaStatusUseCase,

) {

    companion object {
        private const val LISTENER_ID = "videoEncodedListener"
        private val log = LoggerFactory.getLogger(VideoEncoderListener::class.java)
    }


    @RabbitListener(id = LISTENER_ID, queues = ["\${amqp.queues.video-encoded.queue}"])
    fun onVideoEncodedMessage(@Payload message: String) {
        val aResult = Json.readValue(message, VideoEncoderResult::class.java)

        if (aResult is VideoEncoderCompleted) {
            log.error("[message:video.listener.income] [status:completed] [payload:{}]", message)
            val aCmd = UpdateMediaStatusCommand(
                MediaStatus.COMPLETED,
                aResult.id,
                aResult.video.resourceId,
                aResult.video.encodedVideoFolder,
                aResult.video.filePath
            )
            updateMediaStatusUseCase.execute(aCmd)
        } else if (aResult is VideoEncoderError) {
            log.error("[message:video.listener.income] [status:error] [payload:$message]");
        }  else {
            log.error("[message:video.listener.income] [status:unknown] [payload:$message]");
        }
    }

}