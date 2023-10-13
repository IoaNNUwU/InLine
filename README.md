# InLine
[![Version](https://img.shields.io/jetbrains/plugin/v/21051-inline.svg?color=aa3030)](https://plugins.jetbrains.com/plugin/21051-inline)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/21051-inline.svg?color=aa3090)](https://plugins.jetbrains.com/plugin/21051-inline)
[![Rating](https://img.shields.io/jetbrains/plugin/r/rating/21051-inline?color=30aa30)](https://plugins.jetbrains.com/plugin/21051-inline)
<!-- Plugin description -->

# !! Deprecation warning !!
I'm not interested in developing `InLine` and using `IntelliJ IDEA` anymore since JetBrains dropped support for [Free Rust Plugin](https://plugins.jetbrains.com/plugin/8182--deprecated-rust) 
and started focusing on `new UI` that is IMO ugly. I don't like the direction this IDE is going and decided to switch away.

You should consider using alternative plugins:
* [Inspection Lens](https://plugins.jetbrains.com/plugin/19678-inspection-lens)
* [Inline Problems](https://plugins.jetbrains.com/plugin/20789-inlineproblems)

You can consider using alternative IDE:
* [VSCode](https://code.visualstudio.com/) - feature & plugin Rich IDE with not great UI (By Microsoft).
* [Lapce](https://lapce.dev/) - Middleground between VIM & VSCode with fast user-friendly UI similar to __old__ `IntelliJ IDEA` UI, modal editing and built-in LSP & Inline errors. Lacks certain features, __But is written in Rust__ so you can contribute.


---
### InLine is highly customizable plugin that shows errors and hints inline. 
#### Also supports gutter icons, colorful background and special effects.

* Errors are filtered on the line by priority
* Supports different fonts and languages
* Supports different hints styles __After Line, Rust Style__

<kbd>Settings</kbd> > <kbd>Appearance & Behaviour</kbd> > <kbd>⚙ InLine</kbd>

### Shortcuts:
* Hide/Show all errors __alt K__
* Change text style (After/Underline) __alt L__

### In plugin settings you can:
* Show or hide specific __level of errors__ - _error, warning, weak warning, information_
* Change __text colors & text visibility__ for each error level
* Change __text style__ to make it appear _after line end_ or _under line_
* Change __background colors__ & __background visibility__ for each error level
* Change __gutter icons visibility__ for each error level and select number of gutter icons
* Apply additional effect
* Change font of the hints
* __Ignore__ some errors by description

![](https://raw.githubusercontent.com/IoaNNUwU/InLine/main/media/example.png)

### Choices

There are already multiple plugins like this inspired by VSCode ErrorLens extension - check <kbd>Inspired by</kbd> section.
I've decided to make my own because I want to have some extra features for code __writing and demonstration__ purposes such as:
* Fill __background of whole line__
* Show __error icons__ in gutter area for different kinds of errors
* Turn __hint text off__ but make __background stay__

### Future

In the future I am planning to add more customization such as:
* New effects
* Error description changing
* Additional ways to filter errors

### Contribution

This plugin is open source. You can report bugs and contribute at [GitHub](https://github.com/IoaNNUwU/InLine).

<!-- Plugin description end -->
### Change notes:
<!-- Change notes -->
* __1.3.0__ - Add hide & change text type actions & bug fixes
* __1.2.0__ - Update to latest IntelliJ version (2023.2)
* __1.1.1__ - Update to latest IntelliJ version
* __1.1.0__ - Add __Rust Style Errors__ that are shown under line similar to __Rust compiler__ messages
* __1.0.1__ - Bug fixes & Font change support (Fix Chinese characters being shown as `?`)
* __1.0__ - Release
<!-- Change notes end -->
