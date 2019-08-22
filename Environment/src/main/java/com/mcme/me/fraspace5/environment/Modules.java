/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcme.me.fraspace5.environment;

import lombok.Getter;

/**
 *
 * @author fraspace5
 */


public enum Modules {
    
    REDSTONE_TORCH         ("modules.specialBlocks.redstoneTorch"),
    HALF_DOORS             ("modules.specialBlocks.halfDoors"),
    HALF_BEDS              ("modules.specialBlocks.halfBeds"),
    DOUBLE_SLABS           ("modules.specialBlocks.doubleSlabs"),
    PLANTS                 ("modules.specialBlocks.plants"),
    PISTON_EXTENSIONS      ("modules.specialBlocks.pistonExtenstions"),
    SIX_SIDED_LOGS         ("modules.specialBlocks.sixSidedLogs"),
    DRAGON_EGG             ("modules.specialBlocks.dragonEgg"),
    BURNING_FURNACE        ("modules.specialBlocks.burningFurnace"),
    INVENTORY_ACCESS       ("modules.specialBlocks.inventoryAccess"),
    SPECIAL_BLOCKS_PLACE   ("modules.specialBlocks.place"),
    SPECIAL_BLOCKS_FLINT   ("modules.specialBlocks.flint"),
    USE_POWERED_DOORS      ("modules.specialBlocks.poweredDoors"),
    BLOCK_PLAYER_INTERACTION ("modules.specialBlocks.blockPlayerInteraction"),

    ANIMAL_SPAWN_BLOCKING  ("modules.environment.animalSpawnBlocking"),
    DROP_BLOCKING          ("modules.environment.dropBlocking"),
    MONSTER_SPAWN_BLOCKING ("modules.environment.monsterSpawnBlocking"),
    FIRE_SPREAD_BLOCKING   ("modules.environment.fireSpreadBlocking"),
    WEATHER_BLOCKING       ("modules.environment.weatherBlocking"),
    DECAY_BLOCKING         ("modules.environment.decayBlocking"),
    BLOCK_FORM_BLOCKING    ("modules.environment.formBlocking"),
    NO_PHYSICS_LIST_ENABLED("modules.environment.noPhysicsListEnabled"),
    NO_PHYSICS_LIST_INVERTED("modules.environment.noPhysicsListInverted"),
    NO_PHYSICS_CONNECT_STAIRS("modules.environment.noPhysicsConnectStairs"),
    NO_PHYSICS_CONNECT_FENCES("modules.environment.noPhysicsConnectFences"),
    NO_PHYSICS_CONNECT_CHORUS("modules.environment.noPhysicsConnectChorus"),
    NO_PHYSICS_CONNECT_CHESTS("modules.environment.noPhysicsConnectChests"),
    NO_PHYSICS_CONNECT_GLASS("modules.environment.noPhysicsConnectGlass"),
    
    SIGN_EDITOR            ("modules.command.signEditor"),
    CUSTOM_HEAD_MANAGER    ("modules.command.customHeadManager"),
    RESOURCE_PACK_SWITCHER ("modules.command.resourcePackSwitcher"),
    FULL_BRIGHTNESS        ("modules.command.fullBrightness"),
    SPECIAL_BLOCKS_GET     ("modules.command.getSpecialBlocks"),
    ITEM_TEXTURES          ("modules.commmand.itemTexture"),
    WE_SCHEMATICS_VIEWER   ("modules.command.weSchemViewer"),
    VOXEL_VIEWER           ("modules.command.voxelViewer"),
    STENCIL_LIST_EDITOR    ("modules.command.stencilListEditor"),
    BANNER_EDITOR          ("modules.command.bannerEditor"),
    PAINTING_EDITOR        ("modules.command.paintingEditor"),
    CYCLE_BLOCKS           ("modules.command.cycleBlocks"),
    ARMOR_STAND_EDITOR     ("modules.command.armorStandEditor"),
    ARMOR_STAND_ROLLBACK   ("modules.command.armorStandRollback"),
    RANDOMISER             ("modules.command.randomiser"),
    COPY_PASTE             ("modules.command.copypaste"),
    
    VOXEL_BIOME_BRUSH_FIX  ("modules.voxelBiomeBrushFix"),
    
    CHUNK_UPDATE           ("modules.chunkupdate.manual"),
    CHUNK_UPDATE_AUTO      ("modules.chunkupdate.auto"),
    
    ARMOR_STAND_PROTECTION    ("modules.protection.armorStand"),
    HANGING_ENTITY_PROTECTION ("modules.protection.hangingEntity");
    
    @Getter
    private final String moduleKey;

    private Modules(String key) {
        this.moduleKey = key;
    }

}
