---
title: Import & Export
lang: en
order: 11
---

### Import <a name="import"></a>
Specify path to .json file to import template and his FileTemplates(from dir which creates via export).

### Export <a name="export"></a>
Template exports to .json file + dir with dependent FileTemplate.

### AutoImport <a name="autoImport"></a>
For import template you should specify path to file. When you import regularly it takes a lot of time. AutoImport allow you to specify path to dir which will be scanned for **PackageTemplates** for import. Subdirs scans too, excluding reserved dirs called **FileTemplates**.

![plugin_settings_dialog]({{ site.baseurl }}/images/tutorial/plugin_settings_dialog.png){: .image}

* For all found templates, you can specify [Write Rules][2].
* To show SettingsDialog use corresponding icon in ActionBar of HomeDialog.
* To start updating(AutoImporting) use icon(green arrow) in ActionBar of HomeDialog.

**Note:** you can specify several paths for AutoImport.

### Use Case
Suppose that you work in team and want to share your templates. To do this, you need to share dir where you will export the templates. You can do it via third-party services such as [Dropbox][1]. After that, your colleagues should specify this folder for AutoImport and use **Update Action** after templates changes.

### Schemes <a name="schemes"></a>

* if current scheme is **Default**, then import works as usual.
* if current scheme is **Project**, then appears following dialog:

![where_to_save_dialog]({{ site.baseurl }}/images/tutorial/where_to_save_dialog.png){: .image}

Depends on your choice [FileTemplate Source][3] of PackageTemplates will be set to **DEFAULT_ONLY** or **PROJECT_ONLY**.

[1]: {{ site.data.links.dropbox }}
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_write_rules[page.lang] }}
[3]: {{ site.baseurl}}{{ site.data.links.tutorial_file_template_source[page.lang] }}