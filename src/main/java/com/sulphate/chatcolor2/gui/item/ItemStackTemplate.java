package com.sulphate.chatcolor2.gui.item;

import com.sulphate.chatcolor2.exception.InvalidItemTemplateException;
import com.sulphate.chatcolor2.exception.InvalidMaterialException;
import com.sulphate.chatcolor2.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ItemStackTemplate {

    private final Material material;
    private String displayName;
    private final List<String> lore;
    private final String headData;
    private boolean failedToApplyHeadData;

    public ItemStackTemplate(Material material, String displayName, List<String> lore, String headData) {
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
        this.headData = headData;

        failedToApplyHeadData = false;
    }

    public static ItemStackTemplate fromConfigSection(ConfigurationSection section) {
        Material material;
        String displayName = null;
        List<String> lore = Collections.emptyList();
        String headData = null;

        if (section.contains("material")) {
            String materialName = section.getString("material");
            material = Material.getMaterial(materialName);

            if (material == null) {
                throw new InvalidMaterialException(materialName);
            }

            if (material.equals(Material.PLAYER_HEAD)) {
                if (!section.contains("head-data")) {
                    throw new InvalidItemTemplateException("Head items must have a head-data value.");
                }

                headData = section.getString("head-data");
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

        return new ItemStackTemplate(material, displayName, lore, headData);
    }

    public ItemStack build(int amount) {
        ItemStack item = new ItemStack(material, amount);

        if (displayName != null) {
            InventoryUtils.setDisplayName(item, displayName);
        }

        if (lore != null && !lore.isEmpty()) {
            InventoryUtils.setLore(item, lore);
        }

        if (headData != null) {
            applyHeadData(item);
        }

        return item;
    }

    private void applyHeadData(ItemStack head) {
        try {
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            PlayerProfile pProfile = Bukkit.createPlayerProfile(UUID.randomUUID());
            URL textureUrl = getUrlFromHeadData();
            PlayerTextures textures = pProfile.getTextures();

            textures.setSkin(textureUrl);
            pProfile.setTextures(textures);

            meta.setOwnerProfile(pProfile);
            head.setItemMeta(meta);
        }
        catch (NoSuchMethodError ex) {
            failedToApplyHeadData = true;
        }
    }

    public boolean failedToApplyHeadData() {
        return failedToApplyHeadData;
    }

    private URL getUrlFromHeadData() {
        String decoded = new String(Base64.getDecoder().decode(headData));
        String urlPart = decoded.substring(decoded.indexOf("url") + 6, decoded.indexOf("}") - 1);

        try {
            return new URL(urlPart);
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
