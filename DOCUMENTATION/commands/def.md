[<-- Back to Legend](../legend.md)

# Command Name: Def
Keyword: def

### Usages
```
def module ...
def description "..."
def desc "..."
def function ... {}
def func ... {}
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
