[<-- Back to Legend](../legend.md)

# Command Name: Damage
Keyword: damage

### Usages
```
damage nearest_entity <identifier>
damage nearest_block <identifier>
damage any_entity
damage target_entity
damage any_block
damage position <x> <y> <z>
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
