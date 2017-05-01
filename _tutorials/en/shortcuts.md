---
title: Shortcuts
lang: en
navigation: shortcuts
---

Be familiar with tutorials from **jetbrains** listed below:<br>

* [Keymap; Actions][2]
* [Shortcuts; Searching for actions][1]

IDE allow you to bind shortcuts to **Action**. Plugin uses **Action** for start **Main Dialog**. Also you can register **Action** for each **Package Template** by mark corresponding checkbox when [edit][4] template.

U can use search in IDE settings dialog. Action of plugin's **Home Dialog** registred as **"{{ site.data.const.main_action_name }}"**. Actions of PackageTemplates uses corresponding names.

### Action's Context
Where template will be created? For successful use of shortcuts **focus** should be on:

1. **File** or **Directory** in **Project View**. Template will be created within selected directory or in the directory where selected file located is.

![context_project_view]({{ site.baseurl }}/images/tutorial/context_project_view.png){: .imageFragment}

2. **File Editor**. Template will be created in the file's parent directory.

![context_file_editor]({{ site.baseurl }}/images/tutorial/context_file_editor.png){: .imageFragment}

**Note:** Template's **Actions** registers on IDE startup. You should restart IDE to apply changes when **Action's checkbox** was modified.


[1]: {{ site.data.links.shortcuts }}
[2]: {{ site.data.links.shortcuts_actions }}
[3]: {{ site.data.links.jetbrains }}
[4]: {{ site.baseurl}}{{ site.data.links.tutorial_create_template[page.lang] }}