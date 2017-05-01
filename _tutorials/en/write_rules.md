---
title: Write Rules
lang: en
navigation: write_rules
---

### Example
Conflict might occur when you try to use a template whose element names coincide with those already existing in the project (in the same folder).

### Resolving conflicts
Exists few ways:

* **Overwrite** remove existing.
* **Use Existing** Ignores creation of new elements. For directories, the creation operation is ignored, but all internal elements will be created in the existing directory.

### Implementation in plugin

![write_rules_dialog]({{ site.baseurl }}/images/tutorial/write_rules_dialog.png){: .image}

Dropdown have extra options:

1. **From Parent** - Rules of the parent (directory) will be inherited.

2. **Ask Me** - In case of conflicts, a dialog with available options will be displayed.

** Note:** **Undo action** Ctrl + Z (for *mac* Command ⌘ + Z) cancels all actions, but due to IDE specifics **command** might be devide when dialog appears during **comand** execution. **Ask me** uses the dialog. Simple conflicts are checked before the **command** starts, but some conflicts can not be checked previously, for example elements with **Custom Path**.
If you are not sure that **command** was split into several, you can undo it until **command** called as **Execute PackageTemlate**, as in the image below:

![undo_dialog]({{ site.baseurl }}/images/tutorial/undo_dialog.png){: .image}

[1]: {{ site.data.links.file_template_variables }}