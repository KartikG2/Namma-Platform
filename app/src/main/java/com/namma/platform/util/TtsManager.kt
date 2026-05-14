package com.namma.platform.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.namma.platform.domain.model.Train
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.util.Locale
import javax.inject.Inject

@ActivityRetainedScoped
class TtsManager @Inject constructor(@dagger.hilt.android.qualifiers.ApplicationContext context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private val announcementQueue = mutableListOf<AnnouncementPart>()

    data class AnnouncementPart(val text: String, val locale: Locale, val rate: Float = 0.85f, val pitch: Float = 0.9f)

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.setPitch(0.9f) // Medium-low pitch
            tts?.setSpeechRate(0.85f) // 120-130 wpm roughly
            
            val result = tts?.setLanguage(Locale("kn", "IN"))
            isInitialized = !(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    playNextFromQueue()
                }
                override fun onError(utteranceId: String?) {}
            })
        }
    }

    private fun playNextFromQueue() {
        if (announcementQueue.isNotEmpty()) {
            val part = announcementQueue.removeAt(0)
            tts?.apply {
                language = part.locale
                setSpeechRate(part.rate)
                setPitch(part.pitch)
                speak(part.text, TextToSpeech.QUEUE_ADD, null, "ANN_PART")
                // Insert 500ms pause after sentence/part if needed
                playSilentUtterance(500, TextToSpeech.QUEUE_ADD, "PAUSE")
            }
        }
    }

    fun announceArrival(stationName: String, platformNo: Int) {
        if (!isInitialized) return
        announcementQueue.clear()

        val pfText = KannadaFormatter.formatNumber(platformNo)
        
        // Kannada Part: Greeting, Station (Emphasis), Details
        announcementQueue.add(AnnouncementPart("ಗಮನಿಸಿ. ಮುಂದಿನ ನಿಲ್ದಾಣ...", Locale("kn", "IN"), rate = 0.9f))
        announcementQueue.add(AnnouncementPart(stationName, Locale("kn", "IN"), rate = 0.75f, pitch = 0.85f))
        announcementQueue.add(AnnouncementPart("ಆಗಿದೆ. ಪ್ಲಾಟ್ಫಾರಂ ಸಂಖ್ಯೆ $pfText ರಲ್ಲಿ ಆಗಮಿಸಲಿದೆ. ದಯವಿಟ್ಟು ನಿಮ್ಮ ಸಾಮಾನುಗಳನ್ನು ಸಂಗ್ರಹಿಸಿ. ಅಡ್ಡ ಗೆರೆ ದಾಟಬೇಡಿ. ಧನ್ಯವಾದಗಳು.", Locale("kn", "IN"), rate = 0.9f))

        // Hindi Part
        announcementQueue.add(AnnouncementPart("कृपया ध्यान दें. अगला स्टेशन...", Locale("hi", "IN"), rate = 0.9f))
        announcementQueue.add(AnnouncementPart(stationName, Locale("hi", "IN"), rate = 0.75f, pitch = 0.85f))
        announcementQueue.add(AnnouncementPart("है. गाड़ी प्लेटफार्म नंबर $platformNo पर आएगी. धन्यवाद.", Locale("hi", "IN"), rate = 0.9f))

        // English Part
        announcementQueue.add(AnnouncementPart("Your attention please. Next station is...", Locale.ENGLISH, rate = 0.9f))
        announcementQueue.add(AnnouncementPart(stationName, Locale.ENGLISH, rate = 0.75f, pitch = 0.85f))
        announcementQueue.add(AnnouncementPart("Arriving on platform number $platformNo. Thank you.", Locale.ENGLISH, rate = 0.9f))

        playNextFromQueue()
    }

    fun announceDelay(train: Train) {
        if (!isInitialized) return
        announcementQueue.clear()

        val trainNoWords = KannadaFormatter.formatTrainNo(train.trainNo)
        val delayWords = KannadaFormatter.formatNumber(train.delayMinutes)

        // Kannada
        announcementQueue.add(AnnouncementPart("ಗಮನಿಸಿ. ರೈಲು ಸಂಖ್ಯೆ $trainNoWords,", Locale("kn", "IN")))
        announcementQueue.add(AnnouncementPart(train.trainNameKannada, Locale("kn", "IN"), rate = 0.8f))
        announcementQueue.add(AnnouncementPart("ಸುಮಾರು $delayWords ನಿಮಿಷ ತಡವಾಗಿ ಚಲಿಸುತ್ತಿದೆ. ಅಸೌಕರ್ಯಕ್ಕೆ ವಿಷಾದಿಸುತ್ತೇವೆ. ಧನ್ಯವಾದಗಳು.", Locale("kn", "IN")))

        // Hindi
        announcementQueue.add(AnnouncementPart("कृपया ध्यान दें. गाड़ी संख्या ${train.trainNo},", Locale("hi", "IN")))
        announcementQueue.add(AnnouncementPart(train.trainName, Locale("hi", "IN"), rate = 0.8f))
        announcementQueue.add(AnnouncementPart("करीब $delayWords मिनट देरी से चल रही है. असुविधा के लिए खेद है.", Locale("hi", "IN")))

        // English
        announcementQueue.add(AnnouncementPart("May I have your attention please. Train number ${train.trainNo},", Locale.ENGLISH))
        announcementQueue.add(AnnouncementPart(train.trainName, Locale.ENGLISH, rate = 0.8f))
        announcementQueue.add(AnnouncementPart("is running late by approximately $delayWords minutes. We regret the inconvenience caused.", Locale.ENGLISH))

        playNextFromQueue()
    }

    fun announcePlatform(train: Train, origin: String, destination: String) {
        if (!isInitialized) return
        announcementQueue.clear()

        val trainNoWords = KannadaFormatter.formatTrainNo(train.trainNo)
        val pfText = KannadaFormatter.formatNumber(train.platformNo)

        // Kannada
        announcementQueue.add(AnnouncementPart("ಗಮನಿಸಿ. ರೈಲು ಸಂಖ್ಯೆ $trainNoWords,", Locale("kn", "IN")))
        announcementQueue.add(AnnouncementPart("$origin ಇಂದ $destination ಗೆ ಹೊರಡುತ್ತಿದ್ದು,", Locale("kn", "IN"), rate = 0.85f))
        announcementQueue.add(AnnouncementPart("ಪ್ಲಾಟ್ಫಾರಂ ಸಂಖ್ಯೆ $pfText ರಲ್ಲಿ ಆಗಮಿಸಲಿದೆ. ಅಡ್ಡ ಗೆರೆ ದಾಟಬೇಡಿ. ಧನ್ಯವಾದಗಳು.", Locale("kn", "IN")))

        playNextFromQueue()
    }


    fun checkKannadaTtsAvailable(): Boolean = isInitialized

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}

