package main.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.adapter.DurationAdapter;
import main.adapter.LocalDateTimeAdapter;

import java.time.Duration;


import java.time.LocalDateTime;

public class Managers {
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(getDefaultHistoryManager());
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());

        return gsonBuilder.create();
    }
}




