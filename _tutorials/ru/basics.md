---
title: Основы
lang: ru
---

Чтобы открыть **PackageTemplates** воспользуйтесь разделом **new** контекстного меню. Так же можно настроить shortcut в стандартных настройках.

![context_menu]({{ site.baseurl }}/images/tutorial/context_menu.png){: .imageFragment}


Шаблоны можно создавать, удалять и редактировать.

![select_package_template]({{ site.baseurl }}/images/tutorial/select_package_template.png){: .image}

Чтобы воспользоваться шаблоном выбрерите его из списка и нажмите **OK**

### Создание шаблона
Cнизу изображен диалог создания шаблона.

![new_package_template_dialog]({{ site.baseurl }}/images/tutorial/new_package_template_dialog.png){: .image}

1. Зарегистрировать как *Action*. Позволяет привязать *shortcut*.
2. Пропускать диалог перед использованием. Будут использованы имена файлов и папок по умолчанию(указанные при создании\редактировании).
3. Не создавать корневую директорию. Для случаев, когда нужны только файлы.
4. Глобальные переменные. [Подробнее][1].
5. Структура шаблона. Добавлять\удалять элементы можно с помощью ПКМ.<br><br>
![add_element]({{ site.baseurl }}/images/tutorial/add_element.png){: .imageFragment}

### Пример шаблона
Чтобы лучше понять содержимое примера следует ознакомиться с [Глобальными переменными][1] и [Groovy scripts][2].

![new_package_template_dialog_full]({{ site.baseurl }}/images/tutorial/new_package_template_dialog_full.png){: .image}


[1]: {{site.baseurl}}{{ site.data.links.tutorial_global_variables_ru }}
[2]: {{site.baseurl}}{{ site.data.links.tutorial_groovy_script_ru }}