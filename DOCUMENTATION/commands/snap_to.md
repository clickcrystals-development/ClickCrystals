[<-- Back to Legend](../legend.md)

# Command Name: Snap To
Keyword: snap_to

### Usages
```
snap_to any_block aim body then? {}?
snap_to any_block aim feet then? {}?
snap_to any_block aim head then? {}?
snap_to any_block aim legs then? {}?
snap_to any_block aim random then? {}?
snap_to any_block then? {}?
snap_to any_entity aim body then? {}?
snap_to any_entity aim feet then? {}?
snap_to any_entity aim head then? {}?
snap_to any_entity aim legs then? {}?
snap_to any_entity aim random then? {}?
snap_to any_entity then? {}?
snap_to nearest_block <identifier> aim body then? {}?
snap_to nearest_block <identifier> aim feet then? {}?
snap_to nearest_block <identifier> aim head then? {}?
snap_to nearest_block <identifier> aim legs then? {}?
snap_to nearest_block <identifier> aim random then? {}?
snap_to nearest_block <identifier> then? {}?
snap_to nearest_entity <identifier> aim body then? {}?
snap_to nearest_entity <identifier> aim feet then? {}?
snap_to nearest_entity <identifier> aim head then? {}?
snap_to nearest_entity <identifier> aim legs then? {}?
snap_to nearest_entity <identifier> aim random then? {}?
snap_to nearest_entity <identifier> then? {}?
snap_to polar <pitch> <yaw> aim body then? {}?
snap_to polar <pitch> <yaw> aim feet then? {}?
snap_to polar <pitch> <yaw> aim head then? {}?
snap_to polar <pitch> <yaw> aim legs then? {}?
snap_to polar <pitch> <yaw> aim random then? {}?
snap_to polar <pitch> <yaw> then? {}?
snap_to position <x> <y> <z> aim body then? {}?
snap_to position <x> <y> <z> aim feet then? {}?
snap_to position <x> <y> <z> aim head then? {}?
snap_to position <x> <y> <z> aim legs then? {}?
snap_to position <x> <y> <z> aim random then? {}?
snap_to position <x> <y> <z> then? {}?
snap_to target_entity aim body then? {}?
snap_to target_entity aim feet then? {}?
snap_to target_entity aim head then? {}?
snap_to target_entity aim legs then? {}?
snap_to target_entity aim random then? {}?
snap_to target_entity then? {}?
```

### Regex
```regexp
(((snap_to)( (nearest_entity|nearest_block))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (then?))?)|((snap_to)( (any_entity|target_entity|any_block))( (then?))?)|((snap_to)( (position))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (then?))?)|((snap_to)( (polar))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (then?))?)|((snap_to)( (nearest_entity|nearest_block))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (aim))( (head|body|legs|feet|random))( (then?))?)|((snap_to)( (any_entity|target_entity|any_block))( (aim))( (head|body|legs|feet|random))( (then?))?)|((snap_to)( (position))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (aim))( (head|body|legs|feet|random))( (then?))?)|((snap_to)( (polar))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (aim))( (head|body|legs|feet|random))( (then?))?))
```

### Raw Documentation
```yml
# snap_to (nearest_entity|nearest_block) <identifier> then? {}?
# snap_to (any_entity|target_entity|any_block) then? {}?
# snap_to position <x> <y> <z> then? {}?
# snap_to polar <pitch> <yaw> then? {}?
# snap_to (nearest_entity|nearest_block) <identifier> aim <aim-anchor> then? {}?
# snap_to (any_entity|target_entity|any_block) aim <aim-anchor> then? {}?
# snap_to position <x> <y> <z> aim <aim-anchor> then? {}?
# snap_to polar <pitch> <yaw> aim <aim-anchor> then? {}?
```
