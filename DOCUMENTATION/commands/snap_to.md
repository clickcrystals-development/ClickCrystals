[<-- Back to Legend](../legend.md)

# Command Name: Snap To
Keyword: snap_to

### Usages
```
snap_to nearest_entity <identifier> then? {}?
snap_to nearest_block <identifier> then? {}?
snap_to any_entity then? {}?
snap_to target_entity then? {}?
snap_to any_block then? {}?
snap_to position <x> <y> <z> then? {}?
snap_to polar <pitch> <yaw> then? {}?
```

### Regex
```regexp
(((snap_to)( (nearest_entity|nearest_block))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (then?))?)|((snap_to)( (any_entity|target_entity|any_block))( (then?))?)|((snap_to)( (position))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (then?))?)|((snap_to)( (polar))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (then?))?))
```

### Raw Documentation
```yml
# snap_to (nearest_entity|nearest_block) <identifier> then? {}?
# snap_to (any_entity|target_entity|any_block) then? {}?
# snap_to position <x> <y> <z> then? {}?
# snap_to polar <pitch> <yaw> then? {}?
```
