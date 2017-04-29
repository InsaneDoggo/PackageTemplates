---
title: Создание Шаблона
lang: ru
order: 3
---

![new_package_template_dialog]({{ site.baseurl }}/images/tutorial/new_package_template_dialog.png){: .image}

1. Зарегистрировать как *Action*. Позволяет настроить [сочетание клавиш][4].
2. Пропускать диалог с [преднастройками][3]. Будут использованы имена файлов и папок по умолчанию(указанные при создании\редактировании).
3. Не создавать корневую директорию. Для случаев, когда нужны только файлы.
4. Показывать отчет после успешного выполнения шаблона.
5. [FileTemplate Source][8].
6. [Глобальные переменные][1].
7. Структура шаблона. Добавлять\удалять элементы можно с помощью ПКМ.<br>
**A.** Вкл\Выкл элемент, в момент исполнения шаблона будут созданы только отмеченные элементы.<br>
**B.** [Script][5]<br>
**C.** [Custom Path][6]<br>
**D.** [Write Rules][2]<br>
8. Создать [Text Injection][7]

### Добавление элемента

![add_element]({{ site.baseurl }}/images/tutorial/add_element.png){: .imageFragment}

При добавлении файла появится диалог со списком File Templates, которые создаются в стандартных настройках. Флажки сверху - это фильтры.

![select_file_template]({{ site.baseurl }}/images/tutorial/select_file_template.png){: .imageFragment}

### Пример шаблона
Чтобы лучше понять содержимое примера следует ознакомиться с остальными статьями.

![new_package_template_dialog_full]({{ site.baseurl }}/images/tutorial/new_package_template_dialog_full.png){: .image}


[1]: {{ site.baseurl}}{{ site.data.links.tutorial_global_variables[page.lang] }}
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_write_rules[page.lang] }}
[3]: {{ site.baseurl}}{{ site.data.links.tutorial_presettings[page.lang] }}
[4]: {{ site.baseurl}}{{ site.data.links.tutorial_shortcuts[page.lang] }}
[5]: {{ site.baseurl}}{{ site.data.links.tutorial_script[page.lang] }}
[6]: {{ site.baseurl}}{{ site.data.links.tutorial_custom_path[page.lang] }}
[7]: {{ site.baseurl}}{{ site.data.links.tutorial_text_injection[page.lang] }}
[8]: {{ site.baseurl}}{{ site.data.links.tutorial_file_template_source[page.lang] }}