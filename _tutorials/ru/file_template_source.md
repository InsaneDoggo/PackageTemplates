---
title: Источник FileTemplate
lang: ru
order: 2
---

У вас могут быть шаблоны с одинаковыми именами, но в разных [схемах][1]. **Источник FileTemplate** определяет, где искать шаблоны.

![file_template_source_dropdown]({{ site.baseurl }}/images/tutorial/file_template_source_dropdown.png){: .imageFragment}

* DEFAULT_ONLY - только в **default** папке.
* PROJECT_ONLY - только в **project** папке.
* PROJECT_PRIORITY - сначала в **project**, если не найдет, тогда в **default**.
* DEFAULT_PRIORITY - сначала в **default**, если не найдет, тогда в **project**.

**Примечание:** все режимы кроме DEFAULT_ONLY требуют переключения на схему "Project" для корректной работы. В противном случае вы увидете диалог с предупрежденем.

[1]: {{ site.baseurl}}{{ site.data.links.tutorial_scheme[page.lang] }}