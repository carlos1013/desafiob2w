package com.starwars.desafiob2w.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogMessages {

    public static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error ocurred, please try again later";

    public static Logger getLogger(){
        return Logger.getLogger("App");
    }

    public static void logError(String msg){
        getLogger().log(Level.SEVERE, msg);
    }
}
