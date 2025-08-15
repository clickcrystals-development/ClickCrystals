[<-- Back to Legend](../legend.md)

# Command Name: Turn To
Keyword: turn_to

### Usages
```
turn_to nearest_entity <identifier> then? {}?
turn_to nearest_block <identifier> then? {}?
turn_to any_entity then? {}?
turn_to target_entity then? {}?
turn_to any_block then? {}?
turn_to position <x> <y> <z> then? {}?
turn_to polar <pitch> <yaw> then? {}?
```

### Regex
```regexp
(((turn_to)( (nearest_entity|nearest_block))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (then?))?)|((turn_to)( (any_entity|target_entity|any_block))( (then?))?)|((turn_to)( (position))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (then?))?)|((turn_to)( (polar))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (then?))?))
```

### Raw Documentation
```yml
# turn_to (nearest_entity|nearest_block) <identifier> then? {}?
# turn_to (any_entity|target_entity|any_block) then? {}?
# turn_to position <x> <y> <z> then? {}?
# turn_to polar <pitch> <yaw> then? {}?
```
