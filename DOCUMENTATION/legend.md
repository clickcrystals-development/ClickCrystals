# Documentation Legend
Here you will find out how to read this documentation, and what each symbol means.

### Legend Table

| Symbol            | Meaning                                     | Aliases                             | Example           |
|-------------------|---------------------------------------------|-------------------------------------|-------------------|
| \<int\>           | integer                                     |                                     | 123               |
| \<num\>           | number                                      |                                     | 1.23              |
| \<vec\>           | singular relative vector component          | \<x\>,\<y\>,\<z\>,\<pitch\>,\<yaw\> | ~1.23             |
| \<comparator\>    | \> < == >= <= !=                            |                                     | >=                |
| \<identifier\>    | :direct_identifier OR #indirect_identifier  |                                     | :diamond_sword    |
| \<server-packet\> | [server packet](./network_packets.md)       |                                     | playerList        |
| \<client-packet\> | [client packet](./network_packets.md)       |                                     | handSwing         |
| \<input\>         | an input type                               |                                     | attack            |
| ...               | literal                                     |                                     | abc               |
| "..."             | quoted literal                              |                                     | "a b c"           |
| \w+               | constant literal                            |                                     |                   |
| (\w+\|\w+\|...)   | constant literals                           |                                     |                   |
| {}                | command line or code block of command lines |                                     | say "Hello World" |

### Optional Argument Symbols
The argument is optional if a ? is appended at the end. Any argument symbol followed by a question mark
will render said argument optional, meaning the script interpreter will not throw an error if it was absent.

### What is a Command Line?
A script command line is any instruction that'll tell your Minecraft client what to do.
For example, a command line that tells your Minecraft client to say Hello World in chat would look like:

```
say "Hello World"
```

Here are some examples of command lines:
- [As (as)](./commands/as.md)
- [Cancel Packet (cancel_packet)](./commands/cancel_packet.md)
- [Chat (chat)](./commands/chat.md)
- [Config (config)](./commands/config.md)
- [Damage (damage)](./commands/damage.md)
- [Def (def)](./commands/def.md)
- [Define (define)](./commands/define.md)
- [Desc (desc)](./commands/desc.md)
- [Description (description)](./commands/description.md)
- [Drop (drop)](./commands/drop.md)
- [Execute (execute)](./commands/execute.md)
- [Execute Period (execute_period)](./commands/execute_period.md)
- [Execute Random (execute_random)](./commands/execute_random.md)
- [Exit (exit)](./commands/exit.md)
- [Func (func)](./commands/func.md)
- [Function (function)](./commands/function.md)
- [Gui Drop (gui_drop)](./commands/gui_drop.md)
- [Gui Quickmove (gui_quickmove)](./commands/gui_quickmove.md)
- [Gui Swap (gui_swap)](./commands/gui_swap.md)
- [Gui Switch (gui_switch)](./commands/gui_switch.md)
- [Hold Input (hold_input)](./commands/hold_input.md)
- [If (if)](./commands/if.md)
- [If Not (if_not)](./commands/if_not.md)
- [Input (input)](./commands/input.md)
- [Interact (interact)](./commands/interact.md)
- [Loop (loop)](./commands/loop.md)
- [Loop Period (loop_period)](./commands/loop_period.md)
- [Module (module)](./commands/module.md)
- [Notify (notify)](./commands/notify.md)
- [On (on)](./commands/on.md)
- [Playsound (playsound)](./commands/playsound.md)
- [Print (print)](./commands/print.md)
- [Repeat Period (repeat_period)](./commands/repeat_period.md)
- [Say (say)](./commands/say.md)
- [Send (send)](./commands/send.md)
- [Snap To (snap_to)](./commands/snap_to.md)
- [Swap (swap)](./commands/swap.md)
- [Switch (switch)](./commands/switch.md)
- [Teleport (teleport)](./commands/teleport.md)
- [Throw (throw)](./commands/throw.md)
- [Turn To (turn_to)](./commands/turn_to.md)
- [Uncancel Packet (uncancel_packet)](./commands/uncancel_packet.md)
- [Velocity (velocity)](./commands/velocity.md)
- [Wait (wait)](./commands/wait.md)
- [Wait Random (wait_random)](./commands/wait_random.md)
- [While (while)](./commands/while.md)
- [While Not (while_not)](./commands/while_not.md)

### What is a Code Block?
Normally, we would have 1 command line per, let's say, an if statement.

```
if holding :diamond say "I am holding a diamond"
```

The problem emerges when we try to execute more than 1 command line per if statement.
In theory, we could just spam new lines like so:

```
if holding :diamond say "I am holding a diamond"
if holding :diamond say "Hello World"
if holding :diamond input jump
```

But this practice is redundant and may be seen as inefficient to our scripters.
To solve this problem, you can have all of your command lines stored in between brackets,
essentially telling the script interpreter that you want to execute all of these lines. Do note that
each time a new pair of nest brackets appear, it is conventional to increase your indentation by 1.

```
if holding :diamond {
    say "I am holding a diamond"
    say "Hello World"
    input jump
}
```

Nested indentation example:
```
if holding :diamond {
    say "I am holding a diamond"
    say "Hello World"
    input jump

    if off_holding :diamond {
        say "My off hand is also holding a diamond"
    }
}
```

Happy coding and cpvping!
