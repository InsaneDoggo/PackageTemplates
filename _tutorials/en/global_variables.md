---
title: Global Variables
lang: en
navigation: global_variables
---

**Global Variables** similar to [variables][1] in [File Templates][2].

### Syntax
**Example<font class="variable">${VARIABLE_NAME}</font>File**

Use right-click menu to add\remove global variable.

![add_global_variable]({{ site.baseurl }}/images/tutorial/add_global_variable.png){: .imageFragment}

### Example
Suppose we have global variable **VARIABLE_NAME** with value **FooBar**,<br>
Then **Example<font class="variable">${VARIABLE_NAME}</font>File** will be converted to **ExampleFooBarFile**

### Where you can use it?
* In [File Templates][2] content.
* In directory's name.
* In filename.
* In global vars which defined below.

![global_vars_consistently]({{ site.baseurl }}/images/tutorial/global_vars_consistently.png){: .imageFragment}

### Predefined Variables

Depends on IDE you can use different sets of predefined variables. You can view them using the appropriate button (see picture above). They work in all global variables + in the same place as the global variables. An example from Intellij Idea CE (RU localization):

![global_vars_predefined]({{ site.baseurl }}/images/tutorial/global_vars_predefined.png){: .image}

**Note:** Dialog isn't modal (Allow you to switch between dialog and parent window)

* **CTX_FULL_PATH** - path to element where plugin is called from.
* **CTX_DIR_PATH** - like previous var, but contains path to directory.

Example, when plugin called from file:

* **CTX_FULL_PATH**  C:/foo/bar/Main.java
* **CTX_DIR_PATH**   C:/foo/bar

Example, when plugin called from dir:

* **CTX_FULL_PATH** C:/foo/bar
* **CTX_DIR_PATH** C:/foo/bar

**Note:** Path separators alsways is forward slash **/**.

[1]: {{ site.data.links.file_template_variables }}
[2]: {{ site.data.links.file_templates }}