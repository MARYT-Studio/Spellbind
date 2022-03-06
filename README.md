# Spellbind - Minecraft Mod
**Bind spells** to items you are using.
_________
Spellbind is a Minecraft tweak util mod.

It allows you to make custom "spells" by following the steps below:
1. Make a datapack that contains a `spellbind_spells` folder in it. All spells will be written in JSON format and be put here.
2. Specify an item ID. <br>This item should be "usable": You are able to hold your right mouse button to use it. Foods or Bows for example.<br>Here we will use `minecraft:apple`.
3. Specify your spell's attacking distance: how far (in blocks) can your spell reach.<br>It can be a float number, `10.0` for example.
4. Specify a mob entity ID, `minecraft:pig` for example, or use `#i_dont_care#` to let your spell to be effective to all types of mobs.
5. Specify some other criteria.<br>Now Spellbind provides NBT criterion, let your spell only to be effective to those mobs which has the specific NBT tag.<br>If you do not need this, put `#i_dont_care#` here.
6. Set a series of actions. Presently 4 types of actions are available:<br>a. applying potion effects,<br>b. executing commands,<br>c. modifying attributes<br>d. manipulating NBT data.
7. Execute vanilla `/reload` command.

After that,
1. when you **finish eating** an apple,
2. if you are **looking at** a mob, and it is within the range of a spell at the moment,
3. and it meets other criteria - now they are entity ID and NBT tag,
4. then you will see those actions specified in Step 4 applied to that mob.
______
## To-do List
### Base Functions
- [x] 🎉 Criteria system

- [x] 🎉 Action type 1: Applying potion effects

- [x] 🎉 Action type 2: Executing commands

- [x] 🎉 Action type 3: Modifying attributes

- [x] 🎉 Action type 4: Manipulating NBT data.
### Other Functions
More action types

More complex criteria

ZenScript or KubeJS support
________
This project is an open-source project under the MIT License.
