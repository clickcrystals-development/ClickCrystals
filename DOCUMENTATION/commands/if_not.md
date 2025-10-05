[<-- Back to Legend](../legend.md)

# Command Name: If Not
Keyword: if_not

### Usages
```
if_not alive {}
if_not armor <comparator> <int> {}
if_not attack_progress <comparator> <num> {}
if_not block <x> <y> <z> {}
if_not block_in_range <identifier> <num> {}
if_not blocking {}
if_not chance_of <num> {}
if_not colliding {}
if_not colliding_horizontally {}
if_not colliding_vertically {}
if_not cursor_item <identifier> {}
if_not dead {}
if_not dimension overworld {}
if_not dimension the_end {}
if_not dimension the_nether {}
if_not effect_amplifier <identifier> <comparator> <int> {}
if_not effect_duration <identifier> <comparator> <int> {}
if_not entity <x> <y> <z> {}
if_not entity_in_range <identifier> <num> {}
if_not equipment_has <identifier> {}
if_not falling {}
if_not false {}
if_not flying {}
if_not fps <comparator> <int> {}
if_not frozen {}
if_not gamemode adventure {}
if_not gamemode creative {}
if_not gamemode spectator {}
if_not gamemode survival {}
if_not gliding {}
if_not health <comparator> <num> {}
if_not holding <identifier> {}
if_not hotbar_has <identifier> {}
if_not hovering_over <identifier> {}
if_not hunger <comparator> <num> {}
if_not hurt_time <comparator> <int> {}
if_not in_game {}
if_not in_screen {}
if_not in_singleplayer {}
if_not input_active attack {}
if_not input_active backward {}
if_not input_active forward {}
if_not input_active inventory {}
if_not input_active jump {}
if_not input_active key ... {}
if_not input_active left {}
if_not input_active lock_cursor {}
if_not input_active middle {}
if_not input_active mouse_wheel_down {}
if_not input_active mouse_wheel_up {}
if_not input_active right {}
if_not input_active sneak {}
if_not input_active sprint {}
if_not input_active strafe_left {}
if_not input_active strafe_right {}
if_not input_active unlock_cursor {}
if_not input_active use {}
if_not inventory_has <identifier> {}
if_not item_cooldown <identifier> <comparator> <num> {}
if_not item_cooldown mainhand <comparator> <num> {}
if_not item_cooldown offhand <comparator> <num> {}
if_not item_count <identifier> <comparator> <int> {}
if_not item_count mainhand <comparator> <int> {}
if_not item_count offhand <comparator> <int> {}
if_not item_durability <identifier> <comparator> <num> {}
if_not item_durability mainhand <comparator> <num> {}
if_not item_durability offhand <comparator> <num> {}
if_not jumping {}
if_not line_of_sight {}
if_not module_disabled ... {}
if_not module_enabled ... {}
if_not moving {}
if_not off_holding <identifier> {}
if_not on_fire {}
if_not on_ground {}
if_not ping <comparator> <int> {}
if_not playing {}
if_not pos_x <comparator> <num> {}
if_not pos_y <comparator> <num> {}
if_not pos_z <comparator> <num> {}
if_not reference_entity <identifier> {}
if_not reference_entity any_entity {}
if_not reference_entity client {}
if_not sneaking {}
if_not sprinting {}
if_not swimming {}
if_not target_block <identifier> {}
if_not target_entity <identifier> {}
if_not target_fluid <identifier> {}
if_not targeting_block {}
if_not targeting_entity {}
if_not targeting_fluid {}
if_not true {}
if_not vel_x <comparator> <num> {}
if_not vel_y <comparator> <num> {}
if_not vel_z <comparator> <num> {}
```

### Regex
```regexp
(((if_not)( (true)))|((if_not)( (false)))|((if_not)( (holding))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if_not)( (off_holding))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if_not)( (target_block))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if_not)( (target_entity))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if_not)( (target_fluid))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if_not)( (targeting_block)))|((if_not)( (targeting_entity)))|((if_not)( (targeting_fluid)))|((if_not)( (inventory_has))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if_not)( (equipment_has))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if_not)( (hotbar_has))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if_not)( (input_active))( (attack|use|forward|backward|strafe_left|strafe_right|jump|sprint|sneak|lock_cursor|unlock_cursor|left|right|middle|inventory|mouse_wheel_up|mouse_wheel_down)))|((if_not)( (input_active))( (key))( (\S+)))|((if_not)( (block_in_range))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (-?\d*(\.\d*)?)))|((if_not)( (entity_in_range))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (-?\d*(\.\d*)?)))|((if_not)( (attack_progress))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if_not)( (health))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if_not)( (hunger))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if_not)( (hurt_time))( ([<>=!]=)|[<>=])( (-?\d+)))|((if_not)( (armor))( ([<>=!]=)|[<>=])( (-?\d+)))|((if_not)( (pos_x))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if_not)( (pos_y))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if_not)( (pos_z))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if_not)( (vel_x))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if_not)( (vel_y))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if_not)( (vel_z))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if_not)( (module_enabled))( (\S+)))|((if_not)( (module_disabled))( (\S+)))|((if_not)( (block))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?)))|((if_not)( (entity))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?)))|((if_not)( (dimension))( (overworld|the_nether|the_end)))|((if_not)( (effect_amplifier))( !?(([#:](\w+)(\[(.*)\])?,?)+))( ([<>=!]=)|[<>=])( (-?\d+)))|((if_not)( (effect_duration))( !?(([#:](\w+)(\[(.*)\])?,?)+))( ([<>=!]=)|[<>=])( (-?\d+)))|((if_not)( (in_game)))|((if_not)( (in_singleplayer)))|((if_not)( (playing)))|((if_not)( (in_screen)))|((if_not)( (chance_of))( (-?\d*(\.\d*)?)))|((if_not)( (colliding)))|((if_not)( (colliding_horizontally)))|((if_not)( (colliding_vertically)))|((if_not)( (jumping)))|((if_not)( (moving)))|((if_not)( (blocking)))|((if_not)( (on_ground)))|((if_not)( (on_fire)))|((if_not)( (frozen)))|((if_not)( (dead)))|((if_not)( (alive)))|((if_not)( (falling)))|((if_not)( (cursor_item))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if_not)( (hovering_over))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if_not)( (reference_entity))( (client|any_entity)))|((if_not)( (reference_entity))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if_not)( (item_count))( !?(([#:](\w+)(\[(.*)\])?,?)+))( ([<>=!]=)|[<>=])( (-?\d+)))|((if_not)( (item_count))( (mainhand|offhand))( ([<>=!]=)|[<>=])( (-?\d+)))|((if_not)( (item_durability))( !?(([#:](\w+)(\[(.*)\])?,?)+))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if_not)( (item_durability))( (mainhand|offhand))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if_not)( (item_cooldown))( !?(([#:](\w+)(\[(.*)\])?,?)+))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if_not)( (item_cooldown))( (mainhand|offhand))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if_not)( (gamemode))( (creative|survival|adventure|spectator)))|((if_not)( (ping))( ([<>=!]=)|[<>=])( (-?\d+)))|((if_not)( (fps))( ([<>=!]=)|[<>=])( (-?\d+)))|((if_not)( (line_of_sight)))|((if_not)( (flying)))|((if_not)( (sneaking)))|((if_not)( (sprinting)))|((if_not)( (swimming)))|((if_not)( (gliding))))
```

### Raw Documentation
```yml
# if_not true {}
# if_not false {}
# if_not holding <identifier> {}
# if_not off_holding <identifier> {}
# if_not target_block <identifier> {}
# if_not target_entity <identifier> {}
# if_not target_fluid <identifier> {}
# if_not targeting_block {}
# if_not targeting_entity {}
# if_not targeting_fluid {}
# if_not inventory_has <identifier> {}
# if_not equipment_has <identifier> {}
# if_not hotbar_has <identifier> {}
# if_not input_active <input> {}
# if_not input_active key ... {}
# if_not block_in_range <identifier> <num> {}
# if_not entity_in_range <identifier> <num> {}
# if_not attack_progress <comparator> <num> {}
# if_not health <comparator> <num> {}
# if_not hunger <comparator> <num> {}
# if_not hurt_time <comparator> <int> {}
# if_not armor <comparator> <int> {}
# if_not pos_x <comparator> <num> {}
# if_not pos_y <comparator> <num> {}
# if_not pos_z <comparator> <num> {}
# if_not vel_x <comparator> <num> {}
# if_not vel_y <comparator> <num> {}
# if_not vel_z <comparator> <num> {}
# if_not module_enabled ... {}
# if_not module_disabled ... {}
# if_not block <x> <y> <z> {}
# if_not entity <x> <y> <z> {}
# if_not dimension (overworld|the_nether|the_end) {}
# if_not effect_amplifier <identifier> <comparator> <int> {}
# if_not effect_duration <identifier> <comparator> <int> {}
# if_not in_game {}
# if_not in_singleplayer {}
# if_not playing {}
# if_not in_screen {}
# if_not chance_of <num> {}
# if_not colliding {}
# if_not colliding_horizontally {}
# if_not colliding_vertically {}
# if_not jumping {}
# if_not moving {}
# if_not blocking {}
# if_not on_ground {}
# if_not on_fire {}
# if_not frozen {}
# if_not dead {}
# if_not alive {}
# if_not falling {}
# if_not cursor_item <identifier> {}
# if_not hovering_over <identifier> {}
# if_not reference_entity (client|any_entity) {}
# if_not reference_entity <identifier> {}
# if_not item_count <identifier> <comparator> <int> {}
# if_not item_count (mainhand|offhand) <comparator> <int> {}
# if_not item_durability <identifier> <comparator> <num> {}
# if_not item_durability (mainhand|offhand) <comparator> <num> {}
# if_not item_cooldown <identifier> <comparator> <num> {}
# if_not item_cooldown (mainhand|offhand) <comparator> <num> {}
# if_not gamemode (creative|survival|adventure|spectator) {}
# if_not ping <comparator> <int> {}
# if_not fps <comparator> <int> {}
# if_not line_of_sight {}
# if_not flying {}
# if_not sneaking {}
# if_not sprinting {}
# if_not swimming {}
# if_not gliding {}
```
