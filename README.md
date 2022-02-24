# Spellbind - Minecraft Mod
**Bind spells** to items you are using.
_________
Spellbind is a Minecraft tweak util mod.

It allows modpack makers to make custom "spells" by following the steps below:
1. Specify an item ID, `minecraft:apple` for example.
2. Specify a mob entity ID, `minecraft:zombie` for example.
3. Specify some criteria. What I'm planning to implement is, check if the entity has a certain NBT tag.
4. Set a series of actions. 4 types of action are planned to implement: applying potion effects, executing commands, modifying attributes and manipulating NBT data.
5. Load this configuration.

After that,
1. when a player **finishes eating** an apple,
2. if he/she is **looking at** a zombie at the moment,
3. and the zombie meet the criteria, 

those actions specified in Step 4 will be applied to that zombie.
______
## To-do List
### Base Functions
[ ] Criteria system

[ ] Action type 1: Applying potion effects

[ ] Action type 2: Executing commands

[ ] Action type 3: Modifying attributes

[ ] Action type 4: Manipulating NBT data.
### Other Functions
More action types

More complex criteria

ZenScript or KubeJS support
________
This project is an open-source project under the MIT License.