[<-- Back to Legend](../legend.md)

# Command Name: If
Keyword: if

### Usages
```
if true {}
if false {}
if holding <identifier> {}
if off_holding <identifier> {}
if target_block <identifier> {}
if target_entity <identifier> {}
if targeting_block {}
if targeting_entity {}
if inventory_has <identifier> {}
if equipment_has <identifier> {}
if hotbar_has <identifier> {}
if input_active attack {}
if input_active mouse_wheel_up {}
if input_active mouse_wheel_down {}
if input_active use {}
if input_active forward {}
if input_active backward {}
if input_active strafe_left {}
if input_active strafe_right {}
if input_active jump {}
if input_active sprint {}
if input_active sneak {}
if input_active lock_cursor {}
if input_active unlock_cursor {}
if input_active left {}
if input_active right {}
if input_active middle {}
if input_active inventory {}
if input_active key ... {}
if block_in_range <identifier> <num> {}
if entity_in_range <identifier> <num> {}
if attack_progress <comparator> <num> {}
if health <comparator> <num> {}
if hunger <comparator> <num> {}
if hurt_time <comparator> <int> {}
if armor <comparator> <int> {}
if pos_x <comparator> <num> {}
if pos_y <comparator> <num> {}
if pos_z <comparator> <num> {}
if vel_x <comparator> <num> {}
if vel_y <comparator> <num> {}
if vel_z <comparator> <num> {}
if module_enabled ... {}
if module_disabled ... {}
if block <x> <y> <z> {}
if entity <x> <y> <z> {}
if dimension overworld {}
if dimension the_nether {}
if dimension the_end {}
if effect_amplifier <identifier> <comparator> <int> {}
if effect_duration <identifier> <comparator> <int> {}
if in_game {}
if in_singleplayer {}
if playing {}
if in_screen {}
if chance_of <num> {}
if colliding {}
if colliding_horizontally {}
if colliding_vertically {}
if jumping {}
if moving {}
if blocking {}
if on_ground {}
if on_fire {}
if frozen {}
if dead {}
if alive {}
if falling {}
if cursor_item <identifier> {}
if hovering_over <identifier> {}
if reference_entity client {}
if reference_entity any_entity {}
if reference_entity <identifier> {}
if <identifier> <comparator> <int> {}
if <identifier> <comparator> <num> {}
if gamemode creative {}
if gamemode survival {}
if gamemode adventure {}
if gamemode spectator {}
if line_of_sight {}
if flying {}
if sneaking {}
if sprinting {}
if swimming {}
if gliding {}
```

### Regex
```regexp
(((if)( (true)))|((if)( (false)))|((if)( (holding))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if)( (off_holding))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if)( (target_block))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if)( (target_entity))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if)( (targeting_block)))|((if)( (targeting_entity)))|((if)( (inventory_has))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if)( (equipment_has))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if)( (hotbar_has))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if)( (input_active))( (attack|mouse_wheel_up|mouse_wheel_down|use|forward|backward|strafe_left|strafe_right|jump|sprint|sneak|lock_cursor|unlock_cursor|left|right|middle|inventory)))|((if)( (input_active))( (key))( (\S+)))|((if)( (block_in_range))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (-?\d*(\.\d*)?)))|((if)( (entity_in_range))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (-?\d*(\.\d*)?)))|((if)( (attack_progress))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if)( (health))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if)( (hunger))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if)( (hurt_time))( ([<>=!]=)|[<>=])( (-?\d+)))|((if)( (armor))( ([<>=!]=)|[<>=])( (-?\d+)))|((if)( (pos_x))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if)( (pos_y))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if)( (pos_z))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if)( (vel_x))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if)( (vel_y))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if)( (vel_z))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if)( (module_enabled))( (\S+)))|((if)( (module_disabled))( (\S+)))|((if)( (block))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?)))|((if)( (entity))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?)))|((if)( (dimension))( (overworld|the_nether|the_end)))|((if)( (effect_amplifier))( !?(([#:](\w+)(\[(.*)\])?,?)+))( ([<>=!]=)|[<>=])( (-?\d+)))|((if)( (effect_duration))( !?(([#:](\w+)(\[(.*)\])?,?)+))( ([<>=!]=)|[<>=])( (-?\d+)))|((if)( (in_game)))|((if)( (in_singleplayer)))|((if)( (playing)))|((if)( (in_screen)))|((if)( (chance_of))( (-?\d*(\.\d*)?)))|((if)( (colliding)))|((if)( (colliding_horizontally)))|((if)( (colliding_vertically)))|((if)( (jumping)))|((if)( (moving)))|((if)( (blocking)))|((if)( (on_ground)))|((if)( (on_fire)))|((if)( (frozen)))|((if)( (dead)))|((if)( (alive)))|((if)( (falling)))|((if)( (cursor_item))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if)( (hovering_over))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if)( (reference_entity))( (client|any_entity)))|((if)( (reference_entity))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((if)( !?(([#:](\w+)(\[(.*)\])?,?)+))( ([<>=!]=)|[<>=])( (-?\d+)))|((if)( !?(([#:](\w+)(\[(.*)\])?,?)+))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((if)( (gamemode))( (creative|survival|adventure|spectator)))|((if)( (line_of_sight)))|((if)( (flying)))|((if)( (sneaking)))|((if)( (sprinting)))|((if)( (swimming)))|((if)( (gliding))))
```

### Raw Documentation
```yml
# if true {}
# if false {}
# if holding <identifier> {}
# if off_holding <identifier> {}
# if target_block <identifier> {}
# if target_entity <identifier> {}
# if targeting_block {}
# if targeting_entity {}
# if inventory_has <identifier> {}
# if equipment_has <identifier> {}
# if hotbar_has <identifier> {}
# if input_active (attack|mouse_wheel_up|mouse_wheel_down|use|forward|backward|strafe_left|strafe_right|jump|sprint|sneak|lock_cursor|unlock_cursor|left|right|middle|inventory) {}
# if input_active key ... {}
# if block_in_range <identifier> <num> {}
# if entity_in_range <identifier> <num> {}
# if attack_progress <comparator> <num> {}
# if health <comparator> <num> {}
# if hunger <comparator> <num> {}
# if hurt_time <comparator> <int> {}
# if armor <comparator> <int> {}
# if pos_x <comparator> <num> {}
# if pos_y <comparator> <num> {}
# if pos_z <comparator> <num> {}
# if vel_x <comparator> <num> {}
# if vel_y <comparator> <num> {}
# if vel_z <comparator> <num> {}
# if module_enabled ... {}
# if module_disabled ... {}
# if block <x> <y> <z> {}
# if entity <x> <y> <z> {}
# if dimension (overworld|the_nether|the_end) {}
# if effect_amplifier <identifier> <comparator> <int> {}
# if effect_duration <identifier> <comparator> <int> {}
# if in_game {}
# if in_singleplayer {}
# if playing {}
# if in_screen {}
# if chance_of <num> {}
# if colliding {}
# if colliding_horizontally {}
# if colliding_vertically {}
# if jumping {}
# if moving {}
# if blocking {}
# if on_ground {}
# if on_fire {}
# if frozen {}
# if dead {}
# if alive {}
# if falling {}
# if cursor_item <identifier> {}
# if hovering_over <identifier> {}
# if reference_entity (client|any_entity) {}
# if reference_entity <identifier> {}
# if <identifier> <comparator> <int> {}
# if <identifier> <comparator> <num> {}
# if gamemode (creative|survival|adventure|spectator) {}
# if line_of_sight {}
# if flying {}
# if sneaking {}
# if sprinting {}
# if swimming {}
# if gliding {}
```
