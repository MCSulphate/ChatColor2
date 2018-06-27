package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.utils.CC2Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ColorGUIListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onEvent(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        if (inventory == null) {
            return;
        }

        if (inventory.getTitle().equals(CC2Utils.colourise("&9Color Picker GUI"))) {
            event.getWhoClicked().sendMessage(CC2Utils.colourise("&aYou clicked inside the color picker GUI!"));
        }
    }

    public static void openGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 36, CC2Utils.colourise("&9Color Picker GUI"));

        // Get the player's colour.
        String colour = MainClass.getUtils().getColor(player.getUniqueId().toString());
        List<String> colourParts = Arrays.asList(colour.split("&"));
        player.sendMessage(colour);

        // Colour and modifier codes.
        char[] colourCodes = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        String[] colourNames = {"Black", "Dark Blue", "Dark Green", "Dark Aqua", "Dark Red", "Dark Purple", "Gold", "Gray", "Dark Grey", "Blue", "Green", "Aqua", "Red", "Light Purple", "Yellow", "White"};
        char[] modifierCodes = {'k', 'l', 'm', 'n', 'o'};
        String[] modifierNames = {"Obfuscated", "Bold", "Strikethrough", "Underlined", "Italic"};

        // Colours Array - each of these maps to the colour of a stained glass pane.
        short[] colours = {15, 11, 13, 9, 14, 10, 1, 8, 7, 11, 5, 3, 6, 2, 4, 0};

        // Modifier ItemStacks
        ItemStack inactiveModifier = new ItemStack(Material.INK_SACK, 1, (short) 8);
        ItemStack activeModifier = new ItemStack(Material.INK_SACK, 1, (short) 10);

        // Unavailable ItemStack, Unavailable Mod ItemStack
        ItemStack greyedOut = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemStack unavailableMod = new ItemStack(Material.INK_SACK, 1, (short) 1);

        // Greyed-out lore
        ItemMeta goItemMeta = greyedOut.getItemMeta();
        goItemMeta.setLore(Collections.singletonList(CC2Utils.colourise("&cUnavailable")));
        greyedOut.setItemMeta(goItemMeta);

        // Unavailable lore
        ItemMeta unItemMeta = unavailableMod.getItemMeta();
        unItemMeta.setLore(Collections.singletonList(CC2Utils.colourise("&cUnavailable")));
        unavailableMod.setItemMeta(unItemMeta);

        // Active Modifier lore
        ItemMeta actMeta = activeModifier.getItemMeta();
        actMeta.setLore(Arrays.asList(CC2Utils.colourise("&aActive"), CC2Utils.colourise("&eClick to Toggle")));
        activeModifier.setItemMeta(actMeta);

        // Inactive Modifier lore
        ItemMeta inactMeta = inactiveModifier.getItemMeta();
        inactMeta.setLore(Arrays.asList(CC2Utils.colourise("&7Inactive"), CC2Utils.colourise("&eClick to Toggle")));
        inactiveModifier.setItemMeta(inactMeta);

        // Get their available colours and modifiers.
        List<Character> available = getAvailable(player);

        // Different lores
        List<String> selectedLore = Collections.singletonList(CC2Utils.colourise("&aSelected"));
        List<String> clickToSelectLore = Collections.singletonList(CC2Utils.colourise("&eClick to Select"));

        // Add the colours to the inventory.
        for (int i = 0; i < colourCodes.length; i++) {
            ItemStack itemToAdd;
            ItemMeta im;

            if (available.contains(colourCodes[i])) {
                itemToAdd = new ItemStack(Material.STAINED_GLASS_PANE, 1, colours[i]);
                im = itemToAdd.getItemMeta();
                List<String> lore = colourParts.contains(Character.toString(colourCodes[i])) ? selectedLore : clickToSelectLore;
                im.setLore(lore);
            }
            else {
                itemToAdd = greyedOut.clone();
                im = itemToAdd.getItemMeta();
            }

            im.setDisplayName(CC2Utils.colourise("&" + colourCodes[i] + colourNames[i]));
            itemToAdd.setItemMeta(im);
            inventory.setItem(i, itemToAdd);
        }

        // Add the modifiers to the inventory (bottom row).
        for (int i = 0; i < modifierCodes.length; i++) {
            ItemStack itemToAdd;
            ItemMeta im;

            if (available.contains(modifierCodes[i])) {
                itemToAdd = colourParts.contains(Character.toString(modifierCodes[i])) ? activeModifier.clone() : inactiveModifier.clone();
            }
            else {
                itemToAdd = unavailableMod.clone();
            }

            String displayName = modifierCodes[i] == 'k' ? CC2Utils.colourise("&e&kM&r&eObfuscated&kM") : CC2Utils.colourise("&e&" + modifierCodes[i] + modifierNames[i]);
            im = itemToAdd.getItemMeta();
            im.setDisplayName(displayName);
            itemToAdd.setItemMeta(im);

            inventory.setItem(i + 27, itemToAdd);
        }

        player.openInventory(inventory);
    }

    private static List<Character> getAvailable(Player player) {
        char[] possibilities = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'k', 'l', 'm', 'n', 'o' };
        List<Character> allowed = new ArrayList<>();

        for (int i = 0; i < possibilities.length; i++) {
            String basePermission;

            if (i <= 15) {
                basePermission = "chatcolor.color.";
            }
            else {
                basePermission = "chatcolor.modifier.";
            }

            if (hasPermission(basePermission + possibilities[i], player)) {
                allowed.add(possibilities[i]);
            }
        }

        return allowed;
    }

    private static boolean hasPermission(String permission, Player player) {
        String[] parts = permission.split("\\.");

        StringBuilder currentPermission = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            String permissionToTest = i == parts.length - 1 ? currentPermission.toString() : currentPermission.toString() + ".*";

            if (!player.hasPermission(permissionToTest)) {
                return false;
            }

            currentPermission.append(".").append(parts[i]);
        }

        return true;
    }

}
