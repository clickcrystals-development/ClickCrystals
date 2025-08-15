[<-- Back to Legend](../legend.md)

# Command Name: As
Keyword: as

### Usages
```
as any_entity {}
as target_entity {}
as client {}
as nearest_entity <identifier> {}
```

### Regex
```regexp
(((as)( (any_entity|target_entity|client)))|((as)( (nearest_entity))( !?(([#:](\w+)(\[(.*)\])?,?)+))))
```

### Raw Documentation
```yml
# as (any_entity|target_entity|client) {}
# as nearest_entity <identifier> {}
```
