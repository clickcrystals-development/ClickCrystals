[<-- Back to Legend](../legend.md)

# Command Name: Define
Keyword: define

### Usages
```
define desc "..."
define description "..."
define func ... {}
define function ... {}
define module ...
```

### Regex
```regexp
(((define)( (module))( (\S+)))|((define)( (description|desc))( (\"((\\\")|[^\"])*\")))|((define)( (function|func))( (\S+))))
```

### Raw Documentation
```yml
# define module ...
# define (description|desc) "..."
# define (function|func) ... {}
```
