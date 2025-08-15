[<-- Back to Legend](../legend.md)

# Command Name: On
Keyword: on

### Usages
```
on right_click
on left_click
on middle_click
on mouse_click <int>
on right_release
on left_release
on middle_release
on mouse_release <int>
on place_block
on break_block
on punch_block
on interact_block
on pre_tick <int>?
on tick <int>?
on post_tick <int>?
on item_use
on item_consume
on totem_pop
on module_enable
on module_disable
on move_pos
on move_look
on key_press ...
on key_release ...
on damage
on respawn
on death
on game_join
on game_leave
on chat_send "..."
on chat_receive "..."
on packet_send ...
on packet_receive ...
on sound_play <identifier>
on mouse_wheel_up
on mouse_wheel_down
on mouse_wheel
```

### Regex
```regexp
(((on)( (right_click)))|((on)( (left_click)))|((on)( (middle_click)))|((on)( (mouse_click))( (-?\d+)))|((on)( (right_release)))|((on)( (left_release)))|((on)( (middle_release)))|((on)( (mouse_release))( (-?\d+)))|((on)( (place_block)))|((on)( (break_block)))|((on)( (punch_block)))|((on)( (interact_block)))|((on)( (pre_tick))( (-?\d+))?)|((on)( (tick))( (-?\d+))?)|((on)( (post_tick))( (-?\d+))?)|((on)( (item_use)))|((on)( (item_consume)))|((on)( (totem_pop)))|((on)( (module_enable)))|((on)( (module_disable)))|((on)( (move_pos)))|((on)( (move_look)))|((on)( (key_press))( (\S+)))|((on)( (key_release))( (\S+)))|((on)( (damage)))|((on)( (respawn)))|((on)( (death)))|((on)( (game_join)))|((on)( (game_leave)))|((on)( (chat_send))( (\".*?\")))|((on)( (chat_receive))( (\".*?\")))|((on)( (packet_send))( (\S+)))|((on)( (packet_receive))( (\S+)))|((on)( (sound_play))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((on)( (mouse_wheel_up)))|((on)( (mouse_wheel_down)))|((on)( (mouse_wheel))))
```

### Raw Documentation
```yml
# on right_click
# on left_click
# on middle_click
# on mouse_click <int>
# on right_release
# on left_release
# on middle_release
# on mouse_release <int>
# on place_block
# on break_block
# on punch_block
# on interact_block
# on pre_tick <int>?
# on tick <int>?
# on post_tick <int>?
# on item_use
# on item_consume
# on totem_pop
# on module_enable
# on module_disable
# on move_pos
# on move_look
# on key_press ...
# on key_release ...
# on damage
# on respawn
# on death
# on game_join
# on game_leave
# on chat_send "..."
# on chat_receive "..."
# on packet_send ...
# on packet_receive ...
# on sound_play <identifier>
# on mouse_wheel_up
# on mouse_wheel_down
# on mouse_wheel
```
