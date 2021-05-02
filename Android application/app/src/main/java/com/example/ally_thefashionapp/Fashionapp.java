package in.codepredators.vedanta;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import in.codepredators.vedanta.room.VedantaDB;

public class Vedanta extends Application {
    
    public static VedantaDB vedantaDB;
    public static VedantaDB getDatabase(Context context){
        if(vedantaDB == null)
            vedantaDB = Room.databaseBuilder(context, VedantaDB.class, "doctor,medicine,patient,prescription")
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        return vedantaDB;
    }

}
