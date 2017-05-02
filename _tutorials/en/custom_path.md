---
title: Custom Path
lang: en
navigation: custom_path
---

This feature allows you to search for directories and files. Each element of the template can use **CustomPath**. The found directory will be the parent for the template element (If **CustomPath** searches for the file then the item will be created in the same directory with file).

**Note:** Search algorithm works with file system, so it know nothing about IDE's UI features i.e. Groups in AppCode or some dirs in Android Studio.

![custom_path_dialog]({{ site.baseurl }}/images/tutorial/custom_path_dialog.png){: .image}


[1]: {{ site.data.links.file_template_variables }}

1. Delete action.
2. Search type.
3. Text for search (File\dir name). This input field support [Global Variables][2].
4. Deep limit.
5. RegExp. Select checkbox to use pattern with input field (Plugin uses default impl from standart JDK (Java), [details][1])
6. Add action.

### Deep limit

* Deep 0: among neighbors(in the same dir)
* Deep 1: like **0** + inside 1st level dirs.
* Deep n: nesting up to **n**
* Unlimited when checkbox unchecked.

![custom_path_deep]({{ site.baseurl }}/images/tutorial/custom_path_deep.png){: .imageFragment}

### Start point

Search starts from dirs, which should be used when you create item without **CustomPath**.

### Chain search

Each action after the first starts from the result of the previous one. If the previous one did not find anything, the whole chain is considered unsuccessful (template's element will not be created).

### Example

This CustomPath will find dir **src**, then **destination** and create file in the last one.

![custom_path_example]({{ site.baseurl }}/images/tutorial/custom_path_example.png){: .imageFragment}

**Result:**

![custom_path_example_result]({{ site.baseurl }}/images/tutorial/custom_path_example_result.png){: .imageFragment}

[1]: {{ site.data.links.regexp_docs }}
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_global_variables[page.lang] }}