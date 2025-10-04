[<-- Back to Legend](../legend.md)

# Command Name: Turn To
Keyword: turn_to

### Usages
```
turn_to any_block aim body then? {}?
turn_to any_block aim feet then? {}?
turn_to any_block aim head then? {}?
turn_to any_block aim legs then? {}?
turn_to any_block aim random then? {}?
turn_to any_block speed <num> then? {}?
turn_to any_block then? {}?
turn_to any_entity aim body then? {}?
turn_to any_entity aim feet then? {}?
turn_to any_entity aim head then? {}?
turn_to any_entity aim legs then? {}?
turn_to any_entity aim random then? {}?
turn_to any_entity speed <num> then? {}?
turn_to any_entity then? {}?
turn_to nearest_block <identifier> aim body then? {}?
turn_to nearest_block <identifier> aim feet then? {}?
turn_to nearest_block <identifier> aim head then? {}?
turn_to nearest_block <identifier> aim legs then? {}?
turn_to nearest_block <identifier> aim random then? {}?
turn_to nearest_block <identifier> speed <num> then? {}?
turn_to nearest_block <identifier> then? {}?
turn_to nearest_entity <identifier> aim body then? {}?
turn_to nearest_entity <identifier> aim feet then? {}?
turn_to nearest_entity <identifier> aim head then? {}?
turn_to nearest_entity <identifier> aim legs then? {}?
turn_to nearest_entity <identifier> aim random then? {}?
turn_to nearest_entity <identifier> speed <num> then? {}?
turn_to nearest_entity <identifier> then? {}?
turn_to polar <pitch> <yaw> aim body then? {}?
turn_to polar <pitch> <yaw> aim feet then? {}?
turn_to polar <pitch> <yaw> aim head then? {}?
turn_to polar <pitch> <yaw> aim legs then? {}?
turn_to polar <pitch> <yaw> aim random then? {}?
turn_to polar <pitch> <yaw> speed <num> then? {}?
turn_to polar <pitch> <yaw> then? {}?
turn_to position <x> <y> <z> aim body then? {}?
turn_to position <x> <y> <z> aim feet then? {}?
turn_to position <x> <y> <z> aim head then? {}?
turn_to position <x> <y> <z> aim legs then? {}?
turn_to position <x> <y> <z> aim random then? {}?
turn_to position <x> <y> <z> speed <num> then? {}?
turn_to position <x> <y> <z> then? {}?
turn_to target_entity aim body then? {}?
turn_to target_entity aim feet then? {}?
turn_to target_entity aim head then? {}?
turn_to target_entity aim legs then? {}?
turn_to target_entity aim random then? {}?
turn_to target_entity speed <num> then? {}?
turn_to target_entity then? {}?
```

### Regex
```regexp
(((turn_to)( (nearest_entity|nearest_block))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (then?))?)|((turn_to)( (any_entity|target_entity|any_block))( (then?))?)|((turn_to)( (position))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (then?))?)|((turn_to)( (polar))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (then?))?)|((turn_to)( (nearest_entity|nearest_block))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (aim))( (head|body|legs|feet|random))( (then?))?)|((turn_to)( (any_entity|target_entity|any_block))( (aim))( (head|body|legs|feet|random))( (then?))?)|((turn_to)( (position))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (aim))( (head|body|legs|feet|random))( (then?))?)|((turn_to)( (polar))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (aim))( (head|body|legs|feet|random))( (then?))?)|((turn_to)( (nearest_entity|nearest_block))( !?(([#:](\w+)(\[(.*)\])?,?)+))( (speed))( (-?\d*(\.\d*)?))( (then?))?)|((turn_to)( (any_entity|target_entity|any_block))( (speed))( (-?\d*(\.\d*)?))( (then?))?)|((turn_to)( (position))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (speed))( (-?\d*(\.\d*)?))( (then?))?)|((turn_to)( (polar))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( (speed))( (-?\d*(\.\d*)?))( (then?))?))
```

### Raw Documentation
```yml
# turn_to (nearest_entity|nearest_block) <identifier> then? {}?
# turn_to (any_entity|target_entity|any_block) then? {}?
# turn_to position <x> <y> <z> then? {}?
# turn_to polar <pitch> <yaw> then? {}?
# turn_to (nearest_entity|nearest_block) <identifier> aim <aim-anchor> then? {}?
# turn_to (any_entity|target_entity|any_block) aim <aim-anchor> then? {}?
# turn_to position <x> <y> <z> aim <aim-anchor> then? {}?
# turn_to polar <pitch> <yaw> aim <aim-anchor> then? {}?
# turn_to (nearest_entity|nearest_block) <identifier> speed <num> then? {}?
# turn_to (any_entity|target_entity|any_block) speed <num> then? {}?
# turn_to position <x> <y> <z> speed <num> then? {}?
# turn_to polar <pitch> <yaw> speed <num> then? {}?
```
