---
title: AppCode Groups
lang: ru
layout: other
css: tutorials.css
---

**Groups** - фича из Appcode(XCode). Плагин не умеет работать с **Groups** и будет создавать обычные директории.
В данный момент временным решением является создание группы вручную(из существующих файлов\директорий).
Для этого в Project View нужно выбрать режим **Files**, чтобы были видны созданные файлы, а затем добавить их через контекстное меню(ПКМ на директории).

P.S. Если шаблон состоит только из файлов(FileTemplates), то файлы добавятся в **Groups** без проблем.

![appcode_add_to_group_dialog]({{ site.baseurl }}/images/tutorial/appcode_add_to_group_dialog.png){: .imageFragment}

![appcode_project_view]({{ site.baseurl }}/images/tutorial/appcode_project_view.png){: .imageFragment}

[1]: {{ site.data.links.file_template_variables }}
