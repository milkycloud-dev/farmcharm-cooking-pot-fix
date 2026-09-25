<p align="center"><img src="assets/icon.png" width="128" height="128" alt="FarmCharm Cooking Pot Fix icon"></p>

<h1 align="center">FarmCharm Cooking Pot Fix</h1>

<p align="center">NeoForge 1.21.1 mixin mod that closes an empty-container duplication bug in the Cooking Pot of [Let's Do] Farm &amp; Charm.</p>

<p align="center">
  <a href="https://github.com/milkycloud-dev/farmcharm-cooking-pot-fix/actions/workflows/build.yml"><img src="https://github.com/milkycloud-dev/farmcharm-cooking-pot-fix/actions/workflows/build.yml/badge.svg" alt="Build"></a>
  <a href="https://github.com/milkycloud-dev/farmcharm-cooking-pot-fix/releases/latest"><img src="https://img.shields.io/github/v/release/milkycloud-dev/farmcharm-cooking-pot-fix" alt="Release"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-proprietary-lightgrey" alt="License: proprietary"></a>
</p>

<p align="center"><a href="#english">English</a> | <a href="#русский">Русский</a></p>

<a id="english"></a>

## English

### Overview

| | |
|---|---|
| Minecraft | 1.21.1 |
| Loader | NeoForge 21.1.x |
| Depends on | [Farm & Charm](https://modrinth.com/mod/lets-do-farm-charm) 1.1.x, tested with 1.1.22 |
| Side | server (and single player) |

### The problem

The Farm & Charm **Cooking Pot** has a container slot (slot 6) for the empty vessels a recipe needs: glass bottles for tea, bowls for soup. In Farm & Charm 1.1.22 `CookingPotBlockEntity.craft(...)` shrinks that slot only when the item has a `craftingRemainingItem`:

```java
ItemStack containerSlotStack = getItem(CONTAINER_SLOT);
if (!containerSlotStack.isEmpty()
        && containerSlotStack.getItem().hasCraftingRemainingItem()) {
    // shrink + put remainder back
}
```

Empty glass bottles, bowls and cups have no crafting remainder, so the block is skipped and the container is never consumed. Meanwhile water bottles used as ingredients return empty bottles, and finished drinks return their containers again. Every cook cycle adds containers to the economy.

Affected recipes include `nettle_tea`, `ribwort_tea` and `strawberry_tea` (`minecraft:glass_bottle`), and the soups that use `minecraft:bowl`. Upstream source: [CookingPotBlockEntity.java](https://github.com/Let-s-Do-Collection/FarmAndCharm/blob/1.21.1/common/src/main/java/net/satisfy/farm_and_charm/core/block/entity/CookingPotBlockEntity.java). Bug report: [Let-s-Do-Collection#1073](https://github.com/Let-s-Do-Collection/Let-s-Do-Collection/issues/1073).

### What the mod does

After a successful Cooking Pot craft (at the `TAIL` of `craft`), if the recipe requires a container and slot 6 still holds exactly that empty container item, the stack is shrunk by one. Water bottle ingredients keep their normal remainder behaviour.

### Installation

1. A NeoForge 1.21.1 server or client with Farm & Charm installed.
2. Download `farmcharm-cooking-pot-fix-<version>.jar` from [Releases](https://github.com/milkycloud-dev/farmcharm-cooking-pot-fix/releases) and put it into `mods/`.
3. Restart. The log shows `FarmCharm Cooking Pot Fix loaded`.

To check in game: put a Cooking Pot on heat, start a tea recipe with empty glass bottles in the container slot. After cooking, the number of bottles in that slot must go down.

### Releases

Every tag `v*` is built by GitHub Actions from this source and published in [Releases](../../releases). Farm & Charm 1.1.22 is downloaded from Modrinth during the build for compilation only and is not bundled. The build log of each release is public, so every jar can be traced to its commit.

### Known limitations

- Only the Cooking Pot is patched. Other Farm & Charm stations were not checked for the same pattern.
- The fix consumes one container per craft. A recipe that should use more than one container per result would still leak.
- If Farm & Charm fixes the bug upstream, this mod and the upstream fix together would consume two containers. Remove the mod after updating Farm & Charm to a fixed version.

### Links to the original mod

- Modrinth: https://modrinth.com/mod/lets-do-farm-charm
- CurseForge: https://www.curseforge.com/minecraft/mc-mods/lets-do-farm-charm
- Source: https://github.com/Let-s-Do-Collection/FarmAndCharm

This is an unofficial fix. It is not affiliated with or endorsed by the Let's Do team, and Farm & Charm stays under its own license.

### License

Proprietary, all rights reserved. Official release binaries may be run unmodified on servers you operate. Copying, modifying or redistributing the code or the binaries requires written permission. Full terms: [LICENSE](LICENSE). Version 1.0.0 was published under MIT and remains available under MIT.

<a id="русский"></a>

## Русский

### Обзор

| | |
|---|---|
| Minecraft | 1.21.1 |
| Загрузчик | NeoForge 21.1.x |
| Зависимость | [Farm & Charm](https://modrinth.com/mod/lets-do-farm-charm) 1.1.x, проверено на 1.1.22 |
| Сторона | сервер (и одиночная игра) |

### Проблема

У **котелка** (Cooking Pot) из Farm & Charm есть слот для тары (слот 6), куда кладут пустую посуду для рецепта: стеклянные бутылки для чая, миски для супа. В Farm & Charm 1.1.22 `CookingPotBlockEntity.craft(...)` уменьшает этот слот, только если у предмета есть `craftingRemainingItem`:

```java
ItemStack containerSlotStack = getItem(CONTAINER_SLOT);
if (!containerSlotStack.isEmpty()
        && containerSlotStack.getItem().hasCraftingRemainingItem()) {
    // shrink + put remainder back
}
```

У пустых бутылок, мисок и чашек остатка крафта нет, поэтому блок пропускается и тара не тратится. При этом бутылки с водой в ингредиентах возвращают пустые бутылки, а готовые напитки снова отдают тару. Каждый цикл готовки добавляет посуду в экономику.

Затронуты рецепты `nettle_tea`, `ribwort_tea`, `strawberry_tea` (`minecraft:glass_bottle`) и супы с `minecraft:bowl`. Исходник мода: [CookingPotBlockEntity.java](https://github.com/Let-s-Do-Collection/FarmAndCharm/blob/1.21.1/common/src/main/java/net/satisfy/farm_and_charm/core/block/entity/CookingPotBlockEntity.java). Отчёт об ошибке: [Let-s-Do-Collection#1073](https://github.com/Let-s-Do-Collection/Let-s-Do-Collection/issues/1073).

### Что делает мод

После успешной готовки в котелке (в конце `craft`), если рецепту нужна тара и в слоте 6 всё ещё лежит именно эта пустая тара, стопка уменьшается на один. Остаток от бутылок с водой в ингредиентах работает как обычно.

### Установка

1. Сервер или клиент NeoForge 1.21.1 с установленным Farm & Charm.
2. Скачать `farmcharm-cooking-pot-fix-<версия>.jar` из [Releases](https://github.com/milkycloud-dev/farmcharm-cooking-pot-fix/releases) и положить в `mods/`.
3. Перезапустить. В логе появится `FarmCharm Cooking Pot Fix loaded`.

Проверка в игре: поставить котелок на огонь, запустить рецепт чая с пустыми бутылками в слоте тары. После готовки бутылок в этом слоте должно стать меньше.

### Релизы

Каждый тег `v*` собирается из этих исходников в GitHub Actions и публикуется в [Releases](../../releases). Farm & Charm 1.1.22 скачивается с Modrinth во время сборки только для компиляции и в jar не входит. Лог сборки каждого релиза открыт, так что любой jar можно сверить с его коммитом.

### Известные ограничения

- Исправлен только котелок. Другие станции Farm & Charm на ту же ошибку не проверялись.
- Мод тратит одну тару за готовку. Рецепт, которому нужно больше одной тары на результат, всё равно будет давать излишек.
- Если Farm & Charm исправит ошибку у себя, вместе с этим модом тара будет тратиться дважды. После обновления Farm & Charm до исправленной версии мод нужно убрать.

### Ссылки на оригинальный мод

- Modrinth: https://modrinth.com/mod/lets-do-farm-charm
- CurseForge: https://www.curseforge.com/minecraft/mc-mods/lets-do-farm-charm
- Исходники: https://github.com/Let-s-Do-Collection/FarmAndCharm

Это неофициальное исправление. Оно не связано с командой Let's Do и не одобрено ею, Farm & Charm остаётся под своей лицензией.

### Лицензия

Проприетарная, все права защищены. Официальные сборки из релизов можно запускать без изменений на своих серверах. Копировать, изменять и распространять код или сборки можно только с письменного разрешения. Полный текст: [LICENSE](LICENSE). Версия 1.0.0 выходила под MIT и под MIT и остаётся.
