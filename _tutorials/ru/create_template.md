---
title: Создание Шаблона
lang: ru
order: 3
---

![new_package_template_dialog]({{ site.baseurl }}/images/tutorial/new_package_template_dialog.png){: .image}

1. Зарегистрировать как *Action*. Позволяет настроить [сочетание клавиш][4].
2. Пропускать диалог перед использованием. Будут использованы имена файлов и папок по умолчанию(указанные при создании\редактировании).
3. Не создавать корневую директорию. Для случаев, когда нужны только файлы.
4. [Глобальные переменные][1].
5. Структура шаблона. Добавлять\удалять элементы можно с помощью ПКМ. Флажок вначале элемента отвечает за его создание.

![add_element]({{ site.baseurl }}/images/tutorial/add_element.png){: .imageFragment}

При добавлении файла появится диалог со списком File Templates, которые создаются в стандартных настройках. Флажки сверху - это фильтры.

![select_file_template]({{ site.baseurl }}/images/tutorial/select_file_template.png){: .imageFragment}

### Пример шаблона
Чтобы лучше понять содержимое примера следует ознакомиться с [Глобальными переменными][1] и [Groovy scripts][2].

![new_package_template_dialog_full]({{ site.baseurl }}/images/tutorial/new_package_template_dialog_full.png){: .image}


[1]: {{ site.baseurl}}{{ site.data.links.tutorial_global_variables[page.lang] }}
[2]: {{ site.baseurl}}{{ site.data.links.tutorial_groovy_script[page.lang] }}
[3]: {{ site.baseurl}}{{ site.data.links.tutorial_presettings[page.lang] }}
[4]: {{ site.baseurl}}{{ site.data.links.tutorial_shortcuts[page.lang] }}