---
title: Scheme
lang: en
order: 2
---

First of all be familiar with [Per-project vs default scheme][1]. Plugin supports both schemes. You can change scheme in IDE settings:

![template_source_switch_scheme]({{ site.baseurl }}/images/tutorial/template_source_switch_scheme.png){: .imageFragment}

Similarly to **FileTemplates PackageTemplates** are stored in the following locations:

* The default (global) templates are stored in the IntelliJ IDEA home directory, in the folder **config / PackageTemplates**
* The per-project templates are stored in the **.idea / PackageTemplates** folder. These templates can be shared among the team members.

**Note:** Schemes have influence on [Import][2].

[1]: {{ site.data.links.file_templates_schemes }}
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_impex[page.lang] }}#schemes