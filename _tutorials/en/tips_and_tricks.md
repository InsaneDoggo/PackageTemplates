---
title: Tips and Tricks
lang: en
order: 9
---

# todo translate to eng ^_^

### Unique names for File Templates
At this moment import **File Templates** in the IDEA-based IDE implemented with **silent replace**. When you import settings with ** File -> Import Setting .. ** **File Templates** with same name will be replaced without any notification. we recommend you to use unique names to avoid replacement.

You can use prefixes like:

- MVC_Model (ofc you can use other style: **mvc**Model or **Mvc**Model)
- MVP_Model

### Execution Order
To clarify how plugin works you must be familiar with operations that executes when you use **Package Template**:

1. Execute [Groovy script][2] of BASE_NAME global variable.
2. Replace <font class="variable">${BASE_NAME}</font> value in other [global variables][3].
3. Execute **Groovy scripts** of other **global variables**.
4. Replace **global variables** in **directory** and **file** names.
5. Execute Groovy scripts of **directories** and **files**.
6. Add **global variables** to array of [variables in File Template][1]. 
7. Create directoies and files.

### Backward Compatibility
Backward compatibility guaranteed after release 1.0.0+ version. Don't make a lot of templates until plugin release. Any update under 1.0.0 can broke all your **Package Templates**.

**Note:** Feel free to make a lot of [File Templates][4], it supported by IDE ([JetBrains][5]).

### Undo
**Undo** Ctrl + Z (Command ⌘ + Z for *mac*) doesn't work for whole template, it work for each element(files and directories). E.g. if you want to undo **Template** that consist of 1 directory and 2 files then you need to use **undo** 3 times ^_^.

**Note:** In most cases better way for undo is **select** created root directory and press **delete**.


[1]: {{ site.data.links.file_template_variables }}
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_groovy_script[page.lang] }}
[3]: {{ site.baseurl}}{{ site.data.links.tutorial_global_variables[page.lang] }}
[4]: {{ site.data.links.file_templates }}
[5]: {{ site.data.links.jetbrains }}