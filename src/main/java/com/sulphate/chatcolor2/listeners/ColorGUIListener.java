package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.commands.ChatColorCommand;
import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.utils.CC2Utils;
import com.sulphate.chatcolor2.utils.CCStrings;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ColorGUIListener implements Listener {

    private static final Set<Player> playersUsingGui = new LinkedHashSet<>(Bukkit.getMaxPlayers(), 0.8f);

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEvent(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        InventoryView inventoryView = event.getView();
        int rawSlot = event.getRawSlot();

        if (inventory == null) {
            return;
        }

        String guiTitle = CC2Utils.colourise(CCStrings.guititle);
        if (inventoryView.getTitle().equals(guiTitle)) {
            event.setCancelled(true);

            if (inventoryView.convertSlot(rawSlot) != rawSlot) {
                return;
            }

            ItemStack clickedItem = event.getCurrentItem();

            // Check if they clicked an empty space.
            if (clickedItem == null || clickedItem.getType() == null || clickedItem.getType().equals(Material.AIR)) {
                return;
            }

            // Determine whether it was a colour or a modifier they clicked.
            int clickedSlot = event.getSlot();
            boolean isColour = clickedSlot <= 23;

            // Get the currently selected colour and modifier.
            String selectedColor = "";
            String activeModifier = "";
            for (ItemStack item : inventory.getContents()) {
                if (item != null && !item.getType().equals(Material.AIR)) {
                    // Is it a selected colour?
                    if (item.getItemMeta().getLore().get(0).equals(CCStrings.guiselected)) {
                        String displayName = item.getItemMeta().getDisplayName();
                        if (displayName.equals(CC2Utils.colouriseMessage("rainbow", CCStrings.rainbow, false))) {
                            selectedColor = "rainbow";
                        }
                        else {
                            selectedColor = Character.toString(displayName.charAt(1));
                            // For some reason, the white colour code doesn't display.
                            // So we default to white if another color couldn't be assigned
                            if (ChatColorCommand.getColor(selectedColor) == null) {
                                selectedColor = "f";
                            }
                        }
                    } // Is it an active modifier?
                    else if (item.getItemMeta().getLore().get(0).equals(CCStrings.guiactive)) {
                        activeModifier = Character.toString(item.getItemMeta().getDisplayName().charAt(3));
                    }
                }
            }

            Player player = (Player) event.getWhoClicked();
            String playerUuid = player.getUniqueId().toString();
            String displayName = clickedItem.getItemMeta().getDisplayName();
            String firstLoreLine = clickedItem.getItemMeta().getLore().get(0);

            if (isColour) {
                String newColor;
                if (displayName.equals(CC2Utils.colouriseMessage("rainbow", CCStrings.rainbow, false))) {
                    newColor = "rainbow";
                }
                else {
                    newColor = Character.toString(displayName.charAt(1));

                    // For some reason, the white colour code doesn't display.
                    // So we default to white if another color couldn't be assigned
                    if (ChatColorCommand.getColor(newColor) == null) {
                        newColor = "f";
                    }
                }

                // Make sure it's not unavailable.
                if (firstLoreLine.equals(CCStrings.guiunavailable)) {
                    player.sendMessage(CCStrings.nocolorperms + displayName);
                }
                else {
                    ChatColorCommand.setColorFromArgs(playerUuid, new String[]{ newColor, activeModifier });
                    player.sendMessage(CCStrings.setowncolor + CC2Utils.colouriseMessage(MainClass.getUtils().getColor(playerUuid), CCStrings.colthis, false));
                }
            }
            else {
                char modChar = displayName.charAt(3);

                // Make sure it's not unavailable.
                if (firstLoreLine.equals(CCStrings.guiunavailable)) {
                    player.sendMessage(CCStrings.nomodperms + displayName.substring(2));
                }
                else {
                    if (firstLoreLine.equals(CCStrings.guiactive)) {
                        activeModifier = "";
                    }
                    else {
                        activeModifier = Character.toString(modChar);
                    }

                    ChatColorCommand.setColorFromArgs(playerUuid, new String[]{ selectedColor, activeModifier });
                    player.sendMessage(CCStrings.setowncolor + CC2Utils.colouriseMessage(MainClass.getUtils().getColor(playerUuid), CCStrings.colthis, false));
                }
            }

            openGUI(player); // Refreshes the GUI.
        }
    }

    public static void openGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 36, CC2Utils.colourise(CCStrings.guititle));

        // Get the player's colour.
        String colour = MainClass.getUtils().getColor(player.getUniqueId().toString());
        List<String> colourParts = Arrays.asList(colour.split("&"));

        // Colour and modifier codes.
        String[] colourCodes = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "rainbow"};
        String[] colourNames = {CCStrings.black, CCStrings.darkblue, CCStrings.darkgreen, CCStrings.darkaqua, CCStrings.darkred, CCStrings.darkpurple, CCStrings.gold, CCStrings.gray, CCStrings.darkgray, CCStrings.blue, CCStrings.green, CCStrings.aqua, CCStrings.red, CCStrings.lightpurple, CCStrings.yellow, CCStrings.white, CCStrings.rainbow};
        char[] modifierCodes = {'k', 'l', 'm', 'n', 'o'};
        String[] modifierNames = {CCStrings.obfuscated, CCStrings.bold, CCStrings.strikethrough, CCStrings.underlined, CCStrings.italic};

        // Colours Array - each of these maps to the colour of a stained glass pane.
        // If the value in a position is -1, we'll use thin glass instead.
        short[] colours = {15, 11, 13, 9, 14, 10, 1, 8, 7, 11, 5, 3, 6, 2, 4, 0, -1};

        // Modifier ItemStacks
        ItemStack inactiveModifier = new ItemStack(Material.INK_SACK, 1, (short) 8);
        ItemStack activeModifier = new ItemStack(Material.INK_SACK, 1, (short) 10);

        // Unavailable ItemStack, Unavailable Mod ItemStack
        ItemStack greyedOut = new ItemStack(Material.BARRIER);
        ItemStack unavailableMod = new ItemStack(Material.INK_SACK, 1, (short) 1);

        // Greyed-out lore
        ItemMeta goItemMeta = greyedOut.getItemMeta();
        goItemMeta.setLore(Collections.singletonList(CCStrings.guiunavailable));
        greyedOut.setItemMeta(goItemMeta);

        // Unavailable lore
        ItemMeta unItemMeta = unavailableMod.getItemMeta();
        unItemMeta.setLore(Collections.singletonList(CCStrings.guiunavailable));
        unavailableMod.setItemMeta(unItemMeta);

        // Active Modifier lore
        ItemMeta actMeta = activeModifier.getItemMeta();
        actMeta.setLore(Arrays.asList(CCStrings.guiactive, CCStrings.guiclicktotoggle));
        activeModifier.setItemMeta(actMeta);

        // Inactive Modifier lore
        ItemMeta inactMeta = inactiveModifier.getItemMeta();
        inactMeta.setLore(Arrays.asList(CCStrings.guiinactive, CCStrings.guiclicktotoggle));
        inactiveModifier.setItemMeta(inactMeta);

        // Get their available colours and modifiers.
        List<String> available = getAvailable(player);

        // Different lores
        List<String> selectedLore = Collections.singletonList(CCStrings.guiselected);
        List<String> clickToSelectLore = Collections.singletonList(CCStrings.guiclicktoselect);

        // Add the colours to the inventory.
        for (int i = 0; i < colourCodes.length; i++) {
            ItemStack itemToAdd;
            ItemMeta im;

            if (available.contains(colourCodes[i])) {
                itemToAdd = colours[i] == -1 ? new ItemStack(Material.THIN_GLASS, 1) : new ItemStack(Material.STAINED_GLASS_PANE, 1, colours[i]);
                im = itemToAdd.getItemMeta();
                List<String> lore = colourParts.contains(colourCodes[i]) ? selectedLore : clickToSelectLore;
                im.setLore(lore);
            }
            else {
                itemToAdd = greyedOut.clone();
                im = itemToAdd.getItemMeta();
            }

            im.setDisplayName(CC2Utils.colouriseMessage((colourCodes[i].equals("rainbow") ? "" : "&") + colourCodes[i], colourNames[i], false));
            itemToAdd.setItemMeta(im);

            // For display purposes, ignore tenth inventory slot if we're on the second
            // row, and ignore 4 slots if we're on the third.
            int inventoryIndex = i >= 9 ? (i >= 16 ? i + 6 : i + 1) : i;
            inventory.setItem(inventoryIndex, itemToAdd);
        }

        // Add the modifiers to the inventory (bottom row).
        for (int i = 0; i < modifierCodes.length; i++) {
            ItemStack itemToAdd;
            ItemMeta im;

            if (available.contains(Character.toString(modifierCodes[i]))) {
                itemToAdd = colourParts.contains(Character.toString(modifierCodes[i])) ? activeModifier.clone() : inactiveModifier.clone();
            }
            else {
                itemToAdd = unavailableMod.clone();
            }

            String displayName = modifierCodes[i] == 'k' ? CC2Utils.colourise("&e&kM&r&e" + modifierNames[i] + "&kM") : CC2Utils.colourise("&e&" + modifierCodes[i] + modifierNames[i]);
            im = itemToAdd.getItemMeta();
            im.setDisplayName(displayName);
            itemToAdd.setItemMeta(im);

            // For display purposes, ignore first two slots on the row.
            int inventoryIndex = i + 27 + 2;
            inventory.setItem(inventoryIndex, itemToAdd);
        }

        playersUsingGui.add(player);
        player.openInventory(inventory);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEvent(InventoryCloseEvent event) {
        InventoryView inventoryView = event.getView();
        if (inventoryView != null && inventoryView.getTitle().equals(CCStrings.guititle) && inventoryView.getType().equals(InventoryType.CHEST)) {
            playersUsingGui.remove(inventoryView.getPlayer());
        }
    }

    public static void reloadGUI() {
        // Refresh the GUI after message reload, so players see the latest text
        // and events are handled consistently.
        for (Player player : playersUsingGui) {
            openGUI(player);
        }
    }

    private static List<String> getAvailable(Player player) {
        final String[] possibilities = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "rainbow", "k", "l", "m", "n", "o" };
        List<String> allowed = new ArrayList<>(possibilities.length);

        for (int i = 0; i < possibilities.length; i++) {
            String basePermission;

            if (i <= 16) {
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
        for (int i = 0; i < parts.length; i++) {
            String permissionToTest = i == parts.length - 1 ? currentPermission.toString() : currentPermission.toString() + ".*";

            if (player.hasPermission(permissionToTest)) {
                return true;
            }

            if (i != parts.length - 1) {
                currentPermission.append(".").append(parts[i + 1]);
            }
        }

        return false;
    }

}
