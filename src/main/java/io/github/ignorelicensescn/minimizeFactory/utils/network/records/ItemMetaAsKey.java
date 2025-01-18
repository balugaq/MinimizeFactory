package io.github.ignorelicensescn.minimizeFactory.utils.network.records;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.properties;

//hope java 21 can provide better performance with this

public final class ItemMetaAsKey implements ItemMeta{

    public static final Map<Enchantment,Integer> EMPTY_ENCHANTMENT_MAP = Collections.emptyMap();
    public static final Map<EquipmentSlot,Multimap<Attribute, AttributeModifier>> EMPTY_ATTR_BY_EQUIP_SLOT_MAP = Collections.emptyMap();
    public static final Set<ItemFlag> EMPTY_ITEM_FLAGS = Collections.emptySet();
    public static final Multimap<Attribute, AttributeModifier> EMPTY_ATTR_MULTIMAP = ImmutableMultimap.of();
    public static final int NO_CUSTOM_MODEL_DATA = 0;
    public static final int NO_ENCHANTMENT = 0;
    private final Material fromMaterial;
    private final boolean hasDisplayName;
    private final String displayName;
    private final boolean hasLocalizedName;
    private final String localizedName;
    private final boolean hasLore;
    private final List<String> lore;
    private final boolean hasCustomModelData;
    private final int customModelData;
    private final boolean hasEnchantments;
    private final Map<Enchantment, Integer> enchantments;
    private final Set<ItemFlag> itemFlags;
    private final boolean hasAttributeModifiers;
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private final Map<EquipmentSlot,Multimap<Attribute, AttributeModifier>> attributeModifiersByEquipSlot;

    private final boolean unbreakable;
    private final PersistentDataContainer persistentDataContainer;
    private final Map<String,Object> serialized;
    private final int hash;
    public ItemMetaAsKey(ItemMeta from, Material fromMaterial){
        this.fromMaterial = fromMaterial;
        this.hasDisplayName =
                from != null && from.hasDisplayName();

        String displayName = fromMaterial.name();
        if (from != null && from.hasDisplayName()){
            displayName = from.getDisplayName();
        }
        this.displayName = displayName;

        this.hasLocalizedName = from != null && from.hasLocalizedName();
        String localizedName = fromMaterial.name();
        if (from != null && from.hasLocalizedName()){
            localizedName = from.getLocalizedName();
        }
        this.localizedName = localizedName;

        this.hasLore =
                from != null && from.hasLore();
        List<String> loreCache =
                from == null ? null:
                this.hasLore
                        ? from.getLore()
                        : null;
        if (loreCache != null){
            this.lore = List.of(loreCache.toArray(new String[0]));
        }else {
            this.lore = null;
        }
        this.hasCustomModelData =
                from != null && from.hasCustomModelData();
        int  customModelData = NO_CUSTOM_MODEL_DATA;
        if (from != null && from.hasCustomModelData()){
            customModelData = from.getCustomModelData();
        }
        this.customModelData = customModelData;

        this.hasEnchantments = from != null && from.hasEnchants();
        if (hasEnchantments){
            Map<Enchantment, Integer> enchantments = new EnumMap<>(Enchantment.class);//this good
            enchantments.putAll(from.getEnchants());
            this.enchantments = Collections.unmodifiableMap(enchantments);
        }else {
            this.enchantments = EMPTY_ENCHANTMENT_MAP;
        }

        Set<ItemFlag> itemFlags = EMPTY_ITEM_FLAGS;
        if (from != null){
            itemFlags = new HashSet<>(ItemFlag.values().length);
            itemFlags.addAll(from.getItemFlags());
            itemFlags = Collections.unmodifiableSet(itemFlags);
        }
        this.itemFlags = itemFlags;
        this.hasAttributeModifiers = from != null && from.hasAttributeModifiers();
        Multimap<Attribute, AttributeModifier> attributeModifiers = EMPTY_ATTR_MULTIMAP;
        Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> attributeModifiersByEquipSlot = EMPTY_ATTR_BY_EQUIP_SLOT_MAP;
        if (hasAttributeModifiers){
            attributeModifiers = MultimapBuilder.enumKeys(Attribute.class).arrayListValues().build();
            attributeModifiersByEquipSlot = new TreeMap<>();
            if (from.getAttributeModifiers() != null) {
                attributeModifiers.putAll(from.getAttributeModifiers());
            }
            for (EquipmentSlot slot:EquipmentSlot.values()){
                attributeModifiersByEquipSlot.put(slot,from.getAttributeModifiers(slot));
            }
        }
        this.attributeModifiers = attributeModifiers;
        this.attributeModifiersByEquipSlot = attributeModifiersByEquipSlot;

        this.unbreakable = from != null && from.isUnbreakable();

        if (from == null){
            this.serialized = Collections.emptyMap();
        }else {
            this.serialized = Collections.unmodifiableMap(new TreeMap<>(from.serialize()));
        }
        this.persistentDataContainer = from == null?null:from.getPersistentDataContainer();


        int hashResult = fromMaterial.hashCode();
        hashResult = 31 * hashResult + (hasDisplayName ? 1 : 0);
        hashResult = 31 * hashResult + displayName.hashCode();
        hashResult = 31 * hashResult + (hasLocalizedName ? 1 : 0);
        hashResult = 31 * hashResult + localizedName.hashCode();
        hashResult = 31 * hashResult + (hasLore ? 1 : 0);
        hashResult = 31 * hashResult + (lore != null ? lore.hashCode() : 0);
        hashResult = 31 * hashResult + (hasCustomModelData ? 1 : 0);
        hashResult = 31 * hashResult + customModelData;
        hashResult = 31 * hashResult + (hasEnchantments ? 1 : 0);
        hashResult = 31 * hashResult + enchantments.hashCode();
        hashResult = 31 * hashResult + itemFlags.hashCode();
        hashResult = 31 * hashResult + (hasAttributeModifiers ? 1 : 0);
        hashResult = 31 * hashResult + attributeModifiers.hashCode();
        hashResult = 31 * hashResult + attributeModifiersByEquipSlot.hashCode();
        hashResult = 31 * hashResult + (unbreakable ? 1 : 0);
        hashResult = 31 * hashResult + (persistentDataContainer != null ? persistentDataContainer.hashCode() : 0);
        hashResult = 31 * hashResult + serialized.hashCode();
        this.hash = hashResult;
    }

    @Override
    public boolean hasDisplayName() {
        return hasDisplayName;
    }

    @Override
    @Nonnull
    public String getDisplayName() {
        return displayName;
    }
    @Override
    public void setDisplayName(String name) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }
    @Override
    public boolean hasLocalizedName() {
        return hasLocalizedName;
    }

    @Override
    @Nonnull
    public String getLocalizedName() {
        return localizedName;
    }

    @Override
    public void setLocalizedName(String name) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Override
    public boolean hasLore() {
        return hasLore;
    }

    @Override
    @Nullable
    public List<String> getLore() {
        return lore;
    }

    @Override
    public void setLore(List<String> lore) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Override
    public boolean hasCustomModelData() {
        return hasCustomModelData;
    }

    @Override
    public int getCustomModelData() {
        return customModelData;
    }

    @Override
    public void setCustomModelData(Integer data) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Override
    public boolean hasEnchants() {
        return false;
    }

    @Override
    public boolean hasEnchant(@Nonnull Enchantment ench) {
        return hasEnchantments;
    }

    @Override
    public int getEnchantLevel(@Nonnull Enchantment ench) {
        return enchantments.getOrDefault(ench,NO_ENCHANTMENT);
    }

    @Nonnull
    @Override
    public Map<Enchantment, Integer> getEnchants() {
        return enchantments;
    }

    @Override
    public boolean addEnchant(@Nonnull Enchantment ench, int level, boolean ignoreLevelRestriction) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Override
    public boolean removeEnchant(@Nonnull Enchantment ench) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Override
    public boolean hasConflictingEnchant(@Nonnull Enchantment ench) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("NotImplemented"),this.getClass().getName()));
    }

    @Override
    public void addItemFlags(@Nonnull ItemFlag... itemFlags) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Override
    public void removeItemFlags(@Nonnull ItemFlag... itemFlags) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Nonnull
    @Override
    public Set<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    @Override
    public boolean hasItemFlag(@Nonnull ItemFlag flag) {
        return itemFlags.contains(flag);
    }

    @Override
    public boolean isUnbreakable() {
        return unbreakable;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Override
    public boolean hasAttributeModifiers() {
        return hasAttributeModifiers;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return attributeModifiers;
    }

    @Nonnull
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlot slot) {
        return attributeModifiersByEquipSlot.get(slot);
    }

    @Override
    public Collection<AttributeModifier> getAttributeModifiers(@Nonnull Attribute attribute) {
        return attributeModifiers.get(attribute);
    }

    @Override
    public boolean addAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Override
    public void setAttributeModifiers(Multimap<Attribute, AttributeModifier> attributeModifiers) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Override
    public boolean removeAttributeModifier(@Nonnull Attribute attribute) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Override
    public boolean removeAttributeModifier(@Nonnull EquipmentSlot slot) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Override
    public boolean removeAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Override
    public CustomItemTagContainer getCustomTagContainer() {
        return null;
    }

    @Override
    public void setVersion(int version) {
        throw new UnsupportedOperationException(String.format(properties.getReplacedProperty("ObjectUnmodifiable"),this.getClass().getName()));
    }

    @Nonnull
    @Override
    public ItemMeta clone() {
        return new ItemMetaAsKey(this,this.fromMaterial);
    }

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        return serialized;
    }

    @Nonnull
    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return persistentDataContainer;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemMetaAsKey that = (ItemMetaAsKey) o;

        if (hasDisplayName != that.hasDisplayName) return false;
        if (hasLocalizedName != that.hasLocalizedName) return false;
        if (hasLore != that.hasLore) return false;
        if (hasCustomModelData != that.hasCustomModelData) return false;
        if (customModelData != that.customModelData) return false;
        if (hasEnchantments != that.hasEnchantments) return false;
        if (hasAttributeModifiers != that.hasAttributeModifiers) return false;
        if (unbreakable != that.unbreakable) return false;
        if (fromMaterial != that.fromMaterial) return false;
        if (!displayName.equals(that.displayName)) return false;
        if (!localizedName.equals(that.localizedName)) return false;
        if (!Objects.equals(lore, that.lore)) return false;
        if (!enchantments.equals(that.enchantments)) return false;
        if (!itemFlags.equals(that.itemFlags)) return false;
        if (!attributeModifiers.equals(that.attributeModifiers)) return false;
        if (!attributeModifiersByEquipSlot.equals(that.attributeModifiersByEquipSlot)) return false;
        if (!Objects.equals(persistentDataContainer, that.persistentDataContainer))
            return false;
        return serialized.equals(that.serialized);
    }

    @Override
    public int hashCode() {

        return hash;
    }
}
