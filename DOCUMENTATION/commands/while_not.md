[<-- Back to Legend](../legend.md)

# Command Name: While Not
Keyword: while_not

### Usages
```
while_not <num>? true {}
while_not <num>? false {}
while_not <num>? holding <identifier> {}
while_not <num>? off_holding <identifier> {}
while_not <num>? target_block <identifier> {}
while_not <num>? target_entity <identifier> {}
while_not <num>? targeting_block {}
while_not <num>? targeting_entity {}
while_not <num>? inventory_has <identifier> {}
while_not <num>? equipment_has <identifier> {}
while_not <num>? hotbar_has <identifier> {}
while_not <num>? input_active attack {}
while_not <num>? input_active mouse_wheel_up {}
while_not <num>? input_active mouse_wheel_down {}
while_not <num>? input_active use {}
while_not <num>? input_active forward {}
while_not <num>? input_active backward {}
while_not <num>? input_active strafe_left {}
while_not <num>? input_active strafe_right {}
while_not <num>? input_active jump {}
while_not <num>? input_active sprint {}
while_not <num>? input_active sneak {}
while_not <num>? input_active lock_cursor {}
while_not <num>? input_active unlock_cursor {}
while_not <num>? input_active left {}
while_not <num>? input_active right {}
while_not <num>? input_active middle {}
while_not <num>? input_active inventory {}
while_not <num>? input_active key ... {}
while_not <num>? block_in_range <identifier> <num> {}
while_not <num>? entity_in_range <identifier> <num> {}
while_not <num>? attack_progress <comparator> <num> {}
while_not <num>? health <comparator> <num> {}
while_not <num>? hunger <comparator> <num> {}
while_not <num>? hurt_time <comparator> <int> {}
while_not <num>? armor <comparator> <int> {}
while_not <num>? pos_x <comparator> <num> {}
while_not <num>? pos_y <comparator> <num> {}
while_not <num>? pos_z <comparator> <num> {}
while_not <num>? vel_x <comparator> <num> {}
while_not <num>? vel_y <comparator> <num> {}
while_not <num>? vel_z <comparator> <num> {}
while_not <num>? module_enabled ... {}
while_not <num>? module_disabled ... {}
while_not <num>? block <x> <y> <z> {}
while_not <num>? entity <x> <y> <z> {}
while_not <num>? dimension overworld {}
while_not <num>? dimension the_nether {}
while_not <num>? dimension the_end {}
while_not <num>? <identifier> <comparator> <int> {}
while_not <num>? in_game {}
while_not <num>? in_singleplayer {}
while_not <num>? playing {}
while_not <num>? in_screen {}
while_not <num>? chance_of <num> {}
while_not <num>? colliding {}
while_not <num>? colliding_horizontally {}
while_not <num>? colliding_vertically {}
while_not <num>? jumping {}
while_not <num>? moving {}
while_not <num>? blocking {}
while_not <num>? on_ground {}
while_not <num>? on_fire {}
while_not <num>? frozen {}
while_not <num>? dead {}
while_not <num>? alive {}
while_not <num>? falling {}
while_not <num>? cursor_item <identifier> {}
while_not <num>? hovering_over <identifier> {}
while_not <num>? reference_entity client {}
while_not <num>? reference_entity any_entity {}
while_not <num>? reference_entity <identifier> {}
while_not <num>? <identifier> <comparator> <num> {}
while_not <num>? gamemode creative {}
while_not <num>? gamemode survival {}
while_not <num>? gamemode adventure {}
while_not <num>? gamemode spectator {}
while_not <num>? line_of_sight {}
while_not <num>? flying {}
while_not <num>? sneaking {}
while_not <num>? sprinting {}
while_not <num>? swimming {}
while_not <num>? gliding {}
```

### Regex
```regexp
(((while_not)( (-?\d*(\.\d*)?))?( (true)))|((while_not)( (-?\d*(\.\d*)?))?( (false)))|((while_not)( (-?\d*(\.\d*)?))?( (holding))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while_not)( (-?\d*(\.\d*)?))?( (off_holding))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while_not)( (-?\d*(\.\d*)?))?( (target_block))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while_not)( (-?\d*(\.\d*)?))?( (target_entity))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while_not)( (-?\d*(\.\d*)?))?( (targeting_block)))|((while_not)( (-?\d*(\.\d*)?))?( (targeting_entity)))|((while_not)( (-?\d*(\.\d*)?))?( (inventory_has))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while_not)( (-?\d*(\.\d*)?))?( (equipment_has))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while_not)( (-?\d*(\.\d*)?))?( (hotbar_has))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while_not)( (-?\d*(\.\d*)?))?( (input_active))( (attack|mouse_wheel_up|mouse_wheel_down|use|forward|backward|strafe_left|strafe_right|jump|sprint|sneak|lock_cursor|unlock_cursor|left|right|middle|inventory)))|((while_not)( (-?\d*(\.\d*)?))?( (input_active))( (key))( (\S+)))|((while_not)( (-?\d*(\.\d*)?))?( (block_in_range))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (entity_in_range))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (attack_progress))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (health))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (hunger))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (hurt_time))( ([<>=!]=)|[<>=])( (-?\d+)))|((while_not)( (-?\d*(\.\d*)?))?( (armor))( ([<>=!]=)|[<>=])( (-?\d+)))|((while_not)( (-?\d*(\.\d*)?))?( (pos_x))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (pos_y))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (pos_z))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (vel_x))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (vel_y))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (vel_z))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (module_enabled))( (\S+)))|((while_not)( (-?\d*(\.\d*)?))?( (module_disabled))( (\S+)))|((while_not)( (-?\d*(\.\d*)?))?( (block))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (entity))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (dimension))( (overworld|the_nether|the_end)))|((while_not)( (-?\d*(\.\d*)?))?( !?(([#:](\w+)(\[(.*)\])?,?)+))( ([<>=!]=)|[<>=])( (-?\d+)))|((while_not)( (-?\d*(\.\d*)?))?( (in_game)))|((while_not)( (-?\d*(\.\d*)?))?( (in_singleplayer)))|((while_not)( (-?\d*(\.\d*)?))?( (playing)))|((while_not)( (-?\d*(\.\d*)?))?( (in_screen)))|((while_not)( (-?\d*(\.\d*)?))?( (chance_of))( (-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (colliding)))|((while_not)( (-?\d*(\.\d*)?))?( (colliding_horizontally)))|((while_not)( (-?\d*(\.\d*)?))?( (colliding_vertically)))|((while_not)( (-?\d*(\.\d*)?))?( (jumping)))|((while_not)( (-?\d*(\.\d*)?))?( (moving)))|((while_not)( (-?\d*(\.\d*)?))?( (blocking)))|((while_not)( (-?\d*(\.\d*)?))?( (on_ground)))|((while_not)( (-?\d*(\.\d*)?))?( (on_fire)))|((while_not)( (-?\d*(\.\d*)?))?( (frozen)))|((while_not)( (-?\d*(\.\d*)?))?( (dead)))|((while_not)( (-?\d*(\.\d*)?))?( (alive)))|((while_not)( (-?\d*(\.\d*)?))?( (falling)))|((while_not)( (-?\d*(\.\d*)?))?( (cursor_item))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while_not)( (-?\d*(\.\d*)?))?( (hovering_over))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while_not)( (-?\d*(\.\d*)?))?( (reference_entity))( (client|any_entity)))|((while_not)( (-?\d*(\.\d*)?))?( (reference_entity))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while_not)( (-?\d*(\.\d*)?))?( !?(([#:](\w+)(\[(.*)\])?,?)+))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while_not)( (-?\d*(\.\d*)?))?( (gamemode))( (creative|survival|adventure|spectator)))|((while_not)( (-?\d*(\.\d*)?))?( (line_of_sight)))|((while_not)( (-?\d*(\.\d*)?))?( (flying)))|((while_not)( (-?\d*(\.\d*)?))?( (sneaking)))|((while_not)( (-?\d*(\.\d*)?))?( (sprinting)))|((while_not)( (-?\d*(\.\d*)?))?( (swimming)))|((while_not)( (-?\d*(\.\d*)?))?( (gliding))))
```

### Raw Documentation
```yml
# while_not <num>? true {}
# while_not <num>? false {}
# while_not <num>? holding <identifier> {}
# while_not <num>? off_holding <identifier> {}
# while_not <num>? target_block <identifier> {}
# while_not <num>? target_entity <identifier> {}
# while_not <num>? targeting_block {}
# while_not <num>? targeting_entity {}
# while_not <num>? inventory_has <identifier> {}
# while_not <num>? equipment_has <identifier> {}
# while_not <num>? hotbar_has <identifier> {}
# while_not <num>? input_active (attack|mouse_wheel_up|mouse_wheel_down|use|forward|backward|strafe_left|strafe_right|jump|sprint|sneak|lock_cursor|unlock_cursor|left|right|middle|inventory) {}
# while_not <num>? input_active key ... {}
# while_not <num>? block_in_range <identifier> <num> {}
# while_not <num>? entity_in_range <identifier> <num> {}
# while_not <num>? attack_progress <comparator> <num> {}
# while_not <num>? health <comparator> <num> {}
# while_not <num>? hunger <comparator> <num> {}
# while_not <num>? hurt_time <comparator> <int> {}
# while_not <num>? armor <comparator> <int> {}
# while_not <num>? pos_x <comparator> <num> {}
# while_not <num>? pos_y <comparator> <num> {}
# while_not <num>? pos_z <comparator> <num> {}
# while_not <num>? vel_x <comparator> <num> {}
# while_not <num>? vel_y <comparator> <num> {}
# while_not <num>? vel_z <comparator> <num> {}
# while_not <num>? module_enabled ... {}
# while_not <num>? module_disabled ... {}
# while_not <num>? block <x> <y> <z> {}
# while_not <num>? entity <x> <y> <z> {}
# while_not <num>? dimension (overworld|the_nether|the_end) {}
# while_not <num>? <identifier> <comparator> <int> {}
# while_not <num>? in_game {}
# while_not <num>? in_singleplayer {}
# while_not <num>? playing {}
# while_not <num>? in_screen {}
# while_not <num>? chance_of <num> {}
# while_not <num>? colliding {}
# while_not <num>? colliding_horizontally {}
# while_not <num>? colliding_vertically {}
# while_not <num>? jumping {}
# while_not <num>? moving {}
# while_not <num>? blocking {}
# while_not <num>? on_ground {}
# while_not <num>? on_fire {}
# while_not <num>? frozen {}
# while_not <num>? dead {}
# while_not <num>? alive {}
# while_not <num>? falling {}
# while_not <num>? cursor_item <identifier> {}
# while_not <num>? hovering_over <identifier> {}
# while_not <num>? reference_entity (client|any_entity) {}
# while_not <num>? reference_entity <identifier> {}
# while_not <num>? <identifier> <comparator> <num> {}
# while_not <num>? gamemode (creative|survival|adventure|spectator) {}
# while_not <num>? line_of_sight {}
# while_not <num>? flying {}
# while_not <num>? sneaking {}
# while_not <num>? sprinting {}
# while_not <num>? swimming {}
# while_not <num>? gliding {}
```
