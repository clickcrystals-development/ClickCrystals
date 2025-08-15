[<-- Back to Legend](../legend.md)

# Command Name: Input
Keyword: input

### Usages
```
input attack <num>
input mouse_wheel_up <num>
input mouse_wheel_down <num>
input use <num>
input forward <num>
input backward <num>
input strafe_left <num>
input strafe_right <num>
input jump <num>
input sprint <num>
input sneak <num>
input lock_cursor <num>
input unlock_cursor <num>
input left <num>
input right <num>
input middle <num>
input inventory <num>
input key ...
```

### Regex
```regexp
(((input)( (attack|mouse_wheel_up|mouse_wheel_down|use|forward|backward|strafe_left|strafe_right|jump|sprint|sneak|lock_cursor|unlock_cursor|left|right|middle|inventory))( (-?\d*(\.\d*)?)))|((input)( (key))( (\S+))))
```

### Raw Documentation
```yml
# input (attack|mouse_wheel_up|mouse_wheel_down|use|forward|backward|strafe_left|strafe_right|jump|sprint|sneak|lock_cursor|unlock_cursor|left|right|middle|inventory) <num>
# input key ...
```
