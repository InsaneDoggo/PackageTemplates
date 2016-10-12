---
title: Глобальные переменные
lang: ru
order: 5
---

Глобальные переменные похожи на [переменные][1] в [File Templates][2].

#### Синтаксис
**Example<font class="variable">${VARIABLE_NAME}</font>File**

Чтобы добавить(удалить) глобальную переменную воспользуйтесь ПКМ.

![add_global_variable]({{ site.baseurl }}/images/tutorial/add_global_variable.png){: .imageFragment}

#### Где работает
* В тексте [File Templates][2]
* В названиях директорий
* В названиях файлов

#### Пример
Допустим у нас есть переменная **VARIABLE_NAME** со значением **FooBar**,<br>
Тогда **Example<font class="variable">${VARIABLE_NAME}</font>File** преобразуется в **ExampleFooBarFile**

#### Особенности
- В любом PackageTemplate шаблоне есть стандартная переменная **BASE_NAME**, которая может использоваться в пользовательских глобальных переменных.<br>
Например, переменная **VARIABLE_NAME** со значением **Foo<font class="variable">${BASE_NAME}</font>Bar**

<!-- - **File** Templates поддерживают вариант без скобок: **<font class="variable">$VARIABLE_NAME</font>**, однако в **Package**Templates скобки нужно указывать **всегда**. Это касается только диалога редактирования **Package Template**. В тексте **File Templates** по прежнему валидны оба варианта.  -->

[1]: {{ site.data.links.file_template_variables }}
[2]: {{ site.data.links.file_templates }}