package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.commands.ChatColorCommand;
import com.sulphate.chatcolor2.utils.CompatabilityUtils;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.Messages;

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

public class ColourGUIListener implements Listener {

    private Messages M;
    private ConfigUtils configUtils;

    // Not using LinkedHashSet as order is not important.
    private static final Set<Player> playersUsingGUI = new HashSet<>(Bukkit.getMaxPlayers(), 0.8f);

    public ColourGUIListener(Messages M, ConfigUtils configUtils) {
        this.M = M;
        this.configUtils = configUtils;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEvent(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        InventoryView inventoryView = event.getView();
        int rawSlot = event.getRawSlot();

        if (inventory == null) {
            return;
        }

        String guiTitle = GeneralUtils.colourise(M.GUI_TITLE);
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

            // Get the currently selected colour and modifiers.
            String selectedColour = "";
            ArrayList<Character> activeModifiers = new ArrayList<>();

            for (ItemStack item : inventory.getContents()) {
                if (item != null && !item.getType().equals(Material.AIR)) {
                    // Is it a selected colour?
                    if (item.getItemMeta().getLore().get(0).equals(M.GUI_SELECTED)) {
                        String displayName = item.getItemMeta().getDisplayName();
                        if (displayName.equals(GeneralUtils.colouriseMessage("rainbow", M.RAINBOW, false, configUtils))) {
                            selectedColour = "rainbow";
                        }
                        else {
                            selectedColour = Character.toString(displayName.charAt(1));
                            // For some reason, the white colour code doesn't display.
                            // So we default to white if another colour couldn't be assigned.
                            if (ChatColorCommand.getColour(selectedColour) == null) {
                                selectedColour = "f";
                            }
                        }
                    } // Is it an active modifier?
                    else if (item.getItemMeta().getLore().get(0).equals(M.GUI_ACTIVE)) {
                        activeModifiers.add(item.getItemMeta().getDisplayName().charAt(3));
                    }
                }
            }

            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();
            String displayName = clickedItem.getItemMeta().getDisplayName();
            String firstLoreLine = clickedItem.getItemMeta().getLore().get(0);

            if (isColour) {
                String newColor;
                if (displayName.equals(GeneralUtils.colouriseMessage("rainbow", M.RAINBOW, false, configUtils))) {
                    newColor = "rainbow";
                }
                else {
                    newColor = Character.toString(displayName.charAt(1));

                    // For some reason, the white colour code doesn't display.
                    // So we default to white if another colour couldn't be assigned.
                    if (ChatColorCommand.getColour(newColor) == null) {
                        newColor = "f";
                    }
                }

                // Make sure it's not unavailable.
                if (firstLoreLine.equals(M.GUI_UNAVAILABLE)) {
                    player.sendMessage(M.PREFIX + M.NO_COLOR_PERMS.replace("[color]", displayName));
                }
                else {
                    // Check if it is already their colour.
                    String colour = configUtils.getColour(uuid);

                    if (colour.contains(newColor)) {
                        player.sendMessage(M.PREFIX + M.GUI_COLOR_ALREADY_SET);
                        return;
                    }

                    String[] args = createArgs(newColor, activeModifiers);
                    ChatColorCommand.setColorFromArgs(uuid, args, configUtils);

                    player.sendMessage(M.PREFIX + GeneralUtils.colourSetMessage(M.SET_OWN_COLOR, configUtils.getColour(uuid), configUtils, M));
                }
            }
            else {
                char modChar = displayName.charAt(3);

                // Make sure it's not unavailable.
                if (firstLoreLine.equals(M.GUI_UNAVAILABLE)) {
                    player.sendMessage(M.PREFIX + M.NO_MOD_PERMS.replace("[modifier]", displayName.substring(2)));
                }
                else {
                    // If it's active, toggle it off.
                    if (firstLoreLine.equals(M.GUI_ACTIVE)) {

                        for (int i = 0; i < activeModifiers.size(); i++) {
                            if (activeModifiers.get(i) == modChar) {
                                activeModifiers.remove(i);
                                break;
                            }
                        }
                    }
                    // Otherwise, toggle it on.
                    else {
                        activeModifiers.add(modChar);
                    }

                    String[] args = createArgs(selectedColour, activeModifiers);
                    ChatColorCommand.setColorFromArgs(uuid, args, configUtils);

                    player.sendMessage(M.PREFIX + GeneralUtils.colourSetMessage(M.SET_OWN_COLOR, configUtils.getColour(uuid), configUtils, M));
                }
            }

            openGUI(player, M, configUtils); // Refreshes the GUI.
        }
    }

    private String[] createArgs(String colour, List<Character> modifiers) {
        String[] args = new String[1 + modifiers.size()];
        args[0] = colour;

        for (int i = 0; i < modifiers.size(); i++) {
            args[i + 1] = modifiers.get(i) + "";
        }

        return args;
    }

    public static void openGUI(Player player, Messages M, ConfigUtils configUtils) {
        Inventory inventory = Bukkit.createInventory(null, 36, GeneralUtils.colourise(M.GUI_TITLE));

        // Get the player's colour.
        String colour = configUtils.getColour(player.getUniqueId());
        List<String> colourParts = Arrays.asList(colour.split("&"));

        // Colour and modifier codes.
        String[] colourCodes = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "rainbow" };
        String[] colourNames = { M.BLACK, M.DARK_BLUE, M.DARK_GREEN, M.DARK_AQUA, M.DARK_RED, M.DARK_PURPLE, M.GOLD, M.GRAY, M.DARK_GRAY, M.BLUE, M.GREEN, M.AQUA, M.RED, M.LIGHT_PURPLE, M.YELLOW, M.WHITE, M.RAINBOW };
        char[] modifierCodes = { 'k', 'l', 'm', 'n', 'o' };
        String[] modifierNames = { M.OBFUSCATED, M.BOLD, M.STRIKETHROUGH, M.UNDERLINED, M.ITALIC };

        // Colours Array - each of these maps to the colour of a stained glass pane.
        // If the value in a position is -1, we'll use thin glass instead.
        String[] colours = {"BLACK", "BLUE", "GREEN", "CYAN", "RED", "PURPLE", "ORANGE", "LIGHT_GRAY", "GRAY", "BLUE", "LIME", "LIGHT_BLUE", "PINK", "MAGENTA", "YELLOW", "WHITE", null};

        // Modifier ItemStacks
        ItemStack inactiveModifier = CompatabilityUtils.getColouredItem("GRAY_DYE");
        ItemStack activeModifier = new ItemStack(CompatabilityUtils.getColouredItem("LIME_DYE"));

        // Unavailable ItemStack, Unavailable Mod ItemStack
        ItemStack greyedOut = new ItemStack(Material.BARRIER);
        ItemStack unavailableMod = CompatabilityUtils.getColouredItem("RED_DYE");

        // Greyed-out lore
        ItemMeta goItemMeta = greyedOut.getItemMeta();
        goItemMeta.setLore(Collections.singletonList(M.GUI_UNAVAILABLE));
        greyedOut.setItemMeta(goItemMeta);

        // Unavailable lore
        ItemMeta unItemMeta = unavailableMod.getItemMeta();
        unItemMeta.setLore(Collections.singletonList(M.GUI_UNAVAILABLE));
        unavailableMod.setItemMeta(unItemMeta);

        // Active Modifier lore
        ItemMeta actMeta = activeModifier.getItemMeta();
        actMeta.setLore(Arrays.asList(M.GUI_ACTIVE, M.GUI_CLICK_TO_TOGGLE));
        activeModifier.setItemMeta(actMeta);

        // Inactive Modifier lore
        ItemMeta inactMeta = inactiveModifier.getItemMeta();
        inactMeta.setLore(Arrays.asList(M.GUI_INACTIVE, M.GUI_CLICK_TO_TOGGLE));
        inactiveModifier.setItemMeta(inactMeta);

        // Get their available colours and modifiers.
        List<String> available = getAvailable(player);

        // Different lores
        List<String> selectedLore = Collections.singletonList(M.GUI_SELECTED);
        List<String> clickToSelectLore = Collections.singletonList(M.GUI_CLICK_TO_SELECT);

        // Add the colours to the inventory.
        for (int i = 0; i < colourCodes.length; i++) {
            ItemStack itemToAdd;
            ItemMeta im;

            if (available.contains(colourCodes[i])) {
                itemToAdd = colours[i] == null ? CompatabilityUtils.getColouredItem("GLASS_PANE") : CompatabilityUtils.getColouredItem(colours[i] + "_STAINED_GLASS_PANE");
                im = itemToAdd.getItemMeta();
                List<String> lore = colourParts.contains(colourCodes[i]) ? selectedLore : clickToSelectLore;
                im.setLore(lore);
            }
            else {
                itemToAdd = greyedOut.clone();
                im = itemToAdd.getItemMeta();
            }

            im.setDisplayName(GeneralUtils.colouriseMessage((colourCodes[i].equals("rainbow") ? "" : "&") + colourCodes[i], colourNames[i], false, configUtils));
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

            String displayName = modifierCodes[i] == 'k' ? GeneralUtils.colourise("&e&kM&r&e" + modifierNames[i] + "&kM") : GeneralUtils.colourise("&e&" + modifierCodes[i] + modifierNames[i]);
            im = itemToAdd.getItemMeta();
            im.setDisplayName(displayName);
            itemToAdd.setItemMeta(im);

            // For display purposes, ignore first two slots on the row.
            int inventoryIndex = i + 27 + 2;
            inventory.setItem(inventoryIndex, itemToAdd);
        }

        playersUsingGUI.add(player);
        player.openInventory(inventory);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEvent(InventoryCloseEvent event) {
        InventoryView inventoryView = event.getView();

        if (inventoryView != null && inventoryView.getTitle().equals(M.GUI_TITLE) && inventoryView.getType().equals(InventoryType.CHEST)) {
            playersUsingGUI.remove(inventoryView.getPlayer());
        }
    }

    public static void reloadGUI(Messages M, ConfigUtils configUtils) {
        // Refresh the GUI after message reload, so players see the latest text, and events are handled consistently.
        for (Player player : playersUsingGUI) {
            openGUI(player, M, configUtils);
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
