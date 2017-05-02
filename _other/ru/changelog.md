---
title: Список изменений
lang: ru
layout: other
css: tutorials.css
---

### v0.4.1
- Фикс критического бага с CustomPath.

### v0.4.0

* Добавлены фичи для команды
<br> **a)** Поддержка Project шаблонов. ([Подробнее][4])
<br> **b)** [АвтоИмпорт][3]
* Добавлены опции для [Text Injection][1]:
<br> **a)** Start of File, End of line
<br> **b)** End of File, Start of line
* Добавлена опция **DIR_PARENT** для [CustomPath][5]. (Возвращает родительскую папку).
* Теперь можно **изменять** FileTemplate у элемента. Раньше нужно было удалять и выбирать нужный при создании.
* фиксы мелких багов.

### v0.3.0

* Добавлен [Text Injection][1]
* Немного переделаны [Global Variables][2]
* Правки в UI.


### v0.2.0

Изменений много, лучше перечитайте документацию целиком.<br>
Основные именения:

* Старые шаблоны больше не поддерживаются, придется создавать заново.
* Export\Import шаблонов.
* Custom Path.
* Write Rules.
* Правки в UI.


### v0.1.0

Первая версия.


[1]: {{ site.baseurl}}{{ site.data.links.tutorial_text_injection[page.lang] }}
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_global_variables[page.lang] }}
[3]: {{ site.baseurl}}{{ site.data.links.tutorial_impex[page.lang] }}#autoImport
[4]: {{ site.baseurl}}{{ site.data.links.tutorial_file_template_source[page.lang] }}
[5]: {{ site.baseurl}}{{ site.data.links.tutorial_custom_path[page.lang] }}