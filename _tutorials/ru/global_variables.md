---
title: Глобальные переменные
lang: ru
order: 5
---

Глобальные переменные похожи на [переменные][1] в [File Templates][2].

### Синтаксис
**Example<font class="variable">${VARIABLE_NAME}</font>File**

Чтобы добавить(удалить) глобальную переменную воспользуйтесь ПКМ.

![add_global_variable]({{ site.baseurl }}/images/tutorial/add_global_variable.png){: .imageFragment}

### Пример
Допустим у нас есть переменная **VARIABLE_NAME** со значением **FooBar**,<br>
Тогда **Example<font class="variable">${VARIABLE_NAME}</font>File** преобразуется в **ExampleFooBarFile**

### Где работает
* В тексте [File Templates][2]
* В названиях директорий
* В названиях файлов
* В глобальных переменных, которые объявлены ниже.

![global_vars_consistently]({{ site.baseurl }}/images/tutorial/global_vars_consistently.png){: .imageFragment}

### Набор стандартных переменных

В зависимости от IDE будут доступны различные наборы переменных. Просмотреть их можно с помощью соответсвующей кнопки(см. картинку выше). Они работают во всех глобальных переменных + там же, где и обычные глобальные переменные. Ниже пример из Intellij Idea CE:

![global_vars_predefined]({{ site.baseurl }}/images/tutorial/global_vars_predefined.png){: .image}

**Примечание:** Диалог не модальный (Можно переключаться между родительским окном и диалогом)

* **CTX_FULL_PATH** - путь к элементу на котором был вызван плагин.
* **CTX_DIR_PATH** - похожа на предыдущую переменную, но содержит путь к папке.

Пример, когда плагин вызван на файле:

* **CTX_FULL_PATH** C:/foo/bar/Main.java
* **CTX_DIR_PATH** C:/foo/bar

Пример, когда плагин вызван на папке:

* **CTX_FULL_PATH** C:/foo/bar
* **CTX_DIR_PATH** C:/foo/bar

**Примечание:** Path separators всегда forward slash **/**.

[1]: {{ site.data.links.file_template_variables }}
[2]: {{ site.data.links.file_templates }}