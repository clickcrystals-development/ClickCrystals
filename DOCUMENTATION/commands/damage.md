[<-- Back to Legend](../legend.md)

# Command Name: Damage
Keyword: damage

### Usages
```
damage any_block
damage any_entity
damage nearest_block <identifier>
damage nearest_entity <identifier>
damage position <x> <y> <z>
damage target_entity
```

### Regex
```regexp
(((damage)( (nearest_entity|nearest_block))( !?(([#:](\w+)(\[(.*)\])?,?)+)))|((damage)( (any_entity|target_entity|any_block)))|((damage)( (position))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))( ([\^~]?-?\d*(\.\d*)?))))
```

### Raw Documentation
```yml
# damage (nearest_entity|nearest_block) <identifier>
# damage (any_entity|target_entity|any_block)
# damage position <x> <y> <z>
```
