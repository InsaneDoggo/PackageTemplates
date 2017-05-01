---
title: Tips and Tricks
lang: en
navigation: tips_and_tricks
---

### Unique names for File Templates
At this moment import **File Templates** in the IDEA-based IDE implemented with **silent replace**. When you import settings with ** File -> Import Setting .. ** **File Templates** with same name will be replaced without any notification. we recommend you to use unique names to avoid replacement.

You can use prefixes like:

- MVC_Model (ofc you can use other style: **mvc**Model or **Mvc**Model)
- MVP_Model

### Execution Order
To clarify how plugin works you must be familiar with operations that executes when you use **Package Template**:

1. Calculate values of [global variables][3].<br>
   A) Replace predefined or early defined global <font class="variable">${variable}</font>.<br>
   B) Execute [Script][2].<br>
4. Replace **global variables** in **directory** and **file** names.
5. Execute Groovy scripts of **directories** and **files**.
6. Add **global variables** to array of [variables in File Template][1].
7. Create directoies and files.

### Backward Compatibility
Backward compatibility guaranteed after release 1.0.0+ version. Don't make a lot of templates until plugin release. Any update under 1.0.0 can broke all your **Package Templates**.

**Note:** Feel free to make a lot of [File Templates][4], it supported by IDE ([JetBrains][5]).


[1]: {{ site.data.links.file_template_variables }}
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_groovy_script[page.lang] }}
[3]: {{ site.baseurl}}{{ site.data.links.tutorial_global_variables[page.lang] }}
[4]: {{ site.data.links.file_templates }}
[5]: {{ site.data.links.jetbrains }}