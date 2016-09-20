---
title: Глобальные переменные
lang: ru
---

Глобальные переменные похожи на [переменные][1] в [File Templates][2].

#### Синтаксис
**Example<font class="variable">${VARIABLE_NAME}</font>File**

#### Где работает
* В тексте [File Templates][2]
* В названиях директорий
* В названиях файлов

#### Пример
Допустим у нас есть переменная **VARIABLE_NAME** со значением **FooBar**,<br>
Тогда **Example<font class="variable">${VARIABLE_NAME}</font>File** преобразуется в **ExampleFooBarFile**

#### Особенности
В любом PackageTemplate шаблоне есть стандартная переменная **BASE_NAME**, которая может использоваться в пользовательских глобальных переменных.<br>
Например, переменная **VARIABLE_NAME** со значением **Foo<font class="variable">${BASE_NAME}</font>Bar**

[1]: {{ site.data.links.file_template_variables }}
[2]: {{ site.data.links.file_templates }}