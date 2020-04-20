/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.environment.SoundPacket;

/**
 *
 * @author Fraspace5
 */
public enum SoundsString {

    GENERIC_TREE_DAY("generic.tree.day"),
    //  Plains
    PLAINS_BIRD_DAY("plains.bird.day"),
    PLAINS_BIRD_MORNING("plains.bird.morning"),
    PLAINS_BIRD_NIGHT("plains.bird.night"),
    PLAINS_INSECT_DAY("plains.insect.day"),
    PLAINS_INSECT_MORNING("plains.insect.morning"),
    PLAINS_INSECT_NIGHT("plains.insect.night"),
    //  Forest
    FOREST_BIRD_DAY("forest.bird.day"),
    FOREST_BIRD_MORNING("forest.bird.morning"),
    FOREST_BIRD_NIGHT("forest.bird.night"),
    FOREST_INSECT_DAY("forest.insect.day"),
    FOREST_INSECT_MORNING("forest.insect.morning"),
    FOREST_INSECT_NIGHT("forest.insect.night"),
    //  SwampLand
    SWAMPLAND_FROGS_NIGHT("swampland.frogs.night"),
    SWAMPLAND_CRICKETS_DAY("swampland.crickets.day"),
    SWAMPLAND_CRICKETS_NIGHT("swampland.crickets.night"),
    SWAMPLAND_BIRDS_DAY("swampland.birds.day"),
    SWAMPLAND_BIRDS_NIGHT("swampland.birds.night"),
    //  Caves
    CAVES_DROPLETS("environmental.caves.droplets"),
    CAVES_CRUMBLE("environmental.caves.crumble"),
    CAVE_CRICKETS("animals.insect.cave_crickets"),
    //  Ocean
    WALES("ocean.wales"),
    OCEAN("environmental.ocean"),
    // wind
    WIND("environmental.wind"),
    ELEVATION("environmental.elevation");
   
    private String path;

    SoundsString(String envUrl) {
        this.path = envUrl;
    }

    public String getPath() {
        return path;
    }

}
