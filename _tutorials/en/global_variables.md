---
title: Global Variables
lang: en
order: 5
---

**Global Variables** similar to [variables][1] in [File Templates][2].

#### Syntax
**Example<font class="variable">${VARIABLE_NAME}</font>File**

Use right-click menu to add\remove global variable.

![add_global_variable]({{ site.baseurl }}/images/tutorial/add_global_variable.png){: .imageFragment}

#### Where you can use it?
* In [File Templates][2] content.
* In directory's name.
* In filename.

#### Example
Suppose we have global variable **VARIABLE_NAME** with value **FooBar**,<br>
Then **Example<font class="variable">${VARIABLE_NAME}</font>File** will be converted to **ExampleFooBarFile**

#### Note
- Each PackageTemplate have default variable **BASE_NAME**, which can be used in other **global variables**<br>
e.g. variable **VARIABLE_NAME** with value **Foo<font class="variable">${BASE_NAME}</font>Bar**

<!-- - **File** Templates поддерживают вариант без скобок: **<font class="variable">$VARIABLE_NAME</font>**, однако в **Package**Templates скобки нужно указывать **всегда**. Это касается только диалога редактирования **Package Template**. В тексте **File Templates** по прежнему валидны оба варианта.  -->

[1]: {{ site.data.links.file_template_variables }}
[2]: {{ site.data.links.file_templates }}