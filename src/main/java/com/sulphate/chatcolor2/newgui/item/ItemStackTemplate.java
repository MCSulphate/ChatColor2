package com.sulphate.chatcolor2.newgui.item;

import com.sulphate.chatcolor2.exception.InvalidItemTemplateException;
import com.sulphate.chatcolor2.exception.InvalidMaterialException;
import com.sulphate.chatcolor2.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class ItemStackTemplate {

    private final Material material;
    private String displayName;
    private final List<String> lore;

    public ItemStackTemplate(Material material, String displayName, List<String> lore) {
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
    }

    public static ItemStackTemplate fromConfigSection(ConfigurationSection section) {
        Material material;
        String displayName = null;
        List<String> lore = Collections.emptyList();

        if (section.contains("material")) {
            String materialName = section.getString("material");
            material = Material.getMaterial(materialName);

            if (material == null) {
                throw new InvalidMaterialException(materialName);
            }
        }
        else {
            throw new InvalidItemTemplateException("All items must have a material.");
        }

        if (section.contains("name")) {
            displayName = section.getString("name");
        }

        if (section.contains("lore")) {
            lore = section.getStringList("lore");
        }

        return new ItemStackTemplate(material, displayName, lore);
    }

    public ItemStack build(int amount) {
        ItemStack item = new ItemStack(material, amount);

        if (displayName != null) {
            InventoryUtils.setDisplayName(item, displayName);
        }

        if (lore != null && !lore.isEmpty()) {
            InventoryUtils.setLore(item, lore);
        }

        return item;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
