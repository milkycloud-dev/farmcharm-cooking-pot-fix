# FarmCharm Cooking Pot Fix

NeoForge **1.21.1** mixin mod that fixes an empty-container duplication bug in **[Let's Do] Farm & Charm**.

| | |
|---|---|
| Minecraft | 1.21.1 |
| Loader | NeoForge 21.1.x |
| Depends on | [Farm & Charm](https://modrinth.com/mod/lets-do-farm-charm) `1.1.x` |
| License | [MIT](LICENSE) |

---

## The problem

Farm & Charm's **Cooking Pot** has a dedicated **container slot** (slot 6) for empty vessels required by recipes (glass bottles for tea, bowls for soup, etc.).

In Farm & Charm **1.1.22**, `CookingPotBlockEntity.craft(...)` only shrinks that slot when the item has a `craftingRemainingItem`:

```java
ItemStack containerSlotStack = getItem(CONTAINER_SLOT);
if (!containerSlotStack.isEmpty()
        && containerSlotStack.getItem().hasCraftingRemainingItem()) {
    // shrink + put remainder back
}
```

Empty `minecraft:glass_bottle`, bowls, and similar cups **do not** have a crafting remainder, so this block is skipped and the empty container is **never consumed**.

At the same time:

1. **Water bottles** used as ingredients correctly return empty glass bottles via craft remainder.
2. Finished drinks (tea, etc.) return empty bottles/cups again when crafted with or drunk.

**Net result:** empty glass bottles / cups increase every cook cycle — an economy / duplication exploit.

### Affected recipes (examples)

Under `data/farm_and_charm/recipe/pot_cooking/`:

- `nettle_tea.json` — `container: minecraft:glass_bottle`
- `ribwort_tea.json` — same
- `strawberry_tea.json` — same
- Soup recipes using `minecraft:bowl` hit the same code path

Upstream source (branch `1.21.1`):

https://github.com/Let-s-Do-Collection/FarmAndCharm/blob/1.21.1/common/src/main/java/net/satisfy/farm_and_charm/core/block/entity/CookingPotBlockEntity.java

Bug report (issues on the mod repo are disabled; filed on the collection hub):

https://github.com/Let-s-Do-Collection/Let-s-Do-Collection/issues/1073

---

## What this mod does

After a successful Cooking Pot craft (`craft` method **TAIL**), if:

- the recipe requires a container, and
- slot 6 still holds that exact empty container item,

…the stack is **shrunk by 1**.

That closes the hole for bottles/cups/bowls without changing water-bottle ingredient remainder behavior.

---

## Links — original mod

- Modrinth: https://modrinth.com/mod/lets-do-farm-charm  
- CurseForge: https://www.curseforge.com/minecraft/mc-mods/lets-do-farm-charm  
- Source: https://github.com/Let-s-Do-Collection/FarmAndCharm  
- Collection hub / issues: https://github.com/Let-s-Do-Collection/Let-s-Do-Collection  
- Wiki: https://lets-do.ch/wiki/farm-and-charm/

This project is an **unofficial** compatibility/fix patch. It is not affiliated with or endorsed by the Let's Do / satisfyu team. Farm & Charm remains under its own license; this repository only distributes our MIT-licensed mixin patch.

---

## Install

1. NeoForge 1.21.1 server or client with Farm & Charm installed.
2. Drop `farmcharm-cooking-pot-fix-*.jar` into `mods/`.
3. Restart. Log line: `NoteBuns FarmCharm Fix loaded`.

### Verify in-game

1. Cooking Pot on heat.
2. Tea recipe: ingredients + **empty glass bottles** in the container slot.
3. After cooking, empty bottles in the container slot must **decrease** (not pile up).

---

## Build

Requires **JDK 21**.

```bash
./gradlew build
```

Artifact: `build/libs/farmcharm-cooking-pot-fix-1.0.0.jar`

Farm & Charm is downloaded automatically from Modrinth into `libs/` for `compileOnly` (see `downloadFarmCharm` task). CI runs the same path.

---

## License

[MIT](LICENSE) — free to use, modify, and redistribute.

Third-party: Minecraft, NeoForge, and Farm & Charm belong to their respective owners.
