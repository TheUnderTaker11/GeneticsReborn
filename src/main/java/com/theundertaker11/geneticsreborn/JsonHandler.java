package com.theundertaker11.geneticsreborn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.MobToGeneRegistry;
import com.theundertaker11.geneticsreborn.util.MobToGeneObject;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class JsonHandler {

    public static String jsonLocation;

    public static void handleJson(FMLPreInitializationEvent event) {
        File json = new File(event.getModConfigurationDirectory().getPath() + "/GeneticsReborn/MobAdditions.json");
        jsonLocation = json.getPath();

        if(!json.exists()) {
            try {
                JsonWriter writer = new JsonWriter(new FileWriter(json));
                writer.beginObject();
                writer.setIndent("  ");

                writer.name("EntityTest1");
                writer.beginArray();
                writer.value("eat_grass");
                writer.endArray();

                writer.name("EntityTest2");
                writer.beginArray();
                writer.value("jump_boost");
                writer.value("eat_grass");
                writer.endArray();

                writer.name("EntityTest3");
                writer.beginArray();
                writer.value("wooly");
                writer.value("speed");
                writer.value("milky");
                writer.endArray();

                writer.endObject();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void registerMobsFromJson() {
        File json = new File(jsonLocation);
        try {
            JsonReader reader = new JsonReader(new FileReader(json));
            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                if (!name.toLowerCase().contains("entitytest")) {
                	List<EnumGenes> genes = new ArrayList<EnumGenes>();
                    reader.beginArray();
                    while (reader.hasNext()) {
                        EnumGenes g = EnumGenes.valueOf(reader.nextString().toUpperCase());
                        if (g != null) genes.add(g);
                    }
                    
                    MobToGeneRegistry.registerMob(new MobToGeneObject(name, genes));
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }

            reader.endObject();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
