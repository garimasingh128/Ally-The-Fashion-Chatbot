package `in`.codepredators.vedanta

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager {
    companion object {
        @JvmStatic
        fun getSettings(context: Context): Settings {
            val sharedPref = context.getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", 0)
            val settings = Settings()
            if(!sharedPref.getString("clinic_name", "NONE").equals("NONE"))
                settings.clinicName = sharedPref.getString("clinic_name", "NONE")!!
            if(!sharedPref.getString("clinic_address", "NONE").equals("NONE"))
                settings.clinicAddress = sharedPref.getString("clinic_address", "NONE")!!
            settings.audioModeEnabled = sharedPref.getBoolean("voice_setting", false)
            settings.audioResponseEnabled = sharedPref.getBoolean("audio_output", false)
            settings.exportToPdf = sharedPref.getBoolean("export", false)
            settings.customPrescriptionHeader = sharedPref.getBoolean("custom_header", false)
            settings.darkTheme = sharedPref.getBoolean("theme", false)
            settings.prescriptionHeaderProvided = sharedPref.getBoolean("header", false)
            settings.diagnosesModuleAvailable = sharedPref.getBoolean("diagnosesAvailable", false)
            settings.medicationModuleAvailable = sharedPref.getBoolean("medicationAvailable", false)
            settings.diagnosesModuleRequested = sharedPref.getStringSet("ml_models", null)?.contains("Diagnoses Recommendation Module")!!
            settings.medicationModuleRequested = sharedPref.getStringSet("ml_models", null)?.contains("Medication Recommendation Module")!!
            return settings
        }
        @JvmStatic
        fun setId(context: Context,clinicId: String){
            val sharedPref = context.getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", 0)
            val editor = sharedPref.edit()
            editor.putString("clinic_id",clinicId);
            editor.commit()
        }
        @JvmStatic
        fun getId(context: Context): String {
            val sharedPref = context.getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", 0)
            var id = sharedPref.getString("clinic_id","NONE").toString()
            return id
        }
    }

    class Settings {
        var darkTheme = false
        var audioResponseEnabled = false
        var audioModeEnabled = false
        var exportToPdf = false
        var prescriptionHeaderProvided = false
        var customPrescriptionHeader = false
        var diagnosesModuleRequested = false
        var diagnosesModuleAvailable = false
        var medicationModuleRequested = false
        var medicationModuleAvailable = false
        lateinit var clinicName: String
        lateinit var clinicAddress: String
    }
}

