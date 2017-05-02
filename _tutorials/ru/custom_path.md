---
title: Настраиваемый путь (Custom Path)
lang: ru
navigation: custom_path
---

Данная фича позволяет искать директории и файлы. Каждому элементу шаблона можно назначить **CustomPath**. Найденная директория будет родителем для элемента шаблона(Если **CustomPath** ищет файл, то элемент будет создан в той же директории, что и найденный файл).

**Примечание:** Алгоритм поиска работает с файловой системой, следовательно, ничего не знает про UI надстройки(Groups у AppCode или некоторые директории в Android Studio)

![custom_path_dialog]({{ site.baseurl }}/images/tutorial/custom_path_dialog.png){: .image}


[1]: {{ site.data.links.file_template_variables }}

1. Удалить action.
2. Тип поиска.
3. Текст для поиска (Имя файла\директории). Это поле поддерживает [Global Variables][2].
4. Глубина поиска.
5. Регулярные выражения RegExp. Если отмечено, то вместо Имени в 3-м пункте указывается Pattern(Используется реализация из стандартной библиотеки языка Java, [подробнее][1])
6. Добавить action.

### Глубина

* Глубина 0: среди соседей(в той же папке)
* Глубина 1: как 0 + внутри папок на 1 уровень
* Глубина n: вложенность до n
* Если снять галочку, то вложенность неограниченная.

![custom_path_deep]({{ site.baseurl }}/images/tutorial/custom_path_deep.png){: .imageFragment}

### Стартовая директория

Поиск начинается из директории, в которой был бы создан файл без использования **CustomPath**.

### Цепочный поиск

Каждый action после первого стартует из результата предыдущего. Если предыдущий ничего не нашел, что вся цепочка считается неудачной(Элемент шаблона не будет создан).

### Пример

Данный CustomPath успешно найдет директорию **src**, а затем **destination** в которой и будет создан элемент.

![custom_path_example]({{ site.baseurl }}/images/tutorial/custom_path_example.png){: .imageFragment}

**Результат:**

![custom_path_example_result]({{ site.baseurl }}/images/tutorial/custom_path_example_result.png){: .imageFragment}

[1]: {{ site.data.links.regexp_docs }}
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_global_variables[page.lang] }}
