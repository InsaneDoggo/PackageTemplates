---
title: Groovy Script
lang: en
order: 6
---

[Groovy][1] Script allow you to modify:

* Filename.
* Directory's name.
* Value of [Global Varible][2]

Use right-click menu to add\edit\remove **script**.

![add_global_variable]({{ site.baseurl }}/images/tutorial/add_global_variable.png){: .imageFragment}

When element have script his icon 'G' is colored(green). Otherwise it will be black & white.

### Editing

Edit Dialog have input field with predefined method. All you need to do is implement this method.
You can test your code with input field and button **Try it**. Result will be shown below of the button.<br>

If code incorrect (throw exception) **result** will be converted to **GroovyScriptError**.

**Note:** You must use [Groovy][1] language in script.

![groovy_dialog]({{ site.baseurl }}/images/tutorial/groovy_dialog.png){: .imageFragment}

[1]: http://www.groovy-lang.org/
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_global_variables[page.lang] }}