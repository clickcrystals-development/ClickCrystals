[<-- Back to Legend](../legend.md)

# Command Name: While
Keyword: while

### Usages
```
while <num>? true {}
while <num>? false {}
while <num>? holding <identifier> {}
while <num>? off_holding <identifier> {}
while <num>? target_block <identifier> {}
while <num>? target_entity <identifier> {}
while <num>? targeting_block {}
while <num>? targeting_entity {}
while <num>? inventory_has <identifier> {}
while <num>? equipment_has <identifier> {}
while <num>? hotbar_has <identifier> {}
while <num>? input_active attack {}
while <num>? input_active mouse_wheel_up {}
while <num>? input_active mouse_wheel_down {}
while <num>? input_active use {}
while <num>? input_active forward {}
while <num>? input_active backward {}
while <num>? input_active strafe_left {}
while <num>? input_active strafe_right {}
while <num>? input_active jump {}
while <num>? input_active sprint {}
while <num>? input_active sneak {}
while <num>? input_active lock_cursor {}
while <num>? input_active unlock_cursor {}
while <num>? input_active left {}
while <num>? input_active right {}
while <num>? input_active middle {}
while <num>? input_active inventory {}
while <num>? input_active key ... {}
while <num>? block_in_range <identifier> <num> {}
while <num>? entity_in_range <identifier> <num> {}
while <num>? attack_progress <comparator> <num> {}
while <num>? health <comparator> <num> {}
while <num>? hunger <comparator> <num> {}
while <num>? hurt_time <comparator> <int> {}
while <num>? armor <comparator> <int> {}
while <num>? pos_x <comparator> <num> {}
while <num>? pos_y <comparator> <num> {}
while <num>? pos_z <comparator> <num> {}
while <num>? vel_x <comparator> <num> {}
while <num>? vel_y <comparator> <num> {}
while <num>? vel_z <comparator> <num> {}
while <num>? module_enabled ... {}
while <num>? module_disabled ... {}
while <num>? block <x> <y> <z> {}
while <num>? entity <x> <y> <z> {}
while <num>? dimension overworld {}
while <num>? dimension the_nether {}
while <num>? dimension the_end {}
while <num>? <identifier> <comparator> <int> {}
while <num>? in_game {}
while <num>? in_singleplayer {}
while <num>? playing {}
while <num>? in_screen {}
while <num>? chance_of <num> {}
while <num>? colliding {}
while <num>? colliding_horizontally {}
while <num>? colliding_vertically {}
while <num>? jumping {}
while <num>? moving {}
while <num>? blocking {}
while <num>? on_ground {}
while <num>? on_fire {}
while <num>? frozen {}
while <num>? dead {}
while <num>? alive {}
while <num>? falling {}
while <num>? cursor_item <identifier> {}
while <num>? hovering_over <identifier> {}
while <num>? reference_entity client {}
while <num>? reference_entity any_entity {}
while <num>? reference_entity <identifier> {}
while <num>? <identifier> <comparator> <num> {}
while <num>? gamemode creative {}
while <num>? gamemode survival {}
while <num>? gamemode adventure {}
while <num>? gamemode spectator {}
while <num>? line_of_sight {}
while <num>? flying {}
while <num>? sneaking {}
while <num>? sprinting {}
while <num>? swimming {}
while <num>? gliding {}
```

### Regex
```regexp
(((while)( (-?\d*(\.\d*)?))?( (true)))|((while)( (-?\d*(\.\d*)?))?( (false)))|((while)( (-?\d*(\.\d*)?))?( (holding))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while)( (-?\d*(\.\d*)?))?( (off_holding))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while)( (-?\d*(\.\d*)?))?( (target_block))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while)( (-?\d*(\.\d*)?))?( (target_entity))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while)( (-?\d*(\.\d*)?))?( (targeting_block)))|((while)( (-?\d*(\.\d*)?))?( (targeting_entity)))|((while)( (-?\d*(\.\d*)?))?( (inventory_has))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while)( (-?\d*(\.\d*)?))?( (equipment_has))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while)( (-?\d*(\.\d*)?))?( (hotbar_has))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while)( (-?\d*(\.\d*)?))?( (input_active))( (attack|mouse_wheel_up|mouse_wheel_down|use|forward|backward|strafe_left|strafe_right|jump|sprint|sneak|lock_cursor|unlock_cursor|left|right|middle|inventory)))|((while)( (-?\d*(\.\d*)?))?( (input_active))( (key))( (\S+)))|((while)( (-?\d*(\.\d*)?))?( (block_in_range))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (entity_in_range))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (attack_progress))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (health))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (hunger))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (hurt_time))( ([<>=!]=)|[<>=])( (-?\d+)))|((while)( (-?\d*(\.\d*)?))?( (armor))( ([<>=!]=)|[<>=])( (-?\d+)))|((while)( (-?\d*(\.\d*)?))?( (pos_x))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (pos_y))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (pos_z))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (vel_x))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (vel_y))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (vel_z))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (module_enabled))( (\S+)))|((while)( (-?\d*(\.\d*)?))?( (module_disabled))( (\S+)))|((while)( (-?\d*(\.\d*)?))?( (block))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (entity))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (dimension))( (overworld|the_nether|the_end)))|((while)( (-?\d*(\.\d*)?))?( !?(([#:](\w+)(\[(.*)\])?,?)+))( ([<>=!]=)|[<>=])( (-?\d+)))|((while)( (-?\d*(\.\d*)?))?( (in_game)))|((while)( (-?\d*(\.\d*)?))?( (in_singleplayer)))|((while)( (-?\d*(\.\d*)?))?( (playing)))|((while)( (-?\d*(\.\d*)?))?( (in_screen)))|((while)( (-?\d*(\.\d*)?))?( (chance_of))( (-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (colliding)))|((while)( (-?\d*(\.\d*)?))?( (colliding_horizontally)))|((while)( (-?\d*(\.\d*)?))?( (colliding_vertically)))|((while)( (-?\d*(\.\d*)?))?( (jumping)))|((while)( (-?\d*(\.\d*)?))?( (moving)))|((while)( (-?\d*(\.\d*)?))?( (blocking)))|((while)( (-?\d*(\.\d*)?))?( (on_ground)))|((while)( (-?\d*(\.\d*)?))?( (on_fire)))|((while)( (-?\d*(\.\d*)?))?( (frozen)))|((while)( (-?\d*(\.\d*)?))?( (dead)))|((while)( (-?\d*(\.\d*)?))?( (alive)))|((while)( (-?\d*(\.\d*)?))?( (falling)))|((while)( (-?\d*(\.\d*)?))?( (cursor_item))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while)( (-?\d*(\.\d*)?))?( (hovering_over))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while)( (-?\d*(\.\d*)?))?( (reference_entity))( (client|any_entity)))|((while)( (-?\d*(\.\d*)?))?( (reference_entity))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((while)( (-?\d*(\.\d*)?))?( !?(([#:](\w+)(\[(.*)\])?,?)+))( ([<>=!]=)|[<>=])( (-?\d*(\.\d*)?)))|((while)( (-?\d*(\.\d*)?))?( (gamemode))( (creative|survival|adventure|spectator)))|((while)( (-?\d*(\.\d*)?))?( (line_of_sight)))|((while)( (-?\d*(\.\d*)?))?( (flying)))|((while)( (-?\d*(\.\d*)?))?( (sneaking)))|((while)( (-?\d*(\.\d*)?))?( (sprinting)))|((while)( (-?\d*(\.\d*)?))?( (swimming)))|((while)( (-?\d*(\.\d*)?))?( (gliding))))
```

### Raw Documentation
```yml
# while <num>? true {}
# while <num>? false {}
# while <num>? holding <identifier> {}
# while <num>? off_holding <identifier> {}
# while <num>? target_block <identifier> {}
# while <num>? target_entity <identifier> {}
# while <num>? targeting_block {}
# while <num>? targeting_entity {}
# while <num>? inventory_has <identifier> {}
# while <num>? equipment_has <identifier> {}
# while <num>? hotbar_has <identifier> {}
# while <num>? input_active (attack|mouse_wheel_up|mouse_wheel_down|use|forward|backward|strafe_left|strafe_right|jump|sprint|sneak|lock_cursor|unlock_cursor|left|right|middle|inventory) {}
# while <num>? input_active key ... {}
# while <num>? block_in_range <identifier> <num> {}
# while <num>? entity_in_range <identifier> <num> {}
# while <num>? attack_progress <comparator> <num> {}
# while <num>? health <comparator> <num> {}
# while <num>? hunger <comparator> <num> {}
# while <num>? hurt_time <comparator> <int> {}
# while <num>? armor <comparator> <int> {}
# while <num>? pos_x <comparator> <num> {}
# while <num>? pos_y <comparator> <num> {}
# while <num>? pos_z <comparator> <num> {}
# while <num>? vel_x <comparator> <num> {}
# while <num>? vel_y <comparator> <num> {}
# while <num>? vel_z <comparator> <num> {}
# while <num>? module_enabled ... {}
# while <num>? module_disabled ... {}
# while <num>? block <x> <y> <z> {}
# while <num>? entity <x> <y> <z> {}
# while <num>? dimension (overworld|the_nether|the_end) {}
# while <num>? <identifier> <comparator> <int> {}
# while <num>? in_game {}
# while <num>? in_singleplayer {}
# while <num>? playing {}
# while <num>? in_screen {}
# while <num>? chance_of <num> {}
# while <num>? colliding {}
# while <num>? colliding_horizontally {}
# while <num>? colliding_vertically {}
# while <num>? jumping {}
# while <num>? moving {}
# while <num>? blocking {}
# while <num>? on_ground {}
# while <num>? on_fire {}
# while <num>? frozen {}
# while <num>? dead {}
# while <num>? alive {}
# while <num>? falling {}
# while <num>? cursor_item <identifier> {}
# while <num>? hovering_over <identifier> {}
# while <num>? reference_entity (client|any_entity) {}
# while <num>? reference_entity <identifier> {}
# while <num>? <identifier> <comparator> <num> {}
# while <num>? gamemode (creative|survival|adventure|spectator) {}
# while <num>? line_of_sight {}
# while <num>? flying {}
# while <num>? sneaking {}
# while <num>? sprinting {}
# while <num>? swimming {}
# while <num>? gliding {}
```
