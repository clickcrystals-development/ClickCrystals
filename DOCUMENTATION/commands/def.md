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
(((\S+)( (module))( (\S+)))|((\S+)( (description|desc))( (\".*?\")))|((\S+)( (function|func))( (\S+))))
```

### Raw Documentation
```yml
# def module ...
# def (description|desc) "..."
# def (function|func) ... {}
```
