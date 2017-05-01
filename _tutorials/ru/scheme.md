---
title: Схема
lang: ru
navigation: scheme
---

Прежде всего нужно ознакомиться с [Per-project vs default scheme][1]. Плагин поддерживает обе схемы. Схема изменяется в настройках IDE:

![template_source_switch_scheme]({{ site.baseurl }}/images/tutorial/template_source_switch_scheme.png){: .imageFragment}

**PackageTemplates** по аналогии с **FileTemplates** хранятся в следующих папках:

* Default (global) шаблоны хранятся в стандартной папке с настройками IntelliJ IDEA, in the folder **config / PackageTemplates**
* Per-project шаблоны хранятся в папке **.idea / PackageTemplates**.

**Примечание:** Схемы влияют на [импорт][2].

[1]: {{ site.data.links.file_templates_schemes }}
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_impex[page.lang] }}#schemes