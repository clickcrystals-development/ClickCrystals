[<-- Back to Legend](../legend.md)

# Command Name: Def
Keyword: def

### Usages
```
def desc "..."
def description "..."
def func ... {}
def function ... {}
def module ...
```

### Regex
```regexp
(((def)( (module))( (\S+)))|((def)( (description|desc))( (\".*?\")))|((def)( (function|func))( (\S+))))
```

### Raw Documentation
```yml
# def module ...
# def (description|desc) "..."
# def (function|func) ... {}
```
