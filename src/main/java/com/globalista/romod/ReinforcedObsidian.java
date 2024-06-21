package com.globalista.romod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ReinforcedObsidian implements ModInitializer {

    public static final String MOD_ID = "romod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Block REINFORCED_OBSIDIAN_SLAB;
    public static Block REINFORCED_OBSIDIAN_WALL;
    public static Block REINFORCED_OBSIDIAN_STAIRS;
    public static Block REINFORCED_GLASS;
    public static Block REINFORCED_TINTED_GLASS;

    public static final RegistryKey<ItemGroup> ROGROUP = RegistryKey.of(
            RegistryKeys.ITEM_GROUP, Identifier.of(MOD_ID, "rogroup"));

    private static Item registerBlockItem(String name, Block block) {
        Item item = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), new BlockItem(block, new Item.Settings()));
        ItemGroupEvents.modifyEntriesEvent(ROGROUP).register(content -> content.add(block));
        return item;
    }

    public static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, name), block);
    }

    public static final Block REINFORCED_OBSIDIAN = registerBlock("reinforced_obsidian",
            new Block(Block.Settings.create().sounds(BlockSoundGroup.STONE).strength(50.0f, 3600000.0f).requiresTool()));

    public static final Block REINFORCED_IRON_BARS = registerBlock("reinforced_iron_bars",
            new PaneBlock(Block.Settings.create().sounds(BlockSoundGroup.METAL).strength(7.0f, 92.8f).requiresTool().nonOpaque().sounds(BlockSoundGroup.METAL)));

    static {
        File configFile = FabricLoader.getInstance().getConfigDir().resolve("romod.json").toFile();
        Config config = Config.loadConfigFile(configFile);
        if(config.slabEnable){
            REINFORCED_OBSIDIAN_SLAB = registerBlock("reinforced_obsidian_slab",
                    new SlabBlock(Block.Settings.copy(REINFORCED_OBSIDIAN)));
        }
        if (config.wallEnable) {
            REINFORCED_OBSIDIAN_WALL = registerBlock("reinforced_obsidian_wall",
                    new WallBlock(Block.Settings.copy(REINFORCED_OBSIDIAN)));
        }
        if (config.stairsEnable) {
            REINFORCED_OBSIDIAN_STAIRS = registerBlock("reinforced_obsidian_stairs",
                    new StairsBlock(REINFORCED_OBSIDIAN.getDefaultState(), Block.Settings.copy(REINFORCED_OBSIDIAN)));
        }
        if (config.glassEnable) {
            REINFORCED_GLASS = registerBlock("reinforced_glass",
                    new StainedGlassBlock(DyeColor.LIGHT_GRAY, Block.Settings.create().sounds(BlockSoundGroup.GLASS).strength(0.3f, 3600000.0f).nonOpaque()
                            .sounds(BlockSoundGroup.GLASS).allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never)));
        }
        if (config.tintedGlassEnable) {
            REINFORCED_TINTED_GLASS = registerBlock("reinforced_tinted_glass",
                    new TintedGlassBlock(Block.Settings.create().sounds(BlockSoundGroup.GLASS).strength(0.3f, 3600000.0f).nonOpaque()
                            .sounds(BlockSoundGroup.GLASS).allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never)));
        }
    }

    @Override
    public void onInitialize() {

        Registry.register(Registries.ITEM_GROUP, ROGROUP, FabricItemGroup.builder()
                .icon(() -> new ItemStack(REINFORCED_OBSIDIAN))
                .displayName(Text.translatable("itemGroup.romod.rogroup"))
                .build());

        File configFile = FabricLoader.getInstance().getConfigDir().resolve("romod.json").toFile();
        Config config = Config.loadConfigFile(configFile);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            BlockRenderLayerMap.INSTANCE.putBlock(REINFORCED_IRON_BARS, RenderLayer.getCutout());
            if(config.tintedGlassEnable) BlockRenderLayerMap.INSTANCE.putBlock(REINFORCED_TINTED_GLASS, RenderLayer.getTranslucent());
            if(config.glassEnable) BlockRenderLayerMap.INSTANCE.putBlock(REINFORCED_GLASS, RenderLayer.getTranslucent());
        }

        config.saveConfigFile(configFile);

        LOGGER.info("[Reinforced Obsidian] Mod loaded");
    }
}
