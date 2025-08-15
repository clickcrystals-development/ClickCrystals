[<-- Back to Legend](../legend.md)

# Command Name: Define
Keyword: define

### Usages
```
define module ...
define description "..."
define desc "..."
define function ... {}
define func ... {}
```

### Regex
```regexp
(((define)( (module))( (\S+)))|((define)( (description|desc))( (\".*?\")))|((define)( (function|func))( (\S+))))
```

### Raw Documentation
```yml
# define module ...
# define (description|desc) "..."
# define (function|func) ... {}
```
