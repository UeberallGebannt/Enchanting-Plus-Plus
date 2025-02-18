package net.epoxide.eplus.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.WeightedRandomChestContent;

import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.ChestGenHooks;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;

import net.darkhax.bookshelf.buff.Buff;
import net.darkhax.bookshelf.common.BookshelfRegistry;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.darkhax.bookshelf.lib.util.Utilities;

import net.epoxide.eplus.EnchantingPlus;
import net.epoxide.eplus.block.BlockArcaneInscriber;
import net.epoxide.eplus.block.BlockEnchantTable;
import net.epoxide.eplus.block.BlockEnchantmentBook;
import net.epoxide.eplus.item.ItemBlockEnchantmentBook;
import net.epoxide.eplus.item.ItemBookSummoner;
import net.epoxide.eplus.item.ItemEnchantedScroll;
import net.epoxide.eplus.item.ItemTableUpgrade;
import net.epoxide.eplus.modifiers.ScrollModifier;
import net.epoxide.eplus.tileentity.TileEntityArcaneInscriber;
import net.epoxide.eplus.tileentity.TileEntityEnchantTable;
import net.epoxide.eplus.tileentity.TileEntityEnchantmentBook;

public final class ContentHandler {
    
    /**
     * A blacklist containing all of the numeric IDs of Enchantments that have been
     * blacklisted.
     */
    private static List<Integer> blacklistEnchantments = new ArrayList<Integer>();
    
    /**
     * A blacklist containing all of the string IDs of Items that have been blacklisted.
     */
    private static List<String> blacklistItems = new ArrayList<String>();
    
    /**
     * A map of all enchantment type colors. The key is the enchantment type, and the Integer
     * is a RGB integer. Used when rendering scroll items.
     */
    private static Map<String, Integer> colorMap = new HashMap<String, Integer>();
    
    /**
     * A List of all modifiers that have been registered. Modifiers are used with the Arcane
     * Inscriber.
     */
    public static List<ScrollModifier> modifiers = new ArrayList<ScrollModifier>();
    
    /**
     * An implementation of ChestGenHooks which specifically handles our villager chest.
     */
    public static ChestGenHooks eplusChest = new ChestGenHooks("EplusChest", new WeightedRandomChestContent[0], 3, 7);
    
    /**
     * An integer used to keep track of how many achievements have been registered under this
     * mod. This is used to automatically position the achievements within the GUI.
     */
    public static int achievementCount = 0;
    
    public static Block blockAdvancedTable;
    public static Block blockArcaneInscriber;
    public static Block blockEnchantmentBook;
    public static Block blockBarrier;
    
    public static Item itemTableUpgrage;
    public static Item itemScroll;
    public static Item itemFloatingBook;
    
    public static Buff buffFloatingBook;
    
    public static AchievementPage achievementPageEplus;
    public static Achievement achievementEnchanter;
    public static Achievement achievementRepair;
    public static Achievement achievementStudies;
    public static Achievement achievementResearch;
    public static Achievement achievementEnlightened;
    
    /**
     * Initializes all of the blocks for the Enchanting Plus mod. Used to handle Block
     * construction and registry.
     */
    public static void initBlocks () {
        
        blockAdvancedTable = new BlockEnchantTable();
        GameRegistry.registerBlock(blockAdvancedTable, "advancedEnchantmentTable");
        GameRegistry.registerTileEntity(TileEntityEnchantTable.class, "eplus:advancedEnchantmentTable");
        
        blockArcaneInscriber = new BlockArcaneInscriber();
        GameRegistry.registerBlock(blockArcaneInscriber, "arcane_inscriber");
        GameRegistry.registerTileEntity(TileEntityArcaneInscriber.class, "eplus:arcane_inscriber");
        
        blockEnchantmentBook = new BlockEnchantmentBook();
        GameRegistry.registerBlock(blockEnchantmentBook, ItemBlockEnchantmentBook.class, "enchantment_book");
        GameRegistry.registerTileEntity(TileEntityEnchantmentBook.class, "eplus:enchantment_book");
    }
    
    /**
     * Initializes all of the items for the Enchanting Plus mod. Used to handle Item
     * construction and registry.
     */
    public static void initItems () {
        
        itemTableUpgrage = new ItemTableUpgrade();
        GameRegistry.registerItem(itemTableUpgrage, "tableUpgrade");
        
        itemScroll = new ItemEnchantedScroll();
        GameRegistry.registerItem(itemScroll, "enchantment_scroll");
        
        itemFloatingBook = new ItemBookSummoner();
        GameRegistry.registerItem(itemFloatingBook, "tomb_guardian");
    }
    
    /**
     * Initializes all of the colors for the vanilla enchantment types. Generic enchantments
     * are purple, armor enchantments are gray, weapone enchantments are red, fishing
     * enchantments are blue, and bow enchantments are dark green.
     */
    public static void initEnchantmentColors () {
        
        setEnchantmentColor(EnumEnchantmentType.all, 15029174);
        setEnchantmentColor(EnumEnchantmentType.armor, 10394268);
        setEnchantmentColor(EnumEnchantmentType.armor_feet, 10394268);
        setEnchantmentColor(EnumEnchantmentType.armor_legs, 10394268);
        setEnchantmentColor(EnumEnchantmentType.armor_torso, 10394268);
        setEnchantmentColor(EnumEnchantmentType.armor_head, 10394268);
        setEnchantmentColor(EnumEnchantmentType.weapon, 16711680);
        setEnchantmentColor(EnumEnchantmentType.digger, 9127187);
        setEnchantmentColor(EnumEnchantmentType.fishing_rod, 1596073);
        setEnchantmentColor(EnumEnchantmentType.breakable, 10394268);
        setEnchantmentColor(EnumEnchantmentType.bow, 29696);
    }
    
    /**
     * Initializes all of the modifiers added by the base mod.
     */
    public static void initModifiers () {
        
        addScrollModifier(new ScrollModifier(new ItemStack(Items.blaze_powder), -0.05f, 0.1f, false));
        addScrollModifier(new ScrollModifier(new ItemStack(Blocks.obsidian), 0.1f, -0.05f, false));
        addScrollModifier(new ScrollModifier(new ItemStack(Items.diamond), 0.25f, -0.05f, false));
        addScrollModifier(new ScrollModifier(new ItemStack(Items.emerald), -0.05f, 0.25f, false));
        addScrollModifier(new ScrollModifier(new ItemStack(Items.ender_pearl), 0.05f, 0f, false));
        addScrollModifier(new ScrollModifier(new ItemStack(Blocks.glowstone), 0f, 0.05f, false));
        addScrollModifier(new ScrollModifier(new ItemStack(Items.ender_eye), 0f, 0.1f, false));
    }
    
    /**
     * Initializes all of the recipes added by the mod.
     */
    public static void initRecipes () {
        
        GameRegistry.addRecipe(new ItemStack(itemTableUpgrage), new Object[] { "gbg", "o o", "geg", 'b', Items.writable_book, 'o', Blocks.obsidian, 'e', Items.ender_eye, 'g', Items.gold_ingot });
        GameRegistry.addRecipe(new ItemStack(blockAdvancedTable), new Object[] { "gbg", "oto", "geg", 'b', Items.writable_book, 'o', Blocks.obsidian, 'e', Items.ender_eye, 'g', Items.gold_ingot, 't', Blocks.enchanting_table });
        GameRegistry.addRecipe(new ItemStack(blockArcaneInscriber), new Object[] { "fpi", "bcb", 'f', Items.feather, 'p', Items.paper, 'i', new ItemStack(Items.dye, 1, 0), 'b', Blocks.bookshelf, 'c', Blocks.crafting_table });
        GameRegistry.addRecipe(new ItemStack(blockEnchantmentBook), new Object[] { " g ", "gbg", " g ", 'g', Items.glowstone_dust, 'b', Items.enchanted_book });
        GameRegistry.addShapelessRecipe(new ItemStack(blockAdvancedTable), new Object[] { Blocks.enchanting_table, itemTableUpgrage });
    }
    
    /**
     * Used to initialize random things.
     */
    public static void initMisc () {
        
        VillagerHandler.initVillageHandler();
        
        for (String entry : ConfigurationHandler.blacklistedItems)
            addItemToBlacklist(entry, "Configuration File");
            
        for (String entry : ConfigurationHandler.blacklistedEnchantments)
            if (StringUtils.isNumeric(entry))
                blacklistEnchantment(Integer.parseInt(entry), "Configuration File");
                
        if (Loader.isModLoaded("NotEnoughItems")) {
            
            addInformation(blockAdvancedTable);
            addInformation(blockArcaneInscriber);
            addInformation(blockEnchantmentBook);
            addInformation(itemTableUpgrage);
            addInformation(itemScroll);
            addInformation(itemFloatingBook);
        }
    }
    
    /**
     * Registers all of the achievements for Enchanting Plus.
     */
    public static void initAchievements () {
        
        achievementEnchanter = registerAchievement("eplus.enchanter", Item.getItemFromBlock(blockAdvancedTable));
        achievementRepair = registerAchievement("eplus.repair", Item.getItemFromBlock(Blocks.anvil));
        achievementStudies = registerAchievement("eplus.study", Item.getItemFromBlock(blockArcaneInscriber));
        achievementResearch = registerAchievement("eplus.research", itemScroll);
        achievementEnlightened = registerAchievement("eplus.enlightened", Item.getItemFromBlock(blockEnchantmentBook));
        
        achievementPageEplus = new AchievementPage("Enchanting Plus", new Achievement[] { achievementEnchanter, achievementRepair, achievementStudies, achievementResearch, achievementEnlightened });
        AchievementPage.registerAchievementPage(achievementPageEplus);
    }
    
    /**
     * Registers all of our dungeon loot.
     */
    public static void registerDungeonLoot () {
        
        ChestGenHooks contents = ChestGenHooks.getInfo("EPLUSSCROLLS");
        
        for (Enchantment enchantment : Utilities.getAvailableEnchantments()) {
            
            if (!isBlacklisted(enchantment)) {
                
                WeightedRandomChestContent scrollEntry = new WeightedRandomChestContent(ItemEnchantedScroll.createScroll(enchantment), 1, 1, 1);
                
                for (String type : Utilities.vanillaLootChests)
                    ChestGenHooks.addItem(type, scrollEntry);
                    
                eplusChest.addItem(scrollEntry);
            }
        }
        
        eplusChest.addItem(new WeightedRandomChestContent(new ItemStack(blockEnchantmentBook), 1, 3, 2));
        eplusChest.addItem(new WeightedRandomChestContent(new ItemStack(Items.paper), 1, 3, 2));
        eplusChest.addItem(new WeightedRandomChestContent(new ItemStack(Items.experience_bottle), 1, 5, 2));
    }
    
    /**
     * Adds an Enchantment to the blacklist. Enchantments that are on this list can not be
     * applied by the advanced enchantment table.
     * 
     * @param enchant: The Enchantment to add to the blacklist.
     * @param blacklister: The name of the person, mod, group or entity that is attempting to
     *            add this entry to the blacklist.
     */
    public static void blacklistEnchantment (Enchantment enchant, String blacklister) {
        
        blacklistEnchantment(enchant.effectId, blacklister);
    }
    
    /**
     * Adds an Enchantment to the blacklist. Enchantments that are on this list can not be
     * applied by the advanced enchantment table.
     * 
     * @param enchantID: The Enchantment ID to add to the blacklist.
     * @param blacklister: The name of the person, mod, group or entity that is attempting to
     *            add this entry to the blacklist.
     */
    public static void blacklistEnchantment (int enchantID, String blacklister) {
        
        if (!blacklistEnchantments.contains(enchantID)) {
            
            blacklistEnchantments.add(enchantID);
            EnchantingPlus.printDebugMessage(blacklister + " has succesfully blacklisted an enchantment with the ID of " + enchantID);
        }
    }
    
    /**
     * Adds an Item to the blacklist. Items that are on this list can not be enchanted using
     * the advanced enchanting table.
     * 
     * @param stack: The ItemStack to add to the blacklist. Does not support meta or NBT!
     * @param blacklister: The name of the person, mod, group or entity that is attempting to
     *            add this entry to the blacklist.
     */
    public static void addItemToBlacklist (ItemStack stack, String blacklister) {
        
        if (ItemStackUtils.isValidStack(stack))
            addItemToBlacklist(stack.getItem(), blacklister);
    }
    
    /**
     * Adds an Item to the blacklist. Items that are on this list can not be enchanted using
     * the advanced enchantment table.
     * 
     * @param item: The Item to add to the blacklist.
     * @param blacklister: The name of the person, mod, group or entity that is attempting to
     *            add this entry to the blacklist.
     */
    public static void addItemToBlacklist (Item item, String blacklister) {
        
        addItemToBlacklist(GameData.getItemRegistry().getNameForObject(item), blacklister);
    }
    
    /**
     * Adds an Item to the blacklist, directly from it's item ID. Items on this list can not be
     * enchanted using the advanced enchantment table.
     * 
     * @param name: The name of the item to blacklist. Should be equal to the item ID.
     * @param blacklister: The name of the person, mod, group or entity that is attempting to
     *            add this entry to the blacklist.
     */
    public static void addItemToBlacklist (String name, String blacklister) {
        
        if (!blacklistItems.contains(name)) {
            
            blacklistItems.add(name);
            EnchantingPlus.printDebugMessage(blacklister + " has successfully blacklisted " + name);
        }
    }
    
    /**
     * Checks if an Enchantment is on the blacklist.
     * 
     * @param enchant: The enchantment to check for.
     * @return boolean: True if the blacklist contains the ID of the passed enchantment.
     */
    public static boolean isBlacklisted (Enchantment enchant) {
        
        return blacklistEnchantments.contains(enchant.effectId);
    }
    
    /**
     * Checks if an ItemStack is on the blacklist.
     * 
     * @param stack: The ItemStack to check for.
     * @return boolean: True if the Item contained in the passed stack is blacklisted, or if
     *         the passed stack is invalid.
     */
    public static boolean isBlacklisted (ItemStack stack) {
        
        return !ItemStackUtils.isValidStack(stack) || isBlacklisted(stack.getItem());
    }
    
    /**
     * Checks if an Item is on the blacklist.
     * 
     * @param item: The Item to check for.
     * @return boolean: True if the name of the passed Item is in the blacklist.
     */
    public static boolean isBlacklisted (Item item) {
        
        return blacklistItems.contains(GameData.getItemRegistry().getNameForObject(item));
    }
    
    /**
     * Retrieves the color linked to an EnumEnchantmentType. If no color has been set, white
     * will be used.
     * 
     * @param enchType: The name of the EnumEnchantmentType you are getting the color for.
     * @return int: The color associated to the EnumEnchantmentType. If no color is set, white
     *         will be used.
     */
    public static int getEnchantmentColor (String enchType) {
        
        return (colorMap.containsKey(enchType)) ? colorMap.get(enchType) : 16777215;
    }
    
    /**
     * Sets a color to represent an enchantment type. This color is used in rendering the
     * scroll items, and possibly in other places.
     * 
     * @param enchType: The EnumEnchantmentType you are setting the color for.
     * @param color: An RGB integer that represents the color to set.
     */
    public static void setEnchantmentColor (EnumEnchantmentType enchType, int color) {
        
        setEnchantmentColor(enchType.name(), color);
    }
    
    /**
     * Sets a color to represent an enchantment type. This color is used in rendering the
     * scroll items, and possibly in other places.
     * 
     * @param enchType: The name of the EnumEnchantmentType you are setting the color for.
     * @param color: An RGB integer that represents the color to set.
     */
    public static void setEnchantmentColor (String enchType, int color) {
        
        if (!colorMap.containsKey(enchType)) {
            
            colorMap.put(enchType, color);
            EnchantingPlus.printDebugMessage("The color of enchantment type " + enchType + " has been set to " + color);
        }
    }
    
    /**
     * Registers a ScrollModifier with our List of modifiers.
     *
     * @param modifier: The modifier to register.
     */
    public static void addScrollModifier (ScrollModifier modifier) {
        
        modifiers.add(modifier);
    }
    
    /**
     * Retrieves a ScrollModifier using the modifier ItemStack as a key.
     * 
     * @param stack: The ItemStack associated with the modifier you are looking for.
     * @return ScrollModifier: If a valid modifier is found, it will be returned. Otherwise
     *         null.
     */
    public static ScrollModifier findScrollModifier (ItemStack stack) {
        
        for (ScrollModifier modifier : modifiers)
            if (ItemStackUtils.areStacksSimilar(modifier.stack, stack))
                return modifier;
                
        return null;
    }
    
    /**
     * Adds information for an EPlus Item.
     * 
     * @param item: The Item to add information for.
     */
    private static void addInformation (Item item) {
        
        BookshelfRegistry.addInformation(item, "info." + item.getUnlocalizedName());
    }
    
    /**
     * Adds information for an EPlus block.
     * 
     * @param block: The block to add information for.
     */
    private static void addInformation (Block block) {
        
        BookshelfRegistry.addInformation(block, "info." + block.getUnlocalizedName());
    }
    
    /**
     * Creates a new Achievement to be used in the mod. The posX and posY are automatically
     * generated.
     * 
     * @param key: The localization key to use for both the description and the title.
     * @param item: The Item to show in the tab. Also works for blocks.
     * @return Achievement: The instance of Achievement created.
     */
    public static Achievement registerAchievement (String key, Item item) {
        
        final int posX = achievementCount % 8;
        final int posY = achievementCount / 8;
        achievementCount++;
        
        return new Achievement(key, key, posX, posY + 1, item, null).registerStat();
    }
}