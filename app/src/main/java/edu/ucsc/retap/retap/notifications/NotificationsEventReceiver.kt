package edu.ucsc.retap.retap.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.telephony.SmsMessage
import edu.ucsc.retap.retap.messages.model.Message
import edu.ucsc.retap.retap.vibration.VibrationInteractor

class NotificationsEventReceiver: BroadcastReceiver() {
    companion object {
        private const val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
        private const val PDUS_KEY = "pdus"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        when (intent.action) {
            SMS_RECEIVED -> {
                val extras = intent.extras ?: return
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                val vibrationInteractor = VibrationInteractor(vibrator)
                val pdus = extras.get(PDUS_KEY) as Array<*>
                val message = Message.createFrom(SmsMessage.createFromPdu(pdus[0] as ByteArray))
                vibrationInteractor.vibrate(message)
            }
        }
    }
}
